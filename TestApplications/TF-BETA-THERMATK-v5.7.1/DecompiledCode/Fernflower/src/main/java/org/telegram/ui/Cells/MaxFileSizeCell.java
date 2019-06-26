package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SeekBarView;

public class MaxFileSizeCell extends FrameLayout {
   private long currentSize;
   private SeekBarView seekBarView;
   private TextView sizeTextView;
   private TextView textView;

   public MaxFileSizeCell(Context var1) {
      super(var1);
      this.setWillNotDraw(false);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      TextView var2 = this.textView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var2.setGravity(var5 | 48);
      this.textView.setEllipsize(TruncateAt.END);
      var2 = this.textView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      this.addView(var2, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, 21.0F, 13.0F, 21.0F, 0.0F));
      this.sizeTextView = new TextView(var1);
      this.sizeTextView.setTextColor(Theme.getColor("dialogTextBlue2"));
      this.sizeTextView.setTextSize(1, 16.0F);
      this.sizeTextView.setLines(1);
      this.sizeTextView.setMaxLines(1);
      this.sizeTextView.setSingleLine(true);
      var2 = this.sizeTextView;
      if (LocaleController.isRTL) {
         var5 = 3;
      } else {
         var5 = 5;
      }

      var2.setGravity(var5 | 48);
      var2 = this.sizeTextView;
      var5 = var4;
      if (LocaleController.isRTL) {
         var5 = 3;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, -1.0F, var5 | 48, 21.0F, 13.0F, 21.0F, 0.0F));
      this.seekBarView = new SeekBarView(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            if (var1.getAction() == 0) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            return super.onTouchEvent(var1);
         }
      };
      this.seekBarView.setReportChanges(true);
      this.seekBarView.setDelegate(new _$$Lambda$MaxFileSizeCell$cPUnEl5DY5tp_A3IQaD5cETcr3k(this));
      this.addView(this.seekBarView, LayoutHelper.createFrame(-1, 30.0F, 51, 10.0F, 40.0F, 10.0F, 0.0F));
   }

   protected void didChangedSizeValue(int var1) {
   }

   public boolean dispatchTouchEvent(MotionEvent var1) {
      return !this.isEnabled() ? true : super.dispatchTouchEvent(var1);
   }

   public long getSize() {
      return this.currentSize;
   }

   // $FF: synthetic method
   public void lambda$new$0$MaxFileSizeCell(float var1) {
      float var3;
      float var4;
      if (var1 <= 0.25F) {
         float var2 = (float)512000;
         var3 = 536576.0F;
         var4 = var1;
         var1 = var2;
      } else {
         var4 = var1 - 0.25F;
         if (var4 < 0.25F) {
            var1 = (float)1048576;
            var3 = 9437184.0F;
         } else {
            var4 -= 0.25F;
            if (var4 <= 0.25F) {
               var1 = (float)10485760;
               var3 = 9.437184E7F;
            } else {
               var4 -= 0.25F;
               var1 = (float)104857600;
               var3 = 1.50575514E9F;
            }
         }
      }

      int var5 = (int)(var1 + var4 / 0.25F * var3);
      TextView var6 = this.sizeTextView;
      long var7 = (long)var5;
      var6.setText(LocaleController.formatString("AutodownloadSizeLimitUpTo", 2131558801, AndroidUtilities.formatFileSize(var7)));
      this.currentSize = var7;
      this.didChangedSizeValue(var5);
   }

   protected void onDraw(Canvas var1) {
      float var2;
      if (LocaleController.isRTL) {
         var2 = 0.0F;
      } else {
         var2 = (float)AndroidUtilities.dp(20.0F);
      }

      float var3 = (float)(this.getMeasuredHeight() - 1);
      int var4 = this.getMeasuredWidth();
      int var5;
      if (LocaleController.isRTL) {
         var5 = AndroidUtilities.dp(20.0F);
      } else {
         var5 = 0;
      }

      var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      return !this.isEnabled() ? true : super.onInterceptTouchEvent(var1);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0F), 1073741824));
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(80.0F));
      var1 = this.getMeasuredWidth() - AndroidUtilities.dp(42.0F);
      this.sizeTextView.measure(MeasureSpec.makeMeasureSpec(var1, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0F), 1073741824));
      var1 = Math.max(AndroidUtilities.dp(10.0F), var1 - this.sizeTextView.getMeasuredWidth() - AndroidUtilities.dp(8.0F));
      this.textView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0F), 1073741824));
      this.seekBarView.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth() - AndroidUtilities.dp(20.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0F), 1073741824));
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return !this.isEnabled() ? true : super.onTouchEvent(var1);
   }

   public void setEnabled(boolean var1, ArrayList var2) {
      super.setEnabled(var1);
      float var3 = 1.0F;
      float var5;
      if (var2 != null) {
         TextView var4 = this.textView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var4, "alpha", new float[]{var5}));
         SeekBarView var8 = this.seekBarView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var8, "alpha", new float[]{var5}));
         var4 = this.sizeTextView;
         if (!var1) {
            var3 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var4, "alpha", new float[]{var3}));
      } else {
         TextView var6 = this.textView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var6.setAlpha(var5);
         SeekBarView var7 = this.seekBarView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var7.setAlpha(var5);
         var6 = this.sizeTextView;
         if (!var1) {
            var3 = 0.5F;
         }

         var6.setAlpha(var3);
      }

   }

   public void setSize(long var1) {
      this.currentSize = var1;
      this.sizeTextView.setText(LocaleController.formatString("AutodownloadSizeLimitUpTo", 2131558801, AndroidUtilities.formatFileSize(var1)));
      var1 -= 512000L;
      float var3;
      if (var1 < 536576L) {
         var3 = Math.max(0.0F, (float)var1 / 536576.0F) * 0.25F;
      } else {
         var1 -= 536576L;
         if (var1 < 9437184L) {
            var3 = Math.max(0.0F, (float)var1 / 9437184.0F) * 0.25F + 0.25F;
         } else {
            var3 = 0.5F;
            var1 -= 9437184L;
            float var4;
            if (var1 < 94371840L) {
               var4 = Math.max(0.0F, (float)var1 / 9.437184E7F);
            } else {
               var3 = 0.75F;
               var4 = Math.max(0.0F, (float)(var1 - 94371840L) / 1.50575514E9F);
            }

            var3 += var4 * 0.25F;
         }
      }

      this.seekBarView.setProgress(var3);
   }

   public void setText(String var1) {
      this.textView.setText(var1);
   }
}
