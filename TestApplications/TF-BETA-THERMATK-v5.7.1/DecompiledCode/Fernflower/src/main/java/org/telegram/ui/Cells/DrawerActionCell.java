package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class DrawerActionCell extends FrameLayout {
   private TextView textView;

   public DrawerActionCell(Context var1) {
      super(var1);
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
      this.textView.setTextSize(1, 15.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setGravity(19);
      this.textView.setCompoundDrawablePadding(AndroidUtilities.dp(29.0F));
      this.addView(this.textView, LayoutHelper.createFrame(-1, -1.0F, 51, 19.0F, 0.0F, 16.0F, 0.0F));
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
   }

   public void setTextAndIcon(String var1, int var2) {
      Throwable var10000;
      label30: {
         boolean var10001;
         Drawable var7;
         try {
            this.textView.setText(var1);
            var7 = this.getResources().getDrawable(var2).mutate();
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label30;
         }

         if (var7 != null) {
            try {
               PorterDuffColorFilter var3 = new PorterDuffColorFilter(Theme.getColor("chats_menuItemIcon"), Mode.MULTIPLY);
               var7.setColorFilter(var3);
            } catch (Throwable var5) {
               var10000 = var5;
               var10001 = false;
               break label30;
            }
         }

         try {
            this.textView.setCompoundDrawablesWithIntrinsicBounds(var7, (Drawable)null, (Drawable)null, (Drawable)null);
            return;
         } catch (Throwable var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      Throwable var8 = var10000;
      FileLog.e(var8);
   }
}
