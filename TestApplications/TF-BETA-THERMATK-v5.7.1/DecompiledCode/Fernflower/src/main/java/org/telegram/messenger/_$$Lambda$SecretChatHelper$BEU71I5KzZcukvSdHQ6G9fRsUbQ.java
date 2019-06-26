package org.telegram.messenger;

import android.content.Context;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$BEU71I5KzZcukvSdHQ6G9fRsUbQ implements RequestDelegate {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final Context f$1;
   // $FF: synthetic field
   private final AlertDialog f$2;
   // $FF: synthetic field
   private final byte[] f$3;
   // $FF: synthetic field
   private final TLRPC.User f$4;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$BEU71I5KzZcukvSdHQ6G9fRsUbQ(SecretChatHelper var1, Context var2, AlertDialog var3, byte[] var4, TLRPC.User var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$27$SecretChatHelper(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
