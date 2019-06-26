package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SettingsActivity$SearchAdapter$mPyLXiUd_cjxJI7NTOZqkYVtkUM implements RequestDelegate {
   // $FF: synthetic field
   private final SettingsActivity.SearchAdapter f$0;

   // $FF: synthetic method
   public _$$Lambda$SettingsActivity$SearchAdapter$mPyLXiUd_cjxJI7NTOZqkYVtkUM(SettingsActivity.SearchAdapter var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadFaqWebPage$82$SettingsActivity$SearchAdapter(var1, var2);
   }
}
