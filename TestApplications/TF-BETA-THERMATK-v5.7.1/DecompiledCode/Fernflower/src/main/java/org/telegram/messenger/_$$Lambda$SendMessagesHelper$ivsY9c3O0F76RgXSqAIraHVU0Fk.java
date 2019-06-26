package org.telegram.messenger;

import java.io.File;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$ivsY9c3O0F76RgXSqAIraHVU0Fk implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$1;
   // $FF: synthetic field
   private final File f$2;
   // $FF: synthetic field
   private final TLRPC.Document f$3;
   // $FF: synthetic field
   private final MessageObject f$4;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$ivsY9c3O0F76RgXSqAIraHVU0Fk(SendMessagesHelper var1, SendMessagesHelper.DelayedMessage var2, File var3, TLRPC.Document var4, MessageObject var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$3$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
