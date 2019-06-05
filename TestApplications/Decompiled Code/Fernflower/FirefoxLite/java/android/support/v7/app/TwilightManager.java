package android.support.v7.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import java.util.Calendar;

class TwilightManager {
   private static TwilightManager sInstance;
   private final Context mContext;
   private final LocationManager mLocationManager;
   private final TwilightManager.TwilightState mTwilightState = new TwilightManager.TwilightState();

   TwilightManager(Context var1, LocationManager var2) {
      this.mContext = var1;
      this.mLocationManager = var2;
   }

   static TwilightManager getInstance(Context var0) {
      if (sInstance == null) {
         var0 = var0.getApplicationContext();
         sInstance = new TwilightManager(var0, (LocationManager)var0.getSystemService("location"));
      }

      return sInstance;
   }

   @SuppressLint({"MissingPermission"})
   private Location getLastKnownLocation() {
      int var1 = PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION");
      Location var2 = null;
      Location var3;
      if (var1 == 0) {
         var3 = this.getLastKnownLocationForProvider("network");
      } else {
         var3 = null;
      }

      if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
         var2 = this.getLastKnownLocationForProvider("gps");
      }

      if (var2 != null && var3 != null) {
         Location var4 = var3;
         if (var2.getTime() > var3.getTime()) {
            var4 = var2;
         }

         return var4;
      } else {
         if (var2 != null) {
            var3 = var2;
         }

         return var3;
      }
   }

   private Location getLastKnownLocationForProvider(String var1) {
      try {
         if (this.mLocationManager.isProviderEnabled(var1)) {
            Location var3 = this.mLocationManager.getLastKnownLocation(var1);
            return var3;
         }
      } catch (Exception var2) {
         Log.d("TwilightManager", "Failed to get last known location", var2);
      }

      return null;
   }

   private boolean isStateValid() {
      boolean var1;
      if (this.mTwilightState.nextUpdate > System.currentTimeMillis()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void updateState(Location var1) {
      TwilightManager.TwilightState var2 = this.mTwilightState;
      long var3 = System.currentTimeMillis();
      TwilightCalculator var5 = TwilightCalculator.getInstance();
      var5.calculateTwilight(var3 - 86400000L, var1.getLatitude(), var1.getLongitude());
      long var6 = var5.sunset;
      var5.calculateTwilight(var3, var1.getLatitude(), var1.getLongitude());
      boolean var8;
      if (var5.state == 1) {
         var8 = true;
      } else {
         var8 = false;
      }

      long var9 = var5.sunrise;
      long var11 = var5.sunset;
      var5.calculateTwilight(86400000L + var3, var1.getLatitude(), var1.getLongitude());
      long var13 = var5.sunrise;
      if (var9 != -1L && var11 != -1L) {
         if (var3 > var11) {
            var3 = 0L + var13;
         } else if (var3 > var9) {
            var3 = 0L + var11;
         } else {
            var3 = 0L + var9;
         }

         var3 += 60000L;
      } else {
         var3 += 43200000L;
      }

      var2.isNight = var8;
      var2.yesterdaySunset = var6;
      var2.todaySunrise = var9;
      var2.todaySunset = var11;
      var2.tomorrowSunrise = var13;
      var2.nextUpdate = var3;
   }

   boolean isNight() {
      TwilightManager.TwilightState var1 = this.mTwilightState;
      if (this.isStateValid()) {
         return var1.isNight;
      } else {
         Location var2 = this.getLastKnownLocation();
         if (var2 != null) {
            this.updateState(var2);
            return var1.isNight;
         } else {
            Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
            int var3 = Calendar.getInstance().get(11);
            boolean var4;
            if (var3 >= 6 && var3 < 22) {
               var4 = false;
            } else {
               var4 = true;
            }

            return var4;
         }
      }
   }

   private static class TwilightState {
      boolean isNight;
      long nextUpdate;
      long todaySunrise;
      long todaySunset;
      long tomorrowSunrise;
      long yesterdaySunset;

      TwilightState() {
      }
   }
}
