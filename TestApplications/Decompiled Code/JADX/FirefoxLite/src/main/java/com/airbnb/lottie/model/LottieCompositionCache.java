package com.airbnb.lottie.model;

import android.support.p001v4.util.LruCache;
import com.airbnb.lottie.LottieComposition;

public class LottieCompositionCache {
    private static final LottieCompositionCache INSTANCE = new LottieCompositionCache();
    private final LruCache<String, LottieComposition> cache = new LruCache(10485760);

    public static LottieCompositionCache getInstance() {
        return INSTANCE;
    }

    LottieCompositionCache() {
    }

    public LottieComposition get(String str) {
        return str == null ? null : (LottieComposition) this.cache.get(str);
    }

    public void put(String str, LottieComposition lottieComposition) {
        if (str != null) {
            this.cache.put(str, lottieComposition);
        }
    }
}
