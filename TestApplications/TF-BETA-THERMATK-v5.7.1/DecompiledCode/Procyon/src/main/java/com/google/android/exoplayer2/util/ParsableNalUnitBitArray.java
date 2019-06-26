// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

public final class ParsableNalUnitBitArray
{
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    private byte[] data;
    
    public ParsableNalUnitBitArray(final byte[] array, final int n, final int n2) {
        this.reset(array, n, n2);
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
    
    private int readExpGolombCodeNum() {
        int bits = 0;
        int n = 0;
        while (!this.readBit()) {
            ++n;
        }
        if (n > 0) {
            bits = this.readBits(n);
        }
        return (1 << n) - 1 + bits;
    }
    
    private boolean shouldSkipByte(final int n) {
        boolean b = true;
        if (2 <= n && n < this.byteLimit) {
            final byte[] data = this.data;
            if (data[n] == 3 && data[n - 2] == 0 && data[n - 1] == 0) {
                return b;
            }
        }
        b = false;
        return b;
    }
    
    public boolean canReadBits(int byteLimit) {
        final int byteOffset = this.byteOffset;
        final int n = byteLimit / 8;
        final int n2 = byteOffset + n;
        final int n3 = this.bitOffset + byteLimit - n * 8;
        byteLimit = n2;
        int n4 = n3;
        if (n3 > 7) {
            byteLimit = n2 + 1;
            n4 = n3 - 8;
        }
        final boolean b = true;
        int n5 = byteLimit;
        byteLimit = byteOffset;
        while (true) {
            final int n6 = byteLimit + 1;
            if (n6 > n5 || n5 >= this.byteLimit) {
                break;
            }
            byteLimit = n6;
            if (!this.shouldSkipByte(n6)) {
                continue;
            }
            ++n5;
            byteLimit = n6 + 2;
        }
        byteLimit = this.byteLimit;
        boolean b2 = b;
        if (n5 >= byteLimit) {
            b2 = (n5 == byteLimit && n4 == 0 && b);
        }
        return b2;
    }
    
    public boolean canReadExpGolombCodedNum() {
        final int byteOffset = this.byteOffset;
        final int bitOffset = this.bitOffset;
        final boolean b = false;
        int n = 0;
        while (this.byteOffset < this.byteLimit && !this.readBit()) {
            ++n;
        }
        final boolean b2 = this.byteOffset == this.byteLimit;
        this.byteOffset = byteOffset;
        this.bitOffset = bitOffset;
        boolean b3 = b;
        if (!b2) {
            b3 = b;
            if (this.canReadBits(n * 2 + 1)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public boolean readBit() {
        final boolean b = (this.data[this.byteOffset] & 128 >> this.bitOffset) != 0x0;
        this.skipBit();
        return b;
    }
    
    public int readBits(final int n) {
        this.bitOffset += n;
        int n2 = 0;
        int bitOffset;
        int n3;
        while (true) {
            bitOffset = this.bitOffset;
            n3 = 2;
            if (bitOffset <= 8) {
                break;
            }
            this.bitOffset = bitOffset - 8;
            final byte[] data = this.data;
            final int byteOffset = this.byteOffset;
            n2 |= (data[byteOffset] & 0xFF) << this.bitOffset;
            if (!this.shouldSkipByte(byteOffset + 1)) {
                n3 = 1;
            }
            this.byteOffset = byteOffset + n3;
        }
        final byte[] data2 = this.data;
        final int byteOffset2 = this.byteOffset;
        final byte b = data2[byteOffset2];
        if (bitOffset == 8) {
            this.bitOffset = 0;
            if (!this.shouldSkipByte(byteOffset2 + 1)) {
                n3 = 1;
            }
            this.byteOffset = byteOffset2 + n3;
        }
        this.assertValidOffset();
        return -1 >>> 32 - n & (n2 | (b & 0xFF) >> 8 - bitOffset);
    }
    
    public int readSignedExpGolombCodedInt() {
        final int expGolombCodeNum = this.readExpGolombCodeNum();
        int n;
        if (expGolombCodeNum % 2 == 0) {
            n = -1;
        }
        else {
            n = 1;
        }
        return n * ((expGolombCodeNum + 1) / 2);
    }
    
    public int readUnsignedExpGolombCodedInt() {
        return this.readExpGolombCodeNum();
    }
    
    public void reset(final byte[] data, final int byteOffset, final int byteLimit) {
        this.data = data;
        this.byteOffset = byteOffset;
        this.byteLimit = byteLimit;
        this.bitOffset = 0;
        this.assertValidOffset();
    }
    
    public void skipBit() {
        int bitOffset = this.bitOffset;
        int n = 1;
        ++bitOffset;
        this.bitOffset = bitOffset;
        if (bitOffset == 8) {
            this.bitOffset = 0;
            final int byteOffset = this.byteOffset;
            if (this.shouldSkipByte(byteOffset + 1)) {
                n = 2;
            }
            this.byteOffset = byteOffset + n;
        }
        this.assertValidOffset();
    }
    
    public void skipBits(int n) {
        final int byteOffset = this.byteOffset;
        final int n2 = n / 8;
        this.byteOffset = byteOffset + n2;
        this.bitOffset += n - n2 * 8;
        final int bitOffset = this.bitOffset;
        n = byteOffset;
        if (bitOffset > 7) {
            ++this.byteOffset;
            this.bitOffset = bitOffset - 8;
            n = byteOffset;
        }
        while (true) {
            final int n3 = n + 1;
            if (n3 > this.byteOffset) {
                break;
            }
            n = n3;
            if (!this.shouldSkipByte(n3)) {
                continue;
            }
            ++this.byteOffset;
            n = n3 + 2;
        }
        this.assertValidOffset();
    }
}
