package org.mozilla.focus.utils;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebView;

public final class DebugUtils {
   static String loadWebViewVersion(Context var0) {
      String var3;
      try {
         WebView var1 = new WebView(var0);
         var3 = loadWebViewVersion(var1);
      } catch (Exception var2) {
         var3 = "";
      }

      return var3;
   }

   private static String loadWebViewVersion(WebView var0) {
      String var2;
      try {
         var2 = parseWebViewVersion(var0.getSettings().getUserAgentString());
      } catch (Throwable var1) {
         var2 = "";
      }

      return var2;
   }

   public static String parseWebViewVersion(String var0) {
      if (TextUtils.isEmpty(var0)) {
         return "";
      } else {
         int var1 = var0.lastIndexOf("Chrome/") + "Chrome/".length();
         return var0.substring(var1, var0.indexOf(" ", var1));
      }
   }
}
