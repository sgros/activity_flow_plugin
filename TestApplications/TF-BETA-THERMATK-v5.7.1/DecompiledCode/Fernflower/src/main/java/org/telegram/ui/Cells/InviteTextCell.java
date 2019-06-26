package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;

public class InviteTextCell extends FrameLayout {
   private ImageView imageView;
   private SimpleTextView textView;

   public InviteTextCell(Context var1) {
      super(var1);
      this.textView = new SimpleTextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(17);
      SimpleTextView var2 = this.textView;
      byte var3;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      var2.setGravity(var3);
      this.addView(this.textView);
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
      this.addView(this.imageView);
   }

   public SimpleTextView getTextView() {
      return this.textView;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var5 - var3;
      var5 = (var6 - this.textView.getTextHeight()) / 2;
      float var7;
      if (!LocaleController.isRTL) {
         var7 = 71.0F;
      } else {
         var7 = 24.0F;
      }

      var3 = AndroidUtilities.dp(var7);
      SimpleTextView var8 = this.textView;
      var8.layout(var3, var5, var8.getMeasuredWidth() + var3, this.textView.getMeasuredHeight() + var5);
      var3 = (var6 - this.imageView.getMeasuredHeight()) / 2;
      if (!LocaleController.isRTL) {
         var2 = AndroidUtilities.dp(20.0F);
      } else {
         var2 = var4 - var2 - this.imageView.getMeasuredWidth() - AndroidUtilities.dp(20.0F);
      }

      ImageView var9 = this.imageView;
      var9.layout(var2, var3, var9.getMeasuredWidth() + var2, this.imageView.getMeasuredHeight() + var3);
   }

   protected void onMeasure(int var1, int var2) {
      var1 = MeasureSpec.getSize(var1);
      var2 = AndroidUtilities.dp(72.0F);
      this.textView.measure(MeasureSpec.makeMeasureSpec(var1 - AndroidUtilities.dp(95.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0F), 1073741824));
      this.imageView.measure(MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE));
      this.setMeasuredDimension(var1, AndroidUtilities.dp(72.0F));
   }

   public void setTextAndIcon(String var1, int var2) {
      this.textView.setText(var1);
      this.imageView.setImageResource(var2);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }
}
