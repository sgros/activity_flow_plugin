package org.mozilla.rocket.tabs;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient.FileChooserParams;

public class TabChromeClient {
   public void onCloseWindow(TabView var1) {
   }

   public boolean onCreateWindow(boolean var1, boolean var2, Message var3) {
      return false;
   }

   public void onEnterFullScreen(TabView.FullscreenCallback var1, View var2) {
   }

   public void onExitFullScreen() {
   }

   public void onGeolocationPermissionsShowPrompt(String var1, Callback var2) {
   }

   public void onLongPress(TabView.HitTarget var1) {
   }

   public void onProgressChanged(int var1) {
   }

   public void onReceivedIcon(TabView var1, Bitmap var2) {
   }

   public void onReceivedTitle(TabView var1, String var2) {
   }

   public boolean onShowFileChooser(TabView var1, ValueCallback var2, FileChooserParams var3) {
      return false;
   }
}
