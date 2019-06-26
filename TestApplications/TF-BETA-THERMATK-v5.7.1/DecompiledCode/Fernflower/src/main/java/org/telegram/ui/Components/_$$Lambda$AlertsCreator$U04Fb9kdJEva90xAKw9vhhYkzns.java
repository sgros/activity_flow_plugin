package org.telegram.ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$U04Fb9kdJEva90xAKw9vhhYkzns implements OnClickListener {
   // $FF: synthetic field
   private final int[] f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final AlertDialog.Builder f$2;
   // $FF: synthetic field
   private final Runnable f$3;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$U04Fb9kdJEva90xAKw9vhhYkzns(int[] var1, int var2, AlertDialog.Builder var3, Runnable var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void onClick(View var1) {
      AlertsCreator.lambda$createPopupSelectDialog$36(this.f$0, this.f$1, this.f$2, this.f$3, var1);
   }
}
