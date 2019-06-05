package org.mozilla.focus.tabs.tabtray;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

public class FaviconModelLoaderFactory implements ModelLoaderFactory {
   public ModelLoader build(MultiModelLoaderFactory var1) {
      return new FaviconModelLoaderFactory.FaviconModelLoader();
   }

   public static class FaviconModelLoader implements ModelLoader {
      public ModelLoader.LoadData buildLoadData(FaviconModel var1, int var2, int var3, Options var4) {
         return new ModelLoader.LoadData(new ObjectKey(var1.url), new FaviconModelLoaderFactory.Fetcher(var1));
      }

      public boolean handles(FaviconModel var1) {
         return true;
      }
   }

   public static class Fetcher implements DataFetcher {
      private FaviconModel model;

      Fetcher(FaviconModel var1) {
         this.model = var1;
      }

      public void cancel() {
      }

      public void cleanup() {
      }

      public Class getDataClass() {
         return FaviconModel.class;
      }

      public DataSource getDataSource() {
         return DataSource.LOCAL;
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         var2.onDataReady(this.model);
      }
   }
}
