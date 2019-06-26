package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$AuH1gRs2iXmoSCC0c3xvFDAEwcM implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final TLRPC.InputPeer f$3;
   // $FF: synthetic field
   private final long f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$AuH1gRs2iXmoSCC0c3xvFDAEwcM(MessagesStorage var1, long var2, boolean var4, TLRPC.InputPeer var5, long var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$11$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
