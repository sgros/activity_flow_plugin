package org.mozilla.focus.history;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import org.mozilla.focus.history.model.Site;

// $FF: synthetic class
public final class _$$Lambda$HistoryItemAdapter$zWP3JpvSFUCJaxizmKnYSFVKYpk implements PopupMenu.OnMenuItemClickListener {
   // $FF: synthetic field
   private final HistoryItemAdapter f$0;
   // $FF: synthetic field
   private final Site f$1;

   // $FF: synthetic method
   public _$$Lambda$HistoryItemAdapter$zWP3JpvSFUCJaxizmKnYSFVKYpk(HistoryItemAdapter var1, Site var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final boolean onMenuItemClick(MenuItem var1) {
      return HistoryItemAdapter.lambda$onBindViewHolder$0(this.f$0, this.f$1, var1);
   }
}
