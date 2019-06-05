// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;

public interface BitmapPool
{
    void clearMemory();
    
    Bitmap get(final int p0, final int p1, final Bitmap$Config p2);
    
    Bitmap getDirty(final int p0, final int p1, final Bitmap$Config p2);
    
    void put(final Bitmap p0);
    
    void trimMemory(final int p0);
}
