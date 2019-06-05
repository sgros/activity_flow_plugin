package org.mozilla.focus.webkit;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

public class ErrorPageDelegate {
   private final ErrorPageDelegate.ErrorViewFactory factory;

   ErrorPageDelegate(final WebView var1) {
      this.factory = new ErrorPageDelegate.ErrorViewFactory() {
         @SuppressLint({"SetJavaScriptEnabled"})
         private void bindRetryButton(ErrorPageDelegate.HtmlErrorViewHolder var1x) {
            var1x.webView.getSettings().setJavaScriptEnabled(true);
            var1x.webView.addJavascriptInterface(new ErrorPageDelegate.JsInterface() {
               void onReloadOnJsThread() {
                  var1.post(new Runnable() {
                     public void run() {
                        var1.reload();
                     }
                  });
               }
            }, "jsInterface");
         }

         public void onBindErrorView(ErrorPageDelegate.HtmlErrorViewHolder var1x, int var2, String var3, String var4) {
            ErrorPage.loadErrorPage(var1x.webView, var4, var2);
            this.bindRetryButton(var1x);
         }

         public void onBindSslErrorView(ErrorPageDelegate.HtmlErrorViewHolder var1x, SslErrorHandler var2, SslError var3) {
            if (var1.copyBackForwardList().getCurrentItem() != null) {
               var1.removeView(var1x.rootView);
               ErrorPage.loadErrorPage(var1, var3.getUrl(), var3.getPrimaryError());
            }

         }

         public ErrorPageDelegate.HtmlErrorViewHolder onCreateErrorView() {
            WebView var1x = new WebView(var1.getContext());
            ErrorPageDelegate.HtmlErrorViewHolder var2 = new ErrorPageDelegate.HtmlErrorViewHolder(var1x);
            var2.webView = var1x;
            return var2;
         }

         public void onErrorViewCreated(ErrorPageDelegate.HtmlErrorViewHolder var1x) {
            var1.addView(var1x.rootView, new LayoutParams(-1, -1));
         }

         public void onErrorViewDestroyed(ErrorPageDelegate.HtmlErrorViewHolder var1x) {
            if (var1x.webView != null) {
               var1.removeView(var1x.rootView);
               var1x.webView.removeJavascriptInterface("jsInterface");
               var1x.webView.destroy();
            }

         }
      };
   }

   private void createErrorView() {
      this.factory.createErrorView();
   }

   private void dismissErrorView() {
      this.factory.destroyErrorView();
   }

   public void onPageStarted() {
      this.dismissErrorView();
   }

   public void onReceivedError(WebView var1, int var2, String var3, String var4) {
      this.createErrorView();
      this.factory.onBindErrorView(this.factory.getErrorViewHolder(), var2, var3, var4);
   }

   public void onReceivedHttpError(WebView var1, WebResourceRequest var2, WebResourceResponse var3) {
   }

   public void onReceivedSslError(WebView var1, SslErrorHandler var2, SslError var3) {
      this.createErrorView();
      this.factory.onBindSslErrorView(this.factory.getErrorViewHolder(), var2, var3);
   }

   public void onWebViewScrolled(int var1, int var2) {
      ErrorPageDelegate.ErrorViewHolder var3 = this.factory.getErrorViewHolder();
      if (var3 != null) {
         var3.rootView.setTranslationX((float)var1);
         var3.rootView.setTranslationY((float)var2);
      }

   }

   public abstract static class ErrorViewFactory {
      private ErrorPageDelegate.ErrorViewHolder errorViewHolder;

      private void createErrorView() {
         if (this.errorViewHolder != null) {
            this.destroyErrorView();
         }

         ErrorPageDelegate.ErrorViewHolder var1 = this.onCreateErrorView();
         this.errorViewHolder = var1;
         this.onErrorViewCreated(var1);
      }

      private void destroyErrorView() {
         if (this.errorViewHolder != null) {
            this.onErrorViewDestroyed(this.errorViewHolder);
         }

         this.errorViewHolder = null;
      }

      private ErrorPageDelegate.ErrorViewHolder getErrorViewHolder() {
         return this.errorViewHolder;
      }

      public abstract void onBindErrorView(ErrorPageDelegate.ErrorViewHolder var1, int var2, String var3, String var4);

      public abstract void onBindSslErrorView(ErrorPageDelegate.ErrorViewHolder var1, SslErrorHandler var2, SslError var3);

      public abstract ErrorPageDelegate.ErrorViewHolder onCreateErrorView();

      public abstract void onErrorViewCreated(ErrorPageDelegate.ErrorViewHolder var1);

      public abstract void onErrorViewDestroyed(ErrorPageDelegate.ErrorViewHolder var1);
   }

   public static class ErrorViewHolder {
      public View rootView;

      ErrorViewHolder(View var1) {
         this.rootView = var1;
      }
   }

   public static class HtmlErrorViewHolder extends ErrorPageDelegate.ErrorViewHolder {
      WebView webView;

      HtmlErrorViewHolder(View var1) {
         super(var1);
      }
   }

   private abstract static class JsInterface {
      private JsInterface() {
      }

      // $FF: synthetic method
      JsInterface(Object var1) {
         this();
      }

      abstract void onReloadOnJsThread();

      @JavascriptInterface
      public void onRetryClicked() {
         this.onReloadOnJsThread();
      }
   }
}
