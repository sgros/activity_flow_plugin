package org.telegram.ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$NzxH3UcpchY_thDrzzdwxnXbOG4 implements OnClickListener {
   // $FF: synthetic field
   private final AlertDialog.Builder f$0;
   // $FF: synthetic field
   private final android.content.DialogInterface.OnClickListener f$1;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$NzxH3UcpchY_thDrzzdwxnXbOG4(AlertDialog.Builder var1, android.content.DialogInterface.OnClickListener var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(View var1) {
      AlertsCreator.lambda$createSingleChoiceDialog$37(this.f$0, this.f$1, var1);
   }
}
