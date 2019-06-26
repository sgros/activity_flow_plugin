package org.telegram.messenger;

import java.io.File;

// $FF: synthetic class
public final class _$$Lambda$ImageLoader$k74dlCvVooO9jw6g30Qy8tUSaQg implements Runnable {
   // $FF: synthetic field
   private final ImageLoader f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final File f$3;

   // $FF: synthetic method
   public _$$Lambda$ImageLoader$k74dlCvVooO9jw6g30Qy8tUSaQg(ImageLoader var1, String var2, int var3, File var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$fileDidLoaded$8$ImageLoader(this.f$1, this.f$2, this.f$3);
   }
}
