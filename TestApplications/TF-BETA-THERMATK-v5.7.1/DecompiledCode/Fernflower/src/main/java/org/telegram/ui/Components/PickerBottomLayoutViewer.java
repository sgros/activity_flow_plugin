package org.telegram.ui.Components;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;

public class PickerBottomLayoutViewer extends FrameLayout {
   public TextView cancelButton;
   public TextView doneButton;
   public TextView doneButtonBadgeTextView;
   private boolean isDarkTheme;

   public PickerBottomLayoutViewer(Context var1) {
      this(var1, true);
   }

   public PickerBottomLayoutViewer(Context var1, boolean var2) {
      super(var1);
      this.isDarkTheme = var2;
      int var3;
      if (this.isDarkTheme) {
         var3 = -15066598;
      } else {
         var3 = -1;
      }

      this.setBackgroundColor(var3);
      this.cancelButton = new TextView(var1);
      this.cancelButton.setTextSize(1, 14.0F);
      TextView var4 = this.cancelButton;
      var2 = this.isDarkTheme;
      int var5 = -15095832;
      if (var2) {
         var3 = -1;
      } else {
         var3 = -15095832;
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
      this.cancelButton.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.cancelButton.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
      this.cancelButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.addView(this.cancelButton, LayoutHelper.createFrame(-2, -1, 51));
      this.doneButton = new TextView(var1);
      this.doneButton.setTextSize(1, 14.0F);
      var4 = this.doneButton;
      var3 = var5;
      if (this.isDarkTheme) {
         var3 = -1;
      }

      var4.setTextColor(var3);
      this.doneButton.setGravity(17);
      var4 = this.doneButton;
      if (this.isDarkTheme) {
         var3 = var6;
      } else {
         var3 = 788529152;
      }

      var4.setBackgroundDrawable(Theme.createSelectorDrawable(var3, 0));
      this.doneButton.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.doneButton.setText(LocaleController.getString("Send", 2131560685).toUpperCase());
      this.doneButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.addView(this.doneButton, LayoutHelper.createFrame(-2, -1, 53));
      this.doneButtonBadgeTextView = new TextView(var1);
      this.doneButtonBadgeTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.doneButtonBadgeTextView.setTextSize(1, 13.0F);
      this.doneButtonBadgeTextView.setTextColor(-1);
      this.doneButtonBadgeTextView.setGravity(17);
      TextView var7 = this.doneButtonBadgeTextView;
      if (this.isDarkTheme) {
         var3 = 2131165754;
      } else {
         var3 = 2131165306;
      }

      var7.setBackgroundResource(var3);
      this.doneButtonBadgeTextView.setMinWidth(AndroidUtilities.dp(23.0F));
      this.doneButtonBadgeTextView.setPadding(AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F), AndroidUtilities.dp(1.0F));
      this.addView(this.doneButtonBadgeTextView, LayoutHelper.createFrame(-2, 23.0F, 53, 0.0F, 0.0F, 7.0F, 0.0F));
   }

   public void updateSelectedCount(int var1, boolean var2) {
      int var3 = -1;
      TextView var4;
      if (var1 == 0) {
         this.doneButtonBadgeTextView.setVisibility(8);
         if (var2) {
            this.doneButton.setTextColor(-6710887);
            this.doneButton.setEnabled(false);
         } else {
            var4 = this.doneButton;
            if (!this.isDarkTheme) {
               var3 = -15095832;
            }

            var4.setTextColor(var3);
         }
      } else {
         this.doneButtonBadgeTextView.setVisibility(0);
         this.doneButtonBadgeTextView.setText(String.format("%d", var1));
         var4 = this.doneButton;
         if (!this.isDarkTheme) {
            var3 = -15095832;
         }

         var4.setTextColor(var3);
         if (var2) {
            this.doneButton.setEnabled(true);
         }
      }

   }
}
