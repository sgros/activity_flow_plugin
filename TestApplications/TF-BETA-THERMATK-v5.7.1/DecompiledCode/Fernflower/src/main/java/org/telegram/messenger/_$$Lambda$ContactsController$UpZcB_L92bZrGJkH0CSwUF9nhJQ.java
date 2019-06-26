package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$UpZcB_L92bZrGJkH0CSwUF9nhJQ implements RequestDelegate {
   // $FF: synthetic field
   private final ContactsController f$0;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$UpZcB_L92bZrGJkH0CSwUF9nhJQ(ContactsController var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadPrivacySettings$57$ContactsController(var1, var2);
   }
}
