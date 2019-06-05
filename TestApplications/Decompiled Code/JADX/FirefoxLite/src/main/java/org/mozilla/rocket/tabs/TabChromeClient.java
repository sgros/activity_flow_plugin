package org.mozilla.rocket.tabs;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import org.mozilla.rocket.tabs.TabView.FullscreenCallback;
import org.mozilla.rocket.tabs.TabView.HitTarget;

public class TabChromeClient {
    public void onCloseWindow(TabView tabView) {
    }

    public boolean onCreateWindow(boolean z, boolean z2, Message message) {
        return false;
    }

    public void onEnterFullScreen(FullscreenCallback fullscreenCallback, View view) {
    }

    public void onExitFullScreen() {
    }

    public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
    }

    public void onLongPress(HitTarget hitTarget) {
    }

    public void onProgressChanged(int i) {
    }

    public void onReceivedIcon(TabView tabView, Bitmap bitmap) {
    }

    public void onReceivedTitle(TabView tabView, String str) {
    }

    public boolean onShowFileChooser(TabView tabView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        return false;
    }
}
