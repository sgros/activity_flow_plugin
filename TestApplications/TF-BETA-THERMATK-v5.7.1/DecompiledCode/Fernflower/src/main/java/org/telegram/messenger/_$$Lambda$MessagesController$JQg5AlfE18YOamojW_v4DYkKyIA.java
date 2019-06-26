package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$JQg5AlfE18YOamojW_v4DYkKyIA implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.Chat f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$JQg5AlfE18YOamojW_v4DYkKyIA(MessagesController var1, TLRPC.Chat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$putChat$11$MessagesController(this.f$1);
   }
}
