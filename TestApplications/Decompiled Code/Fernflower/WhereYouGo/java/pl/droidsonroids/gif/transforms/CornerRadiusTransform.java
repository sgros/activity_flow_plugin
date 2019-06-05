package pl.droidsonroids.gif.transforms;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.annotation.FloatRange;

public class CornerRadiusTransform implements Transform {
   private float mCornerRadius;
   private final RectF mDstRectF = new RectF();
   private Shader mShader;

   public CornerRadiusTransform(@FloatRange(from = 0.0D) float var1) {
      this.setCornerRadius(var1);
   }

   @FloatRange(
      from = 0.0D
   )
   public float getCornerRadius() {
      return this.mCornerRadius;
   }

   public void onBoundsChange(Rect var1) {
      this.mDstRectF.set(var1);
      this.mShader = null;
   }

   public void onDraw(Canvas var1, Paint var2, Bitmap var3) {
      if (this.mCornerRadius == 0.0F) {
         var1.drawBitmap(var3, (Rect)null, this.mDstRectF, var2);
      } else {
         if (this.mShader == null) {
            this.mShader = new BitmapShader(var3, TileMode.CLAMP, TileMode.CLAMP);
            Matrix var4 = new Matrix();
            var4.setTranslate(this.mDstRectF.left, this.mDstRectF.top);
            var4.preScale(this.mDstRectF.width() / (float)var3.getWidth(), this.mDstRectF.height() / (float)var3.getHeight());
            this.mShader.setLocalMatrix(var4);
         }

         var2.setShader(this.mShader);
         var1.drawRoundRect(this.mDstRectF, this.mCornerRadius, this.mCornerRadius, var2);
      }

   }

   public void setCornerRadius(@FloatRange(from = 0.0D) float var1) {
      var1 = Math.max(0.0F, var1);
      if (var1 != this.mCornerRadius) {
         this.mCornerRadius = var1;
         this.mShader = null;
      }

   }
}
