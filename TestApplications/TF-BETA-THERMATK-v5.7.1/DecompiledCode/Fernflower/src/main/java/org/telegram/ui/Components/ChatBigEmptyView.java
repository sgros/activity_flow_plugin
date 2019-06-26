package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;

public class ChatBigEmptyView extends LinearLayout {
   public static final int EMPTY_VIEW_TYPE_GROUP = 1;
   public static final int EMPTY_VIEW_TYPE_SAVED = 2;
   public static final int EMPTY_VIEW_TYPE_SECRET = 0;
   private ArrayList imageViews = new ArrayList();
   private TextView statusTextView;
   private ArrayList textViews = new ArrayList();

   public ChatBigEmptyView(Context var1, int var2) {
      super(var1);
      this.setBackgroundResource(2131165871);
      this.getBackground().setColorFilter(Theme.colorFilter);
      this.setPadding(AndroidUtilities.dp(16.0F), AndroidUtilities.dp(12.0F), AndroidUtilities.dp(16.0F), AndroidUtilities.dp(12.0F));
      this.setOrientation(1);
      if (var2 == 0) {
         this.statusTextView = new TextView(var1);
         this.statusTextView.setTextSize(1, 15.0F);
         this.statusTextView.setTextColor(Theme.getColor("chat_serviceText"));
         this.statusTextView.setGravity(1);
         this.statusTextView.setMaxWidth(AndroidUtilities.dp(210.0F));
         this.textViews.add(this.statusTextView);
         this.addView(this.statusTextView, LayoutHelper.createLinear(-2, -2, 49));
      } else if (var2 == 1) {
         this.statusTextView = new TextView(var1);
         this.statusTextView.setTextSize(1, 15.0F);
         this.statusTextView.setTextColor(Theme.getColor("chat_serviceText"));
         this.statusTextView.setGravity(1);
         this.statusTextView.setMaxWidth(AndroidUtilities.dp(210.0F));
         this.textViews.add(this.statusTextView);
         this.addView(this.statusTextView, LayoutHelper.createLinear(-2, -2, 49));
      } else {
         ImageView var3 = new ImageView(var1);
         var3.setImageResource(2131165358);
         this.addView(var3, LayoutHelper.createLinear(-2, -2, 49, 0, 2, 0, 0));
      }

      TextView var8 = new TextView(var1);
      if (var2 == 0) {
         var8.setText(LocaleController.getString("EncryptedDescriptionTitle", 2131559357));
         var8.setTextSize(1, 15.0F);
      } else if (var2 == 1) {
         var8.setText(LocaleController.getString("GroupEmptyTitle2", 2131559608));
         var8.setTextSize(1, 15.0F);
      } else {
         var8.setText(LocaleController.getString("ChatYourSelfTitle", 2131559051));
         var8.setTextSize(1, 16.0F);
         var8.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var8.setGravity(1);
      }

      var8.setTextColor(Theme.getColor("chat_serviceText"));
      this.textViews.add(var8);
      var8.setMaxWidth(AndroidUtilities.dp(260.0F));
      byte var4;
      if (var2 != 2) {
         if (LocaleController.isRTL) {
            var4 = 5;
         } else {
            var4 = 3;
         }
      } else {
         var4 = 1;
      }

      byte var5;
      if (var2 != 2) {
         var5 = 0;
      } else {
         var5 = 8;
      }

      this.addView(var8, LayoutHelper.createLinear(-2, -2, var4 | 48, 0, 8, 0, var5));

      for(int var9 = 0; var9 < 4; ++var9) {
         LinearLayout var6 = new LinearLayout(var1);
         var6.setOrientation(0);
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         this.addView(var6, LayoutHelper.createLinear(-2, -2, var5, 0, 8, 0, 0));
         ImageView var7 = new ImageView(var1);
         var7.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_serviceText"), Mode.MULTIPLY));
         if (var2 == 0) {
            var7.setImageResource(2131165448);
         } else if (var2 == 2) {
            var7.setImageResource(2131165525);
         } else {
            var7.setImageResource(2131165406);
         }

         this.imageViews.add(var7);
         var8 = new TextView(var1);
         var8.setTextSize(1, 15.0F);
         var8.setTextColor(Theme.getColor("chat_serviceText"));
         this.textViews.add(var8);
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var8.setGravity(var5 | 16);
         var8.setMaxWidth(AndroidUtilities.dp(260.0F));
         if (var9 != 0) {
            if (var9 != 1) {
               if (var9 != 2) {
                  if (var9 == 3) {
                     if (var2 == 0) {
                        var8.setText(LocaleController.getString("EncryptedDescription4", 2131559356));
                     } else if (var2 == 2) {
                        var8.setText(LocaleController.getString("ChatYourSelfDescription4", 2131559049));
                     } else {
                        var8.setText(LocaleController.getString("GroupDescription4", 2131559606));
                     }
                  }
               } else if (var2 == 0) {
                  var8.setText(LocaleController.getString("EncryptedDescription3", 2131559355));
               } else if (var2 == 2) {
                  var8.setText(LocaleController.getString("ChatYourSelfDescription3", 2131559048));
               } else {
                  var8.setText(LocaleController.getString("GroupDescription3", 2131559605));
               }
            } else if (var2 == 0) {
               var8.setText(LocaleController.getString("EncryptedDescription2", 2131559354));
            } else if (var2 == 2) {
               var8.setText(LocaleController.getString("ChatYourSelfDescription2", 2131559047));
            } else {
               var8.setText(LocaleController.getString("GroupDescription2", 2131559604));
            }
         } else if (var2 == 0) {
            var8.setText(LocaleController.getString("EncryptedDescription1", 2131559353));
         } else if (var2 == 2) {
            var8.setText(LocaleController.getString("ChatYourSelfDescription1", 2131559046));
         } else {
            var8.setText(LocaleController.getString("GroupDescription1", 2131559603));
         }

         if (LocaleController.isRTL) {
            var6.addView(var8, LayoutHelper.createLinear(-2, -2));
            if (var2 == 0) {
               var6.addView(var7, LayoutHelper.createLinear(-2, -2, 8.0F, 3.0F, 0.0F, 0.0F));
            } else if (var2 == 2) {
               var6.addView(var7, LayoutHelper.createLinear(-2, -2, 8.0F, 7.0F, 0.0F, 0.0F));
            } else {
               var6.addView(var7, LayoutHelper.createLinear(-2, -2, 8.0F, 3.0F, 0.0F, 0.0F));
            }
         } else {
            if (var2 == 0) {
               var6.addView(var7, LayoutHelper.createLinear(-2, -2, 0.0F, 4.0F, 8.0F, 0.0F));
            } else if (var2 == 2) {
               var6.addView(var7, LayoutHelper.createLinear(-2, -2, 0.0F, 8.0F, 8.0F, 0.0F));
            } else {
               var6.addView(var7, LayoutHelper.createLinear(-2, -2, 0.0F, 4.0F, 8.0F, 0.0F));
            }

            var6.addView(var8, LayoutHelper.createLinear(-2, -2));
         }
      }

   }

   public void setStatusText(CharSequence var1) {
      this.statusTextView.setText(var1);
   }

   public void setTextColor(int var1) {
      byte var2 = 0;
      int var3 = 0;

      while(true) {
         int var4 = var2;
         if (var3 >= this.textViews.size()) {
            while(var4 < this.imageViews.size()) {
               ((ImageView)this.imageViews.get(var4)).setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_serviceText"), Mode.MULTIPLY));
               ++var4;
            }

            return;
         }

         ((TextView)this.textViews.get(var3)).setTextColor(var1);
         ++var3;
      }
   }
}
