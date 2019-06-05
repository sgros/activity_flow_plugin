package com.adjust.sdk;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
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
    Map<String, String> pluginKeys;
    String screenDensity;
    String screenFormat;
    String screenSize;
    String vmInstructionSet;

    private String getDeviceType(int i) {
        switch (i & 15) {
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

    private String getOsName() {
        return "android";
    }

    private String getScreenFormat(int i) {
        i &= 48;
        return i != 16 ? i != 32 ? null : Constants.LONG : Constants.NORMAL;
    }

    private String getScreenSize(int i) {
        switch (i & 15) {
            case 1:
                return Constants.SMALL;
            case 2:
                return Constants.NORMAL;
            case 3:
                return Constants.LARGE;
            case 4:
                return Constants.XLARGE;
            default:
                return null;
        }
    }

    DeviceInfo(Context context, String str) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        Locale locale = Util.getLocale(configuration);
        int i = configuration.screenLayout;
        boolean z = Util.getPlayAdId(context) != null;
        String macAddress = getMacAddress(context, z);
        context.getContentResolver();
        this.packageName = getPackageName(context);
        this.appVersion = getAppVersion(context);
        this.deviceType = getDeviceType(i);
        this.deviceName = getDeviceName();
        this.deviceManufacturer = getDeviceManufacturer();
        this.osName = getOsName();
        this.osVersion = getOsVersion();
        this.apiLevel = getApiLevel();
        this.language = getLanguage(locale);
        this.country = getCountry(locale);
        this.screenSize = getScreenSize(i);
        this.screenFormat = getScreenFormat(i);
        this.screenDensity = getScreenDensity(displayMetrics);
        this.displayWidth = getDisplayWidth(displayMetrics);
        this.displayHeight = getDisplayHeight(displayMetrics);
        this.clientSdk = getClientSdk(str);
        this.androidId = getAndroidId(context, z);
        this.fbAttributionId = getFacebookAttributionId(context);
        this.pluginKeys = Util.getPluginKeys(context);
        this.macSha1 = getMacSha1(macAddress);
        this.macShortMd5 = getMacShortMd5(macAddress);
        this.hardwareName = getHardwareName();
        this.abi = getABI();
        this.buildName = getBuildName();
        this.vmInstructionSet = getVmInstructionSet();
        this.appInstallTime = getAppInstallTime(context);
        this.appUpdateTime = getAppUpdateTime(context);
    }

    private String getMacAddress(Context context, boolean z) {
        if (z) {
            return null;
        }
        if (!Util.checkPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
            AdjustFactory.getLogger().warn("Missing permission: ACCESS_WIFI_STATE", new Object[0]);
        }
        return Util.getMacAddress(context);
    }

    private String getPackageName(Context context) {
        return context.getPackageName();
    }

    private String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception unused) {
            return null;
        }
    }

    private String getDeviceName() {
        return Build.MODEL;
    }

    private String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    private String getOsVersion() {
        return VERSION.RELEASE;
    }

    private String getApiLevel() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(VERSION.SDK_INT);
        return stringBuilder.toString();
    }

    private String getLanguage(Locale locale) {
        return locale.getLanguage();
    }

    private String getCountry(Locale locale) {
        return locale.getCountry();
    }

    private String getBuildName() {
        return Build.ID;
    }

    private String getHardwareName() {
        return Build.DISPLAY;
    }

    private String getScreenDensity(DisplayMetrics displayMetrics) {
        int i = displayMetrics.densityDpi;
        if (i == 0) {
            return null;
        }
        if (i < 140) {
            return Constants.LOW;
        }
        return i > 200 ? Constants.HIGH : Constants.MEDIUM;
    }

    private String getDisplayWidth(DisplayMetrics displayMetrics) {
        return String.valueOf(displayMetrics.widthPixels);
    }

    private String getDisplayHeight(DisplayMetrics displayMetrics) {
        return String.valueOf(displayMetrics.heightPixels);
    }

    private String getClientSdk(String str) {
        if (str == null) {
            return Constants.CLIENT_SDK;
        }
        return String.format(Locale.US, "%s@%s", new Object[]{str, Constants.CLIENT_SDK});
    }

    private String getMacSha1(String str) {
        return str == null ? null : Util.sha1(str);
    }

    private String getMacShortMd5(String str) {
        if (str == null) {
            return null;
        }
        return Util.md5(str.replaceAll(":", ""));
    }

    private String getAndroidId(Context context, boolean z) {
        return !z ? Util.getAndroidId(context) : null;
    }

    private String getFacebookAttributionId(Context context) {
        try {
            Cursor query = context.getContentResolver().query(Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider"), new String[]{"aid"}, null, null, null);
            if (query == null) {
                return null;
            }
            if (query.moveToFirst()) {
                String string = query.getString(query.getColumnIndex("aid"));
                query.close();
                return string;
            }
            query.close();
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    private String getABI() {
        String[] supportedAbis = Util.getSupportedAbis();
        if (supportedAbis == null || supportedAbis.length == 0) {
            return Util.getCpuAbi();
        }
        return supportedAbis[0];
    }

    private String getVmInstructionSet() {
        return Util.getVmInstructionSet();
    }

    private String getAppInstallTime(Context context) {
        try {
            return Util.dateFormatter.format(new Date(context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).firstInstallTime));
        } catch (Exception unused) {
            return null;
        }
    }

    private String getAppUpdateTime(Context context) {
        try {
            return Util.dateFormatter.format(new Date(context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).lastUpdateTime));
        } catch (Exception unused) {
            return null;
        }
    }
}
