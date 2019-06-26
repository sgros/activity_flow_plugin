package com.journeyapps.barcodescanner.camera;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.SurfaceHolder;
import com.google.zxing.client.android.AmbientLightManager;
import com.google.zxing.client.android.camera.CameraConfigurationUtils;
import com.google.zxing.client.android.camera.open.OpenCameraInterface;
import com.journeyapps.barcodescanner.Size;
import com.journeyapps.barcodescanner.SourceData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CameraManager {
   private static final String TAG = CameraManager.class.getSimpleName();
   private AmbientLightManager ambientLightManager;
   private AutoFocusManager autoFocusManager;
   private Camera camera;
   private CameraInfo cameraInfo;
   private final CameraManager.CameraPreviewCallback cameraPreviewCallback;
   private Context context;
   private String defaultParameters;
   private DisplayConfiguration displayConfiguration;
   private Size previewSize;
   private boolean previewing;
   private Size requestedPreviewSize;
   private int rotationDegrees = -1;
   private CameraSettings settings = new CameraSettings();

   public CameraManager(Context var1) {
      this.context = var1;
      this.cameraPreviewCallback = new CameraManager.CameraPreviewCallback();
   }

   private int calculateDisplayRotation() {
      int var1 = this.displayConfiguration.getRotation();
      short var2 = 0;
      switch(var1) {
      case 0:
         var2 = 0;
         break;
      case 1:
         var2 = 90;
         break;
      case 2:
         var2 = 180;
         break;
      case 3:
         var2 = 270;
      }

      int var3;
      if (this.cameraInfo.facing == 1) {
         var3 = (360 - (this.cameraInfo.orientation + var2) % 360) % 360;
      } else {
         var3 = (this.cameraInfo.orientation - var2 + 360) % 360;
      }

      Log.i(TAG, "Camera Display Orientation: " + var3);
      return var3;
   }

   private Parameters getDefaultCameraParameters() {
      Parameters var1 = this.camera.getParameters();
      if (this.defaultParameters == null) {
         this.defaultParameters = var1.flatten();
      } else {
         var1.unflatten(this.defaultParameters);
      }

      return var1;
   }

   private static List getPreviewSizes(Parameters var0) {
      List var1 = var0.getSupportedPreviewSizes();
      ArrayList var2 = new ArrayList();
      android.hardware.Camera.Size var3;
      if (var1 == null) {
         var3 = var0.getPreviewSize();
         if (var3 != null) {
            var2.add(new Size(var3.width, var3.height));
         }
      } else {
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            var3 = (android.hardware.Camera.Size)var4.next();
            var2.add(new Size(var3.width, var3.height));
         }
      }

      return var2;
   }

   private void setCameraDisplayOrientation(int var1) {
      this.camera.setDisplayOrientation(var1);
   }

   private void setDesiredParameters(boolean var1) {
      Parameters var2 = this.getDefaultCameraParameters();
      if (var2 == null) {
         Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
      } else {
         Log.i(TAG, "Initial camera parameters: " + var2.flatten());
         if (var1) {
            Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
         }

         CameraConfigurationUtils.setFocus(var2, this.settings.getFocusMode(), var1);
         if (!var1) {
            CameraConfigurationUtils.setTorch(var2, false);
            if (this.settings.isScanInverted()) {
               CameraConfigurationUtils.setInvertColor(var2);
            }

            if (this.settings.isBarcodeSceneModeEnabled()) {
               CameraConfigurationUtils.setBarcodeSceneMode(var2);
            }

            if (this.settings.isMeteringEnabled() && VERSION.SDK_INT >= 15) {
               CameraConfigurationUtils.setVideoStabilization(var2);
               CameraConfigurationUtils.setFocusArea(var2);
               CameraConfigurationUtils.setMetering(var2);
            }
         }

         List var3 = getPreviewSizes(var2);
         if (var3.size() == 0) {
            this.requestedPreviewSize = null;
         } else {
            this.requestedPreviewSize = this.displayConfiguration.getBestPreviewSize(var3, this.isCameraRotated());
            var2.setPreviewSize(this.requestedPreviewSize.width, this.requestedPreviewSize.height);
         }

         if (Build.DEVICE.equals("glass-1")) {
            CameraConfigurationUtils.setBestPreviewFPS(var2);
         }

         Log.i(TAG, "Final camera parameters: " + var2.flatten());
         this.camera.setParameters(var2);
      }

   }

   private void setParameters() {
      try {
         this.rotationDegrees = this.calculateDisplayRotation();
         this.setCameraDisplayOrientation(this.rotationDegrees);
      } catch (Exception var4) {
         Log.w(TAG, "Failed to set rotation.");
      }

      try {
         this.setDesiredParameters(false);
      } catch (Exception var3) {
         try {
            this.setDesiredParameters(true);
         } catch (Exception var2) {
            Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
         }
      }

      android.hardware.Camera.Size var1 = this.camera.getParameters().getPreviewSize();
      if (var1 == null) {
         this.previewSize = this.requestedPreviewSize;
      } else {
         this.previewSize = new Size(var1.width, var1.height);
      }

      this.cameraPreviewCallback.setResolution(this.previewSize);
   }

   public void close() {
      if (this.camera != null) {
         this.camera.release();
         this.camera = null;
      }

   }

   public void configure() {
      if (this.camera == null) {
         throw new RuntimeException("Camera not open");
      } else {
         this.setParameters();
      }
   }

   public Camera getCamera() {
      return this.camera;
   }

   public int getCameraRotation() {
      return this.rotationDegrees;
   }

   public CameraSettings getCameraSettings() {
      return this.settings;
   }

   public DisplayConfiguration getDisplayConfiguration() {
      return this.displayConfiguration;
   }

   public Size getNaturalPreviewSize() {
      return this.previewSize;
   }

   public Size getPreviewSize() {
      Size var1;
      if (this.previewSize == null) {
         var1 = null;
      } else if (this.isCameraRotated()) {
         var1 = this.previewSize.rotate();
      } else {
         var1 = this.previewSize;
      }

      return var1;
   }

   public boolean isCameraRotated() {
      if (this.rotationDegrees == -1) {
         throw new IllegalStateException("Rotation not calculated yet. Call configure() first.");
      } else {
         boolean var1;
         if (this.rotationDegrees % 180 != 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public boolean isOpen() {
      boolean var1;
      if (this.camera != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isTorchOn() {
      boolean var1 = false;
      Parameters var2 = this.camera.getParameters();
      boolean var3 = var1;
      if (var2 != null) {
         String var4 = var2.getFlashMode();
         var3 = var1;
         if (var4 != null) {
            if (!"on".equals(var4)) {
               var3 = var1;
               if (!"torch".equals(var4)) {
                  return var3;
               }
            }

            var3 = true;
         }
      }

      return var3;
   }

   public void open() {
      this.camera = OpenCameraInterface.open(this.settings.getRequestedCameraId());
      if (this.camera == null) {
         throw new RuntimeException("Failed to open camera");
      } else {
         int var1 = OpenCameraInterface.getCameraId(this.settings.getRequestedCameraId());
         this.cameraInfo = new CameraInfo();
         Camera.getCameraInfo(var1, this.cameraInfo);
      }
   }

   public void requestPreviewFrame(PreviewCallback var1) {
      Camera var2 = this.camera;
      if (var2 != null && this.previewing) {
         this.cameraPreviewCallback.setCallback(var1);
         var2.setOneShotPreviewCallback(this.cameraPreviewCallback);
      }

   }

   public void setCameraSettings(CameraSettings var1) {
      this.settings = var1;
   }

   public void setDisplayConfiguration(DisplayConfiguration var1) {
      this.displayConfiguration = var1;
   }

   public void setPreviewDisplay(SurfaceHolder var1) throws IOException {
      this.setPreviewDisplay(new CameraSurface(var1));
   }

   public void setPreviewDisplay(CameraSurface var1) throws IOException {
      var1.setPreview(this.camera);
   }

   public void setTorch(boolean var1) {
      if (this.camera != null) {
         try {
            if (var1 != this.isTorchOn()) {
               if (this.autoFocusManager != null) {
                  this.autoFocusManager.stop();
               }

               Parameters var2 = this.camera.getParameters();
               CameraConfigurationUtils.setTorch(var2, var1);
               if (this.settings.isExposureEnabled()) {
                  CameraConfigurationUtils.setBestExposure(var2, var1);
               }

               this.camera.setParameters(var2);
               if (this.autoFocusManager != null) {
                  this.autoFocusManager.start();
               }
            }
         } catch (RuntimeException var3) {
            Log.e(TAG, "Failed to set torch", var3);
         }
      }

   }

   public void startPreview() {
      Camera var1 = this.camera;
      if (var1 != null && !this.previewing) {
         var1.startPreview();
         this.previewing = true;
         this.autoFocusManager = new AutoFocusManager(this.camera, this.settings);
         this.ambientLightManager = new AmbientLightManager(this.context, this, this.settings);
         this.ambientLightManager.start();
      }

   }

   public void stopPreview() {
      if (this.autoFocusManager != null) {
         this.autoFocusManager.stop();
         this.autoFocusManager = null;
      }

      if (this.ambientLightManager != null) {
         this.ambientLightManager.stop();
         this.ambientLightManager = null;
      }

      if (this.camera != null && this.previewing) {
         this.camera.stopPreview();
         this.cameraPreviewCallback.setCallback((PreviewCallback)null);
         this.previewing = false;
      }

   }

   private final class CameraPreviewCallback implements android.hardware.Camera.PreviewCallback {
      private PreviewCallback callback;
      private Size resolution;

      public CameraPreviewCallback() {
      }

      public void onPreviewFrame(byte[] var1, Camera var2) {
         Size var3 = this.resolution;
         PreviewCallback var4 = this.callback;
         if (var3 != null && var4 != null) {
            RuntimeException var10000;
            boolean var10001;
            if (var1 == null) {
               try {
                  NullPointerException var8 = new NullPointerException("No preview data received");
                  throw var8;
               } catch (RuntimeException var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            } else {
               try {
                  int var5 = var2.getParameters().getPreviewFormat();
                  SourceData var10 = new SourceData(var1, var3.width, var3.height, var5, CameraManager.this.getCameraRotation());
                  var4.onPreview(var10);
                  return;
               } catch (RuntimeException var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }

            RuntimeException var9 = var10000;
            Log.e(CameraManager.TAG, "Camera preview failed", var9);
            var4.onPreviewError(var9);
         } else {
            Log.d(CameraManager.TAG, "Got preview callback, but no handler or resolution available");
            if (var4 != null) {
               var4.onPreviewError(new Exception("No resolution available"));
            }
         }

      }

      public void setCallback(PreviewCallback var1) {
         this.callback = var1;
      }

      public void setResolution(Size var1) {
         this.resolution = var1;
      }
   }
}
