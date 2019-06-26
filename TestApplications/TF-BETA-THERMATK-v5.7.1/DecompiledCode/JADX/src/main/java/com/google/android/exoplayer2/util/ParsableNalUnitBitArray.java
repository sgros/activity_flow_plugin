package com.google.android.exoplayer2.util;

public final class ParsableNalUnitBitArray {
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    private byte[] data;

    public ParsableNalUnitBitArray(byte[] bArr, int i, int i2) {
        reset(bArr, i, i2);
    }

    public void reset(byte[] bArr, int i, int i2) {
        this.data = bArr;
        this.byteOffset = i;
        this.byteLimit = i2;
        this.bitOffset = 0;
        assertValidOffset();
    }

    public void skipBit() {
        int i = 1;
        int i2 = this.bitOffset + 1;
        this.bitOffset = i2;
        if (i2 == 8) {
            this.bitOffset = 0;
            i2 = this.byteOffset;
            if (shouldSkipByte(i2 + 1)) {
                i = 2;
            }
            this.byteOffset = i2 + i;
        }
        assertValidOffset();
    }

    public void skipBits(int i) {
        int i2 = this.byteOffset;
        int i3 = i / 8;
        this.byteOffset = i2 + i3;
        this.bitOffset += i - (i3 * 8);
        i = this.bitOffset;
        if (i > 7) {
            this.byteOffset++;
            this.bitOffset = i - 8;
        }
        while (true) {
            i2++;
            if (i2 > this.byteOffset) {
                assertValidOffset();
                return;
            } else if (shouldSkipByte(i2)) {
                this.byteOffset++;
                i2 += 2;
            }
        }
    }

    public boolean canReadBits(int i) {
        int i2 = this.byteOffset;
        int i3 = i / 8;
        int i4 = i2 + i3;
        int i5 = (this.bitOffset + i) - (i3 * 8);
        if (i5 > 7) {
            i4++;
            i5 -= 8;
        }
        while (true) {
            i2++;
            if (i2 > i4 || i4 >= this.byteLimit) {
                i2 = this.byteLimit;
            } else if (shouldSkipByte(i2)) {
                i4++;
                i2 += 2;
            }
        }
        i2 = this.byteLimit;
        if (i4 < i2) {
            return true;
        }
        if (i4 == i2 && i5 == 0) {
            return true;
        }
        return false;
    }

    public boolean readBit() {
        boolean z = (this.data[this.byteOffset] & (128 >> this.bitOffset)) != 0;
        skipBit();
        return z;
    }

    public int readBits(int i) {
        int i2;
        int i3;
        this.bitOffset += i;
        int i4 = 0;
        while (true) {
            i2 = this.bitOffset;
            i3 = 2;
            if (i2 <= 8) {
                break;
            }
            this.bitOffset = i2 - 8;
            byte[] bArr = this.data;
            int i5 = this.byteOffset;
            i4 |= (bArr[i5] & NalUnitUtil.EXTENDED_SAR) << this.bitOffset;
            if (!shouldSkipByte(i5 + 1)) {
                i3 = 1;
            }
            this.byteOffset = i5 + i3;
        }
        byte[] bArr2 = this.data;
        int i6 = this.byteOffset;
        i = (-1 >>> (32 - i)) & (i4 | ((bArr2[i6] & NalUnitUtil.EXTENDED_SAR) >> (8 - i2)));
        if (i2 == 8) {
            this.bitOffset = 0;
            if (!shouldSkipByte(i6 + 1)) {
                i3 = 1;
            }
            this.byteOffset = i6 + i3;
        }
        assertValidOffset();
        return i;
    }

    public boolean canReadExpGolombCodedNum() {
        int i = this.byteOffset;
        int i2 = this.bitOffset;
        int i3 = 0;
        while (this.byteOffset < this.byteLimit && !readBit()) {
            i3++;
        }
        Object obj = this.byteOffset == this.byteLimit ? 1 : null;
        this.byteOffset = i;
        this.bitOffset = i2;
        if (obj == null && canReadBits((i3 * 2) + 1)) {
            return true;
        }
        return false;
    }

    public int readUnsignedExpGolombCodedInt() {
        return readExpGolombCodeNum();
    }

    public int readSignedExpGolombCodedInt() {
        int readExpGolombCodeNum = readExpGolombCodeNum();
        return (readExpGolombCodeNum % 2 == 0 ? -1 : 1) * ((readExpGolombCodeNum + 1) / 2);
    }

    private int readExpGolombCodeNum() {
        int i = 0;
        int i2 = 0;
        while (!readBit()) {
            i2++;
        }
        int i3 = (1 << i2) - 1;
        if (i2 > 0) {
            i = readBits(i2);
        }
        return i3 + i;
    }

    private boolean shouldSkipByte(int i) {
        if (2 <= i && i < this.byteLimit) {
            byte[] bArr = this.data;
            if (bArr[i] == (byte) 3 && bArr[i - 2] == (byte) 0 && bArr[i - 1] == (byte) 0) {
                return true;
            }
        }
        return false;
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