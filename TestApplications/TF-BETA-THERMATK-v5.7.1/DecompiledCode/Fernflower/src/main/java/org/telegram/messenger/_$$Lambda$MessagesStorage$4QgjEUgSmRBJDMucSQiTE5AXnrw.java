package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$4QgjEUgSmRBJDMucSQiTE5AXnrw implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final MessagesStorage.IntCallback f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$4QgjEUgSmRBJDMucSQiTE5AXnrw(MessagesStorage var1, long var2, MessagesStorage.IntCallback var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
   }

   public final void run() {
      this.f$0.lambda$getMessagesCount$98$MessagesStorage(this.f$1, this.f$2);
   }
}
