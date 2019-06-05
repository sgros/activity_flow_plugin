package org.mozilla.focus.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Browsers {
   private final Map browsers;
   private final ActivityInfo defaultBrowser;
   private ActivityInfo firefoxBrandedBrowser;

   public Browsers(Context var1, String var2) {
      PackageManager var4 = var1.getPackageManager();
      Uri var3 = Uri.parse(var2);
      Map var5 = this.resolveBrowsers(var4, var3);
      this.findKnownBrowsers(var4, var5, var3);
      this.browsers = var5;
      this.defaultBrowser = this.findDefault(var4, var3);
      this.firefoxBrandedBrowser = this.findFirefoxBrandedBrowser();
   }

   private ActivityInfo findDefault(PackageManager var1, Uri var2) {
      ResolveInfo var3 = var1.resolveActivity(new Intent("android.intent.action.VIEW", var2), 0);
      if (var3 != null && var3.activityInfo != null) {
         if (!var3.activityInfo.exported) {
            return null;
         } else {
            return !this.browsers.containsKey(var3.activityInfo.packageName) ? null : var3.activityInfo;
         }
      } else {
         return null;
      }
   }

   private ActivityInfo findFirefoxBrandedBrowser() {
      if (this.browsers.containsKey(Browsers.KnownBrowser.FIREFOX.packageName)) {
         return (ActivityInfo)this.browsers.get(Browsers.KnownBrowser.FIREFOX.packageName);
      } else if (this.browsers.containsKey(Browsers.KnownBrowser.FIREFOX_BETA.packageName)) {
         return (ActivityInfo)this.browsers.get(Browsers.KnownBrowser.FIREFOX_BETA.packageName);
      } else if (this.browsers.containsKey(Browsers.KnownBrowser.FIREFOX_AURORA.packageName)) {
         return (ActivityInfo)this.browsers.get(Browsers.KnownBrowser.FIREFOX_AURORA.packageName);
      } else {
         return this.browsers.containsKey(Browsers.KnownBrowser.FIREFOX_NIGHTLY.packageName) ? (ActivityInfo)this.browsers.get(Browsers.KnownBrowser.FIREFOX_NIGHTLY.packageName) : null;
      }
   }

   private void findKnownBrowsers(PackageManager var1, Map var2, Uri var3) {
      Browsers.KnownBrowser[] var4 = Browsers.KnownBrowser.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Browsers.KnownBrowser var7 = var4[var6];
         if (!var2.containsKey(var7.packageName)) {
            try {
               var1.getPackageInfo(var7.packageName, 0);
            } catch (NameNotFoundException var9) {
               continue;
            }

            Intent var8 = new Intent("android.intent.action.VIEW");
            var8.setData(var3);
            var8.setPackage(var7.packageName);
            ResolveInfo var10 = var1.resolveActivity(var8, 0);
            if (var10 != null && var10.activityInfo != null && var10.activityInfo.exported) {
               var2.put(var10.activityInfo.packageName, var10.activityInfo);
            }
         }
      }

   }

   public static boolean hasDefaultBrowser(Context var0) {
      boolean var1;
      if ((new Browsers(var0, "http://mozilla.org")).defaultBrowser != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isDefaultBrowser(Context var0) {
      Browsers var1 = new Browsers(var0, "http://mozilla.org");
      boolean var2;
      if (var1.defaultBrowser != null && var0.getPackageName().equals(var1.defaultBrowser.packageName)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private Map resolveBrowsers(PackageManager var1, Uri var2) {
      HashMap var3 = new HashMap();
      Intent var4 = new Intent("android.intent.action.VIEW");
      var4.setData(var2);
      Iterator var6 = var1.queryIntentActivities(var4, 0).iterator();

      while(var6.hasNext()) {
         ResolveInfo var5 = (ResolveInfo)var6.next();
         var3.put(var5.activityInfo.packageName, var5.activityInfo);
      }

      return var3;
   }

   public static enum KnownBrowser {
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

      private KnownBrowser(String var3) {
         this.packageName = var3;
      }
   }
}
