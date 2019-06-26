package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;

public class ChatEditActivity extends BaseFragment implements ImageUpdater.ImageUpdaterDelegate, NotificationCenter.NotificationCenterDelegate {
   private static final int done_button = 1;
   private TextCell adminCell;
   private TLRPC.FileLocation avatar;
   private AnimatorSet avatarAnimation;
   private TLRPC.FileLocation avatarBig;
   private LinearLayout avatarContainer;
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private ImageView avatarEditor;
   private BackupImageView avatarImage;
   private View avatarOverlay;
   private RadialProgressView avatarProgressView;
   private TextCell blockCell;
   private int chatId;
   private boolean createAfterUpload;
   private TLRPC.Chat currentChat;
   private TextSettingsCell deleteCell;
   private FrameLayout deleteContainer;
   private ShadowSectionCell deleteInfoCell;
   private EditTextBoldCursor descriptionTextView;
   private View doneButton;
   private boolean donePressed;
   private TextDetailCell historyCell;
   private boolean historyHidden;
   private ImageUpdater imageUpdater = new ImageUpdater();
   private TLRPC.ChatFull info;
   private LinearLayout infoContainer;
   private ShadowSectionCell infoSectionCell;
   private boolean isChannel;
   private TextDetailCell linkedCell;
   private TextCell logCell;
   private TextCell membersCell;
   private EditTextEmoji nameTextView;
   private AlertDialog progressDialog;
   private LinearLayout settingsContainer;
   private ShadowSectionCell settingsSectionCell;
   private ShadowSectionCell settingsTopSectionCell;
   private TextCheckCell signCell;
   private boolean signMessages;
   private TextSettingsCell stickersCell;
   private FrameLayout stickersContainer;
   private TextInfoPrivacyCell stickersInfoCell3;
   private TextDetailCell typeCell;
   private LinearLayout typeEditContainer;
   private TLRPC.InputFile uploadedAvatar;

   public ChatEditActivity(Bundle var1) {
      super(var1);
      this.chatId = var1.getInt("chat_id", 0);
   }

   // $FF: synthetic method
   static ActionBar access$200(ChatEditActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$400(ChatEditActivity var0) {
      return var0.actionBar;
   }

   private boolean checkDiscard() {
      String var4;
      label53: {
         TLRPC.ChatFull var1 = this.info;
         if (var1 != null) {
            var4 = var1.about;
            if (var4 != null) {
               break label53;
            }
         }

         var4 = "";
      }

      if ((this.info == null || !ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory == this.historyHidden) && this.imageUpdater.uploadingImage == null) {
         EditTextEmoji var2 = this.nameTextView;
         if (var2 == null || this.currentChat.title.equals(var2.getText().toString())) {
            EditTextBoldCursor var5 = this.descriptionTextView;
            if (var5 == null || var4.equals(var5.getText().toString())) {
               boolean var3 = this.signMessages;
               TLRPC.Chat var6 = this.currentChat;
               if (var3 == var6.signatures && this.uploadedAvatar == null && (this.avatar != null || !(var6.photo instanceof TLRPC.TL_chatPhoto))) {
                  return true;
               }
            }
         }
      }

      AlertDialog.Builder var7 = new AlertDialog.Builder(this.getParentActivity());
      var7.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131560995));
      if (this.isChannel) {
         var7.setMessage(LocaleController.getString("ChannelSettingsChangedAlert", 2131558999));
      } else {
         var7.setMessage(LocaleController.getString("GroupSettingsChangedAlert", 2131559613));
      }

