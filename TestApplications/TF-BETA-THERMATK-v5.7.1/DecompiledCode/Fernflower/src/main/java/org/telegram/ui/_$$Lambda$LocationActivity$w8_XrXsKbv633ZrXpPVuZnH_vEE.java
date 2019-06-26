package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LocationActivity$w8_XrXsKbv633ZrXpPVuZnH_vEE implements RequestDelegate {
   // $FF: synthetic field
   private final LocationActivity f$0;
   // $FF: synthetic field
   private final long f$1;

   // $FF: synthetic method
   public _$$Lambda$LocationActivity$w8_XrXsKbv633ZrXpPVuZnH_vEE(LocationActivity var1, long var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$getRecentLocations$11$LocationActivity(this.f$1, var1, var2);
   }
}
