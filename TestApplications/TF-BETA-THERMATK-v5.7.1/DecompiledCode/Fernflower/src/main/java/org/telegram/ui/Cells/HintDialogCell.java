package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class HintDialogCell extends FrameLayout {
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private StaticLayout countLayout;
   private int countWidth;
   private int currentAccount;
   private TLRPC.User currentUser;
   private long dialog_id;
   private BackupImageView imageView;
   private int lastUnreadCount;
   private TextView nameTextView;
   private RectF rect = new RectF();

   public HintDialogCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.imageView = new BackupImageView(var1);
      this.imageView.setRoundRadius(AndroidUtilities.dp(27.0F));
      this.addView(this.imageView, LayoutHelper.createFrame(54, 54.0F, 49, 0.0F, 7.0F, 0.0F, 0.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.nameTextView.setTextSize(1, 12.0F);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setGravity(49);
      this.nameTextView.setLines(1);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 6.0F, 64.0F, 6.0F, 0.0F));
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5 = super.drawChild(var1, var2, var3);
      if (var2 == this.imageView) {
         int var6;
         int var7;
         float var10;
         if (this.countLayout != null) {
            var6 = AndroidUtilities.dp(6.0F);
            var7 = AndroidUtilities.dp(54.0F);
            int var8 = var7 - AndroidUtilities.dp(5.5F);
            this.rect.set((float)var8, (float)var6, (float)(var8 + this.countWidth + AndroidUtilities.dp(11.0F)), (float)(AndroidUtilities.dp(23.0F) + var6));
            RectF var9 = this.rect;
            var10 = AndroidUtilities.density;
            Paint var12;
            if (MessagesController.getInstance(this.currentAccount).isDialogMuted(this.dialog_id)) {
               var12 = Theme.dialogs_countGrayPaint;
            } else {
               var12 = Theme.dialogs_countPaint;
            }

            var1.drawRoundRect(var9, var10 * 11.5F, var10 * 11.5F, var12);
            var1.save();
            var1.translate((float)var7, (float)(var6 + AndroidUtilities.dp(4.0F)));
            this.countLayout.draw(var1);
            var1.restore();
         }

         TLRPC.User var13 = this.currentUser;
         if (var13 != null && !var13.bot) {
            TLRPC.UserStatus var14 = var13.status;
            if (var14 != null && var14.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(this.currentUser.id)) {
               var6 = AndroidUtilities.dp(53.0F);
               var7 = AndroidUtilities.dp(59.0F);
               Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite"));
               var10 = (float)var7;
               float var11 = (float)var6;
               var1.drawCircle(var10, var11, (float)AndroidUtilities.dp(7.0F), Theme.dialogs_onlineCirclePaint);
               Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle"));
               var1.drawCircle(var10, var11, (float)AndroidUtilities.dp(5.0F), Theme.dialogs_onlineCirclePaint);
            }
         }
      }

      return var5;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0F), 1073741824));
   }

   public void setDialog(int var1, boolean var2, CharSequence var3) {
      this.dialog_id = (long)var1;
      if (var1 > 0) {
         this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(var1);
         if (var3 != null) {
            this.nameTextView.setText(var3);
         } else {
            TLRPC.User var5 = this.currentUser;
            if (var5 != null) {
               this.nameTextView.setText(UserObject.getFirstName(var5));
            } else {
               this.nameTextView.setText("");
            }
         }

         this.avatarDrawable.setInfo(this.currentUser);
         this.imageView.setImage((ImageLocation)ImageLocation.getForUser(this.currentUser, false), "50_50", (Drawable)this.avatarDrawable, (Object)this.currentUser);
      } else {
         TLRPC.Chat var4 = MessagesController.getInstance(this.currentAccount).getChat(-var1);
         if (var3 != null) {
            this.nameTextView.setText(var3);
         } else if (var4 != null) {
            this.nameTextView.setText(var4.title);
         } else {
            this.nameTextView.setText("");
         }

         this.avatarDrawable.setInfo(var4);
         this.currentUser = null;
         this.imageView.setImage((ImageLocation)ImageLocation.getForChat(var4, false), "50_50", (Drawable)this.avatarDrawable, (Object)var4);
      }

      if (var2) {
         this.update(0);
      } else {
         this.countLayout = null;
      }

   }

   public void update() {
      int var1 = (int)this.dialog_id;
      if (var1 > 0) {
         this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(var1);
         this.avatarDrawable.setInfo(this.currentUser);
      } else {
         TLRPC.Chat var2 = MessagesController.getInstance(this.currentAccount).getChat(-var1);
         this.avatarDrawable.setInfo(var2);
         this.currentUser = null;
      }

   }

   public void update(int var1) {
      if ((var1 & 4) != 0 && this.currentUser != null) {
         this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(this.currentUser.id);
         this.imageView.invalidate();
         this.invalidate();
      }

      if (var1 == 0 || (var1 & 256) != 0 || (var1 & 2048) != 0) {
         TLRPC.Dialog var2 = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
         if (var2 != null) {
            int var3 = var2.unread_count;
            if (var3 != 0) {
               if (this.lastUnreadCount != var3) {
                  this.lastUnreadCount = var3;
                  String var4 = String.format("%d", var3);
                  this.countWidth = Math.max(AndroidUtilities.dp(12.0F), (int)Math.ceil((double)Theme.dialogs_countTextPaint.measureText(var4)));
                  this.countLayout = new StaticLayout(var4, Theme.dialogs_countTextPaint, this.countWidth, Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
                  if (var1 != 0) {
                     this.invalidate();
                     return;
                  }
               }

               return;
            }
         }

         if (this.countLayout != null) {
            if (var1 != 0) {
               this.invalidate();
            }

            this.lastUnreadCount = 0;
            this.countLayout = null;
         }

      }
   }
}
