package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$_TcC0mAoIoAzht6PKxrSMA0GNwQ implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.StickerSet f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$_TcC0mAoIoAzht6PKxrSMA0GNwQ(DataQuery var1, TLRPC.StickerSet var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$loadGroupStickerSet$6$DataQuery(this.f$1);
   }
}
