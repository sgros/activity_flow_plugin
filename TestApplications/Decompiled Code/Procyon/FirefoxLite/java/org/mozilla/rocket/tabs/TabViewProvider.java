// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.tabs;

import android.webkit.WebViewDatabase;
import android.webkit.WebStorage;
import android.webkit.ValueCallback;
import android.webkit.CookieManager;
import android.content.Context;

public abstract class TabViewProvider
{
    public static void purify(final Context context) {
        CookieManager.getInstance().removeAllCookies((ValueCallback)null);
        WebStorage.getInstance().deleteAllData();
        final WebViewDatabase instance = WebViewDatabase.getInstance(context);
        instance.clearFormData();
        instance.clearHttpAuthUsernamePassword();
    }
    
    public abstract TabView create();
}
