package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$Xb58sIGp8srP4NjwJOAz4ujR9zM implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$Xb58sIGp8srP4NjwJOAz4ujR9zM(MessagesStorage var1, TLRPC.EncryptedChat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$updateEncryptedChat$107$MessagesStorage(this.f$1);
   }
}
