package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$uX9RNDdADQA1Zb0x7WR8P7gNoZY implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$uX9RNDdADQA1Zb0x7WR8P7gNoZY(MessagesStorage var1, boolean var2, int var3, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$removeFromDownloadQueue$114$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
