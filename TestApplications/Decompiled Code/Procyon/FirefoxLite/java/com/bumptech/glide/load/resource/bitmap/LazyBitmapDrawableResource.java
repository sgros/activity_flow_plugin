// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.util.Util;
import com.bumptech.glide.Glide;
import android.content.Context;
import com.bumptech.glide.util.Preconditions;
import android.content.res.Resources;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.Initializable;

public class LazyBitmapDrawableResource implements Initializable, Resource<BitmapDrawable>
{
    private final Bitmap bitmap;
    private final BitmapPool bitmapPool;
    private final Resources resources;
    
    LazyBitmapDrawableResource(final Resources resources, final BitmapPool bitmapPool, final Bitmap bitmap) {
        this.resources = Preconditions.checkNotNull(resources);
        this.bitmapPool = Preconditions.checkNotNull(bitmapPool);
        this.bitmap = Preconditions.checkNotNull(bitmap);
    }
    
    public static LazyBitmapDrawableResource obtain(final Context context, final Bitmap bitmap) {
        return obtain(context.getResources(), Glide.get(context).getBitmapPool(), bitmap);
    }
    
    public static LazyBitmapDrawableResource obtain(final Resources resources, final BitmapPool bitmapPool, final Bitmap bitmap) {
        return new LazyBitmapDrawableResource(resources, bitmapPool, bitmap);
    }
    
    @Override
    public BitmapDrawable get() {
        return new BitmapDrawable(this.resources, this.bitmap);
    }
    
    @Override
    public Class<BitmapDrawable> getResourceClass() {
        return BitmapDrawable.class;
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
