package menion.android.whereyougo.geo.location;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.gui.activity.SatelliteActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Logger;

public class LocationState {
   private static final int GPS_OFF = 1;
   private static final int GPS_ON = 0;
   private static final String KEY_B_GPS_ENABLE_ASK_ON_ENABLE = "KEY_B_GPS_ENABLE_ASK_ON_ENABLE";
   public static final String SIMULATE_LOCATION = "SIMULATE_LOCATION";
   private static final String TAG = "LocationState";
   private static GpsConnection gpsConn;
   private static int lastSource;
   private static Location location;
   private static long mLastGpsFixTime = 0L;
   private static ArrayList mListeners;
   private static final Point2D.Int mSatsCount = new Point2D.Int();
   private static int mSource = 1;
   private static boolean speedCorrection = false;

   public static void addLocationChangeListener(ILocationEventListener var0) {
      synchronized(LocationState.class){}
      if (var0 != null) {
         label162: {
            Throwable var10000;
            label160: {
               ArrayList var1;
               boolean var10001;
               try {
                  var1 = mListeners;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label160;
               }

               if (var1 == null) {
                  break label162;
               }

               try {
                  if (mListeners.contains(var0)) {
                     break label162;
                  }

                  mListeners.add(var0);
                  if (mListeners.size() > 0) {
                     ArrayList var14 = mListeners;
                     Comparator var16 = new Comparator() {
                        public int compare(ILocationEventListener var1, ILocationEventListener var2) {
                           return var1.getPriority() - var2.getPriority();
                        }
                     };
                     Collections.sort(var14, var16);
                  }
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label160;
               }

               label148:
               try {
                  onScreenOn(true);
                  break label162;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label148;
               }
            }

            Throwable var15 = var10000;
            throw var15;
         }
      }

   }

   public static void destroy(Context var0) {
      setState(var0, 1, false);
      mListeners.clear();
      gpsConn = null;
      location = null;
   }

   public static long getLastFixTime() {
      return mLastGpsFixTime;
   }

   public static Location getLastKnownLocation(Activity var0) {
      LocationManager var1 = (LocationManager)var0.getSystemService("location");
      Location var2 = null;

      Location var8;
      label44: {
         try {
            var8 = new Location(var1.getLastKnownLocation("gps"));
         } catch (SecurityException var6) {
            Logger.w("LocationState", "Failed to retrieve location: access appears to be disabled.");
            break label44;
         } catch (IllegalArgumentException var7) {
            Logger.w("LocationState", "Failed to retrieve location: device has no GPS provider.");
            break label44;
         }

         var2 = var8;
      }

      Location var3 = null;

      label39: {
         try {
            var8 = new Location(var1.getLastKnownLocation("network"));
         } catch (SecurityException var4) {
            Logger.w("LocationState", "Failed to retrieve location: access appears to be disabled.");
            break label39;
         } catch (IllegalArgumentException var5) {
            Logger.w("LocationState", "Failed to retrieve location: device has no network provider.");
            break label39;
         }

         var3 = var8;
      }

      if (var2 != null && var3 != null) {
         if (var2.getTime() > var3.getTime()) {
            var8 = var2;
         } else {
            var8 = var3;
         }
      } else {
         var8 = var2;
         if (var2 == null) {
            if (var3 != null) {
               var8 = var3;
            } else {
               var8 = null;
            }
         }
      }

      return var8;
   }

   public static Location getLocation() {
      Location var0;
      if (location == null) {
         var0 = new Location("LocationState");
      } else {
         var0 = new Location(location);
      }

      return var0;
   }

   public static Point2D.Int getSatCount() {
      return mSatsCount;
   }

   public static void init(Context var0) {
      if (location == null) {
         location = PreferenceValues.getLastKnownLocation();
         mListeners = new ArrayList();
         lastSource = -1;
      }

   }

