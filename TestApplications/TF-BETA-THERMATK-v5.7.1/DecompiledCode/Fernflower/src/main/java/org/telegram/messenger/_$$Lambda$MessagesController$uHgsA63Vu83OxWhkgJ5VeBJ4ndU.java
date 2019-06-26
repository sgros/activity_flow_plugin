package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$uHgsA63Vu83OxWhkgJ5VeBJ4ndU implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.updates_Difference f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$uHgsA63Vu83OxWhkgJ5VeBJ4ndU(MessagesController var1, TLRPC.updates_Difference var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$207$MessagesController(this.f$1);
   }
}
