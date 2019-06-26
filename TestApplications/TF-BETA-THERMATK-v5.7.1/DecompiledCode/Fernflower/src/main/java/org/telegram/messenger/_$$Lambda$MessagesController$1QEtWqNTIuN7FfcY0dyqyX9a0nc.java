package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$1QEtWqNTIuN7FfcY0dyqyX9a0nc implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final TLRPC.TL_channels_createChannel f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$1QEtWqNTIuN7FfcY0dyqyX9a0nc(MessagesController var1, TLRPC.TL_error var2, BaseFragment var3, TLRPC.TL_channels_createChannel var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$158$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
