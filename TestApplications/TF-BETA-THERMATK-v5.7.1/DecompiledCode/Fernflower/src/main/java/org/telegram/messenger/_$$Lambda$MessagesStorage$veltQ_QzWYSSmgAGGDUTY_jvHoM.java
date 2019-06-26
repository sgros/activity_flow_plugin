package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$veltQ_QzWYSSmgAGGDUTY_jvHoM implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final TLRPC.InputChannel f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$veltQ_QzWYSSmgAGGDUTY_jvHoM(MessagesStorage var1, int var2, int var3, long var4, TLRPC.InputChannel var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$12$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
