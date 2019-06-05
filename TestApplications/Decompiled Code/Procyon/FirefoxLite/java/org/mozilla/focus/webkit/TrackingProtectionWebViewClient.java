// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import android.net.Uri;
import java.io.InputStream;
import android.webkit.WebResourceResponse;
import android.webkit.WebResourceRequest;
import android.webkit.HttpAuthHandler;
import org.mozilla.focus.web.BrowsingSession;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.os.AsyncTask;
import org.mozilla.focus.utils.Settings;
import android.content.Context;
import org.mozilla.focus.webkit.matcher.UrlMatcher;
import android.webkit.WebViewClient;

public class TrackingProtectionWebViewClient extends WebViewClient
{
    private static volatile UrlMatcher MATCHER;
    private boolean blockingEnabled;
    String currentPageURL;
    
    TrackingProtectionWebViewClient(final Context context) {
        triggerPreload(context);
        this.blockingEnabled = Settings.getInstance(context).shouldUseTurboMode();
    }
    
    private static UrlMatcher getMatcher(final Context context) {
        synchronized (TrackingProtectionWebViewClient.class) {
            if (TrackingProtectionWebViewClient.MATCHER == null) {
                TrackingProtectionWebViewClient.MATCHER = UrlMatcher.loadMatcher(context, 2131689474, new int[] { 2131689480 }, 2131689475, 2131689473);
            }
            return TrackingProtectionWebViewClient.MATCHER;
        }
    }
    
    public static void triggerPreload(final Context context) {
        if (TrackingProtectionWebViewClient.MATCHER == null) {
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(final Void... array) {
                    getMatcher(context);
                    return null;
                }
            }.execute((Object[])new Void[0]);
        }
    }
    
    public boolean isBlockingEnabled() {
        return this.blockingEnabled;
    }
    
    public void notifyCurrentURL(final String currentPageURL) {
        this.currentPageURL = currentPageURL;
    }
    
    public void onPageStarted(final WebView webView, final String currentPageURL, final Bitmap bitmap) {
        if (this.blockingEnabled) {
            BrowsingSession.getInstance().resetTrackerCount();
        }
        super.onPageStarted(webView, this.currentPageURL = currentPageURL, bitmap);
    }
    
    public void onReceivedHttpAuthRequest(final WebView webView, final HttpAuthHandler httpAuthHandler, final String s, final String s2) {
    }
    
    public void setBlockingEnabled(final boolean blockingEnabled) {
        this.blockingEnabled = blockingEnabled;
    }
    
    public WebResourceResponse shouldInterceptRequest(final WebView webView, final WebResourceRequest webResourceRequest) {
        if (!this.blockingEnabled) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }
        final Uri url = webResourceRequest.getUrl();
        final String scheme = url.getScheme();
        if (!webResourceRequest.isForMainFrame() && !scheme.equals("http") && !scheme.equals("https") && !scheme.equals("blob")) {
            return new WebResourceResponse((String)null, (String)null, (InputStream)null);
        }
        final UrlMatcher matcher = getMatcher(webView.getContext());
        if (this.currentPageURL != null && !webResourceRequest.isForMainFrame() && matcher.matches(url, Uri.parse(this.currentPageURL))) {
            BrowsingSession.getInstance().countBlockedTracker();
            return new WebResourceResponse((String)null, (String)null, (InputStream)null);
        }
        return super.shouldInterceptRequest(webView, webResourceRequest);
    }
}
