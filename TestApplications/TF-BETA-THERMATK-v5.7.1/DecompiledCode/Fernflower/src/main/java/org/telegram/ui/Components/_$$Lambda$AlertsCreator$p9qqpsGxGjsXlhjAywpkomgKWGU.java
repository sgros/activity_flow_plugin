package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$p9qqpsGxGjsXlhjAywpkomgKWGU implements OnClickListener {
   // $FF: synthetic field
   private final boolean f$0;
   // $FF: synthetic field
   private final NumberPicker f$1;
   // $FF: synthetic field
   private final NumberPicker f$2;
   // $FF: synthetic field
   private final NumberPicker f$3;
   // $FF: synthetic field
   private final AlertsCreator.DatePickerDelegate f$4;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$p9qqpsGxGjsXlhjAywpkomgKWGU(boolean var1, NumberPicker var2, NumberPicker var3, NumberPicker var4, AlertsCreator.DatePickerDelegate var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createDatePickerDialog$18(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
