package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$ChpY_h3KplD8DdHCKUKN4FRi5P8 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final TLRPC.Chat f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$ChpY_h3KplD8DdHCKUKN4FRi5P8(MessagesController var1, boolean var2, TLRPC.Chat var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$startShortPoll$196$MessagesController(this.f$1, this.f$2);
   }
}
