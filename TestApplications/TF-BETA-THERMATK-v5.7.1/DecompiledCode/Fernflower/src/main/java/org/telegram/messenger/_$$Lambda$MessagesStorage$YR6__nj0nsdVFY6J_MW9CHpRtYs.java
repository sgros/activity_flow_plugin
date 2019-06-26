package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$YR6__nj0nsdVFY6J_MW9CHpRtYs implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$YR6__nj0nsdVFY6J_MW9CHpRtYs(MessagesStorage var1, long var2, boolean var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
   }

   public final void run() {
      this.f$0.lambda$setDialogUnread$146$MessagesStorage(this.f$1, this.f$2);
   }
}
