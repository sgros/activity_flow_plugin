// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.preference.PreferenceManager;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager
{
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static SharedPreferences pref;
    
    public PrefManager(final Context context) {
        PrefManager.pref = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public static boolean isFirstTimeLaunch() {
        return PrefManager.pref.getBoolean("IsFirstTimeLaunch", true);
    }
    
    public static void setFirstTimeLaunch(final boolean b) {
        PrefManager.pref.edit().putBoolean("IsFirstTimeLaunch", b).apply();
    }
}
