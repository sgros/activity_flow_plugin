// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.nio.charset.Charset;
import java.nio.ByteBuffer;

public final class ParsableByteArray
{
    public byte[] data;
    private int limit;
    private int position;
    
    public ParsableByteArray() {
        this.data = Util.EMPTY_BYTE_ARRAY;
    }
    
    public ParsableByteArray(final int limit) {
        this.data = new byte[limit];
        this.limit = limit;
    }
    
    public ParsableByteArray(final byte[] data) {
        this.data = data;
        this.limit = data.length;
    }
    
    public ParsableByteArray(final byte[] data, final int limit) {
        this.data = data;
        this.limit = limit;
    }
    
    public int bytesLeft() {
        return this.limit - this.position;
    }
    
    public int capacity() {
        return this.data.length;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public int limit() {
        return this.limit;
    }
    
    public char peekChar() {
        final byte[] data = this.data;
        final int position = this.position;
        return (char)((data[position + 1] & 0xFF) | (data[position] & 0xFF) << 8);
    }
    
    public int peekUnsignedByte() {
        return this.data[this.position] & 0xFF;
    }
    
    public void readBytes(final ParsableBitArray parsableBitArray, final int n) {
        this.readBytes(parsableBitArray.data, 0, n);
        parsableBitArray.setPosition(0);
    }
    
    public void readBytes(final ByteBuffer byteBuffer, final int length) {
        byteBuffer.put(this.data, this.position, length);
        this.position += length;
    }
    
    public void readBytes(final byte[] array, final int n, final int n2) {
        System.arraycopy(this.data, this.position, array, n, n2);
        this.position += n2;
    }
    
    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }
    
    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }
    
    public int readInt() {
        final byte[] data = this.data;
        return (data[this.position++] & 0xFF) | ((data[this.position++] & 0xFF) << 24 | (data[this.position++] & 0xFF) << 16 | (data[this.position++] & 0xFF) << 8);
    }
    
    public int readInt24() {
        final byte[] data = this.data;
        return (data[this.position++] & 0xFF) | ((data[this.position++] & 0xFF) << 24 >> 8 | (data[this.position++] & 0xFF) << 8);
    }
    
    public String readLine() {
        if (this.bytesLeft() == 0) {
            return null;
        }
        int position;
        for (position = this.position; position < this.limit && !Util.isLinebreak(this.data[position]); ++position) {}
        final int position2 = this.position;
        if (position - position2 >= 3) {
            final byte[] data = this.data;
            if (data[position2] == -17 && data[position2 + 1] == -69 && data[position2 + 2] == -65) {
                this.position = position2 + 3;
            }
        }
        final byte[] data2 = this.data;
        final int position3 = this.position;
        final String fromUtf8Bytes = Util.fromUtf8Bytes(data2, position3, position - position3);
        this.position = position;
        final int position4 = this.position;
        final int limit = this.limit;
        if (position4 == limit) {
            return fromUtf8Bytes;
        }
        if (this.data[position4] == 13) {
            this.position = position4 + 1;
            if (this.position == limit) {
                return fromUtf8Bytes;
            }
        }
        final byte[] data3 = this.data;
        final int position5 = this.position;
        if (data3[position5] == 10) {
            this.position = position5 + 1;
        }
        return fromUtf8Bytes;
    }
    
    public int readLittleEndianInt() {
        final byte[] data = this.data;
        return (data[this.position++] & 0xFF) << 24 | ((data[this.position++] & 0xFF) | (data[this.position++] & 0xFF) << 8 | (data[this.position++] & 0xFF) << 16);
    }
    
    public int readLittleEndianInt24() {
        final byte[] data = this.data;
        return (data[this.position++] & 0xFF) << 16 | ((data[this.position++] & 0xFF) | (data[this.position++] & 0xFF) << 8);
    }
    
    public long readLittleEndianLong() {
        final byte[] data = this.data;
        return ((long)data[this.position++] & 0xFFL) | ((long)data[this.position++] & 0xFFL) << 8 | ((long)data[this.position++] & 0xFFL) << 16 | ((long)data[this.position++] & 0xFFL) << 24 | ((long)data[this.position++] & 0xFFL) << 32 | ((long)data[this.position++] & 0xFFL) << 40 | ((long)data[this.position++] & 0xFFL) << 48 | (0xFFL & (long)data[this.position++]) << 56;
    }
    
    public short readLittleEndianShort() {
        final byte[] data = this.data;
        return (short)((data[this.position++] & 0xFF) << 8 | (data[this.position++] & 0xFF));
    }
    
    public long readLittleEndianUnsignedInt() {
        final byte[] data = this.data;
        return ((long)data[this.position++] & 0xFFL) | ((long)data[this.position++] & 0xFFL) << 8 | ((long)data[this.position++] & 0xFFL) << 16 | (0xFFL & (long)data[this.position++]) << 24;
    }
    
    public int readLittleEndianUnsignedInt24() {
        final byte[] data = this.data;
        return (data[this.position++] & 0xFF) << 16 | ((data[this.position++] & 0xFF) | (data[this.position++] & 0xFF) << 8);
    }
    
    public int readLittleEndianUnsignedIntToInt() {
        final int littleEndianInt = this.readLittleEndianInt();
        if (littleEndianInt >= 0) {
            return littleEndianInt;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Top bit not zero: ");
        sb.append(littleEndianInt);
        throw new IllegalStateException(sb.toString());
    }
    
    public int readLittleEndianUnsignedShort() {
        final byte[] data = this.data;
        return (data[this.position++] & 0xFF) << 8 | (data[this.position++] & 0xFF);
    }
    
    public long readLong() {
        final byte[] data = this.data;
        return ((long)data[this.position++] & 0xFFL) << 56 | ((long)data[this.position++] & 0xFFL) << 48 | ((long)data[this.position++] & 0xFFL) << 40 | ((long)data[this.position++] & 0xFFL) << 32 | ((long)data[this.position++] & 0xFFL) << 24 | ((long)data[this.position++] & 0xFFL) << 16 | ((long)data[this.position++] & 0xFFL) << 8 | (0xFFL & (long)data[this.position++]);
    }
    
    public String readNullTerminatedString() {
        if (this.bytesLeft() == 0) {
            return null;
        }
        int position;
        for (position = this.position; position < this.limit && this.data[position] != 0; ++position) {}
        final byte[] data = this.data;
        final int position2 = this.position;
        final String fromUtf8Bytes = Util.fromUtf8Bytes(data, position2, position - position2);
        this.position = position;
        final int position3 = this.position;
        if (position3 < this.limit) {
            this.position = position3 + 1;
        }
        return fromUtf8Bytes;
    }
    
    public String readNullTerminatedString(final int n) {
        if (n == 0) {
            return "";
        }
        final int n2 = this.position + n - 1;
        int n3;
        if (n2 < this.limit && this.data[n2] == 0) {
            n3 = n - 1;
        }
        else {
            n3 = n;
        }
        final String fromUtf8Bytes = Util.fromUtf8Bytes(this.data, this.position, n3);
        this.position += n;
        return fromUtf8Bytes;
    }
    
    public short readShort() {
        final byte[] data = this.data;
        return (short)((data[this.position++] & 0xFF) | (data[this.position++] & 0xFF) << 8);
    }
    
    public String readString(final int n) {
        return this.readString(n, Charset.forName("UTF-8"));
    }
    
    public String readString(final int length, final Charset charset) {
        final String s = new String(this.data, this.position, length, charset);
        this.position += length;
        return s;
    }
    
    public int readSynchSafeInt() {
        return this.readUnsignedByte() << 21 | this.readUnsignedByte() << 14 | this.readUnsignedByte() << 7 | this.readUnsignedByte();
    }
    
    public int readUnsignedByte() {
        return this.data[this.position++] & 0xFF;
    }
    
    public int readUnsignedFixedPoint1616() {
        final byte[] data = this.data;
        final byte b = data[this.position++];
        final byte b2 = data[this.position++];
        this.position += 2;
        return (b2 & 0xFF) | (b & 0xFF) << 8;
    }
    
    public long readUnsignedInt() {
        final byte[] data = this.data;
        return ((long)data[this.position++] & 0xFFL) << 24 | ((long)data[this.position++] & 0xFFL) << 16 | ((long)data[this.position++] & 0xFFL) << 8 | (0xFFL & (long)data[this.position++]);
    }
    
    public int readUnsignedInt24() {
        final byte[] data = this.data;
        return (data[this.position++] & 0xFF) | ((data[this.position++] & 0xFF) << 16 | (data[this.position++] & 0xFF) << 8);
    }
    
    public int readUnsignedIntToInt() {
        final int int1 = this.readInt();
        if (int1 >= 0) {
            return int1;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Top bit not zero: ");
        sb.append(int1);
        throw new IllegalStateException(sb.toString());
    }
    
    public long readUnsignedLongToLong() {
        final long long1 = this.readLong();
        if (long1 >= 0L) {
            return long1;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Top bit not zero: ");
        sb.append(long1);
        throw new IllegalStateException(sb.toString());
    }
    
    public int readUnsignedShort() {
        final byte[] data = this.data;
        return (data[this.position++] & 0xFF) | (data[this.position++] & 0xFF) << 8;
    }
    
    public long readUtf8EncodedLong() {
        long n = this.data[this.position];
        int n2 = 7;
        int i = 0;
        int n4 = 0;
        Label_0077: {
            while (true) {
                i = 1;
                if (n2 < 0) {
                    break;
                }
                final int n3 = 1 << n2;
                if (((long)n3 & n) == 0x0L) {
                    if (n2 < 6) {
                        n &= n3 - 1;
                        n4 = 7 - n2;
                        break Label_0077;
                    }
                    if (n2 == 7) {
                        n4 = 1;
                        break Label_0077;
                    }
                    break;
                }
                else {
                    --n2;
                }
            }
            n4 = 0;
        }
        if (n4 != 0) {
            while (i < n4) {
                final byte b = this.data[this.position + i];
                if ((b & 0xC0) != 0x80) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid UTF-8 sequence continuation byte: ");
                    sb.append(n);
                    throw new NumberFormatException(sb.toString());
                }
                n = (n << 6 | (long)(b & 0x3F));
                ++i;
            }
            this.position += n4;
            return n;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Invalid UTF-8 sequence first byte: ");
        sb2.append(n);
        throw new NumberFormatException(sb2.toString());
    }
    
    public void reset() {
        this.position = 0;
        this.limit = 0;
    }
    
    public void reset(final int n) {
        byte[] data;
        if (this.capacity() < n) {
            data = new byte[n];
        }
        else {
            data = this.data;
        }
        this.reset(data, n);
    }
    
    public void reset(final byte[] array) {
        this.reset(array, array.length);
    }
    
    public void reset(final byte[] data, final int limit) {
        this.data = data;
        this.limit = limit;
        this.position = 0;
    }
    
    public void setLimit(final int limit) {
        Assertions.checkArgument(limit >= 0 && limit <= this.data.length);
        this.limit = limit;
    }
    
    public void setPosition(final int position) {
        Assertions.checkArgument(position >= 0 && position <= this.limit);
        this.position = position;
    }
    
    public void skipBytes(final int n) {
        this.setPosition(this.position + n);
    }
}
