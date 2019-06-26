package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$_Vi_J8_93cHW3trChoxTwI17XeA implements RequestDelegate {
   // $FF: synthetic field
   private final ContactsController f$0;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$_Vi_J8_93cHW3trChoxTwI17XeA(ContactsController var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$addContact$50$ContactsController(var1, var2);
   }
}
