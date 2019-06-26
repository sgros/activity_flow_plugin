package com.google.android.exoplayer2.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class ParsableByteArray {
    public byte[] data;
    private int limit;
    private int position;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:22:0x0073 in {6, 8, 9, 10, 15, 17, 19, 21} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public long readUtf8EncodedLong() {
        /*
        r12 = this;
        r0 = r12.data;
        r1 = r12.position;
        r0 = r0[r1];
        r0 = (long) r0;
        r2 = 7;
        r3 = 7;
        r4 = 6;
        r5 = 1;
        if (r3 < 0) goto L_0x0025;
        r6 = r5 << r3;
        r7 = (long) r6;
        r7 = r7 & r0;
        r9 = 0;
        r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r11 != 0) goto L_0x0022;
        if (r3 >= r4) goto L_0x001e;
        r6 = r6 - r5;
        r6 = (long) r6;
        r0 = r0 & r6;
        r2 = r2 - r3;
        goto L_0x0026;
        if (r3 != r2) goto L_0x0025;
        r2 = 1;
        goto L_0x0026;
        r3 = r3 + -1;
        goto L_0x0009;
        r2 = 0;
        if (r2 == 0) goto L_0x005c;
        if (r5 >= r2) goto L_0x0056;
        r3 = r12.data;
        r6 = r12.position;
        r6 = r6 + r5;
        r3 = r3[r6];
        r6 = r3 & 192;
        r7 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r6 != r7) goto L_0x003f;
        r0 = r0 << r4;
        r3 = r3 & 63;
        r6 = (long) r3;
        r0 = r0 | r6;
        r5 = r5 + 1;
        goto L_0x0028;
        r2 = new java.lang.NumberFormatException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Invalid UTF-8 sequence continuation byte: ";
        r3.append(r4);
        r3.append(r0);
        r0 = r3.toString();
        r2.<init>(r0);
        throw r2;
        r3 = r12.position;
        r3 = r3 + r2;
        r12.position = r3;
        return r0;
        r2 = new java.lang.NumberFormatException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Invalid UTF-8 sequence first byte: ";
        r3.append(r4);
        r3.append(r0);
        r0 = r3.toString();
        r2.<init>(r0);
        throw r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.util.ParsableByteArray.readUtf8EncodedLong():long");
    }

    public ParsableByteArray() {
        this.data = Util.EMPTY_BYTE_ARRAY;
    }

    public ParsableByteArray(int i) {
        this.data = new byte[i];
        this.limit = i;
    }

    public ParsableByteArray(byte[] bArr) {
        this.data = bArr;
        this.limit = bArr.length;
    }

    public ParsableByteArray(byte[] bArr, int i) {
        this.data = bArr;
        this.limit = i;
    }

    public void reset() {
        this.position = 0;
        this.limit = 0;
    }

    public void reset(int i) {
        reset(capacity() < i ? new byte[i] : this.data, i);
    }

    public void reset(byte[] bArr) {
        reset(bArr, bArr.length);
    }

    public void reset(byte[] bArr, int i) {
        this.data = bArr;
        this.limit = i;
        this.position = 0;
    }

    public int bytesLeft() {
        return this.limit - this.position;
    }

    public int limit() {
        return this.limit;
    }

    public void setLimit(int i) {
        boolean z = i >= 0 && i <= this.data.length;
        Assertions.checkArgument(z);
        this.limit = i;
    }

    public int getPosition() {
        return this.position;
    }

    public int capacity() {
        return this.data.length;
    }

    public void setPosition(int i) {
        boolean z = i >= 0 && i <= this.limit;
        Assertions.checkArgument(z);
        this.position = i;
    }

    public void skipBytes(int i) {
        setPosition(this.position + i);
    }

    public void readBytes(ParsableBitArray parsableBitArray, int i) {
        readBytes(parsableBitArray.data, 0, i);
        parsableBitArray.setPosition(0);
    }

    public void readBytes(byte[] bArr, int i, int i2) {
        System.arraycopy(this.data, this.position, bArr, i, i2);
        this.position += i2;
    }

    public void readBytes(ByteBuffer byteBuffer, int i) {
        byteBuffer.put(this.data, this.position, i);
        this.position += i;
    }

    public int peekUnsignedByte() {
        return this.data[this.position] & NalUnitUtil.EXTENDED_SAR;
    }

    public char peekChar() {
        byte[] bArr = this.data;
        int i = this.position;
        return (char) ((bArr[i + 1] & NalUnitUtil.EXTENDED_SAR) | ((bArr[i] & NalUnitUtil.EXTENDED_SAR) << 8));
    }

    public int readUnsignedByte() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & NalUnitUtil.EXTENDED_SAR;
    }

    public int readUnsignedShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = (bArr[i] & NalUnitUtil.EXTENDED_SAR) << 8;
        int i2 = this.position;
        this.position = i2 + 1;
        return (bArr[i2] & NalUnitUtil.EXTENDED_SAR) | i;
    }

    public int readLittleEndianUnsignedShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = bArr[i] & NalUnitUtil.EXTENDED_SAR;
        int i2 = this.position;
        this.position = i2 + 1;
        return ((bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 8) | i;
    }

    public short readShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = (bArr[i] & NalUnitUtil.EXTENDED_SAR) << 8;
        int i2 = this.position;
        this.position = i2 + 1;
        return (short) ((bArr[i2] & NalUnitUtil.EXTENDED_SAR) | i);
    }

    public short readLittleEndianShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = bArr[i] & NalUnitUtil.EXTENDED_SAR;
        int i2 = this.position;
        this.position = i2 + 1;
        return (short) (((bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 8) | i);
    }

    public int readUnsignedInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = (bArr[i] & NalUnitUtil.EXTENDED_SAR) << 16;
        int i2 = this.position;
        this.position = i2 + 1;
        i |= (bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        return (bArr[i2] & NalUnitUtil.EXTENDED_SAR) | i;
    }

    public int readInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = ((bArr[i] & NalUnitUtil.EXTENDED_SAR) << 24) >> 8;
        int i2 = this.position;
        this.position = i2 + 1;
        i |= (bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        return (bArr[i2] & NalUnitUtil.EXTENDED_SAR) | i;
    }

    public int readLittleEndianInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = bArr[i] & NalUnitUtil.EXTENDED_SAR;
        int i2 = this.position;
        this.position = i2 + 1;
        i |= (bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        return ((bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 16) | i;
    }

    public int readLittleEndianUnsignedInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = bArr[i] & NalUnitUtil.EXTENDED_SAR;
        int i2 = this.position;
        this.position = i2 + 1;
        i |= (bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        return ((bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 16) | i;
    }

    public long readUnsignedInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        long j = (((long) bArr[i]) & 255) << 24;
        int i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 16;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        return j | (255 & ((long) bArr[i2]));
    }

    public long readLittleEndianUnsignedInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        long j = ((long) bArr[i]) & 255;
        int i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 16;
        i2 = this.position;
        this.position = i2 + 1;
        return j | ((255 & ((long) bArr[i2])) << 24);
    }

    public int readInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = (bArr[i] & NalUnitUtil.EXTENDED_SAR) << 24;
        int i2 = this.position;
        this.position = i2 + 1;
        i |= (bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 16;
        i2 = this.position;
        this.position = i2 + 1;
        i |= (bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        return (bArr[i2] & NalUnitUtil.EXTENDED_SAR) | i;
    }

    public int readLittleEndianInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = bArr[i] & NalUnitUtil.EXTENDED_SAR;
        int i2 = this.position;
        this.position = i2 + 1;
        i |= (bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        i |= (bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 16;
        i2 = this.position;
        this.position = i2 + 1;
        return ((bArr[i2] & NalUnitUtil.EXTENDED_SAR) << 24) | i;
    }

    public long readLong() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        long j = (((long) bArr[i]) & 255) << 56;
        int i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 48;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 40;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 32;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 24;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 16;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        return j | (255 & ((long) bArr[i2]));
    }

    public long readLittleEndianLong() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        long j = ((long) bArr[i]) & 255;
        int i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 8;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 16;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 24;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 32;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 40;
        i2 = this.position;
        this.position = i2 + 1;
        j |= (((long) bArr[i2]) & 255) << 48;
        i2 = this.position;
        this.position = i2 + 1;
        return j | ((255 & ((long) bArr[i2])) << 56);
    }

    public int readUnsignedFixedPoint1616() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        i = (bArr[i] & NalUnitUtil.EXTENDED_SAR) << 8;
        int i2 = this.position;
        this.position = i2 + 1;
        int i3 = (bArr[i2] & NalUnitUtil.EXTENDED_SAR) | i;
        this.position += 2;
        return i3;
    }

    public int readSynchSafeInt() {
        return (((readUnsignedByte() << 21) | (readUnsignedByte() << 14)) | (readUnsignedByte() << 7)) | readUnsignedByte();
    }

    public int readUnsignedIntToInt() {
        int readInt = readInt();
        if (readInt >= 0) {
            return readInt;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Top bit not zero: ");
        stringBuilder.append(readInt);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int readLittleEndianUnsignedIntToInt() {
        int readLittleEndianInt = readLittleEndianInt();
        if (readLittleEndianInt >= 0) {
            return readLittleEndianInt;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Top bit not zero: ");
        stringBuilder.append(readLittleEndianInt);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public long readUnsignedLongToLong() {
        long readLong = readLong();
        if (readLong >= 0) {
            return readLong;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Top bit not zero: ");
        stringBuilder.append(readLong);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public String readString(int i) {
        return readString(i, Charset.forName("UTF-8"));
    }

    public String readString(int i, Charset charset) {
        String str = new String(this.data, this.position, i, charset);
        this.position += i;
        return str;
    }

    public String readNullTerminatedString(int i) {
        if (i == 0) {
            return "";
        }
        int i2 = (this.position + i) - 1;
        i2 = (i2 >= this.limit || this.data[i2] != (byte) 0) ? i : i - 1;
        String fromUtf8Bytes = Util.fromUtf8Bytes(this.data, this.position, i2);
        this.position += i;
        return fromUtf8Bytes;
    }

    public String readNullTerminatedString() {
        if (bytesLeft() == 0) {
            return null;
        }
        int i = this.position;
        while (i < this.limit && this.data[i] != (byte) 0) {
            i++;
        }
        byte[] bArr = this.data;
        int i2 = this.position;
        String fromUtf8Bytes = Util.fromUtf8Bytes(bArr, i2, i - i2);
        this.position = i;
        i = this.position;
        if (i < this.limit) {
            this.position = i + 1;
        }
        return fromUtf8Bytes;
    }

    public String readLine() {
        if (bytesLeft() == 0) {
            return null;
        }
        int i = this.position;
        while (i < this.limit && !Util.isLinebreak(this.data[i])) {
            i++;
        }
        int i2 = this.position;
        if (i - i2 >= 3) {
            byte[] bArr = this.data;
            if (bArr[i2] == (byte) -17 && bArr[i2 + 1] == (byte) -69 && bArr[i2 + 2] == (byte) -65) {
                this.position = i2 + 3;
            }
        }
        byte[] bArr2 = this.data;
        int i3 = this.position;
        String fromUtf8Bytes = Util.fromUtf8Bytes(bArr2, i3, i - i3);
        this.position = i;
        i = this.position;
        i3 = this.limit;
        if (i == i3) {
            return fromUtf8Bytes;
        }
        if (this.data[i] == (byte) 13) {
            this.position = i + 1;
            if (this.position == i3) {
                return fromUtf8Bytes;
            }
        }
        byte[] bArr3 = this.data;
        i3 = this.position;
        if (bArr3[i3] == (byte) 10) {
            this.position = i3 + 1;
        }
        return fromUtf8Bytes;
    }
}
