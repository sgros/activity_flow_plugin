package org.telegram.ui;

import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$3j0ynD_14Ne6162eLDw5z0libqA implements RequestDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final LocaleController.LocaleInfo[] f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$3j0ynD_14Ne6162eLDw5z0libqA(LaunchActivity var1, LocaleController.LocaleInfo[] var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$showLanguageAlert$55$LaunchActivity(this.f$1, this.f$2, var1, var2);
   }
}
