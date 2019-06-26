package org.telegram.messenger;

import java.io.File;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$a2su0LhJhPbUXm5fG2WcDQ_Npn4 implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.TL_photo f$1;
   // $FF: synthetic field
   private final MessageObject f$2;
   // $FF: synthetic field
   private final File f$3;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$4;
   // $FF: synthetic field
   private final String f$5;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$a2su0LhJhPbUXm5fG2WcDQ_Npn4(SendMessagesHelper var1, TLRPC.TL_photo var2, MessageObject var3, File var4, SendMessagesHelper.DelayedMessage var5, String var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$1$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
