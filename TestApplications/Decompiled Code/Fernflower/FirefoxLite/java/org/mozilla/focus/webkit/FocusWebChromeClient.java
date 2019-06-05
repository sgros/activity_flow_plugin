package org.mozilla.focus.webkit;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebChromeClient.FileChooserParams;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.tabs.TabChromeClient;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.util.LoggerWrapper;

class FocusWebChromeClient extends WebChromeClient {
   private TabView host;
   private TabChromeClient tabChromeClient;

   FocusWebChromeClient(TabView var1) {
      this.host = var1;
   }

   public void onCloseWindow(WebView var1) {
      if (this.tabChromeClient != null) {
         this.tabChromeClient.onCloseWindow(this.host);
      }

   }

   public boolean onCreateWindow(WebView var1, boolean var2, boolean var3, Message var4) {
      if (this.tabChromeClient != null && this.tabChromeClient.onCreateWindow(var2, var3, var4)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void onGeolocationPermissionsHidePrompt() {
      super.onGeolocationPermissionsHidePrompt();
   }

   public void onGeolocationPermissionsShowPrompt(String var1, Callback var2) {
      TelemetryWrapper.browseGeoLocationPermissionEvent();
      if (this.tabChromeClient != null) {
         this.tabChromeClient.onGeolocationPermissionsShowPrompt(var1, var2);
      }

   }

   public void onHideCustomView() {
      if (this.tabChromeClient != null) {
         this.tabChromeClient.onExitFullScreen();
      }

      TelemetryWrapper.browseExitFullScreenEvent();
   }

   public void onPermissionRequest(PermissionRequest var1) {
      super.onPermissionRequest(var1);
      TelemetryWrapper.browsePermissionEvent(var1.getResources());
   }

   public void onProgressChanged(WebView var1, int var2) {
      if (this.tabChromeClient != null) {
         this.tabChromeClient.onProgressChanged(var2);
      }

   }

   public void onReceivedIcon(WebView var1, Bitmap var2) {
      String var3 = var1.getUrl();
      if (!TextUtils.isEmpty(var3)) {
         String var4 = var1.getTitle();

         try {
            WeakReference var7 = new WeakReference(var1.getContext());
            FileUtils.GetFaviconFolder var6 = new FileUtils.GetFaviconFolder(var7);
            File var10 = var6.get();
            BrowsingHistoryManager.UpdateHistoryWrapper var9 = new BrowsingHistoryManager.UpdateHistoryWrapper(var4, var3);
            FavIconUtils.SaveBitmapTask var5 = new FavIconUtils.SaveBitmapTask(var10, var3, var2, var9, CompressFormat.PNG, 0);
            var5.execute(new Void[0]);
         } catch (InterruptedException | ExecutionException var8) {
            LoggerWrapper.throwOrWarn("FocusWebChromeClient", "Failed to get cache folder in onReceivedIcon.");
         }

         if (this.tabChromeClient != null) {
            this.tabChromeClient.onReceivedIcon(this.host, var2);
         }

      }
   }

   public void onReceivedTitle(WebView var1, String var2) {
      super.onReceivedTitle(var1, var2);
      if (this.tabChromeClient != null) {
         this.tabChromeClient.onReceivedTitle(this.host, var2);
      }

   }

   public void onShowCustomView(View var1, final CustomViewCallback var2) {
      TabView.FullscreenCallback var3 = new TabView.FullscreenCallback() {
         public void fullScreenExited() {
            var2.onCustomViewHidden();
         }
      };
      if (this.tabChromeClient != null) {
         this.tabChromeClient.onEnterFullScreen(var3, var1);
      }

      TelemetryWrapper.browseEnterFullScreenEvent();
   }

   public boolean onShowFileChooser(WebView var1, ValueCallback var2, FileChooserParams var3) {
      boolean var4;
      if (this.tabChromeClient != null && this.tabChromeClient.onShowFileChooser(this.host, var2, var3)) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public void setChromeClient(TabChromeClient var1) {
      this.tabChromeClient = var1;
   }
}
