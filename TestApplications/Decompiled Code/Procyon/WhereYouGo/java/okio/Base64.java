// 
// Decompiled by Procyon v0.5.34
// 

package okio;

import java.io.UnsupportedEncodingException;

final class Base64
{
    private static final byte[] MAP;
    private static final byte[] URL_MAP;
    
    static {
        MAP = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        URL_MAP = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
    }
    
    private Base64() {
    }
    
    public static byte[] decode(final String s) {
        int i;
        for (i = s.length(); i > 0; --i) {
            final char char1 = s.charAt(i - 1);
            if (char1 != '=' && char1 != '\n' && char1 != '\r' && char1 != ' ' && char1 != '\t') {
                break;
            }
        }
        final byte[] array = new byte[(int)(i * 6L / 8L)];
        int n = 0;
        int n2 = 0;
        int j = 0;
        int n3 = 0;
    Label_0185_Outer:
        while (j < i) {
            final char char2 = s.charAt(j);
            while (true) {
                int n5 = 0;
                Label_0474: {
                    int n4;
                    if (char2 >= 'A' && char2 <= 'Z') {
                        n4 = char2 - 'A';
                    }
                    else if (char2 >= 'a' && char2 <= 'z') {
                        n4 = char2 - 'G';
                    }
                    else if (char2 >= '0' && char2 <= '9') {
                        n4 = char2 + '\u0004';
                    }
                    else if (char2 == '+' || char2 == '-') {
                        n4 = 62;
                    }
                    else if (char2 == '/' || char2 == '_') {
                        n4 = 63;
                    }
                    else {
                        n5 = n;
                        int n6 = n2;
                        if (char2 == '\n') {
                            break Label_0474;
                        }
                        n5 = n;
                        n6 = n2;
                        if (char2 == '\r') {
                            break Label_0474;
                        }
                        n5 = n;
                        n6 = n2;
                        if (char2 == ' ') {
                            break Label_0474;
                        }
                        if (char2 == '\t') {
                            n6 = n2;
                            break Label_0185;
                        }
                        return null;
                    }
                    final int n7 = n2 << 6 | (byte)n4;
                    n5 = ++n;
                    int n6 = n7;
                    if (n % 4 != 0) {
                        break Label_0474;
                    }
                    final int n8 = n3 + 1;
                    array[n3] = (byte)(n7 >> 16);
                    final int n9 = n8 + 1;
                    array[n8] = (byte)(n7 >> 8);
                    n3 = n9 + 1;
                    array[n9] = (byte)n7;
                    n6 = n7;
                    ++j;
                    n2 = n6;
                    continue Label_0185_Outer;
                }
                n = n5;
                continue;
            }
        }
        final int n10 = n % 4;
        if (n10 == 1) {
            return null;
        }
        int n12;
        if (n10 == 2) {
            final int n11 = n3 + 1;
            array[n3] = (byte)(n2 << 12 >> 16);
            n12 = n11;
        }
        else {
            int n13 = n3;
            if (n10 == 3) {
                final int n14 = n2 << 6;
                final int n15 = n3 + 1;
                array[n3] = (byte)(n14 >> 16);
                n13 = n15 + 1;
                array[n15] = (byte)(n14 >> 8);
            }
            n12 = n13;
        }
        byte[] array2 = array;
        if (n12 != array.length) {
            array2 = new byte[n12];
            System.arraycopy(array, 0, array2, 0, n12);
            return array2;
        }
        return array2;
    }
    
    public static String encode(final byte[] array) {
        return encode(array, Base64.MAP);
    }
    
    private static String encode(final byte[] array, final byte[] array2) {
        final byte[] bytes = new byte[(array.length + 2) / 3 * 4];
        final int n = array.length - array.length % 3;
        int i = 0;
        int n2 = 0;
        while (i < n) {
            final int n3 = n2 + 1;
            bytes[n2] = array2[(array[i] & 0xFF) >> 2];
            final int n4 = n3 + 1;
            bytes[n3] = array2[(array[i] & 0x3) << 4 | (array[i + 1] & 0xFF) >> 4];
            final int n5 = n4 + 1;
            bytes[n4] = array2[(array[i + 1] & 0xF) << 2 | (array[i + 2] & 0xFF) >> 6];
            n2 = n5 + 1;
            bytes[n5] = array2[array[i + 2] & 0x3F];
            i += 3;
        }
        Label_0180: {
            while (true) {
                switch (array.length % 3) {
                    default: {
                        break Label_0180;
                    }
                    case 1: {
                        Label_0193: {
                            break Label_0193;
                            try {
                                return new String(bytes, "US-ASCII");
                                final int n6 = n2 + 1;
                                bytes[n2] = array2[(array[n] & 0xFF) >> 2];
                                final int n7 = n6 + 1;
                                bytes[n6] = array2[(array[n] & 0x3) << 4];
                                final int n8 = n7 + 1;
                                bytes[n7] = 61;
                                bytes[n8] = 61;
                                return new String(bytes, "US-ASCII");
                                final int n9 = n2 + 1;
                                bytes[n2] = array2[(array[n] & 0xFF) >> 2];
                                final int n10 = n9 + 1;
                                bytes[n9] = array2[(array[n] & 0x3) << 4 | (array[n + 1] & 0xFF) >> 4];
                                final int n11 = n10 + 1;
                                bytes[n10] = array2[(array[n + 1] & 0xF) << 2];
                                bytes[n11] = 61;
                                return new String(bytes, "US-ASCII");
                            }
                            catch (UnsupportedEncodingException detailMessage) {
                                throw new AssertionError((Object)detailMessage);
                            }
                        }
                        break;
                    }
                    case 2: {
                        continue;
                    }
                }
                break;
            }
        }
    }
    
    public static String encodeUrl(final byte[] array) {
        return encode(array, Base64.URL_MAP);
    }
}
