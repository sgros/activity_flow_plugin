// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

import com.airbnb.lottie.LottieComposition;
import android.support.v4.util.LruCache;

public class LottieCompositionCache
{
    private static final LottieCompositionCache INSTANCE;
    private final LruCache<String, LottieComposition> cache;
    
    static {
        INSTANCE = new LottieCompositionCache();
    }
    
    LottieCompositionCache() {
        this.cache = new LruCache<String, LottieComposition>(10485760);
    }
    
    public static LottieCompositionCache getInstance() {
        return LottieCompositionCache.INSTANCE;
    }
    
    public LottieComposition get(final String s) {
        if (s == null) {
            return null;
        }
        return this.cache.get(s);
    }
    
    public void put(final String s, final LottieComposition lottieComposition) {
        if (s == null) {
            return;
        }
        this.cache.put(s, lottieComposition);
    }
}
