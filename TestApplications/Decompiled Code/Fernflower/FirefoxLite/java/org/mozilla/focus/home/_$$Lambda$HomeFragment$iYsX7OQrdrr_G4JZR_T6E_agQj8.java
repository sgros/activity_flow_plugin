package org.mozilla.focus.home;

import org.mozilla.focus.history.model.Site;

// $FF: synthetic class
public final class _$$Lambda$HomeFragment$iYsX7OQrdrr_G4JZR_T6E_agQj8 implements HomeFragment.OnRemovedListener {
   // $FF: synthetic field
   private final Site f$0;

   // $FF: synthetic method
   public _$$Lambda$HomeFragment$iYsX7OQrdrr_G4JZR_T6E_agQj8(Site var1) {
      this.f$0 = var1;
   }

   public final void onRemoved(Site var1) {
      HomeFragment.lambda$mergeHistorySiteToTopSites$10(this.f$0, var1);
   }
}
