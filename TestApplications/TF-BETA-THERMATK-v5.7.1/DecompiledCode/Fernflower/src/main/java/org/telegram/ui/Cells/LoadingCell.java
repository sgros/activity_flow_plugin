package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;

public class LoadingCell extends FrameLayout {
   private int height;
   private RadialProgressView progressBar;

   public LoadingCell(Context var1) {
      this(var1, AndroidUtilities.dp(40.0F), AndroidUtilities.dp(54.0F));
   }

   public LoadingCell(Context var1, int var2, int var3) {
      super(var1);
      this.height = var3;
      this.progressBar = new RadialProgressView(var1);
      this.progressBar.setSize(var2);
      this.addView(this.progressBar, LayoutHelper.createFrame(-2, -2, 17));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(this.height, 1073741824));
   }
}
