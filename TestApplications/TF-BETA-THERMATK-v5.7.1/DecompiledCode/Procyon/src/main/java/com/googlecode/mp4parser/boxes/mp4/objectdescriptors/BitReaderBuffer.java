// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.nio.ByteBuffer;

public class BitReaderBuffer
{
    private ByteBuffer buffer;
    int initialPos;
    int position;
    
    public BitReaderBuffer(final ByteBuffer buffer) {
        this.buffer = buffer;
        this.initialPos = buffer.position();
    }
    
    public int readBits(int n) {
        int value;
        final byte b = (byte)(value = this.buffer.get(this.initialPos + this.position / 8));
        if (b < 0) {
            value = b + 256;
        }
        final int position = this.position;
        final int n2 = 8 - position % 8;
        if (n <= n2) {
            final int n3 = (value << position % 8 & 0xFF) >> position % 8 + (n2 - n);
            this.position = position + n;
            n = n3;
        }
        else {
            n -= n2;
            n = (this.readBits(n2) << n) + this.readBits(n);
        }
        final ByteBuffer buffer = this.buffer;
        final int initialPos = this.initialPos;
        final double v = this.position;
        Double.isNaN(v);
        buffer.position(initialPos + (int)Math.ceil(v / 8.0));
        return n;
    }
    
    public boolean readBool() {
        return this.readBits(1) == 1;
    }
    
    public int remainingBits() {
        return this.buffer.limit() * 8 - this.position;
    }
}
