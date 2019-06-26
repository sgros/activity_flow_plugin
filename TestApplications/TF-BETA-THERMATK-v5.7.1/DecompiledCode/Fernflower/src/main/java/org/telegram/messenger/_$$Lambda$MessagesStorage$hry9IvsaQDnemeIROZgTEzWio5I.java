package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$hry9IvsaQDnemeIROZgTEzWio5I implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.messages_Dialogs f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$hry9IvsaQDnemeIROZgTEzWio5I(MessagesStorage var1, TLRPC.messages_Dialogs var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$putDialogs$148$MessagesStorage(this.f$1, this.f$2);
   }
}
