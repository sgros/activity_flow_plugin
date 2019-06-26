package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$ziNY0SahWhkV1q9_2nyx_UlZ_Kc implements RequestDelegate {
   // $FF: synthetic field
   private final PassportActivity f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final PassportActivity.PassportActivityDelegate f$2;
   // $FF: synthetic field
   private final TLRPC.TL_account_sendVerifyPhoneCode f$3;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$ziNY0SahWhkV1q9_2nyx_UlZ_Kc(PassportActivity var1, String var2, PassportActivity.PassportActivityDelegate var3, TLRPC.TL_account_sendVerifyPhoneCode var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$startPhoneVerification$67$PassportActivity(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
