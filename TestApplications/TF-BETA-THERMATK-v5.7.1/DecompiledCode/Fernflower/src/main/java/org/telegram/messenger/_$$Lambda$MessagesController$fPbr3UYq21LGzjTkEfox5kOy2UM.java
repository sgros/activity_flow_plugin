package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$fPbr3UYq21LGzjTkEfox5kOy2UM implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final ArrayList f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$fPbr3UYq21LGzjTkEfox5kOy2UM(MessagesController var1, boolean var2, int var3, ArrayList var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$processUpdates$232$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
