package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$L5hF2IhINUG6nIA5dSvAtaxsz8k implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.TL_updateNewMessage f$1;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$L5hF2IhINUG6nIA5dSvAtaxsz8k(SendMessagesHelper var1, TLRPC.TL_updateNewMessage var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$34$SendMessagesHelper(this.f$1);
   }
}
