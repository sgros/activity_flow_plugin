// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.common;

import com.google.zxing.NotFoundException;
import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;

public final class HybridBinarizer extends GlobalHistogramBinarizer
{
    private static final int BLOCK_SIZE = 8;
    private static final int BLOCK_SIZE_MASK = 7;
    private static final int BLOCK_SIZE_POWER = 3;
    private static final int MINIMUM_DIMENSION = 40;
    private static final int MIN_DYNAMIC_RANGE = 24;
    private BitMatrix matrix;
    
    public HybridBinarizer(final LuminanceSource luminanceSource) {
        super(luminanceSource);
    }
    
    private static int[][] calculateBlackPoints(final byte[] array, final int n, final int n2, final int n3, final int n4) {
        final int[][] array2 = new int[n2][n];
        for (int i = 0; i < n2; ++i) {
            final int n5 = i << 3;
            final int n6 = n4 - 8;
            int n7;
            if ((n7 = n5) > n6) {
                n7 = n6;
            }
            for (int j = 0; j < n; ++j) {
                final int n8 = j << 3;
                final int n9 = n3 - 8;
                int n10;
                if ((n10 = n8) > n9) {
                    n10 = n9;
                }
                final int n11 = 0;
                int n12 = 255;
                int n13 = 0;
                int k = 0;
                int n14 = n7 * n3 + n10;
                int n15 = n11;
                while (k < 8) {
                    int n17;
                    int n18;
                    int n19;
                    for (int l = 0; l < 8; ++l, n13 = n19, n12 = n18, n15 = n17) {
                        final int n16 = array[n14 + l] & 0xFF;
                        n17 = n15 + n16;
                        if (n16 < (n18 = n12)) {
                            n18 = n16;
                        }
                        if (n16 > (n19 = n13)) {
                            n19 = n16;
                        }
                    }
                    int n20 = n14;
                    int n21 = n15;
                    int n22 = k;
                    if (n13 - n12 > 24) {
                        ++k;
                        final int n23 = n14 + n3;
                        int n24 = n15;
                        int n25 = n23;
                        while (true) {
                            n20 = n25;
                            n21 = n24;
                            n22 = k;
                            if (k >= 8) {
                                break;
                            }
                            for (int n26 = 0; n26 < 8; ++n26) {
                                n24 += (array[n25 + n26] & 0xFF);
                            }
                            ++k;
                            n25 += n3;
                        }
                    }
                    k = n22 + 1;
                    n14 = n20 + n3;
                    n15 = n21;
                }
                int n27 = n15 >> 6;
                if (n13 - n12 <= 24) {
                    final int n28 = n27 = n12 / 2;
                    if (i > 0) {
                        n27 = n28;
                        if (j > 0) {
                            final int n29 = (array2[i - 1][j] + array2[i][j - 1] * 2 + array2[i - 1][j - 1]) / 4;
                            n27 = n28;
                            if (n12 < n29) {
                                n27 = n29;
                            }
                        }
                    }
                }
                array2[i][j] = n27;
            }
        }
        return array2;
    }
    
    private static void calculateThresholdForBlock(final byte[] array, final int n, final int n2, final int n3, final int n4, final int[][] array2, final BitMatrix bitMatrix) {
        for (int i = 0; i < n2; ++i) {
            final int n5 = i << 3;
            final int n6 = n4 - 8;
            int n7;
            if ((n7 = n5) > n6) {
                n7 = n6;
            }
            for (int j = 0; j < n; ++j) {
                final int n8 = j << 3;
                final int n9 = n3 - 8;
                int n10;
                if ((n10 = n8) > n9) {
                    n10 = n9;
                }
                final int cap = cap(j, 2, n - 3);
                final int cap2 = cap(i, 2, n2 - 3);
                int n11 = 0;
                for (int k = -2; k <= 2; ++k) {
                    final int[] array3 = array2[cap2 + k];
                    n11 += array3[cap - 2] + array3[cap - 1] + array3[cap] + array3[cap + 1] + array3[cap + 2];
                }
                thresholdBlock(array, n10, n7, n11 / 25, n3, bitMatrix);
            }
        }
    }
    
    private static int cap(final int n, int n2, final int n3) {
        if (n >= n2) {
            if (n > n3) {
                n2 = n3;
            }
            else {
                n2 = n;
            }
        }
        return n2;
    }
    
    private static void thresholdBlock(final byte[] array, final int n, final int n2, final int n3, final int n4, final BitMatrix bitMatrix) {
        for (int i = 0, n5 = n2 * n4 + n; i < 8; ++i, n5 += n4) {
            for (int j = 0; j < 8; ++j) {
                if ((array[n5 + j] & 0xFF) <= n3) {
                    bitMatrix.set(n + j, n2 + i);
                }
            }
        }
    }
    
    @Override
    public Binarizer createBinarizer(final LuminanceSource luminanceSource) {
        return new HybridBinarizer(luminanceSource);
    }
    
    @Override
    public BitMatrix getBlackMatrix() throws NotFoundException {
        BitMatrix bitMatrix;
        if (this.matrix != null) {
            bitMatrix = this.matrix;
        }
        else {
            final LuminanceSource luminanceSource = this.getLuminanceSource();
            final int width = luminanceSource.getWidth();
            final int height = luminanceSource.getHeight();
            if (width >= 40 && height >= 40) {
                final byte[] matrix = luminanceSource.getMatrix();
                int n2;
                final int n = n2 = width >> 3;
                if ((width & 0x7) != 0x0) {
                    n2 = n + 1;
                }
                int n4;
                final int n3 = n4 = height >> 3;
                if ((height & 0x7) != 0x0) {
                    n4 = n3 + 1;
                }
                final int[][] calculateBlackPoints = calculateBlackPoints(matrix, n2, n4, width, height);
                final BitMatrix matrix2 = new BitMatrix(width, height);
                calculateThresholdForBlock(matrix, n2, n4, width, height, calculateBlackPoints, matrix2);
                this.matrix = matrix2;
            }
            else {
                this.matrix = super.getBlackMatrix();
            }
            bitMatrix = this.matrix;
        }
        return bitMatrix;
    }
}
