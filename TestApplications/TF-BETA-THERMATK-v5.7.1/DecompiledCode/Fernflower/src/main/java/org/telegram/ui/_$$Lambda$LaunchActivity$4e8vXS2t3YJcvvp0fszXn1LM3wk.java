package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$4e8vXS2t3YJcvvp0fszXn1LM3wk implements LocationActivity.LocationActivityDelegate {
   // $FF: synthetic field
   private final int[] f$0;
   // $FF: synthetic field
   private final long f$1;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$4e8vXS2t3YJcvvp0fszXn1LM3wk(int[] var1, long var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void didSelectLocation(TLRPC.MessageMedia var1, int var2) {
      LaunchActivity.lambda$null$6(this.f$0, this.f$1, var1, var2);
   }
}
