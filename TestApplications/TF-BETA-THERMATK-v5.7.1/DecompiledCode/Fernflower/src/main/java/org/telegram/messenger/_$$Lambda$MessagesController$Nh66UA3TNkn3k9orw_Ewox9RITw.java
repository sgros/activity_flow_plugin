package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$Nh66UA3TNkn3k9orw_Ewox9RITw implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_channels_channelParticipant f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$Nh66UA3TNkn3k9orw_Ewox9RITw(MessagesController var1, TLRPC.TL_channels_channelParticipant var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$226$MessagesController(this.f$1);
   }
}
