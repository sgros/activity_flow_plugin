package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessageObject;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$Tr_7Iy8c3EReHr8wdqnTYAGvNzs implements OnClickListener {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final MessageObject f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$Tr_7Iy8c3EReHr8wdqnTYAGvNzs(ChatActivity var1, MessageObject var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$processSelectedOption$81$ChatActivity(this.f$1, var1, var2);
   }
}
