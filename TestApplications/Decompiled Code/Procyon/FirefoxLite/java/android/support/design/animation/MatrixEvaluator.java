// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import android.graphics.Matrix;
import android.animation.TypeEvaluator;

public class MatrixEvaluator implements TypeEvaluator<Matrix>
{
    private final float[] tempEndValues;
    private final Matrix tempMatrix;
    private final float[] tempStartValues;
    
    public MatrixEvaluator() {
        this.tempStartValues = new float[9];
        this.tempEndValues = new float[9];
        this.tempMatrix = new Matrix();
    }
    
    public Matrix evaluate(final float n, final Matrix matrix, final Matrix matrix2) {
        matrix.getValues(this.tempStartValues);
        matrix2.getValues(this.tempEndValues);
        for (int i = 0; i < 9; ++i) {
            this.tempEndValues[i] = this.tempStartValues[i] + (this.tempEndValues[i] - this.tempStartValues[i]) * n;
        }
        this.tempMatrix.setValues(this.tempEndValues);
        return this.tempMatrix;
    }
}
