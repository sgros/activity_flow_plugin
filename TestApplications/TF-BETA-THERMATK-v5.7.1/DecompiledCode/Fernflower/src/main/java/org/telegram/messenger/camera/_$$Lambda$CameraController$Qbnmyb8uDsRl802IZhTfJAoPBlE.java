package org.telegram.messenger.camera;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import java.io.File;

// $FF: synthetic class
public final class _$$Lambda$CameraController$Qbnmyb8uDsRl802IZhTfJAoPBlE implements PictureCallback {
   // $FF: synthetic field
   private final File f$0;
   // $FF: synthetic field
   private final CameraInfo f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final Runnable f$3;

   // $FF: synthetic method
   public _$$Lambda$CameraController$Qbnmyb8uDsRl802IZhTfJAoPBlE(File var1, CameraInfo var2, boolean var3, Runnable var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void onPictureTaken(byte[] var1, Camera var2) {
      CameraController.lambda$takePicture$5(this.f$0, this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
