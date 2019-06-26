package org.telegram.messenger;

import android.util.LongSparseArray;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$Gpj0Tfm4ESrlwMoiHamza4rhl1c implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final LongSparseArray f$1;
   // $FF: synthetic field
   private final LongSparseArray f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$Gpj0Tfm4ESrlwMoiHamza4rhl1c(MessagesController var1, LongSparseArray var2, LongSparseArray var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$updatePrintingStrings$97$MessagesController(this.f$1, this.f$2);
   }
}
