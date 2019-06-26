package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class ThemeCell extends FrameLayout {
   private static byte[] bytes = new byte[1024];
   private ImageView checkImage;
   private Theme.ThemeInfo currentThemeInfo;
   private boolean isNightTheme;
   private boolean needDivider;
   private ImageView optionsButton;
   private Paint paint;
   private TextView textView;

   public ThemeCell(Context var1, boolean var2) {
      super(var1);
      this.setWillNotDraw(false);
      this.isNightTheme = var2;
      this.paint = new Paint(1);
      this.textView = new TextView(var1);
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(1.0F));
      this.textView.setEllipsize(TruncateAt.END);
      TextView var3 = this.textView;
      var2 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var2) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var3.setGravity(var5 | 16);
      var3 = this.textView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 105.0F;
      } else {
         var6 = 60.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 60.0F;
      } else {
         var7 = 105.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, var6, 0.0F, var7, 0.0F));
      this.checkImage = new ImageView(var1);
      this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), Mode.MULTIPLY));
      this.checkImage.setImageResource(2131165858);
      ImageView var8;
      if (!this.isNightTheme) {
         ImageView var9 = this.checkImage;
         if (LocaleController.isRTL) {
            var5 = 3;
         } else {
            var5 = 5;
         }

         this.addView(var9, LayoutHelper.createFrame(19, 14.0F, var5 | 16, 59.0F, 0.0F, 59.0F, 0.0F));
         this.optionsButton = new ImageView(var1);
         this.optionsButton.setFocusable(false);
         this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
         this.optionsButton.setImageResource(2131165416);
         this.optionsButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("stickers_menu"), Mode.MULTIPLY));
         this.optionsButton.setScaleType(ScaleType.CENTER);
         this.optionsButton.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
         var8 = this.optionsButton;
         if (LocaleController.isRTL) {
            var4 = 3;
         }

         this.addView(var8, LayoutHelper.createFrame(48, 48, var4 | 48));
      } else {
         var8 = this.checkImage;
         if (LocaleController.isRTL) {
            var4 = 3;
         }

         this.addView(var8, LayoutHelper.createFrame(19, 14.0F, var4 | 16, 21.0F, 0.0F, 21.0F, 0.0F));
      }

   }

   public Theme.ThemeInfo getCurrentThemeInfo() {
      return this.currentThemeInfo;
   }

   public TextView getTextView() {
      return this.textView;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), Mode.MULTIPLY));
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
   }

   protected void onDraw(Canvas var1) {
      int var3;
      int var4;
      if (this.needDivider) {
         int var2 = Theme.dividerPaint.getColor();
         var3 = Color.alpha(var2);
         var4 = 0;
         FileLog.d(String.format("set color %d %d %d %d", var3, Color.red(var2), Color.green(var2), Color.blue(var2)));
         float var5;
         if (LocaleController.isRTL) {
            var5 = 0.0F;
         } else {
            var5 = (float)AndroidUtilities.dp(20.0F);
         }

         float var6 = (float)(this.getMeasuredHeight() - 1);
         var3 = this.getMeasuredWidth();
         if (LocaleController.isRTL) {
            var4 = AndroidUtilities.dp(20.0F);
         }

         var1.drawLine(var5, var6, (float)(var3 - var4), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

      var3 = AndroidUtilities.dp(31.0F);
      var4 = var3;
      if (LocaleController.isRTL) {
         var4 = this.getWidth() - var3;
      }

      var1.drawCircle((float)var4, (float)AndroidUtilities.dp(24.0F), (float)AndroidUtilities.dp(11.0F), this.paint);
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      boolean var2;
      if (this.checkImage.getVisibility() == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setSelected(var2);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0F) + this.needDivider, 1073741824));
   }

   public void setOnOptionsClick(OnClickListener var1) {
      this.optionsButton.setOnClickListener(var1);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }

   public void setTheme(Theme.ThemeInfo param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public void updateCurrentThemeCheck() {
      Theme.ThemeInfo var1;
      if (this.isNightTheme) {
         var1 = Theme.getCurrentNightTheme();
      } else {
         var1 = Theme.getCurrentTheme();
      }

      byte var2;
      if (this.currentThemeInfo == var1) {
         var2 = 0;
      } else {
         var2 = 4;
      }

      if (this.checkImage.getVisibility() != var2) {
         this.checkImage.setVisibility(var2);
      }

   }
}
