package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$UPmZCQ76huGf0mwqhk7A6RKfTkw implements RequestDelegate {
   // $FF: synthetic field
   private final ProfileActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$UPmZCQ76huGf0mwqhk7A6RKfTkw(ProfileActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$createView$8$ProfileActivity(var1, var2);
   }
}
