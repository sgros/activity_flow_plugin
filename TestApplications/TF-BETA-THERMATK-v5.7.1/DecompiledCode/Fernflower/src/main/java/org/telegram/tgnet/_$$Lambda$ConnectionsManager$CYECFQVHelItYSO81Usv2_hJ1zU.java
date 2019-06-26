package org.telegram.tgnet;

// $FF: synthetic class
public final class _$$Lambda$ConnectionsManager$CYECFQVHelItYSO81Usv2_hJ1zU implements Runnable {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final TLRPC.TL_config f$1;

   // $FF: synthetic method
   public _$$Lambda$ConnectionsManager$CYECFQVHelItYSO81Usv2_hJ1zU(int var1, TLRPC.TL_config var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      ConnectionsManager.lambda$onUpdateConfig$10(this.f$0, this.f$1);
   }
}
