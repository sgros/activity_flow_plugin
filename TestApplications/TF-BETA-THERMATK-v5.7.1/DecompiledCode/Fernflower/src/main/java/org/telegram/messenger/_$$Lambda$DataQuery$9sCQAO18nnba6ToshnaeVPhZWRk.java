package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$9sCQAO18nnba6ToshnaeVPhZWRk implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$9sCQAO18nnba6ToshnaeVPhZWRk(DataQuery var1, TLRPC.Message var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$savePinnedMessage$88$DataQuery(this.f$1);
   }
}
