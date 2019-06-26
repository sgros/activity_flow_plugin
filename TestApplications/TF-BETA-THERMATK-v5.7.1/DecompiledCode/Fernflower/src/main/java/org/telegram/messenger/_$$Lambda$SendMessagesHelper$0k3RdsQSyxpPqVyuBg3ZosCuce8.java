package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$0k3RdsQSyxpPqVyuBg3ZosCuce8 implements RequestDelegate {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final BaseFragment f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_editMessage f$2;
   // $FF: synthetic field
   private final Runnable f$3;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$0k3RdsQSyxpPqVyuBg3ZosCuce8(SendMessagesHelper var1, BaseFragment var2, TLRPC.TL_messages_editMessage var3, Runnable var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$editMessage$11$SendMessagesHelper(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
