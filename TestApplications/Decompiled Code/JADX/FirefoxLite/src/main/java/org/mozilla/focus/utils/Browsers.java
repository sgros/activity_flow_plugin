package org.mozilla.focus.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

public class Browsers {
    private final Map<String, ActivityInfo> browsers;
    private final ActivityInfo defaultBrowser;
    private ActivityInfo firefoxBrandedBrowser = findFirefoxBrandedBrowser();

    public enum KnownBrowser {
        FIREFOX("org.mozilla.firefox"),
        FIREFOX_BETA("org.mozilla.firefox_beta"),
        FIREFOX_AURORA("org.mozilla.fennec_aurora"),
        FIREFOX_NIGHTLY("org.mozilla.fennec"),
        CHROME("com.android.chrome"),
        CHROME_BETA("com.chrome.beta"),
        CHROME_DEV("com.chrome.dev"),
        CHROME_CANARY("com.chrome.canary"),
        OPERA("com.opera.browser"),
        OPERA_BETA("com.opera.browser.beta"),
        OPERA_MINI("com.opera.mini.native"),
        OPERA_MINI_BETA("com.opera.mini.native.beta"),
        UC_BROWSER("com.UCMobile.intl"),
        UC_BROWSER_MINI("com.uc.browser.en"),
        ANDROID_STOCK_BROWSER("com.android.browser"),
        SAMSUNG_INTERNET("com.sec.android.app.sbrowser"),
        DOLPHIN_BROWSER("mobi.mgeek.TunnyBrowser"),
        BRAVE_BROWSER("com.brave.browser"),
        LINK_BUBBLE("com.linkbubble.playstore"),
        ADBLOCK_BROWSER("org.adblockplus.browser"),
        CHROMER("arun.com.chromer"),
        FLYNX("com.flynx"),
        GHOSTERY_BROWSER("com.ghostery.android.ghostery");
        
        public final String packageName;

        private KnownBrowser(String str) {
            this.packageName = str;
        }
    }

    public Browsers(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        Uri parse = Uri.parse(str);
        Map resolveBrowsers = resolveBrowsers(packageManager, parse);
        findKnownBrowsers(packageManager, resolveBrowsers, parse);
        this.browsers = resolveBrowsers;
        this.defaultBrowser = findDefault(packageManager, parse);
    }

    private ActivityInfo findFirefoxBrandedBrowser() {
        if (this.browsers.containsKey(KnownBrowser.FIREFOX.packageName)) {
            return (ActivityInfo) this.browsers.get(KnownBrowser.FIREFOX.packageName);
        }
        if (this.browsers.containsKey(KnownBrowser.FIREFOX_BETA.packageName)) {
            return (ActivityInfo) this.browsers.get(KnownBrowser.FIREFOX_BETA.packageName);
        }
        if (this.browsers.containsKey(KnownBrowser.FIREFOX_AURORA.packageName)) {
            return (ActivityInfo) this.browsers.get(KnownBrowser.FIREFOX_AURORA.packageName);
        }
        return this.browsers.containsKey(KnownBrowser.FIREFOX_NIGHTLY.packageName) ? (ActivityInfo) this.browsers.get(KnownBrowser.FIREFOX_NIGHTLY.packageName) : null;
    }

    private Map<String, ActivityInfo> resolveBrowsers(PackageManager packageManager, Uri uri) {
        HashMap hashMap = new HashMap();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(uri);
        for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(intent, 0)) {
            hashMap.put(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo);
        }
        return hashMap;
    }

    private void findKnownBrowsers(PackageManager packageManager, Map<String, ActivityInfo> map, Uri uri) {
        for (KnownBrowser knownBrowser : KnownBrowser.values()) {
            if (!map.containsKey(knownBrowser.packageName)) {
                try {
                    packageManager.getPackageInfo(knownBrowser.packageName, 0);
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(uri);
                    intent.setPackage(knownBrowser.packageName);
                    ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 0);
                    if (!(resolveActivity == null || resolveActivity.activityInfo == null || !resolveActivity.activityInfo.exported)) {
                        map.put(resolveActivity.activityInfo.packageName, resolveActivity.activityInfo);
                    }
                } catch (NameNotFoundException unused) {
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:12:0x002b, code skipped:
            return null;
     */
    private android.content.pm.ActivityInfo findDefault(android.content.pm.PackageManager r3, android.net.Uri r4) {
        /*
        r2 = this;
        r0 = new android.content.Intent;
        r1 = "android.intent.action.VIEW";
        r0.<init>(r1, r4);
        r4 = 0;
        r3 = r3.resolveActivity(r0, r4);
        r4 = 0;
        if (r3 == 0) goto L_0x002b;
    L_0x000f:
        r0 = r3.activityInfo;
        if (r0 != 0) goto L_0x0014;
    L_0x0013:
        goto L_0x002b;
    L_0x0014:
        r0 = r3.activityInfo;
        r0 = r0.exported;
        if (r0 != 0) goto L_0x001b;
    L_0x001a:
        return r4;
    L_0x001b:
        r0 = r2.browsers;
        r1 = r3.activityInfo;
        r1 = r1.packageName;
        r0 = r0.containsKey(r1);
        if (r0 != 0) goto L_0x0028;
    L_0x0027:
        return r4;
    L_0x0028:
        r3 = r3.activityInfo;
        return r3;
    L_0x002b:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.utils.Browsers.findDefault(android.content.pm.PackageManager, android.net.Uri):android.content.pm.ActivityInfo");
    }

    public static boolean isDefaultBrowser(Context context) {
        Browsers browsers = new Browsers(context, "http://mozilla.org");
        return browsers.defaultBrowser != null && context.getPackageName().equals(browsers.defaultBrowser.packageName);
    }

    public static boolean hasDefaultBrowser(Context context) {
        return new Browsers(context, "http://mozilla.org").defaultBrowser != null;
    }
}
