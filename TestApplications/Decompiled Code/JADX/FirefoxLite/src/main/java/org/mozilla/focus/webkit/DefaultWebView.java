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
    private TrackingProtectionWebViewClient webViewClient = new DefaultWebViewClient(getContext().getApplicationContext()) {
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            if (!UrlUtils.isInternalErrorURL(str)) {
                DefaultWebView.this.lastNonErrorPageUrl = str;
            }
            super.onPageStarted(webView, str, bitmap);
        }
    };

    public DefaultWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWebViewClient(this.webViewClient);
        setWebChromeClient(this.webChromeClient);
        setLongClickable(true);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.shouldReloadOnAttached) {
            this.shouldReloadOnAttached = false;
            reload();
        }
    }

    public void loadUrl(String str) {
        if (!this.webViewClient.shouldOverrideUrlLoading(this, str)) {
            super.loadUrl(str);
        }
        this.webViewClient.notifyCurrentURL(str);
    }

    public void reload() {
        if (UrlUtils.isInternalErrorURL(getOriginalUrl())) {
            super.loadUrl(getUrl());
        } else {
            super.reload();
        }
    }

    public void goBack() {
        super.goBack();
    }

    public String getUrl() {
        String url = super.getUrl();
        return UrlUtils.isInternalErrorURL(url) ? this.lastNonErrorPageUrl : url;
    }
}
