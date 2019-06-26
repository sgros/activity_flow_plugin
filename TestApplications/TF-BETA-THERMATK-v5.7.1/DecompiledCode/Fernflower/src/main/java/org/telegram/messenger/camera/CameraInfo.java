package org.telegram.messenger.camera;

import android.hardware.Camera;
import java.util.ArrayList;

public class CameraInfo {
   protected Camera camera;
   protected int cameraId;
   protected final int frontCamera;
   protected ArrayList pictureSizes = new ArrayList();
   protected ArrayList previewSizes = new ArrayList();

   public CameraInfo(int var1, int var2) {
      this.cameraId = var1;
      this.frontCamera = var2;
   }

   private Camera getCamera() {
      return this.camera;
   }

   public int getCameraId() {
      return this.cameraId;
   }

   public ArrayList getPictureSizes() {
      return this.pictureSizes;
   }

   public ArrayList getPreviewSizes() {
      return this.previewSizes;
   }

   public boolean isFrontface() {
      boolean var1;
      if (this.frontCamera != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
