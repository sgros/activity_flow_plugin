package org.mozilla.focus.download;

import android.view.View;
import java.util.List;

// $FF: synthetic class
public final class _$$Lambda$DownloadInfoManager$kRTO87mBRttGY83Sgc9tpXiKrPA implements DownloadInfoManager.AsyncQueryListener {
   // $FF: synthetic field
   private final String f$0;
   // $FF: synthetic field
   private final View f$1;

   // $FF: synthetic method
   public _$$Lambda$DownloadInfoManager$kRTO87mBRttGY83Sgc9tpXiKrPA(String var1, View var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onQueryComplete(List var1) {
      DownloadInfoManager.lambda$showOpenDownloadSnackBar$1(this.f$0, this.f$1, var1);
   }
}
