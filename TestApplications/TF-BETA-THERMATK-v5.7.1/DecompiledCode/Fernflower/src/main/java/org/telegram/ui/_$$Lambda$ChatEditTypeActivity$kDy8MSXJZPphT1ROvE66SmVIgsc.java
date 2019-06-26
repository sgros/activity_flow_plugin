package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatEditTypeActivity$kDy8MSXJZPphT1ROvE66SmVIgsc implements RequestDelegate {
   // $FF: synthetic field
   private final ChatEditTypeActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChatEditTypeActivity$kDy8MSXJZPphT1ROvE66SmVIgsc(ChatEditTypeActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadAdminedChannels$16$ChatEditTypeActivity(var1, var2);
   }
}
