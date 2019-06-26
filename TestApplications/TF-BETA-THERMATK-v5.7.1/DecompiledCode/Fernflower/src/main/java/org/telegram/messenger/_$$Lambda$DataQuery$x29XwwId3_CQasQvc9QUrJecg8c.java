package org.telegram.messenger;

import android.content.Context;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$x29XwwId3_CQasQvc9QUrJecg8c implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLRPC.StickerSet f$2;
   // $FF: synthetic field
   private final Context f$3;
   // $FF: synthetic field
   private final int f$4;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$x29XwwId3_CQasQvc9QUrJecg8c(DataQuery var1, TLRPC.TL_error var2, TLRPC.StickerSet var3, Context var4, int var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$45$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
