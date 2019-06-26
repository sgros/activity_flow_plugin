package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
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

public class ManageChatTextCell extends FrameLayout {
   private boolean divider;
   private ImageView imageView;
   private SimpleTextView textView;
   private SimpleTextView valueTextView;

   public ManageChatTextCell(Context var1) {
      super(var1);
      this.textView = new SimpleTextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(16);
      SimpleTextView var2 = this.textView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var2.setGravity(var5);
      this.addView(this.textView);
      this.valueTextView = new SimpleTextView(var1);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteValueText"));
      this.valueTextView.setTextSize(16);
      var2 = this.valueTextView;
      var5 = var4;
      if (LocaleController.isRTL) {
         var5 = 3;
      }

      var2.setGravity(var5);
      this.addView(this.valueTextView);
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
      this.addView(this.imageView);
   }

   public SimpleTextView getTextView() {
      return this.textView;
   }

   public SimpleTextView getValueTextView() {
      return this.valueTextView;
   }

   protected void onDraw(Canvas var1) {
      if (this.divider) {
         var1.drawLine((float)AndroidUtilities.dp(71.0F), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var5 -= var3;
      int var6 = (var5 - this.valueTextView.getTextHeight()) / 2;
      if (LocaleController.isRTL) {
         var3 = AndroidUtilities.dp(24.0F);
      } else {
         var3 = 0;
      }

      SimpleTextView var7 = this.valueTextView;
      var7.layout(var3, var6, var7.getMeasuredWidth() + var3, this.valueTextView.getMeasuredHeight() + var6);
      var5 = (var5 - this.textView.getTextHeight()) / 2;
      if (!LocaleController.isRTL) {
         var3 = AndroidUtilities.dp(71.0F);
      } else {
         var3 = AndroidUtilities.dp(24.0F);
      }

      var7 = this.textView;
      var7.layout(var3, var5, var7.getMeasuredWidth() + var3, this.textView.getMeasuredHeight() + var5);
      var3 = AndroidUtilities.dp(9.0F);
      if (!LocaleController.isRTL) {
         var2 = AndroidUtilities.dp(21.0F);
      } else {
         var2 = var4 - var2 - this.imageView.getMeasuredWidth() - AndroidUtilities.dp(21.0F);
      }

      ImageView var8 = this.imageView;
      var8.layout(var2, var3, var8.getMeasuredWidth() + var2, this.imageView.getMeasuredHeight() + var3);
   }

   protected void onMeasure(int var1, int var2) {
      var2 = MeasureSpec.getSize(var1);
      var1 = AndroidUtilities.dp(48.0F);
      this.valueTextView.measure(MeasureSpec.makeMeasureSpec(var2 - AndroidUtilities.dp(24.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0F), 1073741824));
      this.textView.measure(MeasureSpec.makeMeasureSpec(var2 - AndroidUtilities.dp(95.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0F), 1073741824));
      this.imageView.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
      this.setMeasuredDimension(var2, AndroidUtilities.dp(56.0F) + this.divider);
   }

   public void setColors(String var1, String var2) {
      this.textView.setTextColor(Theme.getColor(var2));
      this.textView.setTag(var2);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(var1), Mode.MULTIPLY));
      this.imageView.setTag(var1);
   }

   public void setText(String var1, String var2, int var3, boolean var4) {
      this.textView.setText(var1);
      if (var2 != null) {
         this.valueTextView.setText(var2);
         this.valueTextView.setVisibility(0);
      } else {
         this.valueTextView.setVisibility(4);
      }

      this.imageView.setPadding(0, AndroidUtilities.dp(5.0F), 0, 0);
      this.imageView.setImageResource(var3);
      this.divider = var4;
      this.setWillNotDraw(this.divider ^ true);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }
}
