package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$vdUYw660VQTUH5B_NPbzLsGZ09M implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final long f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$vdUYw660VQTUH5B_NPbzLsGZ09M(MessagesController var1, long var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$markDialogAsUnread$215$MessagesController(this.f$1, var1, var2);
   }
}
