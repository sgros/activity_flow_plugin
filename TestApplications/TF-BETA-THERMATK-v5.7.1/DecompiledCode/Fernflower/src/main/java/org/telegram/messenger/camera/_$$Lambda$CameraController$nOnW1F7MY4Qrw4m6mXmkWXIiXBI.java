package org.telegram.messenger.camera;

import android.graphics.SurfaceTexture;

// $FF: synthetic class
public final class _$$Lambda$CameraController$nOnW1F7MY4Qrw4m6mXmkWXIiXBI implements Runnable {
   // $FF: synthetic field
   private final CameraController f$0;
   // $FF: synthetic field
   private final CameraSession f$1;
   // $FF: synthetic field
   private final Runnable f$2;
   // $FF: synthetic field
   private final SurfaceTexture f$3;
   // $FF: synthetic field
   private final Runnable f$4;

   // $FF: synthetic method
   public _$$Lambda$CameraController$nOnW1F7MY4Qrw4m6mXmkWXIiXBI(CameraController var1, CameraSession var2, Runnable var3, SurfaceTexture var4, Runnable var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$open$9$CameraController(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
