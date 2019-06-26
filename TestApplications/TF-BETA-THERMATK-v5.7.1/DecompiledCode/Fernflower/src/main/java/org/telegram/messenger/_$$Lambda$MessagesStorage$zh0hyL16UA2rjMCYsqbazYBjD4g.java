package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$zh0hyL16UA2rjMCYsqbazYBjD4g implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.photos_Photos f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final long f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$zh0hyL16UA2rjMCYsqbazYBjD4g(MessagesStorage var1, TLRPC.photos_Photos var2, int var3, int var4, long var5, int var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$47$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
