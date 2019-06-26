package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$WallpapersListActivity$SearchAdapter$4mWogDs9zLmdvnE5jd5DGo_Q4ak implements RequestDelegate {
   // $FF: synthetic field
   private final WallpapersListActivity.SearchAdapter f$0;

   // $FF: synthetic method
   public _$$Lambda$WallpapersListActivity$SearchAdapter$4mWogDs9zLmdvnE5jd5DGo_Q4ak(WallpapersListActivity.SearchAdapter var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$searchBotUser$2$WallpapersListActivity$SearchAdapter(var1, var2);
   }
}
