package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$ul28E39n2CqjyGu1NrRVaRUiBf8 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.ChatParticipants f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$ul28E39n2CqjyGu1NrRVaRUiBf8(MessagesStorage var1, TLRPC.ChatParticipants var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$updateChatParticipants$67$MessagesStorage(this.f$1);
   }
}
