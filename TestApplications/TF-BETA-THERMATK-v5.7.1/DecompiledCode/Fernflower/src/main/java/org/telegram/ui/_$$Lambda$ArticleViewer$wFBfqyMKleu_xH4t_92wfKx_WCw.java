package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$wFBfqyMKleu_xH4t_92wfKx_WCw implements Runnable {
   // $FF: synthetic field
   private final ArticleViewer f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getWebPage f$4;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$wFBfqyMKleu_xH4t_92wfKx_WCw(ArticleViewer var1, int var2, TLObject var3, String var4, TLRPC.TL_messages_getWebPage var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$5$ArticleViewer(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
