package org.telegram.ui.Components;

import android.content.SharedPreferences;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$M0pJapDgZt4nkaop5tg0yx7_Iys implements Runnable {
   // $FF: synthetic field
   private final SharedPreferences f$0;
   // $FF: synthetic field
   private final TLRPC.TL_help_support f$1;
   // $FF: synthetic field
   private final AlertDialog f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final BaseFragment f$4;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$M0pJapDgZt4nkaop5tg0yx7_Iys(SharedPreferences var1, TLRPC.TL_help_support var2, AlertDialog var3, int var4, BaseFragment var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      AlertsCreator.lambda$null$6(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
