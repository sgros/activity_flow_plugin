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
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.webkit.DefaultWebView;
import org.mozilla.focus.webkit.TrackingProtectionWebViewClient;
import org.mozilla.focus.webkit.WebkitView;
import org.mozilla.rocket.C0769R;
import org.mozilla.threadutils.ThreadUtils;

public class WebViewProvider {
    private static String userAgentString;

    public interface WebSettingsHook {
        void modify(WebSettings webSettings);
    }

    public static void preload(Context context) {
        TrackingProtectionWebViewClient.triggerPreload(context);
    }

    public static View create(Context context, AttributeSet attributeSet) {
        return create(context, attributeSet, null);
    }

    public static View create(Context context, AttributeSet attributeSet, WebSettingsHook webSettingsHook) {
        WebView.enableSlowWholeDocumentDraw();
        WebkitView webkitView = new WebkitView(context, attributeSet);
        WebSettings settings = webkitView.getSettings();
        setupView(webkitView);
        configureDefaultSettings(context, settings, webkitView);
        applyMultiTabSettings(context, settings);
        applyAppSettings(context, settings);
        if (webSettingsHook != null) {
            webSettingsHook.modify(settings);
        }
        return webkitView;
    }

    public static View createDefaultWebView(Context context, AttributeSet attributeSet) {
        DefaultWebView defaultWebView = new DefaultWebView(context, attributeSet);
        WebSettings settings = defaultWebView.getSettings();
        setupView(defaultWebView);
        configureDefaultSettings(context, settings, defaultWebView);
        applyAppSettings(context, settings);
        return defaultWebView;
    }

    private static void setupView(WebView webView) {
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private static void configureDefaultSettings(Context context, WebSettings webSettings, WebView webView) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
        webSettings.setAllowFileAccess(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setUserAgentString(getUserAgentString(context));
        webSettings.setAllowContentAccess(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDatabaseEnabled(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(false);
        if (VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
    }

    public static void applyAppSettings(Context context, WebSettings webSettings) {
        webSettings.setBlockNetworkImage(Settings.getInstance(context).shouldBlockImages());
        webSettings.setLoadsImagesAutomatically(Settings.getInstance(context).shouldBlockImages() ^ 1);
    }

    public static void applyMultiTabSettings(Context context, WebSettings webSettings) {
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    static String getUABrowserString(String str, String str2) {
        int indexOf = str.indexOf("AppleWebKit");
        if (indexOf == -1) {
            indexOf = str.indexOf(")") + 2;
            if (indexOf >= str.length()) {
                return str2;
            }
        }
        String[] split = str.substring(indexOf).split(" ");
        for (indexOf = 0; indexOf < split.length; indexOf++) {
            if (split[indexOf].startsWith("Chrome")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(" ");
                stringBuilder.append(split[indexOf]);
                split[indexOf] = stringBuilder.toString();
                return TextUtils.join(" ", split);
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(TextUtils.join(" ", split));
        stringBuilder2.append(" ");
        stringBuilder2.append(str2);
        return stringBuilder2.toString();
    }

    private static String buildUserAgentString(Context context, String str) {
        return buildUserAgentString(context, WebSettings.getDefaultUserAgent(context), str);
    }

    static String buildUserAgentString(Context context, String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str.substring(0, str.indexOf("wv) ") + 4).replace("wv) ", "rv) "));
        try {
            String str3 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append("/");
            stringBuilder2.append(str3);
            stringBuilder.append(getUABrowserString(str, stringBuilder2.toString()));
            return stringBuilder.toString();
        } catch (NameNotFoundException e) {
            throw new IllegalStateException("Unable find package details for Rocket", e);
        }
    }

    public static String getUserAgentString(Context context) {
        if (userAgentString == null) {
            userAgentString = buildUserAgentString(context, context.getResources().getString(C0769R.string.useragent_appname));
            ThreadUtils.postToBackgroundThread(new C0545-$$Lambda$WebViewProvider$sInVC8y955eZKzWXUqprXxIrcMA(context));
        }
        return userAgentString;
    }
}
