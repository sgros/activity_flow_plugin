package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.ui.ActionBar.Theme;

// $FF: synthetic class
public final class _$$Lambda$ThemeActivity$ListAdapter$mOT1foTAY8nRoymoRurolXzJymU implements OnClickListener {
   // $FF: synthetic field
   private final ThemeActivity.ListAdapter f$0;
   // $FF: synthetic field
   private final Theme.ThemeInfo f$1;

   // $FF: synthetic method
   public _$$Lambda$ThemeActivity$ListAdapter$mOT1foTAY8nRoymoRurolXzJymU(ThemeActivity.ListAdapter var1, Theme.ThemeInfo var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$showOptionsForTheme$1$ThemeActivity$ListAdapter(this.f$1, var1, var2);
   }
}
