package com.airbnb.lottie.network;

import android.content.Context;
import android.support.v4.util.Pair;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.airbnb.lottie.LottieTask;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
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

   private LottieTask fetch() {
      return new LottieTask(new Callable() {
         public LottieResult call() throws Exception {
            return NetworkFetcher.this.fetchSync();
         }
      });
   }

   public static LottieTask fetch(Context var0, String var1) {
      return (new NetworkFetcher(var0, var1)).fetch();
   }

   private LottieComposition fetchFromCache() {
      Pair var1 = this.networkCache.fetch();
      if (var1 == null) {
         return null;
      } else {
         FileExtension var2 = (FileExtension)var1.first;
         InputStream var3 = (InputStream)var1.second;
         LottieResult var4;
         if (var2 == FileExtension.Zip) {
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
      StringBuilder var1 = new StringBuilder();
      var1.append("Fetching ");
      var1.append(this.url);
      L.debug(var1.toString());
      HttpURLConnection var8 = (HttpURLConnection)(new URL(this.url)).openConnection();
      var8.setRequestMethod("GET");
      var8.connect();
      StringBuilder var2;
      if (var8.getErrorStream() == null && var8.getResponseCode() == 200) {
         String var10 = var8.getContentType();
         byte var3 = -1;
         int var4 = var10.hashCode();
         boolean var5 = true;
         if (var4 != -1248325150) {
            if (var4 == -43840953 && var10.equals("application/json")) {
               var3 = 1;
            }
         } else if (var10.equals("application/zip")) {
            var3 = 0;
         }

         LottieResult var9;
         FileExtension var11;
         if (var3 != 0) {
            L.debug("Received json response.");
            var11 = FileExtension.Json;
            var9 = LottieCompositionFactory.fromJsonInputStreamSync(new FileInputStream(new File(this.networkCache.writeTempCacheFile(var8.getInputStream(), var11).getAbsolutePath())), this.url);
         } else {
            L.debug("Handling zip response.");
            var11 = FileExtension.Zip;
            var9 = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(new FileInputStream(this.networkCache.writeTempCacheFile(var8.getInputStream(), var11))), this.url);
         }

         if (var9.getValue() != null) {
            this.networkCache.renameTempFile(var11);
         }

         var2 = new StringBuilder();
         var2.append("Completed fetch from network. Success: ");
         if (var9.getValue() == null) {
            var5 = false;
         }

         var2.append(var5);
         L.debug(var2.toString());
         return var9;
      } else {
         BufferedReader var6 = new BufferedReader(new InputStreamReader(var8.getErrorStream()));
         var2 = new StringBuilder();

         while(true) {
            String var7 = var6.readLine();
            if (var7 == null) {
               StringBuilder var12 = new StringBuilder();
               var12.append("Unable to fetch ");
               var12.append(this.url);
               var12.append(". Failed with ");
               var12.append(var8.getResponseCode());
               var12.append("\n");
               var12.append(var2);
               return new LottieResult(new IllegalArgumentException(var12.toString()));
            }

            var2.append(var7);
            var2.append('\n');
         }
      }
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
         L.debug(var2.toString());
         return this.fetchFromNetwork();
      }
   }
}
