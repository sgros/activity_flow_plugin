package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class DataCacheGenerator implements DataFetcher.DataCallback, DataFetcherGenerator {
   private File cacheFile;
   private List cacheKeys;
   private final DataFetcherGenerator.FetcherReadyCallback cb;
   private final DecodeHelper helper;
   private volatile ModelLoader.LoadData loadData;
   private int modelLoaderIndex;
   private List modelLoaders;
   private int sourceIdIndex;
   private Key sourceKey;

   DataCacheGenerator(DecodeHelper var1, DataFetcherGenerator.FetcherReadyCallback var2) {
      this(var1.getCacheKeys(), var1, var2);
   }

   DataCacheGenerator(List var1, DecodeHelper var2, DataFetcherGenerator.FetcherReadyCallback var3) {
      this.sourceIdIndex = -1;
      this.cacheKeys = var1;
      this.helper = var2;
      this.cb = var3;
   }

   private boolean hasNextModelLoader() {
      boolean var1;
      if (this.modelLoaderIndex < this.modelLoaders.size()) {
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

   public void onDataReady(Object var1) {
      this.cb.onDataFetcherReady(this.sourceKey, var1, this.loadData.fetcher, DataSource.DATA_DISK_CACHE, this.sourceKey);
   }

   public void onLoadFailed(Exception var1) {
      this.cb.onDataFetcherFailed(this.sourceKey, var1, this.loadData.fetcher, DataSource.DATA_DISK_CACHE);
   }

   public boolean startNext() {
      while(true) {
         List var1 = this.modelLoaders;
         boolean var2 = false;
         if (var1 != null && this.hasNextModelLoader()) {
            this.loadData = null;

            while(!var2 && this.hasNextModelLoader()) {
               var1 = this.modelLoaders;
               int var3 = this.modelLoaderIndex++;
               this.loadData = ((ModelLoader)var1.get(var3)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
               if (this.loadData != null && this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
                  this.loadData.fetcher.loadData(this.helper.getPriority(), this);
                  var2 = true;
               }
            }

            return var2;
         }

         ++this.sourceIdIndex;
         if (this.sourceIdIndex >= this.cacheKeys.size()) {
            return false;
         }

         Key var5 = (Key)this.cacheKeys.get(this.sourceIdIndex);
         DataCacheKey var4 = new DataCacheKey(var5, this.helper.getSignature());
         this.cacheFile = this.helper.getDiskCache().get(var4);
         if (this.cacheFile != null) {
            this.sourceKey = var5;
            this.modelLoaders = this.helper.getModelLoaders(this.cacheFile);
            this.modelLoaderIndex = 0;
         }
      }
   }
}
