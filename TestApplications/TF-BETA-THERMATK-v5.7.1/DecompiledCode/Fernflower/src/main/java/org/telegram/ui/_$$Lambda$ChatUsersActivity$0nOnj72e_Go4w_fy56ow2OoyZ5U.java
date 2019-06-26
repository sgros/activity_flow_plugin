package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatUsersActivity$0nOnj72e_Go4w_fy56ow2OoyZ5U implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
   // $FF: synthetic field
   private final ChatUsersActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$ChatUsersActivity$0nOnj72e_Go4w_fy56ow2OoyZ5U(ChatUsersActivity var1, int var2, int var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void didSetRights(int var1, TLRPC.TL_chatAdminRights var2, TLRPC.TL_chatBannedRights var3) {
      this.f$0.lambda$openRightsEdit2$6$ChatUsersActivity(this.f$1, this.f$2, this.f$3, var1, var2, var3);
   }
}
