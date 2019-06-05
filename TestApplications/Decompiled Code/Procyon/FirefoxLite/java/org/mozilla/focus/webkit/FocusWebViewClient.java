// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.urlutils.UrlUtils;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import android.webkit.HttpAuthHandler;
import android.graphics.Bitmap;
import org.mozilla.focus.utils.SupportUtils;
import android.webkit.WebView;
import android.content.Context;
import org.mozilla.rocket.tabs.TabViewClient;

class FocusWebViewClient extends TrackingProtectionWebViewClient
{
    private WebViewDebugOverlay debugOverlay;
    private ErrorPageDelegate errorPageDelegate;
    private TabViewClient viewClient;
    
    FocusWebViewClient(final Context context) {
        super(context);
    }
    
    private static boolean shouldOverrideInternalPages(final WebView webView, final String s) {
        if (SupportUtils.isTemplateSupportPages(s)) {
            SupportUtils.loadSupportPages(webView, s);
            return true;
        }
        return false;
    }
    
    public void doUpdateVisitedHistory(final WebView webView, final String s, final boolean b) {
        super.doUpdateVisitedHistory(webView, s, b);
        this.debugOverlay.updateHistory();
    }
    
    public void onPageFinished(final WebView webView, final String str) {
        if (this.viewClient != null) {
            this.viewClient.onPageFinished(webView.getCertificate() != null);
        }
        super.onPageFinished(webView, str);
        this.debugOverlay.updateHistory();
        final WebViewDebugOverlay debugOverlay = this.debugOverlay;
        final StringBuilder sb = new StringBuilder();
        sb.append("onPageFinished:");
        sb.append(str);
        debugOverlay.recordLifecycle(sb.toString(), false);
    }
    
    @Override
    public void onPageStarted(final WebView webView, final String str, final Bitmap bitmap) {
        if (this.viewClient != null) {
            this.viewClient.updateFailingUrl(str, false);
            this.viewClient.onPageStarted(str);
        }
        if (this.errorPageDelegate != null) {
            this.errorPageDelegate.onPageStarted();
        }
        final WebViewDebugOverlay debugOverlay = this.debugOverlay;
        final StringBuilder sb = new StringBuilder();
        sb.append("onPageStarted:");
        sb.append(str);
        debugOverlay.recordLifecycle(sb.toString(), true);
        super.onPageStarted(webView, str, bitmap);
    }
    
