package org.telegram.ui.Components;

import android.graphics.Bitmap;
import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$PhotoFilterView$EGLThread$mV7yxJKIkka2xtURA3o1r9_1nD8 implements Runnable {
   // $FF: synthetic field
   private final PhotoFilterView.EGLThread f$0;
   // $FF: synthetic field
   private final Bitmap[] f$1;
   // $FF: synthetic field
   private final CountDownLatch f$2;

   // $FF: synthetic method
   public _$$Lambda$PhotoFilterView$EGLThread$mV7yxJKIkka2xtURA3o1r9_1nD8(PhotoFilterView.EGLThread var1, Bitmap[] var2, CountDownLatch var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$getTexture$0$PhotoFilterView$EGLThread(this.f$1, this.f$2);
   }
}
