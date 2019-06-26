package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$7jw738vBdzQ8x4ji7riWY5uMZu8 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final Object[] f$3;
   // $FF: synthetic field
   private final CountDownLatch f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$7jw738vBdzQ8x4ji7riWY5uMZu8(MessagesStorage var1, String var2, int var3, Object[] var4, CountDownLatch var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$getSentFile$102$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
