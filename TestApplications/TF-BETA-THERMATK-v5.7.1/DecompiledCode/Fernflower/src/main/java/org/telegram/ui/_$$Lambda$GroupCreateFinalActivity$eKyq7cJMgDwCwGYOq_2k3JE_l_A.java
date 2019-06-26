package org.telegram.ui;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$GroupCreateFinalActivity$eKyq7cJMgDwCwGYOq_2k3JE_l_A implements Runnable {
   // $FF: synthetic field
   private final GroupCreateFinalActivity f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final CountDownLatch f$3;

   // $FF: synthetic method
   public _$$Lambda$GroupCreateFinalActivity$eKyq7cJMgDwCwGYOq_2k3JE_l_A(GroupCreateFinalActivity var1, ArrayList var2, ArrayList var3, CountDownLatch var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$onFragmentCreate$0$GroupCreateFinalActivity(this.f$1, this.f$2, this.f$3);
   }
}
