package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivityEnterView$fmPI_spkDt8Wbdud0XG8IKtQnwY implements OnClickListener {
   // $FF: synthetic field
   private final ChatActivityEnterView f$0;
   // $FF: synthetic field
   private final MessageObject f$1;
   // $FF: synthetic field
   private final TLRPC.KeyboardButton f$2;

   // $FF: synthetic method
   public _$$Lambda$ChatActivityEnterView$fmPI_spkDt8Wbdud0XG8IKtQnwY(ChatActivityEnterView var1, MessageObject var2, TLRPC.KeyboardButton var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$didPressedBotButton$16$ChatActivityEnterView(this.f$1, this.f$2, var1, var2);
   }
}
