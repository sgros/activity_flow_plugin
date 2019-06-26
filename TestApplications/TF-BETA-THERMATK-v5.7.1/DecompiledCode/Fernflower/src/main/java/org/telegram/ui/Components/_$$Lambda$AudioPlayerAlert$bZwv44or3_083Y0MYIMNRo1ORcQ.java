package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;

// $FF: synthetic class
public final class _$$Lambda$AudioPlayerAlert$bZwv44or3_083Y0MYIMNRo1ORcQ implements DialogsActivity.DialogsActivityDelegate {
   // $FF: synthetic field
   private final AudioPlayerAlert f$0;
   // $FF: synthetic field
   private final ArrayList f$1;

   // $FF: synthetic method
   public _$$Lambda$AudioPlayerAlert$bZwv44or3_083Y0MYIMNRo1ORcQ(AudioPlayerAlert var1, ArrayList var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void didSelectDialogs(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
      this.f$0.lambda$onSubItemClick$10$AudioPlayerAlert(this.f$1, var1, var2, var3, var4);
   }
}
