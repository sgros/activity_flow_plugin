package org.mozilla.focus.webkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.adjust.sdk.Constants;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.web.BrowsingSession;
import org.mozilla.focus.webkit.matcher.UrlMatcher;
import org.mozilla.rocket.C0769R;

public class TrackingProtectionWebViewClient extends WebViewClient {
    private static volatile UrlMatcher MATCHER;
    private boolean blockingEnabled;
    String currentPageURL;

    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
    }

    public static void triggerPreload(final Context context) {
        if (MATCHER == null) {
            new AsyncTask<Void, Void, Void>() {
                /* Access modifiers changed, original: protected|varargs */
                public Void doInBackground(Void... voidArr) {
                    TrackingProtectionWebViewClient.getMatcher(context);
                    return null;
                }
            }.execute(new Void[0]);
        }
    }

    private static synchronized UrlMatcher getMatcher(Context context) {
        UrlMatcher urlMatcher;
        synchronized (TrackingProtectionWebViewClient.class) {
            if (MATCHER == null) {
                MATCHER = UrlMatcher.loadMatcher(context, C0769R.raw.blocklist, new int[]{C0769R.raw.google_mapping}, C0769R.raw.entitylist, C0769R.raw.abpindo_adservers);
            }
            urlMatcher = MATCHER;
        }
        return urlMatcher;
    }

    TrackingProtectionWebViewClient(Context context) {
        triggerPreload(context);
        this.blockingEnabled = Settings.getInstance(context).shouldUseTurboMode();
    }

    public void setBlockingEnabled(boolean z) {
        this.blockingEnabled = z;
    }

    public boolean isBlockingEnabled() {
        return this.blockingEnabled;
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        if (!this.blockingEnabled) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }
        Uri url = webResourceRequest.getUrl();
        String scheme = url.getScheme();
        if (!webResourceRequest.isForMainFrame() && !scheme.equals("http") && !scheme.equals(Constants.SCHEME) && !scheme.equals("blob")) {
            return new WebResourceResponse(null, null, null);
        }
        UrlMatcher matcher = getMatcher(webView.getContext());
        if (this.currentPageURL == null || webResourceRequest.isForMainFrame() || !matcher.matches(url, Uri.parse(this.currentPageURL))) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }
        BrowsingSession.getInstance().countBlockedTracker();
        return new WebResourceResponse(null, null, null);
    }

    public void notifyCurrentURL(String str) {
        this.currentPageURL = str;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (this.blockingEnabled) {
            BrowsingSession.getInstance().resetTrackerCount();
        }
        this.currentPageURL = str;
        super.onPageStarted(webView, str, bitmap);
    }
}
