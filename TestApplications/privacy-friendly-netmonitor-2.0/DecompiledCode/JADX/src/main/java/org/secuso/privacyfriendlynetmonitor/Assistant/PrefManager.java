package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefManager {
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static SharedPreferences pref;

    public PrefManager(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setFirstTimeLaunch(boolean z) {
        pref.edit().putBoolean(IS_FIRST_TIME_LAUNCH, z).apply();
    }

    public static boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
