package org.mozilla.focus.webkit;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebView;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import org.mozilla.fileutils.FileUtils.GetFaviconFolder;
import org.mozilla.focus.history.BrowsingHistoryManager.UpdateHistoryWrapper;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.icon.FavIconUtils.SaveBitmapTask;
import org.mozilla.rocket.tabs.TabChromeClient;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.util.LoggerWrapper;

class FocusWebChromeClient extends WebChromeClient {
    private TabView host;
    private TabChromeClient tabChromeClient;

    FocusWebChromeClient(TabView tabView) {
        this.host = tabView;
    }

    public void setChromeClient(TabChromeClient tabChromeClient) {
        this.tabChromeClient = tabChromeClient;
    }

    public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
        return this.tabChromeClient != null && this.tabChromeClient.onCreateWindow(z, z2, message);
    }

    public void onCloseWindow(WebView webView) {
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onCloseWindow(this.host);
        }
    }

    public void onProgressChanged(WebView webView, int i) {
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onProgressChanged(i);
        }
    }

    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
        String url = webView.getUrl();
        if (!TextUtils.isEmpty(url)) {
            try {
                new SaveBitmapTask(new GetFaviconFolder(new WeakReference(webView.getContext())).get(), url, bitmap, new UpdateHistoryWrapper(webView.getTitle(), url), CompressFormat.PNG, 0).execute(new Void[0]);
            } catch (InterruptedException | ExecutionException unused) {
                LoggerWrapper.throwOrWarn("FocusWebChromeClient", "Failed to get cache folder in onReceivedIcon.");
            }
            if (this.tabChromeClient != null) {
                this.tabChromeClient.onReceivedIcon(this.host, bitmap);
            }
        }
    }

    public void onShowCustomView(View view, final CustomViewCallback customViewCallback) {
        C05511 c05511 = new FullscreenCallback() {
            public void fullScreenExited() {
                customViewCallback.onCustomViewHidden();
            }
        };
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onEnterFullScreen(c05511, view);
        }
        TelemetryWrapper.browseEnterFullScreenEvent();
    }

    public void onHideCustomView() {
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onExitFullScreen();
        }
        TelemetryWrapper.browseExitFullScreenEvent();
    }

    public void onPermissionRequest(PermissionRequest permissionRequest) {
        super.onPermissionRequest(permissionRequest);
        TelemetryWrapper.browsePermissionEvent(permissionRequest.getResources());
    }

    public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
        TelemetryWrapper.browseGeoLocationPermissionEvent();
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onGeolocationPermissionsShowPrompt(str, callback);
        }
    }

    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        return this.tabChromeClient != null && this.tabChromeClient.onShowFileChooser(this.host, valueCallback, fileChooserParams);
    }

    public void onReceivedTitle(WebView webView, String str) {
        super.onReceivedTitle(webView, str);
        if (this.tabChromeClient != null) {
            this.tabChromeClient.onReceivedTitle(this.host, str);
        }
    }
}
