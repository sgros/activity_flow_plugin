package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$EuAKug4zrNU4o4RK27r3kMVoC7I implements Runnable {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$EuAKug4zrNU4o4RK27r3kMVoC7I(ContactsController var1, TLRPC.TL_error var2, TLObject var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$58$ContactsController(this.f$1, this.f$2, this.f$3);
   }
}
