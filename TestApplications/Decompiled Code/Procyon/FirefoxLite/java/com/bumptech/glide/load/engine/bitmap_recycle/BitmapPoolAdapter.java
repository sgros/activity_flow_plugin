// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;

public class BitmapPoolAdapter implements BitmapPool
{
    @Override
    public void clearMemory() {
    }
    
    @Override
    public Bitmap get(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return Bitmap.createBitmap(n, n2, bitmap$Config);
    }
    
    @Override
    public Bitmap getDirty(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return this.get(n, n2, bitmap$Config);
    }
    
    @Override
    public void put(final Bitmap bitmap) {
        bitmap.recycle();
    }
    
    @Override
    public void trimMemory(final int n) {
    }
}
