package menion.android.whereyougo.preferences;

import android.content.Context;
import android.preference.PreferenceManager;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

public class Preferences {
   public static int APPEARANCE_FONT_SIZE;
   public static boolean APPEARANCE_FULLSCREEN;
   public static int APPEARANCE_HIGHLIGHT;
   public static boolean APPEARANCE_IMAGE_STRETCH;
   public static boolean APPEARANCE_STATUSBAR;
   public static int FORMAT_ALTITUDE;
   public static int FORMAT_ANGLE;
   public static int FORMAT_COO_LATLON;
   public static int FORMAT_LENGTH;
   public static int FORMAT_SPEED;
   public static String GC_PASSWORD;
   public static String GC_USERNAME;
   public static boolean GLOBAL_DOUBLE_CLICK;
   public static int GLOBAL_MAP_PROVIDER;
   public static String GLOBAL_ROOT;
   public static boolean GLOBAL_SAVEGAME_AUTO;
   public static int GLOBAL_SAVEGAME_SLOTS;
   public static boolean GPS;
   public static double GPS_ALTITUDE_CORRECTION;
   public static boolean GPS_BEEP_ON_GPS_FIX;
   public static boolean GPS_DISABLE_WHEN_HIDE;
   public static int GPS_MIN_TIME;
   public static boolean GPS_START_AUTOMATICALLY;
   public static boolean GUIDING_GPS_REQUIRED;
   public static boolean GUIDING_SOUNDS;
   public static int GUIDING_WAYPOINT_SOUND;
   public static String GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI;
   public static int GUIDING_WAYPOINT_SOUND_DISTANCE;
   public static int GUIDING_ZONE_NAVIGATION_POINT;
   public static boolean SENSOR_BEARING_TRUE;
   public static boolean SENSOR_HARDWARE_COMPASS;
   public static boolean SENSOR_HARDWARE_COMPASS_AUTO_CHANGE;
   public static int SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE;
   public static int SENSOR_ORIENT_FILTER;
   private static final String TAG = "SettingValues";
   private static Context prefContext;

   public static boolean comparePreferenceKey(String var0, int var1) {
      return var0.equals(prefContext.getString(var1));
   }

   public static boolean getBooleanPreference(int var0) {
      String var1 = prefContext.getString(var0);
      return PreferenceManager.getDefaultSharedPreferences(prefContext).getBoolean(var1, false);
   }

   public static double getDecimalPreference(int var0) {
      String var1 = prefContext.getString(var0);
      return Utils.parseDouble(PreferenceManager.getDefaultSharedPreferences(prefContext).getString(var1, "0.0"));
   }

   public static int getNumericalPreference(int var0) {
      String var1 = prefContext.getString(var0);
      return Utils.parseInt(PreferenceManager.getDefaultSharedPreferences(prefContext).getString(var1, "0"));
   }

   public static String getStringPreference(int var0) {
      String var1 = prefContext.getString(var0);
      return PreferenceManager.getDefaultSharedPreferences(prefContext).getString(var1, "");
   }

   public static void init(Context var0) {
      Logger.d("SettingValues", "init(" + var0 + ")");

      try {
         GLOBAL_ROOT = getStringPreference(2131165582);
         GLOBAL_MAP_PROVIDER = getNumericalPreference(2131165581);
         GLOBAL_SAVEGAME_AUTO = getBooleanPreference(2131165560);
         GLOBAL_SAVEGAME_SLOTS = getNumericalPreference(2131165583);
         GLOBAL_DOUBLE_CLICK = getBooleanPreference(2131165550);
         GC_USERNAME = getStringPreference(2131165571);
         GC_PASSWORD = getStringPreference(2131165570);
         APPEARANCE_STATUSBAR = getBooleanPreference(2131165563);
         APPEARANCE_FULLSCREEN = getBooleanPreference(2131165551);
         APPEARANCE_HIGHLIGHT = getNumericalPreference(2131165579);
         APPEARANCE_IMAGE_STRETCH = getBooleanPreference(2131165559);
         APPEARANCE_FONT_SIZE = getNumericalPreference(2131165569);
         FORMAT_ALTITUDE = getNumericalPreference(2131165585);
         FORMAT_ANGLE = getNumericalPreference(2131165586);
         FORMAT_COO_LATLON = getNumericalPreference(2131165587);
         FORMAT_LENGTH = getNumericalPreference(2131165588);
         FORMAT_SPEED = getNumericalPreference(2131165589);
         GPS = getBooleanPreference(2131165552);
         GPS_START_AUTOMATICALLY = getBooleanPreference(2131165555);
         GPS_MIN_TIME = getNumericalPreference(2131165573);
         GPS_BEEP_ON_GPS_FIX = getBooleanPreference(2131165553);
         GPS_ALTITUDE_CORRECTION = getDecimalPreference(2131165572);
         GPS_DISABLE_WHEN_HIDE = getBooleanPreference(2131165554);
         SENSOR_HARDWARE_COMPASS = getBooleanPreference(2131165562);
         SENSOR_HARDWARE_COMPASS_AUTO_CHANGE = getBooleanPreference(2131165558);
         SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE = getNumericalPreference(2131165578);
         SENSOR_BEARING_TRUE = getBooleanPreference(2131165561);
         SENSOR_ORIENT_FILTER = getNumericalPreference(2131165584);
         GUIDING_GPS_REQUIRED = getBooleanPreference(2131165557);
         GUIDING_SOUNDS = getBooleanPreference(2131165556);
         GUIDING_WAYPOINT_SOUND = getNumericalPreference(2131165574);
         GUIDING_WAYPOINT_SOUND_DISTANCE = getNumericalPreference(2131165576);
         GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI = getStringPreference(2131165575);
         GUIDING_ZONE_NAVIGATION_POINT = getNumericalPreference(2131165577);
      } catch (Exception var1) {
         Logger.e("SettingValues", "init() - ", var1);
      }

   }

   public static void setContext(Context var0) {
      prefContext = var0;
   }

   public static void setPreference(int var0, float var1) {
      String var2 = prefContext.getString(var0);
      PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putFloat(var2, var1).commit();
   }

   public static void setPreference(int var0, int var1) {
      String var2 = prefContext.getString(var0);
      PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putInt(var2, var1).commit();
   }

   public static void setPreference(int var0, String var1) {
      String var2 = prefContext.getString(var0);
      PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putString(var2, var1).commit();
   }

   public static void setPreference(int var0, boolean var1) {
      String var2 = prefContext.getString(var0);
      PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putBoolean(var2, var1).commit();
   }

   public static void setStringPreference(int var0, Object var1) {
      String var2 = prefContext.getString(var0);
      PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putString(var2, String.valueOf(var1)).commit();
   }
}
