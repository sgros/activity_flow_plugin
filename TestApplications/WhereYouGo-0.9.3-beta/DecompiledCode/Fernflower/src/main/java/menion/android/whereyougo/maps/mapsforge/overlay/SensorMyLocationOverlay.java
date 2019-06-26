package menion.android.whereyougo.maps.mapsforge.overlay;

import android.content.Context;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;
import org.mapsforge.android.maps.MapView;

public class SensorMyLocationOverlay extends MyLocationOverlay implements SensorEventListener {
   private static final float UPDATE_AZIMUTH = 5.0F;
   private static final int UPDATE_INTERVAL = 100;
   float currentCompassAzimuth;
   float filter = 0.15F;
   float lastCompassAzimuth;
   long lastCompassTimestamp;
   float lastGPSAzimuth;
   long lastGPSTimestamp;
   final MapView mapView;
   final RotationMarker marker;
   final SensorManager sensorManager;
   final WindowManager windowManager;

   public SensorMyLocationOverlay(Context var1, MapView var2, RotationMarker var3) {
      super(var1, var2, var3);
      this.windowManager = (WindowManager)var1.getSystemService("window");
      this.sensorManager = (SensorManager)var1.getSystemService("sensor");
      this.marker = var3;
      this.mapView = var2;
   }

   public SensorMyLocationOverlay(Context var1, MapView var2, RotationMarker var3, Paint var4, Paint var5) {
      super(var1, var2, var3, var4, var5);
      this.windowManager = (WindowManager)var1.getSystemService("window");
      this.sensorManager = (SensorManager)var1.getSystemService("sensor");
      this.marker = var3;
      this.mapView = var2;
   }

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

      return this.filter * var1 + (1.0F - this.filter) * var3;
   }

   private void setSensor(boolean var1) {
      if (var1) {
         this.setSensor(false);
         this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(3), 2);
      } else {
         this.sensorManager.unregisterListener(this);
      }

   }

   public void disableMyLocation() {
      synchronized(this){}

      try {
         if (this.isMyLocationEnabled()) {
            this.setSensor(false);
         }

         super.disableMyLocation();
      } finally {
         ;
      }

   }

   public boolean enableMyLocation(boolean var1) {
      boolean var2 = true;
      synchronized(this){}
      boolean var5 = false;

      label50: {
         try {
            var5 = true;
            if (super.enableMyLocation(var1)) {
               this.setSensor(true);
               var5 = false;
               break label50;
            }

            var5 = false;
         } finally {
            if (var5) {
               ;
            }
         }

         var1 = false;
         return var1;
      }

      var1 = var2;
      return var1;
   }

   protected int getRotationOffset() {
      short var1;
      switch(this.windowManager.getDefaultDisplay().getRotation()) {
      case 1:
         var1 = 90;
         break;
      case 2:
         var1 = 180;
         break;
      case 3:
         var1 = 270;
         break;
      default:
         var1 = 0;
      }

      return var1;
   }

   public void onAccuracyChanged(Sensor var1, int var2) {
   }

   public void onProviderDisabled(String var1) {
      super.onProviderDisabled(var1);
      if (!super.isMyLocationEnabled()) {
         this.setSensor(false);
      }

   }

   public void onProviderEnabled(String var1) {
      super.onProviderEnabled(var1);
      if (!super.isMyLocationEnabled()) {
         this.setSensor(false);
      }

   }

   public void onSensorChanged(SensorEvent var1) {
      if (var1.sensor.getType() == 3) {
         boolean var2 = false;
         synchronized(this){}

         boolean var6;
         label370: {
            Throwable var10000;
            boolean var10001;
            label371: {
               long var3;
               float var5;
               try {
                  var3 = var1.timestamp / 1000000L;
                  var5 = this.filterValue((var1.values[0] + (float)this.getRotationOffset() + 360.0F) % 360.0F, this.currentCompassAzimuth);
                  this.currentCompassAzimuth = var5;
                  this.marker.setRotation(var5);
               } catch (Throwable var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label371;
               }

               var6 = var2;

               label372: {
                  try {
                     if (Math.abs(var3 - this.lastCompassTimestamp) < 100L) {
                        break label372;
                     }
                  } catch (Throwable var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label371;
                  }

                  var6 = var2;

                  try {
                     if (Math.abs(var5 - this.lastCompassAzimuth) < 5.0F) {
                        break label372;
                     }

                     this.lastCompassTimestamp = var3;
                     this.lastCompassAzimuth = var5;
                  } catch (Throwable var34) {
                     var10000 = var34;
                     var10001 = false;
                     break label371;
                  }

                  var6 = true;
               }

               label347:
               try {
                  break label370;
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label347;
               }
            }

            while(true) {
               Throwable var37 = var10000;

               try {
                  throw var37;
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (var6) {
            this.mapView.getOverlayController().redrawOverlays();
         }
      }

   }
}
