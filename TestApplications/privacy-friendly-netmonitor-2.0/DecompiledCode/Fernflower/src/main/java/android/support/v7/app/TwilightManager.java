package android.support.v7.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import java.util.Calendar;

class TwilightManager {
   private static final int SUNRISE = 6;
   private static final int SUNSET = 22;
   private static final String TAG = "TwilightManager";
   private static TwilightManager sInstance;
   private final Context mContext;
   private final LocationManager mLocationManager;
   private final TwilightManager.TwilightState mTwilightState = new TwilightManager.TwilightState();

   @VisibleForTesting
   TwilightManager(@NonNull Context var1, @NonNull LocationManager var2) {
      this.mContext = var1;
      this.mLocationManager = var2;
   }

   static TwilightManager getInstance(@NonNull Context var0) {
      if (sInstance == null) {
         var0 = var0.getApplicationContext();
         sInstance = new TwilightManager(var0, (LocationManager)var0.getSystemService("location"));
      }

      return sInstance;
   }

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
      if (this.mLocationManager != null) {
         try {
            if (this.mLocationManager.isProviderEnabled(var1)) {
               Location var3 = this.mLocationManager.getLastKnownLocation(var1);
               return var3;
            }
         } catch (Exception var2) {
            Log.d("TwilightManager", "Failed to get last known location", var2);
         }
      }

      return null;
   }

   private boolean isStateValid() {
      boolean var1;
      if (this.mTwilightState != null && this.mTwilightState.nextUpdate > System.currentTimeMillis()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @VisibleForTesting
   static void setInstance(TwilightManager var0) {
      sInstance = var0;
   }

   private void updateState(@NonNull Location var1) {
      TwilightManager.TwilightState var2 = this.mTwilightState;
      long var3 = System.currentTimeMillis();
      TwilightCalculator var5 = TwilightCalculator.getInstance();
      var5.calculateTwilight(var3 - 86400000L, var1.getLatitude(), var1.getLongitude());
      long var6 = var5.sunset;
      var5.calculateTwilight(var3, var1.getLatitude(), var1.getLongitude());
      int var8 = var5.state;
      boolean var9 = true;
      if (var8 != 1) {
         var9 = false;
      }

      long var10 = var5.sunrise;
      long var12 = var5.sunset;
      var5.calculateTwilight(var3 + 86400000L, var1.getLatitude(), var1.getLongitude());
      long var14 = var5.sunrise;
      if (var10 != -1L && var12 != -1L) {
         if (var3 > var12) {
            var3 = 0L + var14;
         } else if (var3 > var10) {
            var3 = 0L + var12;
         } else {
            var3 = 0L + var10;
         }

         var3 += 60000L;
      } else {
         var3 += 43200000L;
      }

      var2.isNight = var9;
      var2.yesterdaySunset = var6;
      var2.todaySunrise = var10;
      var2.todaySunset = var12;
      var2.tomorrowSunrise = var14;
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
