package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoadOperation$pLwgbN71WnWYsTs7i_Ie2IK_Sjs implements RequestDelegate {
   // $FF: synthetic field
   private final FileLoadOperation f$0;
   // $FF: synthetic field
   private final FileLoadOperation.RequestInfo f$1;
   // $FF: synthetic field
   private final TLObject f$2;

   // $FF: synthetic method
   public _$$Lambda$FileLoadOperation$pLwgbN71WnWYsTs7i_Ie2IK_Sjs(FileLoadOperation var1, FileLoadOperation.RequestInfo var2, TLObject var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$startDownloadRequest$12$FileLoadOperation(this.f$1, this.f$2, var1, var2);
   }
}
