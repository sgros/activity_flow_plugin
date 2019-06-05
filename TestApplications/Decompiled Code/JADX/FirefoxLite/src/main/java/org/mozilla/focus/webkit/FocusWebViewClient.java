package org.mozilla.focus.webkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.rocket.tabs.TabViewClient;
import org.mozilla.rocket.tabs.TabViewClient.HttpAuthCallback;
import org.mozilla.urlutils.UrlUtils;

class FocusWebViewClient extends TrackingProtectionWebViewClient {
    private WebViewDebugOverlay debugOverlay;
    private ErrorPageDelegate errorPageDelegate;
    private TabViewClient viewClient;

    /* renamed from: org.mozilla.focus.webkit.FocusWebViewClient$1 */
    class C05521 implements Runnable {
        C05521() {
        }

        public void run() {
            if (FocusWebViewClient.this.viewClient != null) {
                FocusWebViewClient.this.viewClient.onURLChanged(FocusWebViewClient.this.currentPageURL);
            }
        }
    }

    FocusWebViewClient(Context context) {
        super(context);
    }

    public void setViewClient(TabViewClient tabViewClient) {
        this.viewClient = tabViewClient;
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        if (webResourceRequest.isForMainFrame()) {
            String uri = webResourceRequest.getUrl().toString();
            if (this.currentPageURL != null && UrlUtils.urlsMatchExceptForTrailingSlash(this.currentPageURL, uri)) {
                webView.post(new C05521());
            }
        }
        return super.shouldInterceptRequest(webView, webResourceRequest);
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (this.viewClient != null) {
            this.viewClient.updateFailingUrl(str, false);
            this.viewClient.onPageStarted(str);
        }
        if (this.errorPageDelegate != null) {
            this.errorPageDelegate.onPageStarted();
        }
        WebViewDebugOverlay webViewDebugOverlay = this.debugOverlay;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onPageStarted:");
        stringBuilder.append(str);
        webViewDebugOverlay.recordLifecycle(stringBuilder.toString(), true);
        super.onPageStarted(webView, str, bitmap);
    }

    public void onPageFinished(WebView webView, String str) {
        if (this.viewClient != null) {
            this.viewClient.onPageFinished(webView.getCertificate() != null);
        }
        super.onPageFinished(webView, str);
        this.debugOverlay.updateHistory();
        WebViewDebugOverlay webViewDebugOverlay = this.debugOverlay;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onPageFinished:");
        stringBuilder.append(str);
        webViewDebugOverlay.recordLifecycle(stringBuilder.toString(), false);
    }

    private static boolean shouldOverrideInternalPages(WebView webView, String str) {
        if (!SupportUtils.isTemplateSupportPages(str)) {
            return false;
        }
        SupportUtils.loadSupportPages(webView, str);
        return true;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        webView.getSettings().setLoadsImagesAutomatically(true);
        if (str == null) {
            if (!AppConstants.isDevBuild()) {
                return super.shouldOverrideUrlLoading(webView, "");
            }
            throw new RuntimeException("Got null url in FocsWebViewClient.shouldOverrideUrlLoading");
        } else if (str.startsWith("https://accounts.google.com/o/oauth2/") && !str.endsWith("&suppress_webview_warning=true")) {
            webView.loadUrl(str.concat("&suppress_webview_warning=true"));
            return true;
        } else if (shouldOverrideInternalPages(webView, str)) {
            return true;
        } else {
            if (str.equals("about:blank")) {
                return false;
            }
            if (!UrlUtils.isSupportedProtocol(Uri.parse(str).getScheme()) && this.viewClient != null && this.viewClient.handleExternalUrl(str)) {
                return true;
            }
            webView.getSettings().setLoadsImagesAutomatically(1 ^ Settings.getInstance(webView.getContext()).shouldBlockImages());
            return super.shouldOverrideUrlLoading(webView, str);
        }
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        WebViewDebugOverlay webViewDebugOverlay = this.debugOverlay;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onReceivedSslError:");
        stringBuilder.append(sslError.getUrl());
        webViewDebugOverlay.recordLifecycle(stringBuilder.toString(), false);
        if (sslError.getUrl().equals(this.currentPageURL) && this.errorPageDelegate != null) {
            this.errorPageDelegate.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        if (this.viewClient != null) {
            this.viewClient.updateFailingUrl(str2, true);
        }
        WebViewDebugOverlay webViewDebugOverlay = this.debugOverlay;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onReceivedError:");
        stringBuilder.append(str2);
        webViewDebugOverlay.recordLifecycle(stringBuilder.toString(), false);
        if (str2.startsWith("error:")) {
            int i2 = -12;
            try {
                i = Integer.parseInt(str2.substring("error:".length()));
                if (ErrorPage.supportsErrorCode(i)) {
                    i2 = i;
                }
            } catch (NumberFormatException unused) {
            }
            if (this.errorPageDelegate != null) {
                this.errorPageDelegate.onReceivedError(webView, i2, str, str2);
            }
        } else if (str2.equals(this.currentPageURL) && ErrorPage.supportsErrorCode(i)) {
            if (this.errorPageDelegate != null) {
                this.errorPageDelegate.onReceivedError(webView, i, str, str2);
            }
        } else {
            super.onReceivedError(webView, i, str, str2);
        }
    }

    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        Uri url = webResourceRequest.getUrl();
        if (url != null) {
            WebViewDebugOverlay webViewDebugOverlay = this.debugOverlay;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onReceivedHttpError:");
            stringBuilder.append(url.toString());
            webViewDebugOverlay.recordLifecycle(stringBuilder.toString(), false);
            if (webResourceRequest.isForMainFrame() && TextUtils.equals(this.currentPageURL, url.toString()) && this.errorPageDelegate != null) {
                this.errorPageDelegate.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            }
        }
    }

    public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
        super.doUpdateVisitedHistory(webView, str, z);
        this.debugOverlay.updateHistory();
    }

    public void onReceivedHttpAuthRequest(WebView webView, final HttpAuthHandler httpAuthHandler, String str, String str2) {
        C07482 c07482 = new HttpAuthCallback() {
            public void proceed(String str, String str2) {
                httpAuthHandler.proceed(str, str2);
            }

            public void cancel() {
                httpAuthHandler.cancel();
            }
        };
        if (this.viewClient != null) {
            this.viewClient.onHttpAuthRequest(c07482, str, str2);
        }
    }

    /* Access modifiers changed, original: final */
    public final void setDebugOverlay(WebViewDebugOverlay webViewDebugOverlay) {
        this.debugOverlay = webViewDebugOverlay;
    }

    public void setErrorPageDelegate(ErrorPageDelegate errorPageDelegate) {
        this.errorPageDelegate = errorPageDelegate;
    }
}
