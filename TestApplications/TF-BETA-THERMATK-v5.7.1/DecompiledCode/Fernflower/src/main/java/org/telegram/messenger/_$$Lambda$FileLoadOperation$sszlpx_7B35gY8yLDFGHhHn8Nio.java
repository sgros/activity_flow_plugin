package org.telegram.messenger;

import java.io.File;
import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$FileLoadOperation$sszlpx_7B35gY8yLDFGHhHn8Nio implements Runnable {
   // $FF: synthetic field
   private final FileLoadOperation f$0;
   // $FF: synthetic field
   private final File[] f$1;
   // $FF: synthetic field
   private final CountDownLatch f$2;

   // $FF: synthetic method
   public _$$Lambda$FileLoadOperation$sszlpx_7B35gY8yLDFGHhHn8Nio(FileLoadOperation var1, File[] var2, CountDownLatch var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$getCurrentFile$0$FileLoadOperation(this.f$1, this.f$2);
   }
}
