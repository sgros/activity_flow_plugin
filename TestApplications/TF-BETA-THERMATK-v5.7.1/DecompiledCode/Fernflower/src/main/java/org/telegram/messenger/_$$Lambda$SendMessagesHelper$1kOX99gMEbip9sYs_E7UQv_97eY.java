package org.telegram.messenger;

import java.io.File;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$1kOX99gMEbip9sYs_E7UQv_97eY implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final File f$1;
   // $FF: synthetic field
   private final MessageObject f$2;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$3;
   // $FF: synthetic field
   private final String f$4;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$1kOX99gMEbip9sYs_E7UQv_97eY(SendMessagesHelper var1, File var2, MessageObject var3, SendMessagesHelper.DelayedMessage var4, String var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$didReceivedNotification$2$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
