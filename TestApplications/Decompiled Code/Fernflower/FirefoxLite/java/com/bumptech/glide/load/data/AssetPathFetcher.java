package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.IOException;

public abstract class AssetPathFetcher implements DataFetcher {
   private final AssetManager assetManager;
   private final String assetPath;
   private Object data;

   public AssetPathFetcher(AssetManager var1, String var2) {
      this.assetManager = var1;
      this.assetPath = var2;
   }

   public void cancel() {
   }

   public void cleanup() {
      if (this.data != null) {
         try {
            this.close(this.data);
         } catch (IOException var2) {
         }

      }
   }

   protected abstract void close(Object var1) throws IOException;

   public DataSource getDataSource() {
      return DataSource.LOCAL;
   }

   public void loadData(Priority var1, DataFetcher.DataCallback var2) {
      try {
         this.data = this.loadResource(this.assetManager, this.assetPath);
      } catch (IOException var3) {
         if (Log.isLoggable("AssetPathFetcher", 3)) {
            Log.d("AssetPathFetcher", "Failed to load data from asset manager", var3);
         }

         var2.onLoadFailed(var3);
         return;
      }

      var2.onDataReady(this.data);
   }

   protected abstract Object loadResource(AssetManager var1, String var2) throws IOException;
}
