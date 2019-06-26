package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$StickersAlert$70AzTfODxLbeSRDlB8gJ7WKPlnE implements RequestDelegate {
   // $FF: synthetic field
   private final StickersAlert f$0;
   // $FF: synthetic field
   private final Object f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getAttachedStickers f$2;
   // $FF: synthetic field
   private final RequestDelegate f$3;

   // $FF: synthetic method
   public _$$Lambda$StickersAlert$70AzTfODxLbeSRDlB8gJ7WKPlnE(StickersAlert var1, Object var2, TLRPC.TL_messages_getAttachedStickers var3, RequestDelegate var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$new$2$StickersAlert(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
