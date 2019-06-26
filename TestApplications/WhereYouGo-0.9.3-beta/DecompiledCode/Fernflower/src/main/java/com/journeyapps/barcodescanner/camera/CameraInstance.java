package com.journeyapps.barcodescanner.camera;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import com.google.zxing.client.android.R;
import com.journeyapps.barcodescanner.Size;
import com.journeyapps.barcodescanner.Util;

public class CameraInstance {
   private static final String TAG = CameraInstance.class.getSimpleName();
   private boolean cameraClosed = true;
   private CameraManager cameraManager;
   private CameraSettings cameraSettings = new CameraSettings();
   private CameraThread cameraThread;
   private Runnable closer = new Runnable() {
      public void run() {
         try {
            Log.d(CameraInstance.TAG, "Closing camera");
            CameraInstance.this.cameraManager.stopPreview();
            CameraInstance.this.cameraManager.close();
         } catch (Exception var2) {
            Log.e(CameraInstance.TAG, "Failed to close camera", var2);
         }

         CameraInstance.this.cameraClosed = true;
         CameraInstance.this.readyHandler.sendEmptyMessage(R.id.zxing_camera_closed);
         CameraInstance.this.cameraThread.decrementInstances();
      }
   };
   private Runnable configure = new Runnable() {
      public void run() {
         try {
            Log.d(CameraInstance.TAG, "Configuring camera");
            CameraInstance.this.cameraManager.configure();
            if (CameraInstance.this.readyHandler != null) {
               CameraInstance.this.readyHandler.obtainMessage(R.id.zxing_prewiew_size_ready, CameraInstance.this.getPreviewSize()).sendToTarget();
            }
         } catch (Exception var2) {
            CameraInstance.this.notifyError(var2);
            Log.e(CameraInstance.TAG, "Failed to configure camera", var2);
         }

      }
   };
   private DisplayConfiguration displayConfiguration;
   private boolean open = false;
   private Runnable opener = new Runnable() {
      public void run() {
         try {
            Log.d(CameraInstance.TAG, "Opening camera");
            CameraInstance.this.cameraManager.open();
         } catch (Exception var2) {
            CameraInstance.this.notifyError(var2);
            Log.e(CameraInstance.TAG, "Failed to open camera", var2);
         }

      }
   };
   private Runnable previewStarter = new Runnable() {
      public void run() {
         try {
            Log.d(CameraInstance.TAG, "Starting preview");
            CameraInstance.this.cameraManager.setPreviewDisplay(CameraInstance.this.surface);
            CameraInstance.this.cameraManager.startPreview();
         } catch (Exception var2) {
            CameraInstance.this.notifyError(var2);
            Log.e(CameraInstance.TAG, "Failed to start preview", var2);
         }

      }
   };
   private Handler readyHandler;
   private CameraSurface surface;

   public CameraInstance(Context var1) {
      Util.validateMainThread();
      this.cameraThread = CameraThread.getInstance();
      this.cameraManager = new CameraManager(var1);
      this.cameraManager.setCameraSettings(this.cameraSettings);
   }

   public CameraInstance(CameraManager var1) {
      Util.validateMainThread();
      this.cameraManager = var1;
   }

   private Size getPreviewSize() {
      return this.cameraManager.getPreviewSize();
   }

   private void notifyError(Exception var1) {
      if (this.readyHandler != null) {
         this.readyHandler.obtainMessage(R.id.zxing_camera_error, var1).sendToTarget();
      }

   }

   private void validateOpen() {
      if (!this.open) {
         throw new IllegalStateException("CameraInstance is not open");
      }
   }

   public void close() {
      Util.validateMainThread();
      if (this.open) {
         this.cameraThread.enqueue(this.closer);
      } else {
         this.cameraClosed = true;
      }

      this.open = false;
   }

   public void configureCamera() {
      Util.validateMainThread();
      this.validateOpen();
      this.cameraThread.enqueue(this.configure);
   }

   protected CameraManager getCameraManager() {
      return this.cameraManager;
   }

   public int getCameraRotation() {
      return this.cameraManager.getCameraRotation();
   }

   public CameraSettings getCameraSettings() {
      return this.cameraSettings;
   }

   protected CameraThread getCameraThread() {
      return this.cameraThread;
   }

   public DisplayConfiguration getDisplayConfiguration() {
      return this.displayConfiguration;
   }

   protected CameraSurface getSurface() {
      return this.surface;
   }

   public boolean isCameraClosed() {
      return this.cameraClosed;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void open() {
      Util.validateMainThread();
      this.open = true;
      this.cameraClosed = false;
      this.cameraThread.incrementAndEnqueue(this.opener);
   }

   public void requestPreview(final PreviewCallback var1) {
      this.validateOpen();
      this.cameraThread.enqueue(new Runnable() {
         public void run() {
            CameraInstance.this.cameraManager.requestPreviewFrame(var1);
         }
      });
   }

   public void setCameraSettings(CameraSettings var1) {
      if (!this.open) {
         this.cameraSettings = var1;
         this.cameraManager.setCameraSettings(var1);
      }

   }

   public void setDisplayConfiguration(DisplayConfiguration var1) {
      this.displayConfiguration = var1;
      this.cameraManager.setDisplayConfiguration(var1);
   }

   public void setReadyHandler(Handler var1) {
      this.readyHandler = var1;
   }

   public void setSurface(CameraSurface var1) {
      this.surface = var1;
   }

   public void setSurfaceHolder(SurfaceHolder var1) {
      this.setSurface(new CameraSurface(var1));
   }

   public void setTorch(final boolean var1) {
      Util.validateMainThread();
      if (this.open) {
         this.cameraThread.enqueue(new Runnable() {
            public void run() {
               CameraInstance.this.cameraManager.setTorch(var1);
            }
         });
      }

   }

   public void startPreview() {
      Util.validateMainThread();
      this.validateOpen();
      this.cameraThread.enqueue(this.previewStarter);
   }
}
