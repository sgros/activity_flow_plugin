package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivityEnterView$b_Y8EmOjKWjLQcXw9E16k20A4eo implements BotKeyboardView.BotKeyboardViewDelegate {
   // $FF: synthetic field
   private final ChatActivityEnterView f$0;

   // $FF: synthetic method
   public _$$Lambda$ChatActivityEnterView$b_Y8EmOjKWjLQcXw9E16k20A4eo(ChatActivityEnterView var1) {
      this.f$0 = var1;
   }

   public final void didPressedButton(TLRPC.KeyboardButton var1) {
      this.f$0.lambda$setButtons$15$ChatActivityEnterView(var1);
   }
}
