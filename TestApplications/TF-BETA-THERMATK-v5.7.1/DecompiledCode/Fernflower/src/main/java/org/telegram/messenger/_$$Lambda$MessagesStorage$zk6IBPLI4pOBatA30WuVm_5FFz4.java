package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$zk6IBPLI4pOBatA30WuVm_5FFz4 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final TLRPC.TL_poll f$2;
   // $FF: synthetic field
   private final TLRPC.TL_pollResults f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$zk6IBPLI4pOBatA30WuVm_5FFz4(MessagesStorage var1, long var2, TLRPC.TL_poll var4, TLRPC.TL_pollResults var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
   }

   public final void run() {
      this.f$0.lambda$updateMessagePollResults$56$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
