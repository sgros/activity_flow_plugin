package org.telegram.messenger;

import java.io.File;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$pp0U4GJ1r75dDYF4YGnbf9kI6EU implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$1;
   // $FF: synthetic field
   private final File f$2;
   // $FF: synthetic field
   private final MessageObject f$3;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$pp0U4GJ1r75dDYF4YGnbf9kI6EU(SendMessagesHelper var1, SendMessagesHelper.DelayedMessage var2, File var3, MessageObject var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$didReceivedNotification$4$SendMessagesHelper(this.f$1, this.f$2, this.f$3);
   }
}
