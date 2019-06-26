package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$tWVKNMhPWigeKF9TDwsCxxYPaU0 implements Runnable {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final byte[] f$4;
   // $FF: synthetic field
   private final TLRPC.TL_account_updatePasswordSettings f$5;
   // $FF: synthetic field
   private final String f$6;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$tWVKNMhPWigeKF9TDwsCxxYPaU0(TwoStepVerificationActivity var1, TLRPC.TL_error var2, boolean var3, TLObject var4, byte[] var5, TLRPC.TL_account_updatePasswordSettings var6, String var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$23$TwoStepVerificationActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
