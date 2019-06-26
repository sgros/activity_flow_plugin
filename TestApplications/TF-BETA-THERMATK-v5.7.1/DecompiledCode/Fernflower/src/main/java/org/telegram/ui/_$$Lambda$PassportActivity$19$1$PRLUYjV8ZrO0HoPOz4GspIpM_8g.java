package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$19$1$PRLUYjV8ZrO0HoPOz4GspIpM_8g implements Runnable {
   // $FF: synthetic field
   private final PassportActivity.ErrorRunnable f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$19$1$PRLUYjV8ZrO0HoPOz4GspIpM_8g(PassportActivity.ErrorRunnable var1, TLRPC.TL_error var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      null.lambda$run$3(this.f$0, this.f$1, this.f$2);
   }
}
