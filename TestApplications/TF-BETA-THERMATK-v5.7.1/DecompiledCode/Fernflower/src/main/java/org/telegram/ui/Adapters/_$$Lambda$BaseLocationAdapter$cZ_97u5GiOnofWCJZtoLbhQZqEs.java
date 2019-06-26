package org.telegram.ui.Adapters;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$BaseLocationAdapter$cZ_97u5GiOnofWCJZtoLbhQZqEs implements Runnable {
   // $FF: synthetic field
   private final BaseLocationAdapter f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;

   // $FF: synthetic method
   public _$$Lambda$BaseLocationAdapter$cZ_97u5GiOnofWCJZtoLbhQZqEs(BaseLocationAdapter var1, TLRPC.TL_error var2, TLObject var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$2$BaseLocationAdapter(this.f$1, this.f$2);
   }
}
