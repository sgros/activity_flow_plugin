package org.telegram.messenger;

import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$_wu1JxbPB7vnkTrFhWBgUdpKleg implements QuickAckDelegate {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$_wu1JxbPB7vnkTrFhWBgUdpKleg(SendMessagesHelper var1, TLRPC.Message var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$performSendMessageRequest$42$SendMessagesHelper(this.f$1);
   }
}
