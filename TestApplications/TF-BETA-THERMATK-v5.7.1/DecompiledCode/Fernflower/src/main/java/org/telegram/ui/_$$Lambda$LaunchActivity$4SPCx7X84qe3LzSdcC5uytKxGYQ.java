package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$4SPCx7X84qe3LzSdcC5uytKxGYQ implements DialogsActivity.DialogsActivityDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.User f$2;
   // $FF: synthetic field
   private final String f$3;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$4SPCx7X84qe3LzSdcC5uytKxGYQ(LaunchActivity var1, int var2, TLRPC.User var3, String var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void didSelectDialogs(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
      this.f$0.lambda$null$10$LaunchActivity(this.f$1, this.f$2, this.f$3, var1, var2, var3, var4);
   }
}
