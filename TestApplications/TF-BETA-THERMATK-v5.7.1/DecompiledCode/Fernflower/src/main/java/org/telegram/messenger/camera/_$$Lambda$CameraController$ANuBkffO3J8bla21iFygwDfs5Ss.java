package org.telegram.messenger.camera;

import android.graphics.SurfaceTexture;

// $FF: synthetic class
public final class _$$Lambda$CameraController$ANuBkffO3J8bla21iFygwDfs5Ss implements Runnable {
   // $FF: synthetic field
   private final CameraSession f$0;
   // $FF: synthetic field
   private final Runnable f$1;
   // $FF: synthetic field
   private final SurfaceTexture f$2;
   // $FF: synthetic field
   private final Runnable f$3;

   // $FF: synthetic method
   public _$$Lambda$CameraController$ANuBkffO3J8bla21iFygwDfs5Ss(CameraSession var1, Runnable var2, SurfaceTexture var3, Runnable var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      CameraController.lambda$openRound$8(this.f$0, this.f$1, this.f$2, this.f$3);
   }
}
