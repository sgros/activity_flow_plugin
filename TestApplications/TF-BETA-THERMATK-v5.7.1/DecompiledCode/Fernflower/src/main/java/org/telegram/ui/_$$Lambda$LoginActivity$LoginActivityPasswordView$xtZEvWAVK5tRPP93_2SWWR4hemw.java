package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivityPasswordView$xtZEvWAVK5tRPP93_2SWWR4hemw implements OnClickListener {
   // $FF: synthetic field
   private final LoginActivity.LoginActivityPasswordView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_auth_passwordRecovery f$1;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivityPasswordView$xtZEvWAVK5tRPP93_2SWWR4hemw(LoginActivity.LoginActivityPasswordView var1, TLRPC.TL_auth_passwordRecovery var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$1$LoginActivity$LoginActivityPasswordView(this.f$1, var1, var2);
   }
}
