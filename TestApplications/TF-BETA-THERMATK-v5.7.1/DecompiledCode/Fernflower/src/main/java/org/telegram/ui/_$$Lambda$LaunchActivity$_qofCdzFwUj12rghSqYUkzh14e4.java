package org.telegram.ui;

import org.telegram.messenger.LocationController;
import org.telegram.ui.Components.SharingLocationsAlert;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$_qofCdzFwUj12rghSqYUkzh14e4 implements SharingLocationsAlert.SharingLocationsAlertDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final int[] f$1;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$_qofCdzFwUj12rghSqYUkzh14e4(LaunchActivity var1, int[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void didSelectLocation(LocationController.SharingLocationInfo var1) {
      this.f$0.lambda$handleIntent$7$LaunchActivity(this.f$1, var1);
   }
}
