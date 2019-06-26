// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.decoder;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public class SimpleOutputBuffer extends OutputBuffer
{
    public ByteBuffer data;
    private final SimpleDecoder<?, SimpleOutputBuffer, ?> owner;
    
    public SimpleOutputBuffer(final SimpleDecoder<?, SimpleOutputBuffer, ?> owner) {
        this.owner = owner;
    }
    
    @Override
    public void clear() {
        super.clear();
        final ByteBuffer data = this.data;
        if (data != null) {
            data.clear();
        }
    }
    
    public ByteBuffer init(final long timeUs, final int capacity) {
        super.timeUs = timeUs;
        final ByteBuffer data = this.data;
        if (data == null || data.capacity() < capacity) {
            this.data = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        }
        this.data.position(0);
        this.data.limit(capacity);
        return this.data;
    }
    
    @Override
    public void release() {
        this.owner.releaseOutputBuffer(this);
    }
}
