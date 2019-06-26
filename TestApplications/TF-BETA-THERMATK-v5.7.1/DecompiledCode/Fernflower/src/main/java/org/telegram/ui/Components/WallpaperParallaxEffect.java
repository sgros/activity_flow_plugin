package org.telegram.ui.Components;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;
import org.telegram.messenger.AndroidUtilities;

public class WallpaperParallaxEffect implements SensorEventListener {
   private Sensor accelerometer;
   private int bufferOffset;
   private WallpaperParallaxEffect.Callback callback;
   private boolean enabled;
   private float[] pitchBuffer = new float[3];
   private float[] rollBuffer = new float[3];
   private SensorManager sensorManager;
   private WindowManager wm;

   public WallpaperParallaxEffect(Context var1) {
      this.wm = (WindowManager)var1.getSystemService("window");
      this.sensorManager = (SensorManager)var1.getSystemService("sensor");
      this.accelerometer = this.sensorManager.getDefaultSensor(1);
   }

   public float getScale(int var1, int var2) {
      int var3 = AndroidUtilities.dp(16.0F);
      float var4 = (float)var1;
      float var5 = (float)(var3 * 2);
      float var6 = (var4 + var5) / var4;
      var4 = (float)var2;
      return Math.max(var6, (var5 + var4) / var4);
   }

   public void onAccuracyChanged(Sensor var1, int var2) {
   }

   public void onSensorChanged(SensorEvent var1) {
      int var2;
      float var3;
      float var4;
      float var5;
      float[] var10;
      label44: {
         var2 = this.wm.getDefaultDisplay().getRotation();
         var10 = var1.values;
         var3 = var10[0] / 9.80665F;
         var4 = var10[1] / 9.80665F;
         var5 = var10[2] / 9.80665F;
         double var6 = (double)var3;
         float var8 = var5 * var5;
         var5 = (float)(Math.atan2(var6, Math.sqrt((double)(var4 * var4 + var8))) / 3.141592653589793D * 2.0D);
         var8 = (float)(Math.atan2((double)var4, Math.sqrt((double)(var3 * var3 + var8))) / 3.141592653589793D * 2.0D);
         if (var2 != 0) {
            var4 = var8;
            var3 = var5;
            if (var2 == 1) {
               break label44;
            }

            if (var2 == 2) {
               var3 = -var8;
               var4 = -var5;
               break label44;
            }

            if (var2 == 3) {
               var3 = -var5;
               var4 = var8;
               break label44;
            }
         }

         var3 = var8;
         var4 = var5;
      }

      var10 = this.rollBuffer;
      var2 = this.bufferOffset;
      var10[var2] = var3;
      this.pitchBuffer[var2] = var4;
      this.bufferOffset = (var2 + 1) % var10.length;
      var2 = 0;
      var4 = 0.0F;
      var3 = 0.0F;

      while(true) {
         var10 = this.rollBuffer;
         if (var2 >= var10.length) {
            label29: {
               var4 /= (float)var10.length;
               var5 = var3 / (float)var10.length;
               if (var4 > 1.0F) {
                  var3 = 2.0F;
               } else {
                  var3 = var4;
                  if (var4 >= -1.0F) {
                     break label29;
                  }

                  var3 = -2.0F;
               }

               var3 -= var4;
            }

            var2 = Math.round(var5 * AndroidUtilities.dpf2(16.0F));
            int var9 = Math.round(var3 * AndroidUtilities.dpf2(16.0F));
            WallpaperParallaxEffect.Callback var11 = this.callback;
            if (var11 != null) {
               var11.onOffsetsChanged(var2, var9);
            }

            return;
         }

         var4 += var10[var2];
         var3 += this.pitchBuffer[var2];
         ++var2;
      }
   }

   public void setCallback(WallpaperParallaxEffect.Callback var1) {
      this.callback = var1;
   }

   public void setEnabled(boolean var1) {
      if (this.enabled != var1) {
         this.enabled = var1;
         Sensor var2 = this.accelerometer;
         if (var2 == null) {
            return;
         }

         if (var1) {
            this.sensorManager.registerListener(this, var2, 1);
         } else {
            this.sensorManager.unregisterListener(this);
         }
      }

   }

   public interface Callback {
      void onOffsetsChanged(int var1, int var2);
   }
}
