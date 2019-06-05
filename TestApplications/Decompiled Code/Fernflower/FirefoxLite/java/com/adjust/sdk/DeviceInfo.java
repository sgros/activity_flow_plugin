package com.adjust.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

class DeviceInfo {
   String abi;
   String androidId;
   String apiLevel;
   String appInstallTime;
   String appUpdateTime;
   String appVersion;
   String buildName;
   String clientSdk;
   String country;
   String deviceManufacturer;
   String deviceName;
   String deviceType;
   String displayHeight;
   String displayWidth;
   String fbAttributionId;
   String hardwareName;
   String language;
   String macSha1;
   String macShortMd5;
   String osName;
   String osVersion;
   String packageName;
   Map pluginKeys;
   String screenDensity;
   String screenFormat;
   String screenSize;
   String vmInstructionSet;

   DeviceInfo(Context var1, String var2) {
      Resources var3 = var1.getResources();
      DisplayMetrics var4 = var3.getDisplayMetrics();
      Configuration var5 = var3.getConfiguration();
      Locale var9 = Util.getLocale(var5);
      int var6 = var5.screenLayout;
      boolean var7;
      if (Util.getPlayAdId(var1) != null) {
         var7 = true;
      } else {
         var7 = false;
      }

      String var8 = this.getMacAddress(var1, var7);
      var1.getContentResolver();
      this.packageName = this.getPackageName(var1);
      this.appVersion = this.getAppVersion(var1);
      this.deviceType = this.getDeviceType(var6);
      this.deviceName = this.getDeviceName();
      this.deviceManufacturer = this.getDeviceManufacturer();
      this.osName = this.getOsName();
      this.osVersion = this.getOsVersion();
      this.apiLevel = this.getApiLevel();
      this.language = this.getLanguage(var9);
      this.country = this.getCountry(var9);
      this.screenSize = this.getScreenSize(var6);
      this.screenFormat = this.getScreenFormat(var6);
      this.screenDensity = this.getScreenDensity(var4);
      this.displayWidth = this.getDisplayWidth(var4);
      this.displayHeight = this.getDisplayHeight(var4);
      this.clientSdk = this.getClientSdk(var2);
      this.androidId = this.getAndroidId(var1, var7);
      this.fbAttributionId = this.getFacebookAttributionId(var1);
      this.pluginKeys = Util.getPluginKeys(var1);
      this.macSha1 = this.getMacSha1(var8);
      this.macShortMd5 = this.getMacShortMd5(var8);
      this.hardwareName = this.getHardwareName();
      this.abi = this.getABI();
      this.buildName = this.getBuildName();
      this.vmInstructionSet = this.getVmInstructionSet();
      this.appInstallTime = this.getAppInstallTime(var1);
      this.appUpdateTime = this.getAppUpdateTime(var1);
   }

   private String getABI() {
      String[] var1 = Util.getSupportedAbis();
      return var1 != null && var1.length != 0 ? var1[0] : Util.getCpuAbi();
   }

   private String getAndroidId(Context var1, boolean var2) {
      return !var2 ? Util.getAndroidId(var1) : null;
   }

   private String getApiLevel() {
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append(VERSION.SDK_INT);
      return var1.toString();
   }

   private String getAppInstallTime(Context var1) {
      try {
         PackageInfo var2 = var1.getPackageManager().getPackageInfo(var1.getPackageName(), 4096);
         SimpleDateFormat var5 = Util.dateFormatter;
         Date var3 = new Date(var2.firstInstallTime);
         String var6 = var5.format(var3);
         return var6;
      } catch (Exception var4) {
         return null;
      }
   }

   private String getAppUpdateTime(Context var1) {
      try {
         PackageInfo var5 = var1.getPackageManager().getPackageInfo(var1.getPackageName(), 4096);
         SimpleDateFormat var2 = Util.dateFormatter;
         Date var3 = new Date(var5.lastUpdateTime);
         String var6 = var2.format(var3);
         return var6;
      } catch (Exception var4) {
         return null;
      }
   }

   private String getAppVersion(Context var1) {
      try {
         String var3 = var1.getPackageManager().getPackageInfo(var1.getPackageName(), 0).versionName;
         return var3;
      } catch (Exception var2) {
         return null;
      }
   }

   private String getBuildName() {
      return Build.ID;
   }

   private String getClientSdk(String var1) {
      return var1 == null ? "android4.11.4" : String.format(Locale.US, "%s@%s", var1, "android4.11.4");
   }

   private String getCountry(Locale var1) {
      return var1.getCountry();
   }

   private String getDeviceManufacturer() {
      return Build.MANUFACTURER;
   }

   private String getDeviceName() {
      return Build.MODEL;
   }

   private String getDeviceType(int var1) {
      switch(var1 & 15) {
      case 1:
      case 2:
         return "phone";
      case 3:
      case 4:
         return "tablet";
      default:
         return null;
      }
   }

   private String getDisplayHeight(DisplayMetrics var1) {
      return String.valueOf(var1.heightPixels);
   }

   private String getDisplayWidth(DisplayMetrics var1) {
      return String.valueOf(var1.widthPixels);
   }

   private String getFacebookAttributionId(Context param1) {
      // $FF: Couldn't be decompiled
   }

   private String getHardwareName() {
      return Build.DISPLAY;
   }

   private String getLanguage(Locale var1) {
      return var1.getLanguage();
   }

   private String getMacAddress(Context var1, boolean var2) {
      if (!var2) {
         if (!Util.checkPermission(var1, "android.permission.ACCESS_WIFI_STATE")) {
            AdjustFactory.getLogger().warn("Missing permission: ACCESS_WIFI_STATE");
         }

         return Util.getMacAddress(var1);
      } else {
         return null;
      }
   }

   private String getMacSha1(String var1) {
      return var1 == null ? null : Util.sha1(var1);
   }

   private String getMacShortMd5(String var1) {
      return var1 == null ? null : Util.md5(var1.replaceAll(":", ""));
   }

   private String getOsName() {
      return "android";
   }

   private String getOsVersion() {
      return VERSION.RELEASE;
   }

   private String getPackageName(Context var1) {
      return var1.getPackageName();
   }

   private String getScreenDensity(DisplayMetrics var1) {
      int var2 = var1.densityDpi;
      if (var2 == 0) {
         return null;
      } else if (var2 < 140) {
         return "low";
      } else {
         return var2 > 200 ? "high" : "medium";
      }
   }

   private String getScreenFormat(int var1) {
      var1 &= 48;
      if (var1 != 16) {
         return var1 != 32 ? null : "long";
      } else {
         return "normal";
      }
   }

   private String getScreenSize(int var1) {
      switch(var1 & 15) {
      case 1:
         return "small";
      case 2:
         return "normal";
      case 3:
         return "large";
      case 4:
         return "xlarge";
      default:
         return null;
      }
   }

   private String getVmInstructionSet() {
      return Util.getVmInstructionSet();
   }
}
