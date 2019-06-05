// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.Initializable;

public class BitmapResource implements Initializable, Resource<Bitmap>
{
    private final Bitmap bitmap;
    private final BitmapPool bitmapPool;
    
    public BitmapResource(final Bitmap bitmap, final BitmapPool bitmapPool) {
        this.bitmap = Preconditions.checkNotNull(bitmap, "Bitmap must not be null");
        this.bitmapPool = Preconditions.checkNotNull(bitmapPool, "BitmapPool must not be null");
    }
    
    public static BitmapResource obtain(final Bitmap bitmap, final BitmapPool bitmapPool) {
        if (bitmap == null) {
            return null;
        }
        return new BitmapResource(bitmap, bitmapPool);
    }
    
    @Override
    public Bitmap get() {
        return this.bitmap;
    }
    
    @Override
    public Class<Bitmap> getResourceClass() {
        return Bitmap.class;
    }
    
    @Override
    public int getSize() {
        return Util.getBitmapByteSize(this.bitmap);
    }
    
    @Override
    public void initialize() {
        this.bitmap.prepareToDraw();
    }
    
    @Override
    public void recycle() {
        this.bitmapPool.put(this.bitmap);
    }
}
