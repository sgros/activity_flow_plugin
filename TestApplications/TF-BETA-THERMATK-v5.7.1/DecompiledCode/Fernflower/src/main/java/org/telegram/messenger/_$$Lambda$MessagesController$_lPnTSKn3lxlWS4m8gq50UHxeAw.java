package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$_lPnTSKn3lxlWS4m8gq50UHxeAw implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.Chat f$1;
   // $FF: synthetic field
   private final TLRPC.User f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$_lPnTSKn3lxlWS4m8gq50UHxeAw(MessagesController var1, TLRPC.Chat var2, TLRPC.User var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$deleteUserChannelHistory$64$MessagesController(this.f$1, this.f$2, var1, var2);
   }
}
