package org.telegram.ui;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$ChatEditActivity$j_VWblaHSOc0ptEwu8DVX6LNsH0 implements Runnable {
   // $FF: synthetic field
   private final ChatEditActivity f$0;
   // $FF: synthetic field
   private final CountDownLatch f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatEditActivity$j_VWblaHSOc0ptEwu8DVX6LNsH0(ChatEditActivity var1, CountDownLatch var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onFragmentCreate$0$ChatEditActivity(this.f$1);
   }
}
