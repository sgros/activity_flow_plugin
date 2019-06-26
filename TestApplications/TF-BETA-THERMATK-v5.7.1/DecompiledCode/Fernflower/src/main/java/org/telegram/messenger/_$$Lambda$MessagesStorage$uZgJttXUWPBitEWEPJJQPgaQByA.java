package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$uZgJttXUWPBitEWEPJJQPgaQByA implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final long f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$uZgJttXUWPBitEWEPJJQPgaQByA(MessagesStorage var1, long var2, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
   }

   public final void run() {
      this.f$0.lambda$setDialogFlags$24$MessagesStorage(this.f$1, this.f$2);
   }
}
