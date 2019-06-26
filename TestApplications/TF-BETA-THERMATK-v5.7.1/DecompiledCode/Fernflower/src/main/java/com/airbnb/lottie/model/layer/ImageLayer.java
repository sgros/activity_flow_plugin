package com.airbnb.lottie.model.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;

public class ImageLayer extends BaseLayer {
   private BaseKeyframeAnimation colorFilterAnimation;
   private final Rect dst = new Rect();
   private final Paint paint = new LPaint(3);
   private final Rect src = new Rect();

   ImageLayer(LottieDrawable var1, Layer var2) {
      super(var1, var2);
   }

   private Bitmap getBitmap() {
      String var1 = super.layerModel.getRefId();
      return super.lottieDrawable.getImageAsset(var1);
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
      Bitmap var4 = this.getBitmap();
      if (var4 != null && !var4.isRecycled()) {
         float var5 = Utils.dpScale();
         this.paint.setAlpha(var3);
         BaseKeyframeAnimation var6 = this.colorFilterAnimation;
         if (var6 != null) {
            this.paint.setColorFilter((ColorFilter)var6.getValue());
         }

         var1.save();
         var1.concat(var2);
         this.src.set(0, 0, var4.getWidth(), var4.getHeight());
         this.dst.set(0, 0, (int)((float)var4.getWidth() * var5), (int)((float)var4.getHeight() * var5));
         var1.drawBitmap(var4, this.src, this.dst, this.paint);
         var1.restore();
      }

   }

   public void getBounds(RectF var1, Matrix var2, boolean var3) {
      super.getBounds(var1, var2, var3);
      Bitmap var4 = this.getBitmap();
      if (var4 != null) {
         var1.set(0.0F, 0.0F, (float)var4.getWidth() * Utils.dpScale(), (float)var4.getHeight() * Utils.dpScale());
         super.boundsMatrix.mapRect(var1);
      }

   }
}
