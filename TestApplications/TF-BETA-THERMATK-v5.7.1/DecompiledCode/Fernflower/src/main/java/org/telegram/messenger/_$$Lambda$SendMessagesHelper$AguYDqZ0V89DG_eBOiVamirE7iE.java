package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$AguYDqZ0V89DG_eBOiVamirE7iE implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.Peer f$3;
   // $FF: synthetic field
   private final ArrayList f$4;
   // $FF: synthetic field
   private final long f$5;
   // $FF: synthetic field
   private final TLRPC.Message f$6;
   // $FF: synthetic field
   private final int f$7;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$AguYDqZ0V89DG_eBOiVamirE7iE(SendMessagesHelper var1, TLRPC.Message var2, int var3, TLRPC.Peer var4, ArrayList var5, long var6, TLRPC.Message var8, int var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var8;
      this.f$7 = var9;
   }

   public final void run() {
      this.f$0.lambda$null$6$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
