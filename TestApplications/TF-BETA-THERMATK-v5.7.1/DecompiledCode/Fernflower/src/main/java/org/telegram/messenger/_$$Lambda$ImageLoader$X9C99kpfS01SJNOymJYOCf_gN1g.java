package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$ImageLoader$X9C99kpfS01SJNOymJYOCf_gN1g implements Runnable {
   // $FF: synthetic field
   private final ImageLoader f$0;
   // $FF: synthetic field
   private final ImageLoader.HttpFileTask f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$ImageLoader$X9C99kpfS01SJNOymJYOCf_gN1g(ImageLoader var1, ImageLoader.HttpFileTask var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$runHttpFileLoadTasks$11$ImageLoader(this.f$1, this.f$2);
   }
}
