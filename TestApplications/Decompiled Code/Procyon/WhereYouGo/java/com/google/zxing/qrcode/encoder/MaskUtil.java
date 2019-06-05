// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.qrcode.encoder;

final class MaskUtil
{
    private static final int N1 = 3;
    private static final int N2 = 3;
    private static final int N3 = 40;
    private static final int N4 = 10;
    
    private MaskUtil() {
    }
    
    static int applyMaskPenaltyRule1(final ByteMatrix byteMatrix) {
        return applyMaskPenaltyRule1Internal(byteMatrix, true) + applyMaskPenaltyRule1Internal(byteMatrix, false);
    }
    
    private static int applyMaskPenaltyRule1Internal(final ByteMatrix byteMatrix, final boolean b) {
        int n = 0;
        int n2;
        if (b) {
            n2 = byteMatrix.getHeight();
        }
        else {
            n2 = byteMatrix.getWidth();
        }
        int n3;
        if (b) {
            n3 = byteMatrix.getWidth();
        }
        else {
            n3 = byteMatrix.getHeight();
        }
        final byte[][] array = byteMatrix.getArray();
        int n8;
        for (int i = 0; i < n2; ++i, n = n8) {
            int n4 = 0;
            byte b2 = -1;
            int n5;
            int n6;
            for (int j = 0; j < n3; ++j, n4 = n6, n = n5) {
                byte b3;
                if (b) {
                    b3 = array[i][j];
                }
                else {
                    b3 = array[j][i];
                }
                if (b3 == b2) {
                    ++n4;
                    n5 = n;
                    n6 = n4;
                }
                else {
                    int n7 = n;
                    if (n4 >= 5) {
                        n7 = n + (n4 - 5 + 3);
                    }
                    n6 = 1;
                    final byte b4 = b3;
                    n5 = n7;
                    b2 = b4;
                }
            }
            n8 = n;
            if (n4 >= 5) {
                n8 = n + (n4 - 5 + 3);
            }
        }
        return n;
    }
    
    static int applyMaskPenaltyRule2(final ByteMatrix byteMatrix) {
        int n = 0;
        final byte[][] array = byteMatrix.getArray();
        final int width = byteMatrix.getWidth();
        for (int height = byteMatrix.getHeight(), i = 0; i < height - 1; ++i) {
            int n2;
            for (int j = 0; j < width - 1; ++j, n = n2) {
                final byte b = array[i][j];
                n2 = n;
                if (b == array[i][j + 1]) {
                    n2 = n;
                    if (b == array[i + 1][j]) {
                        n2 = n;
                        if (b == array[i + 1][j + 1]) {
                            n2 = n + 1;
                        }
                    }
                }
            }
        }
        return n * 3;
    }
    
    static int applyMaskPenaltyRule3(final ByteMatrix byteMatrix) {
        int n = 0;
        final byte[][] array = byteMatrix.getArray();
        final int width = byteMatrix.getWidth();
        for (int height = byteMatrix.getHeight(), i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                final byte[] array2 = array[i];
                int n2 = n;
                Label_0180: {
                    if (j + 6 < width) {
                        n2 = n;
                        if (array2[j] == 1) {
                            n2 = n;
                            if (array2[j + 1] == 0) {
                                n2 = n;
                                if (array2[j + 2] == 1) {
                                    n2 = n;
                                    if (array2[j + 3] == 1) {
                                        n2 = n;
                                        if (array2[j + 4] == 1) {
                                            n2 = n;
                                            if (array2[j + 5] == 0) {
                                                n2 = n;
                                                if (array2[j + 6] == 1) {
                                                    if (!isWhiteHorizontal(array2, j - 4, j)) {
                                                        n2 = n;
                                                        if (!isWhiteHorizontal(array2, j + 7, j + 11)) {
                                                            break Label_0180;
                                                        }
                                                    }
                                                    n2 = n + 1;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                n = n2;
                if (i + 6 < height) {
                    n = n2;
                    if (array[i][j] == 1) {
                        n = n2;
                        if (array[i + 1][j] == 0) {
                            n = n2;
                            if (array[i + 2][j] == 1) {
                                n = n2;
                                if (array[i + 3][j] == 1) {
                                    n = n2;
                                    if (array[i + 4][j] == 1) {
                                        n = n2;
                                        if (array[i + 5][j] == 0) {
                                            n = n2;
                                            if (array[i + 6][j] == 1) {
                                                if (!isWhiteVertical(array, j, i - 4, i)) {
                                                    n = n2;
                                                    if (!isWhiteVertical(array, j, i + 7, i + 11)) {
                                                        continue;
                                                    }
                                                }
                                                n = n2 + 1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return n * 40;
    }
    
    static int applyMaskPenaltyRule4(final ByteMatrix byteMatrix) {
        int n = 0;
        final byte[][] array = byteMatrix.getArray();
        final int width = byteMatrix.getWidth();
        for (int height = byteMatrix.getHeight(), i = 0; i < height; ++i) {
            final byte[] array2 = array[i];
            int n2;
            for (int j = 0; j < width; ++j, n = n2) {
                n2 = n;
                if (array2[j] == 1) {
                    n2 = n + 1;
                }
            }
        }
        final int n3 = byteMatrix.getHeight() * byteMatrix.getWidth();
        return Math.abs((n << 1) - n3) * 10 / n3 * 10;
    }
    
    static boolean getDataMaskBit(int i, final int n, final int n2) {
        switch (i) {
            default: {
                throw new IllegalArgumentException("Invalid mask pattern: " + i);
            }
            case 0: {
                i = (n2 + n & 0x1);
                break;
            }
            case 1: {
                i = (n2 & 0x1);
                break;
            }
            case 2: {
                i = n % 3;
                break;
            }
            case 3: {
                i = (n2 + n) % 3;
                break;
            }
            case 4: {
                i = (n2 / 2 + n / 3 & 0x1);
                break;
            }
            case 5: {
                i = n2 * n;
                i = (i & 0x1) + i % 3;
                break;
            }
            case 6: {
                i = n2 * n;
                i = ((i & 0x1) + i % 3 & 0x1);
                break;
            }
            case 7: {
                i = (n2 * n % 3 + (n2 + n & 0x1) & 0x1);
                break;
            }
        }
        return i == 0;
    }
    
    private static boolean isWhiteHorizontal(final byte[] array, int i, int min) {
        boolean b = false;
        for (i = Math.max(i, 0), min = Math.min(min, array.length); i < min; ++i) {
            if (array[i] == 1) {
                return b;
            }
        }
        b = true;
        return b;
    }
    
    private static boolean isWhiteVertical(final byte[][] array, final int n, int i, int min) {
        boolean b = false;
        for (i = Math.max(i, 0), min = Math.min(min, array.length); i < min; ++i) {
            if (array[i][n] == 1) {
                return b;
            }
        }
        b = true;
        return b;
    }
}
