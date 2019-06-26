package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$JHJ_M64u5jSxCVF9v5FzZPiyZZ4 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_updateLangPack f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$JHJ_M64u5jSxCVF9v5FzZPiyZZ4(MessagesController var1, TLRPC.TL_updateLangPack var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$processUpdateArray$243$MessagesController(this.f$1);
   }
}
