package org.telegram.ui;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$ELtZVLkMORk4xUSfHIm0LgPewE8 implements Runnable {
   // $FF: synthetic field
   private final ProfileActivity f$0;
   // $FF: synthetic field
   private final CountDownLatch f$1;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$ELtZVLkMORk4xUSfHIm0LgPewE8(ProfileActivity var1, CountDownLatch var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onFragmentCreate$0$ProfileActivity(this.f$1);
   }
}
