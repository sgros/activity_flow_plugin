package org.telegram.ui.Adapters;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SearchAdapterHelper$lQm_KegCMNFAimI1TKtkhyCcOJg implements RequestDelegate {
   // $FF: synthetic field
   private final SearchAdapterHelper f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final boolean f$3;

   // $FF: synthetic method
   public _$$Lambda$SearchAdapterHelper$lQm_KegCMNFAimI1TKtkhyCcOJg(SearchAdapterHelper var1, int var2, String var3, boolean var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$queryServerSearch$1$SearchAdapterHelper(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
