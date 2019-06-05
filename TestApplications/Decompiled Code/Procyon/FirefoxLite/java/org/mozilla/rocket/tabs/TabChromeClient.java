// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

import android.webkit.WebChromeClient$FileChooserParams;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.graphics.Bitmap;
import android.webkit.GeolocationPermissions$Callback;
import android.view.View;
import android.os.Message;

public class TabChromeClient
{
    public void onCloseWindow(final TabView tabView) {
    }
    
    public boolean onCreateWindow(final boolean b, final boolean b2, final Message message) {
        return false;
    }
    
    public void onEnterFullScreen(final TabView.FullscreenCallback fullscreenCallback, final View view) {
    }
    
    public void onExitFullScreen() {
    }
    
    public void onGeolocationPermissionsShowPrompt(final String s, final GeolocationPermissions$Callback geolocationPermissions$Callback) {
    }
    
    public void onLongPress(final TabView.HitTarget hitTarget) {
    }
    
    public void onProgressChanged(final int n) {
    }
    
    public void onReceivedIcon(final TabView tabView, final Bitmap bitmap) {
    }
    
    public void onReceivedTitle(final TabView tabView, final String s) {
    }
    
    public boolean onShowFileChooser(final TabView tabView, final ValueCallback<Uri[]> valueCallback, final WebChromeClient$FileChooserParams webChromeClient$FileChooserParams) {
        return false;
    }
}
