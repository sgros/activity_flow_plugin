package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class ShadowSectionCell extends View {
   private int size;

   public ShadowSectionCell(Context var1) {
      this(var1, 12);
   }

   public ShadowSectionCell(Context var1, int var2) {
      super(var1);
      this.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
      this.size = var2;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)this.size), 1073741824));
   }
}
