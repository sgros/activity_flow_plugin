package org.telegram.ui.Components;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$EmojiView$StickersSearchGridAdapter$1$0O6pCOi0jii8pwBAvqjd7az_Jp8 implements Runnable {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getStickers f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final ArrayList f$3;
   // $FF: synthetic field
   private final LongSparseArray f$4;

   // $FF: synthetic method
   public _$$Lambda$EmojiView$StickersSearchGridAdapter$1$0O6pCOi0jii8pwBAvqjd7az_Jp8(Object var1, TLRPC.TL_messages_getStickers var2, TLObject var3, ArrayList var4, LongSparseArray var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$2$EmojiView$StickersSearchGridAdapter$1(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
