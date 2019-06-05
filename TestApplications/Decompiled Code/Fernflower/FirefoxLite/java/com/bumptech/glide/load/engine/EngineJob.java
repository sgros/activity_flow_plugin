package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class EngineJob implements DecodeJob.Callback, FactoryPools.Poolable {
   private static final EngineJob.EngineResourceFactory DEFAULT_FACTORY = new EngineJob.EngineResourceFactory();
   private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper(), new EngineJob.MainThreadCallback());
   private final List cbs;
   private DataSource dataSource;
   private DecodeJob decodeJob;
   private final GlideExecutor diskCacheExecutor;
   private EngineResource engineResource;
   private final EngineJob.EngineResourceFactory engineResourceFactory;
   private GlideException exception;
   private boolean hasLoadFailed;
   private boolean hasResource;
   private List ignoredCallbacks;
   private boolean isCacheable;
   private volatile boolean isCancelled;
   private Key key;
   private final EngineJobListener listener;
   private final Pools.Pool pool;
   private Resource resource;
   private final GlideExecutor sourceExecutor;
   private final GlideExecutor sourceUnlimitedExecutor;
   private final StateVerifier stateVerifier;
   private boolean useUnlimitedSourceGeneratorPool;

   EngineJob(GlideExecutor var1, GlideExecutor var2, GlideExecutor var3, EngineJobListener var4, Pools.Pool var5) {
      this(var1, var2, var3, var4, var5, DEFAULT_FACTORY);
   }

   EngineJob(GlideExecutor var1, GlideExecutor var2, GlideExecutor var3, EngineJobListener var4, Pools.Pool var5, EngineJob.EngineResourceFactory var6) {
      this.cbs = new ArrayList(2);
      this.stateVerifier = StateVerifier.newInstance();
      this.diskCacheExecutor = var1;
      this.sourceExecutor = var2;
      this.sourceUnlimitedExecutor = var3;
      this.listener = var4;
      this.pool = var5;
      this.engineResourceFactory = var6;
   }

   private void addIgnoredCallback(ResourceCallback var1) {
      if (this.ignoredCallbacks == null) {
         this.ignoredCallbacks = new ArrayList(2);
      }

      if (!this.ignoredCallbacks.contains(var1)) {
         this.ignoredCallbacks.add(var1);
      }

   }

   private GlideExecutor getActiveSourceExecutor() {
      GlideExecutor var1;
      if (this.useUnlimitedSourceGeneratorPool) {
         var1 = this.sourceUnlimitedExecutor;
      } else {
         var1 = this.sourceExecutor;
      }

      return var1;
   }

   private boolean isInIgnoredCallbacks(ResourceCallback var1) {
      boolean var2;
      if (this.ignoredCallbacks != null && this.ignoredCallbacks.contains(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void release(boolean var1) {
      Util.assertMainThread();
      this.cbs.clear();
      this.key = null;
      this.engineResource = null;
      this.resource = null;
      if (this.ignoredCallbacks != null) {
         this.ignoredCallbacks.clear();
      }

      this.hasLoadFailed = false;
      this.isCancelled = false;
      this.hasResource = false;
      this.decodeJob.release(var1);
      this.decodeJob = null;
      this.exception = null;
      this.dataSource = null;
      this.pool.release(this);
   }

   public void addCallback(ResourceCallback var1) {
      Util.assertMainThread();
      this.stateVerifier.throwIfRecycled();
      if (this.hasResource) {
         var1.onResourceReady(this.engineResource, this.dataSource);
      } else if (this.hasLoadFailed) {
         var1.onLoadFailed(this.exception);
      } else {
         this.cbs.add(var1);
      }

   }

   void cancel() {
      if (!this.hasLoadFailed && !this.hasResource && !this.isCancelled) {
         this.isCancelled = true;
         this.decodeJob.cancel();
         this.listener.onEngineJobCancelled(this, this.key);
      }
   }

   public StateVerifier getVerifier() {
      return this.stateVerifier;
   }

   void handleCancelledOnMainThread() {
      this.stateVerifier.throwIfRecycled();
      if (this.isCancelled) {
         this.listener.onEngineJobCancelled(this, this.key);
         this.release(false);
      } else {
         throw new IllegalStateException("Not cancelled");
      }
   }

   void handleExceptionOnMainThread() {
      this.stateVerifier.throwIfRecycled();
      if (this.isCancelled) {
         this.release(false);
      } else if (!this.cbs.isEmpty()) {
         if (!this.hasLoadFailed) {
            this.hasLoadFailed = true;
            this.listener.onEngineJobComplete(this.key, (EngineResource)null);
            Iterator var1 = this.cbs.iterator();

            while(var1.hasNext()) {
               ResourceCallback var2 = (ResourceCallback)var1.next();
               if (!this.isInIgnoredCallbacks(var2)) {
                  var2.onLoadFailed(this.exception);
               }
            }

            this.release(false);
         } else {
            throw new IllegalStateException("Already failed once");
         }
      } else {
         throw new IllegalStateException("Received an exception without any callbacks to notify");
      }
   }

   void handleResultOnMainThread() {
      this.stateVerifier.throwIfRecycled();
      if (this.isCancelled) {
         this.resource.recycle();
         this.release(false);
      } else if (!this.cbs.isEmpty()) {
         if (!this.hasResource) {
            this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable);
            this.hasResource = true;
            this.engineResource.acquire();
            this.listener.onEngineJobComplete(this.key, this.engineResource);
            Iterator var1 = this.cbs.iterator();

            while(var1.hasNext()) {
               ResourceCallback var2 = (ResourceCallback)var1.next();
               if (!this.isInIgnoredCallbacks(var2)) {
                  this.engineResource.acquire();
                  var2.onResourceReady(this.engineResource, this.dataSource);
               }
            }

            this.engineResource.release();
            this.release(false);
         } else {
            throw new IllegalStateException("Already have resource");
         }
      } else {
         throw new IllegalStateException("Received a resource without any callbacks to notify");
      }
   }

   EngineJob init(Key var1, boolean var2, boolean var3) {
      this.key = var1;
      this.isCacheable = var2;
      this.useUnlimitedSourceGeneratorPool = var3;
      return this;
   }

   public void onLoadFailed(GlideException var1) {
      this.exception = var1;
      MAIN_THREAD_HANDLER.obtainMessage(2, this).sendToTarget();
   }

   public void onResourceReady(Resource var1, DataSource var2) {
      this.resource = var1;
      this.dataSource = var2;
      MAIN_THREAD_HANDLER.obtainMessage(1, this).sendToTarget();
   }

   public void removeCallback(ResourceCallback var1) {
      Util.assertMainThread();
      this.stateVerifier.throwIfRecycled();
      if (!this.hasResource && !this.hasLoadFailed) {
         this.cbs.remove(var1);
         if (this.cbs.isEmpty()) {
            this.cancel();
         }
      } else {
         this.addIgnoredCallback(var1);
      }

   }

   public void reschedule(DecodeJob var1) {
      this.getActiveSourceExecutor().execute(var1);
   }

   public void start(DecodeJob var1) {
      this.decodeJob = var1;
      GlideExecutor var2;
      if (var1.willDecodeFromCache()) {
         var2 = this.diskCacheExecutor;
      } else {
         var2 = this.getActiveSourceExecutor();
      }

      var2.execute(var1);
   }

   static class EngineResourceFactory {
      public EngineResource build(Resource var1, boolean var2) {
         return new EngineResource(var1, var2);
      }
   }

   private static class MainThreadCallback implements Callback {
      MainThreadCallback() {
      }

      public boolean handleMessage(Message var1) {
         EngineJob var2 = (EngineJob)var1.obj;
         switch(var1.what) {
         case 1:
            var2.handleResultOnMainThread();
            break;
         case 2:
            var2.handleExceptionOnMainThread();
            break;
         case 3:
            var2.handleCancelledOnMainThread();
            break;
         default:
            StringBuilder var3 = new StringBuilder();
            var3.append("Unrecognized message: ");
            var3.append(var1.what);
            throw new IllegalStateException(var3.toString());
         }

         return true;
      }
   }
}
