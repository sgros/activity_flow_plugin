package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$FileLoadOperation$7ur7HYEmrvSDh4lwc7_mdrw1G5A implements Runnable {
   // $FF: synthetic field
   private final FileLoadOperation f$0;
   // $FF: synthetic field
   private final int[] f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final CountDownLatch f$4;

   // $FF: synthetic method
   public _$$Lambda$FileLoadOperation$7ur7HYEmrvSDh4lwc7_mdrw1G5A(FileLoadOperation var1, int[] var2, int var3, int var4, CountDownLatch var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$getDownloadedLengthFromOffset$1$FileLoadOperation(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}