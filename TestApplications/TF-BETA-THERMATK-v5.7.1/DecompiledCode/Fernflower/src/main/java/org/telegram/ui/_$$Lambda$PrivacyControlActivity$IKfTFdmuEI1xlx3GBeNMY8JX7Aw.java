package org.telegram.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$PrivacyControlActivity$IKfTFdmuEI1xlx3GBeNMY8JX7Aw implements OnClickListener {
   // $FF: synthetic field
   private final PrivacyControlActivity f$0;
   // $FF: synthetic field
   private final SharedPreferences f$1;

   // $FF: synthetic method
   public _$$Lambda$PrivacyControlActivity$IKfTFdmuEI1xlx3GBeNMY8JX7Aw(PrivacyControlActivity var1, SharedPreferences var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$processDone$5$PrivacyControlActivity(this.f$1, var1, var2);
   }
}
