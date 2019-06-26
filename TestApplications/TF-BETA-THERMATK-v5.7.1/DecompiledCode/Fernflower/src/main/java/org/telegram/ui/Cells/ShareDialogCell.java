package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.LayoutHelper;

public class ShareDialogCell extends FrameLayout {
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private CheckBox2 checkBox;
   private int currentAccount;
   private BackupImageView imageView;
   private long lastUpdateTime;
   private TextView nameTextView;
   private float onlineProgress;
   private TLRPC.User user;

   public ShareDialogCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.setWillNotDraw(false);
      this.imageView = new BackupImageView(var1);
      this.imageView.setRoundRadius(AndroidUtilities.dp(28.0F));
      this.addView(this.imageView, LayoutHelper.createFrame(56, 56.0F, 49, 0.0F, 7.0F, 0.0F, 0.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("dialogTextBlack"));
      this.nameTextView.setTextSize(1, 12.0F);
      this.nameTextView.setMaxLines(2);
      this.nameTextView.setGravity(49);
      this.nameTextView.setLines(2);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 6.0F, 66.0F, 6.0F, 0.0F));
      this.checkBox = new CheckBox2(var1);
      this.checkBox.setSize(21);
      this.checkBox.setColor("dialogRoundCheckBox", "dialogBackground", "dialogRoundCheckBoxCheck");
      this.checkBox.setDrawUnchecked(false);
      this.checkBox.setDrawBackgroundAsArc(4);
      this.checkBox.setProgressDelegate(new _$$Lambda$ShareDialogCell$Ua6Rykc_bDn7xM5VKieCIkl7edo(this));
      this.addView(this.checkBox, LayoutHelper.createFrame(24, 24.0F, 49, 19.0F, 42.0F, 0.0F, 0.0F));
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      boolean var5 = super.drawChild(var1, var2, var3);
      if (var2 == this.imageView) {
         TLRPC.User var17 = this.user;
         if (var17 != null && !MessagesController.isSupportUser(var17)) {
            long var6 = SystemClock.uptimeMillis();
            long var8 = var6 - this.lastUpdateTime;
            var3 = var8;
            if (var8 > 17L) {
               var3 = 17L;
            }

            boolean var10;
            label48: {
               this.lastUpdateTime = var6;
               var17 = this.user;
               if (!var17.self && !var17.bot) {
                  TLRPC.UserStatus var18 = var17.status;
                  if (var18 != null && var18.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(this.user.id)) {
                     var10 = true;
                     break label48;
                  }
               }

               var10 = false;
            }

            if (var10 || this.onlineProgress != 0.0F) {
               int var11 = this.imageView.getBottom();
               int var12 = AndroidUtilities.dp(6.0F);
               int var13 = this.imageView.getRight();
               int var14 = AndroidUtilities.dp(10.0F);
               Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite"));
               float var15 = (float)(var13 - var14);
               float var16 = (float)(var11 - var12);
               var1.drawCircle(var15, var16, (float)AndroidUtilities.dp(7.0F) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
               Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle"));
               var1.drawCircle(var15, var16, (float)AndroidUtilities.dp(5.0F) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
               if (var10) {
                  var15 = this.onlineProgress;
                  if (var15 < 1.0F) {
                     this.onlineProgress = var15 + (float)var3 / 150.0F;
                     if (this.onlineProgress > 1.0F) {
                        this.onlineProgress = 1.0F;
                     }

                     this.imageView.invalidate();
                     this.invalidate();
                  }
               } else {
                  var15 = this.onlineProgress;
                  if (var15 > 0.0F) {
                     this.onlineProgress = var15 - (float)var3 / 150.0F;
                     if (this.onlineProgress < 0.0F) {
                        this.onlineProgress = 0.0F;
                     }

                     this.imageView.invalidate();
                     this.invalidate();
                  }
               }
            }
         }
      }

      return var5;
   }

   // $FF: synthetic method
   public void lambda$new$0$ShareDialogCell(float var1) {
      var1 = 1.0F - this.checkBox.getProgress() * 0.143F;
      this.imageView.setScaleX(var1);
      this.imageView.setScaleY(var1);
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.imageView.getLeft();
      int var3 = this.imageView.getMeasuredWidth() / 2;
      int var4 = this.imageView.getTop();
      int var5 = this.imageView.getMeasuredHeight() / 2;
      Theme.checkboxSquare_checkPaint.setColor(Theme.getColor("dialogRoundCheckBox"));
      Theme.checkboxSquare_checkPaint.setAlpha((int)(this.checkBox.getProgress() * 255.0F));
      var1.drawCircle((float)(var2 + var3), (float)(var4 + var5), (float)AndroidUtilities.dp(28.0F), Theme.checkboxSquare_checkPaint);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(103.0F), 1073741824));
   }

   public void setChecked(boolean var1, boolean var2) {
      this.checkBox.setChecked(var1, var2);
   }

   public void setDialog(int var1, boolean var2, CharSequence var3) {
      if (var1 > 0) {
         this.user = MessagesController.getInstance(this.currentAccount).getUser(var1);
         this.avatarDrawable.setInfo(this.user);
         if (UserObject.isUserSelf(this.user)) {
            this.nameTextView.setText(LocaleController.getString("SavedMessages", 2131560633));
            this.avatarDrawable.setAvatarType(1);
            this.imageView.setImage((ImageLocation)null, (String)null, (Drawable)this.avatarDrawable, (Object)this.user);
         } else {
            if (var3 != null) {
               this.nameTextView.setText(var3);
            } else {
               TLRPC.User var5 = this.user;
               if (var5 != null) {
                  this.nameTextView.setText(ContactsController.formatName(var5.first_name, var5.last_name));
               } else {
                  this.nameTextView.setText("");
               }
            }

            this.imageView.setImage((ImageLocation)ImageLocation.getForUser(this.user, false), "50_50", (Drawable)this.avatarDrawable, (Object)this.user);
         }
      } else {
         this.user = null;
         TLRPC.Chat var4 = MessagesController.getInstance(this.currentAccount).getChat(-var1);
         if (var3 != null) {
            this.nameTextView.setText(var3);
         } else if (var4 != null) {
            this.nameTextView.setText(var4.title);
         } else {
            this.nameTextView.setText("");
         }

         this.avatarDrawable.setInfo(var4);
         this.imageView.setImage((ImageLocation)ImageLocation.getForChat(var4, false), "50_50", (Drawable)this.avatarDrawable, (Object)var4);
      }

      this.checkBox.setChecked(var2, false);
   }
}
