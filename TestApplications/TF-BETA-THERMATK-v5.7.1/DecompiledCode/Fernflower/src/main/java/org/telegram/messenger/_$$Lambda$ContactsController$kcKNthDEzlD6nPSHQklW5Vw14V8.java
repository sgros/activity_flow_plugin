package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$kcKNthDEzlD6nPSHQklW5Vw14V8 implements RequestDelegate {
   // $FF: synthetic field
   private final ContactsController f$0;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$kcKNthDEzlD6nPSHQklW5Vw14V8(ContactsController var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$checkInviteText$3$ContactsController(var1, var2);
   }
}
