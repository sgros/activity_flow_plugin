package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$Lyh7x_fVJCtY78eBEW_nfqaECxE implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final TLRPC.Message f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$Lyh7x_fVJCtY78eBEW_nfqaECxE(SendMessagesHelper var1, boolean var2, ArrayList var3, TLRPC.Message var4, int var5, int var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$37$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
