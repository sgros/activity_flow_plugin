package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$CTc5p_O22dGYncyM4gxumAZNVws implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final TLRPC.updates_Difference f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$CTc5p_O22dGYncyM4gxumAZNVws(MessagesController var1, ArrayList var2, TLRPC.updates_Difference var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$211$MessagesController(this.f$1, this.f$2);
   }
}
