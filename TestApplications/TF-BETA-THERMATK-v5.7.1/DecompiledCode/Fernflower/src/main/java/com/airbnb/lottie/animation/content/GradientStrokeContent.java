package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.model.content.GradientStroke;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.LottieValueCallback;

public class GradientStrokeContent extends BaseStrokeContent {
   private final RectF boundsRect = new RectF();
   private final int cacheSteps;
   private final BaseKeyframeAnimation colorAnimation;
   private ValueCallbackKeyframeAnimation colorCallbackAnimation;
   private final BaseKeyframeAnimation endPointAnimation;
   private final boolean hidden;
   private final LongSparseArray linearGradientCache = new LongSparseArray();
   private final String name;
   private final LongSparseArray radialGradientCache = new LongSparseArray();
   private final BaseKeyframeAnimation startPointAnimation;
   private final GradientType type;

   public GradientStrokeContent(LottieDrawable var1, BaseLayer var2, GradientStroke var3) {
      super(var1, var2, var3.getCapType().toPaintCap(), var3.getJoinType().toPaintJoin(), var3.getMiterLimit(), var3.getOpacity(), var3.getWidth(), var3.getLineDashPattern(), var3.getDashOffset());
      this.name = var3.getName();
      this.type = var3.getGradientType();
      this.hidden = var3.isHidden();
      this.cacheSteps = (int)(var1.getComposition().getDuration() / 32.0F);
      this.colorAnimation = var3.getGradientColor().createAnimation();
      this.colorAnimation.addUpdateListener(this);
      var2.addAnimation(this.colorAnimation);
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
      LinearGradient var12 = (LinearGradient)var2.get(var3);
      if (var12 != null) {
         return var12;
      } else {
         PointF var5 = (PointF)this.startPointAnimation.getValue();
         PointF var6 = (PointF)this.endPointAnimation.getValue();
         GradientColor var7 = (GradientColor)this.colorAnimation.getValue();
         int[] var13 = this.applyDynamicColorsIfNeeded(var7.getColors());
         float[] var15 = var7.getPositions();
         RectF var8 = this.boundsRect;
         int var9 = (int)(var8.left + var8.width() / 2.0F + var5.x);
         var8 = this.boundsRect;
         var1 = (int)(var8.top + var8.height() / 2.0F + var5.y);
         RectF var14 = this.boundsRect;
         int var10 = (int)(var14.left + var14.width() / 2.0F + var6.x);
         var14 = this.boundsRect;
         int var11 = (int)(var14.top + var14.height() / 2.0F + var6.y);
         var12 = new LinearGradient((float)var9, (float)var1, (float)var10, (float)var11, var13, var15, TileMode.CLAMP);
         this.linearGradientCache.put(var3, var12);
         return var12;
      }
   }

   private RadialGradient getRadialGradient() {
      int var1 = this.getGradientHash();
      LongSparseArray var2 = this.radialGradientCache;
      long var3 = (long)var1;
      RadialGradient var13 = (RadialGradient)var2.get(var3);
      if (var13 != null) {
         return var13;
      } else {
         PointF var5 = (PointF)this.startPointAnimation.getValue();
         PointF var14 = (PointF)this.endPointAnimation.getValue();
         GradientColor var6 = (GradientColor)this.colorAnimation.getValue();
         int[] var7 = this.applyDynamicColorsIfNeeded(var6.getColors());
         float[] var16 = var6.getPositions();
         RectF var8 = this.boundsRect;
         int var9 = (int)(var8.left + var8.width() / 2.0F + var5.x);
         var8 = this.boundsRect;
         int var10 = (int)(var8.top + var8.height() / 2.0F + var5.y);
         RectF var15 = this.boundsRect;
         var1 = (int)(var15.left + var15.width() / 2.0F + var14.x);
         var15 = this.boundsRect;
         int var11 = (int)(var15.top + var15.height() / 2.0F + var14.y);
         float var12 = (float)Math.hypot((double)(var1 - var9), (double)(var11 - var10));
         var13 = new RadialGradient((float)var9, (float)var10, var12, var7, var16, TileMode.CLAMP);
         this.radialGradientCache.put(var3, var13);
         return var13;
      }
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      super.addValueCallback(var1, var2);
      if (var1 == LottieProperty.GRADIENT_COLOR) {
         if (var2 == null) {
            ValueCallbackKeyframeAnimation var3 = this.colorCallbackAnimation;
            if (var3 != null) {
               super.layer.removeAnimation(var3);
            }

            this.colorCallbackAnimation = null;
         } else {
            this.colorCallbackAnimation = new ValueCallbackKeyframeAnimation(var2);
            this.colorCallbackAnimation.addUpdateListener(this);
            super.layer.addAnimation(this.colorCallbackAnimation);
         }
      }

   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      if (!this.hidden) {
         this.getBounds(this.boundsRect, var2, false);
         Object var4;
         if (this.type == GradientType.LINEAR) {
            var4 = this.getLinearGradient();
         } else {
            var4 = this.getRadialGradient();
         }

         super.paint.setShader((Shader)var4);
         super.draw(var1, var2, var3);
      }
   }

   public String getName() {
      return this.name;
   }
}
