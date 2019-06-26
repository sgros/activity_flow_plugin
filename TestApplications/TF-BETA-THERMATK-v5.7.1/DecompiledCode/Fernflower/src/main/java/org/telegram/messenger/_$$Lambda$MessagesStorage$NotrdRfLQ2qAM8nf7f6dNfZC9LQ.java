package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$NotrdRfLQ2qAM8nf7f6dNfZC9LQ implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final Integer[] f$3;
   // $FF: synthetic field
   private final CountDownLatch f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$NotrdRfLQ2qAM8nf7f6dNfZC9LQ(MessagesStorage var1, boolean var2, long var3, Integer[] var5, CountDownLatch var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var5;
      this.f$4 = var6;
   }

   public final void run() {
      this.f$0.lambda$getDialogReadMax$149$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
