package org.mozilla.focus.webkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.rocket.tabs.TabViewClient;
import org.mozilla.urlutils.UrlUtils;

class FocusWebViewClient extends TrackingProtectionWebViewClient {
   private WebViewDebugOverlay debugOverlay;
   private ErrorPageDelegate errorPageDelegate;
   private TabViewClient viewClient;

   FocusWebViewClient(Context var1) {
      super(var1);
   }

   private static boolean shouldOverrideInternalPages(WebView var0, String var1) {
      if (SupportUtils.isTemplateSupportPages(var1)) {
         SupportUtils.loadSupportPages(var0, var1);
         return true;
      } else {
         return false;
      }
   }

   public void doUpdateVisitedHistory(WebView var1, String var2, boolean var3) {
      super.doUpdateVisitedHistory(var1, var2, var3);
      this.debugOverlay.updateHistory();
   }

   public void onPageFinished(WebView var1, String var2) {
      if (this.viewClient != null) {
         TabViewClient var3 = this.viewClient;
         boolean var4;
         if (var1.getCertificate() != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         var3.onPageFinished(var4);
      }

      super.onPageFinished(var1, var2);
      this.debugOverlay.updateHistory();
      WebViewDebugOverlay var6 = this.debugOverlay;
      StringBuilder var5 = new StringBuilder();
      var5.append("onPageFinished:");
      var5.append(var2);
      var6.recordLifecycle(var5.toString(), false);
   }

   public void onPageStarted(WebView var1, String var2, Bitmap var3) {
      if (this.viewClient != null) {
         this.viewClient.updateFailingUrl(var2, false);
         this.viewClient.onPageStarted(var2);
      }

      if (this.errorPageDelegate != null) {
         this.errorPageDelegate.onPageStarted();
      }

      WebViewDebugOverlay var4 = this.debugOverlay;
      StringBuilder var5 = new StringBuilder();
      var5.append("onPageStarted:");
      var5.append(var2);
      var4.recordLifecycle(var5.toString(), true);
      super.onPageStarted(var1, var2, var3);
   }

   public void onReceivedError(WebView var1, int var2, String var3, String var4) {
      if (this.viewClient != null) {
         this.viewClient.updateFailingUrl(var4, true);
      }

      WebViewDebugOverlay var5 = this.debugOverlay;
      StringBuilder var6 = new StringBuilder();
      var6.append("onReceivedError:");
      var6.append(var4);
      var5.recordLifecycle(var6.toString(), false);
      if (var4.startsWith("error:")) {
         String var10 = var4.substring("error:".length());
         byte var7 = -12;

         label34: {
            boolean var8;
            try {
               var2 = Integer.parseInt(var10);
               var8 = ErrorPage.supportsErrorCode(var2);
            } catch (NumberFormatException var9) {
               var2 = var7;
               break label34;
            }

            if (!var8) {
               var2 = var7;
            }
         }

         if (this.errorPageDelegate != null) {
            this.errorPageDelegate.onReceivedError(var1, var2, var3, var4);
         }

      } else if (var4.equals(this.currentPageURL) && ErrorPage.supportsErrorCode(var2)) {
         if (this.errorPageDelegate != null) {
            this.errorPageDelegate.onReceivedError(var1, var2, var3, var4);
         }

      } else {
         super.onReceivedError(var1, var2, var3, var4);
      }
   }

   public void onReceivedHttpAuthRequest(WebView var1, final HttpAuthHandler var2, String var3, String var4) {
      TabViewClient.HttpAuthCallback var5 = new TabViewClient.HttpAuthCallback() {
         public void cancel() {
            var2.cancel();
         }

         public void proceed(String var1, String var2x) {
            var2.proceed(var1, var2x);
         }
      };
      if (this.viewClient != null) {
         this.viewClient.onHttpAuthRequest(var5, var3, var4);
      }

   }

   public void onReceivedHttpError(WebView var1, WebResourceRequest var2, WebResourceResponse var3) {
      super.onReceivedHttpError(var1, var2, var3);
      Uri var4 = var2.getUrl();
      if (var4 != null) {
         WebViewDebugOverlay var5 = this.debugOverlay;
         StringBuilder var6 = new StringBuilder();
         var6.append("onReceivedHttpError:");
         var6.append(var4.toString());
         var5.recordLifecycle(var6.toString(), false);
         if (var2.isForMainFrame() && TextUtils.equals(this.currentPageURL, var4.toString()) && this.errorPageDelegate != null) {
            this.errorPageDelegate.onReceivedHttpError(var1, var2, var3);
         }
      }

   }

   public void onReceivedSslError(WebView var1, SslErrorHandler var2, SslError var3) {
      super.onReceivedSslError(var1, var2, var3);
      WebViewDebugOverlay var4 = this.debugOverlay;
      StringBuilder var5 = new StringBuilder();
      var5.append("onReceivedSslError:");
      var5.append(var3.getUrl());
      var4.recordLifecycle(var5.toString(), false);
      if (var3.getUrl().equals(this.currentPageURL) && this.errorPageDelegate != null) {
         this.errorPageDelegate.onReceivedSslError(var1, var2, var3);
      }

   }

   final void setDebugOverlay(WebViewDebugOverlay var1) {
      this.debugOverlay = var1;
   }

   public void setErrorPageDelegate(ErrorPageDelegate var1) {
      this.errorPageDelegate = var1;
   }

   public void setViewClient(TabViewClient var1) {
      this.viewClient = var1;
   }

   public WebResourceResponse shouldInterceptRequest(WebView var1, WebResourceRequest var2) {
      if (var2.isForMainFrame()) {
         String var3 = var2.getUrl().toString();
         if (this.currentPageURL != null && UrlUtils.urlsMatchExceptForTrailingSlash(this.currentPageURL, var3)) {
            var1.post(new Runnable() {
               public void run() {
                  if (FocusWebViewClient.this.viewClient != null) {
                     FocusWebViewClient.this.viewClient.onURLChanged(FocusWebViewClient.this.currentPageURL);
                  }

               }
            });
         }
      }

      return super.shouldInterceptRequest(var1, var2);
   }

   public boolean shouldOverrideUrlLoading(WebView var1, String var2) {
      var1.getSettings().setLoadsImagesAutomatically(true);
      if (var2 == null) {
         if (!AppConstants.isDevBuild()) {
            return super.shouldOverrideUrlLoading(var1, "");
         } else {
            throw new RuntimeException("Got null url in FocsWebViewClient.shouldOverrideUrlLoading");
         }
      } else if (var2.startsWith("https://accounts.google.com/o/oauth2/") && !var2.endsWith("&suppress_webview_warning=true")) {
         var1.loadUrl(var2.concat("&suppress_webview_warning=true"));
         return true;
      } else if (shouldOverrideInternalPages(var1, var2)) {
         return true;
      } else if (var2.equals("about:blank")) {
         return false;
      } else if (!UrlUtils.isSupportedProtocol(Uri.parse(var2).getScheme()) && this.viewClient != null && this.viewClient.handleExternalUrl(var2)) {
         return true;
      } else {
         var1.getSettings().setLoadsImagesAutomatically(true ^ Settings.getInstance(var1.getContext()).shouldBlockImages());
         return super.shouldOverrideUrlLoading(var1, var2);
      }
   }
}
