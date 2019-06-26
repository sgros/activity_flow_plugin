package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$NAWdcL37DBZ4gWGx1x8HuDp86gI implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.photos_Photos f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$NAWdcL37DBZ4gWGx1x8HuDp86gI(MessagesController var1, TLRPC.photos_Photos var2, boolean var3, int var4, int var5, int var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$processLoadedUserPhotos$59$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
