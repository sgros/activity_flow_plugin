package org.mozilla.focus.fragment;

import android.arch.lifecycle.Observer;
import org.mozilla.focus.download.DownloadInfo;

// $FF: synthetic class
public final class _$$Lambda$DownloadsFragment$MqKfE5I_hWyS9Uf3uY_tBX4jOug implements Observer {
   // $FF: synthetic field
   private final DownloadsFragment f$0;

   // $FF: synthetic method
   public _$$Lambda$DownloadsFragment$MqKfE5I_hWyS9Uf3uY_tBX4jOug(DownloadsFragment var1) {
      this.f$0 = var1;
   }

   public final void onChanged(Object var1) {
      DownloadsFragment.lambda$onCreateView$3(this.f$0, (DownloadInfo)var1);
   }
}
