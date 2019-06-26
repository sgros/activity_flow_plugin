package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LocaleController$OO_St8W4lBDCp1N4EzTA2EggA1M implements RequestDelegate {
   // $FF: synthetic field
   private final LocaleController f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$LocaleController$OO_St8W4lBDCp1N4EzTA2EggA1M(LocaleController var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadRemoteLanguages$6$LocaleController(this.f$1, var1, var2);
   }
}
