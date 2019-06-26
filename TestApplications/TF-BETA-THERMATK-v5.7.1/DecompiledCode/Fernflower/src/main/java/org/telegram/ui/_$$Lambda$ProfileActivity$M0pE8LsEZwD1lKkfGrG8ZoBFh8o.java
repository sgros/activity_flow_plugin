package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$M0pE8LsEZwD1lKkfGrG8ZoBFh8o implements OnClickListener {
   // $FF: synthetic field
   private final ProfileActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.User f$2;
   // $FF: synthetic field
   private final TLRPC.ChatParticipant f$3;
   // $FF: synthetic field
   private final TLRPC.ChannelParticipant f$4;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$M0pE8LsEZwD1lKkfGrG8ZoBFh8o(ProfileActivity var1, int var2, TLRPC.User var3, TLRPC.ChatParticipant var4, TLRPC.ChannelParticipant var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$4$ProfileActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
