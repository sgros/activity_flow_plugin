// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.pdf417.detector;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.zxing.NotFoundException;
import java.util.List;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.DecodeHintType;
import java.util.Map;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ResultPoint;

public final class Detector
{
    private static final int BARCODE_MIN_HEIGHT = 10;
    private static final int[] INDEXES_START_PATTERN;
    private static final int[] INDEXES_STOP_PATTERN;
    private static final float MAX_AVG_VARIANCE = 0.42f;
    private static final float MAX_INDIVIDUAL_VARIANCE = 0.8f;
    private static final int MAX_PATTERN_DRIFT = 5;
    private static final int MAX_PIXEL_DRIFT = 3;
    private static final int ROW_STEP = 5;
    private static final int SKIPPED_ROW_COUNT_MAX = 25;
    private static final int[] START_PATTERN;
    private static final int[] STOP_PATTERN;
    
    static {
        INDEXES_START_PATTERN = new int[] { 0, 4, 1, 5 };
        INDEXES_STOP_PATTERN = new int[] { 6, 2, 7, 3 };
        START_PATTERN = new int[] { 8, 1, 1, 1, 1, 1, 1, 3 };
        STOP_PATTERN = new int[] { 7, 1, 1, 3, 1, 1, 1, 2, 1 };
    }
    
    private Detector() {
    }
    
    private static void copyToResult(final ResultPoint[] array, final ResultPoint[] array2, final int[] array3) {
        for (int i = 0; i < array3.length; ++i) {
            array[array3[i]] = array2[i];
        }
    }
    
    public static PDF417DetectorResult detect(final BinaryBitmap binaryBitmap, final Map<DecodeHintType, ?> map, final boolean b) throws NotFoundException {
        final BitMatrix blackMatrix = binaryBitmap.getBlackMatrix();
        List<ResultPoint[]> list = detect(b, blackMatrix);
        BitMatrix clone = blackMatrix;
        if (list.isEmpty()) {
            clone = blackMatrix.clone();
            clone.rotate180();
            list = detect(b, clone);
        }
        return new PDF417DetectorResult(clone, list);
    }
    
