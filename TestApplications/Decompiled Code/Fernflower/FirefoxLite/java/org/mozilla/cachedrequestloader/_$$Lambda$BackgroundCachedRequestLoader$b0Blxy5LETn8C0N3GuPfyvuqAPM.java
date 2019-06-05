package org.mozilla.cachedrequestloader;

import android.content.Context;

// $FF: synthetic class
public final class _$$Lambda$BackgroundCachedRequestLoader$b0Blxy5LETn8C0N3GuPfyvuqAPM implements Runnable {
   // $FF: synthetic field
   private final BackgroundCachedRequestLoader f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final Context f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final String f$4;
   // $FF: synthetic field
   private final ResponseData f$5;

   // $FF: synthetic method
   public _$$Lambda$BackgroundCachedRequestLoader$b0Blxy5LETn8C0N3GuPfyvuqAPM(BackgroundCachedRequestLoader var1, int var2, Context var3, String var4, String var5, ResponseData var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      BackgroundCachedRequestLoader.lambda$loadFromRemote$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
