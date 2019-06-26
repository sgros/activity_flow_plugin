package org.telegram.messenger.camera;

import android.graphics.Bitmap;
import java.io.File;

// $FF: synthetic class
public final class _$$Lambda$CameraController$5HicrJ12U2c73GyHfU8XHEFg__A implements Runnable {
   // $FF: synthetic field
   private final CameraController f$0;
   // $FF: synthetic field
   private final File f$1;
   // $FF: synthetic field
   private final Bitmap f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$CameraController$5HicrJ12U2c73GyHfU8XHEFg__A(CameraController var1, File var2, Bitmap var3, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$finishRecordingVideo$11$CameraController(this.f$1, this.f$2, this.f$3);
   }
}
