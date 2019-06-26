package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatUsersActivity$s_tj1rO5rel9STPZNYUOQpZ18CQ implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
   // $FF: synthetic field
   private final ChatUsersActivity f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$ChatUsersActivity$s_tj1rO5rel9STPZNYUOQpZ18CQ(ChatUsersActivity var1, TLObject var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void didSetRights(int var1, TLRPC.TL_chatAdminRights var2, TLRPC.TL_chatBannedRights var3) {
      this.f$0.lambda$openRightsEdit$7$ChatUsersActivity(this.f$1, this.f$2, var1, var2, var3);
   }
}
