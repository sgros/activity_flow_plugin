package org.telegram.tgnet;

// $FF: synthetic class
public final class _$$Lambda$ConnectionsManager$wMpd1_zDWgiLp6x8fjImjIX349A implements Runnable {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$ConnectionsManager$wMpd1_zDWgiLp6x8fjImjIX349A(int var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      ConnectionsManager.lambda$onConnectionStateChanged$6(this.f$0, this.f$1);
   }
}
