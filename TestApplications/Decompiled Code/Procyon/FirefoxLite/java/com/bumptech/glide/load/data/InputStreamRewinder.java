// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data;

import java.io.IOException;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.InputStream;

public final class InputStreamRewinder implements DataRewinder<InputStream>
{
    private final RecyclableBufferedInputStream bufferedStream;
    
    InputStreamRewinder(final InputStream inputStream, final ArrayPool arrayPool) {
        (this.bufferedStream = new RecyclableBufferedInputStream(inputStream, arrayPool)).mark(5242880);
    }
    
    @Override
    public void cleanup() {
        this.bufferedStream.release();
    }
    
    @Override
    public InputStream rewindAndGet() throws IOException {
        this.bufferedStream.reset();
        return this.bufferedStream;
    }
    
    public static final class Factory implements DataRewinder.Factory<InputStream>
    {
        private final ArrayPool byteArrayPool;
        
        public Factory(final ArrayPool byteArrayPool) {
            this.byteArrayPool = byteArrayPool;
        }
        
        public DataRewinder<InputStream> build(final InputStream inputStream) {
            return new InputStreamRewinder(inputStream, this.byteArrayPool);
        }
        
        @Override
        public Class<InputStream> getDataClass() {
            return InputStream.class;
        }
    }
}
