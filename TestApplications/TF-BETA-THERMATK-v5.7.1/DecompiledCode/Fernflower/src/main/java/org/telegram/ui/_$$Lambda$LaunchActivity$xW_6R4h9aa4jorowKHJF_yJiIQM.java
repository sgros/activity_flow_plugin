package org.telegram.ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Cells.LanguageCell;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$xW_6R4h9aa4jorowKHJF_yJiIQM implements OnClickListener {
   // $FF: synthetic field
   private final LocaleController.LocaleInfo[] f$0;
   // $FF: synthetic field
   private final LanguageCell[] f$1;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$xW_6R4h9aa4jorowKHJF_yJiIQM(LocaleController.LocaleInfo[] var1, LanguageCell[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(View var1) {
      LaunchActivity.lambda$showLanguageAlertInternal$49(this.f$0, this.f$1, var1);
   }
}
