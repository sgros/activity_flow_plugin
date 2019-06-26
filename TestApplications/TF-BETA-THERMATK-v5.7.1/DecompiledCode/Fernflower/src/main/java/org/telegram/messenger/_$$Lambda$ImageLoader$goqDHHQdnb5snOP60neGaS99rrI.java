package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$ImageLoader$goqDHHQdnb5snOP60neGaS99rrI implements Runnable {
   // $FF: synthetic field
   private final ImageLoader f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final ImageLocation f$3;

   // $FF: synthetic method
   public _$$Lambda$ImageLoader$goqDHHQdnb5snOP60neGaS99rrI(ImageLoader var1, String var2, String var3, ImageLocation var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$replaceImageInCache$3$ImageLoader(this.f$1, this.f$2, this.f$3);
   }
}
