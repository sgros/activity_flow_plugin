package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$NewContactActivity$1$D3bcNIiNfpYsfTq2W2DLeJJEU84 implements OnClickListener {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.TL_inputPhoneContact f$1;

   // $FF: synthetic method
   public _$$Lambda$NewContactActivity$1$D3bcNIiNfpYsfTq2W2DLeJJEU84(Object var1, TLRPC.TL_inputPhoneContact var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$0$NewContactActivity$1(this.f$1, var1, var2);
   }
}
