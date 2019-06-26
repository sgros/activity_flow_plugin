package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$IE61P_jfCib0uHF9uB4VvmEoPuE implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_updateUserBlocked f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$IE61P_jfCib0uHF9uB4VvmEoPuE(MessagesController var1, TLRPC.TL_updateUserBlocked var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$240$MessagesController(this.f$1);
   }
}
