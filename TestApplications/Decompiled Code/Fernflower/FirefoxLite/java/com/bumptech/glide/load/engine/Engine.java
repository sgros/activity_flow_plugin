package com.bumptech.glide.load.engine;

import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.support.v4.util.Pools;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class Engine implements EngineJobListener, EngineResource.ResourceListener, MemoryCache.ResourceRemovedListener {
   private final Map activeResources;
   private final MemoryCache cache;
   private final Engine.DecodeJobFactory decodeJobFactory;
   private final Engine.LazyDiskCacheProvider diskCacheProvider;
   private final Engine.EngineJobFactory engineJobFactory;
   private final Map jobs;
   private final EngineKeyFactory keyFactory;
   private final ResourceRecycler resourceRecycler;
   private ReferenceQueue resourceReferenceQueue;

   public Engine(MemoryCache var1, DiskCache.Factory var2, GlideExecutor var3, GlideExecutor var4, GlideExecutor var5) {
      this(var1, var2, var3, var4, var5, (Map)null, (EngineKeyFactory)null, (Map)null, (Engine.EngineJobFactory)null, (Engine.DecodeJobFactory)null, (ResourceRecycler)null);
   }

   Engine(MemoryCache var1, DiskCache.Factory var2, GlideExecutor var3, GlideExecutor var4, GlideExecutor var5, Map var6, EngineKeyFactory var7, Map var8, Engine.EngineJobFactory var9, Engine.DecodeJobFactory var10, ResourceRecycler var11) {
      this.cache = var1;
      this.diskCacheProvider = new Engine.LazyDiskCacheProvider(var2);
      Object var12 = var8;
      if (var8 == null) {
         var12 = new HashMap();
      }

      this.activeResources = (Map)var12;
      EngineKeyFactory var13 = var7;
      if (var7 == null) {
         var13 = new EngineKeyFactory();
      }

      this.keyFactory = var13;
      var12 = var6;
      if (var6 == null) {
         var12 = new HashMap();
      }

      this.jobs = (Map)var12;
      Engine.EngineJobFactory var14 = var9;
      if (var9 == null) {
         var14 = new Engine.EngineJobFactory(var3, var4, var5, this);
      }

      this.engineJobFactory = var14;
      Engine.DecodeJobFactory var15 = var10;
      if (var10 == null) {
         var15 = new Engine.DecodeJobFactory(this.diskCacheProvider);
      }

      this.decodeJobFactory = var15;
      ResourceRecycler var16 = var11;
      if (var11 == null) {
         var16 = new ResourceRecycler();
      }

      this.resourceRecycler = var16;
      var1.setResourceRemovedListener(this);
   }

   private EngineResource getEngineResourceFromCache(Key var1) {
      Resource var2 = this.cache.remove(var1);
      EngineResource var3;
      if (var2 == null) {
         var3 = null;
      } else if (var2 instanceof EngineResource) {
         var3 = (EngineResource)var2;
      } else {
         var3 = new EngineResource(var2, true);
      }

      return var3;
   }

   private ReferenceQueue getReferenceQueue() {
      if (this.resourceReferenceQueue == null) {
         this.resourceReferenceQueue = new ReferenceQueue();
         Looper.myQueue().addIdleHandler(new Engine.RefQueueIdleHandler(this.activeResources, this.resourceReferenceQueue));
      }

      return this.resourceReferenceQueue;
   }

   private EngineResource loadFromActiveResources(Key var1, boolean var2) {
      EngineResource var3 = null;
      if (!var2) {
         return null;
      } else {
         WeakReference var4 = (WeakReference)this.activeResources.get(var1);
         if (var4 != null) {
            var3 = (EngineResource)var4.get();
            if (var3 != null) {
               var3.acquire();
            } else {
               this.activeResources.remove(var1);
            }
         }

         return var3;
      }
   }

   private EngineResource loadFromCache(Key var1, boolean var2) {
      if (!var2) {
         return null;
      } else {
         EngineResource var3 = this.getEngineResourceFromCache(var1);
         if (var3 != null) {
            var3.acquire();
            this.activeResources.put(var1, new Engine.ResourceWeakReference(var1, var3, this.getReferenceQueue()));
         }

         return var3;
      }
   }

   private static void logWithTimeAndKey(String var0, long var1, Key var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var0);
      var4.append(" in ");
      var4.append(LogTime.getElapsedMillis(var1));
      var4.append("ms, key: ");
      var4.append(var3);
      Log.v("Engine", var4.toString());
   }

   public Engine.LoadStatus load(GlideContext var1, Object var2, Key var3, int var4, int var5, Class var6, Class var7, Priority var8, DiskCacheStrategy var9, Map var10, boolean var11, boolean var12, Options var13, boolean var14, boolean var15, boolean var16, ResourceCallback var17) {
      Util.assertMainThread();
      long var18 = LogTime.getLogTime();
      EngineKey var20 = this.keyFactory.buildKey(var2, var3, var4, var5, var10, var6, var7, var13);
      EngineResource var21 = this.loadFromCache(var20, var14);
      if (var21 != null) {
         var17.onResourceReady(var21, DataSource.MEMORY_CACHE);
         if (Log.isLoggable("Engine", 2)) {
            logWithTimeAndKey("Loaded resource from cache", var18, var20);
         }

         return null;
      } else {
         var21 = this.loadFromActiveResources(var20, var14);
         if (var21 != null) {
            var17.onResourceReady(var21, DataSource.MEMORY_CACHE);
            if (Log.isLoggable("Engine", 2)) {
               logWithTimeAndKey("Loaded resource from active resources", var18, var20);
            }

            return null;
         } else {
            EngineJob var22 = (EngineJob)this.jobs.get(var20);
            if (var22 != null) {
               var22.addCallback(var17);
               if (Log.isLoggable("Engine", 2)) {
                  logWithTimeAndKey("Added to existing load", var18, var20);
               }

               return new Engine.LoadStatus(var17, var22);
            } else {
               var22 = this.engineJobFactory.build(var20, var14, var15);
               DecodeJob var23 = this.decodeJobFactory.build(var1, var2, var20, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var16, var13, var22);
               this.jobs.put(var20, var22);
               var22.addCallback(var17);
               var22.start(var23);
               if (Log.isLoggable("Engine", 2)) {
                  logWithTimeAndKey("Started new load", var18, var20);
               }

               return new Engine.LoadStatus(var17, var22);
            }
         }
      }
   }

   public void onEngineJobCancelled(EngineJob var1, Key var2) {
      Util.assertMainThread();
      if (var1.equals((EngineJob)this.jobs.get(var2))) {
         this.jobs.remove(var2);
      }

   }

   public void onEngineJobComplete(Key var1, EngineResource var2) {
      Util.assertMainThread();
      if (var2 != null) {
         var2.setResourceListener(var1, this);
         if (var2.isCacheable()) {
            this.activeResources.put(var1, new Engine.ResourceWeakReference(var1, var2, this.getReferenceQueue()));
         }
      }

      this.jobs.remove(var1);
   }

   public void onResourceReleased(Key var1, EngineResource var2) {
      Util.assertMainThread();
      this.activeResources.remove(var1);
      if (var2.isCacheable()) {
         this.cache.put(var1, var2);
      } else {
         this.resourceRecycler.recycle(var2);
      }

   }

   public void onResourceRemoved(Resource var1) {
      Util.assertMainThread();
      this.resourceRecycler.recycle(var1);
   }

   public void release(Resource var1) {
      Util.assertMainThread();
      if (var1 instanceof EngineResource) {
         ((EngineResource)var1).release();
      } else {
         throw new IllegalArgumentException("Cannot release anything but an EngineResource");
      }
   }

   static class DecodeJobFactory {
      private int creationOrder;
      final DecodeJob.DiskCacheProvider diskCacheProvider;
      final Pools.Pool pool = FactoryPools.simple(150, new FactoryPools.Factory() {
         public DecodeJob create() {
            return new DecodeJob(DecodeJobFactory.this.diskCacheProvider, DecodeJobFactory.this.pool);
         }
      });

      DecodeJobFactory(DecodeJob.DiskCacheProvider var1) {
         this.diskCacheProvider = var1;
      }

      DecodeJob build(GlideContext var1, Object var2, EngineKey var3, Key var4, int var5, int var6, Class var7, Class var8, Priority var9, DiskCacheStrategy var10, Map var11, boolean var12, boolean var13, boolean var14, Options var15, DecodeJob.Callback var16) {
         DecodeJob var17 = (DecodeJob)this.pool.acquire();
         int var18 = this.creationOrder++;
         return var17.init(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var18);
      }
   }

   static class EngineJobFactory {
      final GlideExecutor diskCacheExecutor;
      final EngineJobListener listener;
      final Pools.Pool pool = FactoryPools.simple(150, new FactoryPools.Factory() {
         public EngineJob create() {
            return new EngineJob(EngineJobFactory.this.diskCacheExecutor, EngineJobFactory.this.sourceExecutor, EngineJobFactory.this.sourceUnlimitedExecutor, EngineJobFactory.this.listener, EngineJobFactory.this.pool);
         }
      });
      final GlideExecutor sourceExecutor;
      final GlideExecutor sourceUnlimitedExecutor;

      EngineJobFactory(GlideExecutor var1, GlideExecutor var2, GlideExecutor var3, EngineJobListener var4) {
         this.diskCacheExecutor = var1;
         this.sourceExecutor = var2;
         this.sourceUnlimitedExecutor = var3;
         this.listener = var4;
      }

      EngineJob build(Key var1, boolean var2, boolean var3) {
         return ((EngineJob)this.pool.acquire()).init(var1, var2, var3);
      }
   }

   private static class LazyDiskCacheProvider implements DecodeJob.DiskCacheProvider {
      private volatile DiskCache diskCache;
      private final DiskCache.Factory factory;

      public LazyDiskCacheProvider(DiskCache.Factory var1) {
         this.factory = var1;
      }

      public DiskCache getDiskCache() {
         if (this.diskCache == null) {
            synchronized(this){}

            Throwable var10000;
            boolean var10001;
            label226: {
               try {
                  if (this.diskCache == null) {
                     this.diskCache = this.factory.build();
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label226;
               }

               try {
                  if (this.diskCache == null) {
                     DiskCacheAdapter var1 = new DiskCacheAdapter();
                     this.diskCache = var1;
                  }
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label226;
               }

               label214:
               try {
                  return this.diskCache;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label214;
               }
            }

            while(true) {
               Throwable var22 = var10000;

               try {
                  throw var22;
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  continue;
               }
            }
         } else {
            return this.diskCache;
         }
      }
   }

   public static class LoadStatus {
      private final ResourceCallback cb;
      private final EngineJob engineJob;

      public LoadStatus(ResourceCallback var1, EngineJob var2) {
         this.cb = var1;
         this.engineJob = var2;
      }

      public void cancel() {
         this.engineJob.removeCallback(this.cb);
      }
   }

   private static class RefQueueIdleHandler implements IdleHandler {
      private final Map activeResources;
      private final ReferenceQueue queue;

      public RefQueueIdleHandler(Map var1, ReferenceQueue var2) {
         this.activeResources = var1;
         this.queue = var2;
      }

      public boolean queueIdle() {
         Engine.ResourceWeakReference var1 = (Engine.ResourceWeakReference)this.queue.poll();
         if (var1 != null) {
            this.activeResources.remove(var1.key);
         }

         return true;
      }
   }

   private static class ResourceWeakReference extends WeakReference {
      final Key key;

      public ResourceWeakReference(Key var1, EngineResource var2, ReferenceQueue var3) {
         super(var2, var3);
         this.key = var1;
      }
   }
}
