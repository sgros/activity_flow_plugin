package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChannelCreateActivity$Dgmh8FVFPsTpRB5V7O64UrcVHdE implements RequestDelegate {
   // $FF: synthetic field
   private final ChannelCreateActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChannelCreateActivity$Dgmh8FVFPsTpRB5V7O64UrcVHdE(ChannelCreateActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$new$1$ChannelCreateActivity(var1, var2);
   }
}