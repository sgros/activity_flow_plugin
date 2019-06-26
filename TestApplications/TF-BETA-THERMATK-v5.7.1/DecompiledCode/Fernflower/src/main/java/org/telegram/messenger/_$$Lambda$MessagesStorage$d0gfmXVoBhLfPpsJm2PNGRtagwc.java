package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$d0gfmXVoBhLfPpsJm2PNGRtagwc implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.ChatFull f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$d0gfmXVoBhLfPpsJm2PNGRtagwc(MessagesStorage var1, TLRPC.ChatFull var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$updateChatInfo$75$MessagesStorage(this.f$1, this.f$2);
   }
}
