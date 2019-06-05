// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import android.content.res.Resources;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;

public class BitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, BitmapDrawable>
{
    private final BitmapPool bitmapPool;
    private final Resources resources;
    
    public BitmapDrawableTranscoder(final Resources resources, final BitmapPool bitmapPool) {
        this.resources = Preconditions.checkNotNull(resources);
        this.bitmapPool = Preconditions.checkNotNull(bitmapPool);
    }
    
    @Override
    public Resource<BitmapDrawable> transcode(final Resource<Bitmap> resource, final Options options) {
        return LazyBitmapDrawableResource.obtain(this.resources, this.bitmapPool, resource.get());
    }
}
