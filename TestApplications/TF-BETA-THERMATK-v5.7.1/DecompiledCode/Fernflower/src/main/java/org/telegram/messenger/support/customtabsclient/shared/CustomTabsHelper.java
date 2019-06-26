package org.telegram.messenger.support.customtabsclient.shared;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.ApplicationLoader;

public class CustomTabsHelper {
   private static final String ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService";
   static final String BETA_PACKAGE = "com.chrome.beta";
   static final String DEV_PACKAGE = "com.chrome.dev";
   private static final String EXTRA_CUSTOM_TABS_KEEP_ALIVE = "android.support.customtabs.extra.KEEP_ALIVE";
   static final String LOCAL_PACKAGE = "com.google.android.apps.chrome";
   static final String STABLE_PACKAGE = "com.android.chrome";
   private static final String TAG = "CustomTabsHelper";
   private static String sPackageNameToUse;

   private CustomTabsHelper() {
   }

   public static void addKeepAliveExtra(Context var0, Intent var1) {
      var1.putExtra("android.support.customtabs.extra.KEEP_ALIVE", (new Intent()).setClassName(var0.getPackageName(), KeepAliveService.class.getCanonicalName()));
   }

   public static String getPackageNameToUse(Context var0) {
      String var1 = sPackageNameToUse;
      if (var1 != null) {
         return var1;
      } else {
         PackageManager var2 = var0.getPackageManager();
         Intent var3 = new Intent("android.intent.action.VIEW", Uri.parse("http://www.example.com"));
         ResolveInfo var11 = var2.resolveActivity(var3, 0);
         if (var11 != null) {
            var1 = var11.activityInfo.packageName;
         } else {
            var1 = null;
         }

         List var4 = var2.queryIntentActivities(var3, 0);
         ArrayList var5 = new ArrayList();
         Iterator var13 = var4.iterator();

         while(var13.hasNext()) {
            ResolveInfo var6 = (ResolveInfo)var13.next();
            Intent var7 = new Intent();
            var7.setAction("android.support.customtabs.action.CustomTabsService");
            var7.setPackage(var6.activityInfo.packageName);
            if (var2.resolveService(var7, 0) != null) {
               var5.add(var6.activityInfo.packageName);
            }
         }

         if (var5.isEmpty()) {
            sPackageNameToUse = null;
         } else if (var5.size() == 1) {
            sPackageNameToUse = (String)var5.get(0);
         } else if (!TextUtils.isEmpty(var1) && !hasSpecializedHandlerIntents(var0, var3) && var5.contains(var1)) {
            sPackageNameToUse = var1;
         } else if (var5.contains("com.android.chrome")) {
            sPackageNameToUse = "com.android.chrome";
         } else if (var5.contains("com.chrome.beta")) {
            sPackageNameToUse = "com.chrome.beta";
         } else if (var5.contains("com.chrome.dev")) {
            sPackageNameToUse = "com.chrome.dev";
         } else if (var5.contains("com.google.android.apps.chrome")) {
            sPackageNameToUse = "com.google.android.apps.chrome";
         }

         ApplicationInfo var10;
         PackageManager var12;
         boolean var10001;
         try {
            if (!"com.sec.android.app.sbrowser".equalsIgnoreCase(sPackageNameToUse)) {
               return sPackageNameToUse;
            }

            var12 = ApplicationLoader.applicationContext.getPackageManager();
            var10 = var12.getApplicationInfo("com.android.chrome", 0);
         } catch (Throwable var9) {
            var10001 = false;
            return sPackageNameToUse;
         }

         if (var10 != null) {
            try {
               if (var10.enabled) {
                  var12.getPackageInfo("com.android.chrome", 1);
                  sPackageNameToUse = "com.android.chrome";
               }
            } catch (Throwable var8) {
               var10001 = false;
            }
         }

         return sPackageNameToUse;
      }
   }

   public static String[] getPackages() {
      return new String[]{"", "com.android.chrome", "com.chrome.beta", "com.chrome.dev", "com.google.android.apps.chrome"};
   }

   private static boolean hasSpecializedHandlerIntents(Context var0, Intent var1) {
      label72: {
         boolean var10001;
         List var9;
         try {
            var9 = var0.getPackageManager().queryIntentActivities(var1, 64);
         } catch (RuntimeException var8) {
            var10001 = false;
            break label72;
         }

         if (var9 == null) {
            return false;
         }

         try {
            if (var9.size() == 0) {
               return false;
            }
         } catch (RuntimeException var7) {
            var10001 = false;
            break label72;
         }

         Iterator var10;
         try {
            var10 = var9.iterator();
         } catch (RuntimeException var6) {
            var10001 = false;
            break label72;
         }

         while(true) {
            ResolveInfo var2;
            IntentFilter var11;
            try {
               if (!var10.hasNext()) {
                  return false;
               }

               var2 = (ResolveInfo)var10.next();
               var11 = var2.filter;
            } catch (RuntimeException var4) {
               var10001 = false;
               break;
            }

            if (var11 != null) {
               try {
                  if (var11.countDataAuthorities() == 0 || var11.countDataPaths() == 0) {
                     continue;
                  }
               } catch (RuntimeException var5) {
                  var10001 = false;
                  break;
               }

               ActivityInfo var12;
               try {
                  var12 = var2.activityInfo;
               } catch (RuntimeException var3) {
                  var10001 = false;
                  break;
               }

               if (var12 != null) {
                  return true;
               }
            }
         }
      }

      Log.e("CustomTabsHelper", "Runtime exception while getting specialized handlers");
      return false;
   }
}
