package menion.android.whereyougo.preferences;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import java.util.Locale;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Logger;

public class PreferenceValues {
    private static final String TAG = "PreferenceValues";
    public static final int VALUE_FONT_SIZE_DEFAULT = 0;
    public static final int VALUE_FONT_SIZE_LARGE = 3;
    public static final int VALUE_FONT_SIZE_MEDIUM = 2;
    public static final int VALUE_FONT_SIZE_SMALL = 1;
    public static final int VALUE_GUIDING_WAYPOINT_SOUND_BEEP_ON_DISTANCE = 1;
    public static final int VALUE_GUIDING_WAYPOINT_SOUND_CUSTOM_SOUND = 2;
    public static final int VALUE_GUIDING_WAYPOINT_SOUND_INCREASE_CLOSER = 0;
    public static final int VALUE_GUIDING_ZONE_POINT_CENTER = 0;
    public static final int VALUE_GUIDING_ZONE_POINT_NEAREST = 1;
    public static final int VALUE_HIGHLIGHT_ALWAYS = 2;
    public static final int VALUE_HIGHLIGHT_OFF = 0;
    public static final int VALUE_HIGHLIGHT_ONLY_GPS = 1;
    public static final int VALUE_MAP_PROVIDER_LOCUS = 1;
    public static final int VALUE_MAP_PROVIDER_VECTOR = 0;
    public static final int VALUE_SENSORS_ORIENT_FILTER_HEAVY = 3;
    public static final int VALUE_SENSORS_ORIENT_FILTER_LIGHT = 1;
    public static final int VALUE_SENSORS_ORIENT_FILTER_MEDIUM = 2;
    public static final int VALUE_SENSORS_ORIENT_FILTER_NO = 0;
    public static final int VALUE_UNITS_ALTITUDE_FEET = 1;
    public static final int VALUE_UNITS_ALTITUDE_METRES = 0;
    public static final int VALUE_UNITS_ANGLE_DEGREE = 0;
    public static final int VALUE_UNITS_ANGLE_MIL = 1;
    public static final int VALUE_UNITS_COO_LATLON_DEC = 0;
    public static final int VALUE_UNITS_COO_LATLON_MIN = 1;
    public static final int VALUE_UNITS_COO_LATLON_SEC = 2;
    public static final int VALUE_UNITS_LENGTH_IM = 1;
    public static final int VALUE_UNITS_LENGTH_ME = 0;
    public static final int VALUE_UNITS_LENGTH_NA = 2;
    public static final int VALUE_UNITS_SPEED_KMH = 0;
    public static final int VALUE_UNITS_SPEED_KNOTS = 2;
    public static final int VALUE_UNITS_SPEED_MILH = 1;
    private static Activity currentActivity;
    /* renamed from: wl */
    private static WakeLock f59wl;

    public static void disableWakeLock() {
        Logger.m26w(TAG, "disableWakeLock(), wl:" + f59wl);
        if (f59wl != null) {
            f59wl.release();
            f59wl = null;
        }
    }

    public static void enableWakeLock() {
        boolean disable = false;
        try {
            if (Preferences.APPEARANCE_HIGHLIGHT == 0) {
                disable = true;
            } else if (Preferences.APPEARANCE_HIGHLIGHT == 1 && !LocationState.isActuallyHardwareGpsOn()) {
                disable = true;
            }
            Logger.m26w(TAG, "enableWakeLock(), dis:" + disable + ", wl:" + f59wl);
            if (disable && f59wl != null) {
                disableWakeLock();
            } else if (!disable && f59wl == null) {
                f59wl = ((PowerManager) C0322A.getApp().getSystemService("power")).newWakeLock(10, TAG);
                f59wl.acquire();
            }
        } catch (Exception e) {
            Logger.m21e(TAG, "enableWakeLock(), e:" + e.toString());
        }
    }

    public static boolean existCurrentActivity() {
        return currentActivity != null;
    }

    public static int getApplicationVersionActual() {
        int i = 0;
        try {
            return C0322A.getApp().getPackageManager().getPackageInfo(C0322A.getApp().getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            Logger.m22e(TAG, "getApplicationVersionActual()", e);
            return i;
        }
    }

    public static int getApplicationVersionLast() {
        try {
            return Preferences.getNumericalPreference(C0254R.string.pref_KEY_S_APPLICATION_VERSION_LAST);
        } catch (ClassCastException e) {
            Logger.m22e(TAG, "getNumericalPreference( R.string.pref_KEY_S_APPLICATION_VERSION_LAST ) return 0", e);
            return 0;
        }
    }

    public static void setApplicationVersionLast(int lastVersion) {
        Preferences.setStringPreference(C0254R.string.pref_KEY_S_APPLICATION_VERSION_LAST, Integer.valueOf(lastVersion));
    }

    public static Activity getCurrentActivity() {
        return currentActivity == null ? C0322A.getMain() : currentActivity;
    }

    public static void setCurrentActivity(Activity activity) {
        if (currentActivity == null && activity != null) {
            MainApplication.appRestored();
        }
        currentActivity = activity;
    }

    public static String getLanguageCode() {
        String lang = Preferences.getStringPreference(C0254R.string.pref_KEY_S_LANGUAGE);
        if ("".equals(lang) || "default".equals(lang)) {
            lang = Locale.getDefault().getLanguage();
        }
        Logger.m26w(TAG, "getLanguageCode() - " + lang);
        return Locale.getString(C0254R.string.pref_language_cs_shortcut).equals(lang) ? lang : Locale.getString(C0254R.string.pref_language_en_shortcut);
    }

    public static Location getLastKnownLocation() {
        Location lastKnownLocation = new Location(TAG);
        lastKnownLocation.setLatitude(Preferences.getDecimalPreference(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_LATITUDE));
        lastKnownLocation.setLongitude(Preferences.getDecimalPreference(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_LONGITUDE));
        lastKnownLocation.setAltitude(Preferences.getDecimalPreference(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_ALTITUDE));
        return lastKnownLocation;
    }

    public static void setLastKnownLocation() {
        try {
            Preferences.setStringPreference(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_LATITUDE, Double.valueOf(LocationState.getLocation().getLatitude()));
            Preferences.setStringPreference(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_LONGITUDE, Double.valueOf(LocationState.getLocation().getLongitude()));
            Preferences.setStringPreference(C0254R.string.pref_KEY_F_LAST_KNOWN_LOCATION_ALTITUDE, Double.valueOf(LocationState.getLocation().getAltitude()));
        } catch (Exception e) {
            Logger.m22e(TAG, "setLastKnownLocation()", e);
        }
    }
}
