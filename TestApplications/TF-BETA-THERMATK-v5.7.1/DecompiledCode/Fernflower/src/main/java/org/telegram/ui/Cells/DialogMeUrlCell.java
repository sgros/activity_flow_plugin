package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;

public class DialogMeUrlCell extends BaseCell {
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private ImageReceiver avatarImage = new ImageReceiver(this);
   private int avatarTop = AndroidUtilities.dp(10.0F);
   private int currentAccount;
   private boolean drawNameBot;
   private boolean drawNameBroadcast;
   private boolean drawNameGroup;
   private boolean drawNameLock;
   private boolean drawVerified;
   private boolean isSelected;
   private StaticLayout messageLayout;
   private int messageLeft;
   private int messageTop = AndroidUtilities.dp(40.0F);
   private StaticLayout nameLayout;
   private int nameLeft;
   private int nameLockLeft;
   private int nameLockTop;
   private int nameMuteLeft;
   private TLRPC.RecentMeUrl recentMeUrl;
   public boolean useSeparator;

   public DialogMeUrlCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      Theme.createDialogsResources(var1);
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(26.0F));
   }

   public void buildLayout() {
      TextPaint var1 = Theme.dialogs_namePaint;
      TextPaint var2 = Theme.dialogs_messagePaint;
      this.drawNameGroup = false;
      this.drawNameBroadcast = false;
      this.drawNameLock = false;
      this.drawNameBot = false;
      this.drawVerified = false;
      TLRPC.RecentMeUrl var3 = this.recentMeUrl;
      TLRPC.Chat var4;
      int var5;
      int var6;
      String var26;
      if (var3 instanceof TLRPC.TL_recentMeUrlChat) {
         var4 = MessagesController.getInstance(this.currentAccount).getChat(this.recentMeUrl.chat_id);
         if (var4.id < 0 || ChatObject.isChannel(var4) && !var4.megagroup) {
            this.drawNameBroadcast = true;
            this.nameLockTop = AndroidUtilities.dp(16.5F);
         } else {
            this.drawNameGroup = true;
            this.nameLockTop = AndroidUtilities.dp(17.5F);
         }

         this.drawVerified = var4.verified;
         Drawable var23;
         if (!LocaleController.isRTL) {
            this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            var5 = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4));
            if (this.drawNameGroup) {
               var23 = Theme.dialogs_groupDrawable;
            } else {
               var23 = Theme.dialogs_broadcastDrawable;
            }

            this.nameLeft = var5 + var23.getIntrinsicWidth();
         } else {
            var5 = this.getMeasuredWidth();
            var6 = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            if (this.drawNameGroup) {
               var23 = Theme.dialogs_groupDrawable;
            } else {
               var23 = Theme.dialogs_broadcastDrawable;
            }

            this.nameLockLeft = var5 - var6 - var23.getIntrinsicWidth();
            this.nameLeft = AndroidUtilities.dp(14.0F);
         }

         var26 = var4.title;
         this.avatarDrawable.setInfo(var4);
         this.avatarImage.setImage(ImageLocation.getForChat(var4, false), "50_50", this.avatarDrawable, (String)null, this.recentMeUrl, 0);
      } else if (var3 instanceof TLRPC.TL_recentMeUrlUser) {
         TLRPC.User var24 = MessagesController.getInstance(this.currentAccount).getUser(this.recentMeUrl.user_id);
         if (!LocaleController.isRTL) {
            this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
         } else {
            this.nameLeft = AndroidUtilities.dp(14.0F);
         }

         if (var24 != null) {
            if (var24.bot) {
               this.drawNameBot = true;
               this.nameLockTop = AndroidUtilities.dp(16.5F);
               if (!LocaleController.isRTL) {
                  this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                  this.nameLeft = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4)) + Theme.dialogs_botDrawable.getIntrinsicWidth();
               } else {
                  this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - Theme.dialogs_botDrawable.getIntrinsicWidth();
                  this.nameLeft = AndroidUtilities.dp(14.0F);
               }
            }

            this.drawVerified = var24.verified;
         }

         var26 = UserObject.getUserName(var24);
         this.avatarDrawable.setInfo(var24);
         this.avatarImage.setImage(ImageLocation.getForUser(var24, false), "50_50", this.avatarDrawable, (String)null, this.recentMeUrl, 0);
      } else if (var3 instanceof TLRPC.TL_recentMeUrlStickerSet) {
         if (!LocaleController.isRTL) {
            this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
         } else {
            this.nameLeft = AndroidUtilities.dp(14.0F);
         }

         var26 = this.recentMeUrl.set.set.title;
         this.avatarDrawable.setInfo(5, var26, (String)null, false);
         this.avatarImage.setImage(ImageLocation.getForDocument(this.recentMeUrl.set.cover), (String)null, this.avatarDrawable, (String)null, this.recentMeUrl, 0);
      } else if (var3 instanceof TLRPC.TL_recentMeUrlChatInvite) {
         if (!LocaleController.isRTL) {
            this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
         } else {
            this.nameLeft = AndroidUtilities.dp(14.0F);
         }

         TLRPC.ChatInvite var28 = this.recentMeUrl.chat_invite;
         var4 = var28.chat;
         if (var4 != null) {
            this.avatarDrawable.setInfo(var4);
            var4 = this.recentMeUrl.chat_invite.chat;
            var26 = var4.title;
            if (var4.id < 0 || ChatObject.isChannel(var4) && !this.recentMeUrl.chat_invite.chat.megagroup) {
               this.drawNameBroadcast = true;
               this.nameLockTop = AndroidUtilities.dp(16.5F);
            } else {
               this.drawNameGroup = true;
               this.nameLockTop = AndroidUtilities.dp(17.5F);
            }

            var4 = this.recentMeUrl.chat_invite.chat;
            this.drawVerified = var4.verified;
            this.avatarImage.setImage(ImageLocation.getForChat(var4, false), "50_50", this.avatarDrawable, (String)null, this.recentMeUrl, 0);
         } else {
            var26 = var28.title;
            this.avatarDrawable.setInfo(5, var26, (String)null, false);
            TLRPC.ChatInvite var25 = this.recentMeUrl.chat_invite;
            if (!var25.broadcast && !var25.channel) {
               this.drawNameGroup = true;
               this.nameLockTop = AndroidUtilities.dp(17.5F);
            } else {
               this.drawNameBroadcast = true;
               this.nameLockTop = AndroidUtilities.dp(16.5F);
            }

            TLRPC.PhotoSize var27 = FileLoader.getClosestPhotoSizeWithSize(this.recentMeUrl.chat_invite.photo.sizes, 50);
            this.avatarImage.setImage(ImageLocation.getForPhoto(var27, this.recentMeUrl.chat_invite.photo), "50_50", this.avatarDrawable, (String)null, this.recentMeUrl, 0);
         }

         Drawable var29;
         if (!LocaleController.isRTL) {
            this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            var5 = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4));
            if (this.drawNameGroup) {
               var29 = Theme.dialogs_groupDrawable;
            } else {
               var29 = Theme.dialogs_broadcastDrawable;
            }

            this.nameLeft = var5 + var29.getIntrinsicWidth();
         } else {
            var5 = this.getMeasuredWidth();
            var6 = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            if (this.drawNameGroup) {
               var29 = Theme.dialogs_groupDrawable;
            } else {
               var29 = Theme.dialogs_broadcastDrawable;
            }

            this.nameLockLeft = var5 - var6 - var29.getIntrinsicWidth();
            this.nameLeft = AndroidUtilities.dp(14.0F);
         }
      } else if (var3 instanceof TLRPC.TL_recentMeUrlUnknown) {
         if (!LocaleController.isRTL) {
            this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
         } else {
            this.nameLeft = AndroidUtilities.dp(14.0F);
         }

         this.avatarImage.setImage((ImageLocation)null, (String)null, this.avatarDrawable, (String)null, this.recentMeUrl, 0);
         var26 = "Url";
      } else {
         this.avatarImage.setImage((ImageLocation)null, (String)null, this.avatarDrawable, (String)null, var3, 0);
         var26 = "";
      }

      StringBuilder var30 = new StringBuilder();
      var30.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
      var30.append("/");
      var30.append(this.recentMeUrl.url);
      String var7 = var30.toString();
      String var32 = var26;
      if (TextUtils.isEmpty(var26)) {
         var32 = LocaleController.getString("HiddenName", 2131559636);
      }

      if (!LocaleController.isRTL) {
         var6 = this.getMeasuredWidth() - this.nameLeft;
         var5 = AndroidUtilities.dp(14.0F);
      } else {
         var6 = this.getMeasuredWidth() - this.nameLeft;
         var5 = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
      }

      int var8;
      label203: {
         var8 = var6 - var5;
         if (this.drawNameLock) {
            var5 = AndroidUtilities.dp(4.0F);
            var6 = Theme.dialogs_lockDrawable.getIntrinsicWidth();
         } else if (this.drawNameGroup) {
            var5 = AndroidUtilities.dp(4.0F);
            var6 = Theme.dialogs_groupDrawable.getIntrinsicWidth();
         } else if (this.drawNameBroadcast) {
            var5 = AndroidUtilities.dp(4.0F);
            var6 = Theme.dialogs_broadcastDrawable.getIntrinsicWidth();
         } else {
            var5 = var8;
            if (!this.drawNameBot) {
               break label203;
            }

            var5 = AndroidUtilities.dp(4.0F);
            var6 = Theme.dialogs_botDrawable.getIntrinsicWidth();
         }

         var5 = var8 - (var5 + var6);
      }

      var6 = var5;
      if (this.drawVerified) {
         var8 = AndroidUtilities.dp(6.0F) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
         var5 -= var8;
         var6 = var5;
         if (LocaleController.isRTL) {
            this.nameLeft += var8;
            var6 = var5;
         }
      }

      var6 = Math.max(AndroidUtilities.dp(12.0F), var6);

      try {
         CharSequence var31 = TextUtils.ellipsize(var32.replace('\n', ' '), var1, (float)(var6 - AndroidUtilities.dp(12.0F)), TruncateAt.END);
         StaticLayout var34 = new StaticLayout(var31, var1, var6, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.nameLayout = var34;
      } catch (Exception var22) {
         FileLog.e((Throwable)var22);
      }

      int var9 = this.getMeasuredWidth();
      var8 = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 16));
      float var10;
      if (!LocaleController.isRTL) {
         this.messageLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
         if (AndroidUtilities.isTablet()) {
            var10 = 13.0F;
         } else {
            var10 = 9.0F;
         }

         var5 = AndroidUtilities.dp(var10);
      } else {
         this.messageLeft = AndroidUtilities.dp(16.0F);
         var5 = this.getMeasuredWidth();
         if (AndroidUtilities.isTablet()) {
            var10 = 65.0F;
         } else {
            var10 = 61.0F;
         }

         var5 -= AndroidUtilities.dp(var10);
      }

      this.avatarImage.setImageCoords(var5, this.avatarTop, AndroidUtilities.dp(52.0F), AndroidUtilities.dp(52.0F));
      var5 = Math.max(AndroidUtilities.dp(12.0F), var9 - var8);
      CharSequence var35 = TextUtils.ellipsize(var7, var2, (float)(var5 - AndroidUtilities.dp(12.0F)), TruncateAt.END);

      StaticLayout var33;
      try {
         var33 = new StaticLayout(var35, var2, var5, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.messageLayout = var33;
      } catch (Exception var21) {
         FileLog.e((Throwable)var21);
      }

      double var11;
      double var15;
      double var19;
      if (LocaleController.isRTL) {
         var33 = this.nameLayout;
         if (var33 != null && var33.getLineCount() > 0) {
            var10 = this.nameLayout.getLineLeft(0);
            var11 = Math.ceil((double)this.nameLayout.getLineWidth(0));
            if (this.drawVerified) {
               double var13 = (double)this.nameLeft;
               var15 = (double)var6;
               Double.isNaN(var15);
               Double.isNaN(var13);
               double var17 = (double)AndroidUtilities.dp(6.0F);
               Double.isNaN(var17);
               var19 = (double)Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
               Double.isNaN(var19);
               this.nameMuteLeft = (int)(var13 + (var15 - var11) - var17 - var19);
            }

            if (var10 == 0.0F) {
               var15 = (double)var6;
               if (var11 < var15) {
                  var19 = (double)this.nameLeft;
                  Double.isNaN(var15);
                  Double.isNaN(var19);
                  this.nameLeft = (int)(var19 + (var15 - var11));
               }
            }
         }

         var33 = this.messageLayout;
         if (var33 != null && var33.getLineCount() > 0 && this.messageLayout.getLineLeft(0) == 0.0F) {
            var19 = Math.ceil((double)this.messageLayout.getLineWidth(0));
            var15 = (double)var5;
            if (var19 < var15) {
               var11 = (double)this.messageLeft;
               Double.isNaN(var15);
               Double.isNaN(var11);
               this.messageLeft = (int)(var11 + (var15 - var19));
            }
         }
      } else {
         var33 = this.nameLayout;
         if (var33 != null && var33.getLineCount() > 0) {
            var10 = this.nameLayout.getLineRight(0);
            if (var10 == (float)var6) {
               var19 = Math.ceil((double)this.nameLayout.getLineWidth(0));
               var15 = (double)var6;
               if (var19 < var15) {
                  var11 = (double)this.nameLeft;
                  Double.isNaN(var15);
                  Double.isNaN(var11);
                  this.nameLeft = (int)(var11 - (var15 - var19));
               }
            }

            if (this.drawVerified) {
               this.nameMuteLeft = (int)((float)this.nameLeft + var10 + (float)AndroidUtilities.dp(6.0F));
            }
         }

         var33 = this.messageLayout;
         if (var33 != null && var33.getLineCount() > 0 && this.messageLayout.getLineRight(0) == (float)var5) {
            var11 = Math.ceil((double)this.messageLayout.getLineWidth(0));
            var19 = (double)var5;
            if (var11 < var19) {
               var15 = (double)this.messageLeft;
               Double.isNaN(var19);
               Double.isNaN(var15);
               this.messageLeft = (int)(var15 - (var19 - var11));
            }
         }
      }

   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.avatarImage.onAttachedToWindow();
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.avatarImage.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      if (this.isSelected) {
         var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_tabletSeletedPaint);
      }

      if (this.drawNameLock) {
         BaseCell.setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
         Theme.dialogs_lockDrawable.draw(var1);
      } else if (this.drawNameGroup) {
         BaseCell.setDrawableBounds(Theme.dialogs_groupDrawable, this.nameLockLeft, this.nameLockTop);
         Theme.dialogs_groupDrawable.draw(var1);
      } else if (this.drawNameBroadcast) {
         BaseCell.setDrawableBounds(Theme.dialogs_broadcastDrawable, this.nameLockLeft, this.nameLockTop);
         Theme.dialogs_broadcastDrawable.draw(var1);
      } else if (this.drawNameBot) {
         BaseCell.setDrawableBounds(Theme.dialogs_botDrawable, this.nameLockLeft, this.nameLockTop);
         Theme.dialogs_botDrawable.draw(var1);
      }

      if (this.nameLayout != null) {
         var1.save();
         var1.translate((float)this.nameLeft, (float)AndroidUtilities.dp(13.0F));
         this.nameLayout.draw(var1);
         var1.restore();
      }

      if (this.messageLayout != null) {
         var1.save();
         var1.translate((float)this.messageLeft, (float)this.messageTop);

         try {
            this.messageLayout.draw(var1);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         var1.restore();
      }

      if (this.drawVerified) {
         BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5F));
         BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5F));
         Theme.dialogs_verifiedDrawable.draw(var1);
         Theme.dialogs_verifiedCheckDrawable.draw(var1);
      }

      if (this.useSeparator) {
         if (LocaleController.isRTL) {
            var1.drawLine(0.0F, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline)), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
         } else {
            var1.drawLine((float)AndroidUtilities.dp((float)AndroidUtilities.leftBaseline), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
         }
      }

      this.avatarImage.draw(var1);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (var1) {
         this.buildLayout();
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(72.0F) + this.useSeparator);
   }

   public void setDialogSelected(boolean var1) {
      if (this.isSelected != var1) {
         this.invalidate();
      }

      this.isSelected = var1;
   }

   public void setRecentMeUrl(TLRPC.RecentMeUrl var1) {
      this.recentMeUrl = var1;
      this.requestLayout();
   }
}
