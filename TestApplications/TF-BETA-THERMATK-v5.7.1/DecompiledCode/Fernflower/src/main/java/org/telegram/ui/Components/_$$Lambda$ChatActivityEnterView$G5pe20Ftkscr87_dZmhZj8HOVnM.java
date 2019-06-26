package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.DialogsActivity;

// $FF: synthetic class
public final class _$$Lambda$ChatActivityEnterView$G5pe20Ftkscr87_dZmhZj8HOVnM implements DialogsActivity.DialogsActivityDelegate {
   // $FF: synthetic field
   private final ChatActivityEnterView f$0;
   // $FF: synthetic field
   private final MessageObject f$1;
   // $FF: synthetic field
   private final TLRPC.KeyboardButton f$2;

   // $FF: synthetic method
   public _$$Lambda$ChatActivityEnterView$G5pe20Ftkscr87_dZmhZj8HOVnM(ChatActivityEnterView var1, MessageObject var2, TLRPC.KeyboardButton var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void didSelectDialogs(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
      this.f$0.lambda$didPressedBotButton$17$ChatActivityEnterView(this.f$1, this.f$2, var1, var2, var3, var4);
   }
}
