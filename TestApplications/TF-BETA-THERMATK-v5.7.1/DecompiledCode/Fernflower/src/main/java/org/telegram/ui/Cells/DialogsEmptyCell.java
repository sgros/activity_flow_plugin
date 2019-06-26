package org.telegram.ui.Cells;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class DialogsEmptyCell extends LinearLayout {
   private int currentAccount;
   private int currentType;
   private TextView emptyTextView1;
   private TextView emptyTextView2;

   public DialogsEmptyCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.setGravity(17);
      this.setOrientation(1);
      this.setOnTouchListener(_$$Lambda$DialogsEmptyCell$7lLGhZthID2bSlrXEXwZZGk1ZsM.INSTANCE);
      this.emptyTextView1 = new TextView(var1);
      this.emptyTextView1.setTextColor(Theme.getColor("chats_nameMessage_threeLines"));
      this.emptyTextView1.setText(LocaleController.getString("NoChats", 2131559918));
      this.emptyTextView1.setTextSize(1, 20.0F);
      this.emptyTextView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.emptyTextView1.setGravity(17);
      this.addView(this.emptyTextView1, LayoutHelper.createFrame(-1, -2.0F, 51, 52.0F, 4.0F, 52.0F, 0.0F));
      this.emptyTextView2 = new TextView(var1);
      String var2 = LocaleController.getString("NoChatsHelp", 2131559920);
      String var3 = var2;
      if (AndroidUtilities.isTablet()) {
         var3 = var2;
         if (!AndroidUtilities.isSmallTablet()) {
            var3 = var2.replace('\n', ' ');
         }
      }

      this.emptyTextView2.setText(var3);
      this.emptyTextView2.setTextColor(Theme.getColor("chats_message"));
      this.emptyTextView2.setTextSize(1, 14.0F);
      this.emptyTextView2.setGravity(17);
      this.emptyTextView2.setLineSpacing((float)AndroidUtilities.dp(2.0F), 1.0F);
      this.addView(this.emptyTextView2, LayoutHelper.createFrame(-1, -2.0F, 51, 52.0F, 7.0F, 52.0F, 0.0F));
   }

   // $FF: synthetic method
   static boolean lambda$new$0(View var0, MotionEvent var1) {
      return true;
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var2);
      var2 = var3;
      if (var3 == 0) {
         int var4 = AndroidUtilities.displaySize.y;
         var3 = ActionBar.getCurrentActionBarHeight();
         if (VERSION.SDK_INT >= 21) {
            var2 = AndroidUtilities.statusBarHeight;
         } else {
            var2 = 0;
         }

         var2 = var4 - var3 - var2;
      }

      if (this.currentType == 0) {
         ArrayList var5 = MessagesController.getInstance(this.currentAccount).hintDialogs;
         var3 = var2;
         if (!var5.isEmpty()) {
            var3 = var2 - (AndroidUtilities.dp(72.0F) * var5.size() + var5.size() - 1 + AndroidUtilities.dp(50.0F));
         }

         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(var3, 1073741824));
      } else {
         super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(166.0F), 1073741824));
      }

   }

   public void setType(int var1) {
      this.currentType = var1;
      String var2;
      String var3;
      if (this.currentType == 0) {
         var2 = LocaleController.getString("NoChatsHelp", 2131559920);
         var3 = var2;
         if (AndroidUtilities.isTablet()) {
            var3 = var2;
            if (!AndroidUtilities.isSmallTablet()) {
               var3 = var2.replace('\n', ' ');
            }
         }
      } else {
         var2 = LocaleController.getString("NoChatsContactsHelp", 2131559919);
         var3 = var2;
         if (AndroidUtilities.isTablet()) {
            var3 = var2;
            if (!AndroidUtilities.isSmallTablet()) {
               var3 = var2.replace('\n', ' ');
            }
         }
      }

      this.emptyTextView2.setText(var3);
   }
}
