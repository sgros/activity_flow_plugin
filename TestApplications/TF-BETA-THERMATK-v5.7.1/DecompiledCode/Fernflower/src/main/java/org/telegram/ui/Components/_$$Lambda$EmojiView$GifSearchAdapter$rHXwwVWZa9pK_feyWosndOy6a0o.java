package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$EmojiView$GifSearchAdapter$rHXwwVWZa9pK_feyWosndOy6a0o implements RequestDelegate {
   // $FF: synthetic field
   private final EmojiView.GifSearchAdapter f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getInlineBotResults f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$EmojiView$GifSearchAdapter$rHXwwVWZa9pK_feyWosndOy6a0o(EmojiView.GifSearchAdapter var1, TLRPC.TL_messages_getInlineBotResults var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$search$3$EmojiView$GifSearchAdapter(this.f$1, this.f$2, var1, var2);
   }
}
