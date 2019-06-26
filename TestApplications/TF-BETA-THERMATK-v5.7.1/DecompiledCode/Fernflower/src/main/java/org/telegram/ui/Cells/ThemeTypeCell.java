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

public class ThemeTypeCell extends FrameLayout {
   private ImageView checkImage;
   private boolean needDivider;
   private TextView textView;

   public ThemeTypeCell(Context var1) {
      super(var1);
      this.setWillNotDraw(false);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      TextView var2 = this.textView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var2.setGravity(var5 | 16);
      var2 = this.textView;
      if (LocaleController.isRTL) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      float var6;
      if (LocaleController.isRTL) {
         var6 = 71.0F;
      } else {
         var6 = 21.0F;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 21.0F;
      } else {
         var7 = 23.0F;
      }

      this.addView(var2, LayoutHelper.createFrame(-1, -1.0F, var5 | 48, var6, 0.0F, var7, 0.0F));
      this.checkImage = new ImageView(var1);
      this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), Mode.MULTIPLY));
      this.checkImage.setImageResource(2131165858);
      ImageView var8 = this.checkImage;
      var5 = var4;
      if (LocaleController.isRTL) {
         var5 = 3;
      }

      this.addView(var8, LayoutHelper.createFrame(19, 14.0F, var5 | 16, 23.0F, 0.0F, 23.0F, 0.0F));
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
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0F) + this.needDivider, 1073741824));
   }

   public void setTypeChecked(boolean var1) {
      ImageView var2 = this.checkImage;
      byte var3;
      if (var1) {
         var3 = 0;
      } else {
         var3 = 4;
      }

      var2.setVisibility(var3);
   }

   public void setValue(String var1, boolean var2, boolean var3) {
      this.textView.setText(var1);
      ImageView var5 = this.checkImage;
      byte var4;
      if (var2) {
         var4 = 0;
      } else {
         var4 = 4;
      }

      var5.setVisibility(var4);
      this.needDivider = var3;
   }
}
