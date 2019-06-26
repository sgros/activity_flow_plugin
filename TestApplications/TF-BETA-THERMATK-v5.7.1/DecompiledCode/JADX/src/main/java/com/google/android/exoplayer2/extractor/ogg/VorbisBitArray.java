package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;

final class VorbisBitArray {
    private int bitOffset;
    private final int byteLimit;
    private int byteOffset;
    private final byte[] data;

    public VorbisBitArray(byte[] bArr) {
        this.data = bArr;
        this.byteLimit = bArr.length;
    }

    public boolean readBit() {
        boolean z = (((this.data[this.byteOffset] & NalUnitUtil.EXTENDED_SAR) >> this.bitOffset) & 1) == 1;
        skipBits(1);
        return z;
    }

    public int readBits(int i) {
        int i2 = this.byteOffset;
        int min = Math.min(i, 8 - this.bitOffset);
        int i3 = i2 + 1;
        i2 = ((this.data[i2] & NalUnitUtil.EXTENDED_SAR) >> this.bitOffset) & (NalUnitUtil.EXTENDED_SAR >> (8 - min));
        while (min < i) {
            i2 |= (this.data[i3] & NalUnitUtil.EXTENDED_SAR) << min;
            min += 8;
            i3++;
        }
        i2 &= -1 >>> (32 - i);
        skipBits(i);
        return i2;
    }

    public void skipBits(int i) {
        int i2 = i / 8;
        this.byteOffset += i2;
        this.bitOffset += i - (i2 * 8);
        i = this.bitOffset;
        if (i > 7) {
            this.byteOffset++;
            this.bitOffset = i - 8;
        }
        assertValidOffset();
    }

    public int getPosition() {
        return (this.byteOffset * 8) + this.bitOffset;
    }

    private void assertValidOffset() {
        boolean z;
        int i = this.byteOffset;
        if (i >= 0) {
            int i2 = this.byteLimit;
            if (i < i2 || (i == i2 && this.bitOffset == 0)) {
                z = true;
                Assertions.checkState(z);
            }
        }
        z = false;
        Assertions.checkState(z);
    }
}
