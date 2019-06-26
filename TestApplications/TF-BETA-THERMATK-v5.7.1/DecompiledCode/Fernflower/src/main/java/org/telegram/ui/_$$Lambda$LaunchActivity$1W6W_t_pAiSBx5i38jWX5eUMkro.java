package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$1W6W_t_pAiSBx5i38jWX5eUMkro implements RequestDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$1W6W_t_pAiSBx5i38jWX5eUMkro(LaunchActivity var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$checkAppUpdate$35$LaunchActivity(this.f$1, var1, var2);
   }
}
