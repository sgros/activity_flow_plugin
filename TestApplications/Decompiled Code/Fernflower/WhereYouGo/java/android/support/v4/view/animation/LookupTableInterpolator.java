package android.support.v4.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator implements Interpolator {
   private final float mStepSize;
   private final float[] mValues;

   public LookupTableInterpolator(float[] var1) {
      this.mValues = var1;
      this.mStepSize = 1.0F / (float)(this.mValues.length - 1);
   }

   public float getInterpolation(float var1) {
      float var2 = 1.0F;
      if (var1 >= 1.0F) {
         var1 = var2;
      } else if (var1 <= 0.0F) {
         var1 = 0.0F;
      } else {
         int var3 = Math.min((int)((float)(this.mValues.length - 1) * var1), this.mValues.length - 2);
         var1 = (var1 - (float)var3 * this.mStepSize) / this.mStepSize;
         var1 = this.mValues[var3] + (this.mValues[var3 + 1] - this.mValues[var3]) * var1;
      }

      return var1;
   }
}
