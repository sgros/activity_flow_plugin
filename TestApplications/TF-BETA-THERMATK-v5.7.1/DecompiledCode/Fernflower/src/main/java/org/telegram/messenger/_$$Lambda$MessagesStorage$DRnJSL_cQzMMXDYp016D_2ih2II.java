package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$DRnJSL_cQzMMXDYp016D_2ih2II implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final boolean[] f$2;
   // $FF: synthetic field
   private final CountDownLatch f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$DRnJSL_cQzMMXDYp016D_2ih2II(MessagesStorage var1, int var2, boolean[] var3, CountDownLatch var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$hasAuthMessage$109$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}