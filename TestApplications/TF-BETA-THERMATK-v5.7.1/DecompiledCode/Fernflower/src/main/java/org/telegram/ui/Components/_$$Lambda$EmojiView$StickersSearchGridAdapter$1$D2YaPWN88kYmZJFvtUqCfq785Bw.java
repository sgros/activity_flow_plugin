package org.telegram.ui.Components;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$EmojiView$StickersSearchGridAdapter$1$D2YaPWN88kYmZJFvtUqCfq785Bw implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getStickers f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final LongSparseArray f$3;

   // $FF: synthetic method
   public _$$Lambda$EmojiView$StickersSearchGridAdapter$1$D2YaPWN88kYmZJFvtUqCfq785Bw(Object var1, TLRPC.TL_messages_getStickers var2, ArrayList var3, LongSparseArray var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$run$3$EmojiView$StickersSearchGridAdapter$1(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
