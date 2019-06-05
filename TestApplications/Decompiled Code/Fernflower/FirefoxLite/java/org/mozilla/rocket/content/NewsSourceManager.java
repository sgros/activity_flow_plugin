package org.mozilla.rocket.content;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import java.util.Random;
import org.mozilla.focus.utils.Settings;
import org.mozilla.threadutils.ThreadUtils;

public class NewsSourceManager {
   private static NewsSourceManager instance = new NewsSourceManager();
   private boolean loadHasBeenTriggered;
   private String newsSource = null;
   private String newsSourceUrl = "";

   private NewsSourceManager() {
   }

   private void awaitLoadingNewsSourceLocked() {
      if (!this.loadHasBeenTriggered) {
         throw new IllegalStateException("Attempting to retrieve search engines without a corresponding init()");
      } else {
         while(this.newsSource == null) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
            }
         }

      }
   }

   public static NewsSourceManager getInstance() {
      return instance;
   }

   // $FF: synthetic method
   public static void lambda$init$0(NewsSourceManager var0, Context var1) {
      var0.loadHasBeenTriggered = true;
      Settings var2 = Settings.getInstance(var1);
      StringBuilder var3;
      if (TextUtils.isEmpty(var2.getNewsSource())) {
         if ((new Random()).nextInt(1) % 2 == 0) {
            var0.newsSource = "DainikBhaskar.com";
         } else {
            var0.newsSource = "Newspoint";
         }

         var3 = new StringBuilder();
         var3.append("NewsSourceManager sets default:");
         var3.append(var0.newsSource);
         Log.d("NewsSource", var3.toString());
         var2.setNewsSource(var0.newsSource);
         var2.setPriority("pref_int_news_priority", 0);
      } else {
         var0.newsSource = var2.getNewsSource();
         var3 = new StringBuilder();
         var3.append("NewsSourceManager already set:");
         var3.append(var0.newsSource);
         Log.d("NewsSource", var3.toString());
      }

   }

   public String getNewsSource() {
      this.awaitLoadingNewsSourceLocked();
      return this.newsSource;
   }

   public String getNewsSourceUrl() {
      return this.newsSourceUrl;
   }

   public void init(Context var1) {
      ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$NewsSourceManager$0RgRaWEZjKRkWCKW_LiJ_UBTgPE(this, var1)));
   }

   public void setNewsSource(String var1) {
      this.newsSource = var1;
      NewsRepository.reset();
   }

   public void setNewsSourceUrl(String var1) {
      this.newsSourceUrl = var1;
      NewsRepository.resetSubscriptionUrl(var1);
   }
}
