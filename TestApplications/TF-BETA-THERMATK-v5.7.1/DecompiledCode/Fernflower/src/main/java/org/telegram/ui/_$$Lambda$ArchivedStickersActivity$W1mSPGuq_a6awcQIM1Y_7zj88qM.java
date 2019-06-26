package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ArchivedStickersActivity$W1mSPGuq_a6awcQIM1Y_7zj88qM implements Runnable {
   // $FF: synthetic field
   private final ArchivedStickersActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;

   // $FF: synthetic method
   public _$$Lambda$ArchivedStickersActivity$W1mSPGuq_a6awcQIM1Y_7zj88qM(ArchivedStickersActivity var1, TLRPC.TL_error var2, TLObject var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$1$ArchivedStickersActivity(this.f$1, this.f$2);
   }
}
