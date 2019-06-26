package org.telegram.messenger;

import android.util.SparseIntArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$JPDY_4SRGvZvjZ1pDyebULwuKFA implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final SparseIntArray f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$JPDY_4SRGvZvjZ1pDyebULwuKFA(MessagesController var1, ArrayList var2, boolean var3, SparseIntArray var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$processLoadedBlockedUsers$55$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
