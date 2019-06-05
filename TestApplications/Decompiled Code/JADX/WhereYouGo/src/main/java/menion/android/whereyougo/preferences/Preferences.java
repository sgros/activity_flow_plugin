package menion.android.whereyougo.preferences;

import android.content.Context;
import android.preference.PreferenceManager;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;

public class Preferences {
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
    public static String GC_PASSWORD = null;
    public static String GC_USERNAME = null;
    public static boolean GLOBAL_DOUBLE_CLICK = false;
    public static int GLOBAL_MAP_PROVIDER = 0;
    public static String GLOBAL_ROOT = null;
    public static boolean GLOBAL_SAVEGAME_AUTO = false;
    public static int GLOBAL_SAVEGAME_SLOTS = 0;
    public static boolean GPS = false;
    public static double GPS_ALTITUDE_CORRECTION = 0.0d;
    public static boolean GPS_BEEP_ON_GPS_FIX = false;
    public static boolean GPS_DISABLE_WHEN_HIDE = false;
    public static int GPS_MIN_TIME = 0;
    public static boolean GPS_START_AUTOMATICALLY = false;
    public static boolean GUIDING_GPS_REQUIRED = false;
    public static boolean GUIDING_SOUNDS = false;
    public static int GUIDING_WAYPOINT_SOUND = 0;
    public static String GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI = null;
    public static int GUIDING_WAYPOINT_SOUND_DISTANCE = 0;
    public static int GUIDING_ZONE_NAVIGATION_POINT = 0;
    public static boolean SENSOR_BEARING_TRUE = false;
    public static boolean SENSOR_HARDWARE_COMPASS = false;
    public static boolean SENSOR_HARDWARE_COMPASS_AUTO_CHANGE = false;
    public static int SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE = 0;
    public static int SENSOR_ORIENT_FILTER = 0;
    private static final String TAG = "SettingValues";
    private static Context prefContext;

    public static void setContext(Context c) {
        prefContext = c;
    }

    public static boolean comparePreferenceKey(String prefString, int prefId) {
        return prefString.equals(prefContext.getString(prefId));
    }

    public static String getStringPreference(int PreferenceId) {
        return PreferenceManager.getDefaultSharedPreferences(prefContext).getString(prefContext.getString(PreferenceId), "");
    }

    public static double getDecimalPreference(int PreferenceId) {
        return Utils.parseDouble(PreferenceManager.getDefaultSharedPreferences(prefContext).getString(prefContext.getString(PreferenceId), "0.0"));
    }

    public static int getNumericalPreference(int PreferenceId) {
        return Utils.parseInt(PreferenceManager.getDefaultSharedPreferences(prefContext).getString(prefContext.getString(PreferenceId), "0"));
    }

    public static boolean getBooleanPreference(int PreferenceId) {
        return PreferenceManager.getDefaultSharedPreferences(prefContext).getBoolean(prefContext.getString(PreferenceId), false);
    }

    public static void setStringPreference(int PreferenceId, Object value) {
        PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putString(prefContext.getString(PreferenceId), String.valueOf(value)).commit();
    }

    public static void setPreference(int PreferenceId, String value) {
        PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putString(prefContext.getString(PreferenceId), value).commit();
    }

    public static void setPreference(int PreferenceId, int value) {
        PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putInt(prefContext.getString(PreferenceId), value).commit();
    }

    public static void setPreference(int PreferenceId, float value) {
        PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putFloat(prefContext.getString(PreferenceId), value).commit();
    }

    public static void setPreference(int PreferenceId, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(prefContext).edit().putBoolean(prefContext.getString(PreferenceId), value).commit();
    }

