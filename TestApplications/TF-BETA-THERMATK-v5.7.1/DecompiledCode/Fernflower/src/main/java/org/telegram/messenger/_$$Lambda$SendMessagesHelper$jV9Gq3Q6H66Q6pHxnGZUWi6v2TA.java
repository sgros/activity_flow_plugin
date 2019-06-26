package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$jV9Gq3Q6H66Q6pHxnGZUWi6v2TA implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final int f$4;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$jV9Gq3Q6H66Q6pHxnGZUWi6v2TA(SendMessagesHelper var1, TLRPC.Message var2, int var3, long var4, int var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$25$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
