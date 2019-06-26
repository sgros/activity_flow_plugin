package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$m9Jrl7_U__SUx_Gpw56kknTexVQ implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final LongSparseArray f$4;
   // $FF: synthetic field
   private final TLRPC.StickerSet f$5;
   // $FF: synthetic field
   private final TLRPC.TL_messages_allStickers f$6;
   // $FF: synthetic field
   private final int f$7;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$m9Jrl7_U__SUx_Gpw56kknTexVQ(DataQuery var1, TLObject var2, ArrayList var3, int var4, LongSparseArray var5, TLRPC.StickerSet var6, TLRPC.TL_messages_allStickers var7, int var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run() {
      this.f$0.lambda$null$31$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
