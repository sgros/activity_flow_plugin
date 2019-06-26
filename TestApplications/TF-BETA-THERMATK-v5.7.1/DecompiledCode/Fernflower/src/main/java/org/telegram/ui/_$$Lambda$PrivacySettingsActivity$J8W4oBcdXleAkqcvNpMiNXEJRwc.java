package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$PrivacySettingsActivity$J8W4oBcdXleAkqcvNpMiNXEJRwc implements RequestDelegate {
   // $FF: synthetic field
   private final PrivacySettingsActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.TL_account_setAccountTTL f$2;

   // $FF: synthetic method
   public _$$Lambda$PrivacySettingsActivity$J8W4oBcdXleAkqcvNpMiNXEJRwc(PrivacySettingsActivity var1, AlertDialog var2, TLRPC.TL_account_setAccountTTL var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$5$PrivacySettingsActivity(this.f$1, this.f$2, var1, var2);
   }
}
