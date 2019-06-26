package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$JoinGroupAlert$mdXFwxXoZ3zIuXYK2Qsi9b4N8JY implements RequestDelegate {
   // $FF: synthetic field
   private final JoinGroupAlert f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_importChatInvite f$1;

   // $FF: synthetic method
   public _$$Lambda$JoinGroupAlert$mdXFwxXoZ3zIuXYK2Qsi9b4N8JY(JoinGroupAlert var1, TLRPC.TL_messages_importChatInvite var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$2$JoinGroupAlert(this.f$1, var1, var2);
   }
}
