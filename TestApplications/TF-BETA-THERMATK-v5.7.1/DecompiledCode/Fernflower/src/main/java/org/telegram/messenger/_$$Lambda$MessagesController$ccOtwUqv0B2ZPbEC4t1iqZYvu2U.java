package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$ccOtwUqv0B2ZPbEC4t1iqZYvu2U implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage.IntCallback f$0;
   // $FF: synthetic field
   private final TLRPC.Updates f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$ccOtwUqv0B2ZPbEC4t1iqZYvu2U(MessagesStorage.IntCallback var1, TLRPC.Updates var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      MessagesController.lambda$null$162(this.f$0, this.f$1);
   }
}
