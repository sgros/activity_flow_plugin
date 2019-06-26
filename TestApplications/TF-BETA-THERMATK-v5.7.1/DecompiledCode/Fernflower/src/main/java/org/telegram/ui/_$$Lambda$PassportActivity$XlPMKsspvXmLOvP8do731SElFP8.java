package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$XlPMKsspvXmLOvP8do731SElFP8 implements Runnable {
   // $FF: synthetic field
   private final PassportActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final PassportActivity.PassportActivityDelegate f$3;
   // $FF: synthetic field
   private final TLObject f$4;
   // $FF: synthetic field
   private final TLRPC.TL_account_sendVerifyPhoneCode f$5;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$XlPMKsspvXmLOvP8do731SElFP8(PassportActivity var1, TLRPC.TL_error var2, String var3, PassportActivity.PassportActivityDelegate var4, TLObject var5, TLRPC.TL_account_sendVerifyPhoneCode var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$66$PassportActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
