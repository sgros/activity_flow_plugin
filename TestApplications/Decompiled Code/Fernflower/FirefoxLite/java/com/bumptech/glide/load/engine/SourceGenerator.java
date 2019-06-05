package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.LogTime;
import java.util.Collections;
import java.util.List;

class SourceGenerator implements DataFetcher.DataCallback, DataFetcherGenerator, DataFetcherGenerator.FetcherReadyCallback {
   private final DataFetcherGenerator.FetcherReadyCallback cb;
   private Object dataToCache;
   private final DecodeHelper helper;
   private volatile ModelLoader.LoadData loadData;
   private int loadDataListIndex;
   private DataCacheKey originalKey;
   private DataCacheGenerator sourceCacheGenerator;

   public SourceGenerator(DecodeHelper var1, DataFetcherGenerator.FetcherReadyCallback var2) {
      this.helper = var1;
      this.cb = var2;
   }

   private void cacheData(Object var1) {
      long var2 = LogTime.getLogTime();

      try {
         Encoder var4 = this.helper.getSourceEncoder(var1);
         DataCacheWriter var5 = new DataCacheWriter(var4, var1, this.helper.getOptions());
         DataCacheKey var6 = new DataCacheKey(this.loadData.sourceKey, this.helper.getSignature());
         this.originalKey = var6;
         this.helper.getDiskCache().put(this.originalKey, var5);
         if (Log.isLoggable("SourceGenerator", 2)) {
            StringBuilder var9 = new StringBuilder();
            var9.append("Finished encoding source to cache, key: ");
            var9.append(this.originalKey);
            var9.append(", data: ");
            var9.append(var1);
            var9.append(", encoder: ");
            var9.append(var4);
            var9.append(", duration: ");
            var9.append(LogTime.getElapsedMillis(var2));
            Log.v("SourceGenerator", var9.toString());
         }
      } finally {
         this.loadData.fetcher.cleanup();
      }

      this.sourceCacheGenerator = new DataCacheGenerator(Collections.singletonList(this.loadData.sourceKey), this.helper, this);
   }

   private boolean hasNextModelLoader() {
      boolean var1;
      if (this.loadDataListIndex < this.helper.getLoadData().size()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void cancel() {
      ModelLoader.LoadData var1 = this.loadData;
      if (var1 != null) {
         var1.fetcher.cancel();
      }

   }

   public void onDataFetcherFailed(Key var1, Exception var2, DataFetcher var3, DataSource var4) {
      this.cb.onDataFetcherFailed(var1, var2, var3, this.loadData.fetcher.getDataSource());
   }

   public void onDataFetcherReady(Key var1, Object var2, DataFetcher var3, DataSource var4, Key var5) {
      this.cb.onDataFetcherReady(var1, var2, var3, this.loadData.fetcher.getDataSource(), var1);
   }

   public void onDataReady(Object var1) {
      DiskCacheStrategy var2 = this.helper.getDiskCacheStrategy();
      if (var1 != null && var2.isDataCacheable(this.loadData.fetcher.getDataSource())) {
         this.dataToCache = var1;
         this.cb.reschedule();
      } else {
         this.cb.onDataFetcherReady(this.loadData.sourceKey, var1, this.loadData.fetcher, this.loadData.fetcher.getDataSource(), this.originalKey);
      }

   }

   public void onLoadFailed(Exception var1) {
      this.cb.onDataFetcherFailed(this.originalKey, var1, this.loadData.fetcher, this.loadData.fetcher.getDataSource());
   }

   public void reschedule() {
      throw new UnsupportedOperationException();
   }

   public boolean startNext() {
      if (this.dataToCache != null) {
         Object var1 = this.dataToCache;
         this.dataToCache = null;
         this.cacheData(var1);
      }

      if (this.sourceCacheGenerator != null && this.sourceCacheGenerator.startNext()) {
         return true;
      } else {
         this.sourceCacheGenerator = null;
         this.loadData = null;
         boolean var2 = false;

         while(!var2 && this.hasNextModelLoader()) {
            List var4 = this.helper.getLoadData();
            int var3 = this.loadDataListIndex++;
            this.loadData = (ModelLoader.LoadData)var4.get(var3);
            if (this.loadData != null && (this.helper.getDiskCacheStrategy().isDataCacheable(this.loadData.fetcher.getDataSource()) || this.helper.hasLoadPath(this.loadData.fetcher.getDataClass()))) {
               this.loadData.fetcher.loadData(this.helper.getPriority(), this);
               var2 = true;
            }
         }

         return var2;
      }
   }
}
