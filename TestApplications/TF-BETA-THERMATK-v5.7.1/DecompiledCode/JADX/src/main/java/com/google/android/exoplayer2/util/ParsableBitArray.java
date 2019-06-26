package com.google.android.exoplayer2.util;

public final class ParsableBitArray {
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    public byte[] data;

    public ParsableBitArray() {
        this.data = Util.EMPTY_BYTE_ARRAY;
    }

    public ParsableBitArray(byte[] bArr) {
        this(bArr, bArr.length);
    }

    public ParsableBitArray(byte[] bArr, int i) {
        this.data = bArr;
        this.byteLimit = i;
    }

    public void reset(byte[] bArr) {
        reset(bArr, bArr.length);
    }

    public void reset(ParsableByteArray parsableByteArray) {
        reset(parsableByteArray.data, parsableByteArray.limit());
        setPosition(parsableByteArray.getPosition() * 8);
    }

    public void reset(byte[] bArr, int i) {
        this.data = bArr;
        this.byteOffset = 0;
        this.bitOffset = 0;
        this.byteLimit = i;
    }

    public int bitsLeft() {
        return ((this.byteLimit - this.byteOffset) * 8) - this.bitOffset;
    }

    public int getPosition() {
        return (this.byteOffset * 8) + this.bitOffset;
    }

    public int getBytePosition() {
        Assertions.checkState(this.bitOffset == 0);
        return this.byteOffset;
    }

    public void setPosition(int i) {
        this.byteOffset = i / 8;
        this.bitOffset = i - (this.byteOffset * 8);
        assertValidOffset();
    }

    public void skipBit() {
        int i = this.bitOffset + 1;
        this.bitOffset = i;
        if (i == 8) {
            this.bitOffset = 0;
            this.byteOffset++;
        }
        assertValidOffset();
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

    public boolean readBit() {
        boolean z = (this.data[this.byteOffset] & (128 >> this.bitOffset)) != 0;
        skipBit();
        return z;
    }

    public int readBits(int i) {
        if (i == 0) {
            return 0;
        }
        int i2;
        this.bitOffset += i;
        int i3 = 0;
        while (true) {
            i2 = this.bitOffset;
            if (i2 <= 8) {
                break;
            }
            this.bitOffset = i2 - 8;
            byte[] bArr = this.data;
            int i4 = this.byteOffset;
            this.byteOffset = i4 + 1;
            i3 |= (bArr[i4] & NalUnitUtil.EXTENDED_SAR) << this.bitOffset;
        }
        byte[] bArr2 = this.data;
        int i5 = this.byteOffset;
        i = (-1 >>> (32 - i)) & (i3 | ((bArr2[i5] & NalUnitUtil.EXTENDED_SAR) >> (8 - i2)));
        if (i2 == 8) {
            this.bitOffset = 0;
            this.byteOffset = i5 + 1;
        }
        assertValidOffset();
        return i;
    }

    public void readBits(byte[] bArr, int i, int i2) {
        int i3;
        int i4 = (i2 >> 3) + i;
        while (i < i4) {
            byte[] bArr2 = this.data;
            int i5 = this.byteOffset;
            this.byteOffset = i5 + 1;
            byte b = bArr2[i5];
            i3 = this.bitOffset;
            bArr[i] = (byte) (b << i3);
            bArr[i] = (byte) (((NalUnitUtil.EXTENDED_SAR & bArr2[this.byteOffset]) >> (8 - i3)) | bArr[i]);
            i++;
        }
        i = i2 & 7;
        if (i != 0) {
            bArr[i4] = (byte) (bArr[i4] & (NalUnitUtil.EXTENDED_SAR >> i));
            i2 = this.bitOffset;
            if (i2 + i > 8) {
                byte b2 = bArr[i4];
                byte[] bArr3 = this.data;
                i3 = this.byteOffset;
                this.byteOffset = i3 + 1;
                bArr[i4] = (byte) (b2 | ((bArr3[i3] & NalUnitUtil.EXTENDED_SAR) << i2));
                this.bitOffset = i2 - 8;
            }
            this.bitOffset += i;
            byte[] bArr4 = this.data;
            int i6 = this.byteOffset;
            i2 = bArr4[i6] & NalUnitUtil.EXTENDED_SAR;
            int i7 = this.bitOffset;
            bArr[i4] = (byte) (((byte) ((i2 >> (8 - i7)) << (8 - i))) | bArr[i4]);
            if (i7 == 8) {
                this.bitOffset = 0;
                this.byteOffset = i6 + 1;
            }
            assertValidOffset();
        }
    }

    public void byteAlign() {
        if (this.bitOffset != 0) {
            this.bitOffset = 0;
            this.byteOffset++;
            assertValidOffset();
        }
    }

    public void readBytes(byte[] bArr, int i, int i2) {
        Assertions.checkState(this.bitOffset == 0);
        System.arraycopy(this.data, this.byteOffset, bArr, i, i2);
        this.byteOffset += i2;
        assertValidOffset();
    }

    public void skipBytes(int i) {
        Assertions.checkState(this.bitOffset == 0);
        this.byteOffset += i;
        assertValidOffset();
    }

    public void putInt(int i, int i2) {
        if (i2 < 32) {
            i &= (1 << i2) - 1;
        }
        int min = Math.min(8 - this.bitOffset, i2);
        int i3 = this.bitOffset;
        int i4 = (8 - i3) - min;
        i3 = (65280 >> i3) | ((1 << i4) - 1);
        byte[] bArr = this.data;
        int i5 = this.byteOffset;
        bArr[i5] = (byte) (i3 & bArr[i5]);
        min = i2 - min;
        bArr[i5] = (byte) (((i >>> min) << i4) | bArr[i5]);
        i5++;
        while (min > 8) {
            i4 = i5 + 1;
            this.data[i5] = (byte) (i >>> (min - 8));
            min -= 8;
            i5 = i4;
        }
        int i6 = 8 - min;
        byte[] bArr2 = this.data;
        bArr2[i5] = (byte) (bArr2[i5] & ((1 << i6) - 1));
        bArr2[i5] = (byte) (((i & ((1 << min) - 1)) << i6) | bArr2[i5]);
        skipBits(i2);
        assertValidOffset();
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
