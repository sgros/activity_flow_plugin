package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChannelCreateActivity$B1f5C5wPqlSBUACduCE1syb6pss implements RequestDelegate {
   // $FF: synthetic field
   private final ChannelCreateActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChannelCreateActivity$B1f5C5wPqlSBUACduCE1syb6pss(ChannelCreateActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$generateLink$10$ChannelCreateActivity(var1, var2);
   }
}
