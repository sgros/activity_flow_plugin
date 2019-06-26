package org.telegram.messenger;

import java.io.File;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MediaController$0rWtUTn2QUBBNpu8veTtOVfGYSQ implements Runnable {
   // $FF: synthetic field
   private final MediaController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_document f$1;
   // $FF: synthetic field
   private final File f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$MediaController$0rWtUTn2QUBBNpu8veTtOVfGYSQ(MediaController var1, TLRPC.TL_document var2, File var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$19$MediaController(this.f$1, this.f$2, this.f$3);
   }
}
