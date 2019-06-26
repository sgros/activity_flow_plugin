package org.telegram.ui.ActionBar;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Components.LayoutHelper;

public class ActionBarMenuSubItem extends FrameLayout {
   private ImageView imageView;
   private TextView textView;

   public ActionBarMenuSubItem(Context var1) {
      super(var1);
      this.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
      this.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      this.imageView = new ImageView(var1);
      this.imageView.setScaleType(ScaleType.CENTER);
      this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultSubmenuItemIcon"), Mode.MULTIPLY));
      ImageView var2 = this.imageView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      this.addView(var2, LayoutHelper.createFrame(-2, 40, var5 | 16));
      this.textView = new TextView(var1);
      this.textView.setLines(1);
      this.textView.setSingleLine(true);
      this.textView.setGravity(1);
      this.textView.setEllipsize(TruncateAt.END);
      this.textView.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
      this.textView.setTextSize(1, 16.0F);
      TextView var6 = this.textView;
      if (LocaleController.isRTL) {
         var5 = var4;
      } else {
         var5 = 3;
      }

      this.addView(var6, LayoutHelper.createFrame(-2, -2, var5 | 16));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
   }

   public void setColors(int var1, int var2) {
      this.textView.setTextColor(var1);
      this.imageView.setColorFilter(new PorterDuffColorFilter(var2, Mode.MULTIPLY));
   }

   public void setIcon(int var1) {
      this.imageView.setImageResource(var1);
   }

   public void setIconColor(int var1) {
      this.imageView.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
   }

   public void setText(String var1) {
      this.textView.setText(var1);
   }

   public void setTextAndIcon(CharSequence var1, int var2) {
      this.textView.setText(var1);
      if (var2 != 0) {
         this.imageView.setImageResource(var2);
         this.imageView.setVisibility(0);
         TextView var4 = this.textView;
         if (LocaleController.isRTL) {
            var2 = 0;
         } else {
            var2 = AndroidUtilities.dp(43.0F);
         }

         int var3;
         if (LocaleController.isRTL) {
            var3 = AndroidUtilities.dp(43.0F);
         } else {
            var3 = 0;
         }

         var4.setPadding(var2, 0, var3, 0);
      } else {
         this.imageView.setVisibility(4);
         this.textView.setPadding(0, 0, 0, 0);
      }

   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }
}
