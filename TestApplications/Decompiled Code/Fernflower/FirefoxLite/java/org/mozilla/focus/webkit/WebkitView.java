package org.mozilla.focus.webkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.WebViewTransport;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.rocket.tabs.TabChromeClient;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewClient;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import org.mozilla.urlutils.UrlUtils;

public class WebkitView extends NestedWebView implements TabView {
   private WebViewDebugOverlay debugOverlay;
   private DownloadCallback downloadCallback;
   private final ErrorPageDelegate errorPageDelegate;
   private String lastNonErrorPageUrl;
   private final LinkHandler linkHandler;
   private boolean shouldReloadOnAttached = false;
   private FocusWebChromeClient webChromeClient;
   private FocusWebViewClient webViewClient = new FocusWebViewClient(this.getContext().getApplicationContext()) {
      public void onPageStarted(WebView var1, String var2, Bitmap var3) {
         if (!UrlUtils.isInternalErrorURL(var2)) {
            WebkitView.this.lastNonErrorPageUrl = var2;
         }

         super.onPageStarted(var1, var2, var3);
      }
   };

   public WebkitView(Context var1, AttributeSet var2) {
      super(var1, var2);
      FocusWebViewClient var4 = this.webViewClient;
      ErrorPageDelegate var3 = new ErrorPageDelegate(this);
      this.errorPageDelegate = var3;
      var4.setErrorPageDelegate(var3);
      this.webChromeClient = new FocusWebChromeClient(this);
      this.setWebViewClient(this.webViewClient);
      this.setWebChromeClient(this.webChromeClient);
      this.setDownloadListener(this.createDownloadListener());
      this.setLongClickable(true);
      this.linkHandler = new LinkHandler(this, this);
      this.setOnLongClickListener(this.linkHandler);
      this.debugOverlay = WebViewDebugOverlay.create(var1);
      this.debugOverlay.bindWebView(this);
      this.webViewClient.setDebugOverlay(this.debugOverlay);
   }

   private DownloadListener createDownloadListener() {
      return new DownloadListener() {
         public void onDownloadStart(String var1, String var2, String var3, String var4, long var5) {
            if (AppConstants.supportsDownloadingFiles()) {
               if (WebkitView.this.downloadCallback != null) {
                  Download var7 = new Download(var1, URLUtil.guessFileName(var1, var3, var4), var2, var3, var4, var5, false);
                  WebkitView.this.downloadCallback.onDownloadStart(var7);
               }

            }
         }
      };
   }

   // $FF: synthetic method
   public static void lambda$insertBrowsingHistory$0(WebkitView var0, String var1, String var2) {
      if ("null".equals(var2)) {
         Site var3 = BrowsingHistoryManager.prepareSiteForFirstInsert(var1, var0.getTitle(), System.currentTimeMillis());
         BrowsingHistoryManager.getInstance().insert(var3, (QueryHandler.AsyncInsertListener)null);
      }
   }

   private void reloadOnAttached() {
      if (this.isAttachedToWindow()) {
         this.reload();
      } else {
         this.shouldReloadOnAttached = true;
      }

   }

   public void bindOnNewWindowCreation(Message var1) {
      if (var1.obj instanceof WebViewTransport) {
         ((WebViewTransport)var1.obj).setWebView(this);
         var1.sendToTarget();
      } else {
         throw new IllegalArgumentException("Message payload is not a WebViewTransport instance");
      }
   }

   public int getSecurityState() {
      byte var1;
      if (this.getCertificate() == null) {
         var1 = 1;
      } else {
         var1 = 2;
      }

      return var1;
   }

   public String getUrl() {
      String var1 = super.getUrl();
      return UrlUtils.isInternalErrorURL(var1) ? this.lastNonErrorPageUrl : var1;
   }

   public View getView() {
      return this;
   }

   public void goBack() {
      super.goBack();
      this.debugOverlay.updateHistory();
   }

   public void insertBrowsingHistory() {
      String var1 = this.getUrl();
      if (!TextUtils.isEmpty(var1)) {
         if (!"about:blank".equals(var1)) {
            if (UrlUtils.isHttpOrHttps(var1)) {
               this.evaluateJavascript("(function() { return document.getElementById('mozillaErrorPage'); })();", new _$$Lambda$WebkitView$0sE9kpfZXXRmRNSei_1BvwWNpgc(this, var1));
            }
         }
      }
   }

   public void loadUrl(String var1) {
      this.debugOverlay.onLoadUrlCalled();
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

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
   }

   protected void onScrollChanged(int var1, int var2, int var3, int var4) {
      super.onScrollChanged(var1, var2, var3, var4);
      this.errorPageDelegate.onWebViewScrolled(var1, var2);
      this.debugOverlay.onWebViewScrolled(var1, var2);
   }

   public void performExitFullScreen() {
      this.evaluateJavascript("(function() { return document.webkitExitFullscreen(); })();", (ValueCallback)null);
   }

   public void reload() {
      if (UrlUtils.isInternalErrorURL(this.getOriginalUrl())) {
         super.loadUrl(this.getUrl());
      } else {
         super.reload();
      }

      this.debugOverlay.updateHistory();
   }

   public void restoreViewState(Bundle var1) {
      WebBackForwardList var2 = this.restoreState(var1);
      String var3 = var1.getString("currenturl");
      if (!TextUtils.isEmpty(var3)) {
         this.webViewClient.notifyCurrentURL(var3);
         if (var2 != null) {
            WebHistoryItem var4 = var2.getCurrentItem();
            if (var4 != null) {
               String var5 = var4.getUrl();
               if (var3.equals(var5)) {
                  this.reload();
               } else if (!UrlUtils.isInternalErrorURL(var5) || !var3.equals(this.getUrl())) {
                  this.loadUrl(var3);
                  return;
               }

               return;
            }
         }

         this.loadUrl(var3);
      }
   }

   public void saveViewState(Bundle var1) {
      super.saveState(var1);
      var1.putString("currenturl", this.getUrl());
   }

   public void setChromeClient(TabChromeClient var1) {
      this.linkHandler.setChromeClient(var1);
      this.webChromeClient.setChromeClient(var1);
   }

   public void setContentBlockingEnabled(boolean var1) {
      if (this.webViewClient.isBlockingEnabled() != var1) {
         this.webViewClient.setBlockingEnabled(var1);
         if (!var1) {
            this.reloadOnAttached();
         }

      }
   }

   public void setDownloadCallback(DownloadCallback var1) {
      this.downloadCallback = var1;
   }

   public void setFindListener(TabView.FindListener var1) {
      _$$Lambda$wDLFcOhs_95LlKRiUPAKPaA5E_Y var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var1.getClass();
         var2 = new _$$Lambda$wDLFcOhs_95LlKRiUPAKPaA5E_Y(var1);
      }

      this.setFindListener(var2);
   }

   public void setImageBlockingEnabled(boolean var1) {
      WebSettings var2 = this.getSettings();
      if (var1 != var2.getBlockNetworkImage() || var1 != (var2.getLoadsImagesAutomatically() ^ true)) {
         WebViewProvider.applyAppSettings(this.getContext(), this.getSettings());
         if (var1) {
            this.reloadOnAttached();
         }

      }
   }

   public void setViewClient(TabViewClient var1) {
      this.webViewClient.setViewClient(var1);
   }
}
