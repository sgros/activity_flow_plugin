package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DownloadController$Vy_RFVunDaT6j2u2tHT0TGLKrLk implements RequestDelegate {
   // $FF: synthetic field
   private final DownloadController f$0;

   // $FF: synthetic method
   public _$$Lambda$DownloadController$Vy_RFVunDaT6j2u2tHT0TGLKrLk(DownloadController var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadAutoDownloadConfig$2$DownloadController(var1, var2);
   }
}
