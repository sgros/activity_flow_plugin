package org.telegram.messenger;

import android.content.Context;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$DpM7Y4tX6aFYwIY3CiDaFq5CjNE implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.StickerSet f$1;
   // $FF: synthetic field
   private final Context f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$DpM7Y4tX6aFYwIY3CiDaFq5CjNE(DataQuery var1, TLRPC.StickerSet var2, Context var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$removeStickersSet$46$DataQuery(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
