package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileRefController$_CZgXfaxqSfurGMxYgHRkXa2trY implements Runnable {
   // $FF: synthetic field
   private final FileRefController f$0;
   // $FF: synthetic field
   private final TLRPC.User f$1;

   // $FF: synthetic method
   public _$$Lambda$FileRefController$_CZgXfaxqSfurGMxYgHRkXa2trY(FileRefController var1, TLRPC.User var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onRequestComplete$21$FileRefController(this.f$1);
   }
}
