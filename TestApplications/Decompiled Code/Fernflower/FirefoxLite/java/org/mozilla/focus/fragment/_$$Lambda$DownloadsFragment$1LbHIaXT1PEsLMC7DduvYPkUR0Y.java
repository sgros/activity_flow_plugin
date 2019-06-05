package org.mozilla.focus.fragment;

import android.arch.lifecycle.Observer;
import org.mozilla.rocket.download.DownloadInfoPack;

// $FF: synthetic class
public final class _$$Lambda$DownloadsFragment$1LbHIaXT1PEsLMC7DduvYPkUR0Y implements Observer {
   // $FF: synthetic field
   private final DownloadsFragment f$0;

   // $FF: synthetic method
   public _$$Lambda$DownloadsFragment$1LbHIaXT1PEsLMC7DduvYPkUR0Y(DownloadsFragment var1) {
      this.f$0 = var1;
   }

   public final void onChanged(Object var1) {
      DownloadsFragment.lambda$onCreateView$0(this.f$0, (DownloadInfoPack)var1);
   }
}
