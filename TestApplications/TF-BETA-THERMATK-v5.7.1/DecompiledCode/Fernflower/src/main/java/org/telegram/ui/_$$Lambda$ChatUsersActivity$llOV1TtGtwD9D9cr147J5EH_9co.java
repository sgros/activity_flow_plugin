package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.ArrayList;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatUsersActivity$llOV1TtGtwD9D9cr147J5EH_9co implements OnClickListener {
   // $FF: synthetic field
   private final ChatUsersActivity f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final TLRPC.User f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final TLObject f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final TLRPC.TL_chatAdminRights f$7;
   // $FF: synthetic field
   private final TLRPC.TL_chatBannedRights f$8;

   // $FF: synthetic method
   public _$$Lambda$ChatUsersActivity$llOV1TtGtwD9D9cr147J5EH_9co(ChatUsersActivity var1, ArrayList var2, TLRPC.User var3, int var4, boolean var5, TLObject var6, int var7, TLRPC.TL_chatAdminRights var8, TLRPC.TL_chatBannedRights var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$createMenuForParticipant$9$ChatUsersActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, var1, var2);
   }
}
