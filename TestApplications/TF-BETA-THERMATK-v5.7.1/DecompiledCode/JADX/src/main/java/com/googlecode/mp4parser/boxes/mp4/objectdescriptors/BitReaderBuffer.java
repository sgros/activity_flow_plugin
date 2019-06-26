package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.google.android.exoplayer2.util.NalUnitUtil;
import java.nio.ByteBuffer;

public class BitReaderBuffer {
    private ByteBuffer buffer;
    int initialPos;
    int position;

    public BitReaderBuffer(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
        this.initialPos = byteBuffer.position();
    }

    public boolean readBool() {
        return readBits(1) == 1;
    }

    public int readBits(int i) {
        int i2 = this.buffer.get(this.initialPos + (this.position / 8));
        if (i2 < 0) {
            i2 += 256;
        }
        int i3 = this.position;
        int i4 = 8 - (i3 % 8);
        if (i <= i4) {
            i2 = ((i2 << (i3 % 8)) & NalUnitUtil.EXTENDED_SAR) >> ((i3 % 8) + (i4 - i));
            this.position = i3 + i;
        } else {
            i -= i4;
            i2 = (readBits(i4) << i) + readBits(i);
        }
        ByteBuffer byteBuffer = this.buffer;
        i3 = this.initialPos;
        double d = (double) this.position;
        Double.isNaN(d);
        byteBuffer.position(i3 + ((int) Math.ceil(d / 8.0d)));
        return i2;
    }

    public int remainingBits() {
        return (this.buffer.limit() * 8) - this.position;
    }
}
