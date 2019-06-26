// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.nio.ByteBuffer;

public class BitWriterBuffer
{
    private ByteBuffer buffer;
    int initialPos;
    int position;
    
    public BitWriterBuffer(final ByteBuffer buffer) {
        this.position = 0;
        this.buffer = buffer;
        this.initialPos = buffer.position();
    }
    
    public void writeBits(int position, int initialPos) {
        final int position2 = this.position;
        final int n = 8 - position2 % 8;
        final int n2 = 1;
        if (initialPos <= n) {
            final byte value = this.buffer.get(this.initialPos + position2 / 8);
            int n3;
            if ((n3 = value) < 0) {
                n3 = value + 256;
            }
            final int n4 = n3 + (position << n - initialPos);
            final ByteBuffer buffer = this.buffer;
            final int initialPos2 = this.initialPos;
            final int n5 = this.position / 8;
            if ((position = n4) > 127) {
                position = n4 - 256;
            }
            buffer.put(initialPos2 + n5, (byte)position);
            this.position += initialPos;
        }
        else {
            initialPos -= n;
            this.writeBits(position >> initialPos, n);
            this.writeBits(position & (1 << initialPos) - 1, initialPos);
        }
        final ByteBuffer buffer2 = this.buffer;
        initialPos = this.initialPos;
        position = this.position;
        final int n6 = position / 8;
        if (position % 8 > 0) {
            position = n2;
        }
        else {
            position = 0;
        }
        buffer2.position(initialPos + n6 + position);
    }
}
