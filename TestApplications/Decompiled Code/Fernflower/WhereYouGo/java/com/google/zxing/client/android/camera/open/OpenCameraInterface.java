package com.google.zxing.client.android.camera.open;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;

public final class OpenCameraInterface {
   public static final int NO_REQUESTED_CAMERA = -1;
   private static final String TAG = OpenCameraInterface.class.getName();

   private OpenCameraInterface() {
   }

   public static int getCameraId(int var0) {
      int var1 = Camera.getNumberOfCameras();
      if (var1 == 0) {
         Log.w(TAG, "No cameras!");
         var0 = -1;
      } else {
         boolean var2;
         if (var0 >= 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         int var3 = var0;
         if (!var2) {
            for(var0 = 0; var0 < var1; ++var0) {
               CameraInfo var4 = new CameraInfo();
               Camera.getCameraInfo(var0, var4);
               if (var4.facing == 0) {
                  break;
               }
            }

            var3 = var0;
         }

         var0 = var3;
         if (var3 >= var1) {
            if (var2) {
               var0 = -1;
            } else {
               var0 = 0;
            }
         }
      }

      return var0;
   }

   public static Camera open(int var0) {
      var0 = getCameraId(var0);
      Camera var1;
      if (var0 == -1) {
         var1 = null;
      } else {
         var1 = Camera.open(var0);
      }

      return var1;
   }
}
