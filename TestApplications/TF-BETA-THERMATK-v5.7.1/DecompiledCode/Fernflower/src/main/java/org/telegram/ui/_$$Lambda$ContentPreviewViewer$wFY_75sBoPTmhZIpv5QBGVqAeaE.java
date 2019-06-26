package org.telegram.ui;

import org.telegram.ui.Components.RecyclerListView;

// $FF: synthetic class
public final class _$$Lambda$ContentPreviewViewer$wFY_75sBoPTmhZIpv5QBGVqAeaE implements Runnable {
   // $FF: synthetic field
   private final ContentPreviewViewer f$0;
   // $FF: synthetic field
   private final RecyclerListView f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$ContentPreviewViewer$wFY_75sBoPTmhZIpv5QBGVqAeaE(ContentPreviewViewer var1, RecyclerListView var2, int var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$onInterceptTouchEvent$1$ContentPreviewViewer(this.f$1, this.f$2, this.f$3);
   }
}
