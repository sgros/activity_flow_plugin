package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$AcnaGQSIUUZcaRV7Rm4_aExKG2o implements RequestDelegate {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final ArrayList f$2;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$AcnaGQSIUUZcaRV7Rm4_aExKG2o(ContactsController var1, ArrayList var2, ArrayList var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$deleteContact$53$ContactsController(this.f$1, this.f$2, var1, var2);
   }
}
