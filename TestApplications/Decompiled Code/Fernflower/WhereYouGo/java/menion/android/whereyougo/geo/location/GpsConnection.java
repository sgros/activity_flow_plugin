package menion.android.whereyougo.geo.location;

import android.content.Context;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import menion.android.whereyougo.audio.UtilsAudio;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;

public class GpsConnection {
   private static final String TAG = "GpsConnection";
   private final GpsConnection.MyGpsListener gpsListener;
   private boolean gpsProviderEnabled;
   private GpsStatus gpsStatus;
   private boolean isFixed;
   private final GpsConnection.MyLocationListener llGPS;
   private final GpsConnection.MyLocationListener llNetwork;
   private LocationManager locationManager;
   private Timer mGpsTimer;
   private boolean networkProviderEnabled;

   public GpsConnection(Context var1) {
      Logger.w("GpsConnection", "onCreate()");
      this.llGPS = new GpsConnection.MyLocationListener();
      this.llNetwork = new GpsConnection.MyLocationListener();
      this.gpsListener = new GpsConnection.MyGpsListener();
      this.isFixed = false;
      this.locationManager = (LocationManager)var1.getSystemService("location");
      List var2 = this.locationManager.getAllProviders();

      try {
         this.locationManager.removeUpdates(this.llGPS);
      } catch (Exception var8) {
         Logger.w("GpsConnection", "problem removing listeners llGPS, e:" + var8);
      }

      try {
         this.locationManager.removeUpdates(this.llNetwork);
      } catch (Exception var7) {
         Logger.w("GpsConnection", "problem removing listeners llNetwork, e:" + var7);
      }

      if (var2.contains("network")) {
         try {
            this.locationManager.requestLocationUpdates("network", (long)(Preferences.GPS_MIN_TIME * 1000), 0.0F, this.llNetwork);
            this.networkProviderEnabled = true;
         } catch (Exception var6) {
            Logger.w("GpsConnection", "problem adding 'network' provider, e:" + var6);
            this.networkProviderEnabled = false;
         }
      }

      if (var2.contains("gps")) {
         try {
            this.locationManager.requestLocationUpdates("gps", (long)(Preferences.GPS_MIN_TIME * 1000), 0.0F, this.llGPS);
            this.gpsProviderEnabled = true;
         } catch (Exception var5) {
            Logger.w("GpsConnection", "problem adding 'GPS' provider, e:" + var5);
            this.gpsProviderEnabled = false;
         }
      }

      try {
         this.locationManager.addGpsStatusListener(this.gpsListener);
      } catch (Exception var4) {
         Logger.w("GpsConnection", "problem adding 'GPS status' listener, e:" + var4);
      }

      if (!this.networkProviderEnabled && !this.gpsProviderEnabled) {
         if (PreferenceValues.getCurrentActivity() != null) {
            UtilsGUI.showDialogInfo(PreferenceValues.getCurrentActivity(), 2131165226);
         }

         LocationState.setGpsOff(var1);
         this.destroy();
      } else {
         ManagerNotify.toastShortMessage(var1, var1.getString(2131165208));
      }

   }

   private void disableNetwork() {
      if (this.networkProviderEnabled) {
         this.locationManager.removeUpdates(this.llNetwork);
         this.networkProviderEnabled = false;
      }

   }

   private void enableNetwork() {
      if (!this.networkProviderEnabled) {
         try {
            this.locationManager.requestLocationUpdates("network", (long)(Preferences.GPS_MIN_TIME * 1000), 0.0F, this.llNetwork);
            this.networkProviderEnabled = true;
         } catch (Exception var2) {
         }
      }

   }

   private void handleOnLocationChanged(Location var1) {
      synchronized(this){}

      try {
         if (!this.isFixed) {
            if (var1.getProvider().equals("gps")) {
               if (Preferences.GPS_BEEP_ON_GPS_FIX) {
                  UtilsAudio.playBeep(1);
               }

               this.disableNetwork();
               this.isFixed = true;
            }

            LocationState.onLocationChanged(var1);
         } else if (var1.getProvider().equals("gps")) {
            LocationState.onLocationChanged(var1);
            this.setNewTimer();
         }
      } finally {
         ;
      }

   }

