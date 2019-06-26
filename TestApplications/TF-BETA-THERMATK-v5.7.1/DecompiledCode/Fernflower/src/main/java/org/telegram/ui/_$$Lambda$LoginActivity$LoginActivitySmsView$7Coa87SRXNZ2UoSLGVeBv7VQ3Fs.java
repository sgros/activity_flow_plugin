package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivitySmsView$7Coa87SRXNZ2UoSLGVeBv7VQ3Fs implements RequestDelegate {
   // $FF: synthetic field
   private final LoginActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final Bundle f$1;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivitySmsView$7Coa87SRXNZ2UoSLGVeBv7VQ3Fs(LoginActivity.LoginActivitySmsView var1, Bundle var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$resendCode$2$LoginActivity$LoginActivitySmsView(this.f$1, var1, var2);
   }
}
