package com.bumptech.glide.load.engine;

import android.os.Build.VERSION;
import android.support.v4.os.TraceCompat;
import android.support.v4.util.Pools;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DecodeJob implements DataFetcherGenerator.FetcherReadyCallback, FactoryPools.Poolable, Comparable, Runnable {
   private DecodeJob.Callback callback;
   private Key currentAttemptingKey;
   private Object currentData;
   private DataSource currentDataSource;
   private DataFetcher currentFetcher;
   private volatile DataFetcherGenerator currentGenerator;
   Key currentSourceKey;
   private Thread currentThread;
   final DecodeHelper decodeHelper = new DecodeHelper();
   final DecodeJob.DeferredEncodeManager deferredEncodeManager = new DecodeJob.DeferredEncodeManager();
   private final DecodeJob.DiskCacheProvider diskCacheProvider;
   DiskCacheStrategy diskCacheStrategy;
   private final List exceptions = new ArrayList();
   private GlideContext glideContext;
   int height;
   private volatile boolean isCallbackNotified;
   private volatile boolean isCancelled;
   private EngineKey loadKey;
   private boolean onlyRetrieveFromCache;
   Options options;
   private int order;
   private final Pools.Pool pool;
   private Priority priority;
   private final DecodeJob.ReleaseManager releaseManager = new DecodeJob.ReleaseManager();
   private DecodeJob.RunReason runReason;
   Key signature;
   private DecodeJob.Stage stage;
   private long startFetchTime;
   private final StateVerifier stateVerifier = StateVerifier.newInstance();
   int width;

   DecodeJob(DecodeJob.DiskCacheProvider var1, Pools.Pool var2) {
      this.diskCacheProvider = var1;
      this.pool = var2;
   }

   private Resource decodeFromData(DataFetcher var1, Object var2, DataSource var3) throws GlideException {
      if (var2 == null) {
         var1.cleanup();
         return null;
      } else {
         Resource var9;
         try {
            long var4 = LogTime.getLogTime();
            var9 = this.decodeFromFetcher(var2, var3);
            if (Log.isLoggable("DecodeJob", 2)) {
               StringBuilder var8 = new StringBuilder();
               var8.append("Decoded result ");
               var8.append(var9);
               this.logWithTimeAndKey(var8.toString(), var4);
            }
         } finally {
            var1.cleanup();
         }

         return var9;
      }
   }

   private Resource decodeFromFetcher(Object var1, DataSource var2) throws GlideException {
      return this.runLoadPath(var1, var2, this.decodeHelper.getLoadPath(var1.getClass()));
   }

   private void decodeFromRetrievedData() {
      if (Log.isLoggable("DecodeJob", 2)) {
         long var1 = this.startFetchTime;
         StringBuilder var3 = new StringBuilder();
         var3.append("data: ");
         var3.append(this.currentData);
         var3.append(", cache key: ");
         var3.append(this.currentSourceKey);
         var3.append(", fetcher: ");
         var3.append(this.currentFetcher);
         this.logWithTimeAndKey("Retrieved data", var1, var3.toString());
      }

      Resource var6 = null;

      label20: {
         Resource var4;
         try {
            var4 = this.decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
         } catch (GlideException var5) {
            var5.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
            this.exceptions.add(var5);
            break label20;
         }

         var6 = var4;
      }

      if (var6 != null) {
         this.notifyEncodeAndRelease(var6, this.currentDataSource);
      } else {
         this.runGenerators();
      }

   }

   private DataFetcherGenerator getNextGenerator() {
      switch(this.stage) {
      case RESOURCE_CACHE:
         return new ResourceCacheGenerator(this.decodeHelper, this);
      case DATA_CACHE:
         return new DataCacheGenerator(this.decodeHelper, this);
      case SOURCE:
         return new SourceGenerator(this.decodeHelper, this);
      case FINISHED:
         return null;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Unrecognized stage: ");
         var1.append(this.stage);
         throw new IllegalStateException(var1.toString());
      }
   }

   private DecodeJob.Stage getNextStage(DecodeJob.Stage var1) {
      switch(var1) {
      case RESOURCE_CACHE:
         if (this.diskCacheStrategy.decodeCachedData()) {
            var1 = DecodeJob.Stage.DATA_CACHE;
         } else {
            var1 = this.getNextStage(DecodeJob.Stage.DATA_CACHE);
         }

         return var1;
      case DATA_CACHE:
         if (this.onlyRetrieveFromCache) {
            var1 = DecodeJob.Stage.FINISHED;
         } else {
            var1 = DecodeJob.Stage.SOURCE;
         }

         return var1;
      case SOURCE:
      case FINISHED:
         return DecodeJob.Stage.FINISHED;
      case INITIALIZE:
         if (this.diskCacheStrategy.decodeCachedResource()) {
            var1 = DecodeJob.Stage.RESOURCE_CACHE;
         } else {
            var1 = this.getNextStage(DecodeJob.Stage.RESOURCE_CACHE);
         }

         return var1;
      default:
         StringBuilder var2 = new StringBuilder();
         var2.append("Unrecognized stage: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   private Options getOptionsWithHardwareConfig(DataSource var1) {
      Options var2 = this.options;
      if (VERSION.SDK_INT < 26) {
         return var2;
      } else if (var2.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null) {
         return var2;
      } else {
         Options var3;
         if (var1 != DataSource.RESOURCE_DISK_CACHE) {
            var3 = var2;
            if (!this.decodeHelper.isScaleOnlyOrNoTransform()) {
               return var3;
            }
         }

         var3 = new Options();
         var3.putAll(this.options);
         var3.set(Downsampler.ALLOW_HARDWARE_CONFIG, true);
         return var3;
      }
   }

   private int getPriority() {
      return this.priority.ordinal();
   }

   private void logWithTimeAndKey(String var1, long var2) {
      this.logWithTimeAndKey(var1, var2, (String)null);
   }

   private void logWithTimeAndKey(String var1, long var2, String var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append(" in ");
      var5.append(LogTime.getElapsedMillis(var2));
      var5.append(", load key: ");
      var5.append(this.loadKey);
      if (var4 != null) {
         StringBuilder var6 = new StringBuilder();
         var6.append(", ");
         var6.append(var4);
         var1 = var6.toString();
      } else {
         var1 = "";
      }

      var5.append(var1);
      var5.append(", thread: ");
      var5.append(Thread.currentThread().getName());
      Log.v("DecodeJob", var5.toString());
   }

   private void notifyComplete(Resource var1, DataSource var2) {
      this.setNotifiedOrThrow();
      this.callback.onResourceReady(var1, var2);
   }

   private void notifyEncodeAndRelease(Resource var1, DataSource var2) {
      if (var1 instanceof Initializable) {
         ((Initializable)var1).initialize();
      }

      Object var3 = null;
      Object var4 = var1;
      if (this.deferredEncodeManager.hasResourceToEncode()) {
         var4 = LockedResource.obtain(var1);
         var3 = var4;
      }

      this.notifyComplete((Resource)var4, var2);
      this.stage = DecodeJob.Stage.ENCODE;

      try {
         if (this.deferredEncodeManager.hasResourceToEncode()) {
            this.deferredEncodeManager.encode(this.diskCacheProvider, this.options);
         }
      } finally {
         if (var3 != null) {
            ((LockedResource)var3).unlock();
         }

         this.onEncodeComplete();
      }

   }

   private void notifyFailed() {
      this.setNotifiedOrThrow();
      GlideException var1 = new GlideException("Failed to load resource", new ArrayList(this.exceptions));
      this.callback.onLoadFailed(var1);
      this.onLoadFailed();
   }

   private void onEncodeComplete() {
      if (this.releaseManager.onEncodeComplete()) {
         this.releaseInternal();
      }

   }

   private void onLoadFailed() {
      if (this.releaseManager.onFailed()) {
         this.releaseInternal();
      }

   }

   private void releaseInternal() {
      this.releaseManager.reset();
      this.deferredEncodeManager.clear();
      this.decodeHelper.clear();
      this.isCallbackNotified = false;
      this.glideContext = null;
      this.signature = null;
      this.options = null;
      this.priority = null;
      this.loadKey = null;
      this.callback = null;
      this.stage = null;
      this.currentGenerator = null;
      this.currentThread = null;
      this.currentSourceKey = null;
      this.currentData = null;
      this.currentDataSource = null;
      this.currentFetcher = null;
      this.startFetchTime = 0L;
      this.isCancelled = false;
      this.exceptions.clear();
      this.pool.release(this);
   }

   private void runGenerators() {
      this.currentThread = Thread.currentThread();
      this.startFetchTime = LogTime.getLogTime();
      boolean var1 = false;

      boolean var2;
      while(true) {
         var2 = var1;
         if (this.isCancelled) {
            break;
         }

         var2 = var1;
         if (this.currentGenerator == null) {
            break;
         }

         var1 = this.currentGenerator.startNext();
         var2 = var1;
         if (var1) {
            break;
         }

         this.stage = this.getNextStage(this.stage);
         this.currentGenerator = this.getNextGenerator();
         if (this.stage == DecodeJob.Stage.SOURCE) {
            this.reschedule();
            return;
         }
      }

      if ((this.stage == DecodeJob.Stage.FINISHED || this.isCancelled) && !var2) {
         this.notifyFailed();
      }

   }

   private Resource runLoadPath(Object var1, DataSource var2, LoadPath var3) throws GlideException {
      Options var4 = this.getOptionsWithHardwareConfig(var2);
      DataRewinder var10 = this.glideContext.getRegistry().getRewinder(var1);

      Resource var11;
      try {
         int var5 = this.width;
         int var6 = this.height;
         DecodeJob.DecodeCallback var7 = new DecodeJob.DecodeCallback(var2);
         var11 = var3.load(var10, var4, var5, var6, var7);
      } finally {
         var10.cleanup();
      }

      return var11;
   }

   private void runWrapped() {
      switch(this.runReason) {
      case INITIALIZE:
         this.stage = this.getNextStage(DecodeJob.Stage.INITIALIZE);
         this.currentGenerator = this.getNextGenerator();
         this.runGenerators();
         break;
      case SWITCH_TO_SOURCE_SERVICE:
         this.runGenerators();
         break;
      case DECODE_DATA:
         this.decodeFromRetrievedData();
         break;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("Unrecognized run reason: ");
         var1.append(this.runReason);
         throw new IllegalStateException(var1.toString());
      }

   }

   private void setNotifiedOrThrow() {
      this.stateVerifier.throwIfRecycled();
      if (!this.isCallbackNotified) {
         this.isCallbackNotified = true;
      } else {
         throw new IllegalStateException("Already notified");
      }
   }

   public void cancel() {
      this.isCancelled = true;
      DataFetcherGenerator var1 = this.currentGenerator;
      if (var1 != null) {
         var1.cancel();
      }

   }

   public int compareTo(DecodeJob var1) {
      int var2 = this.getPriority() - var1.getPriority();
      int var3 = var2;
      if (var2 == 0) {
         var3 = this.order - var1.order;
      }

      return var3;
   }

   public StateVerifier getVerifier() {
      return this.stateVerifier;
   }

   DecodeJob init(GlideContext var1, Object var2, EngineKey var3, Key var4, int var5, int var6, Class var7, Class var8, Priority var9, DiskCacheStrategy var10, Map var11, boolean var12, boolean var13, boolean var14, Options var15, DecodeJob.Callback var16, int var17) {
      this.decodeHelper.init(var1, var2, var4, var5, var6, var10, var7, var8, var9, var15, var11, var12, var13, this.diskCacheProvider);
      this.glideContext = var1;
      this.signature = var4;
      this.priority = var9;
      this.loadKey = var3;
      this.width = var5;
      this.height = var6;
      this.diskCacheStrategy = var10;
      this.onlyRetrieveFromCache = var14;
      this.options = var15;
      this.callback = var16;
      this.order = var17;
      this.runReason = DecodeJob.RunReason.INITIALIZE;
      return this;
   }

   public void onDataFetcherFailed(Key var1, Exception var2, DataFetcher var3, DataSource var4) {
      var3.cleanup();
      GlideException var5 = new GlideException("Fetching data failed", var2);
      var5.setLoggingDetails(var1, var4, var3.getDataClass());
      this.exceptions.add(var5);
      if (Thread.currentThread() != this.currentThread) {
         this.runReason = DecodeJob.RunReason.SWITCH_TO_SOURCE_SERVICE;
         this.callback.reschedule(this);
      } else {
         this.runGenerators();
      }

   }

   public void onDataFetcherReady(Key var1, Object var2, DataFetcher var3, DataSource var4, Key var5) {
      this.currentSourceKey = var1;
      this.currentData = var2;
      this.currentFetcher = var3;
      this.currentDataSource = var4;
      this.currentAttemptingKey = var5;
      if (Thread.currentThread() != this.currentThread) {
         this.runReason = DecodeJob.RunReason.DECODE_DATA;
         this.callback.reschedule(this);
      } else {
         TraceCompat.beginSection("DecodeJob.decodeFromRetrievedData");

         try {
            this.decodeFromRetrievedData();
         } finally {
            TraceCompat.endSection();
         }
      }

   }

   void release(boolean var1) {
      if (this.releaseManager.release(var1)) {
         this.releaseInternal();
      }

   }

   public void reschedule() {
      this.runReason = DecodeJob.RunReason.SWITCH_TO_SOURCE_SERVICE;
      this.callback.reschedule(this);
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   boolean willDecodeFromCache() {
      DecodeJob.Stage var1 = this.getNextStage(DecodeJob.Stage.INITIALIZE);
      boolean var2;
      if (var1 != DecodeJob.Stage.RESOURCE_CACHE && var1 != DecodeJob.Stage.DATA_CACHE) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   interface Callback {
      void onLoadFailed(GlideException var1);

      void onResourceReady(Resource var1, DataSource var2);

      void reschedule(DecodeJob var1);
   }

   private final class DecodeCallback implements DecodePath.DecodeCallback {
      private final DataSource dataSource;

      DecodeCallback(DataSource var2) {
         this.dataSource = var2;
      }

      private Class getResourceClass(Resource var1) {
         return var1.get().getClass();
      }

      public Resource onResourceDecoded(Resource var1) {
         Class var2 = this.getResourceClass(var1);
         DataSource var3 = this.dataSource;
         DataSource var4 = DataSource.RESOURCE_DISK_CACHE;
         EncodeStrategy var5 = null;
         Transformation var10;
         Resource var12;
         if (var3 != var4) {
            var10 = DecodeJob.this.decodeHelper.getTransformation(var2);
            var12 = var10.transform(DecodeJob.this.glideContext, var1, DecodeJob.this.width, DecodeJob.this.height);
         } else {
            var12 = var1;
            var10 = null;
         }

         if (!var1.equals(var12)) {
            var1.recycle();
         }

         ResourceEncoder var8;
         if (DecodeJob.this.decodeHelper.isResourceEncoderAvailable(var12)) {
            var8 = DecodeJob.this.decodeHelper.getResultEncoder(var12);
            var5 = var8.getEncodeStrategy(DecodeJob.this.options);
         } else {
            EncodeStrategy var6 = EncodeStrategy.NONE;
            var8 = var5;
            var5 = var6;
         }

         boolean var7 = DecodeJob.this.decodeHelper.isSourceKey(DecodeJob.this.currentSourceKey);
         Object var13 = var12;
         if (DecodeJob.this.diskCacheStrategy.isResourceCacheable(var7 ^ true, this.dataSource, var5)) {
            if (var8 == null) {
               throw new Registry.NoResultEncoderAvailableException(var12.get().getClass());
            }

            Object var11;
            if (var5 == EncodeStrategy.SOURCE) {
               var11 = new DataCacheKey(DecodeJob.this.currentSourceKey, DecodeJob.this.signature);
            } else {
               if (var5 != EncodeStrategy.TRANSFORMED) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Unknown strategy: ");
                  var9.append(var5);
                  throw new IllegalArgumentException(var9.toString());
               }

               var11 = new ResourceCacheKey(DecodeJob.this.currentSourceKey, DecodeJob.this.signature, DecodeJob.this.width, DecodeJob.this.height, var10, var2, DecodeJob.this.options);
            }

            var13 = LockedResource.obtain(var12);
            DecodeJob.this.deferredEncodeManager.init((Key)var11, var8, (LockedResource)var13);
         }

         return (Resource)var13;
      }
   }

   private static class DeferredEncodeManager {
      private ResourceEncoder encoder;
      private Key key;
      private LockedResource toEncode;

      DeferredEncodeManager() {
      }

      void clear() {
         this.key = null;
         this.encoder = null;
         this.toEncode = null;
      }

      void encode(DecodeJob.DiskCacheProvider var1, Options var2) {
         TraceCompat.beginSection("DecodeJob.encode");

         try {
            DiskCache var3 = var1.getDiskCache();
            Key var4 = this.key;
            DataCacheWriter var7 = new DataCacheWriter(this.encoder, this.toEncode, var2);
            var3.put(var4, var7);
         } finally {
            this.toEncode.unlock();
            TraceCompat.endSection();
         }

      }

      boolean hasResourceToEncode() {
         boolean var1;
         if (this.toEncode != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      void init(Key var1, ResourceEncoder var2, LockedResource var3) {
         this.key = var1;
         this.encoder = var2;
         this.toEncode = var3;
      }
   }

   interface DiskCacheProvider {
      DiskCache getDiskCache();
   }

   private static class ReleaseManager {
      private boolean isEncodeComplete;
      private boolean isFailed;
      private boolean isReleased;

      ReleaseManager() {
      }

      private boolean isComplete(boolean var1) {
         if ((this.isFailed || var1 || this.isEncodeComplete) && this.isReleased) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      boolean onEncodeComplete() {
         synchronized(this){}

         boolean var1;
         try {
            this.isEncodeComplete = true;
            var1 = this.isComplete(false);
         } finally {
            ;
         }

         return var1;
      }

      boolean onFailed() {
         synchronized(this){}

         boolean var1;
         try {
            this.isFailed = true;
            var1 = this.isComplete(false);
         } finally {
            ;
         }

         return var1;
      }

      boolean release(boolean var1) {
         synchronized(this){}

         try {
            this.isReleased = true;
            var1 = this.isComplete(var1);
         } finally {
            ;
         }

         return var1;
      }

      void reset() {
         synchronized(this){}

         try {
            this.isEncodeComplete = false;
            this.isReleased = false;
            this.isFailed = false;
         } finally {
            ;
         }

      }
   }

   private static enum RunReason {
      DECODE_DATA,
      INITIALIZE,
      SWITCH_TO_SOURCE_SERVICE;
   }

   private static enum Stage {
      DATA_CACHE,
      ENCODE,
      FINISHED,
      INITIALIZE,
      RESOURCE_CACHE,
      SOURCE;
   }
}
