package menion.android.whereyougo.preferences;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.utils.A;
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
   private static WakeLock wl;

   public static void disableWakeLock() {
      Logger.w("PreferenceValues", "disableWakeLock(), wl:" + wl);
      if (wl != null) {
         wl.release();
         wl = null;
      }

   }

   public static void enableWakeLock() {
      boolean var0 = false;

      Exception var10000;
      label69: {
         boolean var10001;
         boolean var1;
         label63: {
            label70: {
               try {
                  if (Preferences.APPEARANCE_HIGHLIGHT == 0) {
                     break label70;
                  }
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label69;
               }

               var1 = var0;

               try {
                  if (Preferences.APPEARANCE_HIGHLIGHT != 1) {
                     break label63;
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label69;
               }

               var1 = var0;

               try {
                  if (LocationState.isActuallyHardwareGpsOn()) {
                     break label63;
                  }
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label69;
               }

               var1 = true;
               break label63;
            }

            var1 = true;
         }

         try {
            StringBuilder var2 = new StringBuilder();
            Logger.w("PreferenceValues", var2.append("enableWakeLock(), dis:").append(var1).append(", wl:").append(wl).toString());
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label69;
         }

         if (var1) {
            try {
               if (wl != null) {
                  disableWakeLock();
                  return;
               }
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label69;
            }
         }

         if (var1) {
            return;
         }

         try {
            if (wl == null) {
               wl = ((PowerManager)A.getApp().getSystemService("power")).newWakeLock(10, "PreferenceValues");
               wl.acquire();
            }

            return;
         } catch (Exception var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      Exception var9 = var10000;
      Logger.e("PreferenceValues", "enableWakeLock(), e:" + var9.toString());
   }

   public static boolean existCurrentActivity() {
      boolean var0;
      if (currentActivity != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static int getApplicationVersionActual() {
      byte var0 = 0;

      int var1;
      try {
         var1 = A.getApp().getPackageManager().getPackageInfo(A.getApp().getPackageName(), 0).versionCode;
      } catch (NameNotFoundException var3) {
         Logger.e("PreferenceValues", "getApplicationVersionActual()", (Exception)var3);
         var1 = var0;
      }

      return var1;
   }

   public static int getApplicationVersionLast() {
      int var0;
      try {
         var0 = Preferences.getNumericalPreference(2131165568);
      } catch (ClassCastException var2) {
         Logger.e("PreferenceValues", "getNumericalPreference( R.string.pref_KEY_S_APPLICATION_VERSION_LAST ) return 0", (Exception)var2);
         var0 = 0;
      }

      return var0;
   }

   public static Activity getCurrentActivity() {
      Object var0;
      if (currentActivity == null) {
         var0 = A.getMain();
      } else {
         var0 = currentActivity;
      }

      return (Activity)var0;
   }

   public static String getLanguageCode() {
      String var1;
      label15: {
         String var0 = Preferences.getStringPreference(2131165580);
         if (!"".equals(var0)) {
            var1 = var0;
            if (!"default".equals(var0)) {
               break label15;
            }
         }

         var1 = java.util.Locale.getDefault().getLanguage();
      }

      Logger.w("PreferenceValues", "getLanguageCode() - " + var1);
      if (!Locale.getString(2131165636).equals(var1)) {
         var1 = Locale.getString(2131165645);
      }

      return var1;
   }

   public static Location getLastKnownLocation() {
      Location var0 = new Location("PreferenceValues");
      var0.setLatitude(Preferences.getDecimalPreference(2131165565));
      var0.setLongitude(Preferences.getDecimalPreference(2131165566));
      var0.setAltitude(Preferences.getDecimalPreference(2131165564));
      return var0;
   }

   public static void setApplicationVersionLast(int var0) {
      Preferences.setStringPreference(2131165568, var0);
   }

   public static void setCurrentActivity(Activity var0) {
      if (currentActivity == null && var0 != null) {
         MainApplication.appRestored();
      }

      currentActivity = var0;
   }

   public static void setLastKnownLocation() {
      try {
         Preferences.setStringPreference(2131165565, LocationState.getLocation().getLatitude());
         Preferences.setStringPreference(2131165566, LocationState.getLocation().getLongitude());
         Preferences.setStringPreference(2131165564, LocationState.getLocation().getAltitude());
      } catch (Exception var1) {
         Logger.e("PreferenceValues", "setLastKnownLocation()", var1);
      }

   }
}
