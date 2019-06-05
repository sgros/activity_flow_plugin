// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit;

import android.webkit.WebChromeClient$FileChooserParams;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient$CustomViewCallback;
import android.view.View;
import java.util.concurrent.ExecutionException;
import org.mozilla.rocket.util.LoggerWrapper;
import org.mozilla.icon.FavIconUtils;
import android.graphics.Bitmap$CompressFormat;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.fileutils.FileUtils;
import android.content.Context;
import java.lang.ref.WeakReference;
import android.text.TextUtils;
import android.graphics.Bitmap;
import android.webkit.PermissionRequest;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.webkit.GeolocationPermissions$Callback;
import android.os.Message;
import android.webkit.WebView;
import org.mozilla.rocket.tabs.TabChromeClient;
import org.mozilla.rocket.tabs.TabView;
import android.webkit.WebChromeClient;

class FocusWebChromeClient extends WebChromeClient
{
    private TabView host;
    private TabChromeClient tabChromeClient;
    
    FocusWebChromeClient(final TabView host) {
        this.host = host;
    }
    
    public void onCloseWindow(final WebView webView) {
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onCloseWindow(this.host);
        }
    }
    
    public boolean onCreateWindow(final WebView webView, final boolean b, final boolean b2, final Message message) {
        return this.tabChromeClient != null && this.tabChromeClient.onCreateWindow(b, b2, message);
    }
    
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }
    
    public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
        TelemetryWrapper.browseGeoLocationPermissionEvent();
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onGeolocationPermissionsShowPrompt(s, geolocationPermissions$Callback);
        }
    }
    
    public void onHideCustomView() {
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onExitFullScreen();
        }
        TelemetryWrapper.browseExitFullScreenEvent();
    }
    
    public void onPermissionRequest(final PermissionRequest permissionRequest) {
        super.onPermissionRequest(permissionRequest);
        TelemetryWrapper.browsePermissionEvent(permissionRequest.getResources());
    }
    
    public void onProgressChanged(final WebView webView, final int n) {
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onProgressChanged(n);
        }
    }
    
    public void onReceivedIcon(final WebView webView, final Bitmap bitmap) {
        final String url = webView.getUrl();
        if (TextUtils.isEmpty((CharSequence)url)) {
            return;
        }
        final String title = webView.getTitle();
        try {
            new FavIconUtils.SaveBitmapTask(new FileUtils.GetFaviconFolder(new WeakReference<Context>(webView.getContext())).get(), url, bitmap, new BrowsingHistoryManager.UpdateHistoryWrapper(title, url), Bitmap$CompressFormat.PNG, 0).execute((Object[])new Void[0]);
        }
        catch (ExecutionException | InterruptedException ex) {
            LoggerWrapper.throwOrWarn("FocusWebChromeClient", "Failed to get cache folder in onReceivedIcon.");
        }
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onReceivedIcon(this.host, bitmap);
        }
    }
    
    public void onReceivedTitle(final WebView webView, final String s) {
        super.onReceivedTitle(webView, s);
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onReceivedTitle(this.host, s);
        }
    }
    
    public void onShowCustomView(final View view, final WebChromeClient$CustomViewCallback webChromeClient$CustomViewCallback) {
        final TabView.FullscreenCallback fullscreenCallback = new TabView.FullscreenCallback() {
            @Override
            public void fullScreenExited() {
                webChromeClient$CustomViewCallback.onCustomViewHidden();
            }
        };
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onEnterFullScreen(fullscreenCallback, view);
        }
        TelemetryWrapper.browseEnterFullScreenEvent();
    }
    
    public boolean onShowFileChooser(final WebView webView, final ValueCallback<Uri[]> valueCallback, final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
        return this.tabChromeClient != null && this.tabChromeClient.onShowFileChooser(this.host, valueCallback, webChromeClient$FileChooserParams);
    }
    
    public void setChromeClient(final TabChromeClient tabChromeClient) {
        this.tabChromeClient = tabChromeClient;
    }
}
