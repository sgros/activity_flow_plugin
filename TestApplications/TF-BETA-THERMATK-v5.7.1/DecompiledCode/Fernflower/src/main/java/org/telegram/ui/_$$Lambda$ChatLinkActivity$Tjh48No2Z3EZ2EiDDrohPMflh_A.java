package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$ChatLinkActivity$Tjh48No2Z3EZ2EiDDrohPMflh_A implements RequestDelegate {
   // $FF: synthetic field
   private final ChatLinkActivity f$0;
   // $FF: synthetic field
   private final AlertDialog[] f$1;
   // $FF: synthetic field
   private final TLRPC.Chat f$2;
   // $FF: synthetic field
   private final BaseFragment f$3;

   // $FF: synthetic method
   public _$$Lambda$ChatLinkActivity$Tjh48No2Z3EZ2EiDDrohPMflh_A(ChatLinkActivity var1, AlertDialog[] var2, TLRPC.Chat var3, BaseFragment var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$linkChat$11$ChatLinkActivity(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
