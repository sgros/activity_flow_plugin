// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import java.io.IOException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.Preconditions;
import android.content.res.Resources;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.ResourceDecoder;

public class BitmapDrawableDecoder<DataType> implements ResourceDecoder<DataType, BitmapDrawable>
{
    private final BitmapPool bitmapPool;
    private final ResourceDecoder<DataType, Bitmap> decoder;
    private final Resources resources;
    
    public BitmapDrawableDecoder(final Resources resources, final BitmapPool bitmapPool, final ResourceDecoder<DataType, Bitmap> resourceDecoder) {
        this.resources = Preconditions.checkNotNull(resources);
        this.bitmapPool = Preconditions.checkNotNull(bitmapPool);
        this.decoder = Preconditions.checkNotNull(resourceDecoder);
    }
    
    @Override
    public Resource<BitmapDrawable> decode(final DataType dataType, final int n, final int n2, final Options options) throws IOException {
        final Resource<Bitmap> decode = this.decoder.decode(dataType, n, n2, options);
        if (decode == null) {
            return null;
        }
        return LazyBitmapDrawableResource.obtain(this.resources, this.bitmapPool, decode.get());
    }
    
    @Override
    public boolean handles(final DataType dataType, final Options options) throws IOException {
        return this.decoder.handles(dataType, options);
    }
}
