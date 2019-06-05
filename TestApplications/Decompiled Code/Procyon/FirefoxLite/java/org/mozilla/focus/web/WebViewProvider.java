// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.web;

import org.mozilla.focus.webkit.TrackingProtectionWebViewClient;
import org.mozilla.focus.utils.DebugUtils;
import org.mozilla.threadutils.ThreadUtils;
import android.text.TextUtils;
import org.mozilla.focus.webkit.DefaultWebView;
import org.mozilla.focus.webkit.WebkitView;
import android.view.View;
import android.util.AttributeSet;
import android.annotation.SuppressLint;
import android.webkit.CookieManager;
import android.os.Build$VERSION;
import android.webkit.WebSettings$LayoutAlgorithm;
import android.webkit.WebView;
import android.content.pm.PackageManager$NameNotFoundException;
import org.mozilla.focus.utils.Settings;
import android.webkit.WebSettings;
import android.content.Context;

public class WebViewProvider
{
    private static String userAgentString;
    
    public static void applyAppSettings(final Context context, final WebSettings webSettings) {
        webSettings.setBlockNetworkImage(Settings.getInstance(context).shouldBlockImages());
        webSettings.setLoadsImagesAutomatically(Settings.getInstance(context).shouldBlockImages() ^ true);
    }
    
    public static void applyMultiTabSettings(final Context context, final WebSettings webSettings) {
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }
    
    private static String buildUserAgentString(final Context context, final String s) {
        return buildUserAgentString(context, WebSettings.getDefaultUserAgent(context), s);
    }
    
    static String buildUserAgentString(final Context context, final String s, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s.substring(0, s.indexOf("wv) ") + 4).replace("wv) ", "rv) "));
        try {
            final String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("/");
            sb2.append(versionName);
            sb.append(getUABrowserString(s, sb2.toString()));
            return sb.toString();
        }
        catch (PackageManager$NameNotFoundException cause) {
            throw new IllegalStateException("Unable find package details for Rocket", (Throwable)cause);
        }
    }
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    private static void configureDefaultSettings(final Context context, final WebSettings webSettings, final WebView webView) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings$LayoutAlgorithm.TEXT_AUTOSIZING);
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
        if (Build$VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
    }
    
    public static View create(final Context context, final AttributeSet set) {
        return create(context, set, null);
    }
    
    public static View create(final Context context, final AttributeSet set, final WebSettingsHook webSettingsHook) {
        WebView.enableSlowWholeDocumentDraw();
        final WebkitView webkitView = new WebkitView(context, set);
        final WebSettings settings = webkitView.getSettings();
        setupView(webkitView);
        configureDefaultSettings(context, settings, webkitView);
        applyMultiTabSettings(context, settings);
        applyAppSettings(context, settings);
        if (webSettingsHook != null) {
            webSettingsHook.modify(settings);
        }
        return (View)webkitView;
    }
    
    public static View createDefaultWebView(final Context context, final AttributeSet set) {
        final DefaultWebView defaultWebView = new DefaultWebView(context, set);
        final WebSettings settings = defaultWebView.getSettings();
        setupView(defaultWebView);
        configureDefaultSettings(context, settings, defaultWebView);
        applyAppSettings(context, settings);
        return (View)defaultWebView;
    }
    
    static String getUABrowserString(final String s, final String s2) {
        int index;
        if ((index = s.indexOf("AppleWebKit")) == -1 && (index = s.indexOf(")") + 2) >= s.length()) {
            return s2;
        }
        final String[] split = s.substring(index).split(" ");
        for (int i = 0; i < split.length; ++i) {
            if (split[i].startsWith("Chrome")) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s2);
                sb.append(" ");
                sb.append(split[i]);
                split[i] = sb.toString();
                return TextUtils.join((CharSequence)" ", (Object[])split);
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(TextUtils.join((CharSequence)" ", (Object[])split));
        sb2.append(" ");
        sb2.append(s2);
        return sb2.toString();
    }
    
    public static String getUserAgentString(final Context context) {
        if (WebViewProvider.userAgentString == null) {
            WebViewProvider.userAgentString = buildUserAgentString(context, context.getResources().getString(2131755427));
            ThreadUtils.postToBackgroundThread(new _$$Lambda$WebViewProvider$sInVC8y955eZKzWXUqprXxIrcMA(context));
        }
        return WebViewProvider.userAgentString;
    }
    
    public static void preload(final Context context) {
        TrackingProtectionWebViewClient.triggerPreload(context);
    }
    
    private static void setupView(final WebView webView) {
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
    }
    
    public interface WebSettingsHook
    {
        void modify(final WebSettings p0);
    }
}
