package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$5FORFfWffqEc1IY97T80wKl0r1Y implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Updates f$1;
   // $FF: synthetic field
   private final TLRPC.Message f$2;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$5FORFfWffqEc1IY97T80wKl0r1Y(SendMessagesHelper var1, TLRPC.Updates var2, TLRPC.Message var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$31$SendMessagesHelper(this.f$1, this.f$2);
   }
}
