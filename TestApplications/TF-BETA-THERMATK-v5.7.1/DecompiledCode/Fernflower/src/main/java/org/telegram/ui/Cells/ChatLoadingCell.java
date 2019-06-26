package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;

public class ChatLoadingCell extends FrameLayout {
   private FrameLayout frameLayout;
   private RadialProgressView progressBar;

   public ChatLoadingCell(Context var1) {
      super(var1);
      this.frameLayout = new FrameLayout(var1);
      this.frameLayout.setBackgroundResource(2131165872);
      this.frameLayout.getBackground().setColorFilter(Theme.colorFilter);
      this.addView(this.frameLayout, LayoutHelper.createFrame(36, 36, 17));
      this.progressBar = new RadialProgressView(var1);
      this.progressBar.setSize(AndroidUtilities.dp(28.0F));
      this.progressBar.setProgressColor(Theme.getColor("chat_serviceText"));
      this.frameLayout.addView(this.progressBar, LayoutHelper.createFrame(32, 32, 17));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0F), 1073741824));
   }

   public void setProgressVisible(boolean var1) {
      FrameLayout var2 = this.frameLayout;
      byte var3;
      if (var1) {
         var3 = 0;
      } else {
         var3 = 4;
      }

      var2.setVisibility(var3);
   }
}
