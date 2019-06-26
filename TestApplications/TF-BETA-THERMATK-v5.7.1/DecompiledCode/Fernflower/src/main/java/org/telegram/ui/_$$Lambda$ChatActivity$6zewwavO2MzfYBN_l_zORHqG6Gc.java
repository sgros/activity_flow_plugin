package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessageObject;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$6zewwavO2MzfYBN_l_zORHqG6Gc implements OnClickListener {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final MessageObject f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$6zewwavO2MzfYBN_l_zORHqG6Gc(ChatActivity var1, MessageObject var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$shareMyContact$41$ChatActivity(this.f$1, var1, var2);
   }
}
