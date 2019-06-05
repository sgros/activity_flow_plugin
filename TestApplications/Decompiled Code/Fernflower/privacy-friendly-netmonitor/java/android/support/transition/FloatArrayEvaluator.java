package android.support.transition;

import android.animation.TypeEvaluator;

class FloatArrayEvaluator implements TypeEvaluator {
   private float[] mArray;

   FloatArrayEvaluator(float[] var1) {
      this.mArray = var1;
   }

   public float[] evaluate(float var1, float[] var2, float[] var3) {
      float[] var4 = this.mArray;
      float[] var5 = var4;
      if (var4 == null) {
         var5 = new float[var2.length];
      }

      for(int var6 = 0; var6 < var5.length; ++var6) {
         float var7 = var2[var6];
         var5[var6] = var7 + (var3[var6] - var7) * var1;
      }

      return var5;
   }
}
