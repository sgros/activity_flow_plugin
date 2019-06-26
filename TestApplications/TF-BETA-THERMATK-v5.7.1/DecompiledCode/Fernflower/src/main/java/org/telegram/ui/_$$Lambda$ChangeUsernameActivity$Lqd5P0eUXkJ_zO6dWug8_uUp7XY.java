package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChangeUsernameActivity$Lqd5P0eUXkJ_zO6dWug8_uUp7XY implements Runnable {
   // $FF: synthetic field
   private final ChangeUsernameActivity f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLObject f$3;

   // $FF: synthetic method
   public _$$Lambda$ChangeUsernameActivity$Lqd5P0eUXkJ_zO6dWug8_uUp7XY(ChangeUsernameActivity var1, String var2, TLRPC.TL_error var3, TLObject var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$2$ChangeUsernameActivity(this.f$1, this.f$2, this.f$3);
   }
}
