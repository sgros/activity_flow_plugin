// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.util.Assertions;

final class VorbisBitArray
{
    private int bitOffset;
    private final int byteLimit;
    private int byteOffset;
    private final byte[] data;
    
    public VorbisBitArray(final byte[] data) {
        this.data = data;
        this.byteLimit = data.length;
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
    
    public int getPosition() {
        return this.byteOffset * 8 + this.bitOffset;
    }
    
    public boolean readBit() {
        final boolean b = ((this.data[this.byteOffset] & 0xFF) >> this.bitOffset & 0x1) == 0x1;
        this.skipBits(1);
        return b;
    }
    
    public int readBits(final int a) {
        final int byteOffset = this.byteOffset;
        int i = Math.min(a, 8 - this.bitOffset);
        final byte[] data = this.data;
        int n = byteOffset + 1;
        int n2 = (data[byteOffset] & 0xFF) >> this.bitOffset & 255 >> 8 - i;
        while (i < a) {
            n2 |= (this.data[n] & 0xFF) << i;
            i += 8;
            ++n;
        }
        this.skipBits(a);
        return n2 & -1 >>> 32 - a;
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
}
