package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$0gsEtoIKwqYGtFeFeKyba_2TLjY implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final boolean[] f$3;
   // $FF: synthetic field
   private final CountDownLatch f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$0gsEtoIKwqYGtFeFeKyba_2TLjY(MessagesStorage var1, long var2, int var4, boolean[] var5, CountDownLatch var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
   }

   public final void run() {
      this.f$0.lambda$checkMessageId$94$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
