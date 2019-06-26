package org.telegram.messenger;

import android.content.Context;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$0tV_MJuVJAhZ10ST8ytcL1B6vB4 implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final Context f$1;
   // $FF: synthetic field
   private final AlertDialog f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final byte[] f$4;
   // $FF: synthetic field
   private final TLRPC.User f$5;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$0tV_MJuVJAhZ10ST8ytcL1B6vB4(SecretChatHelper var1, Context var2, AlertDialog var3, TLObject var4, byte[] var5, TLRPC.User var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$25$SecretChatHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
