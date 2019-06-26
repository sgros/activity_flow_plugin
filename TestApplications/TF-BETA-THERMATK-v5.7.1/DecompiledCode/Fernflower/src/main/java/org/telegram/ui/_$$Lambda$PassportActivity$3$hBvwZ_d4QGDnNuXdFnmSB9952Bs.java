package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$3$hBvwZ_d4QGDnNuXdFnmSB9952Bs implements OnClickListener {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final Runnable f$4;
   // $FF: synthetic field
   private final PassportActivity.ErrorRunnable f$5;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$3$hBvwZ_d4QGDnNuXdFnmSB9952Bs(Object var1, String var2, String var3, String var4, Runnable var5, PassportActivity.ErrorRunnable var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$onIdentityDone$0$PassportActivity$3(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, var1, var2);
   }
}
