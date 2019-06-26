package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$g2LSO2I4rkrZ9986IhA1PoJ9pZ0 implements OnClickListener {
   // $FF: synthetic field
   private final TLRPC.User f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final boolean[] f$3;
   // $FF: synthetic field
   private final BaseFragment f$4;
   // $FF: synthetic field
   private final boolean f$5;
   // $FF: synthetic field
   private final boolean f$6;
   // $FF: synthetic field
   private final TLRPC.Chat f$7;
   // $FF: synthetic field
   private final boolean f$8;
   // $FF: synthetic field
   private final MessagesStorage.BooleanCallback f$9;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$g2LSO2I4rkrZ9986IhA1PoJ9pZ0(TLRPC.User var1, boolean var2, boolean var3, boolean[] var4, BaseFragment var5, boolean var6, boolean var7, TLRPC.Chat var8, boolean var9, MessagesStorage.BooleanCallback var10) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
      this.f$9 = var10;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createClearOrDeleteDialogAlert$11(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, var1, var2);
   }
}
