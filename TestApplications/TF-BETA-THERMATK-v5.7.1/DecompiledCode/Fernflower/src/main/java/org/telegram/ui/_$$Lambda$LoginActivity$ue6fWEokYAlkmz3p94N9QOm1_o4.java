package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$ue6fWEokYAlkmz3p94N9QOm1_o4 implements OnClickListener {
   // $FF: synthetic field
   private final LoginActivity f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$ue6fWEokYAlkmz3p94N9QOm1_o4(LoginActivity var1, boolean var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$needShowInvalidAlert$0$LoginActivity(this.f$1, this.f$2, var1, var2);
   }
}
