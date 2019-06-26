package org.telegram.ui;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.TimePicker;
import org.telegram.ui.Cells.TextSettingsCell;

// $FF: synthetic class
public final class _$$Lambda$ThemeActivity$NM7fAI0FGrIygn_Tl1Tnvhrr91Y implements OnTimeSetListener {
   // $FF: synthetic field
   private final ThemeActivity f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TextSettingsCell f$2;

   // $FF: synthetic method
   public _$$Lambda$ThemeActivity$NM7fAI0FGrIygn_Tl1Tnvhrr91Y(ThemeActivity var1, int var2, TextSettingsCell var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onTimeSet(TimePicker var1, int var2, int var3) {
      this.f$0.lambda$null$4$ThemeActivity(this.f$1, this.f$2, var1, var2, var3);
   }
}
