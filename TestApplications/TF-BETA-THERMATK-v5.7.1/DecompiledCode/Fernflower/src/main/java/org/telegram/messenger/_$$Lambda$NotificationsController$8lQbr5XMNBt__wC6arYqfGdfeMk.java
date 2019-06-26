package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$NotificationsController$8lQbr5XMNBt__wC6arYqfGdfeMk implements Runnable {
   // $FF: synthetic field
   private final NotificationsController f$0;
   // $FF: synthetic field
   private final SparseArray f$1;
   // $FF: synthetic field
   private final ArrayList f$2;

   // $FF: synthetic method
   public _$$Lambda$NotificationsController$8lQbr5XMNBt__wC6arYqfGdfeMk(NotificationsController var1, SparseArray var2, ArrayList var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$removeDeletedMessagesFromNotifications$8$NotificationsController(this.f$1, this.f$2);
   }
}
