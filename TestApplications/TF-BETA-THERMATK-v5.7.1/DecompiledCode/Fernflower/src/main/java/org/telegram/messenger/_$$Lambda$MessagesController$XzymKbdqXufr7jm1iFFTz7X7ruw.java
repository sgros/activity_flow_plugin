package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$XzymKbdqXufr7jm1iFFTz7X7ruw implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final long f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$XzymKbdqXufr7jm1iFFTz7X7ruw(MessagesController var1, long var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$131$MessagesController(this.f$1, var1, var2);
   }
}
