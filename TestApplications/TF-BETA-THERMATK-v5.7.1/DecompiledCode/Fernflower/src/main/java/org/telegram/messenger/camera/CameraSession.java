package org.telegram.messenger.camera;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import java.util.ArrayList;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;

public class CameraSession {
   public static final int ORIENTATION_HYSTERESIS = 5;
   private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
      public void onAutoFocus(boolean var1, Camera var2) {
      }
   };
   protected CameraInfo cameraInfo;
   private String currentFlashMode;
   private int currentOrientation;
   private int diffOrientation;
   private boolean flipFront = true;
   private boolean initied;
   private boolean isVideo;
   private int jpegOrientation;
   private int lastDisplayOrientation = -1;
   private int lastOrientation = -1;
   private boolean meteringAreaSupported;
   private OrientationEventListener orientationEventListener;
   private final int pictureFormat;
   private final Size pictureSize;
   private final Size previewSize;
   private boolean sameTakePictureOrientation;

   public CameraSession(CameraInfo var1, Size var2, Size var3, int var4) {
      this.previewSize = var2;
      this.pictureSize = var3;
      this.pictureFormat = var4;
      this.cameraInfo = var1;
      SharedPreferences var6 = ApplicationLoader.applicationContext.getSharedPreferences("camera", 0);
      String var5;
      if (this.cameraInfo.frontCamera != 0) {
         var5 = "flashMode_front";
      } else {
         var5 = "flashMode";
      }

      this.currentFlashMode = var6.getString(var5, "off");
      this.orientationEventListener = new OrientationEventListener(ApplicationLoader.applicationContext) {
         public void onOrientationChanged(int var1) {
            if (CameraSession.this.orientationEventListener != null && CameraSession.this.initied && var1 != -1) {
               CameraSession var2 = CameraSession.this;
               var2.jpegOrientation = var2.roundOrientation(var1, var2.jpegOrientation);
               var1 = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
               if (CameraSession.this.lastOrientation != CameraSession.this.jpegOrientation || var1 != CameraSession.this.lastDisplayOrientation) {
                  if (!CameraSession.this.isVideo) {
                     CameraSession.this.configurePhotoCamera();
                  }

                  CameraSession.this.lastDisplayOrientation = var1;
                  var2 = CameraSession.this;
                  var2.lastOrientation = var2.jpegOrientation;
               }
            }

         }
      };
      if (this.orientationEventListener.canDetectOrientation()) {
         this.orientationEventListener.enable();
      } else {
         this.orientationEventListener.disable();
         this.orientationEventListener = null;
      }

   }

   private int getDisplayOrientation(android.hardware.Camera.CameraInfo var1, boolean var2) {
      int var3 = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
      byte var4 = 0;
      byte var5 = 90;
      short var6 = var4;
      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  var6 = var4;
               } else {
                  var6 = 270;
               }
            } else {
               var6 = 180;
            }
         } else {
            var6 = 90;
         }
      }

      int var8;
      if (var1.facing == 1) {
         int var7 = (360 - (var1.orientation + var6) % 360) % 360;
         var8 = var7;
         if (!var2) {
            var8 = var7;
            if (var7 == 90) {
               var8 = 270;
            }
         }

         if (!var2 && "Huawei".equals(Build.MANUFACTURER) && "angler".equals(Build.PRODUCT) && var8 == 270) {
            var8 = var5;
         }
      } else {
         var8 = (var1.orientation - var6 + 360) % 360;
      }

      return var8;
   }

   private int getHigh() {
      return "LGE".equals(Build.MANUFACTURER) && "g3_tmo_us".equals(Build.PRODUCT) ? 4 : 1;
   }

   private int roundOrientation(int var1, int var2) {
      boolean var3 = true;
      if (var2 != -1) {
         int var4 = Math.abs(var1 - var2);
         if (Math.min(var4, 360 - var4) < 50) {
            var3 = false;
         }
      }

      return var3 ? (var1 + 45) / 90 * 90 % 360 : var2;
   }

   public void checkFlashMode(String var1) {
      if (!CameraController.getInstance().availableFlashModes.contains(this.currentFlashMode)) {
         this.currentFlashMode = var1;
         this.configurePhotoCamera();
         Editor var2 = ApplicationLoader.applicationContext.getSharedPreferences("camera", 0).edit();
         String var3;
         if (this.cameraInfo.frontCamera != 0) {
            var3 = "flashMode_front";
         } else {
            var3 = "flashMode";
         }

         var2.putString(var3, var1).commit();
      }
   }

   protected void configurePhotoCamera() {
      // $FF: Couldn't be decompiled
   }

   protected void configureRecorder(int var1, MediaRecorder var2) {
      android.hardware.Camera.CameraInfo var3 = new android.hardware.Camera.CameraInfo();
      Camera.getCameraInfo(this.cameraInfo.cameraId, var3);
      this.getDisplayOrientation(var3, false);
      int var4 = this.jpegOrientation;
      if (var4 != -1) {
         if (var3.facing == 1) {
            var4 = (var3.orientation - var4 + 360) % 360;
         } else {
            var4 = (var3.orientation + var4) % 360;
         }
      } else {
         var4 = 0;
      }

      var2.setOrientationHint(var4);
      var4 = this.getHigh();
      boolean var5 = CamcorderProfile.hasProfile(this.cameraInfo.cameraId, var4);
      boolean var6 = CamcorderProfile.hasProfile(this.cameraInfo.cameraId, 0);
      if (var5 && (var1 == 1 || !var6)) {
         var2.setProfile(CamcorderProfile.get(this.cameraInfo.cameraId, var4));
      } else {
         if (!var6) {
            throw new IllegalStateException("cannot find valid CamcorderProfile");
         }

         var2.setProfile(CamcorderProfile.get(this.cameraInfo.cameraId, 0));
      }

      this.isVideo = true;
   }

   protected void configureRoundCamera() {
      // $FF: Couldn't be decompiled
   }

   public void destroy() {
      this.initied = false;
      OrientationEventListener var1 = this.orientationEventListener;
      if (var1 != null) {
         var1.disable();
         this.orientationEventListener = null;
      }

   }

   protected void focusToRect(Rect var1, Rect var2) {
      Exception var15;
      Exception var10000;
      label67: {
         Camera var3;
         boolean var10001;
         try {
            var3 = this.cameraInfo.camera;
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label67;
         }

         if (var3 == null) {
            return;
         }

         try {
            var3.cancelAutoFocus();
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label67;
         }

         Parameters var4 = null;

         label55: {
            Parameters var16;
            try {
               var16 = var3.getParameters();
            } catch (Exception var11) {
               Exception var5 = var11;

               try {
                  FileLog.e((Throwable)var5);
                  break label55;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label67;
               }
            }

            var4 = var16;
         }

         if (var4 == null) {
            return;
         }

         try {
            var4.setFocusMode("auto");
            ArrayList var17 = new ArrayList();
            Area var6 = new Area(var1, 1000);
            var17.add(var6);
            var4.setFocusAreas(var17);
            if (this.meteringAreaSupported) {
               ArrayList var14 = new ArrayList();
               Area var18 = new Area(var2, 1000);
               var14.add(var18);
               var4.setMeteringAreas(var14);
            }
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label67;
         }

         try {
            var3.setParameters(var4);
            var3.autoFocus(this.autoFocusCallback);
            return;
         } catch (Exception var8) {
            var15 = var8;

            try {
               FileLog.e((Throwable)var15);
               return;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }
      }

      var15 = var10000;
      FileLog.e((Throwable)var15);
   }

   public String getCurrentFlashMode() {
      return this.currentFlashMode;
   }

   public int getCurrentOrientation() {
      return this.currentOrientation;
   }

   public int getDisplayOrientation() {
      try {
         android.hardware.Camera.CameraInfo var1 = new android.hardware.Camera.CameraInfo();
         Camera.getCameraInfo(this.cameraInfo.getCameraId(), var1);
         int var2 = this.getDisplayOrientation(var1, true);
         return var2;
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
         return 0;
      }
   }

   public String getNextFlashMode() {
      ArrayList var1 = CameraController.getInstance().availableFlashModes;

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         if (((String)var1.get(var2)).equals(this.currentFlashMode)) {
            if (var2 < var1.size() - 1) {
               return (String)var1.get(var2 + 1);
            }

            return (String)var1.get(0);
         }
      }

      return this.currentFlashMode;
   }

   public int getWorldAngle() {
      return this.diffOrientation;
   }

   public boolean isFlipFront() {
      return this.flipFront;
   }

   public boolean isInitied() {
      return this.initied;
   }

   public boolean isSameTakePictureOrientation() {
      return this.sameTakePictureOrientation;
   }

   public void setCurrentFlashMode(String var1) {
      this.currentFlashMode = var1;
      this.configurePhotoCamera();
      Editor var2 = ApplicationLoader.applicationContext.getSharedPreferences("camera", 0).edit();
      String var3;
      if (this.cameraInfo.frontCamera != 0) {
         var3 = "flashMode_front";
      } else {
         var3 = "flashMode";
      }

      var2.putString(var3, var1).commit();
   }

   public void setFlipFront(boolean var1) {
      this.flipFront = var1;
   }

   public void setInitied() {
      this.initied = true;
   }

   public void setOneShotPreviewCallback(PreviewCallback var1) {
      CameraInfo var2 = this.cameraInfo;
      if (var2 != null) {
         Camera var3 = var2.camera;
         if (var3 != null) {
            var3.setOneShotPreviewCallback(var1);
         }
      }

   }

   public void setPreviewCallback(PreviewCallback var1) {
      this.cameraInfo.camera.setPreviewCallback(var1);
   }

   protected void stopVideoRecording() {
      this.isVideo = false;
      this.configurePhotoCamera();
   }
}
