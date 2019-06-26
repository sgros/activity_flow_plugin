package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LocaleController$C7GeSsDILXp_14TJUT5eNNANB6o implements RequestDelegate {
   // $FF: synthetic field
   private final LocaleController f$0;
   // $FF: synthetic field
   private final LocaleController.LocaleInfo f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$LocaleController$C7GeSsDILXp_14TJUT5eNNANB6o(LocaleController var1, LocaleController.LocaleInfo var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$applyRemoteLanguage$10$LocaleController(this.f$1, this.f$2, var1, var2);
   }
}
