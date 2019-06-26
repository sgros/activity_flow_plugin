package org.telegram.messenger;

import android.location.Location;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$Q4vzAX7T1ldI1PT8YTFIZJtboaE implements Runnable {
   // $FF: synthetic field
   private final Location f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final long f$2;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$Q4vzAX7T1ldI1PT8YTFIZJtboaE(Location var1, int var2, long var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      SendMessagesHelper.lambda$prepareSendingLocation$51(this.f$0, this.f$1, this.f$2);
   }
}
