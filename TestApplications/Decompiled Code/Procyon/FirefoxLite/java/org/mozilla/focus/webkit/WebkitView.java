// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import org.mozilla.rocket.tabs.TabViewClient;
import android.webkit.WebSettings;
import org.mozilla.focus.web.WebViewProvider;
import android.webkit.WebView$FindListener;
import org.mozilla.rocket.tabs.TabChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebBackForwardList;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView$WebViewTransport;
import android.os.Message;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.rocket.tabs.web.Download;
import android.webkit.URLUtil;
import org.mozilla.focus.utils.AppConstants;
import android.webkit.DownloadListener;
import android.view.View$OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import org.mozilla.urlutils.UrlUtils;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.util.AttributeSet;
import android.content.Context;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import org.mozilla.rocket.tabs.TabView;

public class WebkitView extends NestedWebView implements TabView
{
    private WebViewDebugOverlay debugOverlay;
    private DownloadCallback downloadCallback;
    private final ErrorPageDelegate errorPageDelegate;
    private String lastNonErrorPageUrl;
    private final LinkHandler linkHandler;
    private boolean shouldReloadOnAttached;
    private FocusWebChromeClient webChromeClient;
    private FocusWebViewClient webViewClient;
    
    public WebkitView(final Context context, final AttributeSet set) {
        super(context, set);
        this.shouldReloadOnAttached = false;
        (this.webViewClient = new FocusWebViewClient(this.getContext().getApplicationContext()) {
            @Override
            public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
                if (!UrlUtils.isInternalErrorURL(s)) {
                    WebkitView.this.lastNonErrorPageUrl = s;
                }
                super.onPageStarted(webView, s, bitmap);
            }
        }).setErrorPageDelegate(this.errorPageDelegate = new ErrorPageDelegate(this));
        this.webChromeClient = new FocusWebChromeClient(this);
        this.setWebViewClient((WebViewClient)this.webViewClient);
        this.setWebChromeClient((WebChromeClient)this.webChromeClient);
        this.setDownloadListener(this.createDownloadListener());
        this.setLongClickable(true);
        this.setOnLongClickListener((View$OnLongClickListener)(this.linkHandler = new LinkHandler(this, this)));
        (this.debugOverlay = WebViewDebugOverlay.create(context)).bindWebView(this);
        this.webViewClient.setDebugOverlay(this.debugOverlay);
    }
    
    private DownloadListener createDownloadListener() {
        return (DownloadListener)new DownloadListener() {
            public void onDownloadStart(final String s, final String s2, final String s3, final String s4, final long n) {
                if (!AppConstants.supportsDownloadingFiles()) {
                    return;
                }
                if (WebkitView.this.downloadCallback != null) {
                    WebkitView.this.downloadCallback.onDownloadStart(new Download(s, URLUtil.guessFileName(s, s3, s4), s2, s3, s4, n, false));
                }
            }
        };
    }
    
    private void reloadOnAttached() {
        if (this.isAttachedToWindow()) {
            this.reload();
        }
        else {
            this.shouldReloadOnAttached = true;
        }
    }
    
    @Override
    public void bindOnNewWindowCreation(final Message message) {
        if (message.obj instanceof WebView$WebViewTransport) {
            ((WebView$WebViewTransport)message.obj).setWebView((WebView)this);
            message.sendToTarget();
            return;
        }
        throw new IllegalArgumentException("Message payload is not a WebViewTransport instance");
    }
    
    public int getSecurityState() {
        int n;
        if (this.getCertificate() == null) {
            n = 1;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    @Override
    public String getUrl() {
        final String url = super.getUrl();
        if (UrlUtils.isInternalErrorURL(url)) {
            return this.lastNonErrorPageUrl;
        }
        return url;
    }
    
    @Override
    public View getView() {
        return (View)this;
    }
    
    @Override
    public void goBack() {
        super.goBack();
        this.debugOverlay.updateHistory();
    }
    
    @Override
    public void insertBrowsingHistory() {
        final String url = this.getUrl();
        if (TextUtils.isEmpty((CharSequence)url)) {
            return;
        }
        if ("about:blank".equals(url)) {
            return;
        }
        if (!UrlUtils.isHttpOrHttps(url)) {
            return;
        }
        this.evaluateJavascript("(function() { return document.getElementById('mozillaErrorPage'); })();", (ValueCallback)new _$$Lambda$WebkitView$0sE9kpfZXXRmRNSei_1BvwWNpgc(this, url));
    }
    
    @Override
    public void loadUrl(final String s) {
        this.debugOverlay.onLoadUrlCalled();
        if (!this.webViewClient.shouldOverrideUrlLoading(this, s)) {
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
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    
    protected void onScrollChanged(final int n, final int n2, final int n3, final int n4) {
        super.onScrollChanged(n, n2, n3, n4);
        this.errorPageDelegate.onWebViewScrolled(n, n2);
        this.debugOverlay.onWebViewScrolled(n, n2);
    }
    
    @Override
    public void performExitFullScreen() {
        this.evaluateJavascript("(function() { return document.webkitExitFullscreen(); })();", (ValueCallback)null);
    }
    
    @Override
    public void reload() {
        if (UrlUtils.isInternalErrorURL(this.getOriginalUrl())) {
            super.loadUrl(this.getUrl());
        }
        else {
            super.reload();
        }
        this.debugOverlay.updateHistory();
    }
    
    @Override
    public void restoreViewState(final Bundle bundle) {
        final WebBackForwardList restoreState = this.restoreState(bundle);
        final String string = bundle.getString("currenturl");
        if (TextUtils.isEmpty((CharSequence)string)) {
            return;
        }
        this.webViewClient.notifyCurrentURL(string);
        if (restoreState != null) {
            final WebHistoryItem currentItem = restoreState.getCurrentItem();
            if (currentItem != null) {
                final String url = currentItem.getUrl();
                if (string.equals(url)) {
                    this.reload();
                    return;
                }
                if (UrlUtils.isInternalErrorURL(url) && string.equals(this.getUrl())) {
                    return;
                }
                this.loadUrl(string);
                return;
            }
        }
        this.loadUrl(string);
    }
    
    @Override
    public void saveViewState(final Bundle bundle) {
        super.saveState(bundle);
        bundle.putString("currenturl", this.getUrl());
    }
    
    @Override
    public void setChromeClient(final TabChromeClient tabChromeClient) {
        this.linkHandler.setChromeClient(tabChromeClient);
        this.webChromeClient.setChromeClient(tabChromeClient);
    }
    
    @Override
    public void setContentBlockingEnabled(final boolean blockingEnabled) {
        if (this.webViewClient.isBlockingEnabled() == blockingEnabled) {
            return;
        }
        this.webViewClient.setBlockingEnabled(blockingEnabled);
        if (!blockingEnabled) {
            this.reloadOnAttached();
        }
    }
    
    @Override
    public void setDownloadCallback(final DownloadCallback downloadCallback) {
        this.downloadCallback = downloadCallback;
    }
    
    @Override
    public void setFindListener(final FindListener findListener) {
        Object findListener2;
        if (findListener == null) {
            findListener2 = null;
        }
        else {
            findListener.getClass();
            findListener2 = new _$$Lambda$wDLFcOhs_95LlKRiUPAKPaA5E_Y(findListener);
        }
        this.setFindListener((WebView$FindListener)findListener2);
    }
    
    @Override
    public void setImageBlockingEnabled(final boolean b) {
        final WebSettings settings = this.getSettings();
        if (b == settings.getBlockNetworkImage() && b == (settings.getLoadsImagesAutomatically() ^ true)) {
            return;
        }
        WebViewProvider.applyAppSettings(this.getContext(), this.getSettings());
        if (b) {
            this.reloadOnAttached();
        }
    }
    
    @Override
    public void setViewClient(final TabViewClient viewClient) {
        this.webViewClient.setViewClient(viewClient);
    }
}
