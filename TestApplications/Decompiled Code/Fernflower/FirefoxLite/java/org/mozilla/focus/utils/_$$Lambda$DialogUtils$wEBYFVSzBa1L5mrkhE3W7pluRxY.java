package org.mozilla.focus.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$DialogUtils$wEBYFVSzBa1L5mrkhE3W7pluRxY implements OnClickListener {
   // $FF: synthetic field
   private final Context f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;

   // $FF: synthetic method
   public _$$Lambda$DialogUtils$wEBYFVSzBa1L5mrkhE3W7pluRxY(Context var1, AlertDialog var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(View var1) {
      DialogUtils.lambda$showRateAppDialog$2(this.f$0, this.f$1, var1);
   }
}
