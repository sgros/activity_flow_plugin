package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$g5fV5AzEjpYa9k_FQikVmx9CWxY implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final TLRPC.Message f$2;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$g5fV5AzEjpYa9k_FQikVmx9CWxY(DataQuery var1, long var2, TLRPC.Message var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
   }

   public final void run() {
      this.f$0.lambda$putBotKeyboard$109$DataQuery(this.f$1, this.f$2);
   }
}
