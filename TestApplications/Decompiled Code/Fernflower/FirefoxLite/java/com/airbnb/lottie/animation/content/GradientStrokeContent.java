package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.support.v4.util.LongSparseArray;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.model.content.GradientStroke;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.layer.BaseLayer;

public class GradientStrokeContent extends BaseStrokeContent {
   private final RectF boundsRect = new RectF();
   private final int cacheSteps;
   private final BaseKeyframeAnimation colorAnimation;
   private final BaseKeyframeAnimation endPointAnimation;
   private final LongSparseArray linearGradientCache = new LongSparseArray();
   private final String name;
   private final LongSparseArray radialGradientCache = new LongSparseArray();
   private final BaseKeyframeAnimation startPointAnimation;
   private final GradientType type;

   public GradientStrokeContent(LottieDrawable var1, BaseLayer var2, GradientStroke var3) {
      super(var1, var2, var3.getCapType().toPaintCap(), var3.getJoinType().toPaintJoin(), var3.getMiterLimit(), var3.getOpacity(), var3.getWidth(), var3.getLineDashPattern(), var3.getDashOffset());
      this.name = var3.getName();
      this.type = var3.getGradientType();
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
      LinearGradient var11 = (LinearGradient)var2.get(var3);
      if (var11 != null) {
         return var11;
      } else {
         PointF var5 = (PointF)this.startPointAnimation.getValue();
         PointF var6 = (PointF)this.endPointAnimation.getValue();
         GradientColor var7 = (GradientColor)this.colorAnimation.getValue();
         int[] var12 = var7.getColors();
         float[] var13 = var7.getPositions();
         var1 = (int)(this.boundsRect.left + this.boundsRect.width() / 2.0F + var5.x);
         int var8 = (int)(this.boundsRect.top + this.boundsRect.height() / 2.0F + var5.y);
         int var9 = (int)(this.boundsRect.left + this.boundsRect.width() / 2.0F + var6.x);
         int var10 = (int)(this.boundsRect.top + this.boundsRect.height() / 2.0F + var6.y);
         var11 = new LinearGradient((float)var1, (float)var8, (float)var9, (float)var10, var12, var13, TileMode.CLAMP);
         this.linearGradientCache.put(var3, var11);
         return var11;
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
         PointF var5 = (PointF)this.startPointAnimation.getValue();
         PointF var13 = (PointF)this.endPointAnimation.getValue();
         GradientColor var6 = (GradientColor)this.colorAnimation.getValue();
         int[] var7 = var6.getColors();
         float[] var14 = var6.getPositions();
         int var8 = (int)(this.boundsRect.left + this.boundsRect.width() / 2.0F + var5.x);
         int var9 = (int)(this.boundsRect.top + this.boundsRect.height() / 2.0F + var5.y);
         var1 = (int)(this.boundsRect.left + this.boundsRect.width() / 2.0F + var13.x);
         int var10 = (int)(this.boundsRect.top + this.boundsRect.height() / 2.0F + var13.y);
         float var11 = (float)Math.hypot((double)(var1 - var8), (double)(var10 - var9));
         var12 = new RadialGradient((float)var8, (float)var9, var11, var7, var14, TileMode.CLAMP);
         this.radialGradientCache.put(var3, var12);
         return var12;
      }
   }

   public void draw(Canvas var1, Matrix var2, int var3) {
      this.getBounds(this.boundsRect, var2);
      if (this.type == GradientType.Linear) {
         this.paint.setShader(this.getLinearGradient());
      } else {
         this.paint.setShader(this.getRadialGradient());
      }

      super.draw(var1, var2, var3);
   }

   public String getName() {
      return this.name;
   }
}
