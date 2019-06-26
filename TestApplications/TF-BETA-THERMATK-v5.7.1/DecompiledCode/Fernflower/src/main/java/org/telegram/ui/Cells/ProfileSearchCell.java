package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;

public class ProfileSearchCell extends BaseCell {
   private AvatarDrawable avatarDrawable;
   private ImageReceiver avatarImage;
   private TLRPC.Chat chat;
   private StaticLayout countLayout;
   private int countLeft;
   private int countTop;
   private int countWidth;
   private int currentAccount;
   private CharSequence currentName;
   private long dialog_id;
   private boolean drawCheck;
   private boolean drawCount;
   private boolean drawNameBot;
   private boolean drawNameBroadcast;
   private boolean drawNameGroup;
   private boolean drawNameLock;
   private TLRPC.EncryptedChat encryptedChat;
   private TLRPC.FileLocation lastAvatar;
   private String lastName;
   private int lastStatus;
   private int lastUnreadCount;
   private StaticLayout nameLayout;
   private int nameLeft;
   private int nameLockLeft;
   private int nameLockTop;
   private int nameTop;
   private int nameWidth;
   private RectF rect;
   private boolean savedMessages;
   private StaticLayout statusLayout;
   private int statusLeft;
   private CharSequence subLabel;
   private int sublabelOffsetX;
   private int sublabelOffsetY;
   public boolean useSeparator;
   private TLRPC.User user;

