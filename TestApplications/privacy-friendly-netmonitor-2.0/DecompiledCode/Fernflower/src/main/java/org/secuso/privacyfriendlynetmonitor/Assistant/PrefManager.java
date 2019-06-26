package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefManager {
   private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
   private static SharedPreferences pref;

   public PrefManager(Context var1) {
      pref = PreferenceManager.getDefaultSharedPreferences(var1);
   }

   public static boolean isFirstTimeLaunch() {
      return pref.getBoolean("IsFirstTimeLaunch", true);
   }

   public static void setFirstTimeLaunch(boolean var0) {
      pref.edit().putBoolean("IsFirstTimeLaunch", var0).apply();
   }
}
