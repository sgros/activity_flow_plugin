package org.mozilla.cachedrequestloader;

import android.content.Context;
import android.net.TrafficStats;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.httprequest.HttpRequest;
import org.mozilla.threadutils.ThreadUtils;

public class BackgroundCachedRequestLoader implements RequestLoaderDelegation.RequestLoader {
   private static final ExecutorService backgroundExecutorService = Executors.newFixedThreadPool(2);
   private boolean delayCacheLoad;
   private boolean delayNetworkLoad;
   private RequestLoaderDelegation requestLoaderDelegation;

   public BackgroundCachedRequestLoader(Context var1, String var2, String var3, String var4, int var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   public BackgroundCachedRequestLoader(Context var1, String var2, String var3, String var4, int var5, boolean var6) {
      this.delayCacheLoad = false;
      this.delayNetworkLoad = false;
      this.requestLoaderDelegation = new RequestLoaderDelegation(var1, var2, var3, var4, var5, var6, this);
   }

   // $FF: synthetic method
   public static void lambda$loadFromCache$0(BackgroundCachedRequestLoader var0, Context var1, String var2, ResponseData var3) {
      try {
         var0.sleepIfTesting(var1, var0.delayCacheLoad);
         WeakReference var6 = new WeakReference(var1);
         FileUtils.GetCache var4 = new FileUtils.GetCache(var6);
         String var7 = FileUtils.readStringFromFile(var4.get(), var2);
         Pair var8 = new Pair(1, var7);
         var3.postValue(var8);
      } catch (InterruptedException | ExecutionException var5) {
         var5.printStackTrace();
         Log.e("CachedRequestLoader", "Failed to open Cache directory when reading cached banner config");
      }

   }

   // $FF: synthetic method
   public static void lambda$loadFromRemote$1(BackgroundCachedRequestLoader var0, int var1, Context var2, String var3, String var4, ResponseData var5) {
      TrafficStats.setThreadStatsTag(var1);

      try {
         var0.sleepIfTesting(var2, var0.delayNetworkLoad);
         URL var7 = new URL(var3);
         var3 = HttpRequest.get(var7, var4).replace("\n", "");
         Pair var8 = new Pair(0, var3);
         var5.postValue(var8);
         if (TextUtils.isEmpty(var3)) {
            var0.requestLoaderDelegation.deleteCache();
         } else {
            var0.requestLoaderDelegation.writeToCache(var3);
         }
      } catch (MalformedURLException var6) {
         var6.printStackTrace();
      }

   }

   private void sleepIfTesting(Context var1, boolean var2) {
      if (!var1.getResources().getBoolean(R.bool.isAndroidTest) && var2) {
         throw new IllegalStateException("Delays are only available in testing.");
      } else if (var2) {
         try {
            Thread.sleep(300L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }

      }
   }

   public void deleteCache(Context var1, String var2) {
      try {
         WeakReference var6 = new WeakReference(var1);
         FileUtils.GetCache var5 = new FileUtils.GetCache(var6);
         File var4 = new File(var5.get(), var2);
         FileUtils.DeleteFileRunnable var3 = new FileUtils.DeleteFileRunnable(var4);
         ThreadUtils.postToBackgroundThread((Runnable)var3);
      } catch (InterruptedException | ExecutionException var7) {
         var7.printStackTrace();
         Log.e("CachedRequestLoader", "Failed to open cache directory when deleting cache.");
      }

   }

   public ResponseData getStringLiveData() {
      return this.requestLoaderDelegation.getStringLiveData();
   }

   public void loadFromCache(Context var1, String var2, ResponseData var3) {
      backgroundExecutorService.submit(new _$$Lambda$BackgroundCachedRequestLoader$EHwI_s8H_FtNt9t2MTbwyEF0UBA(this, var1, var2, var3));
   }

   public void loadFromRemote(Context var1, ResponseData var2, String var3, String var4, int var5) {
      backgroundExecutorService.submit(new _$$Lambda$BackgroundCachedRequestLoader$b0Blxy5LETn8C0N3GuPfyvuqAPM(this, var5, var1, var3, var4, var2));
   }

   public void writeToCache(String var1, Context var2, String var3) {
      try {
         WeakReference var7 = new WeakReference(var2);
         FileUtils.GetCache var6 = new FileUtils.GetCache(var7);
         File var5 = new File(var6.get(), var3);
         FileUtils.WriteStringToFileRunnable var4 = new FileUtils.WriteStringToFileRunnable(var5, var1);
         ThreadUtils.postToBackgroundThread((Runnable)var4);
      } catch (InterruptedException | ExecutionException var8) {
         var8.printStackTrace();
         Log.e("CachedRequestLoader", "Failed to open cache directory when writing to cache.");
      }

   }
}
