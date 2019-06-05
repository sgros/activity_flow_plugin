package org.mozilla.focus.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import android.webkit.WebView;
import com.adjust.sdk.Constants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Pattern;
import org.mozilla.focus.locale.Locales;
import org.mozilla.rocket.C0769R;

public class SupportUtils {
    static final String[] SUPPORTED_URLS = new String[]{"about:blank", "focusabout:", "file:///android_res/raw/rights.html", "https://www.mozilla.org/privacy/firefox-rocket", "file:///android_res/raw/about.html"};
    private static final Pattern schemePattern = Pattern.compile("^.+://");

    public static String getAboutURI() {
        return "file:///android_res/raw/about.html";
    }

    public static String getPrivacyURL() {
        return "https://www.mozilla.org/privacy/firefox-rocket";
    }

    public static String getYourRightsURI() {
        return "file:///android_res/raw/rights.html";
    }

    public static String normalize(String str) {
        String trim = str.trim();
        Uri parse = Uri.parse(trim);
        for (String equals : SUPPORTED_URLS) {
            if (equals.equals(str)) {
                return str;
            }
        }
        if (TextUtils.isEmpty(parse.getScheme())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://");
            stringBuilder.append(trim);
            parse = Uri.parse(stringBuilder.toString());
        }
        return parse.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a2 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a2 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a2 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a3  */
    public static boolean isUrl(java.lang.String r7) {
        /*
        r0 = android.text.TextUtils.isEmpty(r7);
        r1 = 0;
        if (r0 == 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r7 = r7.trim();
        r0 = java.util.Locale.getDefault();
        r7 = r7.toLowerCase(r0);
        r0 = " ";
        r0 = r7.contains(r0);
        if (r0 == 0) goto L_0x001d;
    L_0x001c:
        return r1;
    L_0x001d:
        r0 = SUPPORTED_URLS;
        r2 = r0.length;
        r3 = 0;
    L_0x0021:
        r4 = 1;
        if (r3 >= r2) goto L_0x0030;
    L_0x0024:
        r5 = r0[r3];
        r5 = r5.equals(r7);
        if (r5 == 0) goto L_0x002d;
    L_0x002c:
        return r4;
    L_0x002d:
        r3 = r3 + 1;
        goto L_0x0021;
    L_0x0030:
        r0 = schemePattern;
        r0 = r0.matcher(r7);
        r0 = r0.find();
        if (r0 == 0) goto L_0x0041;
    L_0x003c:
        r7 = android.net.Uri.parse(r7);
        goto L_0x0056;
    L_0x0041:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = "http://";
        r0.append(r2);
        r0.append(r7);
        r7 = r0.toString();
        r7 = android.net.Uri.parse(r7);
    L_0x0056:
        r0 = r7.getHost();
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x0063;
    L_0x0060:
        r0 = "";
        goto L_0x0067;
    L_0x0063:
        r0 = r7.getHost();
    L_0x0067:
        r2 = r7.getScheme();
        r3 = -1;
        r5 = r2.hashCode();
        r6 = 3143036; // 0x2ff57c float:4.404332E-39 double:1.552866E-317;
        if (r5 == r6) goto L_0x0094;
    L_0x0075:
        r6 = 3213448; // 0x310888 float:4.503E-39 double:1.5876543E-317;
        if (r5 == r6) goto L_0x008a;
    L_0x007a:
        r6 = 99617003; // 0x5f008eb float:2.2572767E-35 double:4.9217339E-316;
        if (r5 == r6) goto L_0x0080;
    L_0x007f:
        goto L_0x009e;
    L_0x0080:
        r5 = "https";
        r2 = r2.equals(r5);
        if (r2 == 0) goto L_0x009e;
    L_0x0088:
        r2 = 1;
        goto L_0x009f;
    L_0x008a:
        r5 = "http";
        r2 = r2.equals(r5);
        if (r2 == 0) goto L_0x009e;
    L_0x0092:
        r2 = 0;
        goto L_0x009f;
    L_0x0094:
        r5 = "file";
        r2 = r2.equals(r5);
        if (r2 == 0) goto L_0x009e;
    L_0x009c:
        r2 = 2;
        goto L_0x009f;
    L_0x009e:
        r2 = -1;
    L_0x009f:
        switch(r2) {
            case 0: goto L_0x00ad;
            case 1: goto L_0x00ad;
            case 2: goto L_0x00a3;
            default: goto L_0x00a2;
        };
    L_0x00a2:
        return r4;
    L_0x00a3:
        r7 = r7.getPath();
        r7 = android.text.TextUtils.isEmpty(r7);
        r7 = r7 ^ r4;
        return r7;
    L_0x00ad:
        r7 = ".";
        r7 = r0.contains(r7);
        if (r7 != 0) goto L_0x00bc;
    L_0x00b5:
        r7 = "localhost";
        r7 = r0.equals(r7);
        return r7;
    L_0x00bc:
        r7 = ".";
        r7 = r0.startsWith(r7);
        if (r7 != 0) goto L_0x00cd;
    L_0x00c4:
        r7 = ".";
        r7 = r0.endsWith(r7);
        if (r7 != 0) goto L_0x00cd;
    L_0x00cc:
        r1 = 1;
    L_0x00cd:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.utils.SupportUtils.isUrl(java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A:{SYNTHETIC, RETURN} */
    public static boolean isTemplateSupportPages(java.lang.String r4) {
        /*
        r0 = r4.hashCode();
        r1 = -473277749; // 0xffffffffe3ca5acb float:-7.465569E21 double:NaN;
        r2 = 1;
        r3 = 0;
        if (r0 == r1) goto L_0x001b;
    L_0x000b:
        r1 = 2112069925; // 0x7de3a125 float:3.7821433E37 double:1.0435011916E-314;
        if (r0 == r1) goto L_0x0011;
    L_0x0010:
        goto L_0x0025;
    L_0x0011:
        r0 = "focusabout:";
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0025;
    L_0x0019:
        r4 = 0;
        goto L_0x0026;
    L_0x001b:
        r0 = "file:///android_res/raw/rights.html";
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0025;
    L_0x0023:
        r4 = 1;
        goto L_0x0026;
    L_0x0025:
        r4 = -1;
    L_0x0026:
        switch(r4) {
            case 0: goto L_0x002a;
            case 1: goto L_0x002a;
            default: goto L_0x0029;
        };
    L_0x0029:
        r2 = 0;
    L_0x002a:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.utils.SupportUtils.isTemplateSupportPages(java.lang.String):boolean");
    }

    public static String getSumoURLForTopic(Context context, String str) {
        try {
            str = URLEncoder.encode(str, Constants.ENCODING);
            try {
                String str2 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                String languageTag = Locales.getLanguageTag(Locale.getDefault());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("https://support.mozilla.org/1/mobile/");
                stringBuilder.append(str2);
                stringBuilder.append("/");
                stringBuilder.append("Android");
                stringBuilder.append("/");
                stringBuilder.append(languageTag);
                stringBuilder.append("/");
                stringBuilder.append(str);
                return stringBuilder.toString();
            } catch (NameNotFoundException e) {
                throw new IllegalStateException("Unable find package details for Focus", e);
            }
        } catch (UnsupportedEncodingException e2) {
            throw new IllegalStateException("utf-8 should always be available", e2);
        }
    }

    public static String getManifestoURL() {
        String languageTag = Locales.getLanguageTag(Locale.getDefault());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://www.mozilla.org/");
        stringBuilder.append(languageTag);
        stringBuilder.append("/about/manifesto/");
        return stringBuilder.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003e  */
    public static void loadSupportPages(android.webkit.WebView r2, java.lang.String r3) {
        /*
        r0 = r3.hashCode();
        r1 = -473277749; // 0xffffffffe3ca5acb float:-7.465569E21 double:NaN;
        if (r0 == r1) goto L_0x0019;
    L_0x0009:
        r1 = 2112069925; // 0x7de3a125 float:3.7821433E37 double:1.0435011916E-314;
        if (r0 == r1) goto L_0x000f;
    L_0x000e:
        goto L_0x0023;
    L_0x000f:
        r0 = "focusabout:";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0023;
    L_0x0017:
        r0 = 0;
        goto L_0x0024;
    L_0x0019:
        r0 = "file:///android_res/raw/rights.html";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0023;
    L_0x0021:
        r0 = 1;
        goto L_0x0024;
    L_0x0023:
        r0 = -1;
    L_0x0024:
        switch(r0) {
            case 0: goto L_0x0042;
            case 1: goto L_0x003e;
            default: goto L_0x0027;
        };
    L_0x0027:
        r2 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Unknown internal pages url: ";
        r0.append(r1);
        r0.append(r3);
        r3 = r0.toString();
        r2.<init>(r3);
        throw r2;
    L_0x003e:
        loadRights(r2);
        goto L_0x0045;
    L_0x0042:
        loadAbout(r2);
    L_0x0045:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.utils.SupportUtils.loadSupportPages(android.webkit.WebView, java.lang.String):void");
    }

    private static void loadRights(WebView webView) {
        Context context = webView.getContext();
        Resources localizedResources = Locales.getLocalizedResources(webView.getContext());
        ArrayMap arrayMap = new ArrayMap();
        String string = context.getResources().getString(C0769R.string.app_name);
        String string2 = context.getResources().getString(C0769R.string.mozilla);
        String string3 = context.getResources().getString(C0769R.string.firefox);
        String string4 = context.getResources().getString(C0769R.string.mpl);
        String str = "%your-rights-content1%";
        arrayMap.put(str, localizedResources.getString(C0769R.string.your_rights_content1, new Object[]{string, string2}));
        arrayMap.put("%your-rights-content2%", localizedResources.getString(C0769R.string.your_rights_content2, new Object[]{string, "https://www.mozilla.org/en-US/MPL/", string4}));
        Object[] objArr = new Object[]{string, "https://www.mozilla.org/foundation/trademarks/policy/", string2, string3};
        arrayMap.put("%your-rights-content3%", localizedResources.getString(C0769R.string.your_rights_content3, objArr));
        string2 = "%your-rights-content4%";
        arrayMap.put(string2, localizedResources.getString(C0769R.string.your_rights_content4, new Object[]{string, "file:///android_asset/licenses.html"}));
        arrayMap.put("%your-rights-content5%", localizedResources.getString(C0769R.string.your_rights_content5, new Object[]{string, "file:///android_asset/gpl.html", "https://wiki.mozilla.org/Security/Tracking_protection#Lists"}));
        WebView webView2 = webView;
        webView2.loadDataWithBaseURL(getYourRightsURI(), HtmlLoader.loadResourceFile(webView.getContext(), C0769R.raw.rights, arrayMap), "text/html", Constants.ENCODING, null);
    }

    private static void loadAbout(WebView webView) {
        String str;
        Object obj;
        Context context = webView.getContext();
        Resources localizedResources = Locales.getLocalizedResources(webView.getContext());
        String loadWebViewVersion = DebugUtils.loadWebViewVersion(webView.getContext());
        ArrayMap arrayMap = new ArrayMap();
        String string = webView.getContext().getResources().getString(C0769R.string.app_name);
        String string2 = webView.getContext().getResources().getString(C0769R.string.mozilla);
        string2 = webView.getContext().getResources().getString(C0769R.string.about_content_body, new Object[]{string, string2});
        String aboutURI = getAboutURI();
        String manifestoURL = getManifestoURL();
        String sumoURLForTopic = getSumoURLForTopic(webView.getContext(), "rocket-help");
        String yourRightsURI = getYourRightsURI();
        String privacyURL = getPrivacyURL();
        String sumoURLForTopic2 = getSumoURLForTopic(webView.getContext(), "firefox-lite-feed");
        String string3 = localizedResources.getString(C0769R.string.about_link_learn_more);
        String string4 = localizedResources.getString(C0769R.string.about_link_support);
        String string5 = localizedResources.getString(C0769R.string.about_link_your_rights);
        String string6 = localizedResources.getString(C0769R.string.about_link_privacy);
        String str2 = aboutURI;
        String str3 = loadWebViewVersion;
        loadWebViewVersion = localizedResources.getString(C0769R.string.about_link_life_feed, new Object[]{localizedResources.getString(C0769R.string.life_feed)});
        aboutURI = "";
        try {
            str = aboutURI;
            try {
                obj = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (NameNotFoundException unused) {
            }
        } catch (NameNotFoundException unused2) {
            str = aboutURI;
            obj = str;
            arrayMap.put("%about-version%", obj);
            arrayMap.put("%about-content%", localizedResources.getString(C0769R.string.about_content, new Object[]{string2, manifestoURL, string3, sumoURLForTopic, string4, yourRightsURI, string5, privacyURL, string6, sumoURLForTopic2, loadWebViewVersion}));
            arrayMap.put("%wordmark%", HtmlLoader.loadDrawableAsDataURI(webView.getContext(), 2131230881, C0769R.color.about_logo_color));
            arrayMap.put("%webview-version%", str3);
            webView.loadDataWithBaseURL(str2, HtmlLoader.loadResourceFile(webView.getContext(), C0769R.raw.about, arrayMap), "text/html", Constants.ENCODING, null);
        }
        arrayMap.put("%about-version%", obj);
        arrayMap.put("%about-content%", localizedResources.getString(C0769R.string.about_content, new Object[]{string2, manifestoURL, string3, sumoURLForTopic, string4, yourRightsURI, string5, privacyURL, string6, sumoURLForTopic2, loadWebViewVersion}));
        arrayMap.put("%wordmark%", HtmlLoader.loadDrawableAsDataURI(webView.getContext(), 2131230881, C0769R.color.about_logo_color));
        arrayMap.put("%webview-version%", str3);
        webView.loadDataWithBaseURL(str2, HtmlLoader.loadResourceFile(webView.getContext(), C0769R.raw.about, arrayMap), "text/html", Constants.ENCODING, null);
    }
}
