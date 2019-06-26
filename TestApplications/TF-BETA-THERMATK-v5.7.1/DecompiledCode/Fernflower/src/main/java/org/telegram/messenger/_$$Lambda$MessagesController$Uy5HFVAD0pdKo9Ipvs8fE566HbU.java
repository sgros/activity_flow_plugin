package org.telegram.messenger;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$Uy5HFVAD0pdKo9Ipvs8fE566HbU implements OnCancelListener {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$Uy5HFVAD0pdKo9Ipvs8fE566HbU(MessagesController var1, int var2, BaseFragment var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onCancel(DialogInterface var1) {
      this.f$0.lambda$checkCanOpenChat$258$MessagesController(this.f$1, this.f$2, var1);
   }
}
