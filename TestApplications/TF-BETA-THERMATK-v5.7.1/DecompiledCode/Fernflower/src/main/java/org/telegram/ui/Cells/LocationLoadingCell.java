package org.telegram.ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;

public class LocationLoadingCell extends FrameLayout {
   private RadialProgressView progressBar;
   private TextView textView;

   public LocationLoadingCell(Context var1) {
      super(var1);
      this.progressBar = new RadialProgressView(var1);
      this.addView(this.progressBar, LayoutHelper.createFrame(-2, -2, 17));
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setText(LocaleController.getString("NoResult", 2131559943));
      this.addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec((int)((float)AndroidUtilities.dp(56.0F) * 2.5F), 1073741824));
   }

   public void setLoading(boolean var1) {
      RadialProgressView var2 = this.progressBar;
      byte var3 = 0;
      byte var4;
      if (var1) {
         var4 = 0;
      } else {
         var4 = 4;
      }

      var2.setVisibility(var4);
      TextView var5 = this.textView;
      var4 = var3;
      if (var1) {
         var4 = 4;
      }

      var5.setVisibility(var4);
   }
}
