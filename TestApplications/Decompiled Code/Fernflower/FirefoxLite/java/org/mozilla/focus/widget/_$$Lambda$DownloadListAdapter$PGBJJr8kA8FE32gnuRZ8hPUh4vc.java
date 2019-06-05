package org.mozilla.focus.widget;

import android.view.View;
import org.mozilla.focus.download.DownloadInfo;

// $FF: synthetic class
public final class _$$Lambda$DownloadListAdapter$PGBJJr8kA8FE32gnuRZ8hPUh4vc implements Runnable {
   // $FF: synthetic field
   private final DownloadListAdapter f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final View f$2;
   // $FF: synthetic field
   private final DownloadInfo f$3;

   // $FF: synthetic method
   public _$$Lambda$DownloadListAdapter$PGBJJr8kA8FE32gnuRZ8hPUh4vc(DownloadListAdapter var1, boolean var2, View var3, DownloadInfo var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      DownloadListAdapter.lambda$null$2(this.f$0, this.f$1, this.f$2, this.f$3);
   }
}
