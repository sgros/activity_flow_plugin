package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$T4klBi4vmBoUx4jRc3cN4SM0ygE implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.updates_Difference f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$T4klBi4vmBoUx4jRc3cN4SM0ygE(MessagesController var1, TLRPC.updates_Difference var2, int var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$206$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
