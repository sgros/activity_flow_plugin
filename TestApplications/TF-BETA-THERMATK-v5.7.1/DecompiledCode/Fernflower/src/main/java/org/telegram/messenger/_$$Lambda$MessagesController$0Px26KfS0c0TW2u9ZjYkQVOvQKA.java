package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$0Px26KfS0c0TW2u9ZjYkQVOvQKA implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_updateUserBlocked f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$0Px26KfS0c0TW2u9ZjYkQVOvQKA(MessagesController var1, TLRPC.TL_updateUserBlocked var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$processUpdateArray$241$MessagesController(this.f$1);
   }
}
