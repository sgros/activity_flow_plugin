package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$E_B3T1gXoq4eD739GgHdBDLPpBs implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_config f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$E_B3T1gXoq4eD739GgHdBDLPpBs(MessagesController var1, TLRPC.TL_config var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$updateConfig$2$MessagesController(this.f$1);
   }
}
