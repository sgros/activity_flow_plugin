package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$TOP_U4EklnBKZLMjFtDvABD1mPg implements OnClickListener {
   // $FF: synthetic field
   private final TLRPC.TL_langPackLanguage f$0;
   // $FF: synthetic field
   private final LaunchActivity f$1;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$TOP_U4EklnBKZLMjFtDvABD1mPg(TLRPC.TL_langPackLanguage var1, LaunchActivity var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createLanguageAlert$2(this.f$0, this.f$1, var1, var2);
   }
}
