package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$QONFZUnsCsoEQ085dztQiYr_zvw implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
   // $FF: synthetic field
   private final ProfileActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.ChatParticipant f$2;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$QONFZUnsCsoEQ085dztQiYr_zvw(ProfileActivity var1, int var2, TLRPC.ChatParticipant var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void didSetRights(int var1, TLRPC.TL_chatAdminRights var2, TLRPC.TL_chatBannedRights var3) {
      this.f$0.lambda$openRightsEdit$13$ProfileActivity(this.f$1, this.f$2, var1, var2, var3);
   }
}
