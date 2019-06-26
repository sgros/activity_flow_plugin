package org.telegram.ui;

import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.MessagesStorage;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$diV3z76REAMmAeIIp51DTLTaiSg implements Runnable {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final MessagesStorage f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final CountDownLatch f$3;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$diV3z76REAMmAeIIp51DTLTaiSg(ChatActivity var1, MessagesStorage var2, int var3, CountDownLatch var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$onFragmentCreate$0$ChatActivity(this.f$1, this.f$2, this.f$3);
   }
}
