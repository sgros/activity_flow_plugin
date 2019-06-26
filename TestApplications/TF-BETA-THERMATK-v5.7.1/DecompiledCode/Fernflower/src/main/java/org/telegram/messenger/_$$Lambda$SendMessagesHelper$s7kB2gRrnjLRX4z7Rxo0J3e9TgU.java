package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$s7kB2gRrnjLRX4z7Rxo0J3e9TgU implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$s7kB2gRrnjLRX4z7Rxo0J3e9TgU(SendMessagesHelper var1, TLRPC.Message var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$41$SendMessagesHelper(this.f$1, this.f$2);
   }
}
