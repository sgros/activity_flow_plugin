package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChannelCreateActivity$sX4Ap2SwpMlriw_rHlFC0Efiffk implements RequestDelegate {
   // $FF: synthetic field
   private final ChannelCreateActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChannelCreateActivity$sX4Ap2SwpMlriw_rHlFC0Efiffk(ChannelCreateActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadAdminedChannels$17$ChannelCreateActivity(var1, var2);
   }
}
