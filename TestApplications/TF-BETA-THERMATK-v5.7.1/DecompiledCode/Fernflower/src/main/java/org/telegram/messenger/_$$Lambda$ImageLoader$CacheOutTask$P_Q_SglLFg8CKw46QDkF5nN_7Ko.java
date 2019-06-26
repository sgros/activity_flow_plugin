package org.telegram.messenger;

import android.graphics.drawable.Drawable;

// $FF: synthetic class
public final class _$$Lambda$ImageLoader$CacheOutTask$P_Q_SglLFg8CKw46QDkF5nN_7Ko implements Runnable {
   // $FF: synthetic field
   private final ImageLoader.CacheOutTask f$0;
   // $FF: synthetic field
   private final Drawable f$1;

   // $FF: synthetic method
   public _$$Lambda$ImageLoader$CacheOutTask$P_Q_SglLFg8CKw46QDkF5nN_7Ko(ImageLoader.CacheOutTask var1, Drawable var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onPostExecute$1$ImageLoader$CacheOutTask(this.f$1);
   }
}
