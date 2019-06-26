package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$11fT0i1GEqsG3tRPr2wtXLuwL70 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.User f$1;
   // $FF: synthetic field
   private final TLRPC.UserFull f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$11fT0i1GEqsG3tRPr2wtXLuwL70(MessagesController var1, TLRPC.User var2, TLRPC.UserFull var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$18$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
