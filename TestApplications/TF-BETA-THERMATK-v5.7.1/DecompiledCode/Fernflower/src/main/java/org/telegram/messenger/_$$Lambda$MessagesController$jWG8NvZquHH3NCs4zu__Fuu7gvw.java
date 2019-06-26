package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$jWG8NvZquHH3NCs4zu__Fuu7gvw implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_updateServiceNotification f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$jWG8NvZquHH3NCs4zu__Fuu7gvw(MessagesController var1, TLRPC.TL_updateServiceNotification var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$processUpdateArray$242$MessagesController(this.f$1);
   }
}
