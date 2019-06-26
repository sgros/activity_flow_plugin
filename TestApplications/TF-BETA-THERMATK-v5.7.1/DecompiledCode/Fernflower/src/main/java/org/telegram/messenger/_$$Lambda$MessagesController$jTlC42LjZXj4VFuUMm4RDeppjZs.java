package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$jTlC42LjZXj4VFuUMm4RDeppjZs implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final TLRPC.TL_messages_editChatDefaultBannedRights f$3;
   // $FF: synthetic field
   private final boolean f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$jTlC42LjZXj4VFuUMm4RDeppjZs(MessagesController var1, TLRPC.TL_error var2, BaseFragment var3, TLRPC.TL_messages_editChatDefaultBannedRights var4, boolean var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$44$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