    private static List<ResultPoint[]> detect(final boolean b, final BitMatrix bitMatrix) {
        final ArrayList<ResultPoint[]> list = new ArrayList<ResultPoint[]>();
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < bitMatrix.getHeight()) {
            final ResultPoint[] vertices = findVertices(bitMatrix, i, n);
            if (vertices[0] == null && vertices[3] == null) {
                if (n2 == 0) {
                    break;
                }
                n2 = 0;
                final int n3 = 0;
                for (final ResultPoint[] array : list) {
                    int a = i;
                    if (array[1] != null) {
                        a = (int)Math.max((float)i, array[1].getY());
                    }
                    i = a;
                    if (array[3] != null) {
                        i = Math.max(a, (int)array[3].getY());
                    }
                }
                i += 5;
                n = n3;
            }
            else {
                n2 = 1;
                list.add(vertices);
                if (!b) {
                    break;
                }
                if (vertices[2] != null) {
                    n = (int)vertices[2].getX();
                    i = (int)vertices[2].getY();
                }
                else {
                    n = (int)vertices[4].getX();
                    i = (int)vertices[4].getY();
                }
            }
        }
        return list;
    }
    
    private static int[] findGuardPattern(final BitMatrix bitMatrix, int n, final int n2, final int n3, boolean b, final int[] array, final int[] a) {
        Arrays.fill(a, 0, a.length, 0);
        for (int n4 = 0; bitMatrix.get(n, n2) && n > 0 && n4 < 3; --n, ++n4) {}
        int i = n;
        int n5 = 0;
        final int length = array.length;
        while (i < n3) {
            if (bitMatrix.get(i, n2) ^ b) {
                ++a[n5];
            }
            else {
                if (n5 == length - 1) {
                    if (patternMatchVariance(a, array, 0.8f) < 0.42f) {
                        return new int[] { n, i };
                    }
                    n += a[0] + a[1];
                    System.arraycopy(a, 2, a, 0, length - 2);
                    a[length - 1] = (a[length - 2] = 0);
                    --n5;
                }
                else {
                    ++n5;
                }
                a[n5] = 1;
                if (!b) {
                    b = true;
                }
                else {
                    b = false;
                }
            }
            ++i;
        }
        if (n5 == length - 1 && patternMatchVariance(a, array, 0.8f) < 0.42f) {
            return new int[] { n, i - 1 };
        }
        return null;
    }
    
    private static ResultPoint[] findRowsWithPattern(final BitMatrix bitMatrix, int i, final int n, int n2, int j, final int[] array) {
        final ResultPoint[] array2 = new ResultPoint[4];
        final int n3 = 0;
        final int[] array3 = new int[array.length];
        int n4;
        int n5;
        while (true) {
            n4 = n3;
            n5 = n2;
            if (n2 >= i) {
                break;
            }
            int[] guardPattern = findGuardPattern(bitMatrix, j, n2, n, false, array, array3);
            if (guardPattern != null) {
                int n6 = n2;
                while (true) {
                    n2 = n6;
                    if (n6 <= 0) {
                        break;
                    }
                    --n6;
                    final int[] guardPattern2 = findGuardPattern(bitMatrix, j, n6, n, false, array, array3);
                    if (guardPattern2 == null) {
                        n2 = n6 + 1;
                        break;
                    }
                    guardPattern = guardPattern2;
                }
                array2[0] = new ResultPoint((float)guardPattern[0], (float)n2);
                array2[1] = new ResultPoint((float)guardPattern[1], (float)n2);
                n4 = 1;
                n5 = n2;
                break;
            }
            n2 += 5;
        }
        n2 = (j = n5 + 1);
        if (n4 != 0) {
            int n7 = 0;
            int[] array4 = { (int)array2[0].getX(), (int)array2[1].getX() };
            int[] guardPattern3;
            for (j = n2; j < i; ++j, n7 = n2) {
                guardPattern3 = findGuardPattern(bitMatrix, array4[0], j, n, false, array, array3);
                if (guardPattern3 != null && Math.abs(array4[0] - guardPattern3[0]) < 5 && Math.abs(array4[1] - guardPattern3[1]) < 5) {
                    array4 = guardPattern3;
                    n2 = 0;
                }
                else {
                    if (n7 > 25) {
                        break;
                    }
                    n2 = n7 + 1;
                }
            }
            j -= n7 + 1;
            array2[2] = new ResultPoint((float)array4[0], (float)j);
            array2[3] = new ResultPoint((float)array4[1], (float)j);
        }
        if (j - n5 < 10) {
            for (i = 0; i < 4; ++i) {
                array2[i] = null;
            }
        }
        return array2;
    }
    
    private static ResultPoint[] findVertices(final BitMatrix bitMatrix, int n, int n2) {
        final int height = bitMatrix.getHeight();
        final int width = bitMatrix.getWidth();
        final ResultPoint[] array = new ResultPoint[8];
        copyToResult(array, findRowsWithPattern(bitMatrix, height, width, n, n2, Detector.START_PATTERN), Detector.INDEXES_START_PATTERN);
        if (array[4] != null) {
            n2 = (int)array[4].getX();
            n = (int)array[4].getY();
        }
        copyToResult(array, findRowsWithPattern(bitMatrix, height, width, n, n2, Detector.STOP_PATTERN), Detector.INDEXES_STOP_PATTERN);
        return array;
    }
    
    private static float patternMatchVariance(final int[] array, final int[] array2, final float n) {
        final float n2 = Float.POSITIVE_INFINITY;
        final int length = array.length;
        int n3 = 0;
        int n4 = 0;
        for (int i = 0; i < length; ++i) {
            n3 += array[i];
            n4 += array2[i];
        }
        float n5;
        if (n3 < n4) {
            n5 = n2;
        }
        else {
            final float n6 = n3 / (float)n4;
            float n7 = 0.0f;
            for (int j = 0; j < length; ++j) {
                final int n8 = array[j];
                final float n9 = array2[j] * n6;
                float n10;
                if (n8 > n9) {
                    n10 = n8 - n9;
                }
                else {
                    n10 = n9 - n8;
                }
                n5 = n2;
                if (n10 > n * n6) {
                    return n5;
                }
                n7 += n10;
            }
            n5 = n7 / n3;
        }
        return n5;
    }
}
