package org.telegram.ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$ZO55ojXzRG9oFnxZDRUXz_6_1OA implements OnClickListener {
   // $FF: synthetic field
   private final AlertDialog[] f$0;
   // $FF: synthetic field
   private final Runnable f$1;
   // $FF: synthetic field
   private final AlertsCreator.AccountSelectDelegate f$2;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$ZO55ojXzRG9oFnxZDRUXz_6_1OA(AlertDialog[] var1, Runnable var2, AlertsCreator.AccountSelectDelegate var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(View var1) {
      AlertsCreator.lambda$createAccountSelectDialog$40(this.f$0, this.f$1, this.f$2, var1);
   }
}
