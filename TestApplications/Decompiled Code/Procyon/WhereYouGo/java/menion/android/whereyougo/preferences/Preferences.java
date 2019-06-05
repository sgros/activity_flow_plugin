// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.preferences;

import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;
import android.preference.PreferenceManager;
import android.content.Context;

public class Preferences
{
    public static int APPEARANCE_FONT_SIZE = 0;
    public static boolean APPEARANCE_FULLSCREEN = false;
    public static int APPEARANCE_HIGHLIGHT = 0;
    public static boolean APPEARANCE_IMAGE_STRETCH = false;
    public static boolean APPEARANCE_STATUSBAR = false;
    public static int FORMAT_ALTITUDE = 0;
    public static int FORMAT_ANGLE = 0;
    public static int FORMAT_COO_LATLON = 0;
    public static int FORMAT_LENGTH = 0;
    public static int FORMAT_SPEED = 0;
    public static String GC_PASSWORD;
    public static String GC_USERNAME;
    public static boolean GLOBAL_DOUBLE_CLICK = false;
    public static int GLOBAL_MAP_PROVIDER = 0;
    public static String GLOBAL_ROOT;
    public static boolean GLOBAL_SAVEGAME_AUTO = false;
    public static int GLOBAL_SAVEGAME_SLOTS = 0;
    public static boolean GPS = false;
    public static double GPS_ALTITUDE_CORRECTION = 0.0;
    public static boolean GPS_BEEP_ON_GPS_FIX = false;
    public static boolean GPS_DISABLE_WHEN_HIDE = false;
    public static int GPS_MIN_TIME = 0;
    public static boolean GPS_START_AUTOMATICALLY = false;
    public static boolean GUIDING_GPS_REQUIRED = false;
    public static boolean GUIDING_SOUNDS = false;
    public static int GUIDING_WAYPOINT_SOUND = 0;
    public static String GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI;
    public static int GUIDING_WAYPOINT_SOUND_DISTANCE = 0;
    public static int GUIDING_ZONE_NAVIGATION_POINT = 0;
    public static boolean SENSOR_BEARING_TRUE = false;
    public static boolean SENSOR_HARDWARE_COMPASS = false;
    public static boolean SENSOR_HARDWARE_COMPASS_AUTO_CHANGE = false;
    public static int SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE = 0;
    public static int SENSOR_ORIENT_FILTER = 0;
    private static final String TAG = "SettingValues";
    private static Context prefContext;
    
    public static boolean comparePreferenceKey(final String s, final int n) {
        return s.equals(Preferences.prefContext.getString(n));
    }
    
    public static boolean getBooleanPreference(final int n) {
        return PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).getBoolean(Preferences.prefContext.getString(n), false);
    }
    
    public static double getDecimalPreference(final int n) {
        return Utils.parseDouble(PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).getString(Preferences.prefContext.getString(n), "0.0"));
    }
    
    public static int getNumericalPreference(final int n) {
        return Utils.parseInt(PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).getString(Preferences.prefContext.getString(n), "0"));
    }
    
    public static String getStringPreference(final int n) {
        return PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).getString(Preferences.prefContext.getString(n), "");
    }
    
    public static void init(final Context obj) {
        Logger.d("SettingValues", "init(" + obj + ")");
        try {
            Preferences.GLOBAL_ROOT = getStringPreference(2131165582);
            Preferences.GLOBAL_MAP_PROVIDER = getNumericalPreference(2131165581);
            Preferences.GLOBAL_SAVEGAME_AUTO = getBooleanPreference(2131165560);
            Preferences.GLOBAL_SAVEGAME_SLOTS = getNumericalPreference(2131165583);
            Preferences.GLOBAL_DOUBLE_CLICK = getBooleanPreference(2131165550);
            Preferences.GC_USERNAME = getStringPreference(2131165571);
            Preferences.GC_PASSWORD = getStringPreference(2131165570);
            Preferences.APPEARANCE_STATUSBAR = getBooleanPreference(2131165563);
            Preferences.APPEARANCE_FULLSCREEN = getBooleanPreference(2131165551);
            Preferences.APPEARANCE_HIGHLIGHT = getNumericalPreference(2131165579);
            Preferences.APPEARANCE_IMAGE_STRETCH = getBooleanPreference(2131165559);
            Preferences.APPEARANCE_FONT_SIZE = getNumericalPreference(2131165569);
            Preferences.FORMAT_ALTITUDE = getNumericalPreference(2131165585);
            Preferences.FORMAT_ANGLE = getNumericalPreference(2131165586);
            Preferences.FORMAT_COO_LATLON = getNumericalPreference(2131165587);
            Preferences.FORMAT_LENGTH = getNumericalPreference(2131165588);
            Preferences.FORMAT_SPEED = getNumericalPreference(2131165589);
            Preferences.GPS = getBooleanPreference(2131165552);
            Preferences.GPS_START_AUTOMATICALLY = getBooleanPreference(2131165555);
            Preferences.GPS_MIN_TIME = getNumericalPreference(2131165573);
            Preferences.GPS_BEEP_ON_GPS_FIX = getBooleanPreference(2131165553);
            Preferences.GPS_ALTITUDE_CORRECTION = getDecimalPreference(2131165572);
            Preferences.GPS_DISABLE_WHEN_HIDE = getBooleanPreference(2131165554);
            Preferences.SENSOR_HARDWARE_COMPASS = getBooleanPreference(2131165562);
            Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE = getBooleanPreference(2131165558);
            Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE = getNumericalPreference(2131165578);
            Preferences.SENSOR_BEARING_TRUE = getBooleanPreference(2131165561);
            Preferences.SENSOR_ORIENT_FILTER = getNumericalPreference(2131165584);
            Preferences.GUIDING_GPS_REQUIRED = getBooleanPreference(2131165557);
            Preferences.GUIDING_SOUNDS = getBooleanPreference(2131165556);
            Preferences.GUIDING_WAYPOINT_SOUND = getNumericalPreference(2131165574);
            Preferences.GUIDING_WAYPOINT_SOUND_DISTANCE = getNumericalPreference(2131165576);
            Preferences.GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI = getStringPreference(2131165575);
            Preferences.GUIDING_ZONE_NAVIGATION_POINT = getNumericalPreference(2131165577);
        }
        catch (Exception ex) {
            Logger.e("SettingValues", "init() - ", ex);
        }
    }
    
    public static void setContext(final Context prefContext) {
        Preferences.prefContext = prefContext;
    }
    
    public static void setPreference(final int n, final float n2) {
        PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).edit().putFloat(Preferences.prefContext.getString(n), n2).commit();
    }
    
    public static void setPreference(final int n, final int n2) {
        PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).edit().putInt(Preferences.prefContext.getString(n), n2).commit();
    }
    
    public static void setPreference(final int n, final String s) {
        PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).edit().putString(Preferences.prefContext.getString(n), s).commit();
    }
    
    public static void setPreference(final int n, final boolean b) {
        PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).edit().putBoolean(Preferences.prefContext.getString(n), b).commit();
    }
    
    public static void setStringPreference(final int n, final Object obj) {
        PreferenceManager.getDefaultSharedPreferences(Preferences.prefContext).edit().putString(Preferences.prefContext.getString(n), String.valueOf(obj)).commit();
    }
}
