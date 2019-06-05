// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.pm.ApplicationInfo;
import android.content.Context;

public class ContextUtils
{
    public static String getAppName(final Context context) {
        return getApplicationInfo(context).loadLabel(context.getPackageManager()).toString();
    }
    
    private static ApplicationInfo getApplicationInfo(final Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new AssertionError((Object)"Could not get own package info");
        }
    }
    
    private static PackageInfo getPackageInfo(final Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new AssertionError((Object)"Could not get own package info");
        }
    }
    
    public static int getVersionCode(final Context context) {
        return getPackageInfo(context).versionCode;
    }
    
    public static String getVersionName(final Context context) {
        return getPackageInfo(context).versionName;
    }
}
