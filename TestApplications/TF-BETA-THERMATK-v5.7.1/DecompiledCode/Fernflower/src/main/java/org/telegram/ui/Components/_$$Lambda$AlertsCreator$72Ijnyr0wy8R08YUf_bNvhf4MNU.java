package org.telegram.ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$72Ijnyr0wy8R08YUf_bNvhf4MNU implements OnClickListener {
   // $FF: synthetic field
   private final long f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final Context f$3;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$72Ijnyr0wy8R08YUf_bNvhf4MNU(long var1, int var3, BaseFragment var4, Context var5) {
      this.f$0 = var1;
      this.f$1 = var3;
      this.f$2 = var4;
      this.f$3 = var5;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createReportAlert$21(this.f$0, this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
