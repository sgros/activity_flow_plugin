package org.mozilla.focus.webkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import org.mozilla.urlutils.UrlUtils;

public class DefaultWebView extends NestedWebView {
   private String lastNonErrorPageUrl;
   private boolean shouldReloadOnAttached = false;
   private WebChromeClient webChromeClient = new WebChromeClient();
   private TrackingProtectionWebViewClient webViewClient = new DefaultWebViewClient(this.getContext().getApplicationContext()) {
      public void onPageStarted(WebView var1, String var2, Bitmap var3) {
         if (!UrlUtils.isInternalErrorURL(var2)) {
            DefaultWebView.this.lastNonErrorPageUrl = var2;
         }

         super.onPageStarted(var1, var2, var3);
      }
   };

   public DefaultWebView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setWebViewClient(this.webViewClient);
      this.setWebChromeClient(this.webChromeClient);
      this.setLongClickable(true);
   }

   public String getUrl() {
      String var1 = super.getUrl();
      return UrlUtils.isInternalErrorURL(var1) ? this.lastNonErrorPageUrl : var1;
   }

   public void goBack() {
      super.goBack();
   }

   public void loadUrl(String var1) {
      if (!this.webViewClient.shouldOverrideUrlLoading(this, var1)) {
         super.loadUrl(var1);
      }

      this.webViewClient.notifyCurrentURL(var1);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.shouldReloadOnAttached) {
         this.shouldReloadOnAttached = false;
         this.reload();
      }

   }

   public void reload() {
      if (UrlUtils.isInternalErrorURL(this.getOriginalUrl())) {
         super.loadUrl(this.getUrl());
      } else {
         super.reload();
      }

   }
}
