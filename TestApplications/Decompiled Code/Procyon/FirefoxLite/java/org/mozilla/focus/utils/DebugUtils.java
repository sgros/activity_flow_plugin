// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.text.TextUtils;
import android.webkit.WebView;
import android.content.Context;

public final class DebugUtils
{
    static String loadWebViewVersion(final Context context) {
        String loadWebViewVersion;
        try {
            loadWebViewVersion = loadWebViewVersion(new WebView(context));
        }
        catch (Exception ex) {
            loadWebViewVersion = "";
        }
        return loadWebViewVersion;
    }
    
    private static String loadWebViewVersion(final WebView webView) {
        String webViewVersion;
        try {
            webViewVersion = parseWebViewVersion(webView.getSettings().getUserAgentString());
        }
        catch (Throwable t) {
            webViewVersion = "";
        }
        return webViewVersion;
    }
    
    public static String parseWebViewVersion(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return "";
        }
        final int n = s.lastIndexOf("Chrome/") + "Chrome/".length();
        return s.substring(n, s.indexOf(" ", n));
    }
}
