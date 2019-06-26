package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$tZ31OdkMW8PKLVCYnMiDI_DfRTs implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.InputPeer f$1;
   // $FF: synthetic field
   private final long f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$tZ31OdkMW8PKLVCYnMiDI_DfRTs(MessagesStorage var1, TLRPC.InputPeer var2, long var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$18$MessagesStorage(this.f$1, this.f$2);
   }
}
