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
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.WebViewTransport;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.rocket.tabs.TabChromeClient;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabView.FindListener;
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
    private FocusWebViewClient webViewClient = new FocusWebViewClient(getContext().getApplicationContext()) {
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            if (!UrlUtils.isInternalErrorURL(str)) {
                WebkitView.this.lastNonErrorPageUrl = str;
            }
            super.onPageStarted(webView, str, bitmap);
        }
    };

    /* renamed from: org.mozilla.focus.webkit.WebkitView$3 */
    class C05573 implements DownloadListener {
        C05573() {
        }

        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
            if (AppConstants.supportsDownloadingFiles() && WebkitView.this.downloadCallback != null) {
                String str5 = str;
                String str6 = str4;
                String str7 = str;
                WebkitView.this.downloadCallback.onDownloadStart(new Download(str7, URLUtil.guessFileName(str, str3, str6), str2, str3, str6, j, false));
            }
        }
    }

    public View getView() {
        return this;
    }

    public WebkitView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        FocusWebViewClient focusWebViewClient = this.webViewClient;
        ErrorPageDelegate errorPageDelegate = new ErrorPageDelegate(this);
        this.errorPageDelegate = errorPageDelegate;
        focusWebViewClient.setErrorPageDelegate(errorPageDelegate);
        this.webChromeClient = new FocusWebChromeClient(this);
        setWebViewClient(this.webViewClient);
        setWebChromeClient(this.webChromeClient);
        setDownloadListener(createDownloadListener());
        setLongClickable(true);
        this.linkHandler = new LinkHandler(this, this);
        setOnLongClickListener(this.linkHandler);
        this.debugOverlay = WebViewDebugOverlay.create(context);
        this.debugOverlay.bindWebView(this);
        this.webViewClient.setDebugOverlay(this.debugOverlay);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.shouldReloadOnAttached) {
            this.shouldReloadOnAttached = false;
            reload();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        this.errorPageDelegate.onWebViewScrolled(i, i2);
        this.debugOverlay.onWebViewScrolled(i, i2);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void restoreViewState(Bundle bundle) {
        WebBackForwardList restoreState = restoreState(bundle);
        String string = bundle.getString("currenturl");
        if (!TextUtils.isEmpty(string)) {
            this.webViewClient.notifyCurrentURL(string);
            if (restoreState != null) {
                WebHistoryItem currentItem = restoreState.getCurrentItem();
                if (currentItem != null) {
                    String url = currentItem.getUrl();
                    if (string.equals(url)) {
                        reload();
                    } else if (!(UrlUtils.isInternalErrorURL(url) && string.equals(getUrl()))) {
                        loadUrl(string);
                    }
                }
            }
            loadUrl(string);
        }
    }

    public void saveViewState(Bundle bundle) {
        super.saveState(bundle);
        bundle.putString("currenturl", getUrl());
    }

    public void setContentBlockingEnabled(boolean z) {
        if (this.webViewClient.isBlockingEnabled() != z) {
            this.webViewClient.setBlockingEnabled(z);
            if (!z) {
                reloadOnAttached();
            }
        }
    }

    public void bindOnNewWindowCreation(Message message) {
        if (message.obj instanceof WebViewTransport) {
            ((WebViewTransport) message.obj).setWebView(this);
            message.sendToTarget();
            return;
        }
        throw new IllegalArgumentException("Message payload is not a WebViewTransport instance");
    }

    public void setImageBlockingEnabled(boolean z) {
        WebSettings settings = getSettings();
        if (z != settings.getBlockNetworkImage() || z != (settings.getLoadsImagesAutomatically() ^ 1)) {
            WebViewProvider.applyAppSettings(getContext(), getSettings());
            if (z) {
                reloadOnAttached();
            }
        }
    }

    public void performExitFullScreen() {
        evaluateJavascript("(function() { return document.webkitExitFullscreen(); })();", null);
    }

    public void setViewClient(TabViewClient tabViewClient) {
        this.webViewClient.setViewClient(tabViewClient);
    }

    public void setChromeClient(TabChromeClient tabChromeClient) {
        this.linkHandler.setChromeClient(tabChromeClient);
        this.webChromeClient.setChromeClient(tabChromeClient);
    }

    public void setDownloadCallback(DownloadCallback downloadCallback) {
        this.downloadCallback = downloadCallback;
    }

    public void setFindListener(FindListener findListener) {
        WebView.FindListener findListener2;
        if (findListener == null) {
            findListener2 = null;
        } else {
            findListener.getClass();
            findListener2 = new C0547-$$Lambda$wDLFcOhs-95LlKRiUPAKPaA5E_Y(findListener);
        }
        setFindListener(findListener2);
    }

    public void loadUrl(String str) {
        this.debugOverlay.onLoadUrlCalled();
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
        this.debugOverlay.updateHistory();
    }

    public void goBack() {
        super.goBack();
        this.debugOverlay.updateHistory();
    }

    public String getUrl() {
        String url = super.getUrl();
        return UrlUtils.isInternalErrorURL(url) ? this.lastNonErrorPageUrl : url;
    }

    private void reloadOnAttached() {
        if (isAttachedToWindow()) {
            reload();
        } else {
            this.shouldReloadOnAttached = true;
        }
    }

    public int getSecurityState() {
        return getCertificate() == null ? 1 : 2;
    }

    public void insertBrowsingHistory() {
        String url = getUrl();
        if (!TextUtils.isEmpty(url) && !"about:blank".equals(url) && UrlUtils.isHttpOrHttps(url)) {
            evaluateJavascript("(function() { return document.getElementById('mozillaErrorPage'); })();", new C0546-$$Lambda$WebkitView$0sE9kpfZXXRmRNSei-1BvwWNpgc(this, url));
        }
    }

    public static /* synthetic */ void lambda$insertBrowsingHistory$0(WebkitView webkitView, String str, String str2) {
        if ("null".equals(str2)) {
            BrowsingHistoryManager.getInstance().insert(BrowsingHistoryManager.prepareSiteForFirstInsert(str, webkitView.getTitle(), System.currentTimeMillis()), null);
        }
    }

    private DownloadListener createDownloadListener() {
        return new C05573();
    }
}
