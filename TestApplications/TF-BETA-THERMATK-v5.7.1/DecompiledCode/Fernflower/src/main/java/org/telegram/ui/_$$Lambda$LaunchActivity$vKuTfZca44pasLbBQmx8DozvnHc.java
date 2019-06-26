package org.telegram.ui;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$vKuTfZca44pasLbBQmx8DozvnHc implements LocationActivity.LocationActivityDelegate {
   // $FF: synthetic field
   private final HashMap f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$vKuTfZca44pasLbBQmx8DozvnHc(HashMap var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void didSelectLocation(TLRPC.MessageMedia var1, int var2) {
      LaunchActivity.lambda$null$42(this.f$0, this.f$1, var1, var2);
   }
}
