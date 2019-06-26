package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$TqvcAvYPfh_Cio_d66SYceSFjMQ implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.InputChannel f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final long f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$TqvcAvYPfh_Cio_d66SYceSFjMQ(MessagesStorage var1, int var2, int var3, TLRPC.InputChannel var4, int var5, long var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$15$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
