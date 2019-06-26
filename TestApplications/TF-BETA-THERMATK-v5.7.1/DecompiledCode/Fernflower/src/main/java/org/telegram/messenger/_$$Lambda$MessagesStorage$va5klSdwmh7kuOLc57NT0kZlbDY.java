package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$va5klSdwmh7kuOLc57NT0kZlbDY implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$va5klSdwmh7kuOLc57NT0kZlbDY(MessagesStorage var1, TLRPC.EncryptedChat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$updateEncryptedChatTTL$105$MessagesStorage(this.f$1);
   }
}
