package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.value.LottieValueCallback;

public class SolidLayer extends BaseLayer {
   private BaseKeyframeAnimation colorFilterAnimation;
   private final Layer layerModel;
   private final Paint paint = new LPaint();
   private final Path path = new Path();
   private final float[] points = new float[8];
   private final RectF rect = new RectF();

   SolidLayer(LottieDrawable var1, Layer var2) {
      super(var1, var2);
      this.layerModel = var2;
      this.paint.setAlpha(0);
      this.paint.setStyle(Style.FILL);
      this.paint.setColor(var2.getSolidColor());
   }

   public void addValueCallback(Object var1, LottieValueCallback var2) {
      super.addValueCallback(var1, var2);
      if (var1 == LottieProperty.COLOR_FILTER) {
         if (var2 == null) {
            this.colorFilterAnimation = null;
         } else {
            this.colorFilterAnimation = new ValueCallbackKeyframeAnimation(var2);
         }
      }

   }

   public void drawLayer(Canvas var1, Matrix var2, int var3) {
      int var4 = Color.alpha(this.layerModel.getSolidColor());
      if (var4 != 0) {
         int var5;
         if (super.transform.getOpacity() == null) {
            var5 = 100;
         } else {
            var5 = (Integer)super.transform.getOpacity().getValue();
         }

         var3 = (int)((float)var3 / 255.0F * ((float)var4 / 255.0F * (float)var5 / 100.0F) * 255.0F);
         this.paint.setAlpha(var3);
         BaseKeyframeAnimation var6 = this.colorFilterAnimation;
         if (var6 != null) {
            this.paint.setColorFilter((ColorFilter)var6.getValue());
         }

         if (var3 > 0) {
            float[] var9 = this.points;
            var9[0] = 0.0F;
            var9[1] = 0.0F;
            var9[2] = (float)this.layerModel.getSolidWidth();
            var9 = this.points;
            var9[3] = 0.0F;
            var9[4] = (float)this.layerModel.getSolidWidth();
            this.points[5] = (float)this.layerModel.getSolidHeight();
            var9 = this.points;
            var9[6] = 0.0F;
            var9[7] = (float)this.layerModel.getSolidHeight();
            var2.mapPoints(this.points);
            this.path.reset();
            Path var10 = this.path;
            float[] var7 = this.points;
            var10.moveTo(var7[0], var7[1]);
            Path var8 = this.path;
            var9 = this.points;
            var8.lineTo(var9[2], var9[3]);
            var8 = this.path;
            var9 = this.points;
            var8.lineTo(var9[4], var9[5]);
            var10 = this.path;
            var7 = this.points;
            var10.lineTo(var7[6], var7[7]);
            var10 = this.path;
            var7 = this.points;
            var10.lineTo(var7[0], var7[1]);
            this.path.close();
            var1.drawPath(this.path, this.paint);
         }

      }
   }

   public void getBounds(RectF var1, Matrix var2, boolean var3) {
      super.getBounds(var1, var2, var3);
      this.rect.set(0.0F, 0.0F, (float)this.layerModel.getSolidWidth(), (float)this.layerModel.getSolidHeight());
      super.boundsMatrix.mapRect(this.rect);
      var1.set(this.rect);
   }
}
