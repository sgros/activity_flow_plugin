package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$3$_Z500k40kXIJwsW8EZFrcODEnk4 implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final Runnable f$1;
   // $FF: synthetic field
   private final PassportActivity.ErrorRunnable f$2;
   // $FF: synthetic field
   private final TLRPC.TL_account_verifyEmail f$3;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$3$_Z500k40kXIJwsW8EZFrcODEnk4(Object var1, Runnable var2, PassportActivity.ErrorRunnable var3, TLRPC.TL_account_verifyEmail var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onItemClick$6$PassportActivity$3(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
