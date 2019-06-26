package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$StickersAlert$dpylIMqKi3ssdFN3dGRn2NGUFco implements RequestDelegate {
   // $FF: synthetic field
   private final StickersAlert f$0;

   // $FF: synthetic method
   public _$$Lambda$StickersAlert$dpylIMqKi3ssdFN3dGRn2NGUFco(StickersAlert var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadStickerSet$4$StickersAlert(var1, var2);
   }
}
