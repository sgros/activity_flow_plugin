package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$3$H95RgqCbEw0JF2FdjyTyxP1Zzp8 implements Runnable {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final Runnable f$2;
   // $FF: synthetic field
   private final PassportActivity.ErrorRunnable f$3;
   // $FF: synthetic field
   private final TLRPC.TL_account_verifyEmail f$4;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$3$H95RgqCbEw0JF2FdjyTyxP1Zzp8(Object var1, TLRPC.TL_error var2, Runnable var3, PassportActivity.ErrorRunnable var4, TLRPC.TL_account_verifyEmail var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$5$PassportActivity$3(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
