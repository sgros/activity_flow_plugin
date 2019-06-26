package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$1W4kZzPjzRDtXCpazovyaTPi9gk implements RequestDelegate {
   // $FF: synthetic field
   private final CancelAccountDeletionActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_account_confirmPhone f$1;

   // $FF: synthetic method
   public _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$1W4kZzPjzRDtXCpazovyaTPi9gk(CancelAccountDeletionActivity.LoginActivitySmsView var1, TLRPC.TL_account_confirmPhone var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onNextPressed$7$CancelAccountDeletionActivity$LoginActivitySmsView(this.f$1, var1, var2);
   }
}
