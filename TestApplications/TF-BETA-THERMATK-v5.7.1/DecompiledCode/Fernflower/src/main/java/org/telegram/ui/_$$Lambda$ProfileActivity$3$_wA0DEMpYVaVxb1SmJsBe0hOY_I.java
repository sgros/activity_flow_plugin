package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$3$_wA0DEMpYVaVxb1SmJsBe0hOY_I implements OnClickListener {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.User f$1;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$3$_wA0DEMpYVaVxb1SmJsBe0hOY_I(Object var1, TLRPC.User var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$onItemClick$1$ProfileActivity$3(this.f$1, var1, var2);
   }
}
