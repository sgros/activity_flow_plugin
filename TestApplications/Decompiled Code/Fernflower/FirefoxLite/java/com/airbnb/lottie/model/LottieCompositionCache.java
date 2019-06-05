package com.airbnb.lottie.model;

import android.support.v4.util.LruCache;
import com.airbnb.lottie.LottieComposition;

public class LottieCompositionCache {
   private static final LottieCompositionCache INSTANCE = new LottieCompositionCache();
   private final LruCache cache = new LruCache(10485760);

   LottieCompositionCache() {
   }

   public static LottieCompositionCache getInstance() {
      return INSTANCE;
   }

   public LottieComposition get(String var1) {
      return var1 == null ? null : (LottieComposition)this.cache.get(var1);
   }

   public void put(String var1, LottieComposition var2) {
      if (var1 != null) {
         this.cache.put(var1, var2);
      }
   }
}
