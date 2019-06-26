package org.telegram.messenger;

import android.content.Context;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$CyJGexOzIKpecJNn32s1dWnqMhg implements RequestDelegate {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final Context f$1;
   // $FF: synthetic field
   private final AlertDialog f$2;
   // $FF: synthetic field
   private final TLRPC.User f$3;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$CyJGexOzIKpecJNn32s1dWnqMhg(SecretChatHelper var1, Context var2, AlertDialog var3, TLRPC.User var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$startSecretChat$29$SecretChatHelper(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
