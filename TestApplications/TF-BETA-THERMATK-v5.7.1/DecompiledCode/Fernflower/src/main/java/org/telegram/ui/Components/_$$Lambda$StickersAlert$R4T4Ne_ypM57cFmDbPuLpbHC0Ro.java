package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$StickersAlert$R4T4Ne_ypM57cFmDbPuLpbHC0Ro implements RequestDelegate {
   // $FF: synthetic field
   private final StickersAlert f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getAttachedStickers f$1;

   // $FF: synthetic method
   public _$$Lambda$StickersAlert$R4T4Ne_ypM57cFmDbPuLpbHC0Ro(StickersAlert var1, TLRPC.TL_messages_getAttachedStickers var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$new$1$StickersAlert(this.f$1, var1, var2);
   }
}
