package org.telegram.messenger;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$ImageLoader$CacheImage$DfnPkD34YpkvdtADQzUhGGpSRuE implements Runnable {
   // $FF: synthetic field
   private final ImageLoader.CacheImage f$0;
   // $FF: synthetic field
   private final Drawable f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final String f$3;

   // $FF: synthetic method
   public _$$Lambda$ImageLoader$CacheImage$DfnPkD34YpkvdtADQzUhGGpSRuE(ImageLoader.CacheImage var1, Drawable var2, ArrayList var3, String var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$setImageAndClear$0$ImageLoader$CacheImage(this.f$1, this.f$2, this.f$3);
   }
}
