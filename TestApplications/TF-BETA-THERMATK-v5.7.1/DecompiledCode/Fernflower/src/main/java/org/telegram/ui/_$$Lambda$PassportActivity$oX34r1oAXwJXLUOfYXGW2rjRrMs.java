package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$oX34r1oAXwJXLUOfYXGW2rjRrMs implements OnClickListener {
   // $FF: synthetic field
   private final PassportActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_secureRequiredType f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final boolean f$3;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$oX34r1oAXwJXLUOfYXGW2rjRrMs(PassportActivity var1, TLRPC.TL_secureRequiredType var2, ArrayList var3, boolean var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$62$PassportActivity(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
