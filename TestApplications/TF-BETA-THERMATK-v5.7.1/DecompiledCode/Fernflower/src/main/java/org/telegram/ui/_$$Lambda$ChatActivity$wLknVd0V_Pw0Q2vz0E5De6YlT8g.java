package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$wLknVd0V_Pw0Q2vz0E5De6YlT8g implements OnClickListener {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_game f$1;
   // $FF: synthetic field
   private final MessageObject f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final int f$4;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$wLknVd0V_Pw0Q2vz0E5De6YlT8g(ChatActivity var1, TLRPC.TL_game var2, MessageObject var3, String var4, int var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$showOpenGameAlert$83$ChatActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
