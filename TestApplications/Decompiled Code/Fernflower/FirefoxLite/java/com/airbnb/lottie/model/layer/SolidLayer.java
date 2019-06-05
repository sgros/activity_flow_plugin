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
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.value.LottieValueCallback;

public class SolidLayer extends BaseLayer {
   private BaseKeyframeAnimation colorFilterAnimation;
   private final Layer layerModel;
   private final Paint paint = new Paint();
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
         var3 = (int)((float)var3 / 255.0F * ((float)var4 / 255.0F * (float)(Integer)this.transform.getOpacity().getValue() / 100.0F) * 255.0F);
         this.paint.setAlpha(var3);
         if (this.colorFilterAnimation != null) {
            this.paint.setColorFilter((ColorFilter)this.colorFilterAnimation.getValue());
         }

         if (var3 > 0) {
            this.points[0] = 0.0F;
            this.points[1] = 0.0F;
            this.points[2] = (float)this.layerModel.getSolidWidth();
            this.points[3] = 0.0F;
            this.points[4] = (float)this.layerModel.getSolidWidth();
            this.points[5] = (float)this.layerModel.getSolidHeight();
            this.points[6] = 0.0F;
            this.points[7] = (float)this.layerModel.getSolidHeight();
            var2.mapPoints(this.points);
            this.path.reset();
            this.path.moveTo(this.points[0], this.points[1]);
            this.path.lineTo(this.points[2], this.points[3]);
            this.path.lineTo(this.points[4], this.points[5]);
            this.path.lineTo(this.points[6], this.points[7]);
            this.path.lineTo(this.points[0], this.points[1]);
            this.path.close();
            var1.drawPath(this.path, this.paint);
         }

      }
   }

   public void getBounds(RectF var1, Matrix var2) {
      super.getBounds(var1, var2);
      this.rect.set(0.0F, 0.0F, (float)this.layerModel.getSolidWidth(), (float)this.layerModel.getSolidHeight());
      this.boundsMatrix.mapRect(this.rect);
      var1.set(this.rect);
   }
}
