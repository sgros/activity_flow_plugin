package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileRefController$ezrB_EEVIghp6y7yWEa40dOLdLU implements Runnable {
   // $FF: synthetic field
   private final FileRefController f$0;
   // $FF: synthetic field
   private final TLRPC.Chat f$1;

   // $FF: synthetic method
   public _$$Lambda$FileRefController$ezrB_EEVIghp6y7yWEa40dOLdLU(FileRefController var1, TLRPC.Chat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onRequestComplete$22$FileRefController(this.f$1);
   }
}
