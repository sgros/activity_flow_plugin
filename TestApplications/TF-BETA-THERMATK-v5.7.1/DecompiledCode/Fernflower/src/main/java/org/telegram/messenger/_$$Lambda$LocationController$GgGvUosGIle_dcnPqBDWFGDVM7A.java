package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LocationController$GgGvUosGIle_dcnPqBDWFGDVM7A implements RequestDelegate {
   // $FF: synthetic field
   private final LocationController f$0;
   // $FF: synthetic field
   private final LocationController.SharingLocationInfo f$1;
   // $FF: synthetic field
   private final int[] f$2;

   // $FF: synthetic method
   public _$$Lambda$LocationController$GgGvUosGIle_dcnPqBDWFGDVM7A(LocationController var1, LocationController.SharingLocationInfo var2, int[] var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$broadcastLastKnownLocation$2$LocationController(this.f$1, this.f$2, var1, var2);
   }
}