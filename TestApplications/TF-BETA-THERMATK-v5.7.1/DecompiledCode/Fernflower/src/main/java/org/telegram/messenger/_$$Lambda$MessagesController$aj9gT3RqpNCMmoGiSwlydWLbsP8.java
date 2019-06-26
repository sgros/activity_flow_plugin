package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$aj9gT3RqpNCMmoGiSwlydWLbsP8 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final TLRPC.ChatFull f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final MessageObject f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$aj9gT3RqpNCMmoGiSwlydWLbsP8(MessagesController var1, ArrayList var2, boolean var3, TLRPC.ChatFull var4, boolean var5, MessageObject var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$processChatInfo$75$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
