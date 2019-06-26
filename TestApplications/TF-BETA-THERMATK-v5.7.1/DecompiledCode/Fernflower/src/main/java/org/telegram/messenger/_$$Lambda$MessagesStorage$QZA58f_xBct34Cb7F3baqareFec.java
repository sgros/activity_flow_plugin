package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$QZA58f_xBct34Cb7F3baqareFec implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final long f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$QZA58f_xBct34Cb7F3baqareFec(MessagesStorage var1, int var2, long var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$resetMentionsCount$60$MessagesStorage(this.f$1, this.f$2);
   }
}
