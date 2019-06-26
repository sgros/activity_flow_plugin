package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$UNG3O_q0_n6IbfxTyxeEDWN4wjc implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$UNG3O_q0_n6IbfxTyxeEDWN4wjc(MessagesStorage var1, ArrayList var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$unpinAllDialogsExceptNew$145$MessagesStorage(this.f$1, this.f$2);
   }
}
