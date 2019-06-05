// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.EncodeHintType;
import java.util.Map;
import com.google.zxing.BarcodeFormat;

public final class EAN8Writer extends UPCEANWriter
{
    private static final int CODE_WIDTH = 67;
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat obj, final int n, final int n2, final Map<EncodeHintType, ?> map) throws WriterException {
        if (obj != BarcodeFormat.EAN_8) {
            throw new IllegalArgumentException("Can only encode EAN_8, but got " + obj);
        }
        return super.encode(s, obj, n, n2, map);
    }
    
    @Override
    public boolean[] encode(final String s) {
        if (s.length() != 8) {
            throw new IllegalArgumentException("Requested contents should be 8 digits long, but got " + s.length());
        }
        final boolean[] array = new boolean[67];
        int n = OneDimensionalCodeWriter.appendPattern(array, 0, UPCEANReader.START_END_PATTERN, true) + 0;
        for (int i = 0; i <= 3; ++i) {
            n += OneDimensionalCodeWriter.appendPattern(array, n, UPCEANReader.L_PATTERNS[Integer.parseInt(s.substring(i, i + 1))], false);
        }
        int n2 = n + OneDimensionalCodeWriter.appendPattern(array, n, UPCEANReader.MIDDLE_PATTERN, false);
        for (int j = 4; j <= 7; ++j) {
            n2 += OneDimensionalCodeWriter.appendPattern(array, n2, UPCEANReader.L_PATTERNS[Integer.parseInt(s.substring(j, j + 1))], true);
        }
        OneDimensionalCodeWriter.appendPattern(array, n2, UPCEANReader.START_END_PATTERN, true);
        return array;
    }
}
