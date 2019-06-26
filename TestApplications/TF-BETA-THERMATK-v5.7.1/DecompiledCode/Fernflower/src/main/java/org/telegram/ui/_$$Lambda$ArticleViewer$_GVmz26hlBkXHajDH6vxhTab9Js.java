package org.telegram.ui;

import org.telegram.ui.Components.SeekBar;

// $FF: synthetic class
public final class _$$Lambda$ArticleViewer$_GVmz26hlBkXHajDH6vxhTab9Js implements SeekBar.SeekBarDelegate {
   // $FF: synthetic field
   private final ArticleViewer f$0;

   // $FF: synthetic method
   public _$$Lambda$ArticleViewer$_GVmz26hlBkXHajDH6vxhTab9Js(ArticleViewer var1) {
      this.f$0 = var1;
   }

   public final void onSeekBarDrag(float var1) {
      this.f$0.lambda$setParentActivity$19$ArticleViewer(var1);
   }
}
