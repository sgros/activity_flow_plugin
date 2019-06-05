// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.EncodeHintType;
import java.util.Map;
import com.google.zxing.BarcodeFormat;

public final class UPCEWriter extends UPCEANWriter
{
    private static final int CODE_WIDTH = 51;
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat obj, final int n, final int n2, final Map<EncodeHintType, ?> map) throws WriterException {
        if (obj != BarcodeFormat.UPC_E) {
            throw new IllegalArgumentException("Can only encode UPC_E, but got " + obj);
        }
        return super.encode(s, obj, n, n2, map);
    }
    
    @Override
    public boolean[] encode(final String s) {
        if (s.length() != 8) {
            throw new IllegalArgumentException("Requested contents should be 8 digits long, but got " + s.length());
        }
        final int n = UPCEReader.CHECK_DIGIT_ENCODINGS[Integer.parseInt(s.substring(7, 8))];
        final boolean[] array = new boolean[51];
        int n2 = OneDimensionalCodeWriter.appendPattern(array, 0, UPCEANReader.START_END_PATTERN, true) + 0;
        for (int i = 1; i <= 6; ++i) {
            int int1 = Integer.parseInt(s.substring(i, i + 1));
            if ((n >> 6 - i & 0x1) == 0x1) {
                int1 += 10;
            }
            n2 += OneDimensionalCodeWriter.appendPattern(array, n2, UPCEANReader.L_AND_G_PATTERNS[int1], false);
        }
        OneDimensionalCodeWriter.appendPattern(array, n2, UPCEANReader.END_PATTERN, false);
        return array;
    }
}
