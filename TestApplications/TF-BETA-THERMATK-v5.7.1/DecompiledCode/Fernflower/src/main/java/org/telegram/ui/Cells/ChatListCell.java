package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadioButton;

public class ChatListCell extends LinearLayout {
   private ChatListCell.ListView[] listView = new ChatListCell.ListView[2];

   public ChatListCell(Context var1) {
      super(var1);
      this.setOrientation(0);
      this.setPadding(AndroidUtilities.dp(21.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(21.0F), 0);

      for(int var2 = 0; var2 < this.listView.length; ++var2) {
         boolean var3;
         if (var2 == 1) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.listView[var2] = new ChatListCell.ListView(var1, var3);
         ChatListCell.ListView var4 = this.listView[var2];
         byte var5;
         if (var2 == 1) {
            var5 = 10;
         } else {
            var5 = 0;
         }

         this.addView(var4, LayoutHelper.createLinear(-1, -1, 0.5F, var5, 0, 0, 0));
         this.listView[var2].setOnClickListener(new _$$Lambda$ChatListCell$iynS8JCANgqC1uqLhRG8bhggO74(this, var3));
      }

   }

   protected void didSelectChatType(boolean var1) {
   }

   public void invalidate() {
      super.invalidate();
      int var1 = 0;

      while(true) {
         ChatListCell.ListView[] var2 = this.listView;
         if (var1 >= var2.length) {
            return;
         }

         var2[var1].invalidate();
         ++var1;
      }
   }

   // $FF: synthetic method
   public void lambda$new$0$ChatListCell(boolean var1, View var2) {
      for(int var3 = 0; var3 < 2; ++var3) {
         RadioButton var4 = this.listView[var3].button;
         boolean var5;
         if (this.listView[var3] == var2) {
            var5 = true;
         } else {
            var5 = false;
         }

         var4.setChecked(var5, true);
      }

      this.didSelectChatType(var1);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(123.0F), 1073741824));
   }

   private class ListView extends FrameLayout {
      private RadioButton button;
      private boolean isThreeLines;
      private RectF rect;
      private TextPaint textPaint;

      public ListView(Context var2, boolean var3) {
         RadioButton var5;
         label20: {
            super(var2);
            this.rect = new RectF();
            boolean var4 = true;
            this.textPaint = new TextPaint(1);
            this.setWillNotDraw(false);
            this.isThreeLines = var3;
            this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0F));
            this.button = new RadioButton(var2) {
               public void invalidate() {
                  super.invalidate();
                  ListView.this.invalidate();
               }
            };
            this.button.setSize(AndroidUtilities.dp(20.0F));
            this.addView(this.button, LayoutHelper.createFrame(22, 22.0F, 53, 0.0F, 26.0F, 10.0F, 0.0F));
            var5 = this.button;
            if (this.isThreeLines) {
               var3 = var4;
               if (SharedConfig.useThreeLinesLayout) {
                  break label20;
               }
            }

            if (!this.isThreeLines && !SharedConfig.useThreeLinesLayout) {
               var3 = var4;
            } else {
               var3 = false;
            }
         }

         var5.setChecked(var3, false);
      }

      protected void onDraw(Canvas var1) {
         int var2 = Theme.getColor("switchTrack");
         int var3 = Color.red(var2);
         int var4 = Color.green(var2);
         int var5 = Color.blue(var2);
         this.button.setColor(Theme.getColor("radioBackground"), Theme.getColor("radioBackgroundChecked"));
         this.rect.set((float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(1.0F), (float)(this.getMeasuredWidth() - AndroidUtilities.dp(1.0F)), (float)AndroidUtilities.dp(73.0F));
         Theme.chat_instantViewRectPaint.setColor(Color.argb((int)(this.button.getProgress() * 43.0F), var3, var4, var5));
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), Theme.chat_instantViewRectPaint);
         this.rect.set(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)AndroidUtilities.dp(74.0F));
         Theme.dialogs_onlineCirclePaint.setColor(Color.argb((int)((1.0F - this.button.getProgress()) * 31.0F), var3, var4, var5));
         var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(6.0F), (float)AndroidUtilities.dp(6.0F), Theme.dialogs_onlineCirclePaint);
         String var6;
         if (this.isThreeLines) {
            var2 = 2131559041;
            var6 = "ChatListExpanded";
         } else {
            var2 = 2131559040;
            var6 = "ChatListDefault";
         }

         var6 = LocaleController.getString(var6, var2);
         var2 = (int)Math.ceil((double)this.textPaint.measureText(var6));
         this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         var1.drawText(var6, (float)((this.getMeasuredWidth() - var2) / 2), (float)AndroidUtilities.dp(96.0F), this.textPaint);

         for(var2 = 0; var2 < 2; ++var2) {
            float var7;
            if (var2 == 0) {
               var7 = 21.0F;
            } else {
               var7 = 53.0F;
            }

            int var8 = AndroidUtilities.dp(var7);
            Paint var16 = Theme.dialogs_onlineCirclePaint;
            short var9;
            if (var2 == 0) {
               var9 = 204;
            } else {
               var9 = 90;
            }

            var16.setColor(Color.argb(var9, var3, var4, var5));
            var1.drawCircle((float)AndroidUtilities.dp(22.0F), (float)var8, (float)AndroidUtilities.dp(11.0F), Theme.dialogs_onlineCirclePaint);
            int var18 = 0;

            while(true) {
               byte var10;
               if (this.isThreeLines) {
                  var10 = 3;
               } else {
                  var10 = 2;
               }

               if (var18 >= var10) {
                  break;
               }

               var16 = Theme.dialogs_onlineCirclePaint;
               short var19;
               if (var18 == 0) {
                  var19 = 204;
               } else {
                  var19 = 90;
               }

               var16.setColor(Color.argb(var19, var3, var4, var5));
               boolean var11 = this.isThreeLines;
               var7 = 72.0F;
               float var12;
               float var14;
               RectF var17;
               int var20;
               if (var11) {
                  var17 = this.rect;
                  var12 = (float)AndroidUtilities.dp(41.0F);
                  float var13 = (float)(var18 * 7);
                  var14 = (float)(var8 - AndroidUtilities.dp(8.3F - var13));
                  var20 = this.getMeasuredWidth();
                  if (var18 != 0) {
                     var7 = 48.0F;
                  }

                  var17.set(var12, var14, (float)(var20 - AndroidUtilities.dp(var7)), (float)(var8 - AndroidUtilities.dp(5.3F - var13)));
                  var1.drawRoundRect(this.rect, AndroidUtilities.dpf2(1.5F), AndroidUtilities.dpf2(1.5F), Theme.dialogs_onlineCirclePaint);
               } else {
                  var17 = this.rect;
                  var12 = (float)AndroidUtilities.dp(41.0F);
                  var20 = var18 * 10;
                  var14 = (float)(var8 - AndroidUtilities.dp((float)(7 - var20)));
                  int var15 = this.getMeasuredWidth();
                  if (var18 != 0) {
                     var7 = 48.0F;
                  }

                  var17.set(var12, var14, (float)(var15 - AndroidUtilities.dp(var7)), (float)(var8 - AndroidUtilities.dp((float)(3 - var20))));
                  var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), Theme.dialogs_onlineCirclePaint);
               }

               ++var18;
            }
         }

      }
   }
}
