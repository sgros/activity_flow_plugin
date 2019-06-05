package org.mozilla.focus.home;

// $FF: synthetic class
public final class _$$Lambda$HomeFragment$LoadRootConfigTask$N7ozq39_Jbe9oQNEx5wgAWpoCWU implements HomeFragment.LoadConfigTask.OnConfigLoadedListener {
   // $FF: synthetic field
   private final HomeFragment.LoadRootConfigTask f$0;
   // $FF: synthetic field
   private final String[] f$1;
   // $FF: synthetic field
   private final HomeFragment.LoadRootConfigTask.OnRootConfigLoadedListener f$2;

   // $FF: synthetic method
   public _$$Lambda$HomeFragment$LoadRootConfigTask$N7ozq39_Jbe9oQNEx5wgAWpoCWU(HomeFragment.LoadRootConfigTask var1, String[] var2, HomeFragment.LoadRootConfigTask.OnRootConfigLoadedListener var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onConfigLoaded(String var1, int var2) {
      HomeFragment.LoadRootConfigTask.lambda$onPostExecute$0(this.f$0, this.f$1, this.f$2, var1, var2);
   }
}
