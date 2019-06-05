// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.FormatException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.EncodeHintType;
import java.util.Map;
import com.google.zxing.BarcodeFormat;

public final class EAN13Writer extends UPCEANWriter
{
    private static final int CODE_WIDTH = 95;
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat obj, final int n, final int n2, final Map<EncodeHintType, ?> map) throws WriterException {
        if (obj != BarcodeFormat.EAN_13) {
            throw new IllegalArgumentException("Can only encode EAN_13, but got " + obj);
        }
        return super.encode(s, obj, n, n2, map);
    }
    
    @Override
    public boolean[] encode(final String s) {
        if (s.length() != 13) {
            throw new IllegalArgumentException("Requested contents should be 13 digits long, but got " + s.length());
        }
        try {
            if (!UPCEANReader.checkStandardUPCEANChecksum(s)) {
                throw new IllegalArgumentException("Contents do not pass checksum");
            }
        }
        catch (FormatException ex) {
            throw new IllegalArgumentException("Illegal contents");
        }
        final int n = EAN13Reader.FIRST_DIGIT_ENCODINGS[Integer.parseInt(s.substring(0, 1))];
        final boolean[] array = new boolean[95];
        int n2 = OneDimensionalCodeWriter.appendPattern(array, 0, UPCEANReader.START_END_PATTERN, true) + 0;
        for (int i = 1; i <= 6; ++i) {
            int int1 = Integer.parseInt(s.substring(i, i + 1));
            if ((n >> 6 - i & 0x1) == 0x1) {
                int1 += 10;
            }
            n2 += OneDimensionalCodeWriter.appendPattern(array, n2, UPCEANReader.L_AND_G_PATTERNS[int1], false);
        }
        int n3 = n2 + OneDimensionalCodeWriter.appendPattern(array, n2, UPCEANReader.MIDDLE_PATTERN, false);
        for (int j = 7; j <= 12; ++j) {
            n3 += OneDimensionalCodeWriter.appendPattern(array, n3, UPCEANReader.L_PATTERNS[Integer.parseInt(s.substring(j, j + 1))], true);
        }
        OneDimensionalCodeWriter.appendPattern(array, n3, UPCEANReader.START_END_PATTERN, true);
        return array;
    }
}
