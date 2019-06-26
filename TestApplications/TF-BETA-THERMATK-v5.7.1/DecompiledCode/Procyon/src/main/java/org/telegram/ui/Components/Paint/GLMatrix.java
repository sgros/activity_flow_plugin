// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.graphics.Matrix;

public class GLMatrix
{
    public static float[] LoadGraphicsMatrix(final Matrix matrix) {
        final float[] array = new float[9];
        matrix.getValues(array);
        return new float[] { array[0], array[1], 0.0f, 0.0f, array[3], array[4], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, array[2], array[5], 0.0f, 1.0f };
    }
    
    public static float[] LoadOrtho(float n, float n2, float n3, final float n4, final float n5, final float n6) {
        final float n7 = n2 - n;
        final float n8 = n4 - n3;
        final float n9 = n6 - n5;
        n = -(n2 + n) / n7;
        n2 = -(n4 + n3) / n8;
        n3 = -(n6 + n5) / n9;
        return new float[] { 2.0f / n7, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f / n8, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f / n9, 0.0f, n, n2, n3, 1.0f };
    }
    
    public static float[] MultiplyMat4f(final float[] array, final float[] array2) {
        return new float[] { array[0] * array2[0] + array[4] * array2[1] + array[8] * array2[2] + array[12] * array2[3], array[1] * array2[0] + array[5] * array2[1] + array[9] * array2[2] + array[13] * array2[3], array[2] * array2[0] + array[6] * array2[1] + array[10] * array2[2] + array[14] * array2[3], array[3] * array2[0] + array[7] * array2[1] + array[11] * array2[2] + array[15] * array2[3], array[0] * array2[4] + array[4] * array2[5] + array[8] * array2[6] + array[12] * array2[7], array[1] * array2[4] + array[5] * array2[5] + array[9] * array2[6] + array[13] * array2[7], array[2] * array2[4] + array[6] * array2[5] + array[10] * array2[6] + array[14] * array2[7], array[3] * array2[4] + array[7] * array2[5] + array[11] * array2[6] + array[15] * array2[7], array[0] * array2[8] + array[4] * array2[9] + array[8] * array2[10] + array[12] * array2[11], array[1] * array2[8] + array[5] * array2[9] + array[9] * array2[10] + array[13] * array2[11], array[2] * array2[8] + array[6] * array2[9] + array[10] * array2[10] + array[14] * array2[11], array[3] * array2[8] + array[7] * array2[9] + array[11] * array2[10] + array[15] * array2[11], array[0] * array2[12] + array[4] * array2[13] + array[8] * array2[14] + array[12] * array2[15], array[1] * array2[12] + array[5] * array2[13] + array[9] * array2[14] + array[13] * array2[15], array[2] * array2[12] + array[6] * array2[13] + array[10] * array2[14] + array[14] * array2[15], array[3] * array2[12] + array[7] * array2[13] + array[11] * array2[14] + array[15] * array2[15] };
    }
}
