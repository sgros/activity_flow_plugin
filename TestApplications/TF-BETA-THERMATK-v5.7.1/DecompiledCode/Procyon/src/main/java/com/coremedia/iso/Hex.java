// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso;

public class Hex
{
    private static final char[] DIGITS;
    
    static {
        DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
    
    public static String encodeHex(final byte[] array) {
        return encodeHex(array, 0);
    }
    
    public static String encodeHex(final byte[] array, final int n) {
        final int length = array.length;
        int i = 0;
        int n2;
        if (n > 0) {
            n2 = length / n;
        }
        else {
            n2 = 0;
        }
        final char[] value = new char[(length << 1) + n2];
        int n3 = 0;
        while (i < length) {
            int n4 = n3;
            if (n > 0) {
                n4 = n3;
                if (i % n == 0 && (n4 = n3) > 0) {
                    value[n3] = 45;
                    n4 = n3 + 1;
                }
            }
            final int n5 = n4 + 1;
            final char[] digits = Hex.DIGITS;
            value[n4] = digits[(array[i] & 0xF0) >>> 4];
            n3 = n5 + 1;
            value[n5] = digits[array[i] & 0xF];
            ++i;
        }
        return new String(value);
    }
}
