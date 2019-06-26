package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatLinkActivity$tvkiY8dJp0f6xPm_0V9yT1Vcn74 implements OnClickListener {
   // $FF: synthetic field
   private final ChatLinkActivity f$0;
   // $FF: synthetic field
   private final TLRPC.ChatFull f$1;
   // $FF: synthetic field
   private final TLRPC.Chat f$2;

   // $FF: synthetic method
   public _$$Lambda$ChatLinkActivity$tvkiY8dJp0f6xPm_0V9yT1Vcn74(ChatLinkActivity var1, TLRPC.ChatFull var2, TLRPC.Chat var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$showLinkAlert$8$ChatLinkActivity(this.f$1, this.f$2, var1, var2);
   }
}