   public static boolean isActualLocationHardwareGps() {
      boolean var0;
      if (mSource == 0 && location.getProvider().equals("gps")) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isActualLocationHardwareNetwork() {
      boolean var0;
      if (mSource == 0 && location.getProvider().equals("network")) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isActuallyHardwareGpsOn() {
      boolean var0;
      if (mSource == 0) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isGpsRequired() {
      boolean var0;
      if (mListeners == null) {
         var0 = false;
      } else {
         boolean var1 = false;
         int var2 = 0;

         while(true) {
            var0 = var1;
            if (var2 >= mListeners.size()) {
               break;
            }

            if (((ILocationEventListener)mListeners.get(var2)).isRequired()) {
               var0 = true;
               break;
            }

            ++var2;
         }
      }

      return var0;
   }

   public static void onActivityPauseInstant(Context var0) {
      boolean var1 = true;

      Exception var10000;
      label108: {
         boolean var2;
         boolean var10001;
         label99: {
            label98: {
               try {
                  if (A.getApp() != null && ((MainApplication)A.getApp()).isScreenOff()) {
                     break label98;
                  }
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label108;
               }

               var2 = false;
               break label99;
            }

            var2 = true;
         }

         label89: {
            if (var0 != null) {
               try {
                  if (Preferences.GPS_DISABLE_WHEN_HIDE) {
                     break label89;
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label108;
               }
            }

            var1 = false;
         }

         label83: {
            label82: {
               try {
                  if (!PreferenceValues.existCurrentActivity()) {
                     break label82;
                  }
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label108;
               }

               if (!var2) {
                  break label83;
               }
            }

            try {
               PreferenceValues.disableWakeLock();
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label108;
            }
         }

         if (!var1) {
            return;
         }

         try {
            if (mListeners.size() == 0 && !PreferenceValues.existCurrentActivity()) {
               lastSource = mSource;
               setState(var0, 1, true);
               return;
            }
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label108;
         }

         if (!var2) {
            label107: {
               try {
                  if (!PreferenceValues.existCurrentActivity()) {
                     break label107;
                  }
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label108;
               }

               try {
                  lastSource = -1;
                  return;
               } catch (Exception var3) {
                  var10000 = var3;
                  var10001 = false;
                  break label108;
               }
            }
         }

         try {
            if (!isGpsRequired()) {
               lastSource = mSource;
               setState(var0, 1, true);
               return;
            }
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label108;
         }

         try {
            lastSource = -1;
            return;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      Exception var12 = var10000;
      Logger.e("LocationState", "onActivityPauseInstant()", var12);
   }

   static void onGpsStatusChanged(int var0, GpsStatus var1) {
      if (mListeners != null && mListeners.size() != 0) {
         if (var0 != 1 && var0 != 2) {
            if (var0 == 4) {
               ArrayList var4 = null;
               if (var1 != null) {
                  ArrayList var5 = new ArrayList();
                  Iterator var8 = var1.getSatellites().iterator();
                  mSatsCount.x = 0;
                  mSatsCount.y = 0;

                  while(true) {
                     var4 = var5;
                     if (!var8.hasNext()) {
                        break;
                     }

                     GpsSatellite var6 = (GpsSatellite)var8.next();
                     SatellitePosition var9 = new SatellitePosition();
                     var9.azimuth = var6.getAzimuth();
                     var9.elevation = var6.getElevation();
                     var9.prn = var6.getPrn();
                     var9.snr = (int)var6.getSnr();
                     var9.fixed = var6.usedInFix();
                     Point2D.Int var10;
                     if (var9.fixed) {
                        var10 = mSatsCount;
                        ++var10.x;
                     }

                     var10 = mSatsCount;
                     ++var10.y;
                     var5.add(var9);
                  }
               }

               postGpsSatelliteChange(var4);
            }
         } else {
            for(int var2 = 0; var2 < mListeners.size(); ++var2) {
               ILocationEventListener var7 = (ILocationEventListener)mListeners.get(var2);
               byte var3;
               if (var0 == 1) {
                  var3 = 2;
               } else {
                  var3 = 1;
               }

               var7.onStatusChanged("gps", var3, (Bundle)null);
            }
         }
      }

   }

   protected static void onGpsStatusChanged(Hashtable var0) {
      ArrayList var1 = null;
      if (var0 != null) {
         ArrayList var2 = new ArrayList();
         Enumeration var3 = var0.elements();
         mSatsCount.x = 0;
         mSatsCount.y = 0;

         while(true) {
            var1 = var2;
            if (!var3.hasMoreElements()) {
               break;
            }

            SatellitePosition var4 = (SatellitePosition)var3.nextElement();
            var2.add(var4);
            Point2D.Int var5;
            if (var4.fixed) {
               var5 = mSatsCount;
               ++var5.x;
            }

            var5 = mSatsCount;
            ++var5.y;
         }
      }

      postGpsSatelliteChange(var1);
   }

   static void onLocationChanged(Location var0) {
      if (var0 != null) {
         Exception var10000;
         label98: {
            boolean var10001;
            label99: {
               try {
                  if (location == null) {
                     break label99;
                  }

                  if (location.getProvider().equals("network") && var0.getProvider().equals("gps") && location.getAccuracy() * 3.0F < var0.getAccuracy()) {
                     return;
                  }
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label98;
               }

               label100: {
                  try {
                     if (!speedCorrection && var0.getTime() - location.getTime() < 5000L && var0.getSpeed() > 100.0F && var0.getSpeed() / location.getSpeed() > 2.0F) {
                        var0.setSpeed(location.getSpeed());
                        speedCorrection = true;
                        break label100;
                     }
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label98;
                  }

                  try {
                     speedCorrection = false;
                  } catch (Exception var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label98;
                  }
               }

               try {
                  if (location.getProvider().equals("gps")) {
                     mLastGpsFixTime = System.currentTimeMillis();
                  }
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label98;
               }

               try {
                  if (var0.getSpeed() < 0.5F && (double)Math.abs(var0.getBearing() - location.getBearing()) > 25.0D) {
                     var0.setBearing(location.getBearing());
                  }
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label98;
               }
            }

            try {
               if (var0.getProvider().equals("gps")) {
                  var0.setAltitude(var0.getAltitude() + Preferences.GPS_ALTITUDE_CORRECTION);
               }
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break label98;
            }

            try {
               location = var0;
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label98;
            }

            int var1 = 0;

            while(true) {
               try {
                  if (var1 >= mListeners.size()) {
                     return;
                  }

                  ((ILocationEventListener)mListeners.get(var1)).onLocationChanged(var0);
               } catch (Exception var3) {
                  var10000 = var3;
                  var10001 = false;
                  break;
               }

               ++var1;
            }
         }

         Exception var2 = var10000;
         Logger.e("LocationState", "onLocationChanged(" + var0 + ")", var2);
      }

   }

   static void onProviderDisabled(String var0) {
   }

   static void onProviderEnabled(String var0) {
      Logger.w("LocationState", "onProviderEnabled(" + var0 + ")");
   }

   public static void onScreenOn(boolean var0) {
      if (lastSource != -1 && mListeners != null && mListeners.size() > 0 && (PreferenceValues.existCurrentActivity() || var0)) {
         setState(PreferenceValues.getCurrentActivity(), lastSource, true);
         lastSource = -1;
      }

   }

   static void onStatusChanged(String var0, int var1, Bundle var2) {
      Logger.w("LocationState", "onStatusChanged(" + var0 + ", " + var1 + ", " + var2 + ")");

      for(int var3 = 0; var3 < mListeners.size(); ++var3) {
         ((ILocationEventListener)mListeners.get(var3)).onStatusChanged(var0, var1, var2);
      }

      if (var0.equals("gps") && var1 == 1 && location != null) {
         location.setProvider("network");
         onLocationChanged(location);
      }

   }

   private static void postGpsSatelliteChange(final ArrayList var0) {
      if (PreferenceValues.getCurrentActivity() != null) {
         PreferenceValues.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
               for(int var1 = 0; var1 < LocationState.mListeners.size(); ++var1) {
                  ((ILocationEventListener)LocationState.mListeners.get(var1)).onGpsStatusChanged(4, var0);
               }

            }
         });
      }

   }

   public static void removeLocationChangeListener(ILocationEventListener var0) {
      synchronized(LocationState.class){}

      label87: {
         Throwable var10000;
         label91: {
            int var1;
            boolean var10001;
            try {
               var1 = mListeners.size();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label91;
            }

            if (var1 == 0 || var0 == null) {
               break label87;
            }

            label82:
            try {
               if (mListeners.contains(var0)) {
                  mListeners.remove(var0);
                  StringBuilder var2 = new StringBuilder();
                  Logger.i("LocationState", var2.append("removeLocationChangeListener(").append(var0).append("), actualSize:").append(mListeners.size()).toString());
               }
               break label87;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label82;
            }
         }

         Throwable var9 = var10000;
         throw var9;
      }

   }

   public static void setGpsOff(Context var0) {
      setState(var0, 1, true);
   }

   public static void setGpsOn(Context var0) {
      if (mSource == 0) {
         setGpsOff(var0);
      }

      setState(var0, 0, true);
   }

   public static void setLocationDirectly(Location var0) {
      if (!Const.STATE_RELEASE && !isActuallyHardwareGpsOn()) {
         var0.setSpeed(20.0F);
         if (location != null) {
            var0.setBearing(location.bearingTo(var0));
         }
      }

      onLocationChanged(var0);
   }

   private static void setState(final Context var0, int var1, boolean var2) {
      if (mSource != var1) {
         if (var2 && var0 != null) {
            if (var1 == 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            Preferences.GPS = var2;
            Preferences.setPreference(2131165552, Preferences.GPS);
         }

         if (var1 == 0 && var0 != null) {
            mSource = 0;
            if (gpsConn != null) {
               gpsConn.destroy();
               gpsConn = null;
            }

            boolean var4 = false;
            String var3 = Secure.getString(var0.getContentResolver(), "location_providers_allowed");
            if (var3 != null && (var3.contains("network") || var3.contains("gps"))) {
               gpsConn = new GpsConnection(var0);
            } else {
               UtilsGUI.showDialogQuestion(PreferenceValues.getCurrentActivity(), 2131165210, new OnClickListener() {
                  public void onClick(DialogInterface var1, int var2) {
                     Intent var3 = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                     var0.startActivity(var3);
                  }
               }, (OnClickListener)null);
               var4 = true;
            }

            if (var4) {
               if (var0 instanceof SatelliteActivity) {
                  ((SatelliteActivity)var0).notifyGpsDisable();
               }

               setState(var0, 1, true);
            }
         } else {
            mSource = 1;
            onGpsStatusChanged(2, (GpsStatus)null);
            if (gpsConn != null) {
               gpsConn.destroy();
               gpsConn = null;
            }
         }

         onLocationChanged(location);
      }

   }
}
