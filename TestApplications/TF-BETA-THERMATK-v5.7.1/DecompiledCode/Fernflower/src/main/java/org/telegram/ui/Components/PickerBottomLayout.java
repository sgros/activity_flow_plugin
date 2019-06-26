package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;

public class PickerBottomLayout extends FrameLayout {
   public TextView cancelButton;
   public LinearLayout doneButton;
   public TextView doneButtonBadgeTextView;
   public TextView doneButtonTextView;
   private boolean isDarkTheme;

   public PickerBottomLayout(Context var1) {
      this(var1, true);
   }

   public PickerBottomLayout(Context var1, boolean var2) {
      super(var1);
      this.isDarkTheme = var2;
      int var3;
      if (this.isDarkTheme) {
         var3 = -15066598;
      } else {
         var3 = Theme.getColor("windowBackgroundWhite");
      }

      this.setBackgroundColor(var3);
      this.cancelButton = new TextView(var1);
      this.cancelButton.setTextSize(1, 14.0F);
      TextView var4 = this.cancelButton;
      var2 = this.isDarkTheme;
      byte var5 = -1;
      if (var2) {
         var3 = -1;
      } else {
         var3 = Theme.getColor("picker_enabledButton");
      }

      var4.setTextColor(var3);
      this.cancelButton.setGravity(17);
      var4 = this.cancelButton;
      var2 = this.isDarkTheme;
      int var6 = -12763843;
      if (var2) {
         var3 = -12763843;
      } else {
         var3 = 788529152;
      }

      var4.setBackgroundDrawable(Theme.createSelectorDrawable(var3, 0));
      this.cancelButton.setPadding(AndroidUtilities.dp(29.0F), 0, AndroidUtilities.dp(29.0F), 0);
      this.cancelButton.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
      this.cancelButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.addView(this.cancelButton, LayoutHelper.createFrame(-2, -1, 51));
      this.doneButton = new LinearLayout(var1);
      this.doneButton.setOrientation(0);
      LinearLayout var8 = this.doneButton;
      if (this.isDarkTheme) {
         var3 = var6;
      } else {
         var3 = 788529152;
      }

      var8.setBackgroundDrawable(Theme.createSelectorDrawable(var3, 0));
      this.doneButton.setPadding(AndroidUtilities.dp(29.0F), 0, AndroidUtilities.dp(29.0F), 0);
      this.addView(this.doneButton, LayoutHelper.createFrame(-2, -1, 53));
      this.doneButtonBadgeTextView = new TextView(var1);
      this.doneButtonBadgeTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.doneButtonBadgeTextView.setTextSize(1, 13.0F);
      var4 = this.doneButtonBadgeTextView;
      if (this.isDarkTheme) {
         var3 = -1;
      } else {
         var3 = Theme.getColor("picker_badgeText");
      }

      var4.setTextColor(var3);
      this.doneButtonBadgeTextView.setGravity(17);
      Drawable var9;
      if (this.isDarkTheme) {
         var9 = Theme.createRoundRectDrawable(AndroidUtilities.dp(11.0F), -10043398);
      } else {
         var9 = Theme.createRoundRectDrawable(AndroidUtilities.dp(11.0F), Theme.getColor("picker_badge"));
      }

      this.doneButtonBadgeTextView.setBackgroundDrawable(var9);
      this.doneButtonBadgeTextView.setMinWidth(AndroidUtilities.dp(23.0F));
      this.doneButtonBadgeTextView.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F), AndroidUtilities.dp(1.0F));
      this.doneButton.addView(this.doneButtonBadgeTextView, LayoutHelper.createLinear(-2, 23, 16, 0, 0, 10, 0));
      this.doneButtonTextView = new TextView(var1);
      this.doneButtonTextView.setTextSize(1, 14.0F);
      TextView var7 = this.doneButtonTextView;
      if (this.isDarkTheme) {
         var3 = var5;
      } else {
         var3 = Theme.getColor("picker_enabledButton");
      }

      var7.setTextColor(var3);
      this.doneButtonTextView.setGravity(17);
      this.doneButtonTextView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0F));
      this.doneButtonTextView.setText(LocaleController.getString("Send", 2131560685).toUpperCase());
      this.doneButtonTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.doneButton.addView(this.doneButtonTextView, LayoutHelper.createLinear(-2, -2, 16));
   }

   public void updateSelectedCount(int var1, boolean var2) {
      int var3 = -1;
      TextView var4 = null;
      TextView var5 = null;
      String var6 = null;
      TextView var7;
      if (var1 == 0) {
         this.doneButtonBadgeTextView.setVisibility(8);
         if (var2) {
            var4 = this.doneButtonTextView;
            if (!this.isDarkTheme) {
               var6 = "picker_disabledButton";
            }

            var4.setTag(var6);
            var7 = this.doneButtonTextView;
            if (this.isDarkTheme) {
               var1 = -6710887;
            } else {
               var1 = Theme.getColor("picker_disabledButton");
            }

            var7.setTextColor(var1);
            this.doneButton.setEnabled(false);
         } else {
            var5 = this.doneButtonTextView;
            if (this.isDarkTheme) {
               var6 = var4;
            } else {
               var6 = "picker_enabledButton";
            }

            var5.setTag(var6);
            var7 = this.doneButtonTextView;
            if (!this.isDarkTheme) {
               var3 = Theme.getColor("picker_enabledButton");
            }

            var7.setTextColor(var3);
         }
      } else {
         this.doneButtonBadgeTextView.setVisibility(0);
         this.doneButtonBadgeTextView.setText(String.format("%d", var1));
         var4 = this.doneButtonTextView;
         if (this.isDarkTheme) {
            var6 = var5;
         } else {
            var6 = "picker_enabledButton";
         }

         var4.setTag(var6);
         var7 = this.doneButtonTextView;
         if (!this.isDarkTheme) {
            var3 = Theme.getColor("picker_enabledButton");
         }

         var7.setTextColor(var3);
         if (var2) {
            this.doneButton.setEnabled(true);
         }
      }

   }
}
