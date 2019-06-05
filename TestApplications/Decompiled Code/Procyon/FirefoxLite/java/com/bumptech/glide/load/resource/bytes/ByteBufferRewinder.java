// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.bumptech.glide.load.data.DataRewinder;

public class ByteBufferRewinder implements DataRewinder<ByteBuffer>
{
    private final ByteBuffer buffer;
    
    public ByteBufferRewinder(final ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    @Override
    public void cleanup() {
    }
    
    @Override
    public ByteBuffer rewindAndGet() throws IOException {
        this.buffer.position(0);
        return this.buffer;
    }
    
    public static class Factory implements DataRewinder.Factory<ByteBuffer>
    {
        public DataRewinder<ByteBuffer> build(final ByteBuffer byteBuffer) {
            return new ByteBufferRewinder(byteBuffer);
        }
        
        @Override
        public Class<ByteBuffer> getDataClass() {
            return ByteBuffer.class;
        }
    }
}
