package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$yqjIwJxQHVRYAcsusKtM70xYOHo implements RequestDelegate {
   // $FF: synthetic field
   private final PassportActivity f$0;
   // $FF: synthetic field
   private final PassportActivity.ErrorRunnable f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final TLRPC.TL_secureRequiredType f$3;
   // $FF: synthetic field
   private final TLRPC.TL_secureRequiredType f$4;
   // $FF: synthetic field
   private final boolean f$5;
   // $FF: synthetic field
   private final ArrayList f$6;
   // $FF: synthetic field
   private final Runnable f$7;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$yqjIwJxQHVRYAcsusKtM70xYOHo(PassportActivity var1, PassportActivity.ErrorRunnable var2, boolean var3, TLRPC.TL_secureRequiredType var4, TLRPC.TL_secureRequiredType var5, boolean var6, ArrayList var7, Runnable var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$deleteValueInternal$61$PassportActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, var1, var2);
   }
}
