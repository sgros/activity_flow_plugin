package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$VIjx98VFT9EYmnJ8mUO_SiJ4810 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final ArrayList f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$VIjx98VFT9EYmnJ8mUO_SiJ4810(MessagesStorage var1, boolean var2, ArrayList var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$putContacts$86$MessagesStorage(this.f$1, this.f$2);
   }
}
