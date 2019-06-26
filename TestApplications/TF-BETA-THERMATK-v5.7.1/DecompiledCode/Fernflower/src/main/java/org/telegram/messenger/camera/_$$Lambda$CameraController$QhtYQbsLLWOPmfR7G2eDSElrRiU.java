package org.telegram.messenger.camera;

import android.hardware.Camera;
import java.io.File;

// $FF: synthetic class
public final class _$$Lambda$CameraController$QhtYQbsLLWOPmfR7G2eDSElrRiU implements Runnable {
   // $FF: synthetic field
   private final CameraController f$0;
   // $FF: synthetic field
   private final Camera f$1;
   // $FF: synthetic field
   private final CameraSession f$2;
   // $FF: synthetic field
   private final File f$3;
   // $FF: synthetic field
   private final CameraInfo f$4;
   // $FF: synthetic field
   private final CameraController.VideoTakeCallback f$5;
   // $FF: synthetic field
   private final Runnable f$6;

   // $FF: synthetic method
   public _$$Lambda$CameraController$QhtYQbsLLWOPmfR7G2eDSElrRiU(CameraController var1, Camera var2, CameraSession var3, File var4, CameraInfo var5, CameraController.VideoTakeCallback var6, Runnable var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$recordVideo$10$CameraController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
