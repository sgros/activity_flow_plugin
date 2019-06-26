package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$NotificationsController$XEAogHRWLk5KuijEFvgR3DVl_Oc implements Runnable {
   // $FF: synthetic field
   private final NotificationsController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final LongSparseArray f$2;
   // $FF: synthetic field
   private final ArrayList f$3;

   // $FF: synthetic method
   public _$$Lambda$NotificationsController$XEAogHRWLk5KuijEFvgR3DVl_Oc(NotificationsController var1, ArrayList var2, LongSparseArray var3, ArrayList var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$processLoadedUnreadMessages$21$NotificationsController(this.f$1, this.f$2, this.f$3);
   }
}
