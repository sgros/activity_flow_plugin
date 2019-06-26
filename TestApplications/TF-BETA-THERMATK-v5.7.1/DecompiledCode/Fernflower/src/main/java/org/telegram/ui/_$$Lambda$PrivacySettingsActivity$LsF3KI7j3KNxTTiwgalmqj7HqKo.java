package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$PrivacySettingsActivity$LsF3KI7j3KNxTTiwgalmqj7HqKo implements Runnable {
   // $FF: synthetic field
   private final PrivacySettingsActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final TLRPC.TL_account_setAccountTTL f$3;

   // $FF: synthetic method
   public _$$Lambda$PrivacySettingsActivity$LsF3KI7j3KNxTTiwgalmqj7HqKo(PrivacySettingsActivity var1, AlertDialog var2, TLObject var3, TLRPC.TL_account_setAccountTTL var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$4$PrivacySettingsActivity(this.f$1, this.f$2, this.f$3);
   }
}
