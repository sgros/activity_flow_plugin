package org.mozilla.focus.home;

import android.content.Context;

// $FF: synthetic class
public final class _$$Lambda$HomeFragment$E3qF6zcvBwKUOoV8AnaHHon_CA4 implements HomeFragment.LoadRootConfigTask.OnRootConfigLoadedListener {
   // $FF: synthetic field
   private final HomeFragment f$0;
   // $FF: synthetic field
   private final Context f$1;

   // $FF: synthetic method
   public _$$Lambda$HomeFragment$E3qF6zcvBwKUOoV8AnaHHon_CA4(HomeFragment var1, Context var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onRootConfigLoaded(String[] var1) {
      HomeFragment.lambda$initBanner$0(this.f$0, this.f$1, var1);
   }
}
