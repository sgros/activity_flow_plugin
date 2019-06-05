package com.bumptech.glide.load.model;

import android.content.res.AssetManager;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorAssetPathFetcher;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.signature.ObjectKey;

public class AssetUriLoader implements ModelLoader {
   private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
   private final AssetManager assetManager;
   private final AssetUriLoader.AssetFetcherFactory factory;

   public AssetUriLoader(AssetManager var1, AssetUriLoader.AssetFetcherFactory var2) {
      this.assetManager = var1;
      this.factory = var2;
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      String var5 = var1.toString().substring(ASSET_PREFIX_LENGTH);
      return new ModelLoader.LoadData(new ObjectKey(var1), this.factory.buildFetcher(this.assetManager, var5));
   }

   public boolean handles(Uri var1) {
      boolean var2 = "file".equals(var1.getScheme());
      boolean var3 = false;
      boolean var4 = var3;
      if (var2) {
         var4 = var3;
         if (!var1.getPathSegments().isEmpty()) {
            var4 = var3;
            if ("android_asset".equals(var1.getPathSegments().get(0))) {
               var4 = true;
            }
         }
      }

      return var4;
   }

   public interface AssetFetcherFactory {
      DataFetcher buildFetcher(AssetManager var1, String var2);
   }

   public static class FileDescriptorFactory implements AssetUriLoader.AssetFetcherFactory, ModelLoaderFactory {
      private final AssetManager assetManager;

      public FileDescriptorFactory(AssetManager var1) {
         this.assetManager = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new AssetUriLoader(this.assetManager, this);
      }

      public DataFetcher buildFetcher(AssetManager var1, String var2) {
         return new FileDescriptorAssetPathFetcher(var1, var2);
      }
   }

   public static class StreamFactory implements AssetUriLoader.AssetFetcherFactory, ModelLoaderFactory {
      private final AssetManager assetManager;

      public StreamFactory(AssetManager var1) {
         this.assetManager = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new AssetUriLoader(this.assetManager, this);
      }

      public DataFetcher buildFetcher(AssetManager var1, String var2) {
         return new StreamAssetPathFetcher(var1, var2);
      }
   }
}
