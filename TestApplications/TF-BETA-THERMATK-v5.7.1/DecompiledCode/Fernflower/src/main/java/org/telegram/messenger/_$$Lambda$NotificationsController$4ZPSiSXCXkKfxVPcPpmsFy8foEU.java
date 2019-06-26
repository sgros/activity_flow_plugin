package org.telegram.messenger;

import android.util.SparseIntArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$NotificationsController$4ZPSiSXCXkKfxVPcPpmsFy8foEU implements Runnable {
   // $FF: synthetic field
   private final NotificationsController f$0;
   // $FF: synthetic field
   private final SparseIntArray f$1;
   // $FF: synthetic field
   private final ArrayList f$2;

   // $FF: synthetic method
   public _$$Lambda$NotificationsController$4ZPSiSXCXkKfxVPcPpmsFy8foEU(NotificationsController var1, SparseIntArray var2, ArrayList var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$removeDeletedHisoryFromNotifications$11$NotificationsController(this.f$1, this.f$2);
   }
}
