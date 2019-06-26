package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ChatLinkActivity$Hzb_oINiZZCK4So4QPZpn91gK3A implements RequestDelegate {
   // $FF: synthetic field
   private final ChatLinkActivity f$0;
   // $FF: synthetic field
   private final AlertDialog[] f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatLinkActivity$Hzb_oINiZZCK4So4QPZpn91gK3A(ChatLinkActivity var1, AlertDialog[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$1$ChatLinkActivity(this.f$1, var1, var2);
   }
}
