package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$19$1$20EqXlc4T1xja88z5UaATcwnhKs implements Runnable {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final TLRPC.TL_secureRequiredType f$3;
   // $FF: synthetic field
   private final PassportActivity.PassportActivityDelegate f$4;
   // $FF: synthetic field
   private final TLRPC.TL_error f$5;
   // $FF: synthetic field
   private final PassportActivity.ErrorRunnable f$6;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$19$1$20EqXlc4T1xja88z5UaATcwnhKs(Object var1, TLObject var2, String var3, TLRPC.TL_secureRequiredType var4, PassportActivity.PassportActivityDelegate var5, TLRPC.TL_error var6, PassportActivity.ErrorRunnable var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$1$PassportActivity$19$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
