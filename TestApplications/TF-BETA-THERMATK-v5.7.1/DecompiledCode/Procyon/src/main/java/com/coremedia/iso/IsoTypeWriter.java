// 
// Decompiled by Procyon v0.5.34
// 

package com.coremedia.iso;

import java.nio.ByteBuffer;

public final class IsoTypeWriter
{
    public static void writeFixedPoint0230(final ByteBuffer byteBuffer, final double n) {
        final int n2 = (int)(n * 1.073741824E9);
        byteBuffer.put((byte)((0xFF000000 & n2) >> 24));
        byteBuffer.put((byte)((0xFF0000 & n2) >> 16));
        byteBuffer.put((byte)((0xFF00 & n2) >> 8));
        byteBuffer.put((byte)(n2 & 0xFF));
    }
    
    public static void writeFixedPoint1616(final ByteBuffer byteBuffer, final double n) {
        final int n2 = (int)(n * 65536.0);
        byteBuffer.put((byte)((0xFF000000 & n2) >> 24));
        byteBuffer.put((byte)((0xFF0000 & n2) >> 16));
        byteBuffer.put((byte)((0xFF00 & n2) >> 8));
        byteBuffer.put((byte)(n2 & 0xFF));
    }
    
    public static void writeFixedPoint88(final ByteBuffer byteBuffer, final double n) {
        final short n2 = (short)(n * 256.0);
        byteBuffer.put((byte)((0xFF00 & n2) >> 8));
        byteBuffer.put((byte)(n2 & 0xFF));
    }
    
    public static void writeIso639(final ByteBuffer byteBuffer, final String str) {
        if (str.getBytes().length == 3) {
            int i = 0;
            int n = 0;
            while (i < 3) {
                n += str.getBytes()[i] - 96 << (2 - i) * 5;
                ++i;
            }
            writeUInt16(byteBuffer, n);
            return;
        }
        final StringBuilder sb = new StringBuilder("\"");
        sb.append(str);
        sb.append("\" language string isn't exactly 3 characters long!");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static void writeUInt16(final ByteBuffer byteBuffer, int n) {
        n &= 0xFFFF;
        writeUInt8(byteBuffer, n >> 8);
        writeUInt8(byteBuffer, n & 0xFF);
    }
    
    public static void writeUInt24(final ByteBuffer byteBuffer, int n) {
        n &= 0xFFFFFF;
        writeUInt16(byteBuffer, n >> 8);
        writeUInt8(byteBuffer, n);
    }
    
    public static void writeUInt32(final ByteBuffer byteBuffer, final long n) {
        byteBuffer.putInt((int)n);
    }
    
    public static void writeUInt64(final ByteBuffer byteBuffer, final long n) {
        byteBuffer.putLong(n);
    }
    
    public static void writeUInt8(final ByteBuffer byteBuffer, final int n) {
        byteBuffer.put((byte)(n & 0xFF));
    }
    
    public static void writeUtf8String(final ByteBuffer byteBuffer, final String s) {
        byteBuffer.put(Utf8.convert(s));
        writeUInt8(byteBuffer, 0);
    }
}
