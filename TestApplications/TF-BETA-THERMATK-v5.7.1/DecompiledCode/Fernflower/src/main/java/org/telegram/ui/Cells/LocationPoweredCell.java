package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class LocationPoweredCell extends FrameLayout {
   private ImageView imageView;
   private TextView textView;
   private TextView textView2;

   public LocationPoweredCell(Context var1) {
      super(var1);
      LinearLayout var2 = new LinearLayout(var1);
      this.addView(var2, LayoutHelper.createFrame(-2, -2, 17));
      this.textView = new TextView(var1);
      this.textView.setTextSize(1, 16.0F);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      this.textView.setText("Powered by");
      var2.addView(this.textView, LayoutHelper.createLinear(-2, -2));
      this.imageView = new ImageView(var1);
      this.imageView.setImageResource(2131165390);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText3"), Mode.MULTIPLY));
      this.imageView.setPadding(0, AndroidUtilities.dp(2.0F), 0, 0);
      var2.addView(this.imageView, LayoutHelper.createLinear(35, -2));
      this.textView2 = new TextView(var1);
      this.textView2.setTextSize(1, 16.0F);
      this.textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      this.textView2.setText("Foursquare");
      var2.addView(this.textView2, LayoutHelper.createLinear(-2, -2));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0F), 1073741824));
   }
}
