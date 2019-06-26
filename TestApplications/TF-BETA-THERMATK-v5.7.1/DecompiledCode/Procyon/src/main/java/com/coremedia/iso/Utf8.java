// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso;

import java.io.UnsupportedEncodingException;

public final class Utf8
{
    public static String convert(final byte[] bytes) {
        if (bytes != null) {
            try {
                return new String(bytes, "UTF-8");
            }
            catch (UnsupportedEncodingException cause) {
                throw new Error(cause);
            }
        }
        return null;
    }
    
    public static byte[] convert(final String s) {
        if (s != null) {
            try {
                return s.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException cause) {
                throw new Error(cause);
            }
        }
        return null;
    }
    
    public static int utf8StringLengthInBytes(final String s) {
        if (s != null) {
            try {
                return s.getBytes("UTF-8").length;
            }
            catch (UnsupportedEncodingException ex) {
                throw new RuntimeException();
            }
        }
        return 0;
    }
}
