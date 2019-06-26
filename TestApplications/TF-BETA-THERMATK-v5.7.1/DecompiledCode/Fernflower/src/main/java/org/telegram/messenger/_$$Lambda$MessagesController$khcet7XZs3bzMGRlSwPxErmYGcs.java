package org.telegram.messenger;

import android.os.Bundle;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$khcet7XZs3bzMGRlSwPxErmYGcs implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final BaseFragment f$3;
   // $FF: synthetic field
   private final Bundle f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$khcet7XZs3bzMGRlSwPxErmYGcs(MessagesController var1, AlertDialog var2, TLObject var3, BaseFragment var4, Bundle var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$256$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
