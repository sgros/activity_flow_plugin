package org.telegram.ui;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$ChatLinkActivity$QC2Mz33KGE4zAmpWeIpueArOQXY implements Runnable {
   // $FF: synthetic field
   private final ChatLinkActivity f$0;
   // $FF: synthetic field
   private final AlertDialog[] f$1;
   // $FF: synthetic field
   private final TLRPC.Chat f$2;
   // $FF: synthetic field
   private final BaseFragment f$3;

   // $FF: synthetic method
   public _$$Lambda$ChatLinkActivity$QC2Mz33KGE4zAmpWeIpueArOQXY(ChatLinkActivity var1, AlertDialog[] var2, TLRPC.Chat var3, BaseFragment var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$10$ChatLinkActivity(this.f$1, this.f$2, this.f$3);
   }
}
