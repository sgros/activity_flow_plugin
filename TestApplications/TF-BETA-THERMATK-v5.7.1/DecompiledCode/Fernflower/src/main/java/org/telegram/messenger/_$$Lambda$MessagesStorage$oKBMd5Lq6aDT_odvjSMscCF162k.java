package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$oKBMd5Lq6aDT_odvjSMscCF162k implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final boolean[] f$2;
   // $FF: synthetic field
   private final CountDownLatch f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$oKBMd5Lq6aDT_odvjSMscCF162k(MessagesStorage var1, long var2, boolean[] var4, CountDownLatch var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
   }

   public final void run() {
      this.f$0.lambda$isDialogHasMessages$108$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
