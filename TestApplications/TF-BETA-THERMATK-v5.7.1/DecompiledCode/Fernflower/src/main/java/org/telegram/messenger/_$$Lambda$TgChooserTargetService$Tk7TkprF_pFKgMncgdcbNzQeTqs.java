package org.telegram.messenger;

import android.content.ComponentName;
import java.util.List;
import java.util.concurrent.CountDownLatch;

// $FF: synthetic class
public final class _$$Lambda$TgChooserTargetService$Tk7TkprF_pFKgMncgdcbNzQeTqs implements Runnable {
   // $FF: synthetic field
   private final TgChooserTargetService f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final List f$2;
   // $FF: synthetic field
   private final ComponentName f$3;
   // $FF: synthetic field
   private final CountDownLatch f$4;

   // $FF: synthetic method
   public _$$Lambda$TgChooserTargetService$Tk7TkprF_pFKgMncgdcbNzQeTqs(TgChooserTargetService var1, int var2, List var3, ComponentName var4, CountDownLatch var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$onGetChooserTargets$0$TgChooserTargetService(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
