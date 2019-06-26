package org.telegram.tgnet;

// $FF: synthetic class
public final class _$$Lambda$ConnectionsManager$R5V1iXmwj8PWON_tb_jcTaBhzJo implements Runnable {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final TLObject f$1;

   // $FF: synthetic method
   public _$$Lambda$ConnectionsManager$R5V1iXmwj8PWON_tb_jcTaBhzJo(int var1, TLObject var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      ConnectionsManager.lambda$onUnparsedMessageReceived$3(this.f$0, this.f$1);
   }
}
