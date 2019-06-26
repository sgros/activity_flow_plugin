package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$d7oypRr3g0xB92hPNQy49uD_p8A implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final TLRPC.Message f$2;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$d7oypRr3g0xB92hPNQy49uD_p8A(DataQuery var1, long var2, TLRPC.Message var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
   }

   public final void run() {
      this.f$0.lambda$saveDraftReplyMessage$103$DataQuery(this.f$1, this.f$2);
   }
}
