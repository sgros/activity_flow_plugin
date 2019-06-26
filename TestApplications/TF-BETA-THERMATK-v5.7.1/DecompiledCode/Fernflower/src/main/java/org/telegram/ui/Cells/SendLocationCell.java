package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;

public class SendLocationCell extends FrameLayout {
   private SimpleTextView accurateTextView;
   private int currentAccount;
   private long dialogId;
   private ImageView imageView;
   private Runnable invalidateRunnable;
   private RectF rect;
   private SimpleTextView titleTextView;

   public SendLocationCell(Context var1, boolean var2) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.invalidateRunnable = new Runnable() {
         public void run() {
            SendLocationCell.this.checkText();
            SendLocationCell var1 = SendLocationCell.this;
            var1.invalidate((int)var1.rect.left - 5, (int)SendLocationCell.this.rect.top - 5, (int)SendLocationCell.this.rect.right + 5, (int)SendLocationCell.this.rect.bottom + 5);
            AndroidUtilities.runOnUIThread(SendLocationCell.this.invalidateRunnable, 1000L);
         }
      };
      this.imageView = new ImageView(var1);
      ImageView var3 = this.imageView;
      String var4;
      if (var2) {
         var4 = "location_sendLiveLocationBackgroundlocation_sendLiveLocationIcon";
      } else {
         var4 = "location_sendLocationBackgroundlocation_sendLocationIcon";
      }

      var3.setTag(var4);
      int var5 = AndroidUtilities.dp(40.0F);
      String var12 = "location_sendLiveLocationBackground";
      if (var2) {
         var4 = "location_sendLiveLocationBackground";
      } else {
         var4 = "location_sendLocationBackground";
      }

      int var6 = Theme.getColor(var4);
      if (var2) {
         var4 = var12;
      } else {
         var4 = "location_sendLocationBackground";
      }

