package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$tOg7TGz_CemIZKoQ4SmkPVL_N7w implements RequestDelegate {
   // $FF: synthetic field
   private final ArticleViewer f$0;
   // $FF: synthetic field
   private final ArticleViewer.BlockChannelCell f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.TL_channels_joinChannel f$3;
   // $FF: synthetic field
   private final TLRPC.Chat f$4;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$tOg7TGz_CemIZKoQ4SmkPVL_N7w(ArticleViewer var1, ArticleViewer.BlockChannelCell var2, int var3, TLRPC.TL_channels_joinChannel var4, TLRPC.Chat var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$joinChannel$35$ArticleViewer(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
