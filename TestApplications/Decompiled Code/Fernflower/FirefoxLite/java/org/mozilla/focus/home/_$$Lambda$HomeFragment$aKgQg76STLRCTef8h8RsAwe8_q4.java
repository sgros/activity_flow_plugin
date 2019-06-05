package org.mozilla.focus.home;

import android.arch.lifecycle.Observer;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;

// $FF: synthetic class
public final class _$$Lambda$HomeFragment$aKgQg76STLRCTef8h8RsAwe8_q4 implements Observer {
   // $FF: synthetic field
   private final HomeFragment f$0;

   // $FF: synthetic method
   public _$$Lambda$HomeFragment$aKgQg76STLRCTef8h8RsAwe8_q4(HomeFragment var1) {
      this.f$0 = var1;
   }

   public final void onChanged(Object var1) {
      HomeFragment.lambda$onCreateView$5(this.f$0, (DownloadIndicatorViewModel.Status)var1);
   }
}
