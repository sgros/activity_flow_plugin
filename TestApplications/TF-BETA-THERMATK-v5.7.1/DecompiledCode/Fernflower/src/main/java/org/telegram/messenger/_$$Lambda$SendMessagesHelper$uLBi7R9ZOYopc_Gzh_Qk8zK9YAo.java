package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$uLBi7R9ZOYopc_Gzh_Qk8zK9YAo implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final ArrayList f$3;
   // $FF: synthetic field
   private final ArrayList f$4;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$uLBi7R9ZOYopc_Gzh_Qk8zK9YAo(SendMessagesHelper var1, ArrayList var2, ArrayList var3, ArrayList var4, ArrayList var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$processUnsentMessages$43$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
