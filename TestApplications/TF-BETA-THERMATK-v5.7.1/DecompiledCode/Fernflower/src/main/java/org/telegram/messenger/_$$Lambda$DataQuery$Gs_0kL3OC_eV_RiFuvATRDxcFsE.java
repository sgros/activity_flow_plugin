package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$Gs_0kL3OC_eV_RiFuvATRDxcFsE implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_stickerSet f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$Gs_0kL3OC_eV_RiFuvATRDxcFsE(DataQuery var1, TLRPC.TL_messages_stickerSet var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$putSetToCache$9$DataQuery(this.f$1);
   }
}
