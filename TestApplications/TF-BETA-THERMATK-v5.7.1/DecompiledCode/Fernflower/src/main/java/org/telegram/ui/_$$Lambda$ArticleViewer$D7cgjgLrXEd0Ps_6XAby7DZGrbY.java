package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$D7cgjgLrXEd0Ps_6XAby7DZGrbY implements RecyclerListView.OnItemClickListener {
   // $FF: synthetic field
   private final ArticleViewer f$0;
   // $FF: synthetic field
   private final ArticleViewer.WebpageAdapter f$1;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$D7cgjgLrXEd0Ps_6XAby7DZGrbY(ArticleViewer var1, ArticleViewer.WebpageAdapter var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onItemClick(View var1, int var2) {
      this.f$0.lambda$setParentActivity$11$ArticleViewer(this.f$1, var1, var2);
   }
}
