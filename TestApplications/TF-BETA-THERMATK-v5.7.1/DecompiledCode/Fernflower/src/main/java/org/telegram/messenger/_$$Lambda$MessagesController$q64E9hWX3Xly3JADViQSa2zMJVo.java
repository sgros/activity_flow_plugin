package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$q64E9hWX3Xly3JADViQSa2zMJVo implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.messages_Dialogs f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$q64E9hWX3Xly3JADViQSa2zMJVo(MessagesController var1, TLRPC.messages_Dialogs var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$processDialogsUpdate$138$MessagesController(this.f$1);
   }
}
