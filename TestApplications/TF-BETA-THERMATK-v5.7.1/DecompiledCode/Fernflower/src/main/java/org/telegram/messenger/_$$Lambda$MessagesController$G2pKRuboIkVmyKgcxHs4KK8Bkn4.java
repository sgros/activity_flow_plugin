package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$G2pKRuboIkVmyKgcxHs4KK8Bkn4 implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_wallPaperSettings f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$G2pKRuboIkVmyKgcxHs4KK8Bkn4(MessagesController var1, TLRPC.TL_wallPaperSettings var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$didReceivedNotification$6$MessagesController(this.f$1, var1, var2);
   }
}
