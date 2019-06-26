package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$AJRaVVVB2_z__jsu2wGba_x0X0k implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final Object f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_saveGif f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$AJRaVVVB2_z__jsu2wGba_x0X0k(MessagesController var1, Object var2, TLRPC.TL_messages_saveGif var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$saveGif$71$MessagesController(this.f$1, this.f$2, var1, var2);
   }
}
