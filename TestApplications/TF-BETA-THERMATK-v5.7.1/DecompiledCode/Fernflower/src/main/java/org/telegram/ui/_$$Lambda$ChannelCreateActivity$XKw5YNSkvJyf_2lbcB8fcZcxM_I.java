package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChannelCreateActivity$XKw5YNSkvJyf_2lbcB8fcZcxM_I implements OnClickListener {
   // $FF: synthetic field
   private final ChannelCreateActivity f$0;
   // $FF: synthetic field
   private final TLRPC.Chat f$1;

   // $FF: synthetic method
   public _$$Lambda$ChannelCreateActivity$XKw5YNSkvJyf_2lbcB8fcZcxM_I(ChannelCreateActivity var1, TLRPC.Chat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$14$ChannelCreateActivity(this.f$1, var1, var2);
   }
}
