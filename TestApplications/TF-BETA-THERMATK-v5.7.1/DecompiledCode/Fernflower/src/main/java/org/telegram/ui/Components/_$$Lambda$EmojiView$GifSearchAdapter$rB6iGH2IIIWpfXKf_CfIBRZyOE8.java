package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$EmojiView$GifSearchAdapter$rB6iGH2IIIWpfXKf_CfIBRZyOE8 implements RequestDelegate {
   // $FF: synthetic field
   private final EmojiView.GifSearchAdapter f$0;

   // $FF: synthetic method
   public _$$Lambda$EmojiView$GifSearchAdapter$rB6iGH2IIIWpfXKf_CfIBRZyOE8(EmojiView.GifSearchAdapter var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$searchBotUser$1$EmojiView$GifSearchAdapter(var1, var2);
   }
}
