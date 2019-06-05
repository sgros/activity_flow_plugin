package org.mozilla.cachedrequestloader;

import android.content.Context;

// $FF: synthetic class
public final class _$$Lambda$BackgroundCachedRequestLoader$EHwI_s8H_FtNt9t2MTbwyEF0UBA implements Runnable {
   // $FF: synthetic field
   private final BackgroundCachedRequestLoader f$0;
   // $FF: synthetic field
   private final Context f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final ResponseData f$3;

   // $FF: synthetic method
   public _$$Lambda$BackgroundCachedRequestLoader$EHwI_s8H_FtNt9t2MTbwyEF0UBA(BackgroundCachedRequestLoader var1, Context var2, String var3, ResponseData var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      BackgroundCachedRequestLoader.lambda$loadFromCache$0(this.f$0, this.f$1, this.f$2, this.f$3);
   }
}
