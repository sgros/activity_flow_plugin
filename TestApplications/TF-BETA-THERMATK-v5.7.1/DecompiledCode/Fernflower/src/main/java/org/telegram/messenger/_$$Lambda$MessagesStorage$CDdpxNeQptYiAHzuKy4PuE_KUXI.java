package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$CDdpxNeQptYiAHzuKy4PuE_KUXI implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$CDdpxNeQptYiAHzuKy4PuE_KUXI(MessagesStorage var1, TLRPC.Message var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$markMessageAsSendError$125$MessagesStorage(this.f$1);
   }
}
