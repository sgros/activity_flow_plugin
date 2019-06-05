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
import android.support.v4.util.LongSparseArray;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
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

public class GradientFillContent implements DrawingContent, KeyPathElementContent, BaseKeyframeAnimation.AnimationListener {
   private final RectF boundsRect = new RectF();
   private final int cacheSteps;
   private final BaseKeyframeAnimation colorAnimation;
   private BaseKeyframeAnimation colorFilterAnimation;
   private final BaseKeyframeAnimation endPointAnimation;
   private final BaseLayer layer;
   private final LongSparseArray linearGradientCache = new LongSparseArray();
   private final LottieDrawable lottieDrawable;
   private final String name;
   private final BaseKeyframeAnimation opacityAnimation;
   private final Paint paint = new Paint(1);
   private final Path path = new Path();
   private final List paths = new ArrayList();
   private final LongSparseArray radialGradientCache = new LongSparseArray();
   private final Matrix shaderMatrix = new Matrix();
   private final BaseKeyframeAnimation startPointAnimation;
   private final GradientType type;

   public GradientFillContent(LottieDrawable var1, BaseLayer var2, GradientFill var3) {
      this.layer = var2;
      this.name = var3.getName();
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
         PointF var10 = (PointF)this.startPointAnimation.getValue();
         PointF var5 = (PointF)this.endPointAnimation.getValue();
         GradientColor var6 = (GradientColor)this.colorAnimation.getValue();
         int[] var7 = var6.getColors();
         float[] var8 = var6.getPositions();
         var9 = new LinearGradient(var10.x, var10.y, var5.x, var5.y, var7, var8, TileMode.CLAMP);
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
         PointF var5 = (PointF)this.startPointAnimation.getValue();
         PointF var6 = (PointF)this.endPointAnimation.getValue();
         GradientColor var7 = (GradientColor)this.colorAnimation.getValue();
         int[] var13 = var7.getColors();
         float[] var14 = var7.getPositions();
         float var8 = var5.x;
         float var9 = var5.y;
         float var10 = var6.x;
         float var11 = var6.y;
         var12 = new RadialGradient(var8, var9, (float)Math.hypot((double)(var10 - var8), (double)(var11 - var9)), var13, var14, TileMode.CLAMP);
         this.radialGradientCache.put(var3, var12);
         return var12;
      }
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      if (var1 == LottieProperty.COLOR_FILTER) {
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
      L.beginSection("GradientFillContent#draw");
      this.path.reset();

      for(int var4 = 0; var4 < this.paths.size(); ++var4) {
         this.path.addPath(((PathContent)this.paths.get(var4)).getPath(), var2);
      }

      this.path.computeBounds(this.boundsRect, false);
      Object var5;
      if (this.type == GradientType.Linear) {
         var5 = this.getLinearGradient();
      } else {
         var5 = this.getRadialGradient();
      }

      this.shaderMatrix.set(var2);
      ((Shader)var5).setLocalMatrix(this.shaderMatrix);
      this.paint.setShader((Shader)var5);
      if (this.colorFilterAnimation != null) {
         this.paint.setColorFilter((ColorFilter)this.colorFilterAnimation.getValue());
      }

      var3 = (int)((float)var3 / 255.0F * (float)(Integer)this.opacityAnimation.getValue() / 100.0F * 255.0F);
      this.paint.setAlpha(MiscUtils.clamp(var3, 0, 255));
      var1.drawPath(this.path, this.paint);
      L.endSection("GradientFillContent#draw");
   }

   public void getBounds(RectF var1, Matrix var2) {
      this.path.reset();

      for(int var3 = 0; var3 < this.paths.size(); ++var3) {
         this.path.addPath(((PathContent)this.paths.get(var3)).getPath(), var2);
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
