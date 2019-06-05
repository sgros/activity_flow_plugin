package org.mozilla.telemetry.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class ContextUtils {
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static String getAppName(Context context) {
        return getApplicationInfo(context).loadLabel(context.getPackageManager()).toString();
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException unused) {
            throw new AssertionError("Could not get own package info");
        }
    }

    private static ApplicationInfo getApplicationInfo(Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException unused) {
            throw new AssertionError("Could not get own package info");
        }
    }
}
