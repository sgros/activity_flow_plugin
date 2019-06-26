package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$_y89sXvt9qXhyQF6eZF_c0a7Hdc implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.TL_emojiKeywordsDifference f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$_y89sXvt9qXhyQF6eZF_c0a7Hdc(DataQuery var1, TLRPC.TL_emojiKeywordsDifference var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$putEmojiKeywords$118$DataQuery(this.f$1, this.f$2);
   }
}
