package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.LocaleController;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$__OYvDrFInFakn0WORCpq62kH08 implements OnClickListener {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final LocaleController.LocaleInfo[] f$1;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$__OYvDrFInFakn0WORCpq62kH08(LaunchActivity var1, LocaleController.LocaleInfo[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$showLanguageAlertInternal$51$LaunchActivity(this.f$1, var1, var2);
   }
}
