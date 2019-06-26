package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$oDX4i73L5lyJayv_jik2z2hGP7Q implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final ArrayList f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final String f$6;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$oDX4i73L5lyJayv_jik2z2hGP7Q(SendMessagesHelper var1, TLRPC.Message var2, int var3, boolean var4, ArrayList var5, int var6, String var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$38$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
