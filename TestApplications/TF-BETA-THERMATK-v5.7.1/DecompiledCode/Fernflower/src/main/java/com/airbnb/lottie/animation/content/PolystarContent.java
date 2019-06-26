package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.PolystarShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class PolystarContent implements PathContent, BaseKeyframeAnimation.AnimationListener, KeyPathElementContent {
   private final boolean hidden;
   private final BaseKeyframeAnimation innerRadiusAnimation;
   private final BaseKeyframeAnimation innerRoundednessAnimation;
   private boolean isPathValid;
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final BaseKeyframeAnimation outerRadiusAnimation;
   private final BaseKeyframeAnimation outerRoundednessAnimation;
   private final Path path = new Path();
   private final BaseKeyframeAnimation pointsAnimation;
   private final BaseKeyframeAnimation positionAnimation;
   private final BaseKeyframeAnimation rotationAnimation;
   private CompoundTrimPathContent trimPaths = new CompoundTrimPathContent();
   private final PolystarShape.Type type;

   public PolystarContent(LottieDrawable var1, BaseLayer var2, PolystarShape var3) {
      this.lottieDrawable = var1;
      this.name = var3.getName();
      this.type = var3.getType();
      this.hidden = var3.isHidden();
      this.pointsAnimation = var3.getPoints().createAnimation();
      this.positionAnimation = var3.getPosition().createAnimation();
      this.rotationAnimation = var3.getRotation().createAnimation();
      this.outerRadiusAnimation = var3.getOuterRadius().createAnimation();
      this.outerRoundednessAnimation = var3.getOuterRoundedness().createAnimation();
      if (this.type == PolystarShape.Type.STAR) {
         this.innerRadiusAnimation = var3.getInnerRadius().createAnimation();
         this.innerRoundednessAnimation = var3.getInnerRoundedness().createAnimation();
      } else {
         this.innerRadiusAnimation = null;
         this.innerRoundednessAnimation = null;
      }

      var2.addAnimation(this.pointsAnimation);
      var2.addAnimation(this.positionAnimation);
      var2.addAnimation(this.rotationAnimation);
      var2.addAnimation(this.outerRadiusAnimation);
      var2.addAnimation(this.outerRoundednessAnimation);
      if (this.type == PolystarShape.Type.STAR) {
         var2.addAnimation(this.innerRadiusAnimation);
         var2.addAnimation(this.innerRoundednessAnimation);
      }

      this.pointsAnimation.addUpdateListener(this);
      this.positionAnimation.addUpdateListener(this);
      this.rotationAnimation.addUpdateListener(this);
      this.outerRadiusAnimation.addUpdateListener(this);
      this.outerRoundednessAnimation.addUpdateListener(this);
      if (this.type == PolystarShape.Type.STAR) {
         this.innerRadiusAnimation.addUpdateListener(this);
         this.innerRoundednessAnimation.addUpdateListener(this);
      }

   }

   private void createPolygonPath() {
      int var1 = (int)Math.floor((double)(Float)this.pointsAnimation.getValue());
      BaseKeyframeAnimation var2 = this.rotationAnimation;
      double var3;
      if (var2 == null) {
         var3 = 0.0D;
      } else {
         var3 = (double)(Float)var2.getValue();
      }

      double var5 = Math.toRadians(var3 - 90.0D);
      double var7 = (double)var1;
      Double.isNaN(var7);
      float var9 = (float)(6.283185307179586D / var7);
      float var10 = (Float)this.outerRoundednessAnimation.getValue() / 100.0F;
      float var11 = (Float)this.outerRadiusAnimation.getValue();
      double var12 = (double)var11;
      var3 = Math.cos(var5);
      Double.isNaN(var12);
      float var14 = (float)(var3 * var12);
      var3 = Math.sin(var5);
      Double.isNaN(var12);
      float var15 = (float)(var3 * var12);
      this.path.moveTo(var14, var15);
      var3 = (double)var9;
      Double.isNaN(var3);
      var5 += var3;
      var7 = Math.ceil(var7);

      for(var1 = 0; (double)var1 < var7; var14 = var9) {
         double var16 = Math.cos(var5);
         Double.isNaN(var12);
         var9 = (float)(var16 * var12);
         var16 = Math.sin(var5);
         Double.isNaN(var12);
         float var18 = (float)(var12 * var16);
         if (var10 != 0.0F) {
            var16 = (double)((float)(Math.atan2((double)var15, (double)var14) - 1.5707963267948966D));
            float var19 = (float)Math.cos(var16);
            float var20 = (float)Math.sin(var16);
            var16 = (double)((float)(Math.atan2((double)var18, (double)var9) - 1.5707963267948966D));
            float var21 = (float)Math.cos(var16);
            float var22 = (float)Math.sin(var16);
            float var23 = var11 * var10 * 0.25F;
            this.path.cubicTo(var14 - var19 * var23, var15 - var20 * var23, var9 + var21 * var23, var18 + var23 * var22, var9, var18);
         } else {
            this.path.lineTo(var9, var18);
         }

         Double.isNaN(var3);
         var5 += var3;
         ++var1;
         var15 = var18;
      }

      PointF var24 = (PointF)this.positionAnimation.getValue();
      this.path.offset(var24.x, var24.y);
      this.path.close();
   }

   private void createStarPath() {
      float var1 = (Float)this.pointsAnimation.getValue();
      BaseKeyframeAnimation var2 = this.rotationAnimation;
      double var3;
      if (var2 == null) {
         var3 = 0.0D;
      } else {
         var3 = (double)(Float)var2.getValue();
      }

      double var5 = Math.toRadians(var3 - 90.0D);
      double var7 = (double)var1;
      Double.isNaN(var7);
      float var9 = (float)(6.283185307179586D / var7);
      float var10 = var9 / 2.0F;
      float var11 = var1 - (float)((int)var1);
      var3 = var5;
      if (var11 != 0.0F) {
         var3 = (double)((1.0F - var11) * var10);
         Double.isNaN(var3);
         var3 += var5;
      }

      float var12 = (Float)this.outerRadiusAnimation.getValue();
      var1 = (Float)this.innerRadiusAnimation.getValue();
      var2 = this.innerRoundednessAnimation;
      float var13;
      if (var2 != null) {
         var13 = (Float)var2.getValue() / 100.0F;
      } else {
         var13 = 0.0F;
      }

      var2 = this.outerRoundednessAnimation;
      float var14;
      if (var2 != null) {
         var14 = (Float)var2.getValue() / 100.0F;
      } else {
         var14 = 0.0F;
      }

      float var15;
      double var16;
      float var18;
      float var19;
      if (var11 != 0.0F) {
         var15 = (var12 - var1) * var11 + var1;
         var5 = (double)var15;
         var16 = Math.cos(var3);
         Double.isNaN(var5);
         var18 = (float)(var5 * var16);
         var16 = Math.sin(var3);
         Double.isNaN(var5);
         var19 = (float)(var5 * var16);
         this.path.moveTo(var18, var19);
         var5 = (double)(var9 * var11 / 2.0F);
         Double.isNaN(var5);
         var3 += var5;
      } else {
         var5 = (double)var12;
         var16 = Math.cos(var3);
         Double.isNaN(var5);
         var18 = (float)(var5 * var16);
         var16 = Math.sin(var3);
         Double.isNaN(var5);
         var19 = (float)(var5 * var16);
         this.path.moveTo(var18, var19);
         var5 = (double)var10;
         Double.isNaN(var5);
         var3 += var5;
         var15 = 0.0F;
      }

      var5 = Math.ceil(var7) * 2.0D;
      int var20 = 0;
      boolean var21 = false;
      float var22 = var19;
      float var23 = var18;
      var19 = var10;
      var18 = var12;

      while(true) {
         var7 = (double)var20;
         if (var7 >= var5) {
            PointF var36 = (PointF)this.positionAnimation.getValue();
            this.path.offset(var36.x, var36.y);
            this.path.close();
            return;
         }

         if (var21) {
            var10 = var18;
         } else {
            var10 = var1;
         }

         float var24;
         if (var15 != 0.0F && var7 == var5 - 2.0D) {
            var24 = var9 * var11 / 2.0F;
         } else {
            var24 = var19;
         }

         if (var15 != 0.0F && var7 == var5 - 1.0D) {
            var10 = var15;
         }

         var16 = (double)var10;
         double var25 = Math.cos(var3);
         Double.isNaN(var16);
         float var27 = (float)(var16 * var25);
         var25 = Math.sin(var3);
         Double.isNaN(var16);
         float var28 = (float)(var16 * var25);
         if (var13 == 0.0F && var14 == 0.0F) {
            this.path.lineTo(var27, var28);
         } else {
            var16 = (double)var22;
            var10 = var1;
            var12 = var13;
            var16 = (double)((float)(Math.atan2(var16, (double)var23) - 1.5707963267948966D));
            float var29 = (float)Math.cos(var16);
            float var30 = (float)Math.sin(var16);
            var16 = (double)var28;
            var16 = (double)((float)(Math.atan2(var16, (double)var27) - 1.5707963267948966D));
            float var33 = (float)Math.cos(var16);
            float var34 = (float)Math.sin(var16);
            float var35;
            if (var21) {
               var35 = var13;
            } else {
               var35 = var14;
            }

            if (var21) {
               var12 = var14;
            }

            float var31;
            if (var21) {
               var31 = var1;
            } else {
               var31 = var18;
            }

            if (var21) {
               var10 = var18;
            }

            var31 = var31 * var35 * 0.47829F;
            var29 *= var31;
            var30 = var31 * var30;
            var10 = var10 * var12 * 0.47829F;
            var33 *= var10;
            var34 = var10 * var34;
            var10 = var29;
            var35 = var33;
            var31 = var30;
            var12 = var34;
            if (var11 != 0.0F) {
               if (var20 == 0) {
                  var10 = var29 * var11;
                  var31 = var30 * var11;
                  var35 = var33;
                  var12 = var34;
               } else {
                  var10 = var29;
                  var35 = var33;
                  var31 = var30;
                  var12 = var34;
                  if (var7 == var5 - 1.0D) {
                     var35 = var33 * var11;
                     var12 = var34 * var11;
                     var31 = var30;
                     var10 = var29;
                  }
               }
            }

            this.path.cubicTo(var23 - var10, var22 - var31, var27 + var35, var28 + var12, var27, var28);
         }

         var7 = (double)var24;
         Double.isNaN(var7);
         var3 += var7;
         var21 ^= true;
         ++var20;
         var23 = var27;
         var22 = var28;
      }
   }

   private void invalidate() {
      this.isPathValid = false;
      this.lottieDrawable.invalidateSelf();
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (var1 == LottieProperty.POLYSTAR_POINTS) {
         this.pointsAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POLYSTAR_ROTATION) {
         this.rotationAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.POSITION) {
         this.positionAnimation.setValueCallback(var2);
      } else {
         BaseKeyframeAnimation var3;
         if (var1 == LottieProperty.POLYSTAR_INNER_RADIUS) {
            var3 = this.innerRadiusAnimation;
            if (var3 != null) {
               var3.setValueCallback(var2);
               return;
            }
         }

         if (var1 == LottieProperty.POLYSTAR_OUTER_RADIUS) {
            this.outerRadiusAnimation.setValueCallback(var2);
         } else {
            if (var1 == LottieProperty.POLYSTAR_INNER_ROUNDEDNESS) {
               var3 = this.innerRoundednessAnimation;
               if (var3 != null) {
                  var3.setValueCallback(var2);
                  return;
               }
            }

            if (var1 == LottieProperty.POLYSTAR_OUTER_ROUNDEDNESS) {
               this.outerRoundednessAnimation.setValueCallback(var2);
            }
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public Path getPath() {
      if (this.isPathValid) {
         return this.path;
      } else {
         this.path.reset();
         if (this.hidden) {
            this.isPathValid = true;
            return this.path;
         } else {
            int var1 = null.$SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type[this.type.ordinal()];
            if (var1 != 1) {
               if (var1 == 2) {
                  this.createPolygonPath();
               }
            } else {
               this.createStarPath();
            }

            this.path.close();
            this.trimPaths.apply(this.path);
            this.isPathValid = true;
            return this.path;
         }
      }
   }

   public void onValueChanged() {
      this.invalidate();
   }

   public void resolveKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      MiscUtils.resolveKeyPath(var1, var2, var3, var4, this);
   }

   public void setContents(List var1, List var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         Content var4 = (Content)var1.get(var3);
         if (var4 instanceof TrimPathContent) {
            TrimPathContent var5 = (TrimPathContent)var4;
            if (var5.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
               this.trimPaths.addTrimPath(var5);
               var5.addListener(this);
            }
         }
      }

   }
}
