package org.mozilla.focus.home;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import org.mozilla.focus.history.model.Site;

// $FF: synthetic class
public final class _$$Lambda$HomeFragment$SiteItemClickListener$4IB3ndGZ_ufqnkodgjuU_qaCzyA implements PopupMenu.OnMenuItemClickListener {
   // $FF: synthetic field
   private final HomeFragment.SiteItemClickListener f$0;
   // $FF: synthetic field
   private final Site f$1;

   // $FF: synthetic method
   public _$$Lambda$HomeFragment$SiteItemClickListener$4IB3ndGZ_ufqnkodgjuU_qaCzyA(HomeFragment.SiteItemClickListener var1, Site var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final boolean onMenuItemClick(MenuItem var1) {
      return HomeFragment.SiteItemClickListener.lambda$onLongClick$1(this.f$0, this.f$1, var1);
   }
}
