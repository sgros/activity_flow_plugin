package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$Z9uUIzBqjWHBx6zjrK5T9uXZVkc implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.BotInfo f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$Z9uUIzBqjWHBx6zjrK5T9uXZVkc(DataQuery var1, TLRPC.BotInfo var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$putBotInfo$110$DataQuery(this.f$1);
   }
}