    public static void init(Context c) {
        Logger.m20d(TAG, "init(" + c + ")");
        try {
            GLOBAL_ROOT = getStringPreference(C0254R.string.pref_KEY_S_ROOT);
            GLOBAL_MAP_PROVIDER = getNumericalPreference(C0254R.string.pref_KEY_S_MAP_PROVIDER);
            GLOBAL_SAVEGAME_AUTO = getBooleanPreference(C0254R.string.pref_KEY_B_SAVEGAME_AUTO);
            GLOBAL_SAVEGAME_SLOTS = getNumericalPreference(C0254R.string.pref_KEY_S_SAVEGAME_SLOTS);
            GLOBAL_DOUBLE_CLICK = getBooleanPreference(C0254R.string.pref_KEY_B_DOUBLE_CLICK);
            GC_USERNAME = getStringPreference(C0254R.string.pref_KEY_S_GC_USERNAME);
            GC_PASSWORD = getStringPreference(C0254R.string.pref_KEY_S_GC_PASSWORD);
            APPEARANCE_STATUSBAR = getBooleanPreference(C0254R.string.pref_KEY_B_STATUSBAR);
            APPEARANCE_FULLSCREEN = getBooleanPreference(C0254R.string.pref_KEY_B_FULLSCREEN);
            APPEARANCE_HIGHLIGHT = getNumericalPreference(C0254R.string.pref_KEY_S_HIGHLIGHT);
            APPEARANCE_IMAGE_STRETCH = getBooleanPreference(C0254R.string.pref_KEY_B_IMAGE_STRETCH);
            APPEARANCE_FONT_SIZE = getNumericalPreference(C0254R.string.pref_KEY_S_FONT_SIZE);
            FORMAT_ALTITUDE = getNumericalPreference(C0254R.string.pref_KEY_S_UNITS_ALTITUDE);
            FORMAT_ANGLE = getNumericalPreference(C0254R.string.pref_KEY_S_UNITS_ANGLE);
            FORMAT_COO_LATLON = getNumericalPreference(C0254R.string.pref_KEY_S_UNITS_COO_LATLON);
            FORMAT_LENGTH = getNumericalPreference(C0254R.string.pref_KEY_S_UNITS_LENGTH);
            FORMAT_SPEED = getNumericalPreference(C0254R.string.pref_KEY_S_UNITS_SPEED);
            GPS = getBooleanPreference(C0254R.string.pref_KEY_B_GPS);
            GPS_START_AUTOMATICALLY = getBooleanPreference(C0254R.string.pref_KEY_B_GPS_START_AUTOMATICALLY);
            GPS_MIN_TIME = getNumericalPreference(C0254R.string.pref_KEY_S_GPS_MIN_TIME_NOTIFICATION);
            GPS_BEEP_ON_GPS_FIX = getBooleanPreference(C0254R.string.pref_KEY_B_GPS_BEEP_ON_GPS_FIX);
            GPS_ALTITUDE_CORRECTION = getDecimalPreference(C0254R.string.pref_KEY_S_GPS_ALTITUDE_MANUAL_CORRECTION);
            GPS_DISABLE_WHEN_HIDE = getBooleanPreference(C0254R.string.pref_KEY_B_GPS_DISABLE_WHEN_HIDE);
            SENSOR_HARDWARE_COMPASS = getBooleanPreference(C0254R.string.pref_KEY_B_SENSOR_HARDWARE_COMPASS);
            SENSOR_HARDWARE_COMPASS_AUTO_CHANGE = getBooleanPreference(C0254R.string.pref_KEY_B_HARDWARE_COMPASS_AUTO_CHANGE);
            SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE = getNumericalPreference(C0254R.string.pref_KEY_S_HARDWARE_COMPASS_AUTO_CHANGE_VALUE);
            SENSOR_BEARING_TRUE = getBooleanPreference(C0254R.string.pref_KEY_B_SENSORS_BEARING_TRUE);
            SENSOR_ORIENT_FILTER = getNumericalPreference(C0254R.string.pref_KEY_S_SENSORS_ORIENT_FILTER);
            GUIDING_GPS_REQUIRED = getBooleanPreference(C0254R.string.pref_KEY_B_GUIDING_GPS_REQUIRED);
            GUIDING_SOUNDS = getBooleanPreference(C0254R.string.pref_KEY_B_GUIDING_COMPASS_SOUNDS);
            GUIDING_WAYPOINT_SOUND = getNumericalPreference(C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND);
            GUIDING_WAYPOINT_SOUND_DISTANCE = getNumericalPreference(C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND_DISTANCE);
            GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI = getStringPreference(C0254R.string.pref_KEY_S_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND_URI);
            GUIDING_ZONE_NAVIGATION_POINT = getNumericalPreference(C0254R.string.pref_KEY_S_GUIDING_ZONE_POINT);
        } catch (Exception e) {
            Logger.m22e(TAG, "init() - ", e);
        }
    }
}
