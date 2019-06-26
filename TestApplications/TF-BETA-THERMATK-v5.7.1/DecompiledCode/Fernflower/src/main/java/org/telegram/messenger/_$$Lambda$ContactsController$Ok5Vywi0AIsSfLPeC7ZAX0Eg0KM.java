package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$Ok5Vywi0AIsSfLPeC7ZAX0Eg0KM implements RequestDelegate {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final Runnable f$1;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$Ok5Vywi0AIsSfLPeC7ZAX0Eg0KM(ContactsController var1, Runnable var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$deleteAllContacts$8$ContactsController(this.f$1, var1, var2);
   }
}
