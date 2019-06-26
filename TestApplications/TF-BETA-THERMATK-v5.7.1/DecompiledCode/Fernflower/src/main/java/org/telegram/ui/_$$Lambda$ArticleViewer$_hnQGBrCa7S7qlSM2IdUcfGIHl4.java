package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$_hnQGBrCa7S7qlSM2IdUcfGIHl4 implements Runnable {
   // $FF: synthetic field
   private final ArticleViewer f$0;
   // $FF: synthetic field
   private final ArticleViewer.BlockChannelCell f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.TL_error f$3;
   // $FF: synthetic field
   private final TLRPC.TL_channels_joinChannel f$4;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$_hnQGBrCa7S7qlSM2IdUcfGIHl4(ArticleViewer var1, ArticleViewer.BlockChannelCell var2, int var3, TLRPC.TL_error var4, TLRPC.TL_channels_joinChannel var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$32$ArticleViewer(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
