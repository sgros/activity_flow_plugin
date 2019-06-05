package org.mozilla.focus.fragment;

import android.arch.lifecycle.Observer;
import android.view.ViewGroup;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;

// $FF: synthetic class
public final class _$$Lambda$BrowserFragment$l94_q85PEWz_WW25q4mSUZ5WGEE implements Observer {
   // $FF: synthetic field
   private final BrowserFragment f$0;
   // $FF: synthetic field
   private final ViewGroup f$1;

   // $FF: synthetic method
   public _$$Lambda$BrowserFragment$l94_q85PEWz_WW25q4mSUZ5WGEE(BrowserFragment var1, ViewGroup var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onChanged(Object var1) {
      BrowserFragment.lambda$onCreateView$2(this.f$0, this.f$1, (DownloadIndicatorViewModel.Status)var1);
   }
}
