package android.support.v4.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator implements Interpolator {
   private final float mStepSize;
   private final float[] mValues;

   protected LookupTableInterpolator(float[] var1) {
      this.mValues = var1;
      this.mStepSize = 1.0F / (float)(this.mValues.length - 1);
   }

   public float getInterpolation(float var1) {
      if (var1 >= 1.0F) {
         return 1.0F;
      } else if (var1 <= 0.0F) {
         return 0.0F;
      } else {
         int var2 = Math.min((int)((float)(this.mValues.length - 1) * var1), this.mValues.length - 2);
         var1 = (var1 - (float)var2 * this.mStepSize) / this.mStepSize;
         return this.mValues[var2] + var1 * (this.mValues[var2 + 1] - this.mValues[var2]);
      }
   }
}
