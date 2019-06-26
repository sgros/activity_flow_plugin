package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$EmojiView$GifSearchAdapter$T00z6XPCmLZsEfaqGzAOZW0Q_C8 implements Runnable {
   // $FF: synthetic field
   private final EmojiView.GifSearchAdapter f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getInlineBotResults f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final TLObject f$3;

   // $FF: synthetic method
   public _$$Lambda$EmojiView$GifSearchAdapter$T00z6XPCmLZsEfaqGzAOZW0Q_C8(EmojiView.GifSearchAdapter var1, TLRPC.TL_messages_getInlineBotResults var2, String var3, TLObject var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$2$EmojiView$GifSearchAdapter(this.f$1, this.f$2, this.f$3);
   }
}
