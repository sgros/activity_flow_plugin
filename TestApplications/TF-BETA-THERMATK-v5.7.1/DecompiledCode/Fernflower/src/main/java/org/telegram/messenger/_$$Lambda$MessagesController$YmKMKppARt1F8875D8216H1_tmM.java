package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$YmKMKppARt1F8875D8216H1_tmM implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$YmKMKppARt1F8875D8216H1_tmM(MessagesController var1, long var2, boolean var4, long var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$saveWallpaperToServer$60$MessagesController(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
