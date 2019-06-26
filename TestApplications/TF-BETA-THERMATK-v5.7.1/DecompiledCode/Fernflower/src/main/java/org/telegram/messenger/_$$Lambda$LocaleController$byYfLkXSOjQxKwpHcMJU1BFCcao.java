package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LocaleController$byYfLkXSOjQxKwpHcMJU1BFCcao implements Runnable {
   // $FF: synthetic field
   private final LocaleController f$0;
   // $FF: synthetic field
   private final LocaleController.LocaleInfo f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.TL_langPackDifference f$3;
   // $FF: synthetic field
   private final HashMap f$4;

   // $FF: synthetic method
   public _$$Lambda$LocaleController$byYfLkXSOjQxKwpHcMJU1BFCcao(LocaleController var1, LocaleController.LocaleInfo var2, int var3, TLRPC.TL_langPackDifference var4, HashMap var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$saveRemoteLocaleStrings$4$LocaleController(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
