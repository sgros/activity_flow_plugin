package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$XuUcVoATmHzvdWgq3344GUvreTA implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.TL_updateNewChannelMessage f$1;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$XuUcVoATmHzvdWgq3344GUvreTA(SendMessagesHelper var1, TLRPC.TL_updateNewChannelMessage var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$35$SendMessagesHelper(this.f$1);
   }
}
