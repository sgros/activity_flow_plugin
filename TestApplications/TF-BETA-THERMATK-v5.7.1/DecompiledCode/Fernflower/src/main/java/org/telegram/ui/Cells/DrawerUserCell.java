package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.GroupCreateCheckBox;
import org.telegram.ui.Components.LayoutHelper;

public class DrawerUserCell extends FrameLayout {
   private int accountNumber;
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private GroupCreateCheckBox checkBox;
   private BackupImageView imageView;
   private RectF rect = new RectF();
   private TextView textView;

   public DrawerUserCell(Context var1) {
      super(var1);
      this.avatarDrawable.setTextSize(AndroidUtilities.dp(12.0F));
      this.imageView = new BackupImageView(var1);
      this.imageView.setRoundRadius(AndroidUtilities.dp(18.0F));
      this.addView(this.imageView, LayoutHelper.createFrame(36, 36.0F, 51, 14.0F, 6.0F, 0.0F, 0.0F));
      this.textView = new TextView(var1);
      this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
      this.textView.setTextSize(1, 15.0F);
      this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setGravity(19);
      this.textView.setEllipsize(TruncateAt.END);
      this.addView(this.textView, LayoutHelper.createFrame(-1, -1.0F, 51, 72.0F, 0.0F, 60.0F, 0.0F));
      this.checkBox = new GroupCreateCheckBox(var1);
      this.checkBox.setChecked(true, false);
      this.checkBox.setCheckScale(0.9F);
      this.checkBox.setInnerRadDiff(AndroidUtilities.dp(1.5F));
      this.checkBox.setColorKeysOverrides("chats_unreadCounterText", "chats_unreadCounter", "chats_menuBackground");
      this.addView(this.checkBox, LayoutHelper.createFrame(18, 18.0F, 51, 37.0F, 27.0F, 0.0F, 0.0F));
      this.setWillNotDraw(false);
   }

   public int getAccountNumber() {
      return this.accountNumber;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
   }

   protected void onDraw(Canvas var1) {
      if (UserConfig.getActivatedAccountsCount() > 1 && NotificationsController.getInstance(this.accountNumber).showBadgeNumber) {
         int var2 = NotificationsController.getInstance(this.accountNumber).getTotalUnreadCount();
         if (var2 <= 0) {
            return;
         }

         String var3 = String.format("%d", var2);
         int var4 = AndroidUtilities.dp(12.5F);
         int var5 = (int)Math.ceil((double)Theme.dialogs_countTextPaint.measureText(var3));
         int var6 = Math.max(AndroidUtilities.dp(10.0F), var5);
         var2 = this.getMeasuredWidth() - var6 - AndroidUtilities.dp(25.0F) - AndroidUtilities.dp(5.5F);
         this.rect.set((float)var2, (float)var4, (float)(var2 + var6 + AndroidUtilities.dp(14.0F)), (float)(AndroidUtilities.dp(23.0F) + var4));
         RectF var7 = this.rect;
         float var8 = AndroidUtilities.density;
         var1.drawRoundRect(var7, var8 * 11.5F, var8 * 11.5F, Theme.dialogs_countPaint);
         var7 = this.rect;
         var1.drawText(var3, var7.left + (var7.width() - (float)var5) / 2.0F, (float)(var4 + AndroidUtilities.dp(16.0F)), Theme.dialogs_countTextPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
   }

   public void setAccount(int var1) {
      this.accountNumber = var1;
      TLRPC.User var2 = UserConfig.getInstance(this.accountNumber).getCurrentUser();
      if (var2 != null) {
         this.avatarDrawable.setInfo(var2);
         this.textView.setText(ContactsController.formatName(var2.first_name, var2.last_name));
         this.imageView.getImageReceiver().setCurrentAccount(var1);
         BackupImageView var3 = this.imageView;
         byte var4 = 0;
         var3.setImage((ImageLocation)ImageLocation.getForUser(var2, false), "50_50", (Drawable)this.avatarDrawable, (Object)var2);
         GroupCreateCheckBox var6 = this.checkBox;
         byte var5;
         if (var1 == UserConfig.selectedAccount) {
            var5 = var4;
         } else {
            var5 = 4;
         }

         var6.setVisibility(var5);
      }
   }
}
