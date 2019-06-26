package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.messenger.support.SparseLongArray;

// $FF: synthetic class
public final class _$$Lambda$NotificationsController$bn_qy54k0GHNymLhNYsBBa6g2mw implements Runnable {
   // $FF: synthetic field
   private final NotificationsController f$0;
   // $FF: synthetic field
   private final SparseLongArray f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final boolean f$6;

   // $FF: synthetic method
   public _$$Lambda$NotificationsController$bn_qy54k0GHNymLhNYsBBa6g2mw(NotificationsController var1, SparseLongArray var2, ArrayList var3, long var4, int var6, int var7, boolean var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
   }

   public final void run() {
      this.f$0.lambda$processReadMessages$13$NotificationsController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
