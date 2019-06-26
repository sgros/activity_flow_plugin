package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;

public class TextCell extends FrameLayout {
   private ImageView imageView;
   private boolean needDivider;
   private SimpleTextView textView;
   private ImageView valueImageView;
   private SimpleTextView valueTextView;

   public TextCell(Context var1) {
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
      this.valueImageView = new ImageView(var1);
      this.valueImageView.setScaleType(ScaleType.CENTER);
      this.addView(this.valueImageView);
      this.setFocusable(true);
   }

   public SimpleTextView getTextView() {
      return this.textView;
   }

   public ImageView getValueImageView() {
      return this.valueImageView;
   }

   public SimpleTextView getValueTextView() {
      return this.valueTextView;
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         boolean var2 = LocaleController.isRTL;
         float var3 = 68.0F;
         float var4;
         if (var2) {
            var4 = 0.0F;
         } else {
            if (this.imageView.getVisibility() == 0) {
               var4 = 68.0F;
            } else {
               var4 = 20.0F;
            }

            var4 = (float)AndroidUtilities.dp(var4);
         }

         float var5 = (float)(this.getMeasuredHeight() - 1);
         int var6 = this.getMeasuredWidth();
         int var7;
         if (LocaleController.isRTL) {
            if (this.imageView.getVisibility() != 0) {
               var3 = 20.0F;
            }

            var7 = AndroidUtilities.dp(var3);
         } else {
            var7 = 0;
         }

         var1.drawLine(var4, var5, (float)(var6 - var7), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var3 = var5 - var3;
      var4 -= var2;
      var5 = (var3 - this.valueTextView.getTextHeight()) / 2;
      if (LocaleController.isRTL) {
         var2 = AndroidUtilities.dp(23.0F);
      } else {
         var2 = 0;
      }

      SimpleTextView var6 = this.valueTextView;
      var6.layout(var2, var5, var6.getMeasuredWidth() + var2, this.valueTextView.getMeasuredHeight() + var5);
      var5 = (var3 - this.textView.getTextHeight()) / 2;
      var1 = LocaleController.isRTL;
      float var7 = 71.0F;
      if (var1) {
         var2 = this.getMeasuredWidth();
         int var8 = this.textView.getMeasuredWidth();
         if (this.imageView.getVisibility() != 0) {
            var7 = 23.0F;
         }

         var2 = var2 - var8 - AndroidUtilities.dp(var7);
      } else {
         if (this.imageView.getVisibility() != 0) {
            var7 = 23.0F;
         }

         var2 = AndroidUtilities.dp(var7);
      }

      var6 = this.textView;
      var6.layout(var2, var5, var6.getMeasuredWidth() + var2, this.textView.getMeasuredHeight() + var5);
      ImageView var9;
      if (this.imageView.getVisibility() == 0) {
         var5 = AndroidUtilities.dp(5.0F);
         if (!LocaleController.isRTL) {
            var2 = AndroidUtilities.dp(21.0F);
         } else {
            var2 = var4 - this.imageView.getMeasuredWidth() - AndroidUtilities.dp(21.0F);
         }

         var9 = this.imageView;
         var9.layout(var2, var5, var9.getMeasuredWidth() + var2, this.imageView.getMeasuredHeight() + var5);
      }

      if (this.valueImageView.getVisibility() == 0) {
         var3 = (var3 - this.valueImageView.getMeasuredHeight()) / 2;
         if (LocaleController.isRTL) {
            var2 = AndroidUtilities.dp(23.0F);
         } else {
            var2 = var4 - this.valueImageView.getMeasuredWidth() - AndroidUtilities.dp(23.0F);
         }

         var9 = this.valueImageView;
         var9.layout(var2, var3, var9.getMeasuredWidth() + var2, this.valueImageView.getMeasuredHeight() + var3);
      }

   }

   protected void onMeasure(int var1, int var2) {
      var2 = MeasureSpec.getSize(var1);
      var1 = AndroidUtilities.dp(48.0F);
      this.valueTextView.measure(MeasureSpec.makeMeasureSpec(var2 - AndroidUtilities.dp(23.0F), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0F), 1073741824));
      this.textView.measure(MeasureSpec.makeMeasureSpec(var2 - AndroidUtilities.dp(95.0F) - this.valueTextView.getTextWidth(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0F), 1073741824));
      if (this.imageView.getVisibility() == 0) {
         this.imageView.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
      }

      if (this.valueImageView.getVisibility() == 0) {
         this.valueImageView.measure(MeasureSpec.makeMeasureSpec(var2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE));
      }

      this.setMeasuredDimension(var2, AndroidUtilities.dp(50.0F) + this.needDivider);
   }

   public void setColors(String var1, String var2) {
      this.textView.setTextColor(Theme.getColor(var2));
      this.textView.setTag(var2);
      if (var1 != null) {
         this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(var1), Mode.MULTIPLY));
         this.imageView.setTag(var1);
      }

   }

   public void setText(String var1, boolean var2) {
      this.textView.setText(var1);
      this.valueTextView.setText((CharSequence)null);
      this.imageView.setVisibility(8);
      this.valueTextView.setVisibility(8);
      this.valueImageView.setVisibility(8);
      this.needDivider = var2;
      this.setWillNotDraw(this.needDivider ^ true);
   }

   public void setTextAndIcon(String var1, int var2, boolean var3) {
      this.textView.setText(var1);
      this.valueTextView.setText((CharSequence)null);
      this.imageView.setImageResource(var2);
      this.imageView.setVisibility(0);
      this.valueTextView.setVisibility(8);
      this.valueImageView.setVisibility(8);
      this.imageView.setPadding(0, AndroidUtilities.dp(7.0F), 0, 0);
      this.needDivider = var3;
      this.setWillNotDraw(this.needDivider ^ true);
   }

   public void setTextAndValue(String var1, String var2, boolean var3) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.valueTextView.setVisibility(0);
      this.imageView.setVisibility(8);
      this.valueImageView.setVisibility(8);
      this.needDivider = var3;
      this.setWillNotDraw(this.needDivider ^ true);
   }

   public void setTextAndValueAndIcon(String var1, String var2, int var3, boolean var4) {
      this.textView.setText(var1);
      this.valueTextView.setText(var2);
      this.valueTextView.setVisibility(0);
      this.valueImageView.setVisibility(8);
      this.imageView.setVisibility(0);
      this.imageView.setPadding(0, AndroidUtilities.dp(7.0F), 0, 0);
      this.imageView.setImageResource(var3);
      this.needDivider = var4;
      this.setWillNotDraw(this.needDivider ^ true);
   }

   public void setTextAndValueDrawable(String var1, Drawable var2, boolean var3) {
      this.textView.setText(var1);
      this.valueTextView.setText((CharSequence)null);
      this.valueImageView.setVisibility(0);
      this.valueImageView.setImageDrawable(var2);
      this.valueTextView.setVisibility(8);
      this.imageView.setVisibility(8);
      this.imageView.setPadding(0, AndroidUtilities.dp(7.0F), 0, 0);
      this.needDivider = var3;
      this.setWillNotDraw(this.needDivider ^ true);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }
}
