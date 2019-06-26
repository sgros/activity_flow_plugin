package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$xuR4Dv4llnFxbNDnZ9xFUOdZuww implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$xuR4Dv4llnFxbNDnZ9xFUOdZuww(MessagesController var1, TLRPC.TL_error var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$204$MessagesController(this.f$1, this.f$2);
   }
}
