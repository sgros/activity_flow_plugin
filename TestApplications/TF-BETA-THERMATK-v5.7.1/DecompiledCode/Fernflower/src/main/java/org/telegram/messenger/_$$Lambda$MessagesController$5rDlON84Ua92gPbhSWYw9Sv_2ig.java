package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$5rDlON84Ua92gPbhSWYw9Sv_2ig implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$5rDlON84Ua92gPbhSWYw9Sv_2ig(MessagesController var1, ArrayList var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$processLoadedDeleteTask$36$MessagesController(this.f$1, this.f$2);
   }
}
