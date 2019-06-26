package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$ScXQOgmc_sCdZ0TlvzYeQcRtFgQ implements Runnable {
   // $FF: synthetic field
   private final MessageObject f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.TL_document f$2;
   // $FF: synthetic field
   private final MessageObject f$3;
   // $FF: synthetic field
   private final HashMap f$4;
   // $FF: synthetic field
   private final String f$5;
   // $FF: synthetic field
   private final long f$6;
   // $FF: synthetic field
   private final MessageObject f$7;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$ScXQOgmc_sCdZ0TlvzYeQcRtFgQ(MessageObject var1, int var2, TLRPC.TL_document var3, MessageObject var4, HashMap var5, String var6, long var7, MessageObject var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var9;
   }

   public final void run() {
      SendMessagesHelper.lambda$null$45(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
