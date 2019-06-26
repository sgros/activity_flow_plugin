package org.telegram.messenger;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$NotificationsController$blpPMIxTaKEgWkp2zDr1_y8eGUY implements Runnable {
   // $FF: synthetic field
   private final NotificationsController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final CountDownLatch f$5;

   // $FF: synthetic method
   public _$$Lambda$NotificationsController$blpPMIxTaKEgWkp2zDr1_y8eGUY(NotificationsController var1, ArrayList var2, ArrayList var3, boolean var4, boolean var5, CountDownLatch var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$processNewMessages$16$NotificationsController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
