package org.telegram.ui.Adapters;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SearchAdapterHelper$dscYso9YL4gEzQpoIdpN_Bu9BdA implements RequestDelegate {
   // $FF: synthetic field
   private final SearchAdapterHelper f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final String f$5;

   // $FF: synthetic method
   public _$$Lambda$SearchAdapterHelper$dscYso9YL4gEzQpoIdpN_Bu9BdA(SearchAdapterHelper var1, int var2, boolean var3, boolean var4, boolean var5, String var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$queryServerSearch$3$SearchAdapterHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, var1, var2);
   }
}
