package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$ImageLoader$QQrxxTOTOPgi4Ibzj2dcFh6tMmY implements Runnable {
   // $FF: synthetic field
   private final ImageLoader f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final ImageReceiver f$2;

   // $FF: synthetic method
   public _$$Lambda$ImageLoader$QQrxxTOTOPgi4Ibzj2dcFh6tMmY(ImageLoader var1, boolean var2, ImageReceiver var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$cancelLoadingForImageReceiver$2$ImageLoader(this.f$1, this.f$2);
   }
}
