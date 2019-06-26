package org.telegram.ui.Cells;

import org.telegram.ui.Components.SeekBarView;

// $FF: synthetic class
public final class _$$Lambda$GN4ZDAm3ZJLTBxjR6_2pFIyDFuo implements SeekBarView.SeekBarViewDelegate {
   // $FF: synthetic field
   private final BrightnessControlCell f$0;

   // $FF: synthetic method
   public _$$Lambda$GN4ZDAm3ZJLTBxjR6_2pFIyDFuo(BrightnessControlCell var1) {
      this.f$0 = var1;
   }

   public final void onSeekBarDrag(float var1) {
      this.f$0.didChangedValue(var1);
   }
}
