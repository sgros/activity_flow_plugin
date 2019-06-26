package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$Uzy9GRHh4_9QiCM66VVA1Ftqfj0 implements PhonebookSelectActivity.PhonebookSelectActivityDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final ChatActivity f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$Uzy9GRHh4_9QiCM66VVA1Ftqfj0(LaunchActivity var1, ChatActivity var2, int var3, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void didSelectContact(TLRPC.User var1) {
      this.f$0.lambda$didSelectDialogs$36$LaunchActivity(this.f$1, this.f$2, this.f$3, var1);
   }
}
