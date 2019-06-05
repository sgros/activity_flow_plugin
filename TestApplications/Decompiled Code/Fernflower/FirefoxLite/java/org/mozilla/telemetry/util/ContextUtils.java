package org.mozilla.telemetry.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ContextUtils {
   public static String getAppName(Context var0) {
      PackageManager var1 = var0.getPackageManager();
      return getApplicationInfo(var0).loadLabel(var1).toString();
   }

   private static ApplicationInfo getApplicationInfo(Context var0) {
      try {
         ApplicationInfo var2 = var0.getPackageManager().getApplicationInfo(var0.getPackageName(), 0);
         return var2;
      } catch (NameNotFoundException var1) {
         throw new AssertionError("Could not get own package info");
      }
   }

   private static PackageInfo getPackageInfo(Context var0) {
      try {
         PackageInfo var2 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
         return var2;
      } catch (NameNotFoundException var1) {
         throw new AssertionError("Could not get own package info");
      }
   }

   public static int getVersionCode(Context var0) {
      return getPackageInfo(var0).versionCode;
   }

   public static String getVersionName(Context var0) {
      return getPackageInfo(var0).versionName;
   }
}
