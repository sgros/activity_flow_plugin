package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.CheckBoxCell;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$Ylfr0OF9_BXApgGlUw_IIZ6Lq80 implements OnClickListener {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final CheckBoxCell[] f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final TLRPC.TL_messages_requestUrlAuth f$3;
   // $FF: synthetic field
   private final TLRPC.TL_urlAuthResultRequest f$4;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$Ylfr0OF9_BXApgGlUw_IIZ6Lq80(ChatActivity var1, CheckBoxCell[] var2, String var3, TLRPC.TL_messages_requestUrlAuth var4, TLRPC.TL_urlAuthResultRequest var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$showRequestUrlAlert$90$ChatActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
