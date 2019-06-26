package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$VJV5wRfpqB6jJ8dEtoqePT3HaYk implements RequestDelegate {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final byte[] f$2;
   // $FF: synthetic field
   private final TLRPC.TL_account_updatePasswordSettings f$3;
   // $FF: synthetic field
   private final String f$4;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$VJV5wRfpqB6jJ8dEtoqePT3HaYk(TwoStepVerificationActivity var1, boolean var2, byte[] var3, TLRPC.TL_account_updatePasswordSettings var4, String var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$24$TwoStepVerificationActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
