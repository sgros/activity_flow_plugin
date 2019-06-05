// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import android.webkit.WebViewClient;
import org.mozilla.urlutils.UrlUtils;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.util.AttributeSet;
import android.content.Context;
import android.webkit.WebChromeClient;

public class DefaultWebView extends NestedWebView
{
    private String lastNonErrorPageUrl;
    private boolean shouldReloadOnAttached;
    private WebChromeClient webChromeClient;
    private TrackingProtectionWebViewClient webViewClient;
    
    public DefaultWebView(final Context context, final AttributeSet set) {
        super(context, set);
        this.shouldReloadOnAttached = false;
        this.webViewClient = new DefaultWebViewClient(this.getContext().getApplicationContext()) {
            @Override
            public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
                if (!UrlUtils.isInternalErrorURL(s)) {
                    DefaultWebView.this.lastNonErrorPageUrl = s;
                }
                super.onPageStarted(webView, s, bitmap);
            }
        };
        this.webChromeClient = new WebChromeClient();
        this.setWebViewClient((WebViewClient)this.webViewClient);
        this.setWebChromeClient(this.webChromeClient);
        this.setLongClickable(true);
    }
    
    public String getUrl() {
        final String url = super.getUrl();
        if (UrlUtils.isInternalErrorURL(url)) {
            return this.lastNonErrorPageUrl;
        }
        return url;
    }
    
    public void goBack() {
        super.goBack();
    }
    
    public void loadUrl(final String s) {
        if (!this.webViewClient.shouldOverrideUrlLoading((WebView)this, s)) {
            super.loadUrl(s);
        }
        this.webViewClient.notifyCurrentURL(s);
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
        }
        else {
            super.reload();
        }
    }
}
