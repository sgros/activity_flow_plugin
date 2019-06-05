package com.google.zxing.client.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import com.journeyapps.barcodescanner.camera.CameraManager;
import com.journeyapps.barcodescanner.camera.CameraSettings;

public final class AmbientLightManager implements SensorEventListener {
   private static final float BRIGHT_ENOUGH_LUX = 450.0F;
   private static final float TOO_DARK_LUX = 45.0F;
   private CameraManager cameraManager;
   private CameraSettings cameraSettings;
   private Context context;
   private Handler handler;
   private Sensor lightSensor;

   public AmbientLightManager(Context var1, CameraManager var2, CameraSettings var3) {
      this.context = var1;
      this.cameraManager = var2;
      this.cameraSettings = var3;
      this.handler = new Handler();
   }

   private void setTorch(final boolean var1) {
      this.handler.post(new Runnable() {
         public void run() {
            AmbientLightManager.this.cameraManager.setTorch(var1);
         }
      });
   }

   public void onAccuracyChanged(Sensor var1, int var2) {
   }

   public void onSensorChanged(SensorEvent var1) {
      float var2 = var1.values[0];
      if (this.cameraManager != null) {
         if (var2 <= 45.0F) {
            this.setTorch(true);
         } else if (var2 >= 450.0F) {
            this.setTorch(false);
         }
      }

   }

   public void start() {
      if (this.cameraSettings.isAutoTorchEnabled()) {
         SensorManager var1 = (SensorManager)this.context.getSystemService("sensor");
         this.lightSensor = var1.getDefaultSensor(5);
         if (this.lightSensor != null) {
            var1.registerListener(this, this.lightSensor, 3);
         }
      }

   }

   public void stop() {
      if (this.lightSensor != null) {
         ((SensorManager)this.context.getSystemService("sensor")).unregisterListener(this);
         this.lightSensor = null;
      }

   }
}
