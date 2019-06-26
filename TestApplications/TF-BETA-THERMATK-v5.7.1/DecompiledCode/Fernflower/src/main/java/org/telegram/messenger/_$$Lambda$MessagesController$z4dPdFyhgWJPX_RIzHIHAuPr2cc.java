package org.telegram.messenger;

import org.telegram.messenger.-..Lambda.MessagesController.z4dPdFyhgWJPX_RIzHIHAuPr2cc;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc implements RequestDelegate {
   // $FF: synthetic field
   public static final z4dPdFyhgWJPX_RIzHIHAuPr2cc INSTANCE = new _$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc();

   // $FF: synthetic method
   private _$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc() {
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      MessagesController.lambda$reportSpam$24(var1, var2);
   }
}