      var7.setPositiveButton(LocaleController.getString("ApplyTheme", 2131558639), new _$$Lambda$ChatEditActivity$42WGB1bZqU27h5UDp3vuD_usGEg(this));
      var7.setNegativeButton(LocaleController.getString("PassportDiscard", 2131560212), new _$$Lambda$ChatEditActivity$NBEr6CX4NZ1r3XbdnOXbearPc6k(this));
      this.showDialog(var7.create());
      return false;
   }

   private int getAdminCount() {
      TLRPC.ChatFull var1 = this.info;
      if (var1 == null) {
         return 1;
      } else {
         int var2 = var1.participants.participants.size();
         int var3 = 0;

         int var4;
         int var5;
         for(var4 = 0; var3 < var2; var4 = var5) {
            label19: {
               TLRPC.ChatParticipant var6 = (TLRPC.ChatParticipant)this.info.participants.participants.get(var3);
               if (!(var6 instanceof TLRPC.TL_chatParticipantAdmin)) {
                  var5 = var4;
                  if (!(var6 instanceof TLRPC.TL_chatParticipantCreator)) {
                     break label19;
                  }
               }

               var5 = var4 + 1;
            }

            ++var3;
         }

         return var4;
      }
   }

   // $FF: synthetic method
   static boolean lambda$createView$1(View var0, MotionEvent var1) {
      return true;
   }

   private void processDone() {
      if (!this.donePressed) {
         EditTextEmoji var1 = this.nameTextView;
         if (var1 != null) {
            if (var1.length() == 0) {
               Vibrator var8 = (Vibrator)this.getParentActivity().getSystemService("vibrator");
               if (var8 != null) {
                  var8.vibrate(200L);
               }

               AndroidUtilities.shakeView(this.nameTextView, 2.0F, 0);
               return;
            }

            this.donePressed = true;
            if (!ChatObject.isChannel(this.currentChat) && !this.historyHidden) {
               MessagesController.getInstance(super.currentAccount).convertToMegaGroup(this.getParentActivity(), this.chatId, new _$$Lambda$ChatEditActivity$FM5TsTTFL8A_rr_SPaVMYlNXC2Q(this));
               return;
            }

            boolean var2;
            TLRPC.ChatFull var5;
            if (this.info != null && ChatObject.isChannel(this.currentChat)) {
               var5 = this.info;
               var2 = var5.hidden_prehistory;
               boolean var3 = this.historyHidden;
               if (var2 != var3) {
                  var5.hidden_prehistory = var3;
                  MessagesController.getInstance(super.currentAccount).toogleChannelInvitesHistory(this.chatId, this.historyHidden);
               }
            }

            if (this.imageUpdater.uploadingImage != null) {
               this.createAfterUpload = true;
               this.progressDialog = new AlertDialog(this.getParentActivity(), 3);
               this.progressDialog.setOnCancelListener(new _$$Lambda$ChatEditActivity$G_WbIT_ViCCFZMn6b9uYoBS_uJ0(this));
               this.progressDialog.show();
               return;
            }

            if (!this.currentChat.title.equals(this.nameTextView.getText().toString())) {
               MessagesController.getInstance(super.currentAccount).changeChatTitle(this.chatId, this.nameTextView.getText().toString());
            }

            String var6;
            label58: {
               var5 = this.info;
               if (var5 != null) {
                  var6 = var5.about;
                  if (var6 != null) {
                     break label58;
                  }
               }

               var6 = "";
            }

            EditTextBoldCursor var4 = this.descriptionTextView;
            if (var4 != null && !var6.equals(var4.getText().toString())) {
               MessagesController.getInstance(super.currentAccount).updateChatAbout(this.chatId, this.descriptionTextView.getText().toString(), this.info);
            }

            var2 = this.signMessages;
            TLRPC.Chat var7 = this.currentChat;
            if (var2 != var7.signatures) {
               var7.signatures = true;
               MessagesController.getInstance(super.currentAccount).toogleChannelSignatures(this.chatId, this.signMessages);
            }

            if (this.uploadedAvatar != null) {
               MessagesController.getInstance(super.currentAccount).changeChatAvatar(this.chatId, this.uploadedAvatar, this.avatar, this.avatarBig);
            } else if (this.avatar == null && this.currentChat.photo instanceof TLRPC.TL_chatPhoto) {
               MessagesController.getInstance(super.currentAccount).changeChatAvatar(this.chatId, (TLRPC.InputFile)null, (TLRPC.FileLocation)null, (TLRPC.FileLocation)null);
            }

            this.finishFragment();
         }
      }

   }

   private void showAvatarProgress(final boolean var1, boolean var2) {
      if (this.avatarEditor != null) {
         AnimatorSet var3 = this.avatarAnimation;
         if (var3 != null) {
            var3.cancel();
            this.avatarAnimation = null;
         }

         if (var2) {
            this.avatarAnimation = new AnimatorSet();
            if (var1) {
               this.avatarProgressView.setVisibility(0);
               this.avatarAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, new float[]{0.0F}), ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, new float[]{1.0F})});
            } else {
               this.avatarEditor.setVisibility(0);
               this.avatarAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, new float[]{1.0F}), ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, new float[]{0.0F})});
            }

            this.avatarAnimation.setDuration(180L);
            this.avatarAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1x) {
                  ChatEditActivity.this.avatarAnimation = null;
               }

               public void onAnimationEnd(Animator var1x) {
                  if (ChatEditActivity.this.avatarAnimation != null && ChatEditActivity.this.avatarEditor != null) {
                     if (var1) {
                        ChatEditActivity.this.avatarEditor.setVisibility(4);
                     } else {
                        ChatEditActivity.this.avatarProgressView.setVisibility(4);
                     }

                     ChatEditActivity.this.avatarAnimation = null;
                  }

               }
            });
            this.avatarAnimation.start();
         } else if (var1) {
            this.avatarEditor.setAlpha(1.0F);
            this.avatarEditor.setVisibility(4);
            this.avatarProgressView.setAlpha(1.0F);
            this.avatarProgressView.setVisibility(0);
         } else {
            this.avatarEditor.setAlpha(1.0F);
            this.avatarEditor.setVisibility(0);
            this.avatarProgressView.setAlpha(0.0F);
            this.avatarProgressView.setVisibility(4);
         }

      }
   }

   private void updateFields(boolean var1) {
      TLRPC.Chat var2;
      if (var1) {
         var2 = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
         if (var2 != null) {
            this.currentChat = var2;
         }
      }

      var1 = TextUtils.isEmpty(this.currentChat.username);
      TextDetailCell var9 = this.historyCell;
      byte var4;
      if (var9 != null) {
         label257: {
            if (var1) {
               TLRPC.ChatFull var3 = this.info;
               if (var3 == null || var3.linked_chat_id == 0) {
                  var4 = 0;
                  break label257;
               }
            }

            var4 = 8;
         }

         var9.setVisibility(var4);
      }

      ShadowSectionCell var11 = this.settingsSectionCell;
      if (var11 != null) {
         label246: {
            if (this.signCell == null && this.typeCell == null && this.linkedCell == null) {
               var9 = this.historyCell;
               if (var9 == null || var9.getVisibility() != 0) {
                  var4 = 8;
                  break label246;
               }
            }

            var4 = 0;
         }

         var11.setVisibility(var4);
      }

      TextCell var13 = this.logCell;
      TLRPC.ChatFull var10;
      if (var13 != null) {
         label233: {
            if (this.currentChat.megagroup) {
               var10 = this.info;
               if (var10 == null || var10.participants_count <= 200) {
                  var4 = 8;
                  break label233;
               }
            }

            var4 = 0;
         }

         var13.setVisibility(var4);
      }

      String var12;
      int var21;
      if (this.typeCell != null) {
         if (this.isChannel) {
            if (var1) {
               var21 = 2131560922;
               var12 = "TypePrivate";
            } else {
               var21 = 2131560924;
               var12 = "TypePublic";
            }

            var12 = LocaleController.getString(var12, var21);
         } else {
            if (var1) {
               var21 = 2131560923;
               var12 = "TypePrivateGroup";
            } else {
               var21 = 2131560925;
               var12 = "TypePublicGroup";
            }

            var12 = LocaleController.getString(var12, var21);
         }

         if (this.isChannel) {
            this.typeCell.setTextAndValue(LocaleController.getString("ChannelType", 2131559005), var12, true);
         } else {
            this.typeCell.setTextAndValue(LocaleController.getString("GroupType", 2131559617), var12, true);
         }
      }

      String var5;
      if (this.linkedCell != null) {
         var10 = this.info;
         if (var10 == null || !this.isChannel && var10.linked_chat_id == 0) {
            this.linkedCell.setVisibility(8);
         } else {
            this.linkedCell.setVisibility(0);
            if (this.info.linked_chat_id == 0) {
               this.linkedCell.setTextAndValue(LocaleController.getString("Discussion", 2131559280), LocaleController.getString("DiscussionInfo", 2131559287), true);
            } else {
               var2 = this.getMessagesController().getChat(this.info.linked_chat_id);
               if (var2 == null) {
                  this.linkedCell.setVisibility(8);
               } else if (this.isChannel) {
                  if (TextUtils.isEmpty(var2.username)) {
                     this.linkedCell.setTextAndValue(LocaleController.getString("Discussion", 2131559280), var2.title, true);
                  } else {
                     TextDetailCell var14 = this.linkedCell;
                     var5 = LocaleController.getString("Discussion", 2131559280);
                     StringBuilder var6 = new StringBuilder();
                     var6.append("@");
                     var6.append(var2.username);
                     var14.setTextAndValue(var5, var6.toString(), true);
                  }
               } else if (TextUtils.isEmpty(var2.username)) {
                  this.linkedCell.setTextAndValue(LocaleController.getString("LinkedChannel", 2131559763), var2.title, false);
               } else {
                  TextDetailCell var17 = this.linkedCell;
                  var5 = LocaleController.getString("LinkedChannel", 2131559763);
                  StringBuilder var15 = new StringBuilder();
                  var15.append("@");
                  var15.append(var2.username);
                  var17.setTextAndValue(var5, var15.toString(), false);
               }
            }
         }
      }

      if (this.info != null && this.historyCell != null) {
         if (this.historyHidden) {
            var21 = 2131559034;
            var12 = "ChatHistoryHidden";
         } else {
            var21 = 2131559037;
            var12 = "ChatHistoryVisible";
         }

         var12 = LocaleController.getString(var12, var21);
         this.historyCell.setTextAndValue(LocaleController.getString("ChatHistory", 2131559033), var12, false);
      }

      TextSettingsCell var24 = this.stickersCell;
      if (var24 != null) {
         if (this.info.stickerset != null) {
            var24.setTextAndValue(LocaleController.getString("GroupStickers", 2131559615), this.info.stickerset.title, false);
         } else {
            var24.setText(LocaleController.getString("GroupStickers", 2131559615), false);
         }
      }

      var13 = this.membersCell;
      if (var13 != null) {
         TextCell var25;
         if (this.info != null) {
            if (this.isChannel) {
               var13.setTextAndValueAndIcon(LocaleController.getString("ChannelSubscribers", 2131559004), String.format("%d", this.info.participants_count), 2131165277, true);
               var25 = this.blockCell;
               String var16 = LocaleController.getString("ChannelBlacklist", 2131558932);
               TLRPC.ChatFull var18 = this.info;
               var5 = String.format("%d", Math.max(var18.banned_count, var18.kicked_count));
               TextCell var23 = this.logCell;
               if (var23 != null && var23.getVisibility() == 0) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               var25.setTextAndValueAndIcon(var16, var5, 2131165275, var1);
            } else {
               String var20;
               if (ChatObject.isChannel(this.currentChat)) {
                  var13 = this.membersCell;
                  var5 = LocaleController.getString("ChannelMembers", 2131558962);
                  var20 = String.format("%d", this.info.participants_count);
                  var25 = this.logCell;
                  if (var25 != null && var25.getVisibility() == 0) {
                     var1 = true;
                  } else {
                     var1 = false;
                  }

                  var13.setTextAndValueAndIcon(var5, var20, 2131165277, var1);
               } else {
                  var13 = this.membersCell;
                  var20 = LocaleController.getString("ChannelMembers", 2131558962);
                  var5 = String.format("%d", this.info.participants.participants.size());
                  var25 = this.logCell;
                  if (var25 != null && var25.getVisibility() == 0) {
                     var1 = true;
                  } else {
                     var1 = false;
                  }

                  var13.setTextAndValueAndIcon(var20, var5, 2131165277, var1);
               }

               TLRPC.TL_chatBannedRights var26 = this.currentChat.default_banned_rights;
               if (var26 != null) {
                  if (!var26.send_stickers) {
                     var4 = 1;
                  } else {
                     var4 = 0;
                  }

                  int var7 = var4;
                  if (!this.currentChat.default_banned_rights.send_media) {
                     var7 = var4 + 1;
                  }

                  var21 = var7;
                  if (!this.currentChat.default_banned_rights.embed_links) {
                     var21 = var7 + 1;
                  }

                  int var8 = var21;
                  if (!this.currentChat.default_banned_rights.send_messages) {
                     var8 = var21 + 1;
                  }

                  var7 = var8;
                  if (!this.currentChat.default_banned_rights.pin_messages) {
                     var7 = var8 + 1;
                  }

                  var21 = var7;
                  if (!this.currentChat.default_banned_rights.send_polls) {
                     var21 = var7 + 1;
                  }

                  var7 = var21;
                  if (!this.currentChat.default_banned_rights.invite_users) {
                     var7 = var21 + 1;
                  }

                  var21 = var7;
                  if (!this.currentChat.default_banned_rights.change_info) {
                     var21 = var7 + 1;
                  }
               } else {
                  var21 = 8;
               }

               this.blockCell.setTextAndValueAndIcon(LocaleController.getString("ChannelPermissions", 2131558985), String.format("%d/%d", var21, 8), 2131165273, true);
            }

            var13 = this.adminCell;
            var12 = LocaleController.getString("ChannelAdministrators", 2131558927);
            if (ChatObject.isChannel(this.currentChat)) {
               var21 = this.info.admins_count;
            } else {
               var21 = this.getAdminCount();
            }

            var13.setTextAndValueAndIcon(var12, String.format("%d", var21), 2131165271, true);
         } else {
            if (this.isChannel) {
               var13.setTextAndIcon(LocaleController.getString("ChannelSubscribers", 2131559004), 2131165277, true);
               TextCell var22 = this.blockCell;
               var12 = LocaleController.getString("ChannelBlacklist", 2131558932);
               var13 = this.logCell;
               if (var13 != null && var13.getVisibility() == 0) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               var22.setTextAndIcon(var12, 2131165275, var1);
            } else {
               var5 = LocaleController.getString("ChannelMembers", 2131558962);
               var25 = this.logCell;
               if (var25 != null && var25.getVisibility() == 0) {
                  var1 = true;
               } else {
                  var1 = false;
               }

               var13.setTextAndIcon(var5, 2131165277, var1);
               this.blockCell.setTextAndIcon(LocaleController.getString("ChannelPermissions", 2131558985), 2131165273, true);
            }

            this.adminCell.setTextAndIcon(LocaleController.getString("ChannelAdministrators", 2131558927), 2131165271, true);
         }
      }

      TextSettingsCell var19 = this.stickersCell;
      if (var19 != null) {
         var10 = this.info;
         if (var10 != null) {
            if (var10.stickerset != null) {
               var19.setTextAndValue(LocaleController.getString("GroupStickers", 2131559615), this.info.stickerset.title, false);
            } else {
               var19.setText(LocaleController.getString("GroupStickers", 2131559615), false);
            }
         }
      }

   }

   public View createView(Context var1) {
      EditTextEmoji var2 = this.nameTextView;
      if (var2 != null) {
         var2.onDestroy();
      }

      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (ChatEditActivity.this.checkDiscard()) {
                  ChatEditActivity.this.finishFragment();
               }
            } else if (var1 == 1) {
               ChatEditActivity.this.processDone();
            }

         }
      });
      SizeNotifierFrameLayout var3 = new SizeNotifierFrameLayout(var1) {
         private boolean ignoreLayout;

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            int var6 = this.getChildCount();
            int var7 = this.getKeyboardHeight();
            int var8 = AndroidUtilities.dp(20.0F);
            int var9 = 0;
            if (var7 <= var8 && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
               var8 = ChatEditActivity.this.nameTextView.getEmojiPadding();
            } else {
               var8 = 0;
            }

            this.setBottomClip(var8);

            for(; var9 < var6; ++var9) {
               View var10 = this.getChildAt(var9);
               if (var10.getVisibility() != 8) {
                  LayoutParams var11 = (LayoutParams)var10.getLayoutParams();
                  int var12 = var10.getMeasuredWidth();
                  int var13 = var10.getMeasuredHeight();
                  int var14 = var11.gravity;
                  var7 = var14;
                  if (var14 == -1) {
                     var7 = 51;
                  }

                  int var15;
                  label61: {
                     var15 = var7 & 112;
                     var7 = var7 & 7 & 7;
                     if (var7 != 1) {
                        if (var7 != 5) {
                           var14 = var11.leftMargin;
                           break label61;
                        }

                        var14 = var4 - var12;
                        var7 = var11.rightMargin;
                     } else {
                        var14 = (var4 - var2 - var12) / 2 + var11.leftMargin;
                        var7 = var11.rightMargin;
                     }

                     var14 -= var7;
                  }

                  label55: {
                     if (var15 != 16) {
                        if (var15 == 48) {
                           var7 = var11.topMargin + this.getPaddingTop();
                           break label55;
                        }

                        if (var15 != 80) {
                           var7 = var11.topMargin;
                           break label55;
                        }

                        var7 = var5 - var8 - var3 - var13;
                        var15 = var11.bottomMargin;
                     } else {
                        var7 = (var5 - var8 - var3 - var13) / 2 + var11.topMargin;
                        var15 = var11.bottomMargin;
                     }

                     var7 -= var15;
                  }

                  var15 = var7;
                  if (ChatEditActivity.this.nameTextView != null) {
                     var15 = var7;
                     if (ChatEditActivity.this.nameTextView.isPopupView(var10)) {
                        if (AndroidUtilities.isTablet()) {
                           var7 = this.getMeasuredHeight();
                           var15 = var10.getMeasuredHeight();
                        } else {
                           var7 = this.getMeasuredHeight() + this.getKeyboardHeight();
                           var15 = var10.getMeasuredHeight();
                        }

                        var15 = var7 - var15;
                     }
                  }

                  var10.layout(var14, var15, var12 + var14, var13 + var15);
               }
            }

            this.notifyHeightChanged();
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var1);
            int var4 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(var3, var4);
            int var5 = var4 - this.getPaddingTop();
            this.measureChildWithMargins(ChatEditActivity.access$200(ChatEditActivity.this), var1, 0, var2, 0);
            int var6 = this.getKeyboardHeight();
            int var7 = AndroidUtilities.dp(20.0F);
            var4 = 0;
            if (var6 > var7) {
               this.ignoreLayout = true;
               ChatEditActivity.this.nameTextView.hideEmojiView();
               this.ignoreLayout = false;
            }

            for(var6 = this.getChildCount(); var4 < var6; ++var4) {
               View var8 = this.getChildAt(var4);
               if (var8 != null && var8.getVisibility() != 8 && var8 != ChatEditActivity.access$400(ChatEditActivity.this)) {
                  if (ChatEditActivity.this.nameTextView != null && ChatEditActivity.this.nameTextView.isPopupView(var8)) {
                     if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                        var8.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var8.getLayoutParams().height, 1073741824));
                     } else if (AndroidUtilities.isTablet()) {
                        var7 = MeasureSpec.makeMeasureSpec(var3, 1073741824);
                        float var9;
                        if (AndroidUtilities.isTablet()) {
                           var9 = 200.0F;
                        } else {
                           var9 = 320.0F;
                        }

                        var8.measure(var7, MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(var9), var5 - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                     } else {
                        var8.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var5 - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                     }
                  } else {
                     this.measureChildWithMargins(var8, var1, 0, var2, 0);
                  }
               }
            }

         }

         public void requestLayout() {
            if (!this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      var3.setOnTouchListener(_$$Lambda$ChatEditActivity$VwiI9D4ZnAE2nkj3zFy5AkednDE.INSTANCE);
      super.fragmentView = var3;
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      ScrollView var4 = new ScrollView(var1);
      var4.setFillViewport(true);
      var3.addView(var4, LayoutHelper.createFrame(-1, -1.0F));
      LinearLayout var13 = new LinearLayout(var1);
      var4.addView(var13, new LayoutParams(-1, -2));
      var13.setOrientation(1);
      super.actionBar.setTitle(LocaleController.getString("ChannelEdit", 2131558950));
      this.avatarContainer = new LinearLayout(var1);
      this.avatarContainer.setOrientation(1);
      this.avatarContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      var13.addView(this.avatarContainer, LayoutHelper.createLinear(-1, -2));
      FrameLayout var18 = new FrameLayout(var1);
      this.avatarContainer.addView(var18, LayoutHelper.createLinear(-1, -2));
      this.avatarImage = new BackupImageView(var1) {
         public void invalidate() {
            if (ChatEditActivity.this.avatarOverlay != null) {
               ChatEditActivity.this.avatarOverlay.invalidate();
            }

            super.invalidate();
         }

         public void invalidate(int var1, int var2, int var3, int var4) {
            if (ChatEditActivity.this.avatarOverlay != null) {
               ChatEditActivity.this.avatarOverlay.invalidate();
            }

            super.invalidate(var1, var2, var3, var4);
         }
      };
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(32.0F));
      BackupImageView var5 = this.avatarImage;
      byte var6;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      float var7;
      if (LocaleController.isRTL) {
         var7 = 0.0F;
      } else {
         var7 = 16.0F;
      }

      float var8;
      if (LocaleController.isRTL) {
         var8 = 16.0F;
      } else {
         var8 = 0.0F;
      }

      byte var9 = 5;
      var18.addView(var5, LayoutHelper.createFrame(64, 64.0F, var6 | 48, var7, 12.0F, var8, 12.0F));
      if (ChatObject.canChangeChatInfo(this.currentChat)) {
         this.avatarDrawable.setInfo(5, (String)null, (String)null, false);
         final Paint var20 = new Paint(1);
         var20.setColor(1426063360);
         this.avatarOverlay = new View(var1) {
            protected void onDraw(Canvas var1) {
               if (ChatEditActivity.this.avatarImage != null && ChatEditActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                  var20.setAlpha((int)(ChatEditActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0F));
                  var1.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(32.0F), var20);
               }

            }
         };
         View var22 = this.avatarOverlay;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0.0F;
         } else {
            var7 = 16.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 16.0F;
         } else {
            var8 = 0.0F;
         }

         var18.addView(var22, LayoutHelper.createFrame(64, 64.0F, var6 | 48, var7, 12.0F, var8, 12.0F));
         this.avatarOverlay.setOnClickListener(new _$$Lambda$ChatEditActivity$Hxmf_lPSvYp0l6WoXkJtGNwopNs(this));
         this.avatarOverlay.setContentDescription(LocaleController.getString("ChoosePhoto", 2131559091));
         this.avatarEditor = new ImageView(var1) {
            public void invalidate() {
               super.invalidate();
               ChatEditActivity.this.avatarOverlay.invalidate();
            }

            public void invalidate(int var1, int var2, int var3, int var4) {
               super.invalidate(var1, var2, var3, var4);
               ChatEditActivity.this.avatarOverlay.invalidate();
            }
         };
         this.avatarEditor.setScaleType(ScaleType.CENTER);
         this.avatarEditor.setImageResource(2131165572);
         this.avatarEditor.setEnabled(false);
         this.avatarEditor.setClickable(false);
         ImageView var24 = this.avatarEditor;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0.0F;
         } else {
            var7 = 16.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 16.0F;
         } else {
            var8 = 0.0F;
         }

         var18.addView(var24, LayoutHelper.createFrame(64, 64.0F, var6 | 48, var7, 12.0F, var8, 12.0F));
         this.avatarProgressView = new RadialProgressView(var1);
         this.avatarProgressView.setSize(AndroidUtilities.dp(30.0F));
         this.avatarProgressView.setProgressColor(-1);
         RadialProgressView var26 = this.avatarProgressView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 0.0F;
         } else {
            var7 = 16.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 16.0F;
         } else {
            var8 = 0.0F;
         }

         var18.addView(var26, LayoutHelper.createFrame(64, 64.0F, var6 | 48, var7, 12.0F, var8, 12.0F));
         this.showAvatarProgress(false, false);
      } else {
         this.avatarDrawable.setInfo(5, this.currentChat.title, (String)null, false);
      }

      this.nameTextView = new EditTextEmoji(var1, var3, this, 0);
      if (this.isChannel) {
         this.nameTextView.setHint(LocaleController.getString("EnterChannelName", 2131559367));
      } else {
         this.nameTextView.setHint(LocaleController.getString("GroupName", 2131559610));
      }

      this.nameTextView.setEnabled(ChatObject.canChangeChatInfo(this.currentChat));
      EditTextEmoji var16 = this.nameTextView;
      var16.setFocusable(var16.isEnabled());
      LengthFilter var17 = new LengthFilter(100);
      this.nameTextView.setFilters(new InputFilter[]{var17});
      var16 = this.nameTextView;
      if (LocaleController.isRTL) {
         var7 = 5.0F;
      } else {
         var7 = 96.0F;
      }

      if (LocaleController.isRTL) {
         var8 = 96.0F;
      } else {
         var8 = 5.0F;
      }

      var18.addView(var16, LayoutHelper.createFrame(-1, -2.0F, 16, var7, 0.0F, var8, 0.0F));
      this.settingsContainer = new LinearLayout(var1);
      this.settingsContainer.setOrientation(1);
      this.settingsContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      var13.addView(this.settingsContainer, LayoutHelper.createLinear(-1, -2));
      this.descriptionTextView = new EditTextBoldCursor(var1);
      this.descriptionTextView.setTextSize(1, 16.0F);
      this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.descriptionTextView.setPadding(0, 0, 0, AndroidUtilities.dp(6.0F));
      this.descriptionTextView.setBackgroundDrawable((Drawable)null);
      EditTextBoldCursor var19 = this.descriptionTextView;
      if (LocaleController.isRTL) {
         var6 = var9;
      } else {
         var6 = 3;
      }

      var19.setGravity(var6);
      this.descriptionTextView.setInputType(180225);
      this.descriptionTextView.setImeOptions(6);
      this.descriptionTextView.setEnabled(ChatObject.canChangeChatInfo(this.currentChat));
      var19 = this.descriptionTextView;
      var19.setFocusable(var19.isEnabled());
      LengthFilter var21 = new LengthFilter(255);
      this.descriptionTextView.setFilters(new InputFilter[]{var21});
      this.descriptionTextView.setHint(LocaleController.getString("DescriptionOptionalPlaceholder", 2131559264));
      this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0F));
      this.descriptionTextView.setCursorWidth(1.5F);
      this.settingsContainer.addView(this.descriptionTextView, LayoutHelper.createLinear(-1, -2, 23.0F, 12.0F, 23.0F, 6.0F));
      this.descriptionTextView.setOnEditorActionListener(new _$$Lambda$ChatEditActivity$p1fZRHy8NDyNuO213khwXU229Jc(this));
      this.descriptionTextView.addTextChangedListener(new TextWatcher() {
         public void afterTextChanged(Editable var1) {
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }
      });
      this.settingsTopSectionCell = new ShadowSectionCell(var1);
      var13.addView(this.settingsTopSectionCell, LayoutHelper.createLinear(-1, -2));
      this.typeEditContainer = new LinearLayout(var1);
      this.typeEditContainer.setOrientation(1);
      this.typeEditContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      var13.addView(this.typeEditContainer, LayoutHelper.createLinear(-1, -2));
      TLRPC.ChatFull var23;
      if (this.currentChat.creator) {
         var23 = this.info;
         if (var23 == null || var23.can_set_username) {
            this.typeCell = new TextDetailCell(var1);
            this.typeCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView(this.typeCell, LayoutHelper.createLinear(-1, -2));
            this.typeCell.setOnClickListener(new _$$Lambda$ChatEditActivity$takgJ7d_dj5vza0E_4qO74BhrTA(this));
         }
      }

      if (ChatObject.isChannel(this.currentChat) && (this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 1) || !this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 0))) {
         this.linkedCell = new TextDetailCell(var1);
         this.linkedCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.typeEditContainer.addView(this.linkedCell, LayoutHelper.createLinear(-1, -2));
         this.linkedCell.setOnClickListener(new _$$Lambda$ChatEditActivity$vs7xjVOaqM3gt8vxvzKAx_LFF8w(this));
      }

      if (!this.isChannel && ChatObject.canBlockUsers(this.currentChat) && (ChatObject.isChannel(this.currentChat) || this.currentChat.creator)) {
         this.historyCell = new TextDetailCell(var1);
         this.historyCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.typeEditContainer.addView(this.historyCell, LayoutHelper.createLinear(-1, -2));
         this.historyCell.setOnClickListener(new _$$Lambda$ChatEditActivity$ZaLTd9UrDPsZkM9f0GkspKG3v50(this, var1));
      }

      if (this.isChannel) {
         this.signCell = new TextCheckCell(var1);
         this.signCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.signCell.setTextAndValueAndCheck(LocaleController.getString("ChannelSignMessages", 2131559001), LocaleController.getString("ChannelSignMessagesInfo", 2131559002), this.signMessages, true, false);
         this.typeEditContainer.addView(this.signCell, LayoutHelper.createFrame(-1, -2.0F));
         this.signCell.setOnClickListener(new _$$Lambda$ChatEditActivity$m_aFUQRAeXShPmT_g_6jI0sXdes(this));
      }

      ActionBarMenu var25 = super.actionBar.createMenu();
      if (ChatObject.canChangeChatInfo(this.currentChat) || this.signCell != null || this.historyCell != null) {
         this.doneButton = var25.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F));
         this.doneButton.setContentDescription(LocaleController.getString("Done", 2131559299));
      }

      if (this.signCell != null || this.historyCell != null || this.typeCell != null || this.linkedCell != null) {
         this.settingsSectionCell = new ShadowSectionCell(var1);
         var13.addView(this.settingsSectionCell, LayoutHelper.createLinear(-1, -2));
      }

      this.infoContainer = new LinearLayout(var1);
      this.infoContainer.setOrientation(1);
      this.infoContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
      var13.addView(this.infoContainer, LayoutHelper.createLinear(-1, -2));
      this.blockCell = new TextCell(var1);
      this.blockCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      TextCell var27 = this.blockCell;
      if (!ChatObject.isChannel(this.currentChat) && !this.currentChat.creator) {
         var6 = 8;
      } else {
         var6 = 0;
      }

      var27.setVisibility(var6);
      this.blockCell.setOnClickListener(new _$$Lambda$ChatEditActivity$Z_VSyPell_FXQ74xw_1QaAWQHLA(this));
      this.adminCell = new TextCell(var1);
      this.adminCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      this.adminCell.setOnClickListener(new _$$Lambda$ChatEditActivity$BW8nfxB2gbGLRBoiaMPR3BQCdjM(this));
      this.membersCell = new TextCell(var1);
      this.membersCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
      this.membersCell.setOnClickListener(new _$$Lambda$ChatEditActivity$R8nmnQgVpQwtOgAwCTLlWo2fY0k(this));
      if (ChatObject.isChannel(this.currentChat)) {
         this.logCell = new TextCell(var1);
         this.logCell.setTextAndIcon(LocaleController.getString("EventLog", 2131559382), 2131165405, false);
         this.logCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         this.logCell.setOnClickListener(new _$$Lambda$ChatEditActivity$__qhLtKqz7XJ52ia47Bo4pX1C7s(this));
      }

      if (!this.isChannel) {
         this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
      }

      this.infoContainer.addView(this.adminCell, LayoutHelper.createLinear(-1, -2));
      this.infoContainer.addView(this.membersCell, LayoutHelper.createLinear(-1, -2));
      if (this.isChannel) {
         this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
      }

      var27 = this.logCell;
      if (var27 != null) {
         this.infoContainer.addView(var27, LayoutHelper.createLinear(-1, -2));
      }

      this.infoSectionCell = new ShadowSectionCell(var1);
      var13.addView(this.infoSectionCell, LayoutHelper.createLinear(-1, -2));
      if (!ChatObject.hasAdminRights(this.currentChat)) {
         this.infoContainer.setVisibility(8);
         this.settingsTopSectionCell.setVisibility(8);
      }

      if (!this.isChannel) {
         var23 = this.info;
         if (var23 != null && var23.can_set_stickers) {
            this.stickersContainer = new FrameLayout(var1);
            this.stickersContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            var13.addView(this.stickersContainer, LayoutHelper.createLinear(-1, -2));
            this.stickersCell = new TextSettingsCell(var1);
            this.stickersCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.stickersCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.stickersContainer.addView(this.stickersCell, LayoutHelper.createFrame(-1, -2.0F));
            this.stickersCell.setOnClickListener(new _$$Lambda$ChatEditActivity$51Cw78hSbx5h61mWEeKV8Wy14wg(this));
            this.stickersInfoCell3 = new TextInfoPrivacyCell(var1);
            this.stickersInfoCell3.setText(LocaleController.getString("GroupStickersInfo", 2131559616));
            var13.addView(this.stickersInfoCell3, LayoutHelper.createLinear(-1, -2));
         }
      }

      if (this.currentChat.creator) {
         this.deleteContainer = new FrameLayout(var1);
         this.deleteContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         var13.addView(this.deleteContainer, LayoutHelper.createLinear(-1, -2));
         this.deleteCell = new TextSettingsCell(var1);
         this.deleteCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
         this.deleteCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
         if (this.isChannel) {
            this.deleteCell.setText(LocaleController.getString("ChannelDelete", 2131558943), false);
         } else if (this.currentChat.megagroup) {
            this.deleteCell.setText(LocaleController.getString("DeleteMega", 2131559248), false);
         } else {
            this.deleteCell.setText(LocaleController.getString("DeleteAndExitButton", 2131559235), false);
         }

         this.deleteContainer.addView(this.deleteCell, LayoutHelper.createFrame(-1, -2.0F));
         this.deleteCell.setOnClickListener(new _$$Lambda$ChatEditActivity$qBqi8ghp1hDeyx8_eISTzlbn7qQ(this));
         this.deleteInfoCell = new ShadowSectionCell(var1);
         this.deleteInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         var13.addView(this.deleteInfoCell, LayoutHelper.createLinear(-1, -2));
      } else if (!this.isChannel && this.stickersInfoCell3 == null) {
         this.infoSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
      }

      TextInfoPrivacyCell var14 = this.stickersInfoCell3;
      if (var14 != null) {
         if (this.deleteInfoCell == null) {
            var14.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165395, "windowBackgroundGrayShadow"));
         } else {
            var14.setBackgroundDrawable(Theme.getThemedDrawable(var1, 2131165394, "windowBackgroundGrayShadow"));
         }
      }

      this.nameTextView.setText(this.currentChat.title);
      EditTextEmoji var10 = this.nameTextView;
      var10.setSelection(var10.length());
      TLRPC.ChatFull var11 = this.info;
      if (var11 != null) {
         this.descriptionTextView.setText(var11.about);
      }

      TLRPC.Chat var15 = this.currentChat;
      TLRPC.ChatPhoto var12 = var15.photo;
      if (var12 != null) {
         this.avatar = var12.photo_small;
         this.avatarBig = var12.photo_big;
         this.avatarImage.setImage((ImageLocation)ImageLocation.getForChat(var15, false), "50_50", (Drawable)this.avatarDrawable, (Object)this.currentChat);
      } else {
         this.avatarImage.setImageDrawable(this.avatarDrawable);
      }

      this.updateFields(true);
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.chatInfoDidLoad) {
         TLRPC.ChatFull var6 = (TLRPC.ChatFull)var3[0];
         if (var6.id == this.chatId) {
            if (this.info == null) {
               EditTextBoldCursor var4 = this.descriptionTextView;
               if (var4 != null) {
                  var4.setText(var6.about);
               }
            }

            this.info = var6;
            boolean var5;
            if (ChatObject.isChannel(this.currentChat) && !this.info.hidden_prehistory) {
               var5 = false;
            } else {
               var5 = true;
            }

            this.historyHidden = var5;
            this.updateFields(false);
         }
      }

   }

   public void didUploadPhoto(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatEditActivity$_yvKcw5WJa5Pk6zwjtXTtTJaTb0(this, var1, var3, var2));
   }

   public String getInitialSearchString() {
      return this.nameTextView.getText().toString();
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs var1 = new _$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs(this);
      return new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(this.membersCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.membersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.membersCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.adminCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.adminCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.adminCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.blockCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.blockCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.blockCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.logCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.logCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.logCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.typeCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.typeCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.typeCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.historyCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.historyCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.historyCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputField"), new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteHintText"), new ThemeDescription(this.avatarContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.settingsContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.typeEditContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.deleteContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.stickersContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.infoContainer, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(this.settingsTopSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.settingsSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.deleteInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.infoSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.signCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.signCell, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.signCell, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.signCell, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.deleteCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.deleteCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText5"), new ThemeDescription(this.stickersCell, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"), new ThemeDescription(this.stickersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.stickersInfoCell3, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.stickersInfoCell3, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, var1, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink")};
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$18$ChatEditActivity(DialogInterface var1, int var2) {
      this.processDone();
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$19$ChatEditActivity(DialogInterface var1, int var2) {
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$10$ChatEditActivity(View var1) {
      Bundle var3 = new Bundle();
      var3.putInt("chat_id", this.chatId);
      byte var2;
      if (!this.isChannel) {
         var2 = 3;
      } else {
         var2 = 0;
      }

      var3.putInt("type", var2);
      ChatUsersActivity var4 = new ChatUsersActivity(var3);
      var4.setInfo(this.info);
      this.presentFragment(var4);
   }

   // $FF: synthetic method
   public void lambda$createView$11$ChatEditActivity(View var1) {
      Bundle var2 = new Bundle();
      var2.putInt("chat_id", this.chatId);
      var2.putInt("type", 1);
      ChatUsersActivity var3 = new ChatUsersActivity(var2);
      var3.setInfo(this.info);
      this.presentFragment(var3);
   }

   // $FF: synthetic method
   public void lambda$createView$12$ChatEditActivity(View var1) {
      Bundle var2 = new Bundle();
      var2.putInt("chat_id", this.chatId);
      var2.putInt("type", 2);
      ChatUsersActivity var3 = new ChatUsersActivity(var2);
      var3.setInfo(this.info);
      this.presentFragment(var3);
   }

   // $FF: synthetic method
   public void lambda$createView$13$ChatEditActivity(View var1) {
      this.presentFragment(new ChannelAdminLogActivity(this.currentChat));
   }

   // $FF: synthetic method
   public void lambda$createView$14$ChatEditActivity(View var1) {
      GroupStickersActivity var2 = new GroupStickersActivity(this.currentChat.id);
      var2.setInfo(this.info);
      this.presentFragment(var2);
   }

   // $FF: synthetic method
   public void lambda$createView$16$ChatEditActivity(View var1) {
      AlertsCreator.createClearOrDeleteDialogAlert(this, false, true, false, this.currentChat, (TLRPC.User)null, false, new _$$Lambda$ChatEditActivity$R2aORr4g6yH_oKe4WQtg_qOwYik(this));
   }

   // $FF: synthetic method
   public void lambda$createView$3$ChatEditActivity(View var1) {
      ImageUpdater var3 = this.imageUpdater;
      boolean var2;
      if (this.avatar != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.openMenu(var2, new _$$Lambda$ChatEditActivity$PhelyuCPHfVAtJ2gMmY7rzmsEtA(this));
   }

   // $FF: synthetic method
   public boolean lambda$createView$4$ChatEditActivity(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         View var4 = this.doneButton;
         if (var4 != null) {
            var4.performClick();
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$createView$5$ChatEditActivity(View var1) {
      ChatEditTypeActivity var2 = new ChatEditTypeActivity(this.chatId);
      var2.setInfo(this.info);
      this.presentFragment(var2);
   }

   // $FF: synthetic method
   public void lambda$createView$6$ChatEditActivity(View var1) {
      ChatLinkActivity var2 = new ChatLinkActivity(this.chatId);
      var2.setInfo(this.info);
      this.presentFragment(var2);
   }

   // $FF: synthetic method
   public void lambda$createView$8$ChatEditActivity(Context var1, View var2) {
      BottomSheet.Builder var7 = new BottomSheet.Builder(var1);
      var7.setApplyTopPadding(false);
      LinearLayout var3 = new LinearLayout(var1);
      var3.setOrientation(1);
      HeaderCell var4 = new HeaderCell(var1, true, 23, 15, false);
      var4.setHeight(47);
      var4.setText(LocaleController.getString("ChatHistory", 2131559033));
      var3.addView(var4);
      LinearLayout var5 = new LinearLayout(var1);
      var5.setOrientation(1);
      var3.addView(var5, LayoutHelper.createLinear(-1, -2));
      RadioButtonCell[] var8 = new RadioButtonCell[2];

      for(int var6 = 0; var6 < 2; ++var6) {
         var8[var6] = new RadioButtonCell(var1, true);
         var8[var6].setTag(var6);
         var8[var6].setBackgroundDrawable(Theme.getSelectorDrawable(false));
         if (var6 == 0) {
            var8[var6].setTextAndValue(LocaleController.getString("ChatHistoryVisible", 2131559037), LocaleController.getString("ChatHistoryVisibleInfo", 2131559038), true, this.historyHidden ^ true);
         } else if (ChatObject.isChannel(this.currentChat)) {
            var8[var6].setTextAndValue(LocaleController.getString("ChatHistoryHidden", 2131559034), LocaleController.getString("ChatHistoryHiddenInfo", 2131559035), false, this.historyHidden);
         } else {
            var8[var6].setTextAndValue(LocaleController.getString("ChatHistoryHidden", 2131559034), LocaleController.getString("ChatHistoryHiddenInfo2", 2131559036), false, this.historyHidden);
         }

         var5.addView(var8[var6], LayoutHelper.createLinear(-1, -2));
         var8[var6].setOnClickListener(new _$$Lambda$ChatEditActivity$Jz5JNMPNPO8bP_0CHPma3fBKfwA(this, var8, var7));
      }

      var7.setCustomView(var3);
      this.showDialog(var7.create());
   }

   // $FF: synthetic method
   public void lambda$createView$9$ChatEditActivity(View var1) {
      this.signMessages ^= true;
      ((TextCheckCell)var1).setChecked(this.signMessages);
   }

   // $FF: synthetic method
   public void lambda$didUploadPhoto$17$ChatEditActivity(TLRPC.InputFile var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      if (var1 != null) {
         this.uploadedAvatar = var1;
         if (this.createAfterUpload) {
            try {
               if (this.progressDialog != null && this.progressDialog.isShowing()) {
                  this.progressDialog.dismiss();
                  this.progressDialog = null;
               }
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }

            this.donePressed = false;
            this.doneButton.performClick();
         }

         this.showAvatarProgress(false, true);
      } else {
         this.avatar = var2.location;
         this.avatarBig = var3.location;
         this.avatarImage.setImage((ImageLocation)ImageLocation.getForLocal(this.avatar), "50_50", (Drawable)this.avatarDrawable, (Object)this.currentChat);
         this.showAvatarProgress(true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$22$ChatEditActivity() {
      if (this.avatarImage != null) {
         this.avatarDrawable.setInfo(5, (String)null, (String)null, false);
         this.avatarImage.invalidate();
      }

   }

   // $FF: synthetic method
   public void lambda$null$15$ChatEditActivity(boolean var1) {
      if (AndroidUtilities.isTablet()) {
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, -((long)this.chatId));
      } else {
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
      }

      MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chatId, MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId()), this.info, true, false);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$null$2$ChatEditActivity() {
      this.avatar = null;
      this.avatarBig = null;
      this.uploadedAvatar = null;
      this.showAvatarProgress(false, true);
      this.avatarImage.setImage((ImageLocation)null, (String)null, (Drawable)this.avatarDrawable, (Object)this.currentChat);
   }

   // $FF: synthetic method
   public void lambda$null$7$ChatEditActivity(RadioButtonCell[] var1, BottomSheet.Builder var2, View var3) {
      Integer var8 = (Integer)var3.getTag();
      boolean var4 = false;
      RadioButtonCell var5 = var1[0];
      boolean var6;
      if (var8 == 0) {
         var6 = true;
      } else {
         var6 = false;
      }

      var5.setChecked(var6, true);
      RadioButtonCell var7 = var1[1];
      if (var8 == 1) {
         var6 = true;
      } else {
         var6 = false;
      }

      var7.setChecked(var6, true);
      var6 = var4;
      if (var8 == 1) {
         var6 = true;
      }

      this.historyHidden = var6;
      var2.getDismissRunnable().run();
      this.updateFields(true);
   }

   // $FF: synthetic method
   public void lambda$onFragmentCreate$0$ChatEditActivity(CountDownLatch var1) {
      this.currentChat = MessagesStorage.getInstance(super.currentAccount).getChat(this.chatId);
      var1.countDown();
   }

   // $FF: synthetic method
   public void lambda$processDone$20$ChatEditActivity(int var1) {
      this.chatId = var1;
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(var1);
      this.donePressed = false;
      TLRPC.ChatFull var2 = this.info;
      if (var2 != null) {
         var2.hidden_prehistory = true;
      }

      this.processDone();
   }

   // $FF: synthetic method
   public void lambda$processDone$21$ChatEditActivity(DialogInterface var1) {
      this.createAfterUpload = false;
      this.progressDialog = null;
      this.donePressed = false;
   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      this.imageUpdater.onActivityResult(var1, var2, var3);
   }

   public boolean onBackPressed() {
      EditTextEmoji var1 = this.nameTextView;
      if (var1 != null && var1.isPopupShowing()) {
         this.nameTextView.hidePopup(true);
         return false;
      } else {
         return this.checkDiscard();
      }
   }

   public boolean onFragmentCreate() {
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
      TLRPC.Chat var1 = this.currentChat;
      boolean var2 = true;
      if (var1 == null) {
         label48: {
            CountDownLatch var3 = new CountDownLatch(1);
            MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$ChatEditActivity$j_VWblaHSOc0ptEwu8DVX6LNsH0(this, var3));

            try {
               var3.await();
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
            }

            if (this.currentChat != null) {
               MessagesController.getInstance(super.currentAccount).putChat(this.currentChat, true);
               if (this.info != null) {
                  break label48;
               }

               MessagesStorage.getInstance(super.currentAccount).loadChatInfo(this.chatId, var3, false, false);

               try {
                  var3.await();
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }

               if (this.info != null) {
                  break label48;
               }
            }

            return false;
         }
      }

      if (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup) {
         var2 = false;
      }

      this.isChannel = var2;
      ImageUpdater var6 = this.imageUpdater;
      var6.parentFragment = this;
      var6.delegate = this;
      this.signMessages = this.currentChat.signatures;
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      ImageUpdater var1 = this.imageUpdater;
      if (var1 != null) {
         var1.clear();
      }

      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
      EditTextEmoji var2 = this.nameTextView;
      if (var2 != null) {
         var2.onDestroy();
      }

   }

   public void onPause() {
      super.onPause();
      EditTextEmoji var1 = this.nameTextView;
      if (var1 != null) {
         var1.onPause();
      }

   }

   public void onResume() {
      super.onResume();
      EditTextEmoji var1 = this.nameTextView;
      if (var1 != null) {
         var1.onResume();
         this.nameTextView.getEditText().requestFocus();
      }

      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
      this.updateFields(true);
   }

   public void restoreSelfArgs(Bundle var1) {
      ImageUpdater var2 = this.imageUpdater;
      if (var2 != null) {
         var2.currentPicturePath = var1.getString("path");
      }

   }

   public void saveSelfArgs(Bundle var1) {
      ImageUpdater var2 = this.imageUpdater;
      String var3;
      if (var2 != null) {
         var3 = var2.currentPicturePath;
         if (var3 != null) {
            var1.putString("path", var3);
         }
      }

      EditTextEmoji var4 = this.nameTextView;
      if (var4 != null) {
         var3 = var4.getText().toString();
         if (var3 != null && var3.length() != 0) {
            var1.putString("nameTextView", var3);
         }
      }

   }

   public void setInfo(TLRPC.ChatFull var1) {
      this.info = var1;
      if (var1 != null) {
         if (this.currentChat == null) {
            this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
         }

         boolean var2;
         if (ChatObject.isChannel(this.currentChat) && !this.info.hidden_prehistory) {
            var2 = false;
         } else {
            var2 = true;
         }

         this.historyHidden = var2;
      }

   }
}
