package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.TextCheckCell;

// $FF: synthetic class
public final class _$$Lambda$PrivacySettingsActivity$9W_WSazbZdTZMbEaunliHg9yKU0 implements RequestDelegate {
   // $FF: synthetic field
   private final PrivacySettingsActivity f$0;
   // $FF: synthetic field
   private final TextCheckCell f$1;

   // $FF: synthetic method
   public _$$Lambda$PrivacySettingsActivity$9W_WSazbZdTZMbEaunliHg9yKU0(PrivacySettingsActivity var1, TextCheckCell var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$10$PrivacySettingsActivity(this.f$1, var1, var2);
   }
}
