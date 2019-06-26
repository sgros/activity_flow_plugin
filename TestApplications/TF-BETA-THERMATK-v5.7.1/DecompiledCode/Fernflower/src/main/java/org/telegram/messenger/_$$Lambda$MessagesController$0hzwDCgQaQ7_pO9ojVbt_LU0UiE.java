package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$0hzwDCgQaQ7_pO9ojVbt_LU0UiE implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_help_proxyDataPromo f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_peerDialogs f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$0hzwDCgQaQ7_pO9ojVbt_LU0UiE(MessagesController var1, TLRPC.TL_help_proxyDataPromo var2, TLRPC.TL_messages_peerDialogs var3, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$90$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
