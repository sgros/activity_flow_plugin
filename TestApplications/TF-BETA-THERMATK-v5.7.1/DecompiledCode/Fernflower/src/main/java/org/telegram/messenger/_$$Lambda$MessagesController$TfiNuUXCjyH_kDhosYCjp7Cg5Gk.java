package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$TfiNuUXCjyH_kDhosYCjp7Cg5Gk implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final BaseFragment f$1;
   // $FF: synthetic field
   private final TLRPC.TL_channels_inviteToChannel f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$TfiNuUXCjyH_kDhosYCjp7Cg5Gk(MessagesController var1, BaseFragment var2, TLRPC.TL_channels_inviteToChannel var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$addUsersToChannel$167$MessagesController(this.f$1, this.f$2, var1, var2);
   }
}
