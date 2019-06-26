package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$LgaPvpXbmtv6_BmnTvc8qVOudt4 implements RequestDelegate {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$LgaPvpXbmtv6_BmnTvc8qVOudt4(ContactsController var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadContacts$27$ContactsController(this.f$1, var1, var2);
   }
}
