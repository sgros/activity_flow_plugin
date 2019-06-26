package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$WallpapersListActivity$rkTFtQlkaXRQn8loH7KW7DjD08g implements RequestDelegate {
   // $FF: synthetic field
   private final WallpapersListActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$WallpapersListActivity$rkTFtQlkaXRQn8loH7KW7DjD08g(WallpapersListActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadWallpapers$5$WallpapersListActivity(var1, var2);
   }
}
