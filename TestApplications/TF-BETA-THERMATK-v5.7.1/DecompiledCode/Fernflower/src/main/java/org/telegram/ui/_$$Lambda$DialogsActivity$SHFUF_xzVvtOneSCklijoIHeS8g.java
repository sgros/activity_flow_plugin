package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g implements Runnable {
   // $FF: synthetic field
   private final DialogsActivity f$0;
   // $FF: synthetic field
   private final TLRPC.Chat f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final boolean f$3;

   // $FF: synthetic method
   public _$$Lambda$DialogsActivity$SHFUF_xzVvtOneSCklijoIHeS8g(DialogsActivity var1, TLRPC.Chat var2, long var3, boolean var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var5;
   }

   public final void run() {
      this.f$0.lambda$didReceivedNotification$17$DialogsActivity(this.f$1, this.f$2, this.f$3);
   }
}
