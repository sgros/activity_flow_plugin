package org.mozilla.focus.bookmark;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import org.mozilla.focus.persistence.BookmarkModel;

// $FF: synthetic class
public final class _$$Lambda$BookmarkAdapter$TZbcuR8Z7wK7_KE_yTvlYO8Jg3Y implements PopupMenu.OnMenuItemClickListener {
   // $FF: synthetic field
   private final BookmarkAdapter f$0;
   // $FF: synthetic field
   private final BookmarkModel f$1;

   // $FF: synthetic method
   public _$$Lambda$BookmarkAdapter$TZbcuR8Z7wK7_KE_yTvlYO8Jg3Y(BookmarkAdapter var1, BookmarkModel var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final boolean onMenuItemClick(MenuItem var1) {
      return BookmarkAdapter.lambda$onBindViewHolder$1(this.f$0, this.f$1, var1);
   }
}
