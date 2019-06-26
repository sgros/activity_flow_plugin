package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$OU_4EBuNPSDwj_f4lawwZA9jcMI implements Runnable {
   // $FF: synthetic field
   private final ArticleViewer f$0;
   // $FF: synthetic field
   private final ArticleViewer.WebpageAdapter f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final ArticleViewer.BlockChannelCell f$5;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$OU_4EBuNPSDwj_f4lawwZA9jcMI(ArticleViewer var1, ArticleViewer.WebpageAdapter var2, TLRPC.TL_error var3, TLObject var4, int var5, ArticleViewer.BlockChannelCell var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$30$ArticleViewer(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
