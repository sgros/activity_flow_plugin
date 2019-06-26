package org.telegram.ui.Components;

import android.view.ViewTreeObserver.OnPreDrawListener;

// $FF: synthetic class
public final class _$$Lambda$qzh_QoBZ7K2XdUWK2VAJcGTe1OY implements Runnable {
   // $FF: synthetic field
   private final OnPreDrawListener f$0;

   // $FF: synthetic method
   public _$$Lambda$qzh_QoBZ7K2XdUWK2VAJcGTe1OY(OnPreDrawListener var1) {
      this.f$0 = var1;
   }

   public final void run() {
      this.f$0.onPreDraw();
   }
}
