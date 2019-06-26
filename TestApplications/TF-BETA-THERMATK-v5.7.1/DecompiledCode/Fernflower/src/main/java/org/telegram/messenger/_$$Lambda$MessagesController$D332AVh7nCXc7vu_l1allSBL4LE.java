package org.telegram.messenger;

import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$D332AVh7nCXc7vu_l1allSBL4LE implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final AlertDialog[] f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final BaseFragment f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$D332AVh7nCXc7vu_l1allSBL4LE(MessagesController var1, AlertDialog[] var2, int var3, BaseFragment var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$openByUserName$262$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
