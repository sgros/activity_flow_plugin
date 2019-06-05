// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import android.graphics.Bitmap;
import java.nio.ByteBuffer;
import com.bumptech.glide.load.ResourceDecoder;

public class ByteBufferBitmapDecoder implements ResourceDecoder<ByteBuffer, Bitmap>
{
    private final Downsampler downsampler;
    
    public ByteBufferBitmapDecoder(final Downsampler downsampler) {
        this.downsampler = downsampler;
    }
    
    @Override
    public Resource<Bitmap> decode(final ByteBuffer byteBuffer, final int n, final int n2, final Options options) throws IOException {
        return this.downsampler.decode(ByteBufferUtil.toStream(byteBuffer), n, n2, options);
    }
    
    @Override
    public boolean handles(final ByteBuffer byteBuffer, final Options options) throws IOException {
        return this.downsampler.handles(byteBuffer);
    }
}
