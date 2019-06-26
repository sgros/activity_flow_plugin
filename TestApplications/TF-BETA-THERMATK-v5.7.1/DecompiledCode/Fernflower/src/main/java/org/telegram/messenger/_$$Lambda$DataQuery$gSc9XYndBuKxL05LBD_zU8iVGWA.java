package org.telegram.messenger;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$gSc9XYndBuKxL05LBD_zU8iVGWA implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final String[] f$1;
   // $FF: synthetic field
   private final DataQuery.KeywordResultCallback f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final ArrayList f$5;
   // $FF: synthetic field
   private final CountDownLatch f$6;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$gSc9XYndBuKxL05LBD_zU8iVGWA(DataQuery var1, String[] var2, DataQuery.KeywordResultCallback var3, String var4, boolean var5, ArrayList var6, CountDownLatch var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$getEmojiSuggestions$122$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
