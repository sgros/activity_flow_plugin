package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;

// $FF: synthetic class
public final class _$$Lambda$MediaActivity$RwU3_9vRjx_ylKGSBWguLaWu_aA implements RecyclerListView.OnItemLongClickListener {
   // $FF: synthetic field
   private final MediaActivity f$0;
   // $FF: synthetic field
   private final MediaActivity.MediaPage f$1;

   // $FF: synthetic method
   public _$$Lambda$MediaActivity$RwU3_9vRjx_ylKGSBWguLaWu_aA(MediaActivity var1, MediaActivity.MediaPage var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final boolean onItemClick(View var1, int var2) {
      return this.f$0.lambda$createView$3$MediaActivity(this.f$1, var1, var2);
   }
}
