package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$19$1$seC_3uNPyiW1J2ZXyiE5PktGlCs implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.TL_secureValue f$1;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$19$1$seC_3uNPyiW1J2ZXyiE5PktGlCs(Object var1, TLRPC.TL_secureValue var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$run$4$PassportActivity$19$1(this.f$1, var1, var2);
   }
}
