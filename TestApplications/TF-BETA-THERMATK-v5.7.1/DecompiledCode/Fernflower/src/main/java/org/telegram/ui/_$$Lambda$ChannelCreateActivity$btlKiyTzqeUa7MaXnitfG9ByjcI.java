package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChannelCreateActivity$btlKiyTzqeUa7MaXnitfG9ByjcI implements RequestDelegate {
   // $FF: synthetic field
   private final ChannelCreateActivity f$0;
   // $FF: synthetic field
   private final String f$1;

   // $FF: synthetic method
   public _$$Lambda$ChannelCreateActivity$btlKiyTzqeUa7MaXnitfG9ByjcI(ChannelCreateActivity var1, String var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$19$ChannelCreateActivity(this.f$1, var1, var2);
   }
}
