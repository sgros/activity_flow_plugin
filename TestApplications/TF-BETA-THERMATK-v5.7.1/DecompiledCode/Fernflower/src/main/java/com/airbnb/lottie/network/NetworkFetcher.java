package com.airbnb.lottie.network;

import android.content.Context;
import androidx.core.util.Pair;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.airbnb.lottie.utils.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.ZipInputStream;

public class NetworkFetcher {
   private final Context appContext;
   private final NetworkCache networkCache;
   private final String url;

   private NetworkFetcher(Context var1, String var2) {
      this.appContext = var1.getApplicationContext();
      this.url = var2;
      this.networkCache = new NetworkCache(this.appContext, var2);
   }

   private LottieComposition fetchFromCache() {
      Pair var1 = this.networkCache.fetch();
      if (var1 == null) {
         return null;
      } else {
         FileExtension var2 = (FileExtension)var1.first;
         InputStream var3 = (InputStream)var1.second;
         LottieResult var4;
         if (var2 == FileExtension.ZIP) {
            var4 = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(var3), this.url);
         } else {
            var4 = LottieCompositionFactory.fromJsonInputStreamSync(var3, this.url);
         }

         return var4.getValue() != null ? (LottieComposition)var4.getValue() : null;
      }
   }

   private LottieResult fetchFromNetwork() {
      try {
         LottieResult var1 = this.fetchFromNetworkInternal();
         return var1;
      } catch (IOException var2) {
         return new LottieResult(var2);
      }
   }

   private LottieResult fetchFromNetworkInternal() throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static LottieResult fetchSync(Context var0, String var1) {
      return (new NetworkFetcher(var0, var1)).fetchSync();
   }

   private String getErrorFromConnection(HttpURLConnection param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private LottieResult getResultFromConnection(HttpURLConnection var1) throws IOException {
      String var2 = var1.getContentType();
      String var3 = var2;
      if (var2 == null) {
         var3 = "application/json";
      }

      byte var4 = -1;
      int var5 = var3.hashCode();
      if (var5 != -1248325150) {
         if (var5 == -43840953 && var3.equals("application/json")) {
            var4 = 1;
         }
      } else if (var3.equals("application/zip")) {
         var4 = 0;
      }

      FileExtension var6;
      LottieResult var10;
      if (var4 != 0) {
         Logger.debug("Received json response.");
         FileExtension var9 = FileExtension.JSON;
         LottieResult var7 = LottieCompositionFactory.fromJsonInputStreamSync(new FileInputStream(new File(this.networkCache.writeTempCacheFile(var1.getInputStream(), var9).getAbsolutePath())), this.url);
         var6 = var9;
         var10 = var7;
      } else {
         Logger.debug("Handling zip response.");
         FileExtension var8 = FileExtension.ZIP;
         var10 = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(new FileInputStream(this.networkCache.writeTempCacheFile(var1.getInputStream(), var8))), this.url);
         var6 = var8;
      }

      if (var10.getValue() != null) {
         this.networkCache.renameTempFile(var6);
      }

      return var10;
   }

   public LottieResult fetchSync() {
      LottieComposition var1 = this.fetchFromCache();
      if (var1 != null) {
         return new LottieResult(var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Animation for ");
         var2.append(this.url);
         var2.append(" not found in cache. Fetching from network.");
         Logger.debug(var2.toString());
         return this.fetchFromNetwork();
      }
   }
}
