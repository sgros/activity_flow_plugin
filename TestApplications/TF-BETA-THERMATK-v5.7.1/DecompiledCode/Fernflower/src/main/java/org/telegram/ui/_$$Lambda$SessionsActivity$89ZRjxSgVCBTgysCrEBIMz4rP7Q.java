package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SessionsActivity$89ZRjxSgVCBTgysCrEBIMz4rP7Q implements RequestDelegate {
   // $FF: synthetic field
   private final SessionsActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$SessionsActivity$89ZRjxSgVCBTgysCrEBIMz4rP7Q(SessionsActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadSessions$13$SessionsActivity(var1, var2);
   }
}
