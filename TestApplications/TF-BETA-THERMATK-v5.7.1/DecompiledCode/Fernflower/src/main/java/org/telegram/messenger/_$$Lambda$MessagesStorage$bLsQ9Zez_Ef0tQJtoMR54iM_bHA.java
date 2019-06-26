package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$bLsQ9Zez_Ef0tQJtoMR54iM_bHA implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$bLsQ9Zez_Ef0tQJtoMR54iM_bHA(MessagesStorage var1, long var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$removePendingTask$7$MessagesStorage(this.f$1);
   }
}
