package org.telegram.messenger;

import java.util.HashMap;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$d0CKfJCWUy1j7KfKMh_A2PiSqHA implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final HashMap f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$d0CKfJCWUy1j7KfKMh_A2PiSqHA(MessagesStorage var1, HashMap var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$putCachedPhoneBook$89$MessagesStorage(this.f$1, this.f$2);
   }
}
