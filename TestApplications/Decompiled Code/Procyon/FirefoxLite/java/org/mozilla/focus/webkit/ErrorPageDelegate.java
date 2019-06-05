// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.annotation.SuppressLint;
import android.webkit.WebView;

public class ErrorPageDelegate
{
    private final ErrorViewFactory<HtmlErrorViewHolder> factory;
    
    ErrorPageDelegate(final WebView webView) {
        this.factory = (ErrorViewFactory<HtmlErrorViewHolder>)new ErrorViewFactory<HtmlErrorViewHolder>() {
            @SuppressLint({ "SetJavaScriptEnabled" })
            private void bindRetryButton(final HtmlErrorViewHolder htmlErrorViewHolder) {
                htmlErrorViewHolder.webView.getSettings().setJavaScriptEnabled(true);
                htmlErrorViewHolder.webView.addJavascriptInterface((Object)new JsInterface() {
                    @Override
                    void onReloadOnJsThread() {
                        webView.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                webView.reload();
                            }
                        });
                    }
                }, "jsInterface");
            }
            
            public void onBindErrorView(final HtmlErrorViewHolder htmlErrorViewHolder, final int n, final String s, final String s2) {
                ErrorPage.loadErrorPage(htmlErrorViewHolder.webView, s2, n);
                this.bindRetryButton(htmlErrorViewHolder);
            }
            
            public void onBindSslErrorView(final HtmlErrorViewHolder htmlErrorViewHolder, final SslErrorHandler sslErrorHandler, final SslError sslError) {
                if (webView.copyBackForwardList().getCurrentItem() != null) {
                    webView.removeView(htmlErrorViewHolder.rootView);
                    ErrorPage.loadErrorPage(webView, sslError.getUrl(), sslError.getPrimaryError());
                }
            }
            
            public HtmlErrorViewHolder onCreateErrorView() {
                final WebView webView = new WebView(webView.getContext());
                final HtmlErrorViewHolder htmlErrorViewHolder = new HtmlErrorViewHolder((View)webView);
                htmlErrorViewHolder.webView = webView;
                return htmlErrorViewHolder;
            }
            
            public void onErrorViewCreated(final HtmlErrorViewHolder htmlErrorViewHolder) {
                webView.addView(htmlErrorViewHolder.rootView, new ViewGroup$LayoutParams(-1, -1));
            }
            
            public void onErrorViewDestroyed(final HtmlErrorViewHolder htmlErrorViewHolder) {
                if (htmlErrorViewHolder.webView != null) {
                    webView.removeView(htmlErrorViewHolder.rootView);
                    htmlErrorViewHolder.webView.removeJavascriptInterface("jsInterface");
                    htmlErrorViewHolder.webView.destroy();
                }
            }
        };
    }
    
    private void createErrorView() {
        ((ErrorViewFactory<ErrorViewHolder>)this.factory).createErrorView();
    }
    
    private void dismissErrorView() {
        ((ErrorViewFactory<ErrorViewHolder>)this.factory).destroyErrorView();
    }
    
    public void onPageStarted() {
        this.dismissErrorView();
    }
    
    public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
        this.createErrorView();
        this.factory.onBindErrorView((HtmlErrorViewHolder)((ErrorViewFactory<ErrorViewHolder>)this.factory).getErrorViewHolder(), n, s, s2);
    }
    
    public void onReceivedHttpError(final WebView webView, final WebResourceRequest webResourceRequest, final WebResourceResponse webResourceResponse) {
    }
    
    public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
        this.createErrorView();
        this.factory.onBindSslErrorView((HtmlErrorViewHolder)((ErrorViewFactory<ErrorViewHolder>)this.factory).getErrorViewHolder(), sslErrorHandler, sslError);
    }
    
    public void onWebViewScrolled(final int n, final int n2) {
        final ErrorViewHolder access$100 = ((ErrorViewFactory<ErrorViewHolder>)this.factory).getErrorViewHolder();
        if (access$100 != null) {
            access$100.rootView.setTranslationX((float)n);
            access$100.rootView.setTranslationY((float)n2);
        }
    }
    
    public abstract static class ErrorViewFactory<T extends ErrorViewHolder>
    {
        private T errorViewHolder;
        
        private void createErrorView() {
            if (this.errorViewHolder != null) {
                this.destroyErrorView();
            }
            this.onErrorViewCreated(this.errorViewHolder = this.onCreateErrorView());
        }
        
        private void destroyErrorView() {
            if (this.errorViewHolder != null) {
                this.onErrorViewDestroyed(this.errorViewHolder);
            }
            this.errorViewHolder = null;
        }
        
        private T getErrorViewHolder() {
            return this.errorViewHolder;
        }
        
        public abstract void onBindErrorView(final T p0, final int p1, final String p2, final String p3);
        
        public abstract void onBindSslErrorView(final T p0, final SslErrorHandler p1, final SslError p2);
        
        public abstract T onCreateErrorView();
        
        public abstract void onErrorViewCreated(final T p0);
        
        public abstract void onErrorViewDestroyed(final T p0);
    }
    
    public static class ErrorViewHolder
    {
        public View rootView;
        
        ErrorViewHolder(final View rootView) {
            this.rootView = rootView;
        }
    }
    
    public static class HtmlErrorViewHolder extends ErrorViewHolder
    {
        WebView webView;
        
        HtmlErrorViewHolder(final View view) {
            super(view);
        }
    }
    
    private abstract static class JsInterface
    {
        abstract void onReloadOnJsThread();
        
        @JavascriptInterface
        public void onRetryClicked() {
            this.onReloadOnJsThread();
        }
    }
}
