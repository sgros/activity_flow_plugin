package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileRefController$FlgFGmJyAwG8D7Z8OYWnK63ajJo implements Runnable {
   // $FF: synthetic field
   private final FileRefController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_stickerSet f$1;

   // $FF: synthetic method
   public _$$Lambda$FileRefController$FlgFGmJyAwG8D7Z8OYWnK63ajJo(FileRefController var1, TLRPC.TL_messages_stickerSet var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onRequestComplete$24$FileRefController(this.f$1);
   }
}
