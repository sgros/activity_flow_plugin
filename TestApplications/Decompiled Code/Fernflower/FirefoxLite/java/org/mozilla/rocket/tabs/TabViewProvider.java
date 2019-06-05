package org.mozilla.rocket.tabs;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebStorage;
import android.webkit.WebViewDatabase;

public abstract class TabViewProvider {
   public static void purify(Context var0) {
      CookieManager.getInstance().removeAllCookies((ValueCallback)null);
      WebStorage.getInstance().deleteAllData();
      WebViewDatabase var1 = WebViewDatabase.getInstance(var0);
      var1.clearFormData();
      var1.clearHttpAuthUsernamePassword();
   }

   public abstract TabView create();
}
