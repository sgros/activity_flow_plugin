package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$b8qjtHw_SwhVCQIFBk7EnKFy_9c implements Runnable {
   // $FF: synthetic field
   private final PassportActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final PassportActivity.ErrorRunnable f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final TLRPC.TL_secureRequiredType f$4;
   // $FF: synthetic field
   private final TLRPC.TL_secureRequiredType f$5;
   // $FF: synthetic field
   private final boolean f$6;
   // $FF: synthetic field
   private final ArrayList f$7;
   // $FF: synthetic field
   private final Runnable f$8;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$b8qjtHw_SwhVCQIFBk7EnKFy_9c(PassportActivity var1, TLRPC.TL_error var2, PassportActivity.ErrorRunnable var3, boolean var4, TLRPC.TL_secureRequiredType var5, TLRPC.TL_secureRequiredType var6, boolean var7, ArrayList var8, Runnable var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
   }

   public final void run() {
      this.f$0.lambda$null$60$PassportActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
   }
}
