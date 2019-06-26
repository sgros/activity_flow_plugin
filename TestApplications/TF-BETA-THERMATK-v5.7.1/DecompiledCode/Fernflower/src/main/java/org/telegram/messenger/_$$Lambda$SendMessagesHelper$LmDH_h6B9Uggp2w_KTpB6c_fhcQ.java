package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$LmDH_h6B9Uggp2w_KTpB6c_fhcQ implements Runnable {
   // $FF: synthetic field
   private final long f$0;
   // $FF: synthetic field
   private final TLRPC.BotInlineResult f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final HashMap f$3;
   // $FF: synthetic field
   private final MessageObject f$4;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$LmDH_h6B9Uggp2w_KTpB6c_fhcQ(long var1, TLRPC.BotInlineResult var3, int var4, HashMap var5, MessageObject var6) {
      this.f$0 = var1;
      this.f$1 = var3;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
   }

   public final void run() {
      SendMessagesHelper.lambda$prepareSendingBotContextResult$53(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
