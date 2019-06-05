// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import java.util.Map;
import android.support.v4.util.ArrayMap;
import android.webkit.WebView;
import android.net.Uri;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import android.content.pm.PackageManager$NameNotFoundException;
import java.net.URLEncoder;
import android.content.Context;
import org.mozilla.focus.locale.Locales;
import java.util.Locale;
import java.util.regex.Pattern;

public class SupportUtils
{
    static final String[] SUPPORTED_URLS;
    private static final Pattern schemePattern;
    
    static {
        schemePattern = Pattern.compile("^.+://");
        SUPPORTED_URLS = new String[] { "about:blank", "focusabout:", "file:///android_res/raw/rights.html", "https://www.mozilla.org/privacy/firefox-rocket", "file:///android_res/raw/about.html" };
    }
    
    public static String getAboutURI() {
        return "file:///android_res/raw/about.html";
    }
    
    public static String getManifestoURL() {
        final String languageTag = Locales.getLanguageTag(Locale.getDefault());
        final StringBuilder sb = new StringBuilder();
        sb.append("https://www.mozilla.org/");
        sb.append(languageTag);
        sb.append("/about/manifesto/");
        return sb.toString();
    }
    
    public static String getPrivacyURL() {
        return "https://www.mozilla.org/privacy/firefox-rocket";
    }
    
    public static String getSumoURLForTopic(final Context context, String encode) {
        try {
            encode = URLEncoder.encode(encode, "UTF-8");
            try {
                final String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                final String languageTag = Locales.getLanguageTag(Locale.getDefault());
                final StringBuilder sb = new StringBuilder();
                sb.append("https://support.mozilla.org/1/mobile/");
                sb.append(versionName);
                sb.append("/");
                sb.append("Android");
                sb.append("/");
                sb.append(languageTag);
                sb.append("/");
                sb.append(encode);
                return sb.toString();
            }
            catch (PackageManager$NameNotFoundException cause) {
                throw new IllegalStateException("Unable find package details for Focus", (Throwable)cause);
            }
        }
        catch (UnsupportedEncodingException cause2) {
            throw new IllegalStateException("utf-8 should always be available", cause2);
        }
    }
    
    public static String getYourRightsURI() {
        return "file:///android_res/raw/rights.html";
    }
    
    public static boolean isTemplateSupportPages(final String s) {
        final int hashCode = s.hashCode();
        boolean b = true;
        int n = 0;
        Label_0052: {
            if (hashCode != -473277749) {
                if (hashCode == 2112069925) {
                    if (s.equals("focusabout:")) {
                        n = 0;
                        break Label_0052;
                    }
                }
            }
            else if (s.equals("file:///android_res/raw/rights.html")) {
                n = 1;
                break Label_0052;
            }
            n = -1;
        }
        switch (n) {
            default: {
                b = false;
                return b;
            }
            case 0:
            case 1: {
                return b;
            }
        }
    }
    
    public static boolean isUrl(String lowerCase) {
        final boolean empty = TextUtils.isEmpty((CharSequence)lowerCase);
        final boolean b = false;
        if (empty) {
            return false;
        }
        lowerCase = lowerCase.trim().toLowerCase(Locale.getDefault());
        if (lowerCase.contains(" ")) {
            return false;
        }
        final String[] supported_URLS = SupportUtils.SUPPORTED_URLS;
        for (int length = supported_URLS.length, i = 0; i < length; ++i) {
            if (supported_URLS[i].equals(lowerCase)) {
                return true;
            }
        }
        Uri uri;
        if (SupportUtils.schemePattern.matcher(lowerCase).find()) {
            uri = Uri.parse(lowerCase);
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("http://");
            sb.append(lowerCase);
            uri = Uri.parse(sb.toString());
        }
        String host;
        if (TextUtils.isEmpty((CharSequence)uri.getHost())) {
            host = "";
        }
        else {
            host = uri.getHost();
        }
        final String scheme = uri.getScheme();
        final int hashCode = scheme.hashCode();
        int n = 0;
        Label_0231: {
            if (hashCode != 3143036) {
                if (hashCode != 3213448) {
                    if (hashCode == 99617003) {
                        if (scheme.equals("https")) {
                            n = 1;
                            break Label_0231;
                        }
                    }
                }
                else if (scheme.equals("http")) {
                    n = 0;
                    break Label_0231;
                }
            }
            else if (scheme.equals("file")) {
                n = 2;
                break Label_0231;
            }
            n = -1;
        }
        switch (n) {
            default: {
                return true;
            }
            case 2: {
                return TextUtils.isEmpty((CharSequence)uri.getPath()) ^ true;
            }
            case 0:
            case 1: {
                if (!host.contains(".")) {
                    return host.equals("localhost");
                }
                boolean b2 = b;
                if (!host.startsWith(".")) {
                    b2 = b;
                    if (!host.endsWith(".")) {
                        b2 = true;
                    }
                }
                return b2;
            }
        }
    }
    
