package org.telegram.ui;

import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.MessagesStorage;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$zbo6FUWEskAD8BCSLY3b_JqK_JY implements Runnable {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final MessagesStorage f$1;
   // $FF: synthetic field
   private final CountDownLatch f$2;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$zbo6FUWEskAD8BCSLY3b_JqK_JY(ChatActivity var1, MessagesStorage var2, CountDownLatch var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$onFragmentCreate$3$ChatActivity(this.f$1, this.f$2);
   }
}
