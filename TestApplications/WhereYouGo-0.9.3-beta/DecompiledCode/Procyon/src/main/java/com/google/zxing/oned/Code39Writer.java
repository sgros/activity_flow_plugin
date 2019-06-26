// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.EncodeHintType;
import java.util.Map;
import com.google.zxing.BarcodeFormat;

public final class Code39Writer extends OneDimensionalCodeWriter
{
    private static void toIntArray(final int n, final int[] array) {
        for (int i = 0; i < 9; ++i) {
            int n2;
            if ((n & 1 << 8 - i) == 0x0) {
                n2 = 1;
            }
            else {
                n2 = 2;
            }
            array[i] = n2;
        }
    }
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat obj, final int n, final int n2, final Map<EncodeHintType, ?> map) throws WriterException {
        if (obj != BarcodeFormat.CODE_39) {
            throw new IllegalArgumentException("Can only encode CODE_39, but got " + obj);
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
        int n = length + 25;
        for (int i = 0; i < length; ++i) {
            final int index = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(str.charAt(i));
            if (index < 0) {
                throw new IllegalArgumentException("Bad contents: " + str);
            }
            toIntArray(Code39Reader.CHARACTER_ENCODINGS[index], array);
            for (int j = 0; j < 9; ++j) {
                n += array[j];
            }
        }
        final boolean[] array2 = new boolean[n];
        toIntArray(Code39Reader.ASTERISK_ENCODING, array);
        final int appendPattern = OneDimensionalCodeWriter.appendPattern(array2, 0, array, true);
        final int[] array3 = { 1 };
        int n2 = appendPattern + OneDimensionalCodeWriter.appendPattern(array2, appendPattern, array3, false);
        for (int k = 0; k < length; ++k) {
            toIntArray(Code39Reader.CHARACTER_ENCODINGS["0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(str.charAt(k))], array);
            final int n3 = n2 + OneDimensionalCodeWriter.appendPattern(array2, n2, array, true);
            n2 = n3 + OneDimensionalCodeWriter.appendPattern(array2, n3, array3, false);
        }
        toIntArray(Code39Reader.ASTERISK_ENCODING, array);
        OneDimensionalCodeWriter.appendPattern(array2, n2, array, true);
        return array2;
    }
}
