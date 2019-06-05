package org.mozilla.focus.bookmark;

import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.persistence.BookmarkModel;

// $FF: synthetic class
public final class _$$Lambda$BookmarkAdapter$JXtUfxkFNFgNxp71Na4vaKYplv0 implements OnClickListener {
   // $FF: synthetic field
   private final BookmarkAdapter f$0;
   // $FF: synthetic field
   private final BookmarkModel f$1;

   // $FF: synthetic method
   public _$$Lambda$BookmarkAdapter$JXtUfxkFNFgNxp71Na4vaKYplv0(BookmarkAdapter var1, BookmarkModel var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(View var1) {
      BookmarkAdapter.lambda$onBindViewHolder$0(this.f$0, this.f$1, var1);
   }
}
