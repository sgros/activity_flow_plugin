package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$MGypkmy8t7OJpHH0z_YM20VQ6qU implements RequestDelegate {
   // $FF: synthetic field
   private final ChatActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$MGypkmy8t7OJpHH0z_YM20VQ6qU(ChatActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$startEditingMessageObject$67$ChatActivity(var1, var2);
   }
}
