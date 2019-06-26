package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.TypefaceSpan;

public class DialogCell extends BaseCell {
   private boolean animatingArchiveAvatar;
   private float animatingArchiveAvatarProgress;
   private float archiveBackgroundProgress;
   private boolean archiveHidden;
   private boolean attachedToWindow;
   private AvatarDrawable avatarDrawable;
   private ImageReceiver avatarImage;
   private int bottomClip;
   private TLRPC.Chat chat;
   private CheckBox2 checkBox;
   private int checkDrawLeft;
   private int checkDrawTop;
   private boolean clearingDialog;
   private float clipProgress;
   private float cornerProgress;
   private StaticLayout countLayout;
   private int countLeft;
   private int countTop;
   private int countWidth;
   private int currentAccount;
   private int currentDialogFolderDialogsCount;
   private int currentDialogFolderId;
   private long currentDialogId;
   private int currentEditDate;
   private float currentRevealBounceProgress;
   private float currentRevealProgress;
   private DialogCell.CustomDialog customDialog;
   private boolean dialogMuted;
   private int dialogsType;
   private TLRPC.DraftMessage draftMessage;
   private boolean drawCheck1;
   private boolean drawCheck2;
   private boolean drawClock;
   private boolean drawCount;
   private boolean drawError;
   private boolean drawMention;
   private boolean drawNameBot;
   private boolean drawNameBroadcast;
   private boolean drawNameGroup;
   private boolean drawNameLock;
   private boolean drawPin;
   private boolean drawPinBackground;
   private boolean drawReorder;
   private boolean drawRevealBackground;
   private boolean drawScam;
   private boolean drawVerified;
   private TLRPC.EncryptedChat encryptedChat;
   private int errorLeft;
   private int errorTop;
   private int folderId;
   public boolean fullSeparator;
   public boolean fullSeparator2;
   private int halfCheckDrawLeft;
   private int index;
   private DialogCell.BounceInterpolator interpolator;
   private boolean isDialogCell;
   private boolean isSelected;
   private boolean isSliding;
   private int lastMessageDate;
   private CharSequence lastMessageString;
   private CharSequence lastPrintString;
   private int lastSendState;
   private boolean lastUnreadState;
   private long lastUpdateTime;
   private boolean markUnread;
   private int mentionCount;
   private StaticLayout mentionLayout;
   private int mentionLeft;
   private int mentionWidth;
   private MessageObject message;
   private int messageId;
   private StaticLayout messageLayout;
   private int messageLeft;
   private StaticLayout messageNameLayout;
   private int messageNameLeft;
   private int messageNameTop;
   private int messageTop;
   private StaticLayout nameLayout;
   private int nameLeft;
   private int nameLockLeft;
   private int nameLockTop;
   private int nameMuteLeft;
   private float onlineProgress;
   private int pinLeft;
   private int pinTop;
   private RectF rect;
   private float reorderIconProgress;
   private StaticLayout timeLayout;
   private int timeLeft;
   private int timeTop;
   private int topClip;
   private boolean translationAnimationStarted;
   private Drawable translationDrawable;
   private float translationX;
   private int unreadCount;
   public boolean useForceThreeLines;
   public boolean useSeparator;
   private TLRPC.User user;

