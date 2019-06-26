package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$NotificationsSettingsActivity$9FhV71oy8_vyXyR3LWFGjX_RReE implements OnClickListener {
   // $FF: synthetic field
   private final NotificationsSettingsActivity f$0;
   // $FF: synthetic field
   private final ArrayList f$1;

   // $FF: synthetic method
   public _$$Lambda$NotificationsSettingsActivity$9FhV71oy8_vyXyR3LWFGjX_RReE(NotificationsSettingsActivity var1, ArrayList var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$showExceptionsAlert$9$NotificationsSettingsActivity(this.f$1, var1, var2);
   }
}
