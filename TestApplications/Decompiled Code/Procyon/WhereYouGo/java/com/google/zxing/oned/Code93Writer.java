// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.EncodeHintType;
import java.util.Map;
import com.google.zxing.BarcodeFormat;

public class Code93Writer extends OneDimensionalCodeWriter
{
    protected static int appendPattern(final boolean[] array, int i, final int[] array2, final boolean b) {
        final int length = array2.length;
        final int n = 0;
        int n2;
        for (n2 = i, i = n; i < length; ++i, ++n2) {
            array[n2] = (array2[i] != 0);
        }
        return 9;
    }
    
    private static int computeChecksumIndex(final String s, final int n) {
        int n2 = 1;
        int n3 = 0;
        for (int i = s.length() - 1; i >= 0; --i) {
            n3 += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".indexOf(s.charAt(i)) * n2;
            if (++n2 > n) {
                n2 = 1;
            }
        }
        return n3 % 47;
    }
    
    private static void toIntArray(final int n, final int[] array) {
        for (int i = 0; i < 9; ++i) {
            int n2;
            if ((n & 1 << 8 - i) == 0x0) {
                n2 = 0;
            }
            else {
                n2 = 1;
            }
            array[i] = n2;
        }
    }
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat obj, final int n, final int n2, final Map<EncodeHintType, ?> map) throws WriterException {
        if (obj != BarcodeFormat.CODE_93) {
            throw new IllegalArgumentException("Can only encode CODE_93, but got " + obj);
        }
        return super.encode(s, obj, n, n2, map);
    }
    
    @Override
    public boolean[] encode(final String str) {
        final int length = str.length();
        if (length > 80) {
            throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + length);
        }
        final int[] array = new int[9];
        final boolean[] array2 = new boolean[(str.length() + 2 + 2) * 9 + 1];
        toIntArray(Code93Reader.CHARACTER_ENCODINGS[47], array);
        int appendPattern = appendPattern(array2, 0, array, true);
        for (int i = 0; i < length; ++i) {
            toIntArray(Code93Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".indexOf(str.charAt(i))], array);
            appendPattern += appendPattern(array2, appendPattern, array, true);
        }
        final int computeChecksumIndex = computeChecksumIndex(str, 20);
        toIntArray(Code93Reader.CHARACTER_ENCODINGS[computeChecksumIndex], array);
        final int n = appendPattern + appendPattern(array2, appendPattern, array, true);
        toIntArray(Code93Reader.CHARACTER_ENCODINGS[computeChecksumIndex(str + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".charAt(computeChecksumIndex), 15)], array);
        final int n2 = n + appendPattern(array2, n, array, true);
        toIntArray(Code93Reader.CHARACTER_ENCODINGS[47], array);
        array2[n2 + appendPattern(array2, n2, array, true)] = true;
        return array2;
    }
}