      Drawable var16 = Theme.createSimpleSelectorCircleDrawable(var5, var6, Theme.getColor(var4));
      Drawable var13;
      CombinedDrawable var17;
      if (var2) {
         this.rect = new RectF();
         var13 = this.getResources().getDrawable(2131165535);
         var13.setColorFilter(new PorterDuffColorFilter(Theme.getColor("location_sendLiveLocationIcon"), Mode.MULTIPLY));
         var17 = new CombinedDrawable(var16, var13);
         var17.setCustomSize(AndroidUtilities.dp(40.0F), AndroidUtilities.dp(40.0F));
         this.imageView.setBackgroundDrawable(var17);
         AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000L);
         this.setWillNotDraw(false);
      } else {
         var13 = this.getResources().getDrawable(2131165762);
         var13.setColorFilter(new PorterDuffColorFilter(Theme.getColor("location_sendLocationIcon"), Mode.MULTIPLY));
         var17 = new CombinedDrawable(var16, var13);
         var17.setCustomSize(AndroidUtilities.dp(40.0F), AndroidUtilities.dp(40.0F));
         var17.setIconSize(AndroidUtilities.dp(24.0F), AndroidUtilities.dp(24.0F));
         this.imageView.setBackgroundDrawable(var17);
      }

      ImageView var18 = this.imageView;
      boolean var7 = LocaleController.isRTL;
      byte var14 = 5;
      byte var15;
      if (var7) {
         var15 = 5;
      } else {
         var15 = 3;
      }

      var7 = LocaleController.isRTL;
      float var8 = 17.0F;
      float var9;
      if (var7) {
         var9 = 0.0F;
      } else {
         var9 = 17.0F;
      }

      if (!LocaleController.isRTL) {
         var8 = 0.0F;
      }

      this.addView(var18, LayoutHelper.createFrame(40, 40.0F, var15 | 48, var9, 13.0F, var8, 0.0F));
      this.titleTextView = new SimpleTextView(var1);
      this.titleTextView.setTextSize(16);
      SimpleTextView var10 = this.titleTextView;
      var12 = "windowBackgroundWhiteRedText2";
      if (var2) {
         var4 = "windowBackgroundWhiteRedText2";
      } else {
         var4 = "windowBackgroundWhiteBlueText7";
      }

      var10.setTag(var4);
      var10 = this.titleTextView;
      if (var2) {
         var4 = var12;
      } else {
         var4 = "windowBackgroundWhiteBlueText7";
      }

      var10.setTextColor(Theme.getColor(var4));
      SimpleTextView var19 = this.titleTextView;
      if (LocaleController.isRTL) {
         var15 = 5;
      } else {
         var15 = 3;
      }

      var19.setGravity(var15);
      this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var19 = this.titleTextView;
      if (LocaleController.isRTL) {
         var15 = 5;
      } else {
         var15 = 3;
      }

      if (LocaleController.isRTL) {
         var9 = 16.0F;
      } else {
         var9 = 73.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 73.0F;
      } else {
         var8 = 16.0F;
      }

      this.addView(var19, LayoutHelper.createFrame(-1, 20.0F, var15 | 48, var9, 12.0F, var8, 0.0F));
      this.accurateTextView = new SimpleTextView(var1);
      this.accurateTextView.setTextSize(14);
      this.accurateTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
      SimpleTextView var11 = this.accurateTextView;
      if (LocaleController.isRTL) {
         var15 = 5;
      } else {
         var15 = 3;
      }

      var11.setGravity(var15);
      var11 = this.accurateTextView;
      if (LocaleController.isRTL) {
         var15 = var14;
      } else {
         var15 = 3;
      }

      if (LocaleController.isRTL) {
         var9 = 16.0F;
      } else {
         var9 = 73.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 73.0F;
      } else {
         var8 = 16.0F;
      }

      this.addView(var11, LayoutHelper.createFrame(-1, 20.0F, var15 | 48, var9, 37.0F, var8, 0.0F));
   }

   private void checkText() {
      LocationController.SharingLocationInfo var1 = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
      if (var1 != null) {
         String var2 = LocaleController.getString("StopLiveLocation", 2131560823);
         TLRPC.Message var6 = var1.messageObject.messageOwner;
         int var3 = var6.edit_date;
         long var4;
         if (var3 != 0) {
            var4 = (long)var3;
         } else {
            var4 = (long)var6.date;
         }

         this.setText(var2, LocaleController.formatLocationUpdateDate(var4));
      } else {
         this.setText(LocaleController.getString("SendLiveLocation", 2131560695), LocaleController.getString("SendLiveLocationInfo", 2131560699));
      }

   }

   private ImageView getImageView() {
      return this.imageView;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.rect != null) {
         AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000L);
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
   }

   protected void onDraw(Canvas var1) {
      LocationController.SharingLocationInfo var2 = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
      if (var2 != null) {
         int var3 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         int var4 = var2.stopTime;
         if (var4 >= var3) {
            float var5 = (float)Math.abs(var4 - var3) / (float)var2.period;
            if (LocaleController.isRTL) {
               this.rect.set((float)AndroidUtilities.dp(13.0F), (float)AndroidUtilities.dp(18.0F), (float)AndroidUtilities.dp(43.0F), (float)AndroidUtilities.dp(48.0F));
            } else {
               this.rect.set((float)(this.getMeasuredWidth() - AndroidUtilities.dp(43.0F)), (float)AndroidUtilities.dp(18.0F), (float)(this.getMeasuredWidth() - AndroidUtilities.dp(13.0F)), (float)AndroidUtilities.dp(48.0F));
            }

            var4 = Theme.getColor("location_liveLocationProgress");
            Theme.chat_radialProgress2Paint.setColor(var4);
            Theme.chat_livePaint.setColor(var4);
            var1.drawArc(this.rect, -90.0F, var5 * -360.0F, false, Theme.chat_radialProgress2Paint);
            String var6 = LocaleController.formatLocationLeftTime(Math.abs(var2.stopTime - var3));
            var5 = Theme.chat_livePaint.measureText(var6);
            var1.drawText(var6, this.rect.centerX() - var5 / 2.0F, (float)AndroidUtilities.dp(37.0F), Theme.chat_livePaint);
         }
      }
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(66.0F), 1073741824));
   }

   public void setDialogId(long var1) {
      this.dialogId = var1;
      this.checkText();
   }

   public void setHasLocation(boolean var1) {
      if (LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId) == null) {
         SimpleTextView var2 = this.titleTextView;
         float var3 = 1.0F;
         float var4;
         if (var1) {
            var4 = 1.0F;
         } else {
            var4 = 0.5F;
         }

         var2.setAlpha(var4);
         var2 = this.accurateTextView;
         if (var1) {
            var4 = 1.0F;
         } else {
            var4 = 0.5F;
         }

         var2.setAlpha(var4);
         ImageView var5 = this.imageView;
         if (var1) {
            var4 = var3;
         } else {
            var4 = 0.5F;
         }

         var5.setAlpha(var4);
      }

   }

   public void setText(String var1, String var2) {
      this.titleTextView.setText(var1);
      this.accurateTextView.setText(var2);
   }
}
