package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatUsersActivity$PIq1XQnFcSvW_MebsnvdhxTy3xQ implements OnClickListener {
   // $FF: synthetic field
   private final ChatUsersActivity f$0;
   // $FF: synthetic field
   private final CharSequence[] f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.TL_chatAdminRights f$3;
   // $FF: synthetic field
   private final TLObject f$4;
   // $FF: synthetic field
   private final TLRPC.TL_chatBannedRights f$5;

   // $FF: synthetic method
   public _$$Lambda$ChatUsersActivity$PIq1XQnFcSvW_MebsnvdhxTy3xQ(ChatUsersActivity var1, CharSequence[] var2, int var3, TLRPC.TL_chatAdminRights var4, TLObject var5, TLRPC.TL_chatBannedRights var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$createMenuForParticipant$14$ChatUsersActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, var1, var2);
   }
}
