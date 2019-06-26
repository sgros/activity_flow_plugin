package org.telegram.ui.ActionBar;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

// $FF: synthetic class
public final class _$$Lambda$BaseFragment$vXTvtAK8XZpLjv4Env96FSJndOM implements OnDismissListener {
   // $FF: synthetic field
   private final BaseFragment f$0;
   // $FF: synthetic field
   private final OnDismissListener f$1;

   // $FF: synthetic method
   public _$$Lambda$BaseFragment$vXTvtAK8XZpLjv4Env96FSJndOM(BaseFragment var1, OnDismissListener var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onDismiss(DialogInterface var1) {
      this.f$0.lambda$showDialog$0$BaseFragment(this.f$1, var1);
   }
}
