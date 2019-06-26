package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LocationController$9e6Jr8NCwhd7x0jAE3itGEDvCZ8 implements RequestDelegate {
   // $FF: synthetic field
   private final LocationController f$0;
   // $FF: synthetic field
   private final long f$1;

   // $FF: synthetic method
   public _$$Lambda$LocationController$9e6Jr8NCwhd7x0jAE3itGEDvCZ8(LocationController var1, long var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadLiveLocations$18$LocationController(this.f$1, var1, var2);
   }
}
