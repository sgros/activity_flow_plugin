package org.mozilla.focus.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.webkit.DefaultWebViewClient;

public class InfoFragment extends DefaultWebFragment {
   private ProgressBar progressView;

   public static InfoFragment create(String var0) {
      Bundle var1 = new Bundle();
      var1.putString("url", var0);
      InfoFragment var2 = new InfoFragment();
      var2.setArguments(var1);
      return var2;
   }

   public boolean canGoBack() {
      boolean var1;
      if (this.webView != null && this.webView.canGoBack()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public WebChromeClient createWebChromeClient() {
      return new WebChromeClient() {
         public boolean onCreateWindow(WebView var1, boolean var2, boolean var3, Message var4) {
            return false;
         }

         public void onProgressChanged(WebView var1, int var2) {
            InfoFragment.this.progressView.setProgress(var2);
         }
      };
   }

   public WebViewClient createWebViewClient() {
      return new DefaultWebViewClient(this.getContext().getApplicationContext()) {
         public void onPageFinished(WebView var1, String var2) {
            super.onPageFinished(var1, var2);
            InfoFragment.this.progressView.announceForAccessibility(InfoFragment.this.getString(2131755055));
            InfoFragment.this.progressView.setVisibility(4);
            if (InfoFragment.this.webView.getVisibility() != 0) {
               InfoFragment.this.webView.setVisibility(0);
            }

         }

         public void onPageStarted(WebView var1, String var2, Bitmap var3) {
            super.onPageStarted(var1, var2, var3);
            InfoFragment.this.progressView.announceForAccessibility(InfoFragment.this.getString(2131755054));
            InfoFragment.this.progressView.setVisibility(0);
         }
      };
   }

   public String getInitialUrl() {
      return this.getArguments().getString("url");
   }

   public void goBack() {
      if (this.webView != null) {
         this.webView.goBack();
      }

   }

   public View inflateLayout(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = var1.inflate(2131492967, var2, false);
      this.progressView = (ProgressBar)var4.findViewById(2131296576);
      var2 = (ViewGroup)var4.findViewById(2131296723);
      this.webView = (WebView)WebViewProvider.createDefaultWebView(this.getContext(), (AttributeSet)null);
      var2.addView(this.webView);
      String var5 = this.getInitialUrl();
      if (!TextUtils.isEmpty(var5) && !var5.startsWith("http://") && !var5.startsWith("https://")) {
         this.webView.setVisibility(4);
      }

      return var4;
   }
}
