package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$l40Sz3n3XBnqm5WNgM6XcCJnSAw implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_forwardMessages f$2;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$l40Sz3n3XBnqm5WNgM6XcCJnSAw(SendMessagesHelper var1, TLRPC.TL_error var2, TLRPC.TL_messages_forwardMessages var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$7$SendMessagesHelper(this.f$1, this.f$2);
   }
}
