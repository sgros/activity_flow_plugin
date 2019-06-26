// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.preferences;

import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.geo.location.Location;
import java.util.Locale;
import android.content.pm.PackageManager$NameNotFoundException;
import menion.android.whereyougo.utils.A;
import android.os.PowerManager;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.utils.Logger;
import android.os.PowerManager$WakeLock;
import android.app.Activity;

public class PreferenceValues
{
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
    private static PowerManager$WakeLock wl;
    
    public static void disableWakeLock() {
        Logger.w("PreferenceValues", "disableWakeLock(), wl:" + PreferenceValues.wl);
        if (PreferenceValues.wl != null) {
            PreferenceValues.wl.release();
            PreferenceValues.wl = null;
        }
    }
    
    public static void enableWakeLock() {
        final boolean b = false;
        try {
            boolean b2;
            if (Preferences.APPEARANCE_HIGHLIGHT == 0) {
                b2 = true;
            }
            else {
                b2 = b;
                if (Preferences.APPEARANCE_HIGHLIGHT == 1) {
                    b2 = b;
                    if (!LocationState.isActuallyHardwareGpsOn()) {
                        b2 = true;
                    }
                }
            }
            Logger.w("PreferenceValues", "enableWakeLock(), dis:" + b2 + ", wl:" + PreferenceValues.wl);
            if (b2 && PreferenceValues.wl != null) {
                disableWakeLock();
            }
            else if (!b2 && PreferenceValues.wl == null) {
                (PreferenceValues.wl = ((PowerManager)A.getApp().getSystemService("power")).newWakeLock(10, "PreferenceValues")).acquire();
            }
        }
        catch (Exception ex) {
            Logger.e("PreferenceValues", "enableWakeLock(), e:" + ex.toString());
        }
    }
    
    public static boolean existCurrentActivity() {
        return PreferenceValues.currentActivity != null;
    }
    
    public static int getApplicationVersionActual() {
        final int n = 0;
        try {
            return A.getApp().getPackageManager().getPackageInfo(A.getApp().getPackageName(), 0).versionCode;
        }
        catch (PackageManager$NameNotFoundException ex) {
            Logger.e("PreferenceValues", "getApplicationVersionActual()", (Exception)ex);
            return n;
        }
    }
    
    public static int getApplicationVersionLast() {
        try {
            return Preferences.getNumericalPreference(2131165568);
        }
        catch (ClassCastException ex) {
            Logger.e("PreferenceValues", "getNumericalPreference( R.string.pref_KEY_S_APPLICATION_VERSION_LAST ) return 0", ex);
            return 0;
        }
    }
    
    public static Activity getCurrentActivity() {
        Activity activity;
        if (PreferenceValues.currentActivity == null) {
            activity = A.getMain();
        }
        else {
            activity = PreferenceValues.currentActivity;
        }
        return activity;
    }
    
    public static String getLanguageCode() {
        final String stringPreference = Preferences.getStringPreference(2131165580);
        String s = null;
        Label_0033: {
            if (!"".equals(stringPreference)) {
                s = stringPreference;
                if (!"default".equals(stringPreference)) {
                    break Label_0033;
                }
            }
            s = Locale.getDefault().getLanguage();
        }
        Logger.w("PreferenceValues", "getLanguageCode() - " + s);
        if (!menion.android.whereyougo.preferences.Locale.getString(2131165636).equals(s)) {
            s = menion.android.whereyougo.preferences.Locale.getString(2131165645);
        }
        return s;
    }
    
    public static Location getLastKnownLocation() {
        final Location location = new Location("PreferenceValues");
        location.setLatitude(Preferences.getDecimalPreference(2131165565));
        location.setLongitude(Preferences.getDecimalPreference(2131165566));
        location.setAltitude(Preferences.getDecimalPreference(2131165564));
        return location;
    }
    
    public static void setApplicationVersionLast(final int i) {
        Preferences.setStringPreference(2131165568, i);
    }
    
    public static void setCurrentActivity(final Activity currentActivity) {
        if (PreferenceValues.currentActivity == null && currentActivity != null) {
            MainApplication.appRestored();
        }
        PreferenceValues.currentActivity = currentActivity;
    }
    
    public static void setLastKnownLocation() {
        try {
            Preferences.setStringPreference(2131165565, LocationState.getLocation().getLatitude());
            Preferences.setStringPreference(2131165566, LocationState.getLocation().getLongitude());
            Preferences.setStringPreference(2131165564, LocationState.getLocation().getAltitude());
        }
        catch (Exception ex) {
            Logger.e("PreferenceValues", "setLastKnownLocation()", ex);
        }
    }
}
