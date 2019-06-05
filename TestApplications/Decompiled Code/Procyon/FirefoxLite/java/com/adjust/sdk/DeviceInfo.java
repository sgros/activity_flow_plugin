// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import java.util.Date;
import android.os.Build$VERSION;
import java.util.Locale;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.content.res.Resources;
import android.content.Context;
import java.util.Map;

class DeviceInfo
{
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
    Map<String, String> pluginKeys;
    String screenDensity;
    String screenFormat;
    String screenSize;
    String vmInstructionSet;
    
    DeviceInfo(final Context context, final String s) {
        final Resources resources = context.getResources();
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        final Configuration configuration = resources.getConfiguration();
        final Locale locale = Util.getLocale(configuration);
        final int screenLayout = configuration.screenLayout;
        final boolean b = Util.getPlayAdId(context) != null;
        final String macAddress = this.getMacAddress(context, b);
        context.getContentResolver();
        this.packageName = this.getPackageName(context);
        this.appVersion = this.getAppVersion(context);
        this.deviceType = this.getDeviceType(screenLayout);
        this.deviceName = this.getDeviceName();
        this.deviceManufacturer = this.getDeviceManufacturer();
        this.osName = this.getOsName();
        this.osVersion = this.getOsVersion();
        this.apiLevel = this.getApiLevel();
        this.language = this.getLanguage(locale);
        this.country = this.getCountry(locale);
        this.screenSize = this.getScreenSize(screenLayout);
        this.screenFormat = this.getScreenFormat(screenLayout);
        this.screenDensity = this.getScreenDensity(displayMetrics);
        this.displayWidth = this.getDisplayWidth(displayMetrics);
        this.displayHeight = this.getDisplayHeight(displayMetrics);
        this.clientSdk = this.getClientSdk(s);
        this.androidId = this.getAndroidId(context, b);
        this.fbAttributionId = this.getFacebookAttributionId(context);
        this.pluginKeys = Util.getPluginKeys(context);
        this.macSha1 = this.getMacSha1(macAddress);
        this.macShortMd5 = this.getMacShortMd5(macAddress);
        this.hardwareName = this.getHardwareName();
        this.abi = this.getABI();
        this.buildName = this.getBuildName();
        this.vmInstructionSet = this.getVmInstructionSet();
        this.appInstallTime = this.getAppInstallTime(context);
        this.appUpdateTime = this.getAppUpdateTime(context);
    }
    
    private String getABI() {
        final String[] supportedAbis = Util.getSupportedAbis();
        if (supportedAbis != null && supportedAbis.length != 0) {
            return supportedAbis[0];
        }
        return Util.getCpuAbi();
    }
    
    private String getAndroidId(final Context context, final boolean b) {
        if (!b) {
            return Util.getAndroidId(context);
        }
        return null;
    }
    
    private String getApiLevel() {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(Build$VERSION.SDK_INT);
        return sb.toString();
    }
    
    private String getAppInstallTime(final Context context) {
        try {
            return Util.dateFormatter.format(new Date(context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).firstInstallTime));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private String getAppUpdateTime(final Context context) {
        try {
            return Util.dateFormatter.format(new Date(context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).lastUpdateTime));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private String getAppVersion(final Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private String getBuildName() {
        return Build.ID;
    }
    
    private String getClientSdk(final String s) {
        if (s == null) {
            return "android4.11.4";
        }
        return String.format(Locale.US, "%s@%s", s, "android4.11.4");
    }
    
    private String getCountry(final Locale locale) {
        return locale.getCountry();
    }
    
    private String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }
    
    private String getDeviceName() {
        return Build.MODEL;
    }
    
    private String getDeviceType(final int n) {
        switch (n & 0xF) {
            default: {
                return null;
            }
            case 3:
            case 4: {
                return "tablet";
            }
            case 1:
            case 2: {
                return "phone";
            }
        }
    }
    
    private String getDisplayHeight(final DisplayMetrics displayMetrics) {
        return String.valueOf(displayMetrics.heightPixels);
    }
    
    private String getDisplayWidth(final DisplayMetrics displayMetrics) {
        return String.valueOf(displayMetrics.widthPixels);
    }
    
    private String getFacebookAttributionId(final Context context) {
        try {
            final Cursor query = context.getContentResolver().query(Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider"), new String[] { "aid" }, (String)null, (String[])null, (String)null);
            if (query == null) {
                return null;
            }
            if (!query.moveToFirst()) {
                query.close();
                return null;
            }
            final String string = query.getString(query.getColumnIndex("aid"));
            query.close();
            return string;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private String getHardwareName() {
        return Build.DISPLAY;
    }
    
    private String getLanguage(final Locale locale) {
        return locale.getLanguage();
    }
    
    private String getMacAddress(final Context context, final boolean b) {
        if (!b) {
            if (!Util.checkPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
                AdjustFactory.getLogger().warn("Missing permission: ACCESS_WIFI_STATE", new Object[0]);
            }
            return Util.getMacAddress(context);
        }
        return null;
    }
    
    private String getMacSha1(final String s) {
        if (s == null) {
            return null;
        }
        return Util.sha1(s);
    }
    
    private String getMacShortMd5(final String s) {
        if (s == null) {
            return null;
        }
        return Util.md5(s.replaceAll(":", ""));
    }
    
    private String getOsName() {
        return "android";
    }
    
    private String getOsVersion() {
        return Build$VERSION.RELEASE;
    }
    
    private String getPackageName(final Context context) {
        return context.getPackageName();
    }
    
    private String getScreenDensity(final DisplayMetrics displayMetrics) {
        final int densityDpi = displayMetrics.densityDpi;
        if (densityDpi == 0) {
            return null;
        }
        if (densityDpi < 140) {
            return "low";
        }
        if (densityDpi > 200) {
            return "high";
        }
        return "medium";
    }
    
    private String getScreenFormat(int n) {
        n &= 0x30;
        if (n == 16) {
            return "normal";
        }
        if (n != 32) {
            return null;
        }
        return "long";
    }
    
    private String getScreenSize(final int n) {
        switch (n & 0xF) {
            default: {
                return null;
            }
            case 4: {
                return "xlarge";
            }
            case 3: {
                return "large";
            }
            case 2: {
                return "normal";
            }
            case 1: {
                return "small";
            }
        }
    }
    
    private String getVmInstructionSet() {
        return Util.getVmInstructionSet();
    }
}
