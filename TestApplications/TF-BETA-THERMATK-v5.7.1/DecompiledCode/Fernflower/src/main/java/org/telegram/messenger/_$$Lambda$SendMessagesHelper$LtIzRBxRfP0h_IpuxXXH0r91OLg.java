package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$LtIzRBxRfP0h_IpuxXXH0r91OLg implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.TL_updateNewMessage f$1;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$LtIzRBxRfP0h_IpuxXXH0r91OLg(SendMessagesHelper var1, TLRPC.TL_updateNewMessage var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$23$SendMessagesHelper(this.f$1);
   }
}
