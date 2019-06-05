package com.bumptech.glide.load.model;

import android.support.v4.util.Pools;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class MultiModelLoader implements ModelLoader {
   private final Pools.Pool exceptionListPool;
   private final List modelLoaders;

   MultiModelLoader(List var1, Pools.Pool var2) {
      this.modelLoaders = var1;
      this.exceptionListPool = var2;
   }

   public ModelLoader.LoadData buildLoadData(Object var1, int var2, int var3, Options var4) {
      int var5 = this.modelLoaders.size();
      ArrayList var6 = new ArrayList(var5);
      Object var7 = null;
      int var8 = 0;

      Key var9;
      Key var11;
      for(var9 = null; var8 < var5; var9 = var11) {
         ModelLoader var10 = (ModelLoader)this.modelLoaders.get(var8);
         var11 = var9;
         if (var10.handles(var1)) {
            ModelLoader.LoadData var12 = var10.buildLoadData(var1, var2, var3, var4);
            var11 = var9;
            if (var12 != null) {
               var11 = var12.sourceKey;
               var6.add(var12.fetcher);
            }
         }

         ++var8;
      }

      ModelLoader.LoadData var13 = (ModelLoader.LoadData)var7;
      if (!var6.isEmpty()) {
         var13 = new ModelLoader.LoadData(var9, new MultiModelLoader.MultiFetcher(var6, this.exceptionListPool));
      }

      return var13;
   }

   public boolean handles(Object var1) {
      Iterator var2 = this.modelLoaders.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!((ModelLoader)var2.next()).handles(var1));

      return true;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("MultiModelLoader{modelLoaders=");
      var1.append(Arrays.toString(this.modelLoaders.toArray(new ModelLoader[this.modelLoaders.size()])));
      var1.append('}');
      return var1.toString();
   }

   static class MultiFetcher implements DataFetcher, DataFetcher.DataCallback {
      private DataFetcher.DataCallback callback;
      private int currentIndex;
      private final Pools.Pool exceptionListPool;
      private List exceptions;
      private final List fetchers;
      private Priority priority;

      MultiFetcher(List var1, Pools.Pool var2) {
         this.exceptionListPool = var2;
         Preconditions.checkNotEmpty((Collection)var1);
         this.fetchers = var1;
         this.currentIndex = 0;
      }

      private void startNextOrFail() {
         if (this.currentIndex < this.fetchers.size() - 1) {
            ++this.currentIndex;
            this.loadData(this.priority, this.callback);
         } else {
            this.callback.onLoadFailed(new GlideException("Fetch failed", new ArrayList(this.exceptions)));
         }

      }

      public void cancel() {
         Iterator var1 = this.fetchers.iterator();

         while(var1.hasNext()) {
            ((DataFetcher)var1.next()).cancel();
         }

      }

      public void cleanup() {
         if (this.exceptions != null) {
            this.exceptionListPool.release(this.exceptions);
         }

         this.exceptions = null;
         Iterator var1 = this.fetchers.iterator();

         while(var1.hasNext()) {
            ((DataFetcher)var1.next()).cleanup();
         }

      }

      public Class getDataClass() {
         return ((DataFetcher)this.fetchers.get(0)).getDataClass();
      }

      public DataSource getDataSource() {
         return ((DataFetcher)this.fetchers.get(0)).getDataSource();
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         this.priority = var1;
         this.callback = var2;
         this.exceptions = (List)this.exceptionListPool.acquire();
         ((DataFetcher)this.fetchers.get(this.currentIndex)).loadData(var1, this);
      }

      public void onDataReady(Object var1) {
         if (var1 != null) {
            this.callback.onDataReady(var1);
         } else {
            this.startNextOrFail();
         }

      }

      public void onLoadFailed(Exception var1) {
         this.exceptions.add(var1);
         this.startNextOrFail();
      }
   }
}