   private void setNewTimer() {
      if (this.mGpsTimer != null) {
         this.mGpsTimer.cancel();
      }

      this.mGpsTimer = new Timer();
      this.mGpsTimer.schedule(new TimerTask() {
         public void run() {
            if (Preferences.GPS_BEEP_ON_GPS_FIX) {
               UtilsAudio.playBeep(2);
            }

            GpsConnection.this.mGpsTimer = null;
            GpsConnection.this.isFixed = false;
         }
      }, 60000L);
   }

   public void destroy() {
      if (this.locationManager != null) {
         this.disableNetwork();
         this.locationManager.removeUpdates(this.llGPS);
         this.locationManager.removeGpsStatusListener(this.gpsListener);
         this.locationManager = null;
         ManagerNotify.toastShortMessage(2131165207);
      }

   }

   public boolean isProviderEnabled(String var1) {
      boolean var2;
      if (this.locationManager != null && this.locationManager.isProviderEnabled(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private class MyGpsListener implements Listener {
      private MyGpsListener() {
      }

      // $FF: synthetic method
      MyGpsListener(Object var2) {
         this();
      }

      public void onGpsStatusChanged(int var1) {
         Exception var10000;
         label63: {
            boolean var10001;
            try {
               if (GpsConnection.this.locationManager == null) {
                  return;
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label63;
            }

            if (var1 == 3) {
               return;
            }

            if (var1 == 4) {
               label49: {
                  label64: {
                     try {
                        if (GpsConnection.this.gpsStatus == null) {
                           GpsConnection.this.gpsStatus = GpsConnection.this.locationManager.getGpsStatus((GpsStatus)null);
                           break label64;
                        }
                     } catch (Exception var5) {
                        var10000 = var5;
                        var10001 = false;
                        break label49;
                     }

                     try {
                        GpsConnection.this.gpsStatus = GpsConnection.this.locationManager.getGpsStatus(GpsConnection.this.gpsStatus);
                     } catch (Exception var4) {
                        var10000 = var4;
                        var10001 = false;
                        break label49;
                     }
                  }

                  try {
                     LocationState.onGpsStatusChanged(var1, GpsConnection.this.gpsStatus);
                     return;
                  } catch (Exception var3) {
                     var10000 = var3;
                     var10001 = false;
                  }
               }
            } else if (var1 == 1) {
               try {
                  LocationState.onGpsStatusChanged(var1, (GpsStatus)null);
                  return;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            } else {
               if (var1 != 2) {
                  return;
               }

               try {
                  LocationState.onGpsStatusChanged(var1, (GpsStatus)null);
                  return;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }
         }

         Exception var2 = var10000;
         Logger.e("GpsConnection", "onGpsStatusChanged(" + var1 + ")", var2);
      }
   }

   private class MyLocationListener implements LocationListener {
      public MyLocationListener() {
      }

      public void onLocationChanged(android.location.Location var1) {
         GpsConnection.this.handleOnLocationChanged(new Location(var1));
      }

      public void onProviderDisabled(String var1) {
         LocationState.onProviderDisabled(var1);
         if (GpsConnection.this.locationManager != null && !GpsConnection.this.locationManager.isProviderEnabled("gps") && !GpsConnection.this.locationManager.isProviderEnabled("network")) {
            LocationState.setGpsOff(PreferenceValues.getCurrentActivity());
            GpsConnection.this.destroy();
         } else if (var1.equals("gps")) {
            GpsConnection.this.enableNetwork();
         }

      }

      public void onProviderEnabled(String var1) {
         LocationState.onProviderEnabled(var1);
      }

      public void onStatusChanged(String var1, int var2, Bundle var3) {
         LocationState.onStatusChanged(var1, var2, var3);
      }
   }
}
