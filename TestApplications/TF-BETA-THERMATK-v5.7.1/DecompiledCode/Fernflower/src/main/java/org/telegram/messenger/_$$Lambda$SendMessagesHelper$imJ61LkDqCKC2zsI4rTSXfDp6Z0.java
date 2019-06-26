package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$imJ61LkDqCKC2zsI4rTSXfDp6Z0 implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final TLRPC.Message f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$imJ61LkDqCKC2zsI4rTSXfDp6Z0(SendMessagesHelper var1, TLRPC.Message var2, long var3, int var5, TLRPC.Message var6, int var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$5$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
