package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$JoinGroupAlert$IWx_squ4_I5Qyjo_N06x9CUQNvc implements Runnable {
   // $FF: synthetic field
   private final JoinGroupAlert f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final TLRPC.TL_messages_importChatInvite f$3;

   // $FF: synthetic method
   public _$$Lambda$JoinGroupAlert$IWx_squ4_I5Qyjo_N06x9CUQNvc(JoinGroupAlert var1, TLRPC.TL_error var2, TLObject var3, TLRPC.TL_messages_importChatInvite var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$1$JoinGroupAlert(this.f$1, this.f$2, this.f$3);
   }
}
