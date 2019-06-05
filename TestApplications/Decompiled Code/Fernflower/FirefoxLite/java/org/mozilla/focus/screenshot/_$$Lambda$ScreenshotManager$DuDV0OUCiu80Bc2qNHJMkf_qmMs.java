package org.mozilla.focus.screenshot;

import android.arch.lifecycle.Observer;
import android.support.v4.util.Pair;
import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$ScreenshotManager$DuDV0OUCiu80Bc2qNHJMkf_qmMs implements Observer {
   // $FF: synthetic field
   private final ScreenshotManager f$0;
   // $FF: synthetic field
   private final CountDownLatch f$1;

   // $FF: synthetic method
   public _$$Lambda$ScreenshotManager$DuDV0OUCiu80Bc2qNHJMkf_qmMs(ScreenshotManager var1, CountDownLatch var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onChanged(Object var1) {
      ScreenshotManager.lambda$initFromRemote$0(this.f$0, this.f$1, (Pair)var1);
   }
}
