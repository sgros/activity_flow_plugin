// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import java.security.MessageDigest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import android.content.Context;
import com.bumptech.glide.util.Preconditions;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.Transformation;

public class BitmapDrawableTransformation implements Transformation<BitmapDrawable>
{
    private final Transformation<Bitmap> wrapped;
    
    public BitmapDrawableTransformation(final Transformation<Bitmap> transformation) {
        this.wrapped = Preconditions.checkNotNull(transformation);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof BitmapDrawableTransformation && this.wrapped.equals(((BitmapDrawableTransformation)o).wrapped);
    }
    
    @Override
    public int hashCode() {
        return this.wrapped.hashCode();
    }
    
    @Override
    public Resource<BitmapDrawable> transform(final Context context, final Resource<BitmapDrawable> resource, final int n, final int n2) {
        final BitmapResource obtain = BitmapResource.obtain(resource.get().getBitmap(), Glide.get(context).getBitmapPool());
        final Resource<Bitmap> transform = this.wrapped.transform(context, obtain, n, n2);
        if (transform.equals(obtain)) {
            return resource;
        }
        return LazyBitmapDrawableResource.obtain(context, transform.get());
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        this.wrapped.updateDiskCacheKey(messageDigest);
    }
}
