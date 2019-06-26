package org.telegram.ui.Components;

import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$DVGlkIX7hR4sQs3b_3TsrRLkHrw implements OnClickListener {
   // $FF: synthetic field
   private final int[] f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final SharedPreferences f$3;
   // $FF: synthetic field
   private final AlertDialog.Builder f$4;
   // $FF: synthetic field
   private final Runnable f$5;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$DVGlkIX7hR4sQs3b_3TsrRLkHrw(int[] var1, long var2, int var4, SharedPreferences var5, AlertDialog.Builder var6, Runnable var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
   }

   public final void onClick(View var1) {
      AlertsCreator.lambda$createPrioritySelectDialog$35(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, var1);
   }
}
