package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$w1XixRjF16wrwBdg1yg9ZISWPKs implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final LongSparseArray f$3;
   // $FF: synthetic field
   private final TLRPC.StickerSet f$4;
   // $FF: synthetic field
   private final TLRPC.TL_messages_allStickers f$5;
   // $FF: synthetic field
   private final int f$6;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$w1XixRjF16wrwBdg1yg9ZISWPKs(DataQuery var1, ArrayList var2, int var3, LongSparseArray var4, TLRPC.StickerSet var5, TLRPC.TL_messages_allStickers var6, int var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$processLoadStickersResponse$32$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, var1, var2);
   }
}
