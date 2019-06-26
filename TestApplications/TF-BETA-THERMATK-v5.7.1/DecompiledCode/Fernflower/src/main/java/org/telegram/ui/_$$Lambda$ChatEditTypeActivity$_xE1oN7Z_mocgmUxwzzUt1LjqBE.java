package org.telegram.ui;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$ChatEditTypeActivity$_xE1oN7Z_mocgmUxwzzUt1LjqBE implements Runnable {
   // $FF: synthetic field
   private final ChatEditTypeActivity f$0;
   // $FF: synthetic field
   private final CountDownLatch f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatEditTypeActivity$_xE1oN7Z_mocgmUxwzzUt1LjqBE(ChatEditTypeActivity var1, CountDownLatch var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onFragmentCreate$0$ChatEditTypeActivity(this.f$1);
   }
}
