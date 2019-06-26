package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$19$1$z2A8tnBlw_sIzEUvPsIbORU04FQ implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final TLRPC.TL_secureRequiredType f$2;
   // $FF: synthetic field
   private final PassportActivity.PassportActivityDelegate f$3;
   // $FF: synthetic field
   private final PassportActivity.ErrorRunnable f$4;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$19$1$z2A8tnBlw_sIzEUvPsIbORU04FQ(Object var1, String var2, TLRPC.TL_secureRequiredType var3, PassportActivity.PassportActivityDelegate var4, PassportActivity.ErrorRunnable var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$run$2$PassportActivity$19$1(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
