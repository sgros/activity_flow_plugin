package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;

public class UnitModelLoader implements ModelLoader {
   public ModelLoader.LoadData buildLoadData(Object var1, int var2, int var3, Options var4) {
      return new ModelLoader.LoadData(new ObjectKey(var1), new UnitModelLoader.UnitFetcher(var1));
   }

   public boolean handles(Object var1) {
      return true;
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new UnitModelLoader();
      }
   }

   private static class UnitFetcher implements DataFetcher {
      private final Object resource;

      public UnitFetcher(Object var1) {
         this.resource = var1;
      }

      public void cancel() {
      }

      public void cleanup() {
      }

      public Class getDataClass() {
         return this.resource.getClass();
      }

      public DataSource getDataSource() {
         return DataSource.LOCAL;
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         var2.onDataReady(this.resource);
      }
   }
}
