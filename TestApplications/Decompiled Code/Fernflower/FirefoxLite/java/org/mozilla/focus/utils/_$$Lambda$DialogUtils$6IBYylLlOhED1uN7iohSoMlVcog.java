package org.mozilla.focus.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

// $FF: synthetic class
public final class _$$Lambda$DialogUtils$6IBYylLlOhED1uN7iohSoMlVcog implements OnCancelListener {
   // $FF: synthetic field
   private final Context f$0;

   // $FF: synthetic method
   public _$$Lambda$DialogUtils$6IBYylLlOhED1uN7iohSoMlVcog(Context var1) {
      this.f$0 = var1;
   }

   public final void onCancel(DialogInterface var1) {
      DialogUtils.lambda$showRateAppDialog$0(this.f$0, var1);
   }
}
