package org.mozilla.focus.utils;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebView;

public final class DebugUtils {
    static String loadWebViewVersion(Context context) {
        try {
            return loadWebViewVersion(new WebView(context));
        } catch (Exception unused) {
            return "";
        }
    }

    private static String loadWebViewVersion(WebView webView) {
        try {
            return parseWebViewVersion(webView.getSettings().getUserAgentString());
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String parseWebViewVersion(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf("Chrome/") + "Chrome/".length();
        return str.substring(lastIndexOf, str.indexOf(" ", lastIndexOf));
    }
}
