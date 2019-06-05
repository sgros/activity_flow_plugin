package org.mozilla.focus.webkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.InputStream;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.web.BrowsingSession;
import org.mozilla.focus.webkit.matcher.UrlMatcher;

public class TrackingProtectionWebViewClient extends WebViewClient {
   private static volatile UrlMatcher MATCHER;
   private boolean blockingEnabled;
   String currentPageURL;

   TrackingProtectionWebViewClient(Context var1) {
      triggerPreload(var1);
      this.blockingEnabled = Settings.getInstance(var1).shouldUseTurboMode();
   }

   private static UrlMatcher getMatcher(Context var0) {
      synchronized(TrackingProtectionWebViewClient.class){}

      UrlMatcher var3;
      try {
         if (MATCHER == null) {
            MATCHER = UrlMatcher.loadMatcher(var0, 2131689474, new int[]{2131689480}, 2131689475, 2131689473);
         }

         var3 = MATCHER;
      } finally {
         ;
      }

      return var3;
   }

   public static void triggerPreload(final Context var0) {
      if (MATCHER == null) {
         (new AsyncTask() {
            protected Void doInBackground(Void... var1) {
               TrackingProtectionWebViewClient.getMatcher(var0);
               return null;
            }
         }).execute(new Void[0]);
      }

   }

   public boolean isBlockingEnabled() {
      return this.blockingEnabled;
   }

   public void notifyCurrentURL(String var1) {
      this.currentPageURL = var1;
   }

   public void onPageStarted(WebView var1, String var2, Bitmap var3) {
      if (this.blockingEnabled) {
         BrowsingSession.getInstance().resetTrackerCount();
      }

      this.currentPageURL = var2;
      super.onPageStarted(var1, var2, var3);
   }

   public void onReceivedHttpAuthRequest(WebView var1, HttpAuthHandler var2, String var3, String var4) {
   }

   public void setBlockingEnabled(boolean var1) {
      this.blockingEnabled = var1;
   }

   public WebResourceResponse shouldInterceptRequest(WebView var1, WebResourceRequest var2) {
      if (!this.blockingEnabled) {
         return super.shouldInterceptRequest(var1, var2);
      } else {
         Uri var3 = var2.getUrl();
         String var4 = var3.getScheme();
         if (!var2.isForMainFrame() && !var4.equals("http") && !var4.equals("https") && !var4.equals("blob")) {
            return new WebResourceResponse((String)null, (String)null, (InputStream)null);
         } else {
            UrlMatcher var5 = getMatcher(var1.getContext());
            if (this.currentPageURL != null && !var2.isForMainFrame() && var5.matches(var3, Uri.parse(this.currentPageURL))) {
               BrowsingSession.getInstance().countBlockedTracker();
               return new WebResourceResponse((String)null, (String)null, (InputStream)null);
            } else {
               return super.shouldInterceptRequest(var1, var2);
            }
         }
      }
   }
}
