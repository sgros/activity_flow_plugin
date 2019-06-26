package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$yEPLjf91Lbxq_PAfwQRKxAmq0Lc implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_chatFull f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$yEPLjf91Lbxq_PAfwQRKxAmq0Lc(MessagesController var1, int var2, TLRPC.TL_messages_chatFull var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$15$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
