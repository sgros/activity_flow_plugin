package org.telegram.messenger.camera;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$CameraController$Mku2q5OGwNan_h4puDdyST57U90 implements Runnable {
   // $FF: synthetic field
   private final Runnable f$0;
   // $FF: synthetic field
   private final CameraSession f$1;
   // $FF: synthetic field
   private final CountDownLatch f$2;

   // $FF: synthetic method
   public _$$Lambda$CameraController$Mku2q5OGwNan_h4puDdyST57U90(Runnable var1, CameraSession var2, CountDownLatch var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      CameraController.lambda$close$4(this.f$0, this.f$1, this.f$2);
   }
}
