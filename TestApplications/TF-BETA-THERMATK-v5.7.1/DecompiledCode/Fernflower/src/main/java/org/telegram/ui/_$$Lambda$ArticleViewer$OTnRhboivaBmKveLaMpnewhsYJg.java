package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$OTnRhboivaBmKveLaMpnewhsYJg implements RequestDelegate {
   // $FF: synthetic field
   private final ArticleViewer f$0;
   // $FF: synthetic field
   private final ArticleViewer.WebpageAdapter f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final ArticleViewer.BlockChannelCell f$3;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$OTnRhboivaBmKveLaMpnewhsYJg(ArticleViewer var1, ArticleViewer.WebpageAdapter var2, int var3, ArticleViewer.BlockChannelCell var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadChannel$31$ArticleViewer(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
