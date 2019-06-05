package org.mozilla.focus.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$DialogUtils$28dtCamIzRB4NQvWqNCsRNPQFa4 implements OnClickListener {
   // $FF: synthetic field
   private final AlertDialog f$0;
   // $FF: synthetic field
   private final Context f$1;

   // $FF: synthetic method
   public _$$Lambda$DialogUtils$28dtCamIzRB4NQvWqNCsRNPQFa4(AlertDialog var1, Context var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(View var1) {
      DialogUtils.lambda$showShareAppDialog$5(this.f$0, this.f$1, var1);
   }
}
