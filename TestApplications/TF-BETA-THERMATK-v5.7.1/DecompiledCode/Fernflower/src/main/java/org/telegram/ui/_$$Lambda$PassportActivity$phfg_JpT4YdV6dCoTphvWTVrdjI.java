package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$phfg_JpT4YdV6dCoTphvWTVrdjI implements RequestDelegate {
   // $FF: synthetic field
   private final PassportActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$phfg_JpT4YdV6dCoTphvWTVrdjI(PassportActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadPasswordInfo$4$PassportActivity(var1, var2);
   }
}
