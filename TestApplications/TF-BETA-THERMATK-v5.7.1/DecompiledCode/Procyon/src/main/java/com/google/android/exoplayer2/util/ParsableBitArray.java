// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

public final class ParsableBitArray
{
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    public byte[] data;
    
    public ParsableBitArray() {
        this.data = Util.EMPTY_BYTE_ARRAY;
    }
    
    public ParsableBitArray(final byte[] array) {
        this(array, array.length);
    }
    
    public ParsableBitArray(final byte[] data, final int byteLimit) {
        this.data = data;
        this.byteLimit = byteLimit;
    }
    
    private void assertValidOffset() {
        final int byteOffset = this.byteOffset;
        boolean b = false;
        Label_0038: {
            if (byteOffset >= 0) {
                final int byteLimit = this.byteLimit;
                if (byteOffset < byteLimit || (byteOffset == byteLimit && this.bitOffset == 0)) {
                    b = true;
                    break Label_0038;
                }
            }
            b = false;
        }
        Assertions.checkState(b);
    }
    
    public int bitsLeft() {
        return (this.byteLimit - this.byteOffset) * 8 - this.bitOffset;
    }
    
    public void byteAlign() {
        if (this.bitOffset == 0) {
            return;
        }
        this.bitOffset = 0;
        ++this.byteOffset;
        this.assertValidOffset();
    }
    
    public int getBytePosition() {
        Assertions.checkState(this.bitOffset == 0);
        return this.byteOffset;
    }
    
    public int getPosition() {
        return this.byteOffset * 8 + this.bitOffset;
    }
    
    public void putInt(int n, final int b) {
        int n2 = n;
        if (b < 32) {
            n2 = (n & (1 << b) - 1);
        }
        final int min = Math.min(8 - this.bitOffset, b);
        final int bitOffset = this.bitOffset;
        n = 8 - bitOffset - min;
        final byte[] data = this.data;
        final int byteOffset = this.byteOffset;
        data[byteOffset] &= (byte)(65280 >> bitOffset | (1 << n) - 1);
        int i = b - min;
        data[byteOffset] |= (byte)(n2 >>> i << n);
        for (n = byteOffset + 1; i > 8; i -= 8, ++n) {
            this.data[n] = (byte)(n2 >>> i - 8);
        }
        final int n3 = 8 - i;
        final byte[] data2 = this.data;
        data2[n] &= (byte)((1 << n3) - 1);
        data2[n] |= (byte)((n2 & (1 << i) - 1) << n3);
        this.skipBits(b);
        this.assertValidOffset();
    }
    
    public boolean readBit() {
        final boolean b = (this.data[this.byteOffset] & 128 >> this.bitOffset) != 0x0;
        this.skipBit();
        return b;
    }
    
    public int readBits(final int n) {
        if (n == 0) {
            return 0;
        }
        this.bitOffset += n;
        int n2 = 0;
        int bitOffset;
        while (true) {
            bitOffset = this.bitOffset;
            if (bitOffset <= 8) {
                break;
            }
            this.bitOffset = bitOffset - 8;
            n2 |= (this.data[this.byteOffset++] & 0xFF) << this.bitOffset;
        }
        final byte[] data = this.data;
        final int byteOffset = this.byteOffset;
        final byte b = data[byteOffset];
        if (bitOffset == 8) {
            this.bitOffset = 0;
            this.byteOffset = byteOffset + 1;
        }
        this.assertValidOffset();
        return -1 >>> 32 - n & (n2 | (b & 0xFF) >> 8 - bitOffset);
    }
    
    public void readBits(final byte[] array, int i, int n) {
        int n2;
        for (n2 = (n >> 3) + i; i < n2; ++i) {
            final byte[] data = this.data;
            final byte b = data[this.byteOffset++];
            final int bitOffset = this.bitOffset;
            array[i] = (byte)(b << bitOffset);
            array[i] |= (byte)((0xFF & data[this.byteOffset]) >> 8 - bitOffset);
        }
        i = (n & 0x7);
        if (i == 0) {
            return;
        }
        array[n2] &= (byte)(255 >> i);
        final int bitOffset2 = this.bitOffset;
        if (bitOffset2 + i > 8) {
            n = array[n2];
            array[n2] = (byte)(n | (this.data[this.byteOffset++] & 0xFF) << bitOffset2);
            this.bitOffset = bitOffset2 - 8;
        }
        this.bitOffset += i;
        final byte[] data2 = this.data;
        final int byteOffset = this.byteOffset;
        final byte b2 = data2[byteOffset];
        final int bitOffset3 = this.bitOffset;
        n = array[n2];
        array[n2] = (byte)((byte)((b2 & 0xFF) >> 8 - bitOffset3 << 8 - i) | n);
        if (bitOffset3 == 8) {
            this.bitOffset = 0;
            this.byteOffset = byteOffset + 1;
        }
        this.assertValidOffset();
    }
    
    public void readBytes(final byte[] array, final int n, final int n2) {
        Assertions.checkState(this.bitOffset == 0);
        System.arraycopy(this.data, this.byteOffset, array, n, n2);
        this.byteOffset += n2;
        this.assertValidOffset();
    }
    
    public void reset(final ParsableByteArray parsableByteArray) {
        this.reset(parsableByteArray.data, parsableByteArray.limit());
        this.setPosition(parsableByteArray.getPosition() * 8);
    }
    
    public void reset(final byte[] array) {
        this.reset(array, array.length);
    }
    
    public void reset(final byte[] data, final int byteLimit) {
        this.data = data;
        this.byteOffset = 0;
        this.bitOffset = 0;
        this.byteLimit = byteLimit;
    }
    
    public void setPosition(final int n) {
        this.byteOffset = n / 8;
        this.bitOffset = n - this.byteOffset * 8;
        this.assertValidOffset();
    }
    
    public void skipBit() {
        final int bitOffset = this.bitOffset + 1;
        this.bitOffset = bitOffset;
        if (bitOffset == 8) {
            this.bitOffset = 0;
            ++this.byteOffset;
        }
        this.assertValidOffset();
    }
    
    public void skipBits(int bitOffset) {
        final int n = bitOffset / 8;
        this.byteOffset += n;
        this.bitOffset += bitOffset - n * 8;
        bitOffset = this.bitOffset;
        if (bitOffset > 7) {
            ++this.byteOffset;
            this.bitOffset = bitOffset - 8;
        }
        this.assertValidOffset();
    }
    
    public void skipBytes(final int n) {
        Assertions.checkState(this.bitOffset == 0);
        this.byteOffset += n;
        this.assertValidOffset();
    }
}
