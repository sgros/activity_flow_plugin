package com.journeyapps.barcodescanner.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build.VERSION;
import android.view.SurfaceHolder;
import java.io.IOException;

public class CameraSurface {
   private SurfaceHolder surfaceHolder;
   private SurfaceTexture surfaceTexture;

   public CameraSurface(SurfaceTexture var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("surfaceTexture may not be null");
      } else {
         this.surfaceTexture = var1;
      }
   }

   public CameraSurface(SurfaceHolder var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("surfaceHolder may not be null");
      } else {
         this.surfaceHolder = var1;
      }
   }

   public SurfaceHolder getSurfaceHolder() {
      return this.surfaceHolder;
   }

   public SurfaceTexture getSurfaceTexture() {
      return this.surfaceTexture;
   }

   public void setPreview(Camera var1) throws IOException {
      if (this.surfaceHolder != null) {
         var1.setPreviewDisplay(this.surfaceHolder);
      } else {
         if (VERSION.SDK_INT < 11) {
            throw new IllegalStateException("SurfaceTexture not supported.");
         }

         var1.setPreviewTexture(this.surfaceTexture);
      }

   }
}
