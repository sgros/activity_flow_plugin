package org.mozilla.focus.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.SupportUtils;

public abstract class DefaultWebFragment extends LocaleAwareFragment {
   private boolean isWebViewAvailable;
   private String pendingUrl = null;
   protected WebView webView;
   private Bundle webViewState;

   public void applyLocale() {
      (new WebView(this.getContext())).destroy();
   }

   public abstract WebChromeClient createWebChromeClient();

   public abstract WebViewClient createWebViewClient();

   public abstract String getInitialUrl();

   protected WebView getWebView() {
      WebView var1;
      if (this.isWebViewAvailable) {
         var1 = this.webView;
      } else {
         var1 = null;
      }

      return var1;
   }

   public abstract View inflateLayout(LayoutInflater var1, ViewGroup var2, Bundle var3);

   public void loadUrl(String var1) {
      WebView var2 = this.getWebView();
      if (var2 != null) {
         if (SupportUtils.isUrl(var1)) {
            this.pendingUrl = null;
            if (!AppConstants.isDevBuild()) {
               var1 = SupportUtils.normalize(var1);
            }

            var2.loadUrl(var1);
         } else if (AppConstants.isDevBuild()) {
            StringBuilder var3 = new StringBuilder();
            var3.append("trying to open a invalid url: ");
            var3.append(var1);
            throw new RuntimeException(var3.toString());
         }
      } else {
         this.pendingUrl = var1;
      }

   }

   public final View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = this.inflateLayout(var1, var2, var3);
      this.isWebViewAvailable = true;
      WebViewClient var5 = this.createWebViewClient();
      this.webView.setWebViewClient(var5);
      this.webView.setWebChromeClient(this.createWebChromeClient());
      return var4;
   }

   public void onDestroy() {
      if (this.webView != null) {
         this.webView.destroy();
         this.webView = null;
      }

      super.onDestroy();
   }

   public void onDestroyView() {
      this.isWebViewAvailable = false;
      if (this.webView != null) {
         this.webView.setWebViewClient((WebViewClient)null);
         this.webView.setWebChromeClient((WebChromeClient)null);
         this.webViewState = new Bundle();
         this.webView.saveState(this.webViewState);
      }

      super.onDestroyView();
   }

   public void onPause() {
      this.webView.onPause();
      super.onPause();
   }

   public void onResume() {
      this.webView.onResume();
      super.onResume();
   }

   public void onSaveInstanceState(Bundle var1) {
      this.webView.saveState(var1);
      if (var1.containsKey("WEBVIEW_CHROMIUM_STATE") && var1.getByteArray("WEBVIEW_CHROMIUM_STATE").length > 300000) {
         var1.remove("WEBVIEW_CHROMIUM_STATE");
      }

      super.onSaveInstanceState(var1);
   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      if (var2 == null) {
         if (this.webViewState != null) {
            this.webView.restoreState(this.webViewState);
         }

         String var3;
         if (this.webViewState == null) {
            var3 = this.getInitialUrl();
         } else {
            var3 = this.pendingUrl;
         }

         if (!TextUtils.isEmpty(var3)) {
            this.loadUrl(var3);
         }
      } else {
         this.webView.restoreState(var2);
      }

   }
}
