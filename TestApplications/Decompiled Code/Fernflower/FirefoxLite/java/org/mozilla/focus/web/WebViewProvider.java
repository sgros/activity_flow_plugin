package org.mozilla.focus.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import org.mozilla.focus.utils.DebugUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.webkit.DefaultWebView;
import org.mozilla.focus.webkit.TrackingProtectionWebViewClient;
import org.mozilla.focus.webkit.WebkitView;
import org.mozilla.threadutils.ThreadUtils;

public class WebViewProvider {
   private static String userAgentString;

   public static void applyAppSettings(Context var0, WebSettings var1) {
      var1.setBlockNetworkImage(Settings.getInstance(var0).shouldBlockImages());
      var1.setLoadsImagesAutomatically(Settings.getInstance(var0).shouldBlockImages() ^ true);
   }

   public static void applyMultiTabSettings(Context var0, WebSettings var1) {
      var1.setSupportMultipleWindows(true);
      var1.setJavaScriptCanOpenWindowsAutomatically(true);
   }

   private static String buildUserAgentString(Context var0, String var1) {
      return buildUserAgentString(var0, WebSettings.getDefaultUserAgent(var0), var1);
   }

   static String buildUserAgentString(Context var0, String var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var1.substring(0, var1.indexOf("wv) ") + 4).replace("wv) ", "rv) "));

      String var4;
      try {
         var4 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0).versionName;
      } catch (NameNotFoundException var5) {
         throw new IllegalStateException("Unable find package details for Rocket", var5);
      }

      StringBuilder var6 = new StringBuilder();
      var6.append(var2);
      var6.append("/");
      var6.append(var4);
      var3.append(getUABrowserString(var1, var6.toString()));
      return var3.toString();
   }

   @SuppressLint({"SetJavaScriptEnabled"})
   private static void configureDefaultSettings(Context var0, WebSettings var1, WebView var2) {
      var1.setJavaScriptEnabled(true);
      var1.setBuiltInZoomControls(true);
      var1.setDisplayZoomControls(false);
      var1.setLoadWithOverviewMode(true);
      var1.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
      var1.setAllowFileAccess(false);
      var1.setAllowFileAccessFromFileURLs(false);
      var1.setAllowUniversalAccessFromFileURLs(false);
      var1.setUserAgentString(getUserAgentString(var0));
      var1.setAllowContentAccess(false);
      var1.setAppCacheEnabled(false);
      var1.setDatabaseEnabled(false);
      var1.setDomStorageEnabled(true);
      var1.setGeolocationEnabled(true);
      var1.setSaveFormData(true);
      var1.setSavePassword(false);
      if (VERSION.SDK_INT >= 21) {
         CookieManager.getInstance().setAcceptThirdPartyCookies(var2, true);
      }

   }

   public static View create(Context var0, AttributeSet var1) {
      return create(var0, var1, (WebViewProvider.WebSettingsHook)null);
   }

   public static View create(Context var0, AttributeSet var1, WebViewProvider.WebSettingsHook var2) {
      WebView.enableSlowWholeDocumentDraw();
      WebkitView var4 = new WebkitView(var0, var1);
      WebSettings var3 = var4.getSettings();
      setupView(var4);
      configureDefaultSettings(var0, var3, var4);
      applyMultiTabSettings(var0, var3);
      applyAppSettings(var0, var3);
      if (var2 != null) {
         var2.modify(var3);
      }

      return var4;
   }

   public static View createDefaultWebView(Context var0, AttributeSet var1) {
      DefaultWebView var2 = new DefaultWebView(var0, var1);
      WebSettings var3 = var2.getSettings();
      setupView(var2);
      configureDefaultSettings(var0, var3, var2);
      applyAppSettings(var0, var3);
      return var2;
   }

   static String getUABrowserString(String var0, String var1) {
      int var2 = var0.indexOf("AppleWebKit");
      int var3 = var2;
      if (var2 == -1) {
         var2 = var0.indexOf(")") + 2;
         var3 = var2;
         if (var2 >= var0.length()) {
            return var1;
         }
      }

      String[] var5 = var0.substring(var3).split(" ");

      StringBuilder var4;
      for(var3 = 0; var3 < var5.length; ++var3) {
         if (var5[var3].startsWith("Chrome")) {
            var4 = new StringBuilder();
            var4.append(var1);
            var4.append(" ");
            var4.append(var5[var3]);
            var5[var3] = var4.toString();
            return TextUtils.join(" ", var5);
         }
      }

      var4 = new StringBuilder();
      var4.append(TextUtils.join(" ", var5));
      var4.append(" ");
      var4.append(var1);
      return var4.toString();
   }

   public static String getUserAgentString(Context var0) {
      if (userAgentString == null) {
         userAgentString = buildUserAgentString(var0, var0.getResources().getString(2131755427));
         ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$WebViewProvider$sInVC8y955eZKzWXUqprXxIrcMA(var0)));
      }

      return userAgentString;
   }

   // $FF: synthetic method
   static void lambda$getUserAgentString$0(Context var0) {
      Settings.updatePrefString(var0, var0.getString(2131755333), DebugUtils.parseWebViewVersion(userAgentString));
   }

   public static void preload(Context var0) {
      TrackingProtectionWebViewClient.triggerPreload(var0);
   }

   private static void setupView(WebView var0) {
      var0.setVerticalScrollBarEnabled(true);
      var0.setHorizontalScrollBarEnabled(true);
   }

   public interface WebSettingsHook {
      void modify(WebSettings var1);
   }
}
