package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.IntegerKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseStrokeContent implements BaseKeyframeAnimation.AnimationListener, KeyPathElementContent, DrawingContent {
   private BaseKeyframeAnimation colorFilterAnimation;
   private final List dashPatternAnimations;
   private final BaseKeyframeAnimation dashPatternOffsetAnimation;
   private final float[] dashPatternValues;
   protected final BaseLayer layer;
   private final LottieDrawable lottieDrawable;
   private final BaseKeyframeAnimation opacityAnimation;
   final Paint paint = new LPaint(1);
   private final Path path = new Path();
   private final List pathGroups = new ArrayList();
   private final PathMeasure pm = new PathMeasure();
   private final RectF rect = new RectF();
   private final Path trimPathPath = new Path();
   private final BaseKeyframeAnimation widthAnimation;

   BaseStrokeContent(LottieDrawable var1, BaseLayer var2, Cap var3, Join var4, float var5, AnimatableIntegerValue var6, AnimatableFloatValue var7, List var8, AnimatableFloatValue var9) {
      this.lottieDrawable = var1;
      this.layer = var2;
      this.paint.setStyle(Style.STROKE);
      this.paint.setStrokeCap(var3);
      this.paint.setStrokeJoin(var4);
      this.paint.setStrokeMiter(var5);
      this.opacityAnimation = var6.createAnimation();
      this.widthAnimation = var7.createAnimation();
      if (var9 == null) {
         this.dashPatternOffsetAnimation = null;
      } else {
         this.dashPatternOffsetAnimation = var9.createAnimation();
      }

      this.dashPatternAnimations = new ArrayList(var8.size());
      this.dashPatternValues = new float[var8.size()];
      byte var10 = 0;

      int var11;
      for(var11 = 0; var11 < var8.size(); ++var11) {
         this.dashPatternAnimations.add(((AnimatableFloatValue)var8.get(var11)).createAnimation());
      }

      var2.addAnimation(this.opacityAnimation);
      var2.addAnimation(this.widthAnimation);

      for(var11 = 0; var11 < this.dashPatternAnimations.size(); ++var11) {
         var2.addAnimation((BaseKeyframeAnimation)this.dashPatternAnimations.get(var11));
      }

      BaseKeyframeAnimation var12 = this.dashPatternOffsetAnimation;
      if (var12 != null) {
         var2.addAnimation(var12);
      }

      this.opacityAnimation.addUpdateListener(this);
      this.widthAnimation.addUpdateListener(this);

      for(var11 = var10; var11 < var8.size(); ++var11) {
         ((BaseKeyframeAnimation)this.dashPatternAnimations.get(var11)).addUpdateListener(this);
      }

      var12 = this.dashPatternOffsetAnimation;
      if (var12 != null) {
         var12.addUpdateListener(this);
      }

   }

   private void applyDashPatternIfNeeded(Matrix var1) {
      L.beginSection("StrokeContent#applyDashPattern");
      if (this.dashPatternAnimations.isEmpty()) {
         L.endSection("StrokeContent#applyDashPattern");
      } else {
         float var2 = Utils.getScale(var1);

         for(int var3 = 0; var3 < this.dashPatternAnimations.size(); ++var3) {
            this.dashPatternValues[var3] = (Float)((BaseKeyframeAnimation)this.dashPatternAnimations.get(var3)).getValue();
            float[] var4;
            if (var3 % 2 == 0) {
               var4 = this.dashPatternValues;
               if (var4[var3] < 1.0F) {
                  var4[var3] = 1.0F;
               }
            } else {
               var4 = this.dashPatternValues;
               if (var4[var3] < 0.1F) {
                  var4[var3] = 0.1F;
               }
            }

            var4 = this.dashPatternValues;
            var4[var3] *= var2;
         }

         BaseKeyframeAnimation var5 = this.dashPatternOffsetAnimation;
         if (var5 == null) {
            var2 = 0.0F;
         } else {
            var2 = (Float)var5.getValue();
         }

         this.paint.setPathEffect(new DashPathEffect(this.dashPatternValues, var2));
         L.endSection("StrokeContent#applyDashPattern");
      }
   }

   private void applyTrimPath(Canvas var1, BaseStrokeContent.PathGroup var2, Matrix var3) {
      L.beginSection("StrokeContent#applyTrimPath");
      if (var2.trimPath == null) {
         L.endSection("StrokeContent#applyTrimPath");
      } else {
         this.path.reset();

         int var4;
         for(var4 = var2.paths.size() - 1; var4 >= 0; --var4) {
            this.path.addPath(((PathContent)var2.paths.get(var4)).getPath(), var3);
         }

         this.pm.setPath(this.path, false);

         float var5;
         for(var5 = this.pm.getLength(); this.pm.nextContour(); var5 += this.pm.getLength()) {
         }

         float var6 = (Float)var2.trimPath.getOffset().getValue() * var5 / 360.0F;
         float var7 = (Float)var2.trimPath.getStart().getValue() * var5 / 100.0F + var6;
         float var8 = (Float)var2.trimPath.getEnd().getValue() * var5 / 100.0F + var6;
         var4 = var2.paths.size() - 1;

         for(var6 = 0.0F; var4 >= 0; --var4) {
            float var9;
            label75: {
               this.trimPathPath.set(((PathContent)var2.paths.get(var4)).getPath());
               this.trimPathPath.transform(var3);
               this.pm.setPath(this.trimPathPath, false);
               var9 = this.pm.getLength();
               float var10 = 1.0F;
               float var11;
               float var12;
               if (var8 > var5) {
                  var11 = var8 - var5;
                  if (var11 < var6 + var9 && var6 < var11) {
                     if (var7 > var5) {
                        var12 = (var7 - var5) / var9;
                     } else {
                        var12 = 0.0F;
                     }

                     var10 = Math.min(var11 / var9, 1.0F);
                     Utils.applyTrimPathIfNeeded(this.trimPathPath, var12, var10, 0.0F);
                     var1.drawPath(this.trimPathPath, this.paint);
                     break label75;
                  }
               }

               var11 = var6 + var9;
               if (var11 >= var7 && var6 <= var8) {
                  if (var11 <= var8 && var7 < var6) {
                     var1.drawPath(this.trimPathPath, this.paint);
                  } else {
                     if (var7 < var6) {
                        var12 = 0.0F;
                     } else {
                        var12 = (var7 - var6) / var9;
                     }

                     if (var8 <= var11) {
                        var10 = (var8 - var6) / var9;
                     }

                     Utils.applyTrimPathIfNeeded(this.trimPathPath, var12, var10, 0.0F);
                     var1.drawPath(this.trimPathPath, this.paint);
                  }
               }
            }

            var6 += var9;
         }

         L.endSection("StrokeContent#applyTrimPath");
      }
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (var1 == LottieProperty.OPACITY) {
         this.opacityAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.STROKE_WIDTH) {
         this.widthAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.COLOR_FILTER) {
         if (var2 == null) {
            this.colorFilterAnimation = null;
         } else {
            this.colorFilterAnimation = new ValueCallbackKeyframeAnimation(var2);
            this.colorFilterAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorFilterAnimation);
         }
      }

   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      L.beginSection("StrokeContent#draw");
      if (Utils.hasZeroScaleAxis(var2)) {
         L.endSection("StrokeContent#draw");
      } else {
         var3 = (int)((float)var3 / 255.0F * (float)((IntegerKeyframeAnimation)this.opacityAnimation).getIntValue() / 100.0F * 255.0F);
         Paint var4 = this.paint;
         byte var5 = 0;
         var4.setAlpha(MiscUtils.clamp(var3, 0, 255));
         this.paint.setStrokeWidth(((FloatKeyframeAnimation)this.widthAnimation).getFloatValue() * Utils.getScale(var2));
         if (this.paint.getStrokeWidth() <= 0.0F) {
            L.endSection("StrokeContent#draw");
         } else {
            this.applyDashPatternIfNeeded(var2);
            BaseKeyframeAnimation var6 = this.colorFilterAnimation;
            var3 = var5;
            if (var6 != null) {
               this.paint.setColorFilter((ColorFilter)var6.getValue());
               var3 = var5;
            }

            for(; var3 < this.pathGroups.size(); ++var3) {
               BaseStrokeContent.PathGroup var7 = (BaseStrokeContent.PathGroup)this.pathGroups.get(var3);
               if (var7.trimPath != null) {
                  this.applyTrimPath(var1, var7, var2);
               } else {
                  L.beginSection("StrokeContent#buildPath");
                  this.path.reset();

                  for(int var8 = var7.paths.size() - 1; var8 >= 0; --var8) {
                     this.path.addPath(((PathContent)var7.paths.get(var8)).getPath(), var2);
                  }

                  L.endSection("StrokeContent#buildPath");
                  L.beginSection("StrokeContent#drawPath");
                  var1.drawPath(this.path, this.paint);
                  L.endSection("StrokeContent#drawPath");
               }
            }

            L.endSection("StrokeContent#draw");
         }
      }
   }

   public void getBounds(RectF var1, Matrix var2, boolean var3) {
      L.beginSection("StrokeContent#getBounds");
      this.path.reset();

      for(int var4 = 0; var4 < this.pathGroups.size(); ++var4) {
         BaseStrokeContent.PathGroup var5 = (BaseStrokeContent.PathGroup)this.pathGroups.get(var4);

         for(int var6 = 0; var6 < var5.paths.size(); ++var6) {
            this.path.addPath(((PathContent)var5.paths.get(var6)).getPath(), var2);
         }
      }

      this.path.computeBounds(this.rect, false);
      float var7 = ((FloatKeyframeAnimation)this.widthAnimation).getFloatValue();
      RectF var9 = this.rect;
      float var8 = var9.left;
      var7 /= 2.0F;
      var9.set(var8 - var7, var9.top - var7, var9.right + var7, var9.bottom + var7);
      var1.set(this.rect);
      var1.set(var1.left - 1.0F, var1.top - 1.0F, var1.right + 1.0F, var1.bottom + 1.0F);
      L.endSection("StrokeContent#getBounds");
   }

   public void onValueChanged() {
      this.lottieDrawable.invalidateSelf();
   }

   public void resolveKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      MiscUtils.resolveKeyPath(var1, var2, var3, var4, this);
   }

   public void setContents(List var1, List var2) {
      int var3 = var1.size() - 1;

      TrimPathContent var4;
      Content var5;
      TrimPathContent var6;
      for(var4 = null; var3 >= 0; var4 = var6) {
         var5 = (Content)var1.get(var3);
         var6 = var4;
         if (var5 instanceof TrimPathContent) {
            TrimPathContent var9 = (TrimPathContent)var5;
            var6 = var4;
            if (var9.getType() == ShapeTrimPath.Type.INDIVIDUALLY) {
               var6 = var9;
            }
         }

         --var3;
      }

      if (var4 != null) {
         var4.addListener(this);
      }

      var3 = var2.size() - 1;

      BaseStrokeContent.PathGroup var8;
      BaseStrokeContent.PathGroup var10;
      for(var8 = null; var3 >= 0; var8 = var10) {
         label43: {
            var5 = (Content)var2.get(var3);
            if (var5 instanceof TrimPathContent) {
               TrimPathContent var7 = (TrimPathContent)var5;
               if (var7.getType() == ShapeTrimPath.Type.INDIVIDUALLY) {
                  if (var8 != null) {
                     this.pathGroups.add(var8);
                  }

                  var10 = new BaseStrokeContent.PathGroup(var7);
                  var7.addListener(this);
                  break label43;
               }
            }

            var10 = var8;
            if (var5 instanceof PathContent) {
               var10 = var8;
               if (var8 == null) {
                  var10 = new BaseStrokeContent.PathGroup(var4);
               }

               var10.paths.add((PathContent)var5);
            }
         }

         --var3;
      }

      if (var8 != null) {
         this.pathGroups.add(var8);
      }

   }

   private static final class PathGroup {
      private final List paths;
      private final TrimPathContent trimPath;

      private PathGroup(TrimPathContent var1) {
         this.paths = new ArrayList();
         this.trimPath = var1;
      }

      // $FF: synthetic method
      PathGroup(TrimPathContent var1, Object var2) {
         this(var1);
      }
   }
}
