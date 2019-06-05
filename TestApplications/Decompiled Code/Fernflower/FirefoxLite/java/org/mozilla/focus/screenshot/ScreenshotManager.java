package org.mozilla.focus.screenshot;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.cachedrequestloader.BackgroundCachedRequestLoader;
import org.mozilla.cachedrequestloader.ResponseData;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.provider.ScreenshotContract;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.IOUtils;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.urlutils.UrlUtils;

public class ScreenshotManager {
   private static volatile ScreenshotManager sInstance;
   HashMap categories = new HashMap();
   private int categoryVersion = 1;
   private QueryHandler mQueryHandler;
   private ResponseData responseData;

   public static ScreenshotManager getInstance() {
      if (sInstance == null) {
         synchronized(ScreenshotManager.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (sInstance == null) {
                  ScreenshotManager var0 = new ScreenshotManager();
                  sInstance = var0;
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return sInstance;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var13 = var10000;

            try {
               throw var13;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               continue;
            }
         }
      } else {
         return sInstance;
      }
   }

   private void initFromLocal(Context var1) throws IOException {
      this.initWithJson(IOUtils.readAsset(var1, "screenshots-mapping.json"));
   }

   private boolean initFromRemote(Context var1) throws InterruptedException {
      boolean var2 = true;
      CountDownLatch var3 = new CountDownLatch(1);
      String var4 = AppConfigWrapper.getScreenshotCategoryUrl(var1);
      if (TextUtils.isEmpty(var4)) {
         return false;
      } else {
         this.responseData = (new BackgroundCachedRequestLoader(var1, "screenshot_category", var4, WebViewProvider.getUserAgentString(var1), 10003)).getStringLiveData();
         this.responseData.observeForever(new _$$Lambda$ScreenshotManager$DuDV0OUCiu80Bc2qNHJMkf_qmMs(this, var3));
         var3.await(5L, TimeUnit.SECONDS);
         if (var3.getCount() != 0L) {
            var2 = false;
         }

         return var2;
      }
   }

   private void initWithJson(JSONObject param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public static void lambda$initFromRemote$0(ScreenshotManager var0, CountDownLatch var1, Pair var2) {
      if (var2 != null) {
         try {
            String var5 = (String)var2.second;
            if (TextUtils.isEmpty(var5)) {
               return;
            }

            JSONObject var3 = new JSONObject(var5);
            var0.initWithJson(var3);
            var1.countDown();
         } catch (JSONException var4) {
            Log.e("ScreenshotManager", "ScreenshotManager init error with incorrect format: ", var4);
         }

      }
   }

   private void lazyInitCategories(Context param1) {
      // $FF: Couldn't be decompiled
   }

   public void delete(long var1, QueryHandler.AsyncDeleteListener var3) {
      this.mQueryHandler.startDelete(2, new QueryHandler.AsyncDeleteWrapper(var1, var3), ScreenshotContract.Screenshot.CONTENT_URI, "_id = ?", new String[]{Long.toString(var1)});
   }

   public String getCategory(Context var1, String var2) {
      this.lazyInitCategories(var1);

      try {
         if (this.categories.size() != 0) {
            URL var6 = new URL(var2);
            String var3 = UrlUtils.stripCommonSubdomains(var6.getAuthority());
            Iterator var8 = this.categories.entrySet().iterator();

            Entry var7;
            do {
               if (!var8.hasNext()) {
                  return "Others";
               }

               var7 = (Entry)var8.next();
            } while(!var3.endsWith((String)var7.getKey()));

            return (String)var7.getValue();
         } else {
            IllegalStateException var5 = new IllegalStateException("Screenshot category is not ready!");
            throw var5;
         }
      } catch (MalformedURLException var4) {
         return "Error";
      }
   }

   public int getCategoryVersion() {
      if (this.categories.size() != 0) {
         return this.categoryVersion;
      } else {
         throw new IllegalStateException("Screenshot category is not ready! Call init before get Version.");
      }
   }

   public void init(Context var1) {
      this.mQueryHandler = new QueryHandler(var1.getContentResolver());
   }

   public void insert(Screenshot var1, QueryHandler.AsyncInsertListener var2) {
      this.mQueryHandler.startInsert(2, var2, ScreenshotContract.Screenshot.CONTENT_URI, QueryHandler.getContentValuesFromScreenshot(var1));
   }

   public void query(int var1, int var2, QueryHandler.AsyncQueryListener var3) {
      QueryHandler var4 = this.mQueryHandler;
      StringBuilder var5 = new StringBuilder();
      var5.append(ScreenshotContract.Screenshot.CONTENT_URI.toString());
      var5.append("?offset=");
      var5.append(var1);
      var5.append("&limit=");
      var5.append(var2);
      var4.startQuery(2, var3, Uri.parse(var5.toString()), (String[])null, (String)null, (String[])null, "timestamp DESC");
   }
}
