package org.mozilla.focus.activity;

import android.arch.lifecycle.Observer;
import java.util.List;

// $FF: synthetic class
public final class _$$Lambda$MainActivity$d7XGEV0md3UPIIt73gfCZLP0q8o implements Observer {
   // $FF: synthetic field
   private final MainActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$MainActivity$d7XGEV0md3UPIIt73gfCZLP0q8o(MainActivity var1) {
      this.f$0 = var1;
   }

   public final void onChanged(Object var1) {
      MainActivity.lambda$updateMenu$5(this.f$0, (List)var1);
   }
}
