package android.support.design.animation;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

public class MatrixEvaluator implements TypeEvaluator {
   private final float[] tempEndValues = new float[9];
   private final Matrix tempMatrix = new Matrix();
   private final float[] tempStartValues = new float[9];

   public Matrix evaluate(float var1, Matrix var2, Matrix var3) {
      var2.getValues(this.tempStartValues);
      var3.getValues(this.tempEndValues);

      for(int var4 = 0; var4 < 9; ++var4) {
         float var5 = this.tempEndValues[var4];
         float var6 = this.tempStartValues[var4];
         this.tempEndValues[var4] = this.tempStartValues[var4] + (var5 - var6) * var1;
      }

      this.tempMatrix.setValues(this.tempEndValues);
      return this.tempMatrix;
   }
}
