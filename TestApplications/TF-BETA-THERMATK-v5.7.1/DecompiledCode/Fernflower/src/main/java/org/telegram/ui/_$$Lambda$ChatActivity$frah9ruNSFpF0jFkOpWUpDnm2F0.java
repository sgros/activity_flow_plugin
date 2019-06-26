package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$frah9ruNSFpF0jFkOpWUpDnm2F0 implements RequestDelegate {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final AlertDialog[] f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_editMessage f$2;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$frah9ruNSFpF0jFkOpWUpDnm2F0(ChatActivity var1, AlertDialog[] var2, TLRPC.TL_messages_editMessage var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$78$ChatActivity(this.f$1, this.f$2, var1, var2);
   }
}
