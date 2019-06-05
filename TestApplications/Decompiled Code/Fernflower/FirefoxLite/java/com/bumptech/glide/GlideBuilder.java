package com.bumptech.glide;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.DefaultConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.request.RequestOptions;
import java.util.Map;

public final class GlideBuilder {
   private ArrayPool arrayPool;
   private BitmapPool bitmapPool;
   private ConnectivityMonitorFactory connectivityMonitorFactory;
   private RequestOptions defaultRequestOptions = new RequestOptions();
   private final Map defaultTransitionOptions = new ArrayMap();
   private GlideExecutor diskCacheExecutor;
   private DiskCache.Factory diskCacheFactory;
   private Engine engine;
   private int logLevel = 4;
   private MemoryCache memoryCache;
   private MemorySizeCalculator memorySizeCalculator;
   private RequestManagerRetriever.RequestManagerFactory requestManagerFactory;
   private GlideExecutor sourceExecutor;

   public Glide build(Context var1) {
      if (this.sourceExecutor == null) {
         this.sourceExecutor = GlideExecutor.newSourceExecutor();
      }

      if (this.diskCacheExecutor == null) {
         this.diskCacheExecutor = GlideExecutor.newDiskCacheExecutor();
      }

      if (this.memorySizeCalculator == null) {
         this.memorySizeCalculator = (new MemorySizeCalculator.Builder(var1)).build();
      }

      if (this.connectivityMonitorFactory == null) {
         this.connectivityMonitorFactory = new DefaultConnectivityMonitorFactory();
      }

      if (this.bitmapPool == null) {
         int var2 = this.memorySizeCalculator.getBitmapPoolSize();
         if (var2 > 0) {
            this.bitmapPool = new LruBitmapPool(var2);
         } else {
            this.bitmapPool = new BitmapPoolAdapter();
         }
      }

      if (this.arrayPool == null) {
         this.arrayPool = new LruArrayPool(this.memorySizeCalculator.getArrayPoolSizeInBytes());
      }

      if (this.memoryCache == null) {
         this.memoryCache = new LruResourceCache(this.memorySizeCalculator.getMemoryCacheSize());
      }

      if (this.diskCacheFactory == null) {
         this.diskCacheFactory = new InternalCacheDiskCacheFactory(var1);
      }

      if (this.engine == null) {
         this.engine = new Engine(this.memoryCache, this.diskCacheFactory, this.diskCacheExecutor, this.sourceExecutor, GlideExecutor.newUnlimitedSourceExecutor());
      }

      RequestManagerRetriever var3 = new RequestManagerRetriever(this.requestManagerFactory);
      return new Glide(var1, this.engine, this.memoryCache, this.bitmapPool, this.arrayPool, var3, this.connectivityMonitorFactory, this.logLevel, this.defaultRequestOptions.lock(), this.defaultTransitionOptions);
   }

   GlideBuilder setRequestManagerFactory(RequestManagerRetriever.RequestManagerFactory var1) {
      this.requestManagerFactory = var1;
      return this;
   }
}
