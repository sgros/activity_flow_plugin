package org.telegram.messenger;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$FOvPRdYvHRkFV5NyZdXuhMMalnM implements OnCancelListener {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$FOvPRdYvHRkFV5NyZdXuhMMalnM(SecretChatHelper var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onCancel(DialogInterface var1) {
      this.f$0.lambda$startSecretChat$30$SecretChatHelper(this.f$1, var1);
   }
}
