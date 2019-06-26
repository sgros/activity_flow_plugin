package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.nio.ByteBuffer;

public class BitWriterBuffer {
    private ByteBuffer buffer;
    int initialPos;
    int position = 0;

    public BitWriterBuffer(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
        this.initialPos = byteBuffer.position();
    }

    public void writeBits(int i, int i2) {
        ByteBuffer byteBuffer;
        int i3 = this.position;
        int i4 = 8 - (i3 % 8);
        int i5 = 1;
        if (i2 <= i4) {
            i3 = this.buffer.get(this.initialPos + (i3 / 8));
            if (i3 < 0) {
                i3 += 256;
            }
            i3 += i << (i4 - i2);
            byteBuffer = this.buffer;
            i4 = this.initialPos + (this.position / 8);
            if (i3 > 127) {
                i3 -= 256;
            }
            byteBuffer.put(i4, (byte) i3);
            this.position += i2;
        } else {
            i2 -= i4;
            writeBits(i >> i2, i4);
            writeBits(i & ((1 << i2) - 1), i2);
        }
        byteBuffer = this.buffer;
        i2 = this.initialPos;
        i3 = this.position;
        i2 += i3 / 8;
        if (i3 % 8 <= 0) {
            i5 = 0;
        }
        byteBuffer.position(i2 + i5);
    }
}
