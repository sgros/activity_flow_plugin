package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class ResourceCacheGenerator implements DataFetcher.DataCallback, DataFetcherGenerator {
   private File cacheFile;
   private final DataFetcherGenerator.FetcherReadyCallback cb;
   private ResourceCacheKey currentKey;
   private final DecodeHelper helper;
   private volatile ModelLoader.LoadData loadData;
   private int modelLoaderIndex;
   private List modelLoaders;
   private int resourceClassIndex = -1;
   private int sourceIdIndex = 0;
   private Key sourceKey;

   public ResourceCacheGenerator(DecodeHelper var1, DataFetcherGenerator.FetcherReadyCallback var2) {
      this.helper = var1;
      this.cb = var2;
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
      this.cb.onDataFetcherReady(this.sourceKey, var1, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
   }

   public void onLoadFailed(Exception var1) {
      this.cb.onDataFetcherFailed(this.currentKey, var1, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
   }

   public boolean startNext() {
      List var1 = this.helper.getCacheKeys();
      boolean var2 = var1.isEmpty();
      boolean var3 = false;
      if (var2) {
         return false;
      } else {
         List var4 = this.helper.getRegisteredResourceClasses();

         while(this.modelLoaders == null || !this.hasNextModelLoader()) {
            ++this.resourceClassIndex;
            if (this.resourceClassIndex >= var4.size()) {
               ++this.sourceIdIndex;
               if (this.sourceIdIndex >= var1.size()) {
                  return false;
               }

               this.resourceClassIndex = 0;
            }

            Key var6 = (Key)var1.get(this.sourceIdIndex);
            Class var7 = (Class)var4.get(this.resourceClassIndex);
            Transformation var8 = this.helper.getTransformation(var7);
            this.currentKey = new ResourceCacheKey(var6, this.helper.getSignature(), this.helper.getWidth(), this.helper.getHeight(), var8, var7, this.helper.getOptions());
            this.cacheFile = this.helper.getDiskCache().get(this.currentKey);
            if (this.cacheFile != null) {
               this.sourceKey = var6;
               this.modelLoaders = this.helper.getModelLoaders(this.cacheFile);
               this.modelLoaderIndex = 0;
            }
         }

         this.loadData = null;

         while(!var3 && this.hasNextModelLoader()) {
            var4 = this.modelLoaders;
            int var5 = this.modelLoaderIndex++;
            this.loadData = ((ModelLoader)var4.get(var5)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
            if (this.loadData != null && this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
               this.loadData.fetcher.loadData(this.helper.getPriority(), this);
               var3 = true;
            }
         }

         return var3;
      }
   }
}
