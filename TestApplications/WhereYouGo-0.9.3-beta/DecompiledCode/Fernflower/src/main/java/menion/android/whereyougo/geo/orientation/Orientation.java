package menion.android.whereyougo.geo.orientation;

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;

public class Orientation implements SensorEventListener, ILocationEventListener {
   private static final String TAG = "Orientation";
   private static float aboveOrBelow = 0.0F;
   private static GeomagneticField gmf;
   private static long lastCompute;
   private static float orient;
   private static float pitch;
   private static float roll;
   private final Vector listeners = new Vector();
   private float mLastAziGps;
   private float mLastAziSensor;
   private float mLastPitch;
   private float mLastRoll;
   private SensorManager sensorManager;

   private float filterValue(float var1, float var2) {
      float var3;
      if (var1 < var2 - 180.0F) {
         var3 = var2 - 360.0F;
      } else {
         var3 = var2;
         if (var1 > var2 + 180.0F) {
            var3 = var2 + 360.0F;
         }
      }

      var2 = this.getFilter();
      return (float)((double)(var1 * var2) + (double)var3 * (1.0D - (double)var2));
   }

   public static float getDeclination() {
      long var0 = System.currentTimeMillis();
      if (gmf == null || var0 - lastCompute > 300000L) {
         Location var2 = LocationState.getLocation();
         gmf = new GeomagneticField((float)var2.getLatitude(), (float)var2.getLongitude(), (float)var2.getAltitude(), var0);
         lastCompute = var0;
         Logger.w("Orientation", "getDeclination() - dec:" + gmf.getDeclination());
      }

      return gmf.getDeclination();
   }

   private float getFilter() {
      float var1;
      switch(Preferences.SENSOR_ORIENT_FILTER) {
      case 1:
         var1 = 0.2F;
         break;
      case 2:
         var1 = 0.06F;
         break;
      case 3:
         var1 = 0.03F;
         break;
      default:
         var1 = 1.0F;
      }

      return var1;
   }

   private void sendOrientation(float var1, float var2) {
      float var3;
      if (Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE && LocationState.getLocation().getSpeed() >= (float)Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE) {
         var3 = this.mLastAziGps;
      } else if (!Preferences.SENSOR_HARDWARE_COMPASS) {
         var3 = this.mLastAziGps;
      } else {
         var3 = this.mLastAziSensor;
      }

      this.mLastPitch = var1;
      this.mLastRoll = var2;
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((IOrientationEventListener)var4.next()).onOrientationChanged(var3, this.mLastPitch, this.mLastRoll);
      }

   }

   public void addListener(IOrientationEventListener var1) {
      if (!this.listeners.contains(var1)) {
         this.listeners.add(var1);
         Logger.i("Orientation", "addListener(" + var1 + "), listeners.size():" + this.listeners.size());
         this.manageSensors();
      }

   }

   public String getName() {
      return "Orientation";
   }

   public int getPriority() {
      return 2;
   }

   public boolean isRequired() {
      return false;
   }

   public void manageSensors() {
      if (this.sensorManager != null) {
         this.mLastAziGps = 0.0F;
         this.mLastAziSensor = 0.0F;
         this.sendOrientation(0.0F, 0.0F);
         this.sensorManager.unregisterListener(this);
         this.sensorManager = null;
         LocationState.removeLocationChangeListener(this);
      }

      if (this.listeners.size() > 0) {
         if (this.sensorManager == null) {
            this.sensorManager = (SensorManager)A.getMain().getSystemService("sensor");
            this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(3), 1);
            this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(1), 1);
         }

         if (!Preferences.SENSOR_HARDWARE_COMPASS || Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE) {
            LocationState.addLocationChangeListener(this);
            this.mLastAziGps = 0.0F;
         }
      }

   }

   public void onAccuracyChanged(Sensor var1, int var2) {
   }

   public void onGpsStatusChanged(int var1, ArrayList var2) {
   }

   public void onLocationChanged(Location var1) {
      if (var1.getBearing() != 0.0F) {
         this.mLastAziGps = var1.getBearing();
         this.sendOrientation(this.mLastPitch, this.mLastRoll);
      }

   }

   public void onSensorChanged(SensorEvent var1) {
      float var2;
      switch(var1.sensor.getType()) {
      case 1:
         var2 = this.getFilter();
         aboveOrBelow = (float)((double)(var1.values[2] * var2) + (double)aboveOrBelow * (1.0D - (double)var2));
      case 2:
      default:
         break;
      case 3:
         float var3 = var1.values[0];
         var2 = var3;
         if (Preferences.SENSOR_BEARING_TRUE) {
            var2 = var3 + getDeclination();
         }

         orient = this.filterValue(var2, orient);
         pitch = this.filterValue(var1.values[1], pitch);
         roll = this.filterValue(var1.values[2], roll);
         if (aboveOrBelow < 0.0F) {
            if (roll < 0.0F) {
               var2 = -180.0F - roll;
            } else {
               var2 = 180.0F - roll;
            }
         } else {
            var2 = roll;
         }

         this.mLastAziSensor = orient;
         switch(A.getMain().getWindowManager().getDefaultDisplay().getRotation()) {
         case 0:
         default:
            break;
         case 1:
            this.mLastAziSensor += 90.0F;
            break;
         case 2:
            this.mLastAziSensor -= 180.0F;
            break;
         case 3:
            this.mLastAziSensor -= 90.0F;
         }

         this.sendOrientation(pitch, var2);
      }

   }

   public void onStatusChanged(String var1, int var2, Bundle var3) {
   }

   public void removeAllListeners() {
      this.listeners.clear();
      this.manageSensors();
   }

   public void removeListener(IOrientationEventListener var1) {
      if (this.listeners.contains(var1)) {
         this.listeners.remove(var1);
         Logger.i("Orientation", "removeListener(" + var1 + "), listeners.size():" + this.listeners.size());
         this.manageSensors();
      }

   }
}