   public DialogCell(Context var1, boolean var2, boolean var3) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.avatarImage = new ImageReceiver(this);
      this.avatarDrawable = new AvatarDrawable();
      this.interpolator = new DialogCell.BounceInterpolator();
      this.rect = new RectF();
      Theme.createDialogsResources(var1);
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(28.0F));
      this.useForceThreeLines = var3;
      if (var2) {
         this.checkBox = new CheckBox2(var1);
         this.checkBox.setColor((String)null, "windowBackgroundWhite", "checkboxCheck");
         this.checkBox.setSize(21);
         this.checkBox.setDrawUnchecked(false);
         this.checkBox.setDrawBackgroundAsArc(3);
         this.addView(this.checkBox);
      }

   }

   private void checkOnline() {
      boolean var2;
      label25: {
         TLRPC.User var1 = this.user;
         if (var1 != null && !var1.self) {
            TLRPC.UserStatus var4 = var1.status;
            if (var4 != null && var4.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(this.user.id)) {
               var2 = true;
               break label25;
            }
         }

         var2 = false;
      }

      float var3;
      if (var2) {
         var3 = 1.0F;
      } else {
         var3 = 0.0F;
      }

      this.onlineProgress = var3;
   }

   private MessageObject findFolderTopMessage() {
      int var1 = this.currentAccount;
      int var2 = this.dialogsType;
      int var3 = this.currentDialogFolderId;
      int var4 = 0;
      ArrayList var5 = DialogsActivity.getDialogsArray(var1, var2, var3, false);
      boolean var6 = var5.isEmpty();
      MessageObject var7 = null;
      MessageObject var8 = null;
      if (!var6) {
         var3 = var5.size();

         while(true) {
            var7 = var8;
            if (var4 >= var3) {
               break;
            }

            TLRPC.Dialog var9 = (TLRPC.Dialog)var5.get(var4);
            MessageObject var10 = (MessageObject)MessagesController.getInstance(this.currentAccount).dialogMessage.get(var9.id);
            var7 = var8;
            if (var10 != null) {
               label33: {
                  if (var8 != null) {
                     var7 = var8;
                     if (var10.messageOwner.date <= var8.messageOwner.date) {
                        break label33;
                     }
                  }

                  var7 = var10;
               }
            }

            if (var9.pinnedNum == 0) {
               break;
            }

            ++var4;
            var8 = var7;
         }
      }

      return var7;
   }

   private CharSequence formatArchivedDialogNames() {
      ArrayList var1 = MessagesController.getInstance(this.currentAccount).getDialogs(this.currentDialogFolderId);
      this.currentDialogFolderDialogsCount = var1.size();
      SpannableStringBuilder var2 = new SpannableStringBuilder();
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         TLRPC.Dialog var5 = (TLRPC.Dialog)var1.get(var4);
         boolean var6 = DialogObject.isSecretDialogId(var5.id);
         TLRPC.Chat var7 = null;
         int var9;
         TLRPC.User var11;
         if (var6) {
            TLRPC.EncryptedChat var8 = MessagesController.getInstance(this.currentAccount).getEncryptedChat((int)(var5.id >> 32));
            if (var8 != null) {
               var11 = MessagesController.getInstance(this.currentAccount).getUser(var8.user_id);
            } else {
               var11 = null;
            }
         } else {
            var9 = (int)var5.id;
            if (var9 > 0) {
               var11 = MessagesController.getInstance(this.currentAccount).getUser(var9);
            } else {
               var7 = MessagesController.getInstance(this.currentAccount).getChat(-var9);
               var11 = null;
            }
         }

         String var12;
         if (var7 != null) {
            var12 = var7.title.replace('\n', ' ');
         } else {
            if (var11 == null) {
               continue;
            }

            if (UserObject.isDeleted(var11)) {
               var12 = LocaleController.getString("HiddenName", 2131559636);
            } else {
               var12 = ContactsController.formatName(var11.first_name, var11.last_name).replace('\n', ' ');
            }
         }

         if (var2.length() > 0) {
            var2.append(", ");
         }

         var9 = var2.length();
         int var10 = var12.length();
         var2.append(var12);
         if (var5.unread_count > 0) {
            var2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor("chats_nameArchived")), var9, var10 + var9, 33);
         }

         if (var2.length() > 150) {
            break;
         }
      }

      return Emoji.replaceEmoji(var2, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0F), false);
   }

   public void animateArchiveAvatar() {
      if (this.avatarDrawable.getAvatarType() == 3) {
         this.animatingArchiveAvatar = true;
         this.animatingArchiveAvatarProgress = 0.0F;
         Theme.dialogs_archiveAvatarDrawable.setCallback(this);
         Theme.dialogs_archiveAvatarDrawable.setProgress(0.0F);
         Theme.dialogs_archiveAvatarDrawable.start();
         this.invalidate();
      }
   }

   public void buildLayout() {
      TextPaint var1;
      int var2;
      if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
         Theme.dialogs_namePaint.setTextSize((float)AndroidUtilities.dp(17.0F));
         Theme.dialogs_nameEncryptedPaint.setTextSize((float)AndroidUtilities.dp(17.0F));
         Theme.dialogs_messagePaint.setTextSize((float)AndroidUtilities.dp(16.0F));
         Theme.dialogs_messagePrintingPaint.setTextSize((float)AndroidUtilities.dp(16.0F));
         var1 = Theme.dialogs_messagePaint;
         var2 = Theme.getColor("chats_message");
         var1.linkColor = var2;
         var1.setColor(var2);
      } else {
         Theme.dialogs_namePaint.setTextSize((float)AndroidUtilities.dp(16.0F));
         Theme.dialogs_nameEncryptedPaint.setTextSize((float)AndroidUtilities.dp(16.0F));
         Theme.dialogs_messagePaint.setTextSize((float)AndroidUtilities.dp(15.0F));
         Theme.dialogs_messagePrintingPaint.setTextSize((float)AndroidUtilities.dp(15.0F));
         var1 = Theme.dialogs_messagePaint;
         var2 = Theme.getColor("chats_message_threeLines");
         var1.linkColor = var2;
         var1.setColor(var2);
      }

      this.currentDialogFolderDialogsCount = 0;
      Object var3;
      if (this.isDialogCell) {
         var3 = (CharSequence)MessagesController.getInstance(this.currentAccount).printingStrings.get(this.currentDialogId);
      } else {
         var3 = null;
      }

      boolean var4;
      boolean var5;
      String var6;
      boolean var7;
      boolean var43;
      label1260: {
         label1350: {
            var1 = Theme.dialogs_messagePaint;
            this.drawNameGroup = false;
            this.drawNameBroadcast = false;
            this.drawNameLock = false;
            this.drawNameBot = false;
            this.drawVerified = false;
            this.drawScam = false;
            this.drawPinBackground = false;
            var4 = UserObject.isUserSelf(this.user);
            var5 = true;
            var43 = var4 ^ true;
            if (VERSION.SDK_INT >= 18) {
               if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout || this.currentDialogFolderId != 0) {
                  var6 = "%2$s: \u2068%1$s\u2069";
                  break label1350;
               }

               var6 = "\u2068%s\u2069";
            } else {
               if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout || this.currentDialogFolderId != 0) {
                  var6 = "%2$s: %1$s";
                  break label1350;
               }

               var6 = "%1$s";
            }

            var7 = false;
            break label1260;
         }

         var7 = true;
      }

      MessageObject var8 = this.message;
      CharSequence var52;
      if (var8 != null) {
         var52 = var8.messageText;
      } else {
         var52 = null;
      }

      this.lastMessageString = var52;
      DialogCell.CustomDialog var54 = this.customDialog;
      String var9;
      String var10;
      String var11;
      String var12;
      boolean var13;
      int var14;
      Exception var10000;
      boolean var10001;
      Object var44;
      int var48;
      int var50;
      SpannableStringBuilder var51;
      String var53;
      Object var56;
      String var57;
      TextPaint var58;
      Exception var73;
      int var75;
      Object var91;
      if (var54 != null) {
         var2 = var54.type;
         if (var2 == 2) {
            this.drawNameLock = true;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               this.nameLockTop = AndroidUtilities.dp(16.5F);
               if (!LocaleController.isRTL) {
                  this.nameLockLeft = AndroidUtilities.dp(76.0F);
                  this.nameLeft = AndroidUtilities.dp(80.0F) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
               } else {
                  this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(76.0F) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                  this.nameLeft = AndroidUtilities.dp(18.0F);
               }
            } else {
               this.nameLockTop = AndroidUtilities.dp(12.5F);
               if (!LocaleController.isRTL) {
                  this.nameLockLeft = AndroidUtilities.dp(78.0F);
                  this.nameLeft = AndroidUtilities.dp(82.0F) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
               } else {
                  this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(78.0F) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                  this.nameLeft = AndroidUtilities.dp(22.0F);
               }
            }
         } else {
            this.drawVerified = var54.verified;
            if (SharedConfig.drawDialogIcons && var2 == 1) {
               this.drawNameGroup = true;
               Drawable var45;
               if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                  if (!LocaleController.isRTL) {
                     this.nameLockTop = AndroidUtilities.dp(17.5F);
                     this.nameLockLeft = AndroidUtilities.dp(76.0F);
                     var2 = AndroidUtilities.dp(80.0F);
                     if (this.drawNameGroup) {
                        var45 = Theme.dialogs_groupDrawable;
                     } else {
                        var45 = Theme.dialogs_broadcastDrawable;
                     }

                     this.nameLeft = var2 + var45.getIntrinsicWidth();
                  } else {
                     var50 = this.getMeasuredWidth();
                     var2 = AndroidUtilities.dp(76.0F);
                     if (this.drawNameGroup) {
                        var45 = Theme.dialogs_groupDrawable;
                     } else {
                        var45 = Theme.dialogs_broadcastDrawable;
                     }

                     this.nameLockLeft = var50 - var2 - var45.getIntrinsicWidth();
                     this.nameLeft = AndroidUtilities.dp(18.0F);
                  }
               } else {
                  this.nameLockTop = AndroidUtilities.dp(13.5F);
                  if (!LocaleController.isRTL) {
                     this.nameLockLeft = AndroidUtilities.dp(78.0F);
                     var2 = AndroidUtilities.dp(82.0F);
                     if (this.drawNameGroup) {
                        var45 = Theme.dialogs_groupDrawable;
                     } else {
                        var45 = Theme.dialogs_broadcastDrawable;
                     }

                     this.nameLeft = var2 + var45.getIntrinsicWidth();
                  } else {
                     var50 = this.getMeasuredWidth();
                     var2 = AndroidUtilities.dp(78.0F);
                     if (this.drawNameGroup) {
                        var45 = Theme.dialogs_groupDrawable;
                     } else {
                        var45 = Theme.dialogs_broadcastDrawable;
                     }

                     this.nameLockLeft = var50 - var2 - var45.getIntrinsicWidth();
                     this.nameLeft = AndroidUtilities.dp(22.0F);
                  }
               }
            } else if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               if (!LocaleController.isRTL) {
                  this.nameLeft = AndroidUtilities.dp(76.0F);
               } else {
                  this.nameLeft = AndroidUtilities.dp(18.0F);
               }
            } else if (!LocaleController.isRTL) {
               this.nameLeft = AndroidUtilities.dp(78.0F);
            } else {
               this.nameLeft = AndroidUtilities.dp(22.0F);
            }
         }

         DialogCell.CustomDialog var49 = this.customDialog;
         if (var49.type != 1) {
            var56 = var49.message;
            if (var49.isMedia) {
               var1 = Theme.dialogs_messagePrintingPaint;
            }

            var43 = true;
            var8 = null;
            var58 = var1;
            var44 = var8;
         } else {
            var57 = LocaleController.getString("FromYou", 2131559584);
            var49 = this.customDialog;
            if (var49.isMedia) {
               var1 = Theme.dialogs_messagePrintingPaint;
               var51 = SpannableStringBuilder.valueOf(String.format(var6, this.message.messageText));
               var51.setSpan(new ForegroundColorSpan(Theme.getColor("chats_attachMessage")), 0, var51.length(), 33);
            } else {
               var9 = var49.message;
               var53 = var9;
               if (var9.length() > 150) {
                  var53 = var9.substring(0, 150);
               }

               if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                  var51 = SpannableStringBuilder.valueOf(String.format(var6, var53.replace('\n', ' '), var57));
               } else {
                  var51 = SpannableStringBuilder.valueOf(String.format(var6, var53, var57));
               }
            }

            var56 = Emoji.replaceEmoji(var51, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
            var58 = var1;
            var44 = var57;
            var43 = false;
         }

         var9 = LocaleController.stringForMessageListDate((long)this.customDialog.date);
         var50 = this.customDialog.unread_count;
         if (var50 != 0) {
            this.drawCount = true;
            var57 = String.format("%d", var50);
         } else {
            this.drawCount = false;
            var57 = null;
         }

         if (this.customDialog.sent) {
            this.drawCheck1 = true;
            this.drawCheck2 = true;
            this.drawClock = false;
            this.drawError = false;
         } else {
            this.drawCheck1 = false;
            this.drawCheck2 = false;
            this.drawClock = false;
            this.drawError = false;
         }

         var10 = this.customDialog.name;
         var11 = null;
         var12 = var9;
         var13 = var43;
      } else {
         if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
            if (!LocaleController.isRTL) {
               this.nameLeft = AndroidUtilities.dp(76.0F);
            } else {
               this.nameLeft = AndroidUtilities.dp(18.0F);
            }
         } else if (!LocaleController.isRTL) {
            this.nameLeft = AndroidUtilities.dp(78.0F);
         } else {
            this.nameLeft = AndroidUtilities.dp(22.0F);
         }

         TLRPC.Chat var65;
         if (this.encryptedChat != null) {
            if (this.currentDialogFolderId == 0) {
               this.drawNameLock = true;
               if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                  this.nameLockTop = AndroidUtilities.dp(16.5F);
                  if (!LocaleController.isRTL) {
                     this.nameLockLeft = AndroidUtilities.dp(76.0F);
                     this.nameLeft = AndroidUtilities.dp(80.0F) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                  } else {
                     this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(76.0F) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                     this.nameLeft = AndroidUtilities.dp(18.0F);
                  }
               } else {
                  this.nameLockTop = AndroidUtilities.dp(12.5F);
                  if (!LocaleController.isRTL) {
                     this.nameLockLeft = AndroidUtilities.dp(78.0F);
                     this.nameLeft = AndroidUtilities.dp(82.0F) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                  } else {
                     this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(78.0F) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                     this.nameLeft = AndroidUtilities.dp(22.0F);
                  }
               }
            }
         } else if (this.currentDialogFolderId == 0) {
            var65 = this.chat;
            if (var65 != null) {
               if (var65.scam) {
                  this.drawScam = true;
                  Theme.dialogs_scamDrawable.checkText();
               } else {
                  this.drawVerified = var65.verified;
               }

               if (SharedConfig.drawDialogIcons) {
                  Drawable var70;
                  if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                     var65 = this.chat;
                     if (var65.id < 0 || ChatObject.isChannel(var65) && !this.chat.megagroup) {
                        this.drawNameBroadcast = true;
                        this.nameLockTop = AndroidUtilities.dp(16.5F);
                     } else {
                        this.drawNameGroup = true;
                        this.nameLockTop = AndroidUtilities.dp(17.5F);
                     }

                     if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(76.0F);
                        var48 = AndroidUtilities.dp(80.0F);
                        if (this.drawNameGroup) {
                           var70 = Theme.dialogs_groupDrawable;
                        } else {
                           var70 = Theme.dialogs_broadcastDrawable;
                        }

                        this.nameLeft = var48 + var70.getIntrinsicWidth();
                     } else {
                        var75 = this.getMeasuredWidth();
                        var48 = AndroidUtilities.dp(76.0F);
                        if (this.drawNameGroup) {
                           var70 = Theme.dialogs_groupDrawable;
                        } else {
                           var70 = Theme.dialogs_broadcastDrawable;
                        }

                        this.nameLockLeft = var75 - var48 - var70.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(18.0F);
                     }
                  } else {
                     var65 = this.chat;
                     if (var65.id < 0 || ChatObject.isChannel(var65) && !this.chat.megagroup) {
                        this.drawNameBroadcast = true;
                        this.nameLockTop = AndroidUtilities.dp(12.5F);
                     } else {
                        this.drawNameGroup = true;
                        this.nameLockTop = AndroidUtilities.dp(13.5F);
                     }

                     if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(78.0F);
                        var48 = AndroidUtilities.dp(82.0F);
                        if (this.drawNameGroup) {
                           var70 = Theme.dialogs_groupDrawable;
                        } else {
                           var70 = Theme.dialogs_broadcastDrawable;
                        }

                        this.nameLeft = var48 + var70.getIntrinsicWidth();
                     } else {
                        var48 = this.getMeasuredWidth();
                        var75 = AndroidUtilities.dp(78.0F);
                        if (this.drawNameGroup) {
                           var70 = Theme.dialogs_groupDrawable;
                        } else {
                           var70 = Theme.dialogs_broadcastDrawable;
                        }

                        this.nameLockLeft = var48 - var75 - var70.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(22.0F);
                     }
                  }
               }
            } else {
               TLRPC.User var67 = this.user;
               if (var67 != null) {
                  if (var67.scam) {
                     this.drawScam = true;
                     Theme.dialogs_scamDrawable.checkText();
                  } else {
                     this.drawVerified = var67.verified;
                  }

                  if (SharedConfig.drawDialogIcons && this.user.bot) {
                     this.drawNameBot = true;
                     if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                        this.nameLockTop = AndroidUtilities.dp(16.5F);
                        if (!LocaleController.isRTL) {
                           this.nameLockLeft = AndroidUtilities.dp(76.0F);
                           this.nameLeft = AndroidUtilities.dp(80.0F) + Theme.dialogs_botDrawable.getIntrinsicWidth();
                        } else {
                           this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(76.0F) - Theme.dialogs_botDrawable.getIntrinsicWidth();
                           this.nameLeft = AndroidUtilities.dp(18.0F);
                        }
                     } else {
                        this.nameLockTop = AndroidUtilities.dp(12.5F);
                        if (!LocaleController.isRTL) {
                           this.nameLockLeft = AndroidUtilities.dp(78.0F);
                           this.nameLeft = AndroidUtilities.dp(82.0F) + Theme.dialogs_botDrawable.getIntrinsicWidth();
                        } else {
                           this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(78.0F) - Theme.dialogs_botDrawable.getIntrinsicWidth();
                           this.nameLeft = AndroidUtilities.dp(22.0F);
                        }
                     }
                  }
               }
            }
         }

         var75 = this.lastMessageDate;
         var48 = var75;
         if (var75 == 0) {
            var8 = this.message;
            var48 = var75;
            if (var8 != null) {
               var48 = var8.messageOwner.date;
            }
         }

         if (!this.isDialogCell) {
            this.draftMessage = null;
         } else {
            label1384: {
               this.draftMessage = DataQuery.getInstance(this.currentAccount).getDraft(this.currentDialogId);
               TLRPC.DraftMessage var84 = this.draftMessage;
               if (var84 == null || (!TextUtils.isEmpty(var84.message) || this.draftMessage.reply_to_msg_id != 0) && (var48 <= this.draftMessage.date || this.unreadCount == 0)) {
                  label1379: {
                     if (ChatObject.isChannel(this.chat)) {
                        var65 = this.chat;
                        if (!var65.megagroup && !var65.creator) {
                           TLRPC.TL_chatAdminRights var88 = var65.admin_rights;
                           if (var88 == null || !var88.post_messages) {
                              break label1379;
                           }
                        }
                     }

                     var65 = this.chat;
                     if (var65 == null || !var65.left && !var65.kicked) {
                        break label1384;
                     }
                  }
               }

               this.draftMessage = null;
            }
         }

         String var46;
         TLRPC.User var47;
         MessageObject var60;
         TextPaint var89;
         label1159: {
            label1353: {
               var9 = var6;
               if (var3 != null) {
                  this.lastPrintString = (CharSequence)var3;
                  var89 = Theme.dialogs_messagePrintingPaint;
                  var1 = null;
               } else {
                  this.lastPrintString = null;
                  if (this.draftMessage != null) {
                     var3 = LocaleController.getString("Draft", 2131559300);
                     if (TextUtils.isEmpty(this.draftMessage.message)) {
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                           var56 = SpannableStringBuilder.valueOf((CharSequence)var3);
                           ((SpannableStringBuilder)var56).setSpan(new ForegroundColorSpan(Theme.getColor("chats_draft")), 0, ((CharSequence)var3).length(), 33);
                        } else {
                           var56 = "";
                        }
                     } else {
                        var57 = this.draftMessage.message;
                        var6 = var57;
                        if (var57.length() > 150) {
                           var6 = var57.substring(0, 150);
                        }

                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                           var51 = SpannableStringBuilder.valueOf(String.format(var9, var6.replace('\n', ' '), var3));
                           var51.setSpan(new ForegroundColorSpan(Theme.getColor("chats_draft")), 0, ((CharSequence)var3).length() + 1, 33);
                        } else {
                           var51 = SpannableStringBuilder.valueOf(String.format(var9, var6.replace('\n', ' '), var3));
                        }

                        var56 = Emoji.replaceEmoji(var51, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
                     }

                     var7 = false;
                     var5 = true;
                     var89 = var1;
                     break label1159;
                  }

                  TextPaint var62;
                  if (this.clearingDialog) {
                     var62 = Theme.dialogs_messagePrintingPaint;
                     var46 = LocaleController.getString("HistoryCleared", 2131559639);
                  } else {
                     MessageObject var66 = this.message;
                     if (var66 != null) {
                        TLRPC.Chat var69;
                        TLRPC.User var71;
                        if (var66.isFromUser()) {
                           var71 = MessagesController.getInstance(this.currentAccount).getUser(this.message.messageOwner.from_id);
                           var69 = null;
                        } else {
                           var69 = MessagesController.getInstance(this.currentAccount).getChat(this.message.messageOwner.to_id.channel_id);
                           var71 = null;
                        }

                        label1374: {
                           if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                              var56 = LocaleController.getString("SavedMessagesInfo", 2131560634);
                              var7 = false;
                              var5 = true;
                              var43 = false;
                           } else if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                              var56 = this.formatArchivedDialogNames();
                              var7 = true;
                              var5 = false;
                           } else {
                              MessageObject var61 = this.message;
                              if (!(var61.messageOwner instanceof TLRPC.TL_messageService)) {
                                 var65 = this.chat;
                                 if (var65 != null && var65.id > 0 && var69 == null) {
                                    if (var61.isOutOwner()) {
                                       var6 = LocaleController.getString("FromYou", 2131559584);
                                    } else if (var71 != null) {
                                       if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                          var6 = UserObject.getFirstName(var71).replace("\n", "");
                                       } else if (UserObject.isDeleted(var71)) {
                                          var6 = LocaleController.getString("HiddenName", 2131559636);
                                       } else {
                                          var6 = ContactsController.formatName(var71.first_name, var71.last_name).replace("\n", "");
                                       }
                                    } else if (var69 != null) {
                                       var6 = var69.title.replace("\n", "");
                                    } else {
                                       var6 = "DELETED";
                                    }

                                    var8 = this.message;
                                    CharSequence var81 = var8.caption;
                                    SpannableStringBuilder var87;
                                    if (var81 != null) {
                                       var53 = var81.toString();
                                       var57 = var53;
                                       if (var53.length() > 150) {
                                          var57 = var53.substring(0, 150);
                                       }

                                       if (this.message.isVideo()) {
                                          var53 = "\ud83d\udcf9 ";
                                       } else if (this.message.isVoice()) {
                                          var53 = "\ud83c\udfa4 ";
                                       } else if (this.message.isMusic()) {
                                          var53 = "\ud83c\udfa7 ";
                                       } else if (this.message.isPhoto()) {
                                          var53 = "\ud83d\uddbc ";
                                       } else {
                                          var53 = "\ud83d\udcce ";
                                       }

                                       StringBuilder var64 = new StringBuilder();
                                       var64.append(var53);
                                       var64.append(var57.replace('\n', ' '));
                                       var87 = SpannableStringBuilder.valueOf(String.format(var9, var64.toString(), var6));
                                    } else if (var8.messageOwner.media != null && !var8.isMediaEmpty()) {
                                       label1291: {
                                          var58 = Theme.dialogs_messagePrintingPaint;
                                          var8 = this.message;
                                          TLRPC.MessageMedia var59 = var8.messageOwner.media;
                                          if (var59 instanceof TLRPC.TL_messageMediaGame) {
                                             if (VERSION.SDK_INT >= 18) {
                                                var44 = String.format("\ud83c\udfae \u2068%s\u2069", var59.game.title);
                                             } else {
                                                var44 = String.format("\ud83c\udfae %s", var59.game.title);
                                             }
                                          } else if (var8.type == 14) {
                                             if (VERSION.SDK_INT >= 18) {
                                                var44 = String.format("\ud83c\udfa7 \u2068%s - %s\u2069", var8.getMusicAuthor(), this.message.getMusicTitle());
                                             } else {
                                                var44 = String.format("\ud83c\udfa7 %s - %s", var8.getMusicAuthor(), this.message.getMusicTitle());
                                             }
                                          } else {
                                             var44 = var8.messageText;
                                          }

                                          SpannableStringBuilder var92 = SpannableStringBuilder.valueOf(String.format(var9, var44, var6));

                                          label1077: {
                                             label1292: {
                                                ForegroundColorSpan var72;
                                                try {
                                                   var72 = new ForegroundColorSpan(Theme.getColor("chats_attachMessage"));
                                                } catch (Exception var42) {
                                                   var10000 = var42;
                                                   var10001 = false;
                                                   break label1292;
                                                }

                                                if (var7) {
                                                   try {
                                                      var50 = var6.length() + 2;
                                                   } catch (Exception var41) {
                                                      var10000 = var41;
                                                      var10001 = false;
                                                      break label1292;
                                                   }
                                                } else {
                                                   var50 = 0;
                                                }

                                                try {
                                                   var92.setSpan(var72, var50, var92.length(), 33);
                                                   break label1077;
                                                } catch (Exception var40) {
                                                   var10000 = var40;
                                                   var10001 = false;
                                                }
                                             }

                                             var73 = var10000;
                                             FileLog.e((Throwable)var73);
                                             var1 = var58;
                                             var87 = var92;
                                             break label1291;
                                          }

                                          var1 = var58;
                                          var87 = var92;
                                       }
                                    } else {
                                       var57 = this.message.messageOwner.message;
                                       if (var57 != null) {
                                          var53 = var57;
                                          if (var57.length() > 150) {
                                             var53 = var57.substring(0, 150);
                                          }

                                          var87 = SpannableStringBuilder.valueOf(String.format(var9, var53.replace('\n', ' '), var6));
                                       } else {
                                          var87 = SpannableStringBuilder.valueOf("");
                                       }
                                    }

                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout || this.currentDialogFolderId != 0 && var87.length() > 0) {
                                       try {
                                          ForegroundColorSpan var93 = new ForegroundColorSpan(Theme.getColor("chats_nameMessage"));
                                          var87.setSpan(var93, 0, var6.length() + 1, 33);
                                       } catch (Exception var31) {
                                          FileLog.e((Throwable)var31);
                                       }
                                    }

                                    var52 = Emoji.replaceEmoji(var87, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(20.0F), false);
                                    var3 = var6;
                                    var5 = false;
                                    var56 = var52;
                                    var7 = true;
                                    break label1374;
                                 }

                                 TLRPC.MessageMedia var76 = this.message.messageOwner.media;
                                 if (var76 instanceof TLRPC.TL_messageMediaPhoto && var76.photo instanceof TLRPC.TL_photoEmpty && var76.ttl_seconds != 0) {
                                    var56 = LocaleController.getString("AttachPhotoExpired", 2131558728);
                                    var58 = var1;
                                    var13 = var43;
                                 } else {
                                    var76 = this.message.messageOwner.media;
                                    if (var76 instanceof TLRPC.TL_messageMediaDocument && var76.document instanceof TLRPC.TL_documentEmpty && var76.ttl_seconds != 0) {
                                       var56 = LocaleController.getString("AttachVideoExpired", 2131558734);
                                       var58 = var1;
                                       var13 = var43;
                                    } else {
                                       var66 = this.message;
                                       if (var66.caption != null) {
                                          if (var66.isVideo()) {
                                             var6 = "\ud83d\udcf9 ";
                                          } else if (this.message.isVoice()) {
                                             var6 = "\ud83c\udfa4 ";
                                          } else if (this.message.isMusic()) {
                                             var6 = "\ud83c\udfa7 ";
                                          } else if (this.message.isPhoto()) {
                                             var6 = "\ud83d\uddbc ";
                                          } else {
                                             var6 = "\ud83d\udcce ";
                                          }

                                          StringBuilder var74 = new StringBuilder();
                                          var74.append(var6);
                                          var74.append(this.message.caption);
                                          var56 = var74.toString();
                                          var58 = var1;
                                          var13 = var43;
                                       } else {
                                          if (var66.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                             StringBuilder var85 = new StringBuilder();
                                             var85.append("\ud83c\udfae ");
                                             var85.append(this.message.messageOwner.media.game.title);
                                             var91 = var85.toString();
                                          } else if (var66.type == 14) {
                                             var91 = String.format("\ud83c\udfa7 %s - %s", var66.getMusicAuthor(), this.message.getMusicTitle());
                                          } else {
                                             var91 = var66.messageText;
                                          }

                                          var60 = this.message;
                                          var56 = var91;
                                          var58 = var1;
                                          var13 = var43;
                                          if (var60.messageOwner.media != null) {
                                             var56 = var91;
                                             var58 = var1;
                                             var13 = var43;
                                             if (!var60.isMediaEmpty()) {
                                                var58 = Theme.dialogs_messagePrintingPaint;
                                                var56 = var91;
                                                var13 = var43;
                                             }
                                          }
                                       }
                                    }
                                 }
                              } else {
                                 label1115: {
                                    if (ChatObject.isChannel(this.chat)) {
                                       TLRPC.MessageAction var55 = this.message.messageOwner.action;
                                       if (var55 instanceof TLRPC.TL_messageActionHistoryClear || var55 instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                          var56 = "";
                                          var43 = false;
                                          break label1115;
                                       }
                                    }

                                    var56 = this.message.messageText;
                                 }

                                 var58 = Theme.dialogs_messagePrintingPaint;
                                 var13 = var43;
                              }

                              var1 = var58;
                              var7 = true;
                              var5 = true;
                              var43 = var13;
                           }

                           var3 = null;
                        }

                        if (this.currentDialogFolderId != 0) {
                           var3 = this.formatArchivedDialogNames();
                        }

                        var13 = var7;
                        var7 = var5;
                        var89 = var1;
                        var5 = var13;
                        break label1159;
                     }

                     TLRPC.EncryptedChat var63 = this.encryptedChat;
                     if (var63 == null) {
                        break label1353;
                     }

                     var62 = Theme.dialogs_messagePrintingPaint;
                     if (var63 instanceof TLRPC.TL_encryptedChatRequested) {
                        var46 = LocaleController.getString("EncryptionProcessing", 2131559363);
                     } else if (var63 instanceof TLRPC.TL_encryptedChatWaiting) {
                        label1149: {
                           var47 = this.user;
                           if (var47 != null) {
                              var46 = var47.first_name;
                              if (var46 != null) {
                                 var46 = LocaleController.formatString("AwaitingEncryption", 2131558808, var46);
                                 break label1149;
                              }
                           }

                           var46 = LocaleController.formatString("AwaitingEncryption", 2131558808, "");
                        }
                     } else if (var63 instanceof TLRPC.TL_encryptedChatDiscarded) {
                        var46 = LocaleController.getString("EncryptionRejected", 2131559364);
                     } else {
                        var1 = var62;
                        if (!(var63 instanceof TLRPC.TL_encryptedChat)) {
                           break label1353;
                        }

                        if (var63.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                           label1142: {
                              var47 = this.user;
                              if (var47 != null) {
                                 var46 = var47.first_name;
                                 if (var46 != null) {
                                    var46 = LocaleController.formatString("EncryptedChatStartedOutgoing", 2131559352, var46);
                                    break label1142;
                                 }
                              }

                              var46 = LocaleController.formatString("EncryptedChatStartedOutgoing", 2131559352, "");
                           }
                        } else {
                           var46 = LocaleController.getString("EncryptedChatStartedIncoming", 2131559351);
                        }
                     }
                  }

                  var8 = null;
                  var3 = var46;
                  var1 = var8;
                  var89 = var62;
               }

               var5 = true;
               var56 = var3;
               var7 = true;
               var3 = var1;
               break label1159;
            }

            var56 = "";
            var7 = true;
            var5 = true;
            var3 = null;
            var89 = var1;
         }

         TLRPC.DraftMessage var78 = this.draftMessage;
         if (var78 != null) {
            var46 = LocaleController.stringForMessageListDate((long)var78.date);
         } else {
            var75 = this.lastMessageDate;
            if (var75 != 0) {
               var46 = LocaleController.stringForMessageListDate((long)var75);
            } else {
               MessageObject var80 = this.message;
               if (var80 != null) {
                  var46 = LocaleController.stringForMessageListDate((long)var80.messageOwner.date);
               } else {
                  var46 = "";
               }
            }
         }

         var60 = this.message;
         if (var60 == null) {
            this.drawCheck1 = false;
            this.drawCheck2 = false;
            this.drawClock = false;
            this.drawCount = false;
            this.drawMention = false;
            this.drawError = false;
            var12 = null;
            var11 = null;
         } else {
            label1337: {
               label1035: {
                  if (this.currentDialogFolderId != 0) {
                     var75 = this.unreadCount;
                     var14 = this.mentionCount;
                     if (var75 + var14 > 0) {
                        if (var75 <= var14) {
                           this.drawCount = false;
                           this.drawMention = true;
                           var10 = String.format("%d", var75 + var14);
                           var9 = null;
                           break label1035;
                        }

                        this.drawCount = true;
                        this.drawMention = false;
                        var9 = String.format("%d", var75 + var14);
                     } else {
                        this.drawCount = false;
                        this.drawMention = false;
                        var9 = null;
                     }
                  } else {
                     label1356: {
                        if (this.clearingDialog) {
                           this.drawCount = false;
                           var43 = false;
                        } else {
                           var75 = this.unreadCount;
                           if (var75 != 0 && (var75 != 1 || var75 != this.mentionCount || var60 == null || !var60.messageOwner.mentioned)) {
                              this.drawCount = true;
                              var9 = String.format("%d", this.unreadCount);
                              break label1356;
                           }

                           if (this.markUnread) {
                              this.drawCount = true;
                              var9 = "";
                              break label1356;
                           }

                           this.drawCount = false;
                        }

                        var9 = null;
                     }

                     if (this.mentionCount != 0) {
                        this.drawMention = true;
                        var10 = "@";
                        break label1035;
                     }

                     this.drawMention = false;
                  }

                  var10 = null;
               }

               if (this.message.isOut() && this.draftMessage == null && var43) {
                  MessageObject var68 = this.message;
                  if (!(var68.messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                     if (var68.isSending()) {
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawClock = true;
                        this.drawError = false;
                        var12 = var9;
                        var11 = var10;
                        break label1337;
                     }

                     if (this.message.isSendError()) {
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawClock = false;
                        this.drawError = true;
                        this.drawCount = false;
                        this.drawMention = false;
                        var12 = var9;
                        var11 = var10;
                        break label1337;
                     }

                     var12 = var9;
                     var11 = var10;
                     if (!this.message.isSent()) {
                        break label1337;
                     }

                     if (!this.message.isUnread() || ChatObject.isChannel(this.chat) && !this.chat.megagroup) {
                        var4 = true;
                     } else {
                        var4 = false;
                     }

                     this.drawCheck1 = var4;
                     this.drawCheck2 = true;
                     this.drawClock = false;
                     this.drawError = false;
                     var12 = var9;
                     var11 = var10;
                     break label1337;
                  }
               }

               this.drawCheck1 = false;
               this.drawCheck2 = false;
               this.drawClock = false;
               this.drawError = false;
               var11 = var10;
               var12 = var9;
            }
         }

         if (this.dialogsType == 0 && MessagesController.getInstance(this.currentAccount).isProxyDialog(this.currentDialogId, true)) {
            this.drawPinBackground = true;
            var10 = LocaleController.getString("UseProxySponsor", 2131560980);
         } else {
            var10 = var46;
         }

         if (this.currentDialogFolderId != 0) {
            var9 = LocaleController.getString("ArchivedChats", 2131558653);
         } else {
            TLRPC.Chat var86 = this.chat;
            if (var86 != null) {
               var46 = var86.title;
            } else {
               var47 = this.user;
               if (var47 != null) {
                  if (UserObject.isUserSelf(var47)) {
                     if (this.dialogsType == 3) {
                        this.drawPinBackground = true;
                     }

                     var46 = LocaleController.getString("SavedMessages", 2131560633);
                  } else {
                     var2 = this.user.id;
                     if (var2 / 1000 != 777 && var2 / 1000 != 333 && ContactsController.getInstance(this.currentAccount).contactsDict.get(this.user.id) == null) {
                        if (ContactsController.getInstance(this.currentAccount).contactsDict.size() == 0 && (!ContactsController.getInstance(this.currentAccount).contactsLoaded || ContactsController.getInstance(this.currentAccount).isLoadingContacts())) {
                           var46 = UserObject.getUserName(this.user);
                        } else {
                           var46 = this.user.phone;
                           if (var46 != null && var46.length() != 0) {
                              PhoneFormat var90 = PhoneFormat.getInstance();
                              StringBuilder var77 = new StringBuilder();
                              var77.append("+");
                              var77.append(this.user.phone);
                              var46 = var90.format(var77.toString());
                           } else {
                              var46 = UserObject.getUserName(this.user);
                           }
                        }
                     } else {
                        var46 = UserObject.getUserName(this.user);
                     }
                  }
               } else {
                  var46 = "";
               }
            }

            var9 = var46;
            if (var46.length() == 0) {
               var9 = LocaleController.getString("HiddenName", 2131559636);
            }
         }

         var44 = var3;
         var13 = var7;
         var57 = var12;
         var12 = var10;
         var10 = var9;
         var58 = var89;
      }

      if (var5) {
         var14 = (int)Math.ceil((double)Theme.dialogs_timePaint.measureText(var12));
         this.timeLayout = new StaticLayout(var12, Theme.dialogs_timePaint, var14, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         if (!LocaleController.isRTL) {
            this.timeLeft = this.getMeasuredWidth() - AndroidUtilities.dp(15.0F) - var14;
         } else {
            this.timeLeft = AndroidUtilities.dp(15.0F);
         }
      } else {
         this.timeLayout = null;
         this.timeLeft = 0;
         var14 = 0;
      }

      if (!LocaleController.isRTL) {
         var48 = this.getMeasuredWidth() - this.nameLeft - AndroidUtilities.dp(14.0F) - var14;
      } else {
         var48 = this.getMeasuredWidth() - this.nameLeft - AndroidUtilities.dp(77.0F) - var14;
         this.nameLeft += var14;
      }

      label928: {
         if (this.drawNameLock) {
            var2 = AndroidUtilities.dp(4.0F);
            var50 = Theme.dialogs_lockDrawable.getIntrinsicWidth();
         } else if (this.drawNameGroup) {
            var2 = AndroidUtilities.dp(4.0F);
            var50 = Theme.dialogs_groupDrawable.getIntrinsicWidth();
         } else if (this.drawNameBroadcast) {
            var2 = AndroidUtilities.dp(4.0F);
            var50 = Theme.dialogs_broadcastDrawable.getIntrinsicWidth();
         } else {
            var50 = var48;
            if (!this.drawNameBot) {
               break label928;
            }

            var2 = AndroidUtilities.dp(4.0F);
            var50 = Theme.dialogs_botDrawable.getIntrinsicWidth();
         }

         var50 = var48 - (var2 + var50);
      }

      if (this.drawClock) {
         var48 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0F);
         var2 = var50 - var48;
         if (!LocaleController.isRTL) {
            this.checkDrawLeft = this.timeLeft - var48;
         } else {
            this.checkDrawLeft = this.timeLeft + var14 + AndroidUtilities.dp(5.0F);
            this.nameLeft += var48;
         }
      } else {
         var2 = var50;
         if (this.drawCheck2) {
            var48 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0F);
            var2 = var50 - var48;
            if (this.drawCheck1) {
               var2 -= Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0F);
               if (!LocaleController.isRTL) {
                  this.halfCheckDrawLeft = this.timeLeft - var48;
                  this.checkDrawLeft = this.halfCheckDrawLeft - AndroidUtilities.dp(5.5F);
               } else {
                  this.checkDrawLeft = this.timeLeft + var14 + AndroidUtilities.dp(5.0F);
                  this.halfCheckDrawLeft = this.checkDrawLeft + AndroidUtilities.dp(5.5F);
                  this.nameLeft += var48 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0F);
               }
            } else if (!LocaleController.isRTL) {
               this.checkDrawLeft = this.timeLeft - var48;
            } else {
               this.checkDrawLeft = this.timeLeft + var14 + AndroidUtilities.dp(5.0F);
               this.nameLeft += var48;
            }
         }
      }

      if (this.dialogMuted && !this.drawVerified && !this.drawScam) {
         var48 = AndroidUtilities.dp(6.0F) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
         var2 -= var48;
         var50 = var2;
         if (LocaleController.isRTL) {
            this.nameLeft += var48;
            var50 = var2;
         }
      } else if (this.drawVerified) {
         var48 = AndroidUtilities.dp(6.0F) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
         var2 -= var48;
         var50 = var2;
         if (LocaleController.isRTL) {
            this.nameLeft += var48;
            var50 = var2;
         }
      } else {
         var50 = var2;
         if (this.drawScam) {
            var48 = AndroidUtilities.dp(6.0F) + Theme.dialogs_scamDrawable.getIntrinsicWidth();
            var2 -= var48;
            var50 = var2;
            if (LocaleController.isRTL) {
               this.nameLeft += var48;
               var50 = var2;
            }
         }
      }

      var48 = Math.max(AndroidUtilities.dp(12.0F), var50);

      try {
         CharSequence var79 = TextUtils.ellipsize(var10.replace('\n', ' '), Theme.dialogs_namePaint, (float)(var48 - AndroidUtilities.dp(12.0F)), TruncateAt.END);
         StaticLayout var82 = new StaticLayout(var79, Theme.dialogs_namePaint, var48, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
         this.nameLayout = var82;
      } catch (Exception var30) {
         FileLog.e((Throwable)var30);
      }

      if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
         var14 = AndroidUtilities.dp(9.0F);
         this.messageNameTop = AndroidUtilities.dp(31.0F);
         this.timeTop = AndroidUtilities.dp(16.0F);
         this.errorTop = AndroidUtilities.dp(39.0F);
         this.pinTop = AndroidUtilities.dp(39.0F);
         this.countTop = AndroidUtilities.dp(39.0F);
         this.checkDrawTop = AndroidUtilities.dp(17.0F);
         var50 = this.getMeasuredWidth() - AndroidUtilities.dp(95.0F);
         if (!LocaleController.isRTL) {
            var2 = AndroidUtilities.dp(76.0F);
            this.messageNameLeft = var2;
            this.messageLeft = var2;
            var2 = AndroidUtilities.dp(10.0F);
         } else {
            var2 = AndroidUtilities.dp(22.0F);
            this.messageNameLeft = var2;
            this.messageLeft = var2;
            var2 = this.getMeasuredWidth() - AndroidUtilities.dp(64.0F);
         }

         this.avatarImage.setImageCoords(var2, var14, AndroidUtilities.dp(54.0F), AndroidUtilities.dp(54.0F));
         var2 = var50;
      } else {
         var14 = AndroidUtilities.dp(11.0F);
         this.messageNameTop = AndroidUtilities.dp(32.0F);
         this.timeTop = AndroidUtilities.dp(13.0F);
         this.errorTop = AndroidUtilities.dp(43.0F);
         this.pinTop = AndroidUtilities.dp(43.0F);
         this.countTop = AndroidUtilities.dp(43.0F);
         this.checkDrawTop = AndroidUtilities.dp(13.0F);
         var50 = this.getMeasuredWidth() - AndroidUtilities.dp(93.0F);
         if (!LocaleController.isRTL) {
            var2 = AndroidUtilities.dp(78.0F);
            this.messageNameLeft = var2;
            this.messageLeft = var2;
            var2 = AndroidUtilities.dp(10.0F);
         } else {
            var2 = AndroidUtilities.dp(16.0F);
            this.messageNameLeft = var2;
            this.messageLeft = var2;
            var2 = this.getMeasuredWidth() - AndroidUtilities.dp(66.0F);
         }

         this.avatarImage.setImageCoords(var2, var14, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
         var2 = var50;
      }

      if (this.drawPin) {
         if (!LocaleController.isRTL) {
            this.pinLeft = this.getMeasuredWidth() - Theme.dialogs_pinnedDrawable.getIntrinsicWidth() - AndroidUtilities.dp(14.0F);
         } else {
            this.pinLeft = AndroidUtilities.dp(14.0F);
         }
      }

      if (this.drawError) {
         var50 = AndroidUtilities.dp(31.0F);
         var2 -= var50;
         if (!LocaleController.isRTL) {
            this.errorLeft = this.getMeasuredWidth() - AndroidUtilities.dp(34.0F);
         } else {
            this.errorLeft = AndroidUtilities.dp(11.0F);
            this.messageLeft += var50;
            this.messageNameLeft += var50;
         }
      } else if (var57 == null && var11 == null) {
         var50 = var2;
         if (this.drawPin) {
            var14 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0F);
            var2 -= var14;
            var50 = var2;
            if (LocaleController.isRTL) {
               this.messageLeft += var14;
               this.messageNameLeft += var14;
               var50 = var2;
            }
         }

         this.drawCount = false;
         this.drawMention = false;
         var2 = var50;
      } else {
         if (var57 != null) {
            this.countWidth = Math.max(AndroidUtilities.dp(12.0F), (int)Math.ceil((double)Theme.dialogs_countTextPaint.measureText(var57)));
            this.countLayout = new StaticLayout(var57, Theme.dialogs_countTextPaint, this.countWidth, Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
            var14 = this.countWidth + AndroidUtilities.dp(18.0F);
            var50 = var2 - var14;
            if (!LocaleController.isRTL) {
               this.countLeft = this.getMeasuredWidth() - this.countWidth - AndroidUtilities.dp(20.0F);
            } else {
               this.countLeft = AndroidUtilities.dp(20.0F);
               this.messageLeft += var14;
               this.messageNameLeft += var14;
            }

            this.drawCount = true;
         } else {
            this.countWidth = 0;
            var50 = var2;
         }

         var2 = var50;
         if (var11 != null) {
            if (this.currentDialogFolderId != 0) {
               this.mentionWidth = Math.max(AndroidUtilities.dp(12.0F), (int)Math.ceil((double)Theme.dialogs_countTextPaint.measureText(var11)));
               this.mentionLayout = new StaticLayout(var11, Theme.dialogs_countTextPaint, this.mentionWidth, Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
            } else {
               this.mentionWidth = AndroidUtilities.dp(12.0F);
            }

            var14 = this.mentionWidth + AndroidUtilities.dp(18.0F);
            var50 -= var14;
            int var17;
            if (!LocaleController.isRTL) {
               var14 = this.getMeasuredWidth();
               int var16 = this.mentionWidth;
               var17 = AndroidUtilities.dp(20.0F);
               var2 = this.countWidth;
               if (var2 != 0) {
                  var2 += AndroidUtilities.dp(18.0F);
               } else {
                  var2 = 0;
               }

               this.mentionLeft = var14 - var16 - var17 - var2;
            } else {
               var17 = AndroidUtilities.dp(20.0F);
               var2 = this.countWidth;
               if (var2 != 0) {
                  var2 += AndroidUtilities.dp(18.0F);
               } else {
                  var2 = 0;
               }

               this.mentionLeft = var17 + var2;
               this.messageLeft += var14;
               this.messageNameLeft += var14;
            }

            this.drawMention = true;
            var2 = var50;
         }
      }

      var91 = var56;
      if (var13) {
         var91 = var56;
         if (var56 == null) {
            var91 = "";
         }

         var57 = ((CharSequence)var91).toString();
         var6 = var57;
         if (var57.length() > 150) {
            var6 = var57.substring(0, 150);
         }

         label1376: {
            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
               var57 = var6;
               if (var44 == null) {
                  break label1376;
               }
            }

            var57 = var6.replace('\n', ' ');
         }

         var91 = Emoji.replaceEmoji(var57, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0F), false);
      }

      var75 = Math.max(AndroidUtilities.dp(12.0F), var2);
      if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout || var44 == null || this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount != 1) {
         this.messageNameLayout = null;
         if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
            this.messageTop = AndroidUtilities.dp(39.0F);
         } else {
            this.messageTop = AndroidUtilities.dp(32.0F);
         }
      } else {
         try {
            this.messageNameLayout = StaticLayoutEx.createStaticLayout((CharSequence)var44, Theme.dialogs_messageNamePaint, var75, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false, TruncateAt.END, var75, 1);
         } catch (Exception var29) {
            FileLog.e((Throwable)var29);
         }

         this.messageTop = AndroidUtilities.dp(51.0F);
      }

      float var18;
      StaticLayout var95;
      label870: {
         label1308: {
            label1309: {
               label1310: {
                  try {
                     if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                        break label1310;
                     }
                  } catch (Exception var39) {
                     var10000 = var39;
                     var10001 = false;
                     break label1308;
                  }

                  try {
                     if (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount <= 1) {
                        break label1310;
                     }

                     var58 = Theme.dialogs_messagePaint;
                  } catch (Exception var38) {
                     var10000 = var38;
                     var10001 = false;
                     break label1308;
                  }

                  var8 = null;
                  var56 = var44;
                  var44 = var8;
                  break label1309;
               }

               label853: {
                  try {
                     if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                        break label853;
                     }
                  } catch (Exception var37) {
                     var10000 = var37;
                     var10001 = false;
                     break label1308;
                  }

                  if (var44 == null) {
                     var56 = var91;
                     break label1309;
                  }
               }

               try {
                  var56 = TextUtils.ellipsize((CharSequence)var91, var58, (float)(var75 - AndroidUtilities.dp(12.0F)), TruncateAt.END);
               } catch (Exception var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label1308;
               }
            }

            label1320: {
               try {
                  if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                     break label1320;
                  }
               } catch (Exception var35) {
                  var10000 = var35;
                  var10001 = false;
                  break label1308;
               }

               Alignment var83;
               TruncateAt var96;
               try {
                  var83 = Alignment.ALIGN_NORMAL;
                  var18 = (float)AndroidUtilities.dp(1.0F);
                  var96 = TruncateAt.END;
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label1308;
               }

               byte var94;
               if (var44 != null) {
                  var94 = 1;
               } else {
                  var94 = 2;
               }

               try {
                  this.messageLayout = StaticLayoutEx.createStaticLayout((CharSequence)var56, var58, var75, var83, 1.0F, var18, false, var96, var75, var94);
                  break label870;
               } catch (Exception var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label1308;
               }
            }

            try {
               var95 = new StaticLayout((CharSequence)var56, var58, var75, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
               this.messageLayout = var95;
               break label870;
            } catch (Exception var32) {
               var10000 = var32;
               var10001 = false;
            }
         }

         var73 = var10000;
         FileLog.e((Throwable)var73);
      }

      double var19;
      double var21;
      double var25;
      if (LocaleController.isRTL) {
         var95 = this.nameLayout;
         if (var95 != null && var95.getLineCount() > 0) {
            var18 = this.nameLayout.getLineLeft(0);
            var19 = Math.ceil((double)this.nameLayout.getLineWidth(0));
            double var23;
            double var27;
            if (this.dialogMuted && !this.drawVerified && !this.drawScam) {
               var21 = (double)this.nameLeft;
               var23 = (double)var48;
               Double.isNaN(var23);
               Double.isNaN(var21);
               var25 = (double)AndroidUtilities.dp(6.0F);
               Double.isNaN(var25);
               var27 = (double)Theme.dialogs_muteDrawable.getIntrinsicWidth();
               Double.isNaN(var27);
               this.nameMuteLeft = (int)(var21 + (var23 - var19) - var25 - var27);
            } else if (this.drawVerified) {
               var27 = (double)this.nameLeft;
               var25 = (double)var48;
               Double.isNaN(var25);
               Double.isNaN(var27);
               var23 = (double)AndroidUtilities.dp(6.0F);
               Double.isNaN(var23);
               var21 = (double)Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
               Double.isNaN(var21);
               this.nameMuteLeft = (int)(var27 + (var25 - var19) - var23 - var21);
            } else if (this.drawScam) {
               var23 = (double)this.nameLeft;
               var27 = (double)var48;
               Double.isNaN(var27);
               Double.isNaN(var23);
               var21 = (double)AndroidUtilities.dp(6.0F);
               Double.isNaN(var21);
               var25 = (double)Theme.dialogs_scamDrawable.getIntrinsicWidth();
               Double.isNaN(var25);
               this.nameMuteLeft = (int)(var23 + (var27 - var19) - var21 - var25);
            }

            if (var18 == 0.0F) {
               var25 = (double)var48;
               if (var19 < var25) {
                  var21 = (double)this.nameLeft;
                  Double.isNaN(var25);
                  Double.isNaN(var21);
                  this.nameLeft = (int)(var21 + (var25 - var19));
               }
            }
         }

         var95 = this.messageLayout;
         if (var95 != null) {
            var14 = var95.getLineCount();
            if (var14 > 0) {
               var50 = 0;
               var2 = Integer.MAX_VALUE;

               while(true) {
                  var48 = var2;
                  if (var50 >= var14) {
                     break;
                  }

                  if (this.messageLayout.getLineLeft(var50) != 0.0F) {
                     var48 = 0;
                     break;
                  }

                  var19 = Math.ceil((double)this.messageLayout.getLineWidth(var50));
                  var21 = (double)var75;
                  Double.isNaN(var21);
                  var2 = Math.min(var2, (int)(var21 - var19));
                  ++var50;
               }

               if (var48 != Integer.MAX_VALUE) {
                  this.messageLeft += var48;
               }
            }
         }

         var95 = this.messageNameLayout;
         if (var95 != null && var95.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0F) {
            var19 = Math.ceil((double)this.messageNameLayout.getLineWidth(0));
            var25 = (double)var75;
            if (var19 < var25) {
               var21 = (double)this.messageNameLeft;
               Double.isNaN(var25);
               Double.isNaN(var21);
               this.messageNameLeft = (int)(var21 + (var25 - var19));
            }
         }
      } else {
         var95 = this.nameLayout;
         if (var95 != null && var95.getLineCount() > 0) {
            var18 = this.nameLayout.getLineRight(0);
            if (var18 == (float)var48) {
               var19 = Math.ceil((double)this.nameLayout.getLineWidth(0));
               var21 = (double)var48;
               if (var19 < var21) {
                  var25 = (double)this.nameLeft;
                  Double.isNaN(var21);
                  Double.isNaN(var25);
                  this.nameLeft = (int)(var25 - (var21 - var19));
               }
            }

            if (this.dialogMuted || this.drawVerified || this.drawScam) {
               this.nameMuteLeft = (int)((float)this.nameLeft + var18 + (float)AndroidUtilities.dp(6.0F));
            }
         }

         var95 = this.messageLayout;
         if (var95 != null) {
            var50 = var95.getLineCount();
            if (var50 > 0) {
               var2 = 0;

               for(var18 = 2.14748365E9F; var2 < var50; ++var2) {
                  var18 = Math.min(var18, this.messageLayout.getLineLeft(var2));
               }

               this.messageLeft = (int)((float)this.messageLeft - var18);
            }
         }

         var95 = this.messageNameLayout;
         if (var95 != null && var95.getLineCount() > 0) {
            this.messageNameLeft = (int)((float)this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
         }
      }

   }

   public void checkCurrentDialogIndex(boolean var1) {
      ArrayList var2 = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, var1);
      if (this.index < var2.size()) {
         TLRPC.Dialog var3 = (TLRPC.Dialog)var2.get(this.index);
         int var4 = this.index;
         boolean var5 = true;
         TLRPC.Dialog var11;
         if (var4 + 1 < var2.size()) {
            var11 = (TLRPC.Dialog)var2.get(this.index + 1);
         } else {
            var11 = null;
         }

         TLRPC.DraftMessage var6 = DataQuery.getInstance(this.currentAccount).getDraft(this.currentDialogId);
         MessageObject var7;
         if (this.currentDialogFolderId != 0) {
            var7 = this.findFolderTopMessage();
         } else {
            var7 = (MessageObject)MessagesController.getInstance(this.currentAccount).dialogMessage.get(var3.id);
         }

         if (this.currentDialogId == var3.id) {
            MessageObject var8 = this.message;
            if ((var8 == null || var8.getId() == var3.top_message) && (var7 == null || var7.messageOwner.edit_date == this.currentEditDate) && this.unreadCount == var3.unread_count && this.mentionCount == var3.unread_mentions_count && this.markUnread == var3.unread_mark) {
               var8 = this.message;
               if (var8 == var7 && (var8 != null || var7 == null) && var6 == this.draftMessage && this.drawPin == var3.pinned) {
                  return;
               }
            }
         }

         boolean var12;
         if (this.currentDialogId != var3.id) {
            var12 = true;
         } else {
            var12 = false;
         }

         this.currentDialogId = var3.id;
         boolean var9 = var3 instanceof TLRPC.TL_dialogFolder;
         if (var9) {
            this.currentDialogFolderId = ((TLRPC.TL_dialogFolder)var3).folder.id;
         } else {
            this.currentDialogFolderId = 0;
         }

         if (var3 instanceof TLRPC.TL_dialog && var3.pinned && var11 != null && !var11.pinned) {
            var1 = true;
         } else {
            var1 = false;
         }

         this.fullSeparator = var1;
         if (var9 && var11 != null && !var11.pinned) {
            var1 = var5;
         } else {
            var1 = false;
         }

         this.fullSeparator2 = var1;
         this.update(0);
         if (var12) {
            float var10;
            if (this.drawPin && this.drawReorder) {
               var10 = 1.0F;
            } else {
               var10 = 0.0F;
            }

            this.reorderIconProgress = var10;
         }

         this.checkOnline();
      }

   }

   public float getClipProgress() {
      return this.clipProgress;
   }

   public long getDialogId() {
      return this.currentDialogId;
   }

   public int getDialogIndex() {
      return this.index;
   }

   public int getMessageId() {
      return this.messageId;
   }

   public float getTranslationX() {
      return this.translationX;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public void invalidateDrawable(Drawable var1) {
      if (var1 != this.translationDrawable && var1 != Theme.dialogs_archiveAvatarDrawable) {
         super.invalidateDrawable(var1);
      } else {
         this.invalidate(var1.getBounds());
      }

   }

   public boolean isPointInsideAvatar(float var1, float var2) {
      boolean var3 = LocaleController.isRTL;
      boolean var4 = true;
      boolean var5 = true;
      if (!var3) {
         if (var1 < 0.0F || var1 >= (float)AndroidUtilities.dp(60.0F)) {
            var5 = false;
         }

         return var5;
      } else {
         if (var1 >= (float)(this.getMeasuredWidth() - AndroidUtilities.dp(60.0F)) && var1 < (float)this.getMeasuredWidth()) {
            var5 = var4;
         } else {
            var5 = false;
         }

         return var5;
      }
   }

   public boolean isUnread() {
      boolean var1;
      if ((this.unreadCount != 0 || this.markUnread) && !this.dialogMuted) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.avatarImage.onAttachedToWindow();
      this.archiveHidden = SharedConfig.archiveHidden;
      boolean var1 = this.archiveHidden;
      float var2 = 1.0F;
      float var3;
      if (var1) {
         var3 = 0.0F;
      } else {
         var3 = 1.0F;
      }

      this.archiveBackgroundProgress = var3;
      this.avatarDrawable.setArchivedAvatarHiddenProgress(this.archiveBackgroundProgress);
      this.clipProgress = 0.0F;
      this.isSliding = false;
      if (this.drawPin && this.drawReorder) {
         var3 = var2;
      } else {
         var3 = 0.0F;
      }

      this.reorderIconProgress = var3;
      this.attachedToWindow = true;
      this.cornerProgress = 0.0F;
      this.setTranslationX(0.0F);
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.isSliding = false;
      this.drawRevealBackground = false;
      this.currentRevealProgress = 0.0F;
      this.attachedToWindow = false;
      float var1;
      if (this.drawPin && this.drawReorder) {
         var1 = 1.0F;
      } else {
         var1 = 0.0F;
      }

      this.reorderIconProgress = var1;
      this.avatarImage.onDetachedFromWindow();
      Drawable var2 = this.translationDrawable;
      if (var2 != null) {
         if (var2 instanceof LottieDrawable) {
            LottieDrawable var3 = (LottieDrawable)var2;
            var3.stop();
            var3.setProgress(0.0F);
            var3.setCallback((Callback)null);
         }

         this.translationDrawable = null;
         this.translationAnimationStarted = false;
      }

   }

   @SuppressLint({"DrawAllocation"})
   protected void onDraw(Canvas var1) {
      if (this.currentDialogId != 0L || this.customDialog != null) {
         long var2 = SystemClock.uptimeMillis();
         long var4 = var2 - this.lastUpdateTime;
         long var6 = var4;
         if (var4 > 17L) {
            var6 = 17L;
         }

         this.lastUpdateTime = var2;
         if (this.clipProgress != 0.0F && VERSION.SDK_INT != 24) {
            var1.save();
            var1.clipRect(0.0F, (float)this.topClip * this.clipProgress, (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - (int)((float)this.bottomClip * this.clipProgress)));
         }

         int var9;
         float var12;
         int var13;
         float var14;
         int var15;
         int var16;
         Drawable var19;
         if (this.translationX == 0.0F && this.cornerProgress == 0.0F) {
            var19 = this.translationDrawable;
            if (var19 != null) {
               if (var19 instanceof LottieDrawable) {
                  LottieDrawable var20 = (LottieDrawable)var19;
                  var20.stop();
                  var20.setProgress(0.0F);
                  var20.setCallback((Callback)null);
               }

               this.translationDrawable = null;
               this.translationAnimationStarted = false;
            }
         } else {
            var1.save();
            String var8;
            int var10;
            if (this.currentDialogFolderId != 0) {
               if (this.archiveHidden) {
                  var9 = Theme.getColor("chats_archivePinBackground");
                  var10 = Theme.getColor("chats_archiveBackground");
                  var8 = LocaleController.getString("UnhideFromTop", 2131560936);
                  this.translationDrawable = Theme.dialogs_unpinArchiveDrawable;
               } else {
                  var9 = Theme.getColor("chats_archiveBackground");
                  var10 = Theme.getColor("chats_archivePinBackground");
                  var8 = LocaleController.getString("HideOnTop", 2131559637);
                  this.translationDrawable = Theme.dialogs_pinArchiveDrawable;
               }
            } else if (this.folderId == 0) {
               var9 = Theme.getColor("chats_archiveBackground");
               var10 = Theme.getColor("chats_archivePinBackground");
               var8 = LocaleController.getString("Archive", 2131558642);
               this.translationDrawable = Theme.dialogs_archiveDrawable;
            } else {
               var9 = Theme.getColor("chats_archivePinBackground");
               var10 = Theme.getColor("chats_archiveBackground");
               var8 = LocaleController.getString("Unarchive", 2131560928);
               this.translationDrawable = Theme.dialogs_unarchiveDrawable;
            }

            Drawable var11;
            if (!this.translationAnimationStarted && Math.abs(this.translationX) > (float)AndroidUtilities.dp(43.0F)) {
               this.translationAnimationStarted = true;
               var11 = this.translationDrawable;
               if (var11 instanceof LottieDrawable) {
                  LottieDrawable var23 = (LottieDrawable)var11;
                  var23.setProgress(0.0F);
                  var23.setCallback(this);
                  var23.start();
               }
            }

            var12 = (float)this.getMeasuredWidth() + this.translationX;
            if (this.currentRevealProgress < 1.0F) {
               Theme.dialogs_pinnedPaint.setColor(var9);
               var1.drawRect(var12 - (float)AndroidUtilities.dp(8.0F), 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_pinnedPaint);
               if (this.currentRevealProgress == 0.0F && Theme.dialogs_archiveDrawableRecolored) {
                  var11 = Theme.dialogs_archiveDrawable;
                  if (var11 instanceof LottieDrawable) {
                     ((LottieDrawable)var11).addValueCallback(new KeyPath(new String[]{"Arrow", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("chats_archiveBackground"))));
                  }

                  Theme.dialogs_archiveDrawableRecolored = false;
               }
            }

            var13 = this.getMeasuredWidth() - AndroidUtilities.dp(43.0F) - this.translationDrawable.getIntrinsicWidth() / 2;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               var14 = 9.0F;
            } else {
               var14 = 12.0F;
            }

            var15 = AndroidUtilities.dp(var14);
            var9 = var15;
            if (!(this.translationDrawable instanceof LottieDrawable)) {
               var9 = var15 + AndroidUtilities.dp(2.0F);
            }

            var15 = this.translationDrawable.getIntrinsicWidth() / 2 + var13;
            var16 = this.translationDrawable.getIntrinsicHeight() / 2 + var9;
            if (this.currentRevealProgress > 0.0F) {
               var1.save();
               var1.clipRect(var12 - (float)AndroidUtilities.dp(8.0F), 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
               Theme.dialogs_pinnedPaint.setColor(var10);
               var14 = (float)Math.sqrt((double)(var15 * var15 + (var16 - this.getMeasuredHeight()) * (var16 - this.getMeasuredHeight())));
               var1.drawCircle((float)var15, (float)var16, var14 * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
               var1.restore();
               if (!Theme.dialogs_archiveDrawableRecolored) {
                  var11 = Theme.dialogs_archiveDrawable;
                  if (var11 instanceof LottieDrawable) {
                     ((LottieDrawable)var11).addValueCallback(new KeyPath(new String[]{"Arrow", "**"}), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(Theme.getColor("chats_archivePinBackground"))));
                  }

                  Theme.dialogs_archiveDrawableRecolored = true;
               }
            }

            var1.save();
            var1.translate((float)var13, (float)var9);
            var14 = this.currentRevealBounceProgress;
            if (var14 != 0.0F && var14 != 1.0F) {
               var14 = this.interpolator.getInterpolation(var14) + 1.0F;
               var1.scale(var14, var14, (float)(this.translationDrawable.getIntrinsicWidth() / 2), (float)(this.translationDrawable.getIntrinsicHeight() / 2));
            }

            BaseCell.setDrawableBounds(this.translationDrawable, 0, 0);
            this.translationDrawable.draw(var1);
            var1.restore();
            var1.clipRect(var12, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
            var10 = (int)Math.ceil((double)Theme.dialogs_countTextPaint.measureText(var8));
            var12 = (float)(this.getMeasuredWidth() - AndroidUtilities.dp(43.0F) - var10 / 2);
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               var14 = 59.0F;
            } else {
               var14 = 62.0F;
            }

            var1.drawText(var8, var12, (float)AndroidUtilities.dp(var14), Theme.dialogs_archiveTextPaint);
            var1.restore();
         }

         if (this.translationX != 0.0F) {
            var1.save();
            var1.translate(this.translationX, 0.0F);
         }

         if (this.isSelected) {
            var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_tabletSeletedPaint);
         }

         if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0F)) {
            Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay"), this.archiveBackgroundProgress, 1.0F));
            var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_pinnedPaint);
         } else if (this.drawPin || this.drawPinBackground) {
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay"));
            var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_pinnedPaint);
         }

         if (this.translationX != 0.0F || this.cornerProgress != 0.0F) {
            var1.save();
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor("windowBackgroundWhite"));
            this.rect.set((float)(this.getMeasuredWidth() - AndroidUtilities.dp(64.0F)), 0.0F, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
            var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(8.0F) * this.cornerProgress, (float)AndroidUtilities.dp(8.0F) * this.cornerProgress, Theme.dialogs_pinnedPaint);
            if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0F)) {
               Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay"), this.archiveBackgroundProgress, 1.0F));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(8.0F) * this.cornerProgress, (float)AndroidUtilities.dp(8.0F) * this.cornerProgress, Theme.dialogs_pinnedPaint);
            } else if (this.drawPin || this.drawPinBackground) {
               Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay"));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(8.0F) * this.cornerProgress, (float)AndroidUtilities.dp(8.0F) * this.cornerProgress, Theme.dialogs_pinnedPaint);
            }

            var1.restore();
         }

         boolean var24;
         label577: {
            label576: {
               if (this.translationX != 0.0F) {
                  var14 = this.cornerProgress;
                  if (var14 < 1.0F) {
                     this.cornerProgress = var14 + (float)var6 / 150.0F;
                     if (this.cornerProgress > 1.0F) {
                        this.cornerProgress = 1.0F;
                     }
                     break label576;
                  }
               } else {
                  var14 = this.cornerProgress;
                  if (var14 > 0.0F) {
                     this.cornerProgress = var14 - (float)var6 / 150.0F;
                     if (this.cornerProgress < 0.0F) {
                        this.cornerProgress = 0.0F;
                     }
                     break label576;
                  }
               }

               var24 = false;
               break label577;
            }

            var24 = true;
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

         TextPaint var22;
         if (this.nameLayout != null) {
            if (this.currentDialogFolderId != 0) {
               var22 = Theme.dialogs_namePaint;
               var9 = Theme.getColor("chats_nameArchived");
               var22.linkColor = var9;
               var22.setColor(var9);
            } else {
               label697: {
                  if (this.encryptedChat == null) {
                     DialogCell.CustomDialog var21 = this.customDialog;
                     if (var21 == null || var21.type != 2) {
                        var22 = Theme.dialogs_namePaint;
                        var9 = Theme.getColor("chats_name");
                        var22.linkColor = var9;
                        var22.setColor(var9);
                        break label697;
                     }
                  }

                  var22 = Theme.dialogs_namePaint;
                  var9 = Theme.getColor("chats_secretName");
                  var22.linkColor = var9;
                  var22.setColor(var9);
               }
            }

            var1.save();
            var12 = (float)this.nameLeft;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               var14 = 13.0F;
            } else {
               var14 = 10.0F;
            }

            var1.translate(var12, (float)AndroidUtilities.dp(var14));
            this.nameLayout.draw(var1);
            var1.restore();
         }

         if (this.timeLayout != null && this.currentDialogFolderId == 0) {
            var1.save();
            var1.translate((float)this.timeLeft, (float)this.timeTop);
            this.timeLayout.draw(var1);
            var1.restore();
         }

         if (this.messageNameLayout != null) {
            if (this.currentDialogFolderId != 0) {
               var22 = Theme.dialogs_messageNamePaint;
               var9 = Theme.getColor("chats_nameMessageArchived_threeLines");
               var22.linkColor = var9;
               var22.setColor(var9);
            } else if (this.draftMessage != null) {
               var22 = Theme.dialogs_messageNamePaint;
               var9 = Theme.getColor("chats_draft");
               var22.linkColor = var9;
               var22.setColor(var9);
            } else {
               var22 = Theme.dialogs_messageNamePaint;
               var9 = Theme.getColor("chats_nameMessage_threeLines");
               var22.linkColor = var9;
               var22.setColor(var9);
            }

            var1.save();
            var1.translate((float)this.messageNameLeft, (float)this.messageNameTop);

            try {
               this.messageNameLayout.draw(var1);
            } catch (Exception var18) {
               FileLog.e((Throwable)var18);
            }

            var1.restore();
         }

         if (this.messageLayout != null) {
            if (this.currentDialogFolderId != 0) {
               if (this.chat != null) {
                  var22 = Theme.dialogs_messagePaint;
                  var9 = Theme.getColor("chats_nameMessageArchived");
                  var22.linkColor = var9;
                  var22.setColor(var9);
               } else {
                  var22 = Theme.dialogs_messagePaint;
                  var9 = Theme.getColor("chats_messageArchived");
                  var22.linkColor = var9;
                  var22.setColor(var9);
               }
            } else {
               var22 = Theme.dialogs_messagePaint;
               var9 = Theme.getColor("chats_message");
               var22.linkColor = var9;
               var22.setColor(var9);
            }

            var1.save();
            var1.translate((float)this.messageLeft, (float)this.messageTop);

            try {
               this.messageLayout.draw(var1);
            } catch (Exception var17) {
               FileLog.e((Throwable)var17);
            }

            var1.restore();
         }

         if (this.currentDialogFolderId == 0) {
            if (this.drawClock) {
               BaseCell.setDrawableBounds(Theme.dialogs_clockDrawable, this.checkDrawLeft, this.checkDrawTop);
               Theme.dialogs_clockDrawable.draw(var1);
            } else if (this.drawCheck2) {
               if (this.drawCheck1) {
                  BaseCell.setDrawableBounds(Theme.dialogs_halfCheckDrawable, this.halfCheckDrawLeft, this.checkDrawTop);
                  Theme.dialogs_halfCheckDrawable.draw(var1);
                  BaseCell.setDrawableBounds(Theme.dialogs_checkDrawable, this.checkDrawLeft, this.checkDrawTop);
                  Theme.dialogs_checkDrawable.draw(var1);
               } else {
                  BaseCell.setDrawableBounds(Theme.dialogs_checkDrawable, this.checkDrawLeft, this.checkDrawTop);
                  Theme.dialogs_checkDrawable.draw(var1);
               }
            }
         }

         if (this.dialogMuted && !this.drawVerified && !this.drawScam) {
            var19 = Theme.dialogs_muteDrawable;
            var15 = this.nameMuteLeft;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               var14 = 1.0F;
            } else {
               var14 = 0.0F;
            }

            var9 = AndroidUtilities.dp(var14);
            if (SharedConfig.useThreeLinesLayout) {
               var14 = 13.5F;
            } else {
               var14 = 17.5F;
            }

            BaseCell.setDrawableBounds(var19, var15 - var9, AndroidUtilities.dp(var14));
            Theme.dialogs_muteDrawable.draw(var1);
         } else if (this.drawVerified) {
            var19 = Theme.dialogs_verifiedDrawable;
            var9 = this.nameMuteLeft;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               var14 = 16.5F;
            } else {
               var14 = 12.5F;
            }

            BaseCell.setDrawableBounds(var19, var9, AndroidUtilities.dp(var14));
            var19 = Theme.dialogs_verifiedCheckDrawable;
            var9 = this.nameMuteLeft;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               var14 = 16.5F;
            } else {
               var14 = 12.5F;
            }

            BaseCell.setDrawableBounds(var19, var9, AndroidUtilities.dp(var14));
            Theme.dialogs_verifiedDrawable.draw(var1);
            Theme.dialogs_verifiedCheckDrawable.draw(var1);
         } else if (this.drawScam) {
            ScamDrawable var26 = Theme.dialogs_scamDrawable;
            var9 = this.nameMuteLeft;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
               var14 = 15.0F;
            } else {
               var14 = 12.0F;
            }

            BaseCell.setDrawableBounds(var26, var9, AndroidUtilities.dp(var14));
            Theme.dialogs_scamDrawable.draw(var1);
         }

         if (this.drawReorder || this.reorderIconProgress != 0.0F) {
            Theme.dialogs_reorderDrawable.setAlpha((int)(this.reorderIconProgress * 255.0F));
            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
            Theme.dialogs_reorderDrawable.draw(var1);
         }

         if (this.drawError) {
            Theme.dialogs_errorDrawable.setAlpha((int)((1.0F - this.reorderIconProgress) * 255.0F));
            RectF var27 = this.rect;
            var9 = this.errorLeft;
            var27.set((float)var9, (float)this.errorTop, (float)(var9 + AndroidUtilities.dp(23.0F)), (float)(this.errorTop + AndroidUtilities.dp(23.0F)));
            var27 = this.rect;
            var14 = AndroidUtilities.density;
            var1.drawRoundRect(var27, var14 * 11.5F, var14 * 11.5F, Theme.dialogs_errorPaint);
            BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5F), this.errorTop + AndroidUtilities.dp(5.0F));
            Theme.dialogs_errorDrawable.draw(var1);
         } else if (!this.drawCount && !this.drawMention) {
            if (this.drawPin) {
               Theme.dialogs_pinnedDrawable.setAlpha((int)((1.0F - this.reorderIconProgress) * 255.0F));
               BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
               Theme.dialogs_pinnedDrawable.draw(var1);
            }
         } else {
            RectF var25;
            Paint var28;
            if (this.drawCount) {
               if (!this.dialogMuted && this.currentDialogFolderId == 0) {
                  var28 = Theme.dialogs_countPaint;
               } else {
                  var28 = Theme.dialogs_countGrayPaint;
               }

               var28.setAlpha((int)((1.0F - this.reorderIconProgress) * 255.0F));
               Theme.dialogs_countTextPaint.setAlpha((int)((1.0F - this.reorderIconProgress) * 255.0F));
               var9 = this.countLeft - AndroidUtilities.dp(5.5F);
               this.rect.set((float)var9, (float)this.countTop, (float)(var9 + this.countWidth + AndroidUtilities.dp(11.0F)), (float)(this.countTop + AndroidUtilities.dp(23.0F)));
               var25 = this.rect;
               var14 = AndroidUtilities.density;
               var1.drawRoundRect(var25, var14 * 11.5F, var14 * 11.5F, var28);
               if (this.countLayout != null) {
                  var1.save();
                  var1.translate((float)this.countLeft, (float)(this.countTop + AndroidUtilities.dp(4.0F)));
                  this.countLayout.draw(var1);
                  var1.restore();
               }
            }

            if (this.drawMention) {
               Theme.dialogs_countPaint.setAlpha((int)((1.0F - this.reorderIconProgress) * 255.0F));
               var9 = this.mentionLeft - AndroidUtilities.dp(5.5F);
               this.rect.set((float)var9, (float)this.countTop, (float)(var9 + this.mentionWidth + AndroidUtilities.dp(11.0F)), (float)(this.countTop + AndroidUtilities.dp(23.0F)));
               if (this.dialogMuted && this.folderId != 0) {
                  var28 = Theme.dialogs_countGrayPaint;
               } else {
                  var28 = Theme.dialogs_countPaint;
               }

               var25 = this.rect;
               var14 = AndroidUtilities.density;
               var1.drawRoundRect(var25, var14 * 11.5F, var14 * 11.5F, var28);
               if (this.mentionLayout != null) {
                  Theme.dialogs_countTextPaint.setAlpha((int)((1.0F - this.reorderIconProgress) * 255.0F));
                  var1.save();
                  var1.translate((float)this.mentionLeft, (float)(this.countTop + AndroidUtilities.dp(4.0F)));
                  this.mentionLayout.draw(var1);
                  var1.restore();
               } else {
                  Theme.dialogs_mentionDrawable.setAlpha((int)((1.0F - this.reorderIconProgress) * 255.0F));
                  BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0F), this.countTop + AndroidUtilities.dp(3.2F), AndroidUtilities.dp(16.0F), AndroidUtilities.dp(16.0F));
                  Theme.dialogs_mentionDrawable.draw(var1);
               }
            }
         }

         if (this.animatingArchiveAvatar) {
            var1.save();
            var14 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0F) + 1.0F;
            var1.scale(var14, var14, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
         }

         this.avatarImage.draw(var1);
         if (this.animatingArchiveAvatar) {
            var1.restore();
         }

         TLRPC.User var30 = this.user;
         boolean var29;
         if (var30 != null && this.isDialogCell && this.currentDialogFolderId == 0 && !MessagesController.isSupportUser(var30)) {
            var30 = this.user;
            if (!var30.bot) {
               label479: {
                  if (!var30.self) {
                     TLRPC.UserStatus var31 = var30.status;
                     if (var31 != null && var31.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(this.user.id)) {
                        var29 = true;
                        break label479;
                     }
                  }

                  var29 = false;
               }

               if (var29 || this.onlineProgress != 0.0F) {
                  label688: {
                     var16 = this.avatarImage.getImageY2();
                     if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                        var14 = 8.0F;
                     } else {
                        var14 = 6.0F;
                     }

                     var13 = AndroidUtilities.dp(var14);
                     if (LocaleController.isRTL) {
                        var15 = this.avatarImage.getImageX();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                           var14 = 6.0F;
                        } else {
                           var14 = 10.0F;
                        }

                        var15 += AndroidUtilities.dp(var14);
                     } else {
                        var15 = this.avatarImage.getImageX2();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                           var14 = 6.0F;
                        } else {
                           var14 = 10.0F;
                        }

                        var15 -= AndroidUtilities.dp(var14);
                     }

                     Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite"));
                     var14 = (float)var15;
                     var12 = (float)(var16 - var13);
                     var1.drawCircle(var14, var12, (float)AndroidUtilities.dp(7.0F) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                     Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle"));
                     var1.drawCircle(var14, var12, (float)AndroidUtilities.dp(5.0F) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                     if (var29) {
                        var14 = this.onlineProgress;
                        if (var14 >= 1.0F) {
                           break label688;
                        }

                        this.onlineProgress = var14 + (float)var6 / 150.0F;
                        if (this.onlineProgress > 1.0F) {
                           this.onlineProgress = 1.0F;
                        }
                     } else {
                        var14 = this.onlineProgress;
                        if (var14 <= 0.0F) {
                           break label688;
                        }

                        this.onlineProgress = var14 - (float)var6 / 150.0F;
                        if (this.onlineProgress < 0.0F) {
                           this.onlineProgress = 0.0F;
                        }
                     }

                     var24 = true;
                  }
               }
            }
         }

         if (this.translationX != 0.0F) {
            var1.restore();
         }

         if (this.useSeparator) {
            if (this.fullSeparator || this.currentDialogFolderId != 0 && this.archiveHidden && !this.fullSeparator2 || this.fullSeparator2 && !this.archiveHidden) {
               var9 = 0;
            } else {
               var9 = AndroidUtilities.dp(72.0F);
            }

            if (LocaleController.isRTL) {
               var1.drawLine(0.0F, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - var9), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            } else {
               var1.drawLine((float)var9, (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
         }

         if (this.clipProgress != 0.0F) {
            if (VERSION.SDK_INT != 24) {
               var1.restore();
            } else {
               Theme.dialogs_pinnedPaint.setColor(Theme.getColor("windowBackgroundWhite"));
               var14 = (float)this.getMeasuredWidth();
               var12 = (float)this.topClip;
               var1.drawRect(0.0F, 0.0F, var14, this.clipProgress * var12, Theme.dialogs_pinnedPaint);
               var1.drawRect(0.0F, (float)(this.getMeasuredHeight() - (int)((float)this.bottomClip * this.clipProgress)), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_pinnedPaint);
            }
         }

         label421: {
            label657: {
               if (!this.drawReorder) {
                  var29 = var24;
                  if (this.reorderIconProgress == 0.0F) {
                     break label657;
                  }
               }

               if (!this.drawReorder) {
                  var14 = this.reorderIconProgress;
                  if (var14 > 0.0F) {
                     this.reorderIconProgress = var14 - (float)var6 / 170.0F;
                     if (this.reorderIconProgress < 0.0F) {
                        this.reorderIconProgress = 0.0F;
                     }

                     var24 = true;
                  }
                  break label421;
               }

               var14 = this.reorderIconProgress;
               var29 = var24;
               if (var14 < 1.0F) {
                  this.reorderIconProgress = var14 + (float)var6 / 170.0F;
                  if (this.reorderIconProgress > 1.0F) {
                     this.reorderIconProgress = 1.0F;
                  }

                  var29 = true;
               }
            }

            var24 = var29;
         }

         label412: {
            if (this.archiveHidden) {
               var14 = this.archiveBackgroundProgress;
               if (var14 <= 0.0F) {
                  break label412;
               }

               this.archiveBackgroundProgress = var14 - (float)var6 / 170.0F;
               if (this.currentRevealBounceProgress < 0.0F) {
                  this.currentRevealBounceProgress = 0.0F;
               }

               if (this.avatarDrawable.getAvatarType() == 3) {
                  this.avatarDrawable.setArchivedAvatarHiddenProgress(this.archiveBackgroundProgress);
               }
            } else {
               var14 = this.archiveBackgroundProgress;
               if (var14 >= 1.0F) {
                  break label412;
               }

               this.archiveBackgroundProgress = var14 + (float)var6 / 170.0F;
               if (this.currentRevealBounceProgress > 1.0F) {
                  this.currentRevealBounceProgress = 1.0F;
               }

               if (this.avatarDrawable.getAvatarType() == 3) {
                  this.avatarDrawable.setArchivedAvatarHiddenProgress(this.archiveBackgroundProgress);
               }
            }

            var24 = true;
         }

         if (this.animatingArchiveAvatar) {
            this.animatingArchiveAvatarProgress += (float)var6;
            if (this.animatingArchiveAvatarProgress >= 170.0F) {
               this.animatingArchiveAvatarProgress = 170.0F;
               this.animatingArchiveAvatar = false;
            }

            var24 = true;
         }

         label403: {
            if (this.drawRevealBackground) {
               var14 = this.currentRevealBounceProgress;
               if (var14 < 1.0F) {
                  this.currentRevealBounceProgress = var14 + (float)var6 / 170.0F;
                  if (this.currentRevealBounceProgress > 1.0F) {
                     this.currentRevealBounceProgress = 1.0F;
                     var24 = true;
                  }
               }

               var14 = this.currentRevealProgress;
               if (var14 >= 1.0F) {
                  break label403;
               }

               this.currentRevealProgress = var14 + (float)var6 / 300.0F;
               if (this.currentRevealProgress > 1.0F) {
                  this.currentRevealProgress = 1.0F;
               }
            } else {
               if (this.currentRevealBounceProgress == 1.0F) {
                  this.currentRevealBounceProgress = 0.0F;
                  var24 = true;
               }

               var14 = this.currentRevealProgress;
               if (var14 <= 0.0F) {
                  break label403;
               }

               this.currentRevealProgress = var14 - (float)var6 / 300.0F;
               if (this.currentRevealProgress < 0.0F) {
                  this.currentRevealProgress = 0.0F;
               }
            }

            var24 = true;
         }

         if (var24) {
            this.invalidate();
         }

      }
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.addAction(16);
      var1.addAction(32);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (this.currentDialogId != 0L || this.customDialog != null) {
         if (this.checkBox != null) {
            if (LocaleController.isRTL) {
               var2 = var4 - var2 - AndroidUtilities.dp(45.0F);
            } else {
               var2 = AndroidUtilities.dp(45.0F);
            }

            var3 = AndroidUtilities.dp(46.0F);
            CheckBox2 var6 = this.checkBox;
            var6.layout(var2, var3, var6.getMeasuredWidth() + var2, this.checkBox.getMeasuredHeight() + var3);
         }

         if (var1) {
            try {
               this.buildLayout();
            } catch (Exception var7) {
               FileLog.e((Throwable)var7);
            }
         }

      }
   }

   protected void onMeasure(int var1, int var2) {
      CheckBox2 var3 = this.checkBox;
      if (var3 != null) {
         var3.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), 1073741824));
      }

      var1 = MeasureSpec.getSize(var1);
      float var4;
      if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
         var4 = 72.0F;
      } else {
         var4 = 78.0F;
      }

      this.setMeasuredDimension(var1, AndroidUtilities.dp(var4) + this.useSeparator);
      this.topClip = 0;
      this.bottomClip = this.getMeasuredHeight();
   }

   public void onPopulateAccessibilityEvent(AccessibilityEvent var1) {
      super.onPopulateAccessibilityEvent(var1);
      StringBuilder var2 = new StringBuilder();
      TLRPC.User var3;
      if (this.currentDialogFolderId == 1) {
         var2.append(LocaleController.getString("ArchivedChats", 2131558653));
         var2.append(". ");
      } else {
         if (this.encryptedChat != null) {
            var2.append(LocaleController.getString("AccDescrSecretChat", 2131558470));
            var2.append(". ");
         }

         var3 = this.user;
         if (var3 != null) {
            if (var3.bot) {
               var2.append(LocaleController.getString("Bot", 2131558848));
               var2.append(". ");
            }

            var3 = this.user;
            if (var3.self) {
               var2.append(LocaleController.getString("SavedMessages", 2131560633));
            } else {
               var2.append(ContactsController.formatName(var3.first_name, var3.last_name));
            }

            var2.append(". ");
         } else {
            TLRPC.Chat var6 = this.chat;
            if (var6 != null) {
               if (var6.broadcast) {
                  var2.append(LocaleController.getString("AccDescrChannel", 2131558426));
               } else {
                  var2.append(LocaleController.getString("AccDescrGroup", 2131558437));
               }

               var2.append(". ");
               var2.append(this.chat.title);
               var2.append(". ");
            }
         }
      }

      int var4 = this.unreadCount;
      if (var4 > 0) {
         var2.append(LocaleController.formatPluralString("NewMessages", var4));
         var2.append(". ");
      }

      MessageObject var7 = this.message;
      if (var7 != null && this.currentDialogFolderId == 0) {
         int var5 = this.lastMessageDate;
         var4 = var5;
         if (var5 == 0) {
            var4 = var5;
            if (var7 != null) {
               var4 = var7.messageOwner.date;
            }
         }

         String var8 = LocaleController.formatDateAudio((long)var4);
         if (this.message.isOut()) {
            var2.append(LocaleController.formatString("AccDescrSentDate", 2131558471, var8));
         } else {
            var2.append(LocaleController.formatString("AccDescrReceivedDate", 2131558461, var8));
         }

         var2.append(". ");
         if (this.chat != null && !this.message.isOut() && this.message.isFromUser() && this.message.messageOwner.action == null) {
            var3 = MessagesController.getInstance(this.currentAccount).getUser(this.message.messageOwner.from_id);
            if (var3 != null) {
               var2.append(ContactsController.formatName(var3.first_name, var3.last_name));
               var2.append(". ");
            }
         }

         if (this.encryptedChat == null) {
            var2.append(this.message.messageText);
            if (!this.message.isMediaEmpty() && !TextUtils.isEmpty(this.message.caption)) {
               var2.append(". ");
               var2.append(this.message.caption);
            }
         }

         var1.setContentDescription(var2.toString());
      } else {
         var1.setContentDescription(var2.toString());
      }
   }

   public void onReorderStateChanged(boolean var1, boolean var2) {
      if ((this.drawPin || !var1) && this.drawReorder != var1) {
         this.drawReorder = var1;
         float var3 = 1.0F;
         if (var2) {
            if (this.drawReorder) {
               var3 = 0.0F;
            }

            this.reorderIconProgress = var3;
         } else {
            if (!this.drawReorder) {
               var3 = 0.0F;
            }

            this.reorderIconProgress = var3;
         }

         this.invalidate();
      } else {
         if (!this.drawPin) {
            this.drawReorder = false;
         }

      }
   }

   public void setBottomClip(int var1) {
      this.bottomClip = var1;
   }

   public void setChecked(boolean var1, boolean var2) {
      CheckBox2 var3 = this.checkBox;
      if (var3 != null) {
         var3.setChecked(var1, var2);
      }
   }

   public void setClipProgress(float var1) {
      this.clipProgress = var1;
      this.invalidate();
   }

   public void setDialog(long var1, MessageObject var3, int var4) {
      this.currentDialogId = var1;
      this.message = var3;
      this.isDialogCell = false;
      this.lastMessageDate = var4;
      if (var3 != null) {
         var4 = var3.messageOwner.edit_date;
      } else {
         var4 = 0;
      }

      this.currentEditDate = var4;
      this.unreadCount = 0;
      this.markUnread = false;
      if (var3 != null) {
         var4 = var3.getId();
      } else {
         var4 = 0;
      }

      this.messageId = var4;
      this.mentionCount = 0;
      boolean var5;
      if (var3 != null && var3.isUnread()) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.lastUnreadState = var5;
      var3 = this.message;
      if (var3 != null) {
         this.lastSendState = var3.messageOwner.send_state;
      }

      this.update(0);
   }

   public void setDialog(TLRPC.Dialog var1, int var2, int var3) {
      this.currentDialogId = var1.id;
      this.isDialogCell = true;
      if (var1 instanceof TLRPC.TL_dialogFolder) {
         this.currentDialogFolderId = ((TLRPC.TL_dialogFolder)var1).folder.id;
      } else {
         this.currentDialogFolderId = 0;
      }

      this.dialogsType = var2;
      this.folderId = var3;
      this.messageId = 0;
      this.update(0);
      this.checkOnline();
   }

   public void setDialog(DialogCell.CustomDialog var1) {
      this.customDialog = var1;
      this.messageId = 0;
      this.update(0);
      this.checkOnline();
   }

   public void setDialogIndex(int var1) {
      this.index = var1;
   }

   public void setDialogSelected(boolean var1) {
      if (this.isSelected != var1) {
         this.invalidate();
      }

      this.isSelected = var1;
   }

   public void setSliding(boolean var1) {
      this.isSliding = var1;
   }

   public void setTopClip(int var1) {
      this.topClip = var1;
   }

   public void setTranslationX(float var1) {
      this.translationX = (float)((int)var1);
      Drawable var2 = this.translationDrawable;
      boolean var3 = false;
      if (var2 != null && this.translationX == 0.0F) {
         if (var2 instanceof LottieDrawable) {
            ((LottieDrawable)var2).setProgress(0.0F);
         }

         this.translationAnimationStarted = false;
         this.archiveHidden = SharedConfig.archiveHidden;
         this.currentRevealProgress = 0.0F;
         this.isSliding = false;
      }

      if (this.translationX != 0.0F) {
         this.isSliding = true;
      }

      if (this.isSliding) {
         boolean var4 = this.drawRevealBackground;
         if (Math.abs(this.translationX) >= (float)this.getMeasuredWidth() * 0.3F) {
            var3 = true;
         }

         this.drawRevealBackground = var3;
         if (var4 != this.drawRevealBackground && this.archiveHidden == SharedConfig.archiveHidden) {
            try {
               this.performHapticFeedback(3, 2);
            } catch (Exception var5) {
            }
         }
      }

      this.invalidate();
   }

   public void update(int var1) {
      DialogCell.CustomDialog var2 = this.customDialog;
      boolean var3 = true;
      if (var2 != null) {
         this.lastMessageDate = var2.date;
         if (var2.unread_count == 0) {
            var3 = false;
         }

         this.lastUnreadState = var3;
         var2 = this.customDialog;
         this.unreadCount = var2.unread_count;
         this.drawPin = var2.pinned;
         this.dialogMuted = var2.muted;
         this.avatarDrawable.setInfo(var2.id, var2.name, (String)null, false);
         this.avatarImage.setImage((String)null, "50_50", this.avatarDrawable, (String)null, 0);
      } else {
         int var5;
         TLRPC.Dialog var10;
         MessageObject var11;
         if (!this.isDialogCell) {
            this.drawPin = false;
         } else {
            var10 = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
            if (var10 != null) {
               if (var1 == 0) {
                  this.clearingDialog = MessagesController.getInstance(this.currentAccount).isClearingDialog(var10.id);
                  this.message = (MessageObject)MessagesController.getInstance(this.currentAccount).dialogMessage.get(var10.id);
                  MessageObject var4 = this.message;
                  if (var4 != null && var4.isUnread()) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  this.lastUnreadState = var3;
                  this.unreadCount = var10.unread_count;
                  this.markUnread = var10.unread_mark;
                  this.mentionCount = var10.unread_mentions_count;
                  var4 = this.message;
                  if (var4 != null) {
                     var5 = var4.messageOwner.edit_date;
                  } else {
                     var5 = 0;
                  }

                  this.currentEditDate = var5;
                  this.lastMessageDate = var10.last_message_date;
                  if (this.currentDialogFolderId == 0 && var10.pinned) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  this.drawPin = var3;
                  var11 = this.message;
                  if (var11 != null) {
                     this.lastSendState = var11.messageOwner.send_state;
                  }
               }
            } else {
               this.unreadCount = 0;
               this.mentionCount = 0;
               this.currentEditDate = 0;
               this.lastMessageDate = 0;
               this.clearingDialog = false;
            }
         }

         if (var1 != 0) {
            if (this.user != null && (var1 & 4) != 0) {
               this.user = MessagesController.getInstance(this.currentAccount).getUser(this.user.id);
               this.invalidate();
            }

            boolean var6;
            label214: {
               label213: {
                  if (this.isDialogCell && (var1 & 64) != 0) {
                     CharSequence var13 = (CharSequence)MessagesController.getInstance(this.currentAccount).printingStrings.get(this.currentDialogId);
                     if (this.lastPrintString != null && var13 == null || this.lastPrintString == null && var13 != null) {
                        break label213;
                     }

                     CharSequence var12 = this.lastPrintString;
                     if (var12 != null && var13 != null && !var12.equals(var13)) {
                        break label213;
                     }
                  }

                  var6 = false;
                  break label214;
               }

               var6 = true;
            }

            boolean var15 = var6;
            if (!var6) {
               var15 = var6;
               if (('耀' & var1) != 0) {
                  var11 = this.message;
                  var15 = var6;
                  if (var11 != null) {
                     var15 = var6;
                     if (var11.messageText != this.lastMessageString) {
                        var15 = true;
                     }
                  }
               }
            }

            var6 = var15;
            if (!var15) {
               var6 = var15;
               if ((var1 & 2) != 0) {
                  var6 = var15;
                  if (this.chat == null) {
                     var6 = true;
                  }
               }
            }

            boolean var7 = var6;
            if (!var6) {
               var7 = var6;
               if ((var1 & 1) != 0) {
                  var7 = var6;
                  if (this.chat == null) {
                     var7 = true;
                  }
               }
            }

            var15 = var7;
            if (!var7) {
               var15 = var7;
               if ((var1 & 8) != 0) {
                  var15 = var7;
                  if (this.user == null) {
                     var15 = true;
                  }
               }
            }

            var6 = var15;
            if (!var15) {
               var6 = var15;
               if ((var1 & 16) != 0) {
                  var6 = var15;
                  if (this.user == null) {
                     var6 = true;
                  }
               }
            }

            var15 = var6;
            if (!var6) {
               var15 = var6;
               if ((var1 & 256) != 0) {
                  label255: {
                     var11 = this.message;
                     if (var11 != null && this.lastUnreadState != var11.isUnread()) {
                        this.lastUnreadState = this.message.isUnread();
                     } else {
                        var15 = var6;
                        if (!this.isDialogCell) {
                           break label255;
                        }

                        var10 = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                        var15 = var6;
                        if (var10 == null) {
                           break label255;
                        }

                        if (this.unreadCount == var10.unread_count && this.markUnread == var10.unread_mark) {
                           var15 = var6;
                           if (this.mentionCount == var10.unread_mentions_count) {
                              break label255;
                           }
                        }

                        this.unreadCount = var10.unread_count;
                        this.mentionCount = var10.unread_mentions_count;
                        this.markUnread = var10.unread_mark;
                     }

                     var15 = true;
                  }
               }
            }

            var6 = var15;
            if (!var15) {
               var6 = var15;
               if ((var1 & 4096) != 0) {
                  var11 = this.message;
                  var6 = var15;
                  if (var11 != null) {
                     var1 = this.lastSendState;
                     int var17 = var11.messageOwner.send_state;
                     var6 = var15;
                     if (var1 != var17) {
                        this.lastSendState = var17;
                        var6 = true;
                     }
                  }
               }
            }

            if (!var6) {
               this.invalidate();
               return;
            }
         }

         this.user = null;
         this.chat = null;
         this.encryptedChat = null;
         long var8;
         if (this.currentDialogFolderId != 0) {
            this.dialogMuted = false;
            this.message = this.findFolderTopMessage();
            var11 = this.message;
            if (var11 != null) {
               var8 = var11.getDialogId();
            } else {
               var8 = 0L;
            }
         } else {
            if (this.isDialogCell && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId)) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.dialogMuted = var3;
            var8 = this.currentDialogId;
         }

         TLRPC.Chat var14;
         if (var8 != 0L) {
            var1 = (int)var8;
            var5 = (int)(var8 >> 32);
            if (var1 != 0) {
               if (var5 == 1) {
                  this.chat = MessagesController.getInstance(this.currentAccount).getChat(var1);
               } else if (var1 < 0) {
                  this.chat = MessagesController.getInstance(this.currentAccount).getChat(-var1);
                  if (!this.isDialogCell) {
                     var14 = this.chat;
                     if (var14 != null && var14.migrated_to != null) {
                        var14 = MessagesController.getInstance(this.currentAccount).getChat(this.chat.migrated_to.channel_id);
                        if (var14 != null) {
                           this.chat = var14;
                        }
                     }
                  }
               } else {
                  this.user = MessagesController.getInstance(this.currentAccount).getUser(var1);
               }
            } else {
               this.encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(var5);
               if (this.encryptedChat != null) {
                  this.user = MessagesController.getInstance(this.currentAccount).getUser(this.encryptedChat.user_id);
               }
            }
         }

         if (this.currentDialogFolderId != 0) {
            this.avatarDrawable.setAvatarType(3);
            this.avatarImage.setImage((ImageLocation)null, (String)null, this.avatarDrawable, (String)null, this.user, 0);
         } else {
            TLRPC.User var16 = this.user;
            if (var16 != null) {
               this.avatarDrawable.setInfo(var16);
               if (UserObject.isUserSelf(this.user)) {
                  this.avatarDrawable.setAvatarType(1);
                  this.avatarImage.setImage((ImageLocation)null, (String)null, this.avatarDrawable, (String)null, this.user, 0);
               } else {
                  this.avatarImage.setImage(ImageLocation.getForUser(this.user, false), "50_50", this.avatarDrawable, (String)null, this.user, 0);
               }
            } else {
               var14 = this.chat;
               if (var14 != null) {
                  this.avatarDrawable.setInfo(var14);
                  this.avatarImage.setImage(ImageLocation.getForChat(this.chat, false), "50_50", this.avatarDrawable, (String)null, this.chat, 0);
               }
            }
         }
      }

      if (this.getMeasuredWidth() == 0 && this.getMeasuredHeight() == 0) {
         this.requestLayout();
      } else {
         this.buildLayout();
      }

      this.invalidate();
   }

   public class BounceInterpolator implements Interpolator {
      public float getInterpolation(float var1) {
         if (var1 < 0.33F) {
            return var1 / 0.33F * 0.1F;
         } else {
            var1 -= 0.33F;
            return var1 < 0.33F ? 0.1F - var1 / 0.34F * 0.15F : (var1 - 0.34F) / 0.33F * 0.05F - 0.05F;
         }
      }
   }

   public static class CustomDialog {
      public int date;
      public int id;
      public boolean isMedia;
      public String message;
      public boolean muted;
      public String name;
      public boolean pinned;
      public boolean sent;
      public int type;
      public int unread_count;
      public boolean verified;
   }
}
