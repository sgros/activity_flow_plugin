package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class LanguageCell extends FrameLayout {
   private ImageView checkImage;
   private LocaleController.LocaleInfo currentLocale;
   private boolean isDialog;
   private boolean needDivider;
   private TextView textView;
   private TextView textView2;

   public LanguageCell(Context var1, boolean var2) {
      super(var1);
      this.setWillNotDraw(false);
      this.isDialog = var2;
      this.textView = new TextView(var1);
      TextView var3 = this.textView;
      String var4;
      if (var2) {
         var4 = "dialogTextBlack";
      } else {
         var4 = "windowBackgroundWhiteBlackText";
      }

      var3.setTextColor(Theme.getColor(var4));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      TextView var13 = this.textView;
      boolean var5 = LocaleController.isRTL;
      byte var6 = 5;
      byte var7;
      if (var5) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var13.setGravity(var7 | 48);
      var13 = this.textView;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 71.0F;
      } else {
         var8 = 23.0F;
      }

      byte var9;
      if (this.isDialog) {
         var9 = 4;
      } else {
         var9 = 7;
      }

      float var10 = (float)var9;
      float var11;
      if (LocaleController.isRTL) {
         var11 = 23.0F;
      } else {
         var11 = 71.0F;
      }

      this.addView(var13, LayoutHelper.createFrame(-1, -1.0F, var7 | 48, var8, var10, var11, 0.0F));
      this.textView2 = new TextView(var1);
      var3 = this.textView2;
      if (var2) {
         var4 = "dialogTextGray3";
      } else {
         var4 = "windowBackgroundWhiteGrayText3";
      }

      var3.setTextColor(Theme.getColor(var4));
      this.textView2.setTextSize(1, 13.0F);
      this.textView2.setLines(1);
      this.textView2.setMaxLines(1);
      this.textView2.setSingleLine(true);
      this.textView2.setEllipsize(TruncateAt.END);
      var13 = this.textView2;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      var13.setGravity(var7 | 48);
      var13 = this.textView2;
      if (LocaleController.isRTL) {
         var7 = 5;
      } else {
         var7 = 3;
      }

      if (LocaleController.isRTL) {
         var8 = 71.0F;
      } else {
         var8 = 23.0F;
      }

      if (this.isDialog) {
         var9 = 25;
      } else {
         var9 = 29;
      }

      var10 = (float)var9;
      if (LocaleController.isRTL) {
         var11 = 23.0F;
      } else {
         var11 = 71.0F;
      }

      this.addView(var13, LayoutHelper.createFrame(-1, -1.0F, var7 | 48, var8, var10, var11, 0.0F));
      this.checkImage = new ImageView(var1);
      this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), Mode.MULTIPLY));
      this.checkImage.setImageResource(2131165858);
      ImageView var12 = this.checkImage;
      var7 = var6;
      if (LocaleController.isRTL) {
         var7 = 3;
      }

      this.addView(var12, LayoutHelper.createFrame(19, 14.0F, var7 | 16, 23.0F, 0.0F, 23.0F, 0.0F));
   }

   public LocaleController.LocaleInfo getCurrentLocale() {
      return this.currentLocale;
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

   protected void onMeasure(int var1, int var2) {
      var1 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824);
      float var3;
      if (this.isDialog) {
         var3 = 50.0F;
      } else {
         var3 = 54.0F;
      }

      super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(var3) + this.needDivider, 1073741824));
   }

   public void setLanguage(LocaleController.LocaleInfo var1, String var2, boolean var3) {
      TextView var4 = this.textView;
      if (var2 == null) {
         var2 = var1.name;
      }

      var4.setText(var2);
      this.textView2.setText(var1.nameEnglish);
      this.currentLocale = var1;
      this.needDivider = var3;
   }

   public void setLanguageSelected(boolean var1) {
      ImageView var2 = this.checkImage;
      byte var3;
      if (var1) {
         var3 = 0;
      } else {
         var3 = 4;
      }

      var2.setVisibility(var3);
   }

   public void setValue(String var1, String var2) {
      this.textView.setText(var1);
      this.textView2.setText(var2);
      this.checkImage.setVisibility(4);
      this.currentLocale = null;
      this.needDivider = false;
   }
}