   public ProfileSearchCell(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.countTop = AndroidUtilities.dp(19.0F);
      this.rect = new RectF();
      this.avatarImage = new ImageReceiver(this);
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(23.0F));
      this.avatarDrawable = new AvatarDrawable();
   }

   public void buildLayout() {
      this.drawNameBroadcast = false;
      this.drawNameLock = false;
      this.drawNameGroup = false;
      this.drawCheck = false;
      this.drawNameBot = false;
      TLRPC.EncryptedChat var1 = this.encryptedChat;
      int var2;
      int var3;
      TLRPC.Chat var14;
      TLRPC.User var16;
      if (var1 != null) {
         this.drawNameLock = true;
         this.dialog_id = (long)var1.id << 32;
         if (!LocaleController.isRTL) {
            this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            this.nameLeft = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4)) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
         } else {
            this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 2)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
            this.nameLeft = AndroidUtilities.dp(11.0F);
         }

         this.nameLockTop = AndroidUtilities.dp(20.5F);
      } else {
         var14 = this.chat;
         if (var14 != null) {
            var2 = var14.id;
            if (var2 < 0) {
               this.dialog_id = AndroidUtilities.makeBroadcastId(var2);
               if (SharedConfig.drawDialogIcons) {
                  this.drawNameBroadcast = true;
               }

               this.nameLockTop = AndroidUtilities.dp(22.5F);
            } else {
               this.dialog_id = (long)(-var2);
               if (SharedConfig.drawDialogIcons) {
                  if (ChatObject.isChannel(var14) && !this.chat.megagroup) {
                     this.drawNameBroadcast = true;
                     this.nameLockTop = AndroidUtilities.dp(22.5F);
                  } else {
                     this.drawNameGroup = true;
                     this.nameLockTop = AndroidUtilities.dp(24.0F);
                  }
               }
            }

            this.drawCheck = this.chat.verified;
            if (SharedConfig.drawDialogIcons) {
               Drawable var15;
               if (!LocaleController.isRTL) {
                  this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                  var2 = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4));
                  if (this.drawNameGroup) {
                     var15 = Theme.dialogs_groupDrawable;
                  } else {
                     var15 = Theme.dialogs_broadcastDrawable;
                  }

                  this.nameLeft = var2 + var15.getIntrinsicWidth();
               } else {
                  var3 = this.getMeasuredWidth();
                  var2 = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 2));
                  if (this.drawNameGroup) {
                     var15 = Theme.dialogs_groupDrawable;
                  } else {
                     var15 = Theme.dialogs_broadcastDrawable;
                  }

                  this.nameLockLeft = var3 - var2 - var15.getIntrinsicWidth();
                  this.nameLeft = AndroidUtilities.dp(11.0F);
               }
            } else if (!LocaleController.isRTL) {
               this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            } else {
               this.nameLeft = AndroidUtilities.dp(11.0F);
            }
         } else {
            var16 = this.user;
            if (var16 != null) {
               this.dialog_id = (long)var16.id;
               if (!LocaleController.isRTL) {
                  this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
               } else {
                  this.nameLeft = AndroidUtilities.dp(11.0F);
               }

               label257: {
                  if (SharedConfig.drawDialogIcons) {
                     var16 = this.user;
                     if (var16.bot && !MessagesController.isSupportUser(var16)) {
                        this.drawNameBot = true;
                        if (!LocaleController.isRTL) {
                           this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                           this.nameLeft = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4)) + Theme.dialogs_botDrawable.getIntrinsicWidth();
                        } else {
                           this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 2)) - Theme.dialogs_botDrawable.getIntrinsicWidth();
                           this.nameLeft = AndroidUtilities.dp(11.0F);
                        }

                        this.nameLockTop = AndroidUtilities.dp(20.5F);
                        break label257;
                     }
                  }

                  this.nameLockTop = AndroidUtilities.dp(21.0F);
               }

               this.drawCheck = this.user.verified;
            }
         }
      }

      Object var4 = this.currentName;
      String var17;
      if (var4 == null) {
         var14 = this.chat;
         if (var14 != null) {
            var17 = var14.title;
         } else {
            var16 = this.user;
            if (var16 != null) {
               var17 = UserObject.getUserName(var16);
            } else {
               var17 = "";
            }
         }

         var4 = var17.replace('\n', ' ');
      }

      Object var22 = var4;
      if (((CharSequence)var4).length() == 0) {
         label240: {
            var16 = this.user;
            if (var16 != null) {
               var17 = var16.phone;
               if (var17 != null && var17.length() != 0) {
                  PhoneFormat var24 = PhoneFormat.getInstance();
                  StringBuilder var18 = new StringBuilder();
                  var18.append("+");
                  var18.append(this.user.phone);
                  var22 = var24.format(var18.toString());
                  break label240;
               }
            }

            var22 = LocaleController.getString("HiddenName", 2131559636);
         }
      }

      TextPaint var19;
      if (this.encryptedChat != null) {
         var19 = Theme.dialogs_searchNameEncryptedPaint;
      } else {
         var19 = Theme.dialogs_searchNamePaint;
      }

      if (!LocaleController.isRTL) {
         var2 = this.getMeasuredWidth() - this.nameLeft - AndroidUtilities.dp(14.0F);
         this.nameWidth = var2;
      } else {
         var2 = this.getMeasuredWidth() - this.nameLeft - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
         this.nameWidth = var2;
      }

      if (this.drawNameLock) {
         this.nameWidth -= AndroidUtilities.dp(6.0F) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
      } else if (this.drawNameBroadcast) {
         this.nameWidth -= AndroidUtilities.dp(6.0F) + Theme.dialogs_broadcastDrawable.getIntrinsicWidth();
      } else if (this.drawNameGroup) {
         this.nameWidth -= AndroidUtilities.dp(6.0F) + Theme.dialogs_groupDrawable.getIntrinsicWidth();
      } else if (this.drawNameBot) {
         this.nameWidth -= AndroidUtilities.dp(6.0F) + Theme.dialogs_botDrawable.getIntrinsicWidth();
      }

      this.nameWidth -= this.getPaddingLeft() + this.getPaddingRight();
      var3 = var2 - (this.getPaddingLeft() + this.getPaddingRight());
      if (this.drawCount) {
         label232: {
            TLRPC.Dialog var5 = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
            if (var5 != null) {
               var2 = var5.unread_count;
               if (var2 != 0) {
                  this.lastUnreadCount = var2;
                  String var20 = String.format("%d", var2);
                  this.countWidth = Math.max(AndroidUtilities.dp(12.0F), (int)Math.ceil((double)Theme.dialogs_countTextPaint.measureText(var20)));
                  this.countLayout = new StaticLayout(var20, Theme.dialogs_countTextPaint, this.countWidth, Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
                  var2 = this.countWidth + AndroidUtilities.dp(18.0F);
                  this.nameWidth -= var2;
                  if (!LocaleController.isRTL) {
                     this.countLeft = this.getMeasuredWidth() - this.countWidth - AndroidUtilities.dp(19.0F);
                  } else {
                     this.countLeft = AndroidUtilities.dp(19.0F);
                     this.nameLeft += var2;
                  }
                  break label232;
               }
            }

            this.lastUnreadCount = 0;
            this.countLayout = null;
         }
      } else {
         this.lastUnreadCount = 0;
         this.countLayout = null;
      }

      this.nameLayout = new StaticLayout(TextUtils.ellipsize((CharSequence)var22, var19, (float)(this.nameWidth - AndroidUtilities.dp(12.0F)), TruncateAt.END), var19, this.nameWidth, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
      TextPaint var21 = Theme.dialogs_offlinePaint;
      if (!LocaleController.isRTL) {
         this.statusLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
      } else {
         this.statusLeft = AndroidUtilities.dp(11.0F);
      }

      var14 = this.chat;
      if (var14 != null && this.subLabel == null) {
         if (var14 != null) {
            label219: {
               if (ChatObject.isChannel(var14)) {
                  var14 = this.chat;
                  if (!var14.megagroup) {
                     if (TextUtils.isEmpty(var14.username)) {
                        var22 = LocaleController.getString("ChannelPrivate", 2131558988).toLowerCase();
                     } else {
                        var22 = LocaleController.getString("ChannelPublic", 2131558991).toLowerCase();
                     }
                     break label219;
                  }
               }

               if (TextUtils.isEmpty(this.chat.username)) {
                  var22 = LocaleController.getString("MegaPrivate", 2131559831).toLowerCase();
               } else {
                  var22 = LocaleController.getString("MegaPublic", 2131559834).toLowerCase();
               }
            }
         } else {
            var22 = null;
         }

         this.nameTop = AndroidUtilities.dp(19.0F);
      } else {
         var22 = this.subLabel;
         if (var22 != null) {
            var19 = var21;
         } else {
            var16 = this.user;
            if (var16 != null) {
               if (MessagesController.isSupportUser(var16)) {
                  var22 = LocaleController.getString("SupportStatus", 2131560848);
                  var19 = var21;
               } else {
                  var16 = this.user;
                  if (var16.bot) {
                     var22 = LocaleController.getString("Bot", 2131558848);
                     var19 = var21;
                  } else {
                     var2 = var16.id;
                     if (var2 != 333000 && var2 != 777000) {
                        String var6 = LocaleController.formatUserStatus(this.currentAccount, var16);
                        TLRPC.User var7 = this.user;
                        var19 = var21;
                        var22 = var6;
                        if (var7 != null) {
                           label280: {
                              if (var7.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                 TLRPC.UserStatus var23 = this.user.status;
                                 var19 = var21;
                                 var22 = var6;
                                 if (var23 == null) {
                                    break label280;
                                 }

                                 var19 = var21;
                                 var22 = var6;
                                 if (var23.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                                    break label280;
                                 }
                              }

                              var19 = Theme.dialogs_onlinePaint;
                              var22 = LocaleController.getString("Online", 2131560100);
                           }
                        }
                     } else {
                        var22 = LocaleController.getString("ServiceNotifications", 2131560724);
                        var19 = var21;
                     }
                  }
               }
            } else {
               var22 = null;
               var19 = var21;
            }
         }

         var21 = var19;
         if (this.savedMessages) {
            this.nameTop = AndroidUtilities.dp(19.0F);
            var22 = null;
            var21 = var19;
         }
      }

      if (!TextUtils.isEmpty((CharSequence)var22)) {
         this.statusLayout = new StaticLayout(TextUtils.ellipsize((CharSequence)var22, var21, (float)(var3 - AndroidUtilities.dp(12.0F)), TruncateAt.END), var21, var3, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.nameTop = AndroidUtilities.dp(9.0F);
         this.nameLockTop -= AndroidUtilities.dp(10.0F);
      } else {
         this.statusLayout = null;
      }

      if (LocaleController.isRTL) {
         var2 = this.getMeasuredWidth() - AndroidUtilities.dp(57.0F) - this.getPaddingRight();
      } else {
         var2 = AndroidUtilities.dp(11.0F) + this.getPaddingLeft();
      }

      this.avatarImage.setImageCoords(var2, AndroidUtilities.dp(7.0F), AndroidUtilities.dp(46.0F), AndroidUtilities.dp(46.0F));
      double var8;
      double var10;
      double var12;
      StaticLayout var25;
      if (LocaleController.isRTL) {
         if (this.nameLayout.getLineCount() > 0 && this.nameLayout.getLineLeft(0) == 0.0F) {
            var8 = Math.ceil((double)this.nameLayout.getLineWidth(0));
            var2 = this.nameWidth;
            if (var8 < (double)var2) {
               var10 = (double)this.nameLeft;
               var12 = (double)var2;
               Double.isNaN(var12);
               Double.isNaN(var10);
               this.nameLeft = (int)(var10 + (var12 - var8));
            }
         }

         var25 = this.statusLayout;
         if (var25 != null && var25.getLineCount() > 0 && this.statusLayout.getLineLeft(0) == 0.0F) {
            var8 = Math.ceil((double)this.statusLayout.getLineWidth(0));
            var10 = (double)var3;
            if (var8 < var10) {
               var12 = (double)this.statusLeft;
               Double.isNaN(var10);
               Double.isNaN(var12);
               this.statusLeft = (int)(var12 + (var10 - var8));
            }
         }
      } else {
         if (this.nameLayout.getLineCount() > 0 && this.nameLayout.getLineRight(0) == (float)this.nameWidth) {
            var10 = Math.ceil((double)this.nameLayout.getLineWidth(0));
            var2 = this.nameWidth;
            if (var10 < (double)var2) {
               var12 = (double)this.nameLeft;
               var8 = (double)var2;
               Double.isNaN(var8);
               Double.isNaN(var12);
               this.nameLeft = (int)(var12 - (var8 - var10));
            }
         }

         var25 = this.statusLayout;
         if (var25 != null && var25.getLineCount() > 0 && this.statusLayout.getLineRight(0) == (float)var3) {
            var8 = Math.ceil((double)this.statusLayout.getLineWidth(0));
            var10 = (double)var3;
            if (var8 < var10) {
               var12 = (double)this.statusLeft;
               Double.isNaN(var10);
               Double.isNaN(var12);
               this.statusLeft = (int)(var12 - (var10 - var8));
            }
         }
      }

      this.nameLeft += this.getPaddingLeft();
      this.statusLeft += this.getPaddingLeft();
      this.nameLockLeft += this.getPaddingLeft();
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
      if (this.user != null || this.chat != null || this.encryptedChat != null) {
         if (this.useSeparator) {
            if (LocaleController.isRTL) {
               var1.drawLine(0.0F, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline)), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            } else {
               var1.drawLine((float)AndroidUtilities.dp((float)AndroidUtilities.leftBaseline), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
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

         int var2;
         float var3;
         if (this.nameLayout != null) {
            var1.save();
            var1.translate((float)this.nameLeft, (float)this.nameTop);
            this.nameLayout.draw(var1);
            var1.restore();
            if (this.drawCheck) {
               if (LocaleController.isRTL) {
                  if (this.nameLayout.getLineLeft(0) == 0.0F) {
                     var2 = this.nameLeft - AndroidUtilities.dp(6.0F) - Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                  } else {
                     var3 = this.nameLayout.getLineWidth(0);
                     double var4 = (double)(this.nameLeft + this.nameWidth);
                     double var6 = Math.ceil((double)var3);
                     Double.isNaN(var4);
                     double var8 = (double)AndroidUtilities.dp(6.0F);
                     Double.isNaN(var8);
                     double var10 = (double)Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                     Double.isNaN(var10);
                     var2 = (int)(var4 - var6 - var8 - var10);
                  }
               } else {
                  var2 = (int)((float)this.nameLeft + this.nameLayout.getLineRight(0) + (float)AndroidUtilities.dp(6.0F));
               }

               BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, var2, this.nameTop + AndroidUtilities.dp(3.0F));
               BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, var2, this.nameTop + AndroidUtilities.dp(3.0F));
               Theme.dialogs_verifiedDrawable.draw(var1);
               Theme.dialogs_verifiedCheckDrawable.draw(var1);
            }
         }

         if (this.statusLayout != null) {
            var1.save();
            var1.translate((float)(this.statusLeft + this.sublabelOffsetX), (float)(AndroidUtilities.dp(33.0F) + this.sublabelOffsetY));
            this.statusLayout.draw(var1);
            var1.restore();
         }

         if (this.countLayout != null) {
            var2 = this.countLeft - AndroidUtilities.dp(5.5F);
            this.rect.set((float)var2, (float)this.countTop, (float)(var2 + this.countWidth + AndroidUtilities.dp(11.0F)), (float)(this.countTop + AndroidUtilities.dp(23.0F)));
            RectF var12 = this.rect;
            var3 = AndroidUtilities.density;
            Paint var13;
            if (MessagesController.getInstance(this.currentAccount).isDialogMuted(this.dialog_id)) {
               var13 = Theme.dialogs_countGrayPaint;
            } else {
               var13 = Theme.dialogs_countPaint;
            }

            var1.drawRoundRect(var12, var3 * 11.5F, var3 * 11.5F, var13);
            var1.save();
            var1.translate((float)this.countLeft, (float)(this.countTop + AndroidUtilities.dp(4.0F)));
            this.countLayout.draw(var1);
            var1.restore();
         }

         this.avatarImage.draw(var1);
      }
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      StringBuilder var2 = new StringBuilder();
      StaticLayout var3 = this.nameLayout;
      if (var3 != null) {
         var2.append(var3.getText());
      }

      if (this.statusLayout != null) {
         if (var2.length() > 0) {
            var2.append(", ");
         }

         var2.append(this.statusLayout.getText());
      }

      var1.setText(var2.toString());
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.user != null || this.chat != null || this.encryptedChat != null) {
         if (var1) {
            this.buildLayout();
         }

      }
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(60.0F) + this.useSeparator);
   }

   public void setData(TLObject var1, TLRPC.EncryptedChat var2, CharSequence var3, CharSequence var4, boolean var5, boolean var6) {
      this.currentName = var3;
      if (var1 instanceof TLRPC.User) {
         this.user = (TLRPC.User)var1;
         this.chat = null;
      } else if (var1 instanceof TLRPC.Chat) {
         this.chat = (TLRPC.Chat)var1;
         this.user = null;
      }

      this.encryptedChat = var2;
      this.subLabel = var4;
      this.drawCount = var5;
      this.savedMessages = var6;
      this.update(0);
   }

   public void setException(NotificationsSettingsActivity.NotificationException var1, CharSequence var2) {
      boolean var3 = var1.hasCustom;
      int var4 = var1.notify;
      int var5 = var1.muteUntil;
      boolean var6 = false;
      String var7;
      int var13;
      if (var4 == 3 && var5 != Integer.MAX_VALUE) {
         var13 = var5 - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         if (var13 <= 0) {
            if (var3) {
               var7 = LocaleController.getString("NotificationsCustom", 2131560059);
            } else {
               var7 = LocaleController.getString("NotificationsUnmuted", 2131560094);
            }
         } else if (var13 < 3600) {
            var7 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Minutes", var13 / 60));
         } else if (var13 < 86400) {
            var7 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Hours", (int)Math.ceil((double)((float)var13 / 60.0F / 60.0F))));
         } else if (var13 < 31536000) {
            var7 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Days", (int)Math.ceil((double)((float)var13 / 60.0F / 60.0F / 24.0F))));
         } else {
            var7 = null;
         }
      } else {
         if (var4 == 0 || var4 == 1) {
            var6 = true;
         }

         if (var6 && var3) {
            var7 = LocaleController.getString("NotificationsCustom", 2131560059);
         } else if (var6) {
            var7 = LocaleController.getString("NotificationsUnmuted", 2131560094);
         } else {
            var7 = LocaleController.getString("NotificationsMuted", 2131560076);
         }
      }

      String var8 = var7;
      if (var7 == null) {
         var8 = LocaleController.getString("NotificationsOff", 2131560078);
      }

      long var9 = var1.did;
      var5 = (int)var9;
      var13 = (int)(var9 >> 32);
      TLRPC.User var11;
      if (var5 != 0) {
         if (var5 > 0) {
            var11 = MessagesController.getInstance(this.currentAccount).getUser(var5);
            if (var11 != null) {
               this.setData(var11, (TLRPC.EncryptedChat)null, var2, var8, false, false);
            }
         } else {
            TLRPC.Chat var12 = MessagesController.getInstance(this.currentAccount).getChat(-var5);
            if (var12 != null) {
               this.setData(var12, (TLRPC.EncryptedChat)null, var2, var8, false, false);
            }
         }
      } else {
         TLRPC.EncryptedChat var14 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(var13);
         if (var14 != null) {
            var11 = MessagesController.getInstance(this.currentAccount).getUser(var14.user_id);
            if (var11 != null) {
               this.setData(var11, var14, var2, var8, false, false);
            }
         }
      }

   }

   public void setSublabelOffset(int var1, int var2) {
      this.sublabelOffsetX = var1;
      this.sublabelOffsetY = var2;
   }

   public void update(int var1) {
      TLRPC.User var2 = this.user;
      TLRPC.FileLocation var3 = null;
      TLRPC.UserProfilePhoto var4 = null;
      TLRPC.Chat var5 = null;
      if (var2 != null) {
         this.avatarDrawable.setInfo(var2);
         if (this.savedMessages) {
            this.avatarDrawable.setAvatarType(1);
            this.avatarImage.setImage((ImageLocation)null, (String)null, this.avatarDrawable, (String)null, (Object)null, 0);
            var3 = var4;
         } else {
            var4 = this.user.photo;
            var3 = var5;
            if (var4 != null) {
               var3 = var4.photo_small;
            }

            this.avatarImage.setImage(ImageLocation.getForUser(this.user, false), "50_50", this.avatarDrawable, (String)null, this.user, 0);
         }
      } else {
         var5 = this.chat;
         if (var5 != null) {
            TLRPC.ChatPhoto var9 = var5.photo;
            if (var9 != null) {
               var3 = var9.photo_small;
            }

            this.avatarDrawable.setInfo(this.chat);
            this.avatarImage.setImage(ImageLocation.getForChat(this.chat, false), "50_50", this.avatarDrawable, (String)null, this.chat, 0);
         } else {
            this.avatarDrawable.setInfo(0, (String)null, (String)null, false);
            this.avatarImage.setImage((ImageLocation)null, (String)null, this.avatarDrawable, (String)null, (Object)null, 0);
            var3 = var4;
         }
      }

      TLRPC.User var11;
      TLRPC.UserStatus var12;
      StringBuilder var13;
      if (var1 != 0) {
         boolean var6;
         label137: {
            label154: {
               if ((var1 & 2) != 0 && this.user != null || (var1 & 8) != 0 && this.chat != null) {
                  if (this.lastAvatar != null && var3 == null || this.lastAvatar == null && var3 != null) {
                     break label154;
                  }

                  TLRPC.FileLocation var10 = this.lastAvatar;
                  if (var10 != null && var3 != null && (var10.volume_id != var3.volume_id || var10.local_id != var3.local_id)) {
                     break label154;
                  }
               }

               var6 = false;
               break label137;
            }

            var6 = true;
         }

         boolean var7 = var6;
         if (!var6) {
            var7 = var6;
            if ((var1 & 4) != 0) {
               var11 = this.user;
               var7 = var6;
               if (var11 != null) {
                  var12 = var11.status;
                  int var8;
                  if (var12 != null) {
                     var8 = var12.expires;
                  } else {
                     var8 = 0;
                  }

                  var7 = var6;
                  if (var8 != this.lastStatus) {
                     var7 = true;
                  }
               }
            }
         }

         label152: {
            if (var7 || (var1 & 1) == 0 || this.user == null) {
               var6 = var7;
               if ((var1 & 16) == 0) {
                  break label152;
               }

               var6 = var7;
               if (this.chat == null) {
                  break label152;
               }
            }

            String var14;
            if (this.user != null) {
               var13 = new StringBuilder();
               var13.append(this.user.first_name);
               var13.append(this.user.last_name);
               var14 = var13.toString();
            } else {
               var14 = this.chat.title;
            }

            var6 = var7;
            if (!var14.equals(this.lastName)) {
               var6 = true;
            }
         }

         var7 = var6;
         if (!var6) {
            var7 = var6;
            if (this.drawCount) {
               var7 = var6;
               if ((var1 & 256) != 0) {
                  TLRPC.Dialog var15 = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
                  var7 = var6;
                  if (var15 != null) {
                     var7 = var6;
                     if (var15.unread_count != this.lastUnreadCount) {
                        var7 = true;
                     }
                  }
               }
            }
         }

         if (!var7) {
            return;
         }
      }

      var11 = this.user;
      if (var11 != null) {
         var12 = var11.status;
         if (var12 != null) {
            this.lastStatus = var12.expires;
         } else {
            this.lastStatus = 0;
         }

         var13 = new StringBuilder();
         var13.append(this.user.first_name);
         var13.append(this.user.last_name);
         this.lastName = var13.toString();
      } else {
         var5 = this.chat;
         if (var5 != null) {
            this.lastName = var5.title;
         }
      }

      this.lastAvatar = var3;
      if (this.getMeasuredWidth() == 0 && this.getMeasuredHeight() == 0) {
         this.requestLayout();
      } else {
         this.buildLayout();
      }

      this.postInvalidate();
   }
}
