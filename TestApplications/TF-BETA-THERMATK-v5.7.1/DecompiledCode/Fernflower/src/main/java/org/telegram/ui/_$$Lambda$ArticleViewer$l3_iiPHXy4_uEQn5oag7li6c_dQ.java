package org.telegram.ui;

import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$l3_iiPHXy4_uEQn5oag7li6c_dQ implements Runnable {
   // $FF: synthetic field
   private final ArticleViewer f$0;
   // $FF: synthetic field
   private final TLRPC.WebPage f$1;
   // $FF: synthetic field
   private final TLRPC.TL_webPage f$2;
   // $FF: synthetic field
   private final MessageObject f$3;
   // $FF: synthetic field
   private final String f$4;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$l3_iiPHXy4_uEQn5oag7li6c_dQ(ArticleViewer var1, TLRPC.WebPage var2, TLRPC.TL_webPage var3, MessageObject var4, String var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$22$ArticleViewer(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
