package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$iWlc9ep_wbwBIz7hlp_XPY9A0O0 implements OnClickListener {
   // $FF: synthetic field
   private final ProfileActivity f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final TLRPC.ChannelParticipant f$2;
   // $FF: synthetic field
   private final TLRPC.ChatParticipant f$3;
   // $FF: synthetic field
   private final TLRPC.User f$4;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$iWlc9ep_wbwBIz7hlp_XPY9A0O0(ProfileActivity var1, ArrayList var2, TLRPC.ChannelParticipant var3, TLRPC.ChatParticipant var4, TLRPC.User var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$5$ProfileActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
