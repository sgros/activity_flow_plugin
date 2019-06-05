// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import java.security.MessageDigest;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public class FitCenter extends BitmapTransformation
{
    private static final byte[] ID_BYTES;
    
    static {
        ID_BYTES = "com.bumptech.glide.load.resource.bitmap.FitCenter".getBytes(FitCenter.CHARSET);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof FitCenter;
    }
    
    @Override
    public int hashCode() {
        return "com.bumptech.glide.load.resource.bitmap.FitCenter".hashCode();
    }
    
    @Override
    protected Bitmap transform(final BitmapPool bitmapPool, final Bitmap bitmap, final int n, final int n2) {
        return TransformationUtils.fitCenter(bitmapPool, bitmap, n, n2);
    }
    
    @Override
    public void updateDiskCacheKey(final MessageDigest messageDigest) {
        messageDigest.update(FitCenter.ID_BYTES);
    }
}
