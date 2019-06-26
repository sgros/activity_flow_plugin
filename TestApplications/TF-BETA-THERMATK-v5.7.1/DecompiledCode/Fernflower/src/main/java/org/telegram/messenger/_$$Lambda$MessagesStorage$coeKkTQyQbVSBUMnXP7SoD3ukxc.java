package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$coeKkTQyQbVSBUMnXP7SoD3ukxc implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.Chat[] f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final CountDownLatch f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$coeKkTQyQbVSBUMnXP7SoD3ukxc(MessagesStorage var1, TLRPC.Chat[] var2, int var3, CountDownLatch var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$getChatSync$152$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
