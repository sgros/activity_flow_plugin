package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$PTatBcwlbxdtRxQAlB57_yJjqhU implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final TLRPC.TL_channels_editBanned f$3;
   // $FF: synthetic field
   private final boolean f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$PTatBcwlbxdtRxQAlB57_yJjqhU(MessagesController var1, int var2, BaseFragment var3, TLRPC.TL_channels_editBanned var4, boolean var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$setUserBannedRole$42$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
