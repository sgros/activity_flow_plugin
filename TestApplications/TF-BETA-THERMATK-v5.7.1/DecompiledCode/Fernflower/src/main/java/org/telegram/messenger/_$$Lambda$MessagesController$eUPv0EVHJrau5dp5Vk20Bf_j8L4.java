package org.telegram.messenger;

import org.telegram.messenger.-..Lambda.MessagesController.eUPv0EVHJrau5dp5Vk20Bf_j8L4;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4 implements RequestDelegate {
   // $FF: synthetic field
   public static final eUPv0EVHJrau5dp5Vk20Bf_j8L4 INSTANCE = new _$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4();

   // $FF: synthetic method
   private _$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4() {
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      MessagesController.lambda$markMentionsAsRead$149(var1, var2);
   }
}
