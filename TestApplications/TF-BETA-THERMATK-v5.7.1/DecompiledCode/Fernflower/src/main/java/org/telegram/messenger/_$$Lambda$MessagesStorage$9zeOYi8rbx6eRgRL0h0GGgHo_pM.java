package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$9zeOYi8rbx6eRgRL0h0GGgHo_pM implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$9zeOYi8rbx6eRgRL0h0GGgHo_pM(MessagesStorage var1, TLRPC.EncryptedChat var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$updateEncryptedChatSeq$104$MessagesStorage(this.f$1, this.f$2);
   }
}
