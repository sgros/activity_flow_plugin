package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$Kw60WHVImMJ5bL_qjfLao1O7Ey8 implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$Kw60WHVImMJ5bL_qjfLao1O7Ey8(MessagesController var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadGlobalNotificationsSettings$115$MessagesController(this.f$1, var1, var2);
   }
}