    public void onReceivedError(final WebView webView, int int1, final String s, final String str) {
        if (this.viewClient != null) {
            this.viewClient.updateFailingUrl(str, true);
        }
        final WebViewDebugOverlay debugOverlay = this.debugOverlay;
        final StringBuilder sb = new StringBuilder();
        sb.append("onReceivedError:");
        sb.append(str);
        debugOverlay.recordLifecycle(sb.toString(), false);
        Label_0128: {
            if (!str.startsWith("error:")) {
                break Label_0128;
            }
            final String substring = str.substring("error:".length());
            final int n = -12;
        Block_8_Outer:
            while (true) {
                try {
                    int1 = Integer.parseInt(substring);
                    if (!ErrorPage.supportsErrorCode(int1)) {
                        int1 = n;
                    }
                    if (this.errorPageDelegate != null) {
                        this.errorPageDelegate.onReceivedError(webView, int1, s, str);
                    }
                    return;
                    // iftrue(Label_0166:, this.errorPageDelegate == null)
                    while (true) {
                        while (true) {
                            this.errorPageDelegate.onReceivedError(webView, int1, s, str);
                            return;
                            continue Block_8_Outer;
                        }
                        Label_0167: {
                            super.onReceivedError(webView, int1, s, str);
                        }
                        return;
                        Label_0166:
                        return;
                        continue;
                    }
                }
                // iftrue(Label_0167:, !str.equals((Object)this.currentPageURL) || !ErrorPage.supportsErrorCode(int1))
                catch (NumberFormatException ex) {
                    int1 = n;
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public void onReceivedHttpAuthRequest(final WebView webView, final HttpAuthHandler httpAuthHandler, final String s, final String s2) {
        final TabViewClient.HttpAuthCallback httpAuthCallback = new TabViewClient.HttpAuthCallback() {
            @Override
            public void cancel() {
                httpAuthHandler.cancel();
            }
            
            @Override
            public void proceed(final String s, final String s2) {
                httpAuthHandler.proceed(s, s2);
            }
        };
        if (this.viewClient != null) {
            this.viewClient.onHttpAuthRequest((TabViewClient.HttpAuthCallback)httpAuthCallback, s, s2);
        }
    }
    
    public void onReceivedHttpError(final WebView webView, final WebResourceRequest webResourceRequest, final WebResourceResponse webResourceResponse) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        final Uri url = webResourceRequest.getUrl();
        if (url != null) {
            final WebViewDebugOverlay debugOverlay = this.debugOverlay;
            final StringBuilder sb = new StringBuilder();
            sb.append("onReceivedHttpError:");
            sb.append(url.toString());
            debugOverlay.recordLifecycle(sb.toString(), false);
            if (webResourceRequest.isForMainFrame() && TextUtils.equals((CharSequence)this.currentPageURL, (CharSequence)url.toString()) && this.errorPageDelegate != null) {
                this.errorPageDelegate.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            }
        }
    }
    
    public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        final WebViewDebugOverlay debugOverlay = this.debugOverlay;
        final StringBuilder sb = new StringBuilder();
        sb.append("onReceivedSslError:");
        sb.append(sslError.getUrl());
        debugOverlay.recordLifecycle(sb.toString(), false);
        if (sslError.getUrl().equals(this.currentPageURL) && this.errorPageDelegate != null) {
            this.errorPageDelegate.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }
    
    final void setDebugOverlay(final WebViewDebugOverlay debugOverlay) {
        this.debugOverlay = debugOverlay;
    }
    
    public void setErrorPageDelegate(final ErrorPageDelegate errorPageDelegate) {
        this.errorPageDelegate = errorPageDelegate;
    }
    
    public void setViewClient(final TabViewClient viewClient) {
        this.viewClient = viewClient;
    }
    
    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView webView, final WebResourceRequest webResourceRequest) {
        if (webResourceRequest.isForMainFrame()) {
            final String string = webResourceRequest.getUrl().toString();
            if (this.currentPageURL != null && UrlUtils.urlsMatchExceptForTrailingSlash(this.currentPageURL, string)) {
                webView.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (FocusWebViewClient.this.viewClient != null) {
                            FocusWebViewClient.this.viewClient.onURLChanged(FocusWebViewClient.this.currentPageURL);
                        }
                    }
                });
            }
        }
        return super.shouldInterceptRequest(webView, webResourceRequest);
    }
    
    public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
        webView.getSettings().setLoadsImagesAutomatically(true);
        if (s == null) {
            if (!AppConstants.isDevBuild()) {
                return super.shouldOverrideUrlLoading(webView, "");
            }
            throw new RuntimeException("Got null url in FocsWebViewClient.shouldOverrideUrlLoading");
        }
        else {
            if (s.startsWith("https://accounts.google.com/o/oauth2/") && !s.endsWith("&suppress_webview_warning=true")) {
                webView.loadUrl(s.concat("&suppress_webview_warning=true"));
                return true;
            }
            if (shouldOverrideInternalPages(webView, s)) {
                return true;
            }
            if (s.equals("about:blank")) {
                return false;
            }
            if (!UrlUtils.isSupportedProtocol(Uri.parse(s).getScheme()) && this.viewClient != null && this.viewClient.handleExternalUrl(s)) {
                return true;
            }
            webView.getSettings().setLoadsImagesAutomatically(true ^ Settings.getInstance(webView.getContext()).shouldBlockImages());
            return super.shouldOverrideUrlLoading(webView, s);
        }
    }
}
