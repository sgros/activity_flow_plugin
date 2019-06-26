// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public final class IsoTypeReader
{
    public static int byte2int(final byte b) {
        int n = b;
        if (b < 0) {
            n = b + 256;
        }
        return n;
    }
    
    public static String read4cc(final ByteBuffer byteBuffer) {
        final byte[] array = new byte[4];
        byteBuffer.get(array);
        try {
            return new String(array, "ISO-8859-1");
        }
        catch (UnsupportedEncodingException cause) {
            throw new RuntimeException(cause);
        }
    }
    
    public static double readFixedPoint0230(final ByteBuffer byteBuffer) {
        final byte[] dst = new byte[4];
        byteBuffer.get(dst);
        final double v = 0x0 | (dst[0] << 24 & 0xFF000000) | (dst[1] << 16 & 0xFF0000) | (dst[2] << 8 & 0xFF00) | (dst[3] & 0xFF);
        Double.isNaN(v);
        return v / 1.073741824E9;
    }
    
    public static double readFixedPoint1616(final ByteBuffer byteBuffer) {
        final byte[] dst = new byte[4];
        byteBuffer.get(dst);
        final double v = 0x0 | (dst[0] << 24 & 0xFF000000) | (dst[1] << 16 & 0xFF0000) | (dst[2] << 8 & 0xFF00) | (dst[3] & 0xFF);
        Double.isNaN(v);
        return v / 65536.0;
    }
    
    public static float readFixedPoint88(final ByteBuffer byteBuffer) {
        final byte[] dst = new byte[2];
        byteBuffer.get(dst);
        return (short)((short)(0x0 | (dst[0] << 8 & 0xFF00)) | (dst[1] & 0xFF)) / 256.0f;
    }
    
    public static String readIso639(final ByteBuffer byteBuffer) {
        final int uInt16 = readUInt16(byteBuffer);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            sb.append((char)((uInt16 >> (2 - i) * 5 & 0x1F) + 96));
        }
        return sb.toString();
    }
    
    public static String readString(final ByteBuffer byteBuffer, final int n) {
        final byte[] dst = new byte[n];
        byteBuffer.get(dst);
        return Utf8.convert(dst);
    }
    
    public static int readUInt16(final ByteBuffer byteBuffer) {
        return (byte2int(byteBuffer.get()) << 8) + 0 + byte2int(byteBuffer.get());
    }
    
    public static int readUInt24(final ByteBuffer byteBuffer) {
        return (readUInt16(byteBuffer) << 8) + 0 + byte2int(byteBuffer.get());
    }
    
    public static long readUInt32(final ByteBuffer byteBuffer) {
        long n2;
        final long n = n2 = byteBuffer.getInt();
        if (n < 0L) {
            n2 = n + 4294967296L;
        }
        return n2;
    }
    
    public static long readUInt64(final ByteBuffer byteBuffer) {
        final long n = (readUInt32(byteBuffer) << 32) + 0L;
        if (n >= 0L) {
            return n + readUInt32(byteBuffer);
        }
        throw new RuntimeException("I don't know how to deal with UInt64! long is not sufficient and I don't want to use BigInt");
    }
    
    public static int readUInt8(final ByteBuffer byteBuffer) {
        return byte2int(byteBuffer.get());
    }
}
