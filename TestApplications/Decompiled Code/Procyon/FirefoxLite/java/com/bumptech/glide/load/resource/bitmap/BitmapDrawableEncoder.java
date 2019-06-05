// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import java.io.File;
import com.bumptech.glide.load.engine.Resource;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.ResourceEncoder;

public class BitmapDrawableEncoder implements ResourceEncoder<BitmapDrawable>
{
    private final BitmapPool bitmapPool;
    private final ResourceEncoder<Bitmap> encoder;
    
    public BitmapDrawableEncoder(final BitmapPool bitmapPool, final ResourceEncoder<Bitmap> encoder) {
        this.bitmapPool = bitmapPool;
        this.encoder = encoder;
    }
    
    @Override
    public boolean encode(final Resource<BitmapDrawable> resource, final File file, final Options options) {
        return this.encoder.encode(new BitmapResource(resource.get().getBitmap(), this.bitmapPool), file, options);
    }
    
    @Override
    public EncodeStrategy getEncodeStrategy(final Options options) {
        return this.encoder.getEncodeStrategy(options);
    }
}
