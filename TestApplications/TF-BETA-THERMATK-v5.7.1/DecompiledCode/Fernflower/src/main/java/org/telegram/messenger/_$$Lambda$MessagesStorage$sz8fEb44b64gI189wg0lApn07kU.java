package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$sz8fEb44b64gI189wg0lApn07kU implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.Document f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final String f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$sz8fEb44b64gI189wg0lApn07kU(MessagesStorage var1, TLRPC.Document var2, String var3, String var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$addRecentLocalFile$35$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
