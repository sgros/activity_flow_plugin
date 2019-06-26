package org.telegram.ui;

import android.content.Intent;
import org.telegram.ui.Components.AlertsCreator;

// $FF: synthetic class
public final class _$$Lambda$ExternalActionActivity$7MenW_e7ZVDhaPYTW6k7H5_jjBA implements AlertsCreator.AccountSelectDelegate {
   // $FF: synthetic field
   private final ExternalActionActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final Intent f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final boolean f$5;

   // $FF: synthetic method
   public _$$Lambda$ExternalActionActivity$7MenW_e7ZVDhaPYTW6k7H5_jjBA(ExternalActionActivity var1, int var2, Intent var3, boolean var4, boolean var5, boolean var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void didSelectAccount(int var1) {
      this.f$0.lambda$handleIntent$3$ExternalActionActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, var1);
   }
}
