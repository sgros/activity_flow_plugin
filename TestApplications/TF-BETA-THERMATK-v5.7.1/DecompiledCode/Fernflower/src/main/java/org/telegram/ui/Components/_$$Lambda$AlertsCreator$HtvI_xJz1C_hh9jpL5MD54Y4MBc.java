package org.telegram.ui.Components;

import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$HtvI_xJz1C_hh9jpL5MD54Y4MBc implements MessagesStorage.IntCallback {
   // $FF: synthetic field
   private final BaseFragment f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final TLRPC.Chat f$3;
   // $FF: synthetic field
   private final TLRPC.User f$4;
   // $FF: synthetic field
   private final boolean f$5;
   // $FF: synthetic field
   private final MessagesStorage.BooleanCallback f$6;
   // $FF: synthetic field
   private final boolean[] f$7;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$HtvI_xJz1C_hh9jpL5MD54Y4MBc(BaseFragment var1, boolean var2, boolean var3, TLRPC.Chat var4, TLRPC.User var5, boolean var6, MessagesStorage.BooleanCallback var7, boolean[] var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run(int var1) {
      AlertsCreator.lambda$null$10(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, var1);
   }
}
