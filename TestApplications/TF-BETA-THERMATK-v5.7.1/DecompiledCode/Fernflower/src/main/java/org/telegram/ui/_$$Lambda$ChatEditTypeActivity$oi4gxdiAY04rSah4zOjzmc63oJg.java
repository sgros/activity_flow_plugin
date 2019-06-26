package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatEditTypeActivity$oi4gxdiAY04rSah4zOjzmc63oJg implements OnClickListener {
   // $FF: synthetic field
   private final ChatEditTypeActivity f$0;
   // $FF: synthetic field
   private final TLRPC.Chat f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatEditTypeActivity$oi4gxdiAY04rSah4zOjzmc63oJg(ChatEditTypeActivity var1, TLRPC.Chat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$13$ChatEditTypeActivity(this.f$1, var1, var2);
   }
}
