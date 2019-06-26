package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$SBPx7fdXp_MbcirnzsQkO7Z6gg0 implements OnClickListener {
   // $FF: synthetic field
   private final PassportActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_secureRequiredType f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$SBPx7fdXp_MbcirnzsQkO7Z6gg0(PassportActivity var1, TLRPC.TL_secureRequiredType var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$64$PassportActivity(this.f$1, this.f$2, var1, var2);
   }
}
