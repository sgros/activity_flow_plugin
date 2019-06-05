// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned;

import com.google.zxing.WriterException;
import com.google.zxing.EncodeHintType;
import java.util.Map;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;

public final class UPCAWriter implements Writer
{
    private final EAN13Writer subWriter;
    
    public UPCAWriter() {
        this.subWriter = new EAN13Writer();
    }
    
    private static String preencode(final String str) {
        final int length = str.length();
        String string;
        if (length == 11) {
            int n = 0;
            for (int i = 0; i < 11; ++i) {
                final char char1 = str.charAt(i);
                int n2;
                if (i % 2 == 0) {
                    n2 = 3;
                }
                else {
                    n2 = 1;
                }
                n += n2 * (char1 - '0');
            }
            string = str + (1000 - n) % 10;
        }
        else {
            string = str;
            if (length != 12) {
                throw new IllegalArgumentException("Requested contents should be 11 or 12 digits long, but got " + str.length());
            }
        }
        return "0" + string;
    }
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat barcodeFormat, final int n, final int n2) throws WriterException {
        return this.encode(s, barcodeFormat, n, n2, null);
    }
    
    @Override
    public BitMatrix encode(final String s, final BarcodeFormat obj, final int n, final int n2, final Map<EncodeHintType, ?> map) throws WriterException {
        if (obj != BarcodeFormat.UPC_A) {
            throw new IllegalArgumentException("Can only encode UPC-A, but got " + obj);
        }
        return this.subWriter.encode(preencode(s), BarcodeFormat.EAN_13, n, n2, map);
    }
}
