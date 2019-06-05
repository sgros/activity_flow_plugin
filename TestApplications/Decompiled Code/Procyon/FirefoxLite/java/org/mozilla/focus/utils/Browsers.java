// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import java.util.Iterator;
import java.util.HashMap;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.Context;
import android.content.pm.ActivityInfo;
import java.util.Map;

public class Browsers
{
    private final Map<String, ActivityInfo> browsers;
    private final ActivityInfo defaultBrowser;
    private ActivityInfo firefoxBrandedBrowser;
    
    public Browsers(final Context context, final String s) {
        final PackageManager packageManager = context.getPackageManager();
        final Uri parse = Uri.parse(s);
        final Map<String, ActivityInfo> resolveBrowsers = this.resolveBrowsers(packageManager, parse);
        this.findKnownBrowsers(packageManager, resolveBrowsers, parse);
        this.browsers = resolveBrowsers;
        this.defaultBrowser = this.findDefault(packageManager, parse);
        this.firefoxBrandedBrowser = this.findFirefoxBrandedBrowser();
    }
    
    private ActivityInfo findDefault(final PackageManager packageManager, final Uri uri) {
        final ResolveInfo resolveActivity = packageManager.resolveActivity(new Intent("android.intent.action.VIEW", uri), 0);
        if (resolveActivity == null || resolveActivity.activityInfo == null) {
            return null;
        }
        if (!resolveActivity.activityInfo.exported) {
            return null;
        }
        if (!this.browsers.containsKey(resolveActivity.activityInfo.packageName)) {
            return null;
        }
        return resolveActivity.activityInfo;
    }
    
    private ActivityInfo findFirefoxBrandedBrowser() {
        if (this.browsers.containsKey(KnownBrowser.FIREFOX.packageName)) {
            return this.browsers.get(KnownBrowser.FIREFOX.packageName);
        }
        if (this.browsers.containsKey(KnownBrowser.FIREFOX_BETA.packageName)) {
            return this.browsers.get(KnownBrowser.FIREFOX_BETA.packageName);
        }
        if (this.browsers.containsKey(KnownBrowser.FIREFOX_AURORA.packageName)) {
            return this.browsers.get(KnownBrowser.FIREFOX_AURORA.packageName);
        }
        if (this.browsers.containsKey(KnownBrowser.FIREFOX_NIGHTLY.packageName)) {
            return this.browsers.get(KnownBrowser.FIREFOX_NIGHTLY.packageName);
        }
        return null;
    }
    
    private void findKnownBrowsers(final PackageManager packageManager, final Map<String, ActivityInfo> map, final Uri data) {
        final KnownBrowser[] values = KnownBrowser.values();
        final int length = values.length;
        int n = 0;
    Label_0143_Outer:
        while (true) {
            if (n >= length) {
                return;
            }
            final KnownBrowser knownBrowser = values[n];
            while (true) {
                if (map.containsKey(knownBrowser.packageName)) {
                    break Label_0143;
                }
                try {
                    packageManager.getPackageInfo(knownBrowser.packageName, 0);
                    final Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(data);
                    intent.setPackage(knownBrowser.packageName);
                    final ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 0);
                    if (resolveActivity != null) {
                        if (resolveActivity.activityInfo != null) {
                            if (resolveActivity.activityInfo.exported) {
                                map.put(resolveActivity.activityInfo.packageName, resolveActivity.activityInfo);
                            }
                        }
                    }
                    ++n;
                    continue Label_0143_Outer;
                }
                catch (PackageManager$NameNotFoundException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public static boolean hasDefaultBrowser(final Context context) {
        return new Browsers(context, "http://mozilla.org").defaultBrowser != null;
    }
    
    public static boolean isDefaultBrowser(final Context context) {
        final Browsers browsers = new Browsers(context, "http://mozilla.org");
        return browsers.defaultBrowser != null && context.getPackageName().equals(browsers.defaultBrowser.packageName);
    }
    
    private Map<String, ActivityInfo> resolveBrowsers(final PackageManager packageManager, final Uri data) {
        final HashMap<String, ActivityInfo> hashMap = new HashMap<String, ActivityInfo>();
        final Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(data);
        for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(intent, 0)) {
            hashMap.put(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo);
        }
        return hashMap;
    }
    
    public enum KnownBrowser
    {
        ADBLOCK_BROWSER("org.adblockplus.browser"), 
        ANDROID_STOCK_BROWSER("com.android.browser"), 
        BRAVE_BROWSER("com.brave.browser"), 
        CHROME("com.android.chrome"), 
        CHROMER("arun.com.chromer"), 
        CHROME_BETA("com.chrome.beta"), 
        CHROME_CANARY("com.chrome.canary"), 
        CHROME_DEV("com.chrome.dev"), 
        DOLPHIN_BROWSER("mobi.mgeek.TunnyBrowser"), 
        FIREFOX("org.mozilla.firefox"), 
        FIREFOX_AURORA("org.mozilla.fennec_aurora"), 
        FIREFOX_BETA("org.mozilla.firefox_beta"), 
        FIREFOX_NIGHTLY("org.mozilla.fennec"), 
        FLYNX("com.flynx"), 
        GHOSTERY_BROWSER("com.ghostery.android.ghostery"), 
        LINK_BUBBLE("com.linkbubble.playstore"), 
        OPERA("com.opera.browser"), 
        OPERA_BETA("com.opera.browser.beta"), 
        OPERA_MINI("com.opera.mini.native"), 
        OPERA_MINI_BETA("com.opera.mini.native.beta"), 
        SAMSUNG_INTERNET("com.sec.android.app.sbrowser"), 
        UC_BROWSER("com.UCMobile.intl"), 
        UC_BROWSER_MINI("com.uc.browser.en");
        
        public final String packageName;
        
        private KnownBrowser(final String packageName) {
            this.packageName = packageName;
        }
    }
}
