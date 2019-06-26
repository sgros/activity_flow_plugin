package org.telegram.messenger.camera;

// $FF: synthetic class
public final class _$$Lambda$CameraController$L4S5_dxkHAFS7LzqMIp7eixEAXY implements Runnable {
   // $FF: synthetic field
   private final CameraController f$0;
   // $FF: synthetic field
   private final CameraSession f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$CameraController$L4S5_dxkHAFS7LzqMIp7eixEAXY(CameraController var1, CameraSession var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$stopVideoRecording$13$CameraController(this.f$1, this.f$2);
   }
}
