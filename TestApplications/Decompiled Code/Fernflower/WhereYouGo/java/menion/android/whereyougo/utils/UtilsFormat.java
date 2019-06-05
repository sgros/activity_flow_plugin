package menion.android.whereyougo.utils;

import android.location.Location;
import java.text.SimpleDateFormat;
import java.util.Date;
import menion.android.whereyougo.preferences.Preferences;
import org.mapsforge.core.model.GeoPoint;

public class UtilsFormat {
   private static final String TAG = "UtilsFormat";
   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private static final String degreeSign = "°";
   private static Date mDate;
   private static final String minuteSign = "'";
   private static final String secondSign = "''";
   private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

   public static String addZeros(String var0, int var1) {
      String var2;
      if (var0 != null && var0.length() <= var1) {
         int var3 = var0.length();

         while(true) {
            var2 = var0;
            if (var3 >= var1) {
               break;
            }

            var0 = "0" + var0;
            ++var3;
         }
      } else {
         var2 = var0;
      }

      return var2;
   }

   public static String formatAltitude(double var0, boolean var2) {
      return locus.api.android.utils.UtilsFormat.formatAltitude(Preferences.FORMAT_ALTITUDE, var0, var2);
   }

   public static String formatAngle(double var0) {
      return locus.api.android.utils.UtilsFormat.formatAngle(Preferences.FORMAT_ANGLE, (float)(var0 % 360.0D + 360.0D) % 360.0F, false, 0);
   }

   public static String formatCooByType(double var0, double var2, boolean var4) {
      StringBuilder var5 = (new StringBuilder()).append(formatLatitude(var0));
      String var6;
      if (var4) {
         var6 = "<br />";
      } else {
         var6 = " ";
      }

      return var5.append(var6).append(formatLongitude(var2)).toString();
   }

   private static String formatCooLatLon(double param0, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static String formatDate(long var0) {
      if (mDate == null) {
         mDate = new Date();
      }

      mDate.setTime(var0);
      return dateFormat.format(mDate);
   }

   public static String formatDatetime(long var0) {
      if (mDate == null) {
         mDate = new Date();
      }

      mDate.setTime(var0);
      return datetimeFormat.format(mDate);
   }

   public static String formatDistance(double var0, boolean var2) {
      return locus.api.android.utils.UtilsFormat.formatDistance(Preferences.FORMAT_LENGTH, var0, var2);
   }

   public static String formatDouble(double var0, int var2) {
      return locus.api.android.utils.UtilsFormat.formatDouble(var0, var2);
   }

   public static String formatDouble(double var0, int var2, int var3) {
      return locus.api.android.utils.UtilsFormat.formatDouble(var0, var2, var3);
   }

   public static String formatGeoPoint(GeoPoint var0) {
      return formatCooByType(var0.latitude, var0.longitude, false);
   }

   public static String formatGeoPointDefault(GeoPoint var0) {
      return String.format("N %s E %s", Location.convert(var0.latitude, 1).replace(":", "°"), Location.convert(var0.longitude, 1).replace(":", "°"));
   }

   public static String formatLatitude(double var0) {
      StringBuilder var2 = new StringBuilder();
      String var3;
      if (var0 < 0.0D) {
         var3 = "S";
      } else {
         var3 = "N";
      }

      return var2.append(var3).append(" ").append(formatCooLatLon(Math.abs(var0), 2)).toString();
   }

   public static String formatLongitude(double var0) {
      StringBuilder var2 = new StringBuilder();
      String var3;
      if (var0 < 0.0D) {
         var3 = "W";
      } else {
         var3 = "E";
      }

      return var2.append(var3).append(" ").append(formatCooLatLon(Math.abs(var0), 3)).toString();
   }

   public static String formatSpeed(double var0, boolean var2) {
      return locus.api.android.utils.UtilsFormat.formatSpeed(Preferences.FORMAT_SPEED, var0, var2);
   }

   public static String formatTime(long var0) {
      if (mDate == null) {
         mDate = new Date();
      }

      mDate.setTime(var0);
      return timeFormat.format(mDate);
   }

   public static String formatTime(boolean var0, long var1) {
      return formatTime(var0, var1, true);
   }

   public static String formatTime(boolean var0, long var1, boolean var3) {
      long var4 = var1 / 3600000L;
      long var6 = (var1 - 3600000L * var4) / 60000L;
      double var8 = (double)(var1 - 3600000L * var4 - 60000L * var6) / 1000.0D;
      String var10;
      if (var0) {
         if (var3) {
            var10 = var4 + "h:" + formatDouble((double)var6, 0, 2) + "m:" + formatDouble(var8, 0, 2) + "s";
         } else {
            var10 = formatDouble((double)var4, 0, 2) + ":" + formatDouble((double)var6, 0, 2) + ":" + formatDouble(var8, 0, 2);
         }
      } else if (var4 == 0L) {
         if (var6 == 0L) {
            if (var3) {
               var10 = formatDouble(var8, 0) + "s";
            } else {
               var10 = formatDouble(var8, 0, 2);
            }
         } else if (var3) {
            var10 = var6 + "m:" + formatDouble(var8, 0) + "s";
         } else {
            var10 = formatDouble((double)var6, 0, 2) + ":" + formatDouble(var8, 0, 2);
         }
      } else if (var3) {
         var10 = var4 + "h:" + var6 + "m";
      } else {
         var10 = formatDouble((double)var4, 0, 2) + ":" + formatDouble((double)var6, 0, 2) + ":" + formatDouble(var8, 0, 2);
      }

      return var10;
   }
}
