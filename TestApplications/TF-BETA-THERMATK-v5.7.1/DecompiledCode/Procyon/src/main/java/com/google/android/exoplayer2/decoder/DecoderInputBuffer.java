// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.decoder;

import java.nio.ByteBuffer;

public class DecoderInputBuffer extends Buffer
{
    private final int bufferReplacementMode;
    public final CryptoInfo cryptoInfo;
    public ByteBuffer data;
    public long timeUs;
    
    public DecoderInputBuffer(final int bufferReplacementMode) {
        this.cryptoInfo = new CryptoInfo();
        this.bufferReplacementMode = bufferReplacementMode;
    }
    
    private ByteBuffer createReplacementByteBuffer(final int i) {
        final int bufferReplacementMode = this.bufferReplacementMode;
        if (bufferReplacementMode == 1) {
            return ByteBuffer.allocate(i);
        }
        if (bufferReplacementMode == 2) {
            return ByteBuffer.allocateDirect(i);
        }
        final ByteBuffer data = this.data;
        int capacity;
        if (data == null) {
            capacity = 0;
        }
        else {
            capacity = data.capacity();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Buffer too small (");
        sb.append(capacity);
        sb.append(" < ");
        sb.append(i);
        sb.append(")");
        throw new IllegalStateException(sb.toString());
    }
    
    public static DecoderInputBuffer newFlagsOnlyInstance() {
        return new DecoderInputBuffer(0);
    }
    
    @Override
    public void clear() {
        super.clear();
        final ByteBuffer data = this.data;
        if (data != null) {
            data.clear();
        }
    }
    
    public void ensureSpaceForWrite(int n) {
        final ByteBuffer data = this.data;
        if (data == null) {
            this.data = this.createReplacementByteBuffer(n);
            return;
        }
        final int capacity = data.capacity();
        final int position = this.data.position();
        n += position;
        if (capacity >= n) {
            return;
        }
        final ByteBuffer replacementByteBuffer = this.createReplacementByteBuffer(n);
        if (position > 0) {
            this.data.position(0);
            this.data.limit(position);
            replacementByteBuffer.put(this.data);
        }
        this.data = replacementByteBuffer;
    }
    
    public final void flip() {
        this.data.flip();
    }
    
    public final boolean isEncrypted() {
        return this.getFlag(1073741824);
    }
    
    public final boolean isFlagsOnly() {
        return this.data == null && this.bufferReplacementMode == 0;
    }
}
