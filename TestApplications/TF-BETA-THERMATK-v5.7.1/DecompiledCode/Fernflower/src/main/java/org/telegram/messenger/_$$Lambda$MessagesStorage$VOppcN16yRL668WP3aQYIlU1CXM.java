package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$VOppcN16yRL668WP3aQYIlU1CXM implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final CountDownLatch f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final boolean f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$VOppcN16yRL668WP3aQYIlU1CXM(MessagesStorage var1, int var2, CountDownLatch var3, boolean var4, boolean var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$loadChatInfo$84$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
