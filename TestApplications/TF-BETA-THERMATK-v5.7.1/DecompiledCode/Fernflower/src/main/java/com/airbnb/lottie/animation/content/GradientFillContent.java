package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.model.content.GradientFill;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public class GradientFillContent implements DrawingContent, BaseKeyframeAnimation.AnimationListener, KeyPathElementContent {
   private final RectF boundsRect = new RectF();
   private final int cacheSteps;
   private final BaseKeyframeAnimation colorAnimation;
   private ValueCallbackKeyframeAnimation colorCallbackAnimation;
   private BaseKeyframeAnimation colorFilterAnimation;
   private final BaseKeyframeAnimation endPointAnimation;
   private final boolean hidden;
   private final BaseLayer layer;
   private final LongSparseArray linearGradientCache = new LongSparseArray();
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final BaseKeyframeAnimation opacityAnimation;
   private final Paint paint = new LPaint(1);
   private final Path path = new Path();
   private final List paths = new ArrayList();
   private final LongSparseArray radialGradientCache = new LongSparseArray();
   private final Matrix shaderMatrix = new Matrix();
   private final BaseKeyframeAnimation startPointAnimation;
   private final GradientType type;

   public GradientFillContent(LottieDrawable var1, BaseLayer var2, GradientFill var3) {
      this.layer = var2;
      this.name = var3.getName();
      this.hidden = var3.isHidden();
      this.lottieDrawable = var1;
      this.type = var3.getGradientType();
      this.path.setFillType(var3.getFillType());
      this.cacheSteps = (int)(var1.getComposition().getDuration() / 32.0F);
      this.colorAnimation = var3.getGradientColor().createAnimation();
      this.colorAnimation.addUpdateListener(this);
      var2.addAnimation(this.colorAnimation);
      this.opacityAnimation = var3.getOpacity().createAnimation();
      this.opacityAnimation.addUpdateListener(this);
      var2.addAnimation(this.opacityAnimation);
      this.startPointAnimation = var3.getStartPoint().createAnimation();
      this.startPointAnimation.addUpdateListener(this);
      var2.addAnimation(this.startPointAnimation);
      this.endPointAnimation = var3.getEndPoint().createAnimation();
      this.endPointAnimation.addUpdateListener(this);
      var2.addAnimation(this.endPointAnimation);
   }

   private int[] applyDynamicColorsIfNeeded(int[] var1) {
      ValueCallbackKeyframeAnimation var2 = this.colorCallbackAnimation;
      int[] var3 = var1;
      if (var2 != null) {
         Integer[] var8 = (Integer[])var2.getValue();
         int var4 = var1.length;
         int var5 = var8.length;
         byte var6 = 0;
         int var7 = 0;
         if (var4 == var5) {
            while(true) {
               var3 = var1;
               if (var7 >= var1.length) {
                  break;
               }

               var1[var7] = var8[var7];
               ++var7;
            }
         } else {
            var1 = new int[var8.length];
            var7 = var6;

            while(true) {
               var3 = var1;
               if (var7 >= var8.length) {
                  break;
               }

               var1[var7] = var8[var7];
               ++var7;
            }
         }
      }

      return var3;
   }

   private int getGradientHash() {
      int var1 = Math.round(this.startPointAnimation.getProgress() * (float)this.cacheSteps);
      int var2 = Math.round(this.endPointAnimation.getProgress() * (float)this.cacheSteps);
      int var3 = Math.round(this.colorAnimation.getProgress() * (float)this.cacheSteps);
      int var4;
      if (var1 != 0) {
         var4 = 527 * var1;
      } else {
         var4 = 17;
      }

      var1 = var4;
      if (var2 != 0) {
         var1 = var4 * 31 * var2;
      }

      var4 = var1;
      if (var3 != 0) {
         var4 = var1 * 31 * var3;
      }

      return var4;
   }

   private LinearGradient getLinearGradient() {
      int var1 = this.getGradientHash();
      LongSparseArray var2 = this.linearGradientCache;
      long var3 = (long)var1;
      LinearGradient var9 = (LinearGradient)var2.get(var3);
      if (var9 != null) {
         return var9;
      } else {
         PointF var5 = (PointF)this.startPointAnimation.getValue();
         PointF var10 = (PointF)this.endPointAnimation.getValue();
         GradientColor var6 = (GradientColor)this.colorAnimation.getValue();
         int[] var7 = this.applyDynamicColorsIfNeeded(var6.getColors());
         float[] var8 = var6.getPositions();
         var9 = new LinearGradient(var5.x, var5.y, var10.x, var10.y, var7, var8, TileMode.CLAMP);
         this.linearGradientCache.put(var3, var9);
         return var9;
      }
   }

   private RadialGradient getRadialGradient() {
      int var1 = this.getGradientHash();
      LongSparseArray var2 = this.radialGradientCache;
      long var3 = (long)var1;
      RadialGradient var12 = (RadialGradient)var2.get(var3);
      if (var12 != null) {
         return var12;
      } else {
         PointF var13 = (PointF)this.startPointAnimation.getValue();
         PointF var5 = (PointF)this.endPointAnimation.getValue();
         GradientColor var6 = (GradientColor)this.colorAnimation.getValue();
         int[] var7 = this.applyDynamicColorsIfNeeded(var6.getColors());
         float[] var14 = var6.getPositions();
         float var8 = var13.x;
         float var9 = var13.y;
         float var10 = var5.x;
         float var11 = var5.y;
         var11 = (float)Math.hypot((double)(var10 - var8), (double)(var11 - var9));
         if (var11 <= 0.0F) {
            var11 = 0.001F;
         }

         var12 = new RadialGradient(var8, var9, var11, var7, var14, TileMode.CLAMP);
         this.radialGradientCache.put(var3, var12);
         return var12;
      }
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (var1 == LottieProperty.OPACITY) {
         this.opacityAnimation.setValueCallback(var2);
      } else if (var1 == LottieProperty.COLOR_FILTER) {
         if (var2 == null) {
            this.colorFilterAnimation = null;
         } else {
            this.colorFilterAnimation = new ValueCallbackKeyframeAnimation(var2);
            this.colorFilterAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorFilterAnimation);
         }
      } else if (var1 == LottieProperty.GRADIENT_COLOR) {
         if (var2 == null) {
            ValueCallbackKeyframeAnimation var3 = this.colorCallbackAnimation;
            if (var3 != null) {
               this.layer.removeAnimation(var3);
            }

            this.colorCallbackAnimation = null;
         } else {
            this.colorCallbackAnimation = new ValueCallbackKeyframeAnimation(var2);
            this.colorCallbackAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorCallbackAnimation);
         }
      }

   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      if (!this.hidden) {
         L.beginSection("GradientFillContent#draw");
         this.path.reset();

         for(int var4 = 0; var4 < this.paths.size(); ++var4) {
            this.path.addPath(((PathContent)this.paths.get(var4)).getPath(), var2);
         }

         this.path.computeBounds(this.boundsRect, false);
         Object var5;
         if (this.type == GradientType.LINEAR) {
            var5 = this.getLinearGradient();
         } else {
            var5 = this.getRadialGradient();
         }

         this.shaderMatrix.set(var2);
         ((Shader)var5).setLocalMatrix(this.shaderMatrix);
         this.paint.setShader((Shader)var5);
         BaseKeyframeAnimation var6 = this.colorFilterAnimation;
         if (var6 != null) {
            this.paint.setColorFilter((ColorFilter)var6.getValue());
         }

         var3 = (int)((float)var3 / 255.0F * (float)(Integer)this.opacityAnimation.getValue() / 100.0F * 255.0F);
         this.paint.setAlpha(MiscUtils.clamp(var3, 0, 255));
         var1.drawPath(this.path, this.paint);
         L.endSection("GradientFillContent#draw");
      }
   }

   public void getBounds(RectF var1, Matrix var2, boolean var3) {
      this.path.reset();

      for(int var4 = 0; var4 < this.paths.size(); ++var4) {
         this.path.addPath(((PathContent)this.paths.get(var4)).getPath(), var2);
      }

      this.path.computeBounds(var1, false);
      var1.set(var1.left - 1.0F, var1.top - 1.0F, var1.right + 1.0F, var1.bottom + 1.0F);
   }

   public String getName() {
      return this.name;
   }

   public void onValueChanged() {
      this.lottieDrawable.invalidateSelf();
   }

   public void resolveKeyPath(KeyPath var1, int var2, List var3, KeyPath var4) {
      MiscUtils.resolveKeyPath(var1, var2, var3, var4, this);
   }

   public void setContents(List var1, List var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         Content var4 = (Content)var2.get(var3);
         if (var4 instanceof PathContent) {
            this.paths.add((PathContent)var4);
         }
      }

   }
}
