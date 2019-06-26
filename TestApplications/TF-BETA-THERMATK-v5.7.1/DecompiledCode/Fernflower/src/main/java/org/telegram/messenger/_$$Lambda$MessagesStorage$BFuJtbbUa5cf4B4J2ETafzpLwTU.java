package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$BFuJtbbUa5cf4B4J2ETafzpLwTU implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final long f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$BFuJtbbUa5cf4B4J2ETafzpLwTU(MessagesStorage var1, long var2, long var4, boolean var6, long var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var6;
      this.f$4 = var7;
   }

   public final void run() {
      this.f$0.lambda$processPendingRead$85$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
