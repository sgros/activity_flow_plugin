package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileRefController$oQwONz9oH9KbjBHI1GVgQcuvYoU implements RequestDelegate {
   // $FF: synthetic field
   private final FileRefController f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$FileRefController$oQwONz9oH9KbjBHI1GVgQcuvYoU(FileRefController var1, String var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$requestReferenceFromServer$16$FileRefController(this.f$1, this.f$2, var1, var2);
   }
}
