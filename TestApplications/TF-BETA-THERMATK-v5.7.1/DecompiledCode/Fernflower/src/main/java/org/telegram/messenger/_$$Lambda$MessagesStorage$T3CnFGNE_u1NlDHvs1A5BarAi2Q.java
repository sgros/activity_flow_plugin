package org.telegram.messenger;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$T3CnFGNE_u1NlDHvs1A5BarAi2Q implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final CountDownLatch f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$T3CnFGNE_u1NlDHvs1A5BarAi2Q(MessagesStorage var1, int var2, ArrayList var3, CountDownLatch var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$getEncryptedChat$110$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
