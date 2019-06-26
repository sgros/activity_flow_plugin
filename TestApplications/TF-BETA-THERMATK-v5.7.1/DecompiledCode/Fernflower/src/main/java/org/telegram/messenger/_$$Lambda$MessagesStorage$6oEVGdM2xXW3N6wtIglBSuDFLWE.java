package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$6oEVGdM2xXW3N6wtIglBSuDFLWE implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.UserFull f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$6oEVGdM2xXW3N6wtIglBSuDFLWE(MessagesStorage var1, int var2, TLRPC.UserFull var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$76$MessagesStorage(this.f$1, this.f$2);
   }
}
