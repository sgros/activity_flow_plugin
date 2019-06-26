package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$L4Om6tbm2x7Gby_R4CSBHE04Yg4 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.photos_Photos f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$L4Om6tbm2x7Gby_R4CSBHE04Yg4(MessagesStorage var1, int var2, TLRPC.photos_Photos var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$putDialogPhotos$53$MessagesStorage(this.f$1, this.f$2);
   }
}
