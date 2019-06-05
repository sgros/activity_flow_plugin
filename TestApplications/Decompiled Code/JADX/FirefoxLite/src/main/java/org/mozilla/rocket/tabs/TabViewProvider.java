package org.mozilla.rocket.tabs;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.WebStorage;
import android.webkit.WebViewDatabase;

public abstract class TabViewProvider {
    public abstract TabView create();

    public static void purify(Context context) {
        CookieManager.getInstance().removeAllCookies(null);
        WebStorage.getInstance().deleteAllData();
        WebViewDatabase instance = WebViewDatabase.getInstance(context);
        instance.clearFormData();
        instance.clearHttpAuthUsernamePassword();
    }
}
