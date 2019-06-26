package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$uwK8wtI_v95kHeLcf4gXEkrDfLc implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.Dialog f$1;
   // $FF: synthetic field
   private final TLRPC.InputPeer f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$uwK8wtI_v95kHeLcf4gXEkrDfLc(MessagesStorage var1, TLRPC.Dialog var2, TLRPC.InputPeer var3, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$10$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
