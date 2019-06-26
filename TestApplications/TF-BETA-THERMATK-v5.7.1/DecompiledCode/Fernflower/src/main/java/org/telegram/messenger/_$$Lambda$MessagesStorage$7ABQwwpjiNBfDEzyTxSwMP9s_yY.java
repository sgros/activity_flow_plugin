package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$7ABQwwpjiNBfDEzyTxSwMP9s_yY implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.TL_updates_channelDifferenceTooLong f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$7ABQwwpjiNBfDEzyTxSwMP9s_yY(MessagesStorage var1, int var2, int var3, TLRPC.TL_updates_channelDifferenceTooLong var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$overwriteChannel$121$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
