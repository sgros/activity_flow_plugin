package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$NotificationsController$bRv8AkmkiAwGyZ1dPg2TuCyHYS0 implements Runnable {
   // $FF: synthetic field
   private final NotificationsController f$0;
   // $FF: synthetic field
   private final LongSparseArray f$1;
   // $FF: synthetic field
   private final ArrayList f$2;

   // $FF: synthetic method
   public _$$Lambda$NotificationsController$bRv8AkmkiAwGyZ1dPg2TuCyHYS0(NotificationsController var1, LongSparseArray var2, ArrayList var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$processDialogsUpdateRead$19$NotificationsController(this.f$1, this.f$2);
   }
}
