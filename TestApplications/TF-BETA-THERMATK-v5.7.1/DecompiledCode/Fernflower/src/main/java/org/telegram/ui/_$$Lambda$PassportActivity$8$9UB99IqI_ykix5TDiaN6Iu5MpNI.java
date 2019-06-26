package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$8$9UB99IqI_ykix5TDiaN6Iu5MpNI implements RequestDelegate {
   // $FF: synthetic field
   private final PassportActivity$8 f$0;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$8$9UB99IqI_ykix5TDiaN6Iu5MpNI(PassportActivity$8 var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$resetSecret$3$PassportActivity$8(var1, var2);
   }
}
