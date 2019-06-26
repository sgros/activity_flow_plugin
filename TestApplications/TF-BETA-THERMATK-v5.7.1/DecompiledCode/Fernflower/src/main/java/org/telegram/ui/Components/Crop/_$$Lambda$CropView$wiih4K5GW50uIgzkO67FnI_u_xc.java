package org.telegram.ui.Components.Crop;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

// $FF: synthetic class
public final class _$$Lambda$CropView$wiih4K5GW50uIgzkO67FnI_u_xc implements AnimatorUpdateListener {
   // $FF: synthetic field
   private final CropView f$0;
   // $FF: synthetic field
   private final float f$1;
   // $FF: synthetic field
   private final float[] f$2;
   // $FF: synthetic field
   private final float f$3;
   // $FF: synthetic field
   private final float f$4;

   // $FF: synthetic method
   public _$$Lambda$CropView$wiih4K5GW50uIgzkO67FnI_u_xc(CropView var1, float var2, float[] var3, float var4, float var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void onAnimationUpdate(ValueAnimator var1) {
      this.f$0.lambda$fitContentInBounds$1$CropView(this.f$1, this.f$2, this.f$3, this.f$4, var1);
   }
}
