package org.telegram.ui.Components;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class BotKeyboardView extends LinearLayout {
   private TLRPC.TL_replyKeyboardMarkup botButtons;
   private int buttonHeight;
   private ArrayList buttonViews = new ArrayList();
   private LinearLayout container;
   private BotKeyboardView.BotKeyboardViewDelegate delegate;
   private boolean isFullSize;
   private int panelHeight;
   private ScrollView scrollView;

   public BotKeyboardView(Context var1) {
      super(var1);
      this.setOrientation(1);
      this.scrollView = new ScrollView(var1);
      this.addView(this.scrollView);
      this.container = new LinearLayout(var1);
      this.container.setOrientation(1);
      this.scrollView.addView(this.container);
      AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("chat_emojiPanelBackground"));
      this.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
   }

   public int getKeyboardHeight() {
      int var1;
      if (this.isFullSize) {
         var1 = this.panelHeight;
      } else {
         var1 = this.botButtons.rows.size() * AndroidUtilities.dp((float)this.buttonHeight) + AndroidUtilities.dp(30.0F) + (this.botButtons.rows.size() - 1) * AndroidUtilities.dp(10.0F);
      }

      return var1;
   }

   public void invalidateViews() {
      for(int var1 = 0; var1 < this.buttonViews.size(); ++var1) {
         ((TextView)this.buttonViews.get(var1)).invalidate();
      }

   }

   public boolean isFullSize() {
      return this.isFullSize;
   }

   public void setButtons(TLRPC.TL_replyKeyboardMarkup var1) {
      this.botButtons = var1;
      this.container.removeAllViews();
      this.buttonViews.clear();
      this.scrollView.scrollTo(0, 0);
      if (var1 != null && this.botButtons.rows.size() != 0) {
         boolean var2;
         if (!var1.resize) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.isFullSize = var2;
         int var3;
         if (!this.isFullSize) {
            var3 = 42;
         } else {
            var3 = (int)Math.max(42.0F, (float)((this.panelHeight - AndroidUtilities.dp(30.0F) - (this.botButtons.rows.size() - 1) * AndroidUtilities.dp(10.0F)) / this.botButtons.rows.size()) / AndroidUtilities.density);
         }

         this.buttonHeight = var3;

         for(var3 = 0; var3 < var1.rows.size(); ++var3) {
            TLRPC.TL_keyboardButtonRow var4 = (TLRPC.TL_keyboardButtonRow)var1.rows.get(var3);
            LinearLayout var5 = new LinearLayout(this.getContext());
            var5.setOrientation(0);
            LinearLayout var6 = this.container;
            int var7 = this.buttonHeight;
            float var8;
            if (var3 == 0) {
               var8 = 15.0F;
            } else {
               var8 = 10.0F;
            }

            float var9;
            if (var3 == var1.rows.size() - 1) {
               var9 = 15.0F;
            } else {
               var9 = 0.0F;
            }

            var6.addView(var5, LayoutHelper.createLinear(-1, var7, 15.0F, var8, 15.0F, var9));
            var8 = 1.0F / (float)var4.buttons.size();

            for(var7 = 0; var7 < var4.buttons.size(); ++var7) {
               TLRPC.KeyboardButton var12 = (TLRPC.KeyboardButton)var4.buttons.get(var7);
               TextView var10 = new TextView(this.getContext());
               var10.setTag(var12);
               var10.setTextColor(Theme.getColor("chat_botKeyboardButtonText"));
               var10.setTextSize(1, 16.0F);
               var10.setGravity(17);
               var10.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0F), Theme.getColor("chat_botKeyboardButtonBackground"), Theme.getColor("chat_botKeyboardButtonBackgroundPressed")));
               var10.setPadding(AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F), 0);
               var10.setText(Emoji.replaceEmoji(var12.text, var10.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0F), false));
               byte var11;
               if (var7 != var4.buttons.size() - 1) {
                  var11 = 10;
               } else {
                  var11 = 0;
               }

               var5.addView(var10, LayoutHelper.createLinear(0, -1, var8, 0, 0, var11, 0));
               var10.setOnClickListener(new OnClickListener() {
                  public void onClick(View var1) {
                     BotKeyboardView.this.delegate.didPressedButton((TLRPC.KeyboardButton)var1.getTag());
                  }
               });
               this.buttonViews.add(var10);
            }
         }
      }

   }

   public void setDelegate(BotKeyboardView.BotKeyboardViewDelegate var1) {
      this.delegate = var1;
   }

   public void setPanelHeight(int var1) {
      this.panelHeight = var1;
      if (this.isFullSize) {
         TLRPC.TL_replyKeyboardMarkup var2 = this.botButtons;
         if (var2 != null && var2.rows.size() != 0) {
            if (!this.isFullSize) {
               var1 = 42;
            } else {
               var1 = (int)Math.max(42.0F, (float)((this.panelHeight - AndroidUtilities.dp(30.0F) - (this.botButtons.rows.size() - 1) * AndroidUtilities.dp(10.0F)) / this.botButtons.rows.size()) / AndroidUtilities.density);
            }

            this.buttonHeight = var1;
            int var3 = this.container.getChildCount();
            int var4 = AndroidUtilities.dp((float)this.buttonHeight);

            for(var1 = 0; var1 < var3; ++var1) {
               View var5 = this.container.getChildAt(var1);
               LayoutParams var6 = (LayoutParams)var5.getLayoutParams();
               if (var6.height != var4) {
                  var6.height = var4;
                  var5.setLayoutParams(var6);
               }
            }
         }
      }

   }

   public interface BotKeyboardViewDelegate {
      void didPressedButton(TLRPC.KeyboardButton var1);
   }
}
