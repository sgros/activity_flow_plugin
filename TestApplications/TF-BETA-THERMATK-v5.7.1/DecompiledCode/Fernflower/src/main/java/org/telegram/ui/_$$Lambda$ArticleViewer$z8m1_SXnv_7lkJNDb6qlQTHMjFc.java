package org.telegram.ui;

import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$z8m1_SXnv_7lkJNDb6qlQTHMjFc implements RequestDelegate {
   // $FF: synthetic field
   private final ArticleViewer f$0;
   // $FF: synthetic field
   private final TLRPC.WebPage f$1;
   // $FF: synthetic field
   private final MessageObject f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final int f$4;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$z8m1_SXnv_7lkJNDb6qlQTHMjFc(ArticleViewer var1, TLRPC.WebPage var2, MessageObject var3, String var4, int var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$open$23$ArticleViewer(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