    private static void loadAbout(final WebView webView) {
        final Context context = webView.getContext();
        final Resources localizedResources = Locales.getLocalizedResources(webView.getContext());
        final String loadWebViewVersion = DebugUtils.loadWebViewVersion(webView.getContext());
        final ArrayMap<String, String> arrayMap = new ArrayMap<String, String>();
        final String string = webView.getContext().getResources().getString(2131755048, new Object[] { webView.getContext().getResources().getString(2131755062), webView.getContext().getResources().getString(2131755267) });
        final String aboutURI = getAboutURI();
        final String manifestoURL = getManifestoURL();
        final String sumoURLForTopic = getSumoURLForTopic(webView.getContext(), "rocket-help");
        final String yourRightsURI = getYourRightsURI();
        final String privacyURL = getPrivacyURL();
        final String sumoURLForTopic2 = getSumoURLForTopic(webView.getContext(), "firefox-lite-feed");
        final String string2 = localizedResources.getString(2131755049);
        final String string3 = localizedResources.getString(2131755052);
        final String string4 = localizedResources.getString(2131755053);
        final String string5 = localizedResources.getString(2131755051);
        final String string6 = localizedResources.getString(2131755050, new Object[] { localizedResources.getString(2131755245) });
    Label_0207_Outer:
        while (true) {
            PackageManager packageManager = null;
            String packageName = null;
            try {
                packageManager = context.getPackageManager();
                packageName = context.getPackageName();
                final PackageManager packageManager2 = packageManager;
                final String s = packageName;
                final int n = 0;
                final PackageInfo packageInfo = packageManager2.getPackageInfo(s, n);
                final String s2 = packageInfo.versionName;
                break Label_0210;
            }
            catch (PackageManager$NameNotFoundException ex) {}
            while (true) {
                try {
                    final PackageManager packageManager2 = packageManager;
                    final String s = packageName;
                    final int n = 0;
                    final PackageInfo packageInfo = packageManager2.getPackageInfo(s, n);
                    String s2 = packageInfo.versionName;
                    arrayMap.put("%about-version%", s2);
                    arrayMap.put("%about-content%", localizedResources.getString(2131755047, new Object[] { string, manifestoURL, string2, sumoURLForTopic, string3, yourRightsURI, string4, privacyURL, string5, sumoURLForTopic2, string6 }));
                    arrayMap.put("%wordmark%", HtmlLoader.loadDrawableAsDataURI(webView.getContext(), 2131230881, 2131099672));
                    arrayMap.put("%webview-version%", loadWebViewVersion);
                    webView.loadDataWithBaseURL(aboutURI, HtmlLoader.loadResourceFile(webView.getContext(), 2131689472, arrayMap), "text/html", "UTF-8", (String)null);
                    return;
                    s2 = "";
                    continue Label_0207_Outer;
                }
                catch (PackageManager$NameNotFoundException ex2) {
                    continue;
                }
                break;
            }
            break;
        }
    }
    
    private static void loadRights(final WebView webView) {
        final Context context = webView.getContext();
        final Resources localizedResources = Locales.getLocalizedResources(webView.getContext());
        final ArrayMap<String, String> arrayMap = new ArrayMap<String, String>();
        final String string = context.getResources().getString(2131755062);
        final String string2 = context.getResources().getString(2131755267);
        final String string3 = context.getResources().getString(2131755199);
        final String string4 = context.getResources().getString(2131755268);
        arrayMap.put("%your-rights-content1%", localizedResources.getString(2131755428, new Object[] { string, string2 }));
        arrayMap.put("%your-rights-content2%", localizedResources.getString(2131755429, new Object[] { string, "https://www.mozilla.org/en-US/MPL/", string4 }));
        arrayMap.put("%your-rights-content3%", localizedResources.getString(2131755430, new Object[] { string, "https://www.mozilla.org/foundation/trademarks/policy/", string2, string3 }));
        arrayMap.put("%your-rights-content4%", localizedResources.getString(2131755431, new Object[] { string, "file:///android_asset/licenses.html" }));
        arrayMap.put("%your-rights-content5%", localizedResources.getString(2131755432, new Object[] { string, "file:///android_asset/gpl.html", "https://wiki.mozilla.org/Security/Tracking_protection#Lists" }));
        webView.loadDataWithBaseURL(getYourRightsURI(), HtmlLoader.loadResourceFile(webView.getContext(), 2131689484, arrayMap), "text/html", "UTF-8", (String)null);
    }
    
    public static void loadSupportPages(final WebView webView, final String str) {
        final int hashCode = str.hashCode();
        int n = 0;
        Label_0050: {
            if (hashCode != -473277749) {
                if (hashCode == 2112069925) {
                    if (str.equals("focusabout:")) {
                        n = 0;
                        break Label_0050;
                    }
                }
            }
            else if (str.equals("file:///android_res/raw/rights.html")) {
                n = 1;
                break Label_0050;
            }
            n = -1;
        }
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown internal pages url: ");
                sb.append(str);
                throw new IllegalArgumentException(sb.toString());
            }
            case 1: {
                loadRights(webView);
                break;
            }
            case 0: {
                loadAbout(webView);
                break;
            }
        }
    }
    
    public static String normalize(final String anObject) {
        final String trim = anObject.trim();
        final Uri parse = Uri.parse(trim);
        final String[] supported_URLS = SupportUtils.SUPPORTED_URLS;
        for (int length = supported_URLS.length, i = 0; i < length; ++i) {
            if (supported_URLS[i].equals(anObject)) {
                return anObject;
            }
        }
        Uri parse2 = parse;
        if (TextUtils.isEmpty((CharSequence)parse.getScheme())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("http://");
            sb.append(trim);
            parse2 = Uri.parse(sb.toString());
        }
        return parse2.toString();
    }
}
