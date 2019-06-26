package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PhotoPickerActivity$uL23aS4dzDeSWXHf7OAjR3N3zqg implements RequestDelegate {
   // $FF: synthetic field
   private final PhotoPickerActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$PhotoPickerActivity$uL23aS4dzDeSWXHf7OAjR3N3zqg(PhotoPickerActivity var1, int var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$searchImages$9$PhotoPickerActivity(this.f$1, this.f$2, var1, var2);
   }
}
