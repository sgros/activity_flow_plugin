// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import android.graphics.Bitmap;
import java.io.InputStream;
import com.bumptech.glide.load.ResourceDecoder;

public class StreamBitmapDecoder implements ResourceDecoder<InputStream, Bitmap>
{
    private final ArrayPool byteArrayPool;
    private final Downsampler downsampler;
    
    public StreamBitmapDecoder(final Downsampler downsampler, final ArrayPool byteArrayPool) {
        this.downsampler = downsampler;
        this.byteArrayPool = byteArrayPool;
    }
    
    @Override
    public Resource<Bitmap> decode(InputStream inputStream, final int n, final int n2, final Options options) throws IOException {
        boolean b;
        if (inputStream instanceof RecyclableBufferedInputStream) {
            inputStream = inputStream;
            b = false;
        }
        else {
            inputStream = new RecyclableBufferedInputStream(inputStream, this.byteArrayPool);
            b = true;
        }
        final ExceptionCatchingInputStream obtain = ExceptionCatchingInputStream.obtain(inputStream);
        final MarkEnforcingInputStream markEnforcingInputStream = new MarkEnforcingInputStream(obtain);
        final UntrustedCallbacks untrustedCallbacks = new UntrustedCallbacks((RecyclableBufferedInputStream)inputStream, obtain);
        try {
            return this.downsampler.decode(markEnforcingInputStream, n, n2, options, (Downsampler.DecodeCallbacks)untrustedCallbacks);
        }
        finally {
            obtain.release();
            if (b) {
                ((RecyclableBufferedInputStream)inputStream).release();
            }
        }
    }
    
    @Override
    public boolean handles(final InputStream inputStream, final Options options) throws IOException {
        return this.downsampler.handles(inputStream);
    }
    
    static class UntrustedCallbacks implements DecodeCallbacks
    {
        private final RecyclableBufferedInputStream bufferedStream;
        private final ExceptionCatchingInputStream exceptionStream;
        
        public UntrustedCallbacks(final RecyclableBufferedInputStream bufferedStream, final ExceptionCatchingInputStream exceptionStream) {
            this.bufferedStream = bufferedStream;
            this.exceptionStream = exceptionStream;
        }
        
        @Override
        public void onDecodeComplete(final BitmapPool bitmapPool, final Bitmap bitmap) throws IOException {
            final IOException exception = this.exceptionStream.getException();
            if (exception != null) {
                if (bitmap != null) {
                    bitmapPool.put(bitmap);
                }
                throw exception;
            }
        }
        
        @Override
        public void onObtainBounds() {
            this.bufferedStream.fixMarkLimit();
        }
    }
}
