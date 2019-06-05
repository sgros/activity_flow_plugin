// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.prefill;

import android.os.Looper;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import android.os.Handler;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public final class BitmapPreFiller
{
    private final BitmapPool bitmapPool;
    private final DecodeFormat defaultFormat;
    private final Handler handler;
    private final MemoryCache memoryCache;
    
    public BitmapPreFiller(final MemoryCache memoryCache, final BitmapPool bitmapPool, final DecodeFormat defaultFormat) {
        this.handler = new Handler(Looper.getMainLooper());
        this.memoryCache = memoryCache;
        this.bitmapPool = bitmapPool;
        this.defaultFormat = defaultFormat;
    }
}
