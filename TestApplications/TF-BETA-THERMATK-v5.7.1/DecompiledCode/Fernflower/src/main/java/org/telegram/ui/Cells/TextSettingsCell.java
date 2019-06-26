package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class TextSettingsCell extends FrameLayout {
   private boolean canDisable;
   private boolean needDivider;
   private TextView textView;
   private ImageView valueImageView;
   private TextView valueTextView;

   public TextSettingsCell(Context var1) {
      this(var1, 21);
   }

   public TextSettingsCell(Context var1, int var2) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      TextView var3 = this.textView;
      boolean var4 = LocaleController.isRTL;
      byte var5 = 5;
      byte var6;
      if (var4) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var3.setGravity(var6 | 16);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      var3 = this.textView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      float var7 = (float)var2;
      this.addView(var3, LayoutHelper.createFrame(-1, -1.0F, var6 | 48, var7, 0.0F, var7, 0.0F));
      this.valueTextView = new TextView(var1);
      this.valueTextView.setTextSize(1, 16.0F);
      this.valueTextView.setLines(1);
      this.valueTextView.setMaxLines(1);
      this.valueTextView.setSingleLine(true);
      this.valueTextView.setEllipsize(TruncateAt.END);
      var3 = this.valueTextView;
      byte var9;
      if (LocaleController.isRTL) {
         var9 = 3;
      } else {
         var9 = 5;
      }

      var3.setGravity(var9 | 16);
      this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteValueText"));
      var3 = this.valueTextView;
      if (LocaleController.isRTL) {
         var9 = 3;
      } else {
         var9 = 5;
      }

      this.addView(var3, LayoutHelper.createFrame(-2, -1.0F, var9 | 48, var7, 0.0F, var7, 0.0F));
      this.valueImageView = new ImageView(var1);
      this.valueImageView.setScaleType(ScaleType.CENTER);
      this.valueImageView.setVisibility(4);
      this.valueImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
      ImageView var8 = this.valueImageView;
      var9 = var5;
      if (LocaleController.isRTL) {
         var9 = 3;
      }

      this.addView(var8, LayoutHelper.createFrame(-2, -2.0F, var9 | 16, var7, 0.0F, var7, 0.0F));
   }

   public TextView getTextView() {
      return this.textView;
   }

   public TextView getValueTextView() {
      return this.valueTextView;
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
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

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setEnabled(this.isEnabled());
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(50.0F) + this.needDivider);
      var2 = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight() - AndroidUtilities.dp(34.0F);
      int var3 = var2 / 2;
      if (this.valueImageView.getVisibility() == 0) {
         this.valueImageView.measure(MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
      }

      var1 = var2;
      if (this.valueTextView.getVisibility() == 0) {
         this.valueTextView.measure(MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
         var1 = var2 - this.valueTextView.getMeasuredWidth() - AndroidUtilities.dp(8.0F);
      }

      this.textView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
   }

   public void setCanDisable(boolean var1) {
      this.canDisable = var1;
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      TextView var2 = this.textView;
      float var3 = 0.5F;
      float var4;
      if (!var1 && this.canDisable) {
         var4 = 0.5F;
      } else {
         var4 = 1.0F;
      }

      var2.setAlpha(var4);
      if (this.valueTextView.getVisibility() == 0) {
         var2 = this.valueTextView;
         if (!var1 && this.canDisable) {
            var4 = 0.5F;
         } else {
            var4 = 1.0F;
         }

         var2.setAlpha(var4);
      }

      if (this.valueImageView.getVisibility() == 0) {
         ImageView var5;
         label25: {
            var5 = this.valueImageView;
            if (!var1) {
               var4 = var3;
               if (this.canDisable) {
                  break label25;
               }
            }

            var4 = 1.0F;
         }

         var5.setAlpha(var4);
      }

   }

   public void setEnabled(boolean var1, ArrayList var2) {
      this.setEnabled(var1);
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
         if (this.valueTextView.getVisibility() == 0) {
            var4 = this.valueTextView;
            if (var1) {
               var5 = 1.0F;
            } else {
               var5 = 0.5F;
            }

            var2.add(ObjectAnimator.ofFloat(var4, "alpha", new float[]{var5}));
         }

         if (this.valueImageView.getVisibility() == 0) {
            ImageView var8 = this.valueImageView;
            if (!var1) {
               var3 = 0.5F;
            }

            var2.add(ObjectAnimator.ofFloat(var8, "alpha", new float[]{var3}));
         }
      } else {
         TextView var6 = this.textView;
         if (var1) {
            var5 = 1.0F;
         } else {
            var5 = 0.5F;
         }

         var6.setAlpha(var5);
         if (this.valueTextView.getVisibility() == 0) {
            var6 = this.valueTextView;
            if (var1) {
               var5 = 1.0F;
            } else {
               var5 = 0.5F;
            }

            var6.setAlpha(var5);
         }

         if (this.valueImageView.getVisibility() == 0) {
            ImageView var7 = this.valueImageView;
            if (!var1) {
               var3 = 0.5F;
            }

            var7.setAlpha(var3);
         }
      }

   }

   public void setText(String var1, boolean var2) {
      this.textView.setText(var1);
      this.valueTextView.setVisibility(4);
      this.valueImageView.setVisibility(4);
      this.needDivider = var2;
      this.setWillNotDraw(var2 ^ true);
   }

   public void setTextAndIcon(String var1, int var2, boolean var3) {
      this.textView.setText(var1);
      this.valueTextView.setVisibility(4);
      if (var2 != 0) {
         this.valueImageView.setVisibility(0);
         this.valueImageView.setImageResource(var2);
      } else {
         this.valueImageView.setVisibility(4);
      }

      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
   }

   public void setTextAndValue(String var1, String var2, boolean var3) {
      this.textView.setText(var1);
      this.valueImageView.setVisibility(4);
      if (var2 != null) {
         this.valueTextView.setText(var2);
         this.valueTextView.setVisibility(0);
      } else {
         this.valueTextView.setVisibility(4);
      }

      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
      this.requestLayout();
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }

   public void setTextValueColor(int var1) {
      this.valueTextView.setTextColor(var1);
   }
}
