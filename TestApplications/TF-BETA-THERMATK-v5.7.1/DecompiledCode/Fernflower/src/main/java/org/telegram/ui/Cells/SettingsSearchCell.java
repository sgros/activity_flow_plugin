package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.text.style.ImageSpan;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class SettingsSearchCell extends FrameLayout {
   private ImageView imageView;
   private int left;
   private boolean needDivider;
   private TextView textView;
   private TextView valueTextView;

   public SettingsSearchCell(Context var1) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 16.0F);
      TextView var2 = this.textView;
      byte var3;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      var2.setGravity(var3);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      var2 = this.textView;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      float var4;
      if (LocaleController.isRTL) {
         var4 = 16.0F;
      } else {
         var4 = 71.0F;
      }

      float var5;
      if (LocaleController.isRTL) {
         var5 = 71.0F;
      } else {
         var5 = 16.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var3, var4, 10.0F, var5, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
      this.valueTextView.setTextSize(1, 13.0F);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      var2 = this.valueTextView;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      var2.setGravity(var3);
      var2 = this.valueTextView;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      if (LocaleController.isRTL) {
         var4 = 16.0F;
      } else {
         var4 = 71.0F;
      }

      if (LocaleController.isRTL) {
         var5 = 71.0F;
      } else {
         var5 = 16.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -2.0F, var3, var4, 33.0F, var5, 0.0F));
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
      ImageView var6 = this.imageView;
      if (LocaleController.isRTL) {
         var3 = 5;
      } else {
         var3 = 3;
      }

      this.addView(var6, LayoutHelper.createFrame(48, 48.0F, var3, 10.0F, 8.0F, 10.0F, 0.0F));
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
         float var2;
         if (LocaleController.isRTL) {
            var2 = 0.0F;
         } else {
            var2 = (float)AndroidUtilities.dp((float)this.left);
         }

         float var3 = (float)(this.getMeasuredHeight() - 1);
         int var4 = this.getMeasuredWidth();
         int var5;
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp((float)this.left);
         } else {
            var5 = 0;
         }

         var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + this.needDivider, 1073741824));
   }

   public void setTextAndValue(CharSequence var1, String[] var2, boolean var3, boolean var4) {
      LayoutParams var5 = (LayoutParams)this.textView.getLayoutParams();
      SpannableStringBuilder var6;
      int var7;
      Drawable var8;
      if (var3) {
         this.valueTextView.setText(var1);
         var6 = new SpannableStringBuilder();

         for(var7 = 0; var7 < var2.length; ++var7) {
            if (var7 != 0) {
               var6.append(" > ");
               var8 = this.getContext().getResources().getDrawable(2131165815).mutate();
               var8.setBounds(0, 0, var8.getIntrinsicWidth(), var8.getIntrinsicHeight());
               var8.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), Mode.MULTIPLY));
               var6.setSpan(new SettingsSearchCell.VerticalImageSpan(var8), var6.length() - 2, var6.length() - 1, 33);
            }

            var6.append(var2[var7]);
         }

         this.textView.setText(var6);
         this.valueTextView.setVisibility(0);
         var5.topMargin = AndroidUtilities.dp(10.0F);
      } else {
         this.textView.setText(var1);
         if (var2 != null) {
            var6 = new SpannableStringBuilder();

            for(var7 = 0; var7 < var2.length; ++var7) {
               if (var7 != 0) {
                  var6.append(" > ");
                  var8 = this.getContext().getResources().getDrawable(2131165815).mutate();
                  var8.setBounds(0, 0, var8.getIntrinsicWidth(), var8.getIntrinsicHeight());
                  var8.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText2"), Mode.MULTIPLY));
                  var6.setSpan(new SettingsSearchCell.VerticalImageSpan(var8), var6.length() - 2, var6.length() - 1, 33);
               }

               var6.append(var2[var7]);
            }

            this.valueTextView.setText(var6);
            this.valueTextView.setVisibility(0);
            var5.topMargin = AndroidUtilities.dp(10.0F);
         } else {
            var5.topMargin = AndroidUtilities.dp(21.0F);
            this.valueTextView.setVisibility(8);
         }
      }

      var7 = AndroidUtilities.dp(16.0F);
      var5.rightMargin = var7;
      var5.leftMargin = var7;
      LayoutParams var9 = (LayoutParams)this.valueTextView.getLayoutParams();
      var7 = AndroidUtilities.dp(16.0F);
      var9.rightMargin = var7;
      var9.leftMargin = var7;
      this.imageView.setVisibility(8);
      this.needDivider = var4;
      this.setWillNotDraw(this.needDivider ^ true);
      this.left = 16;
   }

   public void setTextAndValueAndIcon(CharSequence var1, String[] var2, int var3, boolean var4) {
      this.textView.setText(var1);
      LayoutParams var11 = (LayoutParams)this.textView.getLayoutParams();
      boolean var5 = LocaleController.isRTL;
      float var6 = 16.0F;
      float var7;
      if (var5) {
         var7 = 16.0F;
      } else {
         var7 = 71.0F;
      }

      var11.leftMargin = AndroidUtilities.dp(var7);
      if (LocaleController.isRTL) {
         var7 = 71.0F;
      } else {
         var7 = 16.0F;
      }

      var11.rightMargin = AndroidUtilities.dp(var7);
      if (var2 != null) {
         SpannableStringBuilder var8 = new SpannableStringBuilder();

         for(int var9 = 0; var9 < var2.length; ++var9) {
            if (var9 != 0) {
               var8.append(" > ");
               Drawable var10 = this.getContext().getResources().getDrawable(2131165815).mutate();
               var10.setBounds(0, 0, var10.getIntrinsicWidth(), var10.getIntrinsicHeight());
               var10.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText2"), Mode.MULTIPLY));
               var8.setSpan(new SettingsSearchCell.VerticalImageSpan(var10), var8.length() - 2, var8.length() - 1, 33);
            }

            var8.append(var2[var9]);
         }

         this.valueTextView.setText(var8);
         this.valueTextView.setVisibility(0);
         var11.topMargin = AndroidUtilities.dp(10.0F);
         var11 = (LayoutParams)this.valueTextView.getLayoutParams();
         if (LocaleController.isRTL) {
            var7 = 16.0F;
         } else {
            var7 = 71.0F;
         }

         var11.leftMargin = AndroidUtilities.dp(var7);
         var7 = var6;
         if (LocaleController.isRTL) {
            var7 = 71.0F;
         }

         var11.rightMargin = AndroidUtilities.dp(var7);
      } else {
         var11.topMargin = AndroidUtilities.dp(21.0F);
         this.valueTextView.setVisibility(8);
      }

      if (var3 != 0) {
         this.imageView.setImageResource(var3);
         this.imageView.setVisibility(0);
      } else {
         this.imageView.setVisibility(8);
      }

      this.left = 69;
      this.needDivider = var4;
      this.setWillNotDraw(this.needDivider ^ true);
   }

   public class VerticalImageSpan extends ImageSpan {
      public VerticalImageSpan(Drawable var2) {
         super(var2);
      }

      public void draw(Canvas var1, CharSequence var2, int var3, int var4, float var5, int var6, int var7, int var8, Paint var9) {
         Drawable var10 = this.getDrawable();
         var1.save();
         FontMetricsInt var11 = var9.getFontMetricsInt();
         var3 = var11.descent;
         var1.translate(var5, (float)(var7 + var3 - (var3 - var11.ascent) / 2 - (var10.getBounds().bottom - var10.getBounds().top) / 2));
         if (LocaleController.isRTL) {
            var1.scale(-1.0F, 1.0F, (float)(var10.getIntrinsicWidth() / 2), (float)(var10.getIntrinsicHeight() / 2));
         }

         var10.draw(var1);
         var1.restore();
      }

      public int getSize(Paint var1, CharSequence var2, int var3, int var4, FontMetricsInt var5) {
         Rect var9 = this.getDrawable().getBounds();
         if (var5 != null) {
            FontMetricsInt var8 = var1.getFontMetricsInt();
            int var6 = var8.descent;
            int var7 = var8.ascent;
            var3 = var9.bottom;
            var4 = var9.top;
            var6 = var7 + (var6 - var7) / 2;
            var3 = (var3 - var4) / 2;
            var5.ascent = var6 - var3;
            var5.top = var5.ascent;
            var5.bottom = var6 + var3;
            var5.descent = var5.bottom;
         }

         return var9.right;
      }
   }
}
