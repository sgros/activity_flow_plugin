package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$40Sp7FeVOZVQXMLSFubFHF48OI8 implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$40Sp7FeVOZVQXMLSFubFHF48OI8(MessagesController var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$changeChatTitle$183$MessagesController(var1, var2);
   }
}