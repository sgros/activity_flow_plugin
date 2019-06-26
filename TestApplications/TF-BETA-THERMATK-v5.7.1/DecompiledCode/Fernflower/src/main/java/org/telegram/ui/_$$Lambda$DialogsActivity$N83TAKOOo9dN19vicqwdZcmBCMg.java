package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DialogsActivity$N83TAKOOo9dN19vicqwdZcmBCMg implements MessagesStorage.BooleanCallback {
   // $FF: synthetic field
   private final DialogsActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.Chat f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final boolean f$4;

   // $FF: synthetic method
   public _$$Lambda$DialogsActivity$N83TAKOOo9dN19vicqwdZcmBCMg(DialogsActivity var1, int var2, TLRPC.Chat var3, long var4, boolean var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var6;
   }

   public final void run(boolean var1) {
      this.f$0.lambda$perfromSelectedDialogsAction$14$DialogsActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1);
   }
}
