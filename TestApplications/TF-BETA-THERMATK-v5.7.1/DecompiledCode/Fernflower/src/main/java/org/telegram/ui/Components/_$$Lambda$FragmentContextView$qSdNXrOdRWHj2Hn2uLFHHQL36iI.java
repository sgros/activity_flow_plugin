package org.telegram.ui.Components;

import org.telegram.messenger.LocationController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LocationActivity;

// $FF: synthetic class
public final class _$$Lambda$FragmentContextView$qSdNXrOdRWHj2Hn2uLFHHQL36iI implements LocationActivity.LocationActivityDelegate {
   // $FF: synthetic field
   private final LocationController.SharingLocationInfo f$0;
   // $FF: synthetic field
   private final long f$1;

   // $FF: synthetic method
   public _$$Lambda$FragmentContextView$qSdNXrOdRWHj2Hn2uLFHHQL36iI(LocationController.SharingLocationInfo var1, long var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void didSelectLocation(TLRPC.MessageMedia var1, int var2) {
      FragmentContextView.lambda$openSharingLocation$5(this.f$0, this.f$1, var1, var2);
   }
}
