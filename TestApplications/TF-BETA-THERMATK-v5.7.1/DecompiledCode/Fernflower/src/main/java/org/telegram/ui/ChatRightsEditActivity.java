package org.telegram.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.DialogRadioCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells2.UserCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class ChatRightsEditActivity extends BaseFragment {
   public static final int TYPE_ADMIN = 0;
   public static final int TYPE_BANNED = 1;
   private static final int done_button = 1;
   private int addAdminsRow;
   private int addUsersRow;
   private TLRPC.TL_chatAdminRights adminRights;
   private int banUsersRow;
   private TLRPC.TL_chatBannedRights bannedRights;
   private boolean canEdit;
   private int cantEditInfoRow;
   private int changeInfoRow;
   private int chatId;
   private String currentBannedRights = "";
   private TLRPC.Chat currentChat;
   private int currentType;
   private TLRPC.User currentUser;
   private TLRPC.TL_chatBannedRights defaultBannedRights;
   private ChatRightsEditActivity.ChatRightsEditActivityDelegate delegate;
   private int deleteMessagesRow;
   private int editMesagesRow;
   private int embedLinksRow;
   private boolean isAddingNew;
   private boolean isChannel;
   private RecyclerListView listView;
   private ChatRightsEditActivity.ListAdapter listViewAdapter;
   private TLRPC.TL_chatAdminRights myAdminRights;
   private int pinMessagesRow;
   private int postMessagesRow;
   private int removeAdminRow;
   private int removeAdminShadowRow;
   private int rightsShadowRow;
   private int rowCount;
   private int sendMediaRow;
   private int sendMessagesRow;
   private int sendPollsRow;
   private int sendStickersRow;
   private int untilDateRow;
   private int untilSectionRow;

   public ChatRightsEditActivity(int var1, int var2, TLRPC.TL_chatAdminRights var3, TLRPC.TL_chatBannedRights var4, TLRPC.TL_chatBannedRights var5, int var6, boolean var7, boolean var8) {
      this.isAddingNew = var8;
      this.chatId = var2;
      this.currentUser = MessagesController.getInstance(super.currentAccount).getUser(var1);
      this.currentType = var6;
      this.canEdit = var7;
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
      TLRPC.Chat var9 = this.currentChat;
      boolean var10 = false;
      if (var9 != null) {
         if (ChatObject.isChannel(var9) && !this.currentChat.megagroup) {
            var7 = true;
         } else {
            var7 = false;
         }

         this.isChannel = var7;
         this.myAdminRights = this.currentChat.admin_rights;
      }

      if (this.myAdminRights == null) {
         this.myAdminRights = new TLRPC.TL_chatAdminRights();
         TLRPC.TL_chatAdminRights var13 = this.myAdminRights;
         var13.add_admins = true;
         var13.pin_messages = true;
         var13.invite_users = true;
         var13.ban_users = true;
         var13.delete_messages = true;
         var13.edit_messages = true;
         var13.post_messages = true;
         var13.change_info = true;
      }

      label126: {
         if (var6 == 0) {
            this.adminRights = new TLRPC.TL_chatAdminRights();
            TLRPC.TL_chatAdminRights var12;
            if (var3 == null) {
               var12 = this.adminRights;
               var3 = this.myAdminRights;
               var12.change_info = var3.change_info;
               var12.post_messages = var3.post_messages;
               var12.edit_messages = var3.edit_messages;
               var12.delete_messages = var3.delete_messages;
               var12.ban_users = var3.ban_users;
               var12.invite_users = var3.invite_users;
               var12.pin_messages = var3.pin_messages;
               break label126;
            }

            var12 = this.adminRights;
            var12.change_info = var3.change_info;
            var12.post_messages = var3.post_messages;
            var12.edit_messages = var3.edit_messages;
            var12.delete_messages = var3.delete_messages;
            var12.ban_users = var3.ban_users;
            var12.invite_users = var3.invite_users;
            var12.pin_messages = var3.pin_messages;
            var12.add_admins = var3.add_admins;
            if (!var12.change_info && !var12.post_messages && !var12.edit_messages && !var12.delete_messages && !var12.ban_users && !var12.invite_users && !var12.pin_messages && !var12.add_admins) {
               break label126;
            }
         } else {
            this.defaultBannedRights = var4;
            TLRPC.TL_chatBannedRights var11;
            if (this.defaultBannedRights == null) {
               this.defaultBannedRights = new TLRPC.TL_chatBannedRights();
               var11 = this.defaultBannedRights;
               var11.pin_messages = false;
               var11.change_info = false;
               var11.invite_users = false;
               var11.send_polls = false;
               var11.send_inline = false;
               var11.send_games = false;
               var11.send_gifs = false;
               var11.send_stickers = false;
               var11.embed_links = false;
               var11.send_messages = false;
               var11.send_media = false;
               var11.view_messages = false;
            }

            this.bannedRights = new TLRPC.TL_chatBannedRights();
            if (var5 == null) {
               var11 = this.bannedRights;
               var11.pin_messages = false;
               var11.change_info = false;
               var11.invite_users = false;
               var11.send_polls = false;
               var11.send_inline = false;
               var11.send_games = false;
               var11.send_gifs = false;
               var11.send_stickers = false;
               var11.embed_links = false;
               var11.send_messages = false;
               var11.send_media = false;
               var11.view_messages = false;
            } else {
               var11 = this.bannedRights;
               var11.view_messages = var5.view_messages;
               var11.send_messages = var5.send_messages;
               var11.send_media = var5.send_media;
               var11.send_stickers = var5.send_stickers;
               var11.send_gifs = var5.send_gifs;
               var11.send_games = var5.send_games;
               var11.send_inline = var5.send_inline;
               var11.embed_links = var5.embed_links;
               var11.send_polls = var5.send_polls;
               var11.invite_users = var5.invite_users;
               var11.change_info = var5.change_info;
               var11.pin_messages = var5.pin_messages;
               var11.until_date = var5.until_date;
            }

            if (this.defaultBannedRights.view_messages) {
               this.bannedRights.view_messages = true;
            }

            if (this.defaultBannedRights.send_messages) {
               this.bannedRights.send_messages = true;
            }

            if (this.defaultBannedRights.send_media) {
               this.bannedRights.send_media = true;
            }

            if (this.defaultBannedRights.send_stickers) {
               this.bannedRights.send_stickers = true;
            }

            if (this.defaultBannedRights.send_gifs) {
               this.bannedRights.send_gifs = true;
            }

            if (this.defaultBannedRights.send_games) {
               this.bannedRights.send_games = true;
            }

            if (this.defaultBannedRights.send_inline) {
               this.bannedRights.send_inline = true;
            }

            if (this.defaultBannedRights.embed_links) {
               this.bannedRights.embed_links = true;
            }

            if (this.defaultBannedRights.send_polls) {
               this.bannedRights.send_polls = true;
            }

            if (this.defaultBannedRights.invite_users) {
               this.bannedRights.invite_users = true;
            }

            if (this.defaultBannedRights.change_info) {
               this.bannedRights.change_info = true;
            }

            if (this.defaultBannedRights.pin_messages) {
               this.bannedRights.pin_messages = true;
            }

            this.currentBannedRights = ChatObject.getBannedRightsString(this.bannedRights);
            if (var5 != null && var5.view_messages) {
               break label126;
            }
         }

         var10 = true;
      }

      this.rowCount += 3;
      if (var6 == 0) {
         if (this.isChannel) {
            var2 = this.rowCount++;
            this.changeInfoRow = var2;
            var2 = this.rowCount++;
            this.postMessagesRow = var2;
            var2 = this.rowCount++;
            this.editMesagesRow = var2;
            var2 = this.rowCount++;
            this.deleteMessagesRow = var2;
            var2 = this.rowCount++;
            this.addUsersRow = var2;
            var2 = this.rowCount++;
            this.addAdminsRow = var2;
         } else {
            var2 = this.rowCount++;
            this.changeInfoRow = var2;
            var2 = this.rowCount++;
            this.deleteMessagesRow = var2;
            var2 = this.rowCount++;
            this.banUsersRow = var2;
            var2 = this.rowCount++;
            this.addUsersRow = var2;
            var2 = this.rowCount++;
            this.pinMessagesRow = var2;
            var2 = this.rowCount++;
            this.addAdminsRow = var2;
         }
      } else if (var6 == 1) {
         var2 = this.rowCount++;
         this.sendMessagesRow = var2;
         var2 = this.rowCount++;
         this.sendMediaRow = var2;
         var2 = this.rowCount++;
         this.sendStickersRow = var2;
         var2 = this.rowCount++;
         this.sendPollsRow = var2;
         var2 = this.rowCount++;
         this.embedLinksRow = var2;
         var2 = this.rowCount++;
         this.addUsersRow = var2;
         var2 = this.rowCount++;
         this.pinMessagesRow = var2;
         var2 = this.rowCount++;
         this.changeInfoRow = var2;
         var2 = this.rowCount++;
         this.untilSectionRow = var2;
         var2 = this.rowCount++;
         this.untilDateRow = var2;
      }

      if (this.canEdit && var10) {
         var1 = this.rowCount++;
         this.rightsShadowRow = var1;
         var1 = this.rowCount++;
         this.removeAdminRow = var1;
         var1 = this.rowCount++;
         this.removeAdminShadowRow = var1;
         this.cantEditInfoRow = -1;
      } else {
         this.removeAdminRow = -1;
         this.removeAdminShadowRow = -1;
         if (var6 == 0 && !this.canEdit) {
            this.rightsShadowRow = -1;
            var1 = this.rowCount++;
            this.cantEditInfoRow = var1;
         } else {
            var1 = this.rowCount++;
            this.rightsShadowRow = var1;
         }
      }

   }

   private boolean checkDiscard() {
      if (this.currentType != 1) {
         return true;
      } else {
         String var1 = ChatObject.getBannedRightsString(this.bannedRights);
         if (!this.currentBannedRights.equals(var1)) {
            AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
            var2.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131560995));
            var2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("UserRestrictionsApplyChangesText", 2131560996, MessagesController.getInstance(super.currentAccount).getChat(this.chatId).title)));
            var2.setPositiveButton(LocaleController.getString("ApplyTheme", 2131558639), new _$$Lambda$ChatRightsEditActivity$H_3StnNI8nSWDF0VmiZshr2iV_s(this));
            var2.setNegativeButton(LocaleController.getString("PassportDiscard", 2131560212), new _$$Lambda$ChatRightsEditActivity$JCX77xMHDnfbaKObjN8bjfUr2po(this));
            this.showDialog(var2.create());
            return false;
         } else {
            return true;
         }
      }
   }

   private boolean isDefaultAdminRights() {
      TLRPC.TL_chatAdminRights var1 = this.adminRights;
      boolean var2;
      if (!var1.change_info || !var1.delete_messages || !var1.ban_users || !var1.invite_users || !var1.pin_messages || var1.add_admins) {
         var1 = this.adminRights;
         if (var1.change_info || var1.delete_messages || var1.ban_users || var1.invite_users || var1.pin_messages || var1.add_admins) {
            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   // $FF: synthetic method
   static void lambda$null$1(DialogInterface var0, int var1) {
   }

   // $FF: synthetic method
   static void lambda$null$3(DialogInterface var0, int var1) {
   }

   // $FF: synthetic method
   static void lambda$null$4(DatePicker var0, DialogInterface var1) {
      int var2 = var0.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         View var5 = var0.getChildAt(var3);
         LayoutParams var4 = var5.getLayoutParams();
         var4.width = -1;
         var5.setLayoutParams(var4);
      }

   }

   private void onDonePressed() {
      boolean var1 = ChatObject.isChannel(this.currentChat);
      byte var2 = 1;
      byte var3 = 1;
      int var4;
      if (!var1) {
         var4 = this.currentType;
         if (var4 == 1 || var4 == 0 && !this.isDefaultAdminRights()) {
            MessagesController.getInstance(super.currentAccount).convertToMegaGroup(this.getParentActivity(), this.chatId, new _$$Lambda$ChatRightsEditActivity$hXfQmiw2gnL9RhXwZou1PDR6kt8(this));
            return;
         }
      }

      var4 = this.currentType;
      byte var7;
      if (var4 == 0) {
         TLRPC.TL_chatAdminRights var5;
         if (this.isChannel) {
            var5 = this.adminRights;
            var5.ban_users = false;
            var5.pin_messages = false;
         } else {
            var5 = this.adminRights;
            var5.edit_messages = false;
            var5.post_messages = false;
         }

         MessagesController.getInstance(super.currentAccount).setUserAdminRole(this.chatId, this.currentUser, this.adminRights, this.isChannel, this.getFragmentForAlert(1), this.isAddingNew);
         ChatRightsEditActivity.ChatRightsEditActivityDelegate var6 = this.delegate;
         if (var6 != null) {
            var5 = this.adminRights;
            var7 = var3;
            if (!var5.change_info) {
               var7 = var3;
               if (!var5.post_messages) {
                  var7 = var3;
                  if (!var5.edit_messages) {
                     var7 = var3;
                     if (!var5.delete_messages) {
                        var7 = var3;
                        if (!var5.ban_users) {
                           var7 = var3;
                           if (!var5.invite_users) {
                              var7 = var3;
                              if (!var5.pin_messages) {
                                 if (var5.add_admins) {
                                    var7 = var3;
                                 } else {
                                    var7 = 0;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            var6.didSetRights(var7, this.adminRights, this.bannedRights);
         }
      } else if (var4 == 1) {
         MessagesController.getInstance(super.currentAccount).setUserBannedRole(this.chatId, this.currentUser, this.bannedRights, this.isChannel, this.getFragmentForAlert(1));
         TLRPC.TL_chatBannedRights var8 = this.bannedRights;
         var7 = var2;
         if (!var8.send_messages) {
            var7 = var2;
            if (!var8.send_stickers) {
               var7 = var2;
               if (!var8.embed_links) {
                  var7 = var2;
                  if (!var8.send_media) {
                     var7 = var2;
                     if (!var8.send_gifs) {
                        var7 = var2;
                        if (!var8.send_games) {
                           if (var8.send_inline) {
                              var7 = var2;
                           } else {
                              var8.until_date = 0;
                              var7 = 2;
                           }
                        }
                     }
                  }
               }
            }
         }

         ChatRightsEditActivity.ChatRightsEditActivityDelegate var9 = this.delegate;
         if (var9 != null) {
            var9.didSetRights(var7, this.adminRights, this.bannedRights);
         }
      }

      this.finishFragment();
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      ActionBar var2 = super.actionBar;
      byte var3 = 1;
      var2.setAllowOverlayTitle(true);
      if (this.currentType == 0) {
         super.actionBar.setTitle(LocaleController.getString("EditAdmin", 2131559302));
      } else {
         super.actionBar.setTitle(LocaleController.getString("UserRestrictions", 2131560994));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               if (ChatRightsEditActivity.this.checkDiscard()) {
                  ChatRightsEditActivity.this.finishFragment();
               }
            } else if (var1 == 1) {
               ChatRightsEditActivity.this.onDonePressed();
            }

         }
      });
      if (this.canEdit) {
         super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F), LocaleController.getString("Done", 2131559299));
      }

      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var6 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1);
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      this.listView.setLayoutManager(var4);
      RecyclerListView var7 = this.listView;
      ChatRightsEditActivity.ListAdapter var5 = new ChatRightsEditActivity.ListAdapter(var1);
      this.listViewAdapter = var5;
      var7.setAdapter(var5);
      var7 = this.listView;
      if (!LocaleController.isRTL) {
         var3 = 2;
      }

      var7.setVerticalScrollbarPosition(var3);
      var6.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ChatRightsEditActivity$aMVVQ8fOnltbn3fHM_guwcH1dhE(this, var1)));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8 var1 = new _$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8(this);
      ThemeDescription var2 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{UserCell.class, TextSettingsCell.class, TextCheckCell2.class, HeaderCell.class, TextDetailCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var10 = this.listView;
      Paint var11 = Theme.dividerPaint;
      ThemeDescription var31 = new ThemeDescription(var10, 0, new Class[]{View.class}, var11, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var12 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var32 = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4");
      ThemeDescription var13 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText5");
      ThemeDescription var14 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var15 = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText");
      ThemeDescription var16 = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon");
      ThemeDescription var17 = new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var18 = new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2");
      ThemeDescription var19 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var20 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2");
      ThemeDescription var21 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switch2Track");
      ThemeDescription var22 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switch2TrackChecked");
      ThemeDescription var23 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var24 = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader");
      ThemeDescription var25 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var26 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var27 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var28 = this.listView;
      Drawable var29 = Theme.avatar_broadcastDrawable;
      Drawable var30 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var31, var12, var32, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22, var23, var24, var25, var26, var27, new ThemeDescription(var28, 0, new Class[]{UserCell.class}, (Paint)null, new Drawable[]{var29, var30}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription((View)null, 0, new Class[]{DialogRadioCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextBlack"), new ThemeDescription((View)null, 0, new Class[]{DialogRadioCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogTextGray2"), new ThemeDescription((View)null, ThemeDescription.FLAG_CHECKBOX, new Class[]{DialogRadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogRadioBackground"), new ThemeDescription((View)null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{DialogRadioCell.class}, new String[]{"radioButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "dialogRadioBackgroundChecked")};
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$8$ChatRightsEditActivity(DialogInterface var1, int var2) {
      this.onDonePressed();
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$9$ChatRightsEditActivity(DialogInterface var1, int var2) {
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$6$ChatRightsEditActivity(Context var1, View var2, int var3) {
      if (this.canEdit) {
         if (var3 == 0) {
            Bundle var10 = new Bundle();
            var10.putInt("user_id", this.currentUser.id);
            this.presentFragment(new ProfileActivity(var10));
         } else {
            TLRPC.TL_chatBannedRights var11;
            if (var3 == this.removeAdminRow) {
               var3 = this.currentType;
               if (var3 == 0) {
                  MessagesController.getInstance(super.currentAccount).setUserAdminRole(this.chatId, this.currentUser, new TLRPC.TL_chatAdminRights(), this.isChannel, this.getFragmentForAlert(0), this.isAddingNew);
               } else if (var3 == 1) {
                  this.bannedRights = new TLRPC.TL_chatBannedRights();
                  var11 = this.bannedRights;
                  var11.view_messages = true;
                  var11.send_media = true;
                  var11.send_messages = true;
                  var11.send_stickers = true;
                  var11.send_gifs = true;
                  var11.send_games = true;
                  var11.send_inline = true;
                  var11.embed_links = true;
                  var11.pin_messages = true;
                  var11.send_polls = true;
                  var11.invite_users = true;
                  var11.change_info = true;
                  var11.until_date = 0;
                  MessagesController.getInstance(super.currentAccount).setUserBannedRole(this.chatId, this.currentUser, this.bannedRights, this.isChannel, this.getFragmentForAlert(0));
               }

               ChatRightsEditActivity.ChatRightsEditActivityDelegate var12 = this.delegate;
               if (var12 != null) {
                  var12.didSetRights(0, this.adminRights, this.bannedRights);
               }

               this.finishFragment();
            } else if (var3 == this.untilDateRow) {
               if (this.getParentActivity() == null) {
                  return;
               }

               BottomSheet.Builder var4 = new BottomSheet.Builder(var1);
               var4.setApplyTopPadding(false);
               LinearLayout var5 = new LinearLayout(var1);
               var5.setOrientation(1);
               HeaderCell var14 = new HeaderCell(var1, true, 23, 15, false);
               var14.setHeight(47);
               var14.setText(LocaleController.getString("UserRestrictionsDuration", 2131561001));
               var5.addView(var14);
               LinearLayout var6 = new LinearLayout(var1);
               var6.setOrientation(1);
               var5.addView(var6, LayoutHelper.createLinear(-1, -2));
               BottomSheet.BottomSheetCell[] var7 = new BottomSheet.BottomSheetCell[5];

               for(var3 = 0; var3 < var7.length; ++var3) {
                  var7[var3] = new BottomSheet.BottomSheetCell(var1, 0);
                  var7[var3].setPadding(AndroidUtilities.dp(23.0F), 0, AndroidUtilities.dp(23.0F), 0);
                  var7[var3].setTag(var3);
                  var7[var3].setBackgroundDrawable(Theme.getSelectorDrawable(false));
                  String var15;
                  if (var3 != 0) {
                     if (var3 != 1) {
                        if (var3 != 2) {
                           if (var3 != 3) {
                              var15 = LocaleController.getString("NotificationsCustom", 2131560059);
                           } else {
                              var15 = LocaleController.formatPluralString("Months", 1);
                           }
                        } else {
                           var15 = LocaleController.formatPluralString("Weeks", 1);
                        }
                     } else {
                        var15 = LocaleController.formatPluralString("Days", 1);
                     }
                  } else {
                     var15 = LocaleController.getString("UserRestrictionsUntilForever", 2131561019);
                  }

                  var7[var3].setTextAndIcon(var15, 0);
                  var6.addView(var7[var3], LayoutHelper.createLinear(-1, -2));
                  var7[var3].setOnClickListener(new _$$Lambda$ChatRightsEditActivity$CgALXvrSg_HbFAY5rAsHrlvl1wY(this, var4));
               }

               var4.setCustomView(var5);
               this.showDialog(var4.create());
            } else if (var2 instanceof TextCheckCell2) {
               TextCheckCell2 var13 = (TextCheckCell2)var2;
               if (var13.hasIcon()) {
                  Toast.makeText(this.getParentActivity(), LocaleController.getString("UserRestrictionsDisabled", 2131561000), 0).show();
                  return;
               }

               if (!var13.isEnabled()) {
                  return;
               }

               var13.setChecked(var13.isChecked() ^ true);
               TLRPC.TL_chatAdminRights var16;
               if (var3 == this.changeInfoRow) {
                  if (this.currentType == 0) {
                     var16 = this.adminRights;
                     var16.change_info ^= true;
                  } else {
                     var11 = this.bannedRights;
                     var11.change_info ^= true;
                  }
               } else if (var3 == this.postMessagesRow) {
                  var16 = this.adminRights;
                  var16.post_messages ^= true;
               } else if (var3 == this.editMesagesRow) {
                  var16 = this.adminRights;
                  var16.edit_messages ^= true;
               } else if (var3 == this.deleteMessagesRow) {
                  var16 = this.adminRights;
                  var16.delete_messages ^= true;
               } else if (var3 == this.addAdminsRow) {
                  var16 = this.adminRights;
                  var16.add_admins ^= true;
               } else if (var3 == this.banUsersRow) {
                  var16 = this.adminRights;
                  var16.ban_users ^= true;
               } else if (var3 == this.addUsersRow) {
                  if (this.currentType == 0) {
                     var16 = this.adminRights;
                     var16.invite_users ^= true;
                  } else {
                     var11 = this.bannedRights;
                     var11.invite_users ^= true;
                  }
               } else if (var3 == this.pinMessagesRow) {
                  if (this.currentType == 0) {
                     var16 = this.adminRights;
                     var16.pin_messages ^= true;
                  } else {
                     var11 = this.bannedRights;
                     var11.pin_messages ^= true;
                  }
               } else if (this.bannedRights != null) {
                  boolean var8 = var13.isChecked();
                  if (var3 == this.sendMessagesRow) {
                     var11 = this.bannedRights;
                     var11.send_messages ^= true;
                  } else if (var3 == this.sendMediaRow) {
                     var11 = this.bannedRights;
                     var11.send_media ^= true;
                  } else if (var3 == this.sendStickersRow) {
                     var11 = this.bannedRights;
                     boolean var9 = var11.send_stickers ^ true;
                     var11.send_inline = var9;
                     var11.send_gifs = var9;
                     var11.send_games = var9;
                     var11.send_stickers = var9;
                  } else if (var3 == this.embedLinksRow) {
                     var11 = this.bannedRights;
                     var11.embed_links ^= true;
                  } else if (var3 == this.sendPollsRow) {
                     var11 = this.bannedRights;
                     var11.send_polls ^= true;
                  }

                  RecyclerView.ViewHolder var17;
                  if (var8 ^ true) {
                     var11 = this.bannedRights;
                     if (var11.view_messages && !var11.send_messages) {
                        var11.send_messages = true;
                        var17 = this.listView.findViewHolderForAdapterPosition(this.sendMessagesRow);
                        if (var17 != null) {
                           ((TextCheckCell2)var17.itemView).setChecked(false);
                        }
                     }

                     var11 = this.bannedRights;
                     if (var11.view_messages || var11.send_messages) {
                        var11 = this.bannedRights;
                        if (!var11.send_media) {
                           var11.send_media = true;
                           var17 = this.listView.findViewHolderForAdapterPosition(this.sendMediaRow);
                           if (var17 != null) {
                              ((TextCheckCell2)var17.itemView).setChecked(false);
                           }
                        }
                     }

                     var11 = this.bannedRights;
                     if (var11.view_messages || var11.send_messages) {
                        var11 = this.bannedRights;
                        if (!var11.send_polls) {
                           var11.send_polls = true;
                           var17 = this.listView.findViewHolderForAdapterPosition(this.sendPollsRow);
                           if (var17 != null) {
                              ((TextCheckCell2)var17.itemView).setChecked(false);
                           }
                        }
                     }

                     var11 = this.bannedRights;
                     if (var11.view_messages || var11.send_messages) {
                        var11 = this.bannedRights;
                        if (!var11.send_stickers) {
                           var11.send_inline = true;
                           var11.send_gifs = true;
                           var11.send_games = true;
                           var11.send_stickers = true;
                           var17 = this.listView.findViewHolderForAdapterPosition(this.sendStickersRow);
                           if (var17 != null) {
                              ((TextCheckCell2)var17.itemView).setChecked(false);
                           }
                        }
                     }

                     var11 = this.bannedRights;
                     if (var11.view_messages || var11.send_messages) {
                        var11 = this.bannedRights;
                        if (!var11.embed_links) {
                           var11.embed_links = true;
                           var17 = this.listView.findViewHolderForAdapterPosition(this.embedLinksRow);
                           if (var17 != null) {
                              ((TextCheckCell2)var17.itemView).setChecked(false);
                           }
                        }
                     }
                  } else {
                     var11 = this.bannedRights;
                     if (!var11.send_messages || !var11.embed_links || !var11.send_inline || !var11.send_media || !var11.send_polls) {
                        var11 = this.bannedRights;
                        if (var11.view_messages) {
                           var11.view_messages = false;
                        }
                     }

                     var11 = this.bannedRights;
                     if (!var11.embed_links || !var11.send_inline || !var11.send_media || !var11.send_polls) {
                        var11 = this.bannedRights;
                        if (var11.send_messages) {
                           var11.send_messages = false;
                           var17 = this.listView.findViewHolderForAdapterPosition(this.sendMessagesRow);
                           if (var17 != null) {
                              ((TextCheckCell2)var17.itemView).setChecked(true);
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$10$ChatRightsEditActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof UserCell) {
               ((UserCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$ChatRightsEditActivity(int var1, TimePicker var2, int var3, int var4) {
      this.bannedRights.until_date = var1 + var3 * 3600 + var4 * 60;
      this.listViewAdapter.notifyItemChanged(this.untilDateRow);
   }

   // $FF: synthetic method
   public void lambda$null$2$ChatRightsEditActivity(DatePicker var1, int var2, int var3, int var4) {
      Calendar var8 = Calendar.getInstance();
      var8.clear();
      var8.set(var2, var3, var4);
      var2 = (int)(var8.getTime().getTime() / 1000L);

      try {
         Activity var5 = this.getParentActivity();
         _$$Lambda$ChatRightsEditActivity$DIaVrokvmQgt4iHoxcLzGLzsq3k var6 = new _$$Lambda$ChatRightsEditActivity$DIaVrokvmQgt4iHoxcLzGLzsq3k(this, var2);
         TimePickerDialog var9 = new TimePickerDialog(var5, var6, 0, 0, true);
         var9.setButton(-1, LocaleController.getString("Set", 2131560727), var9);
         var9.setButton(-2, LocaleController.getString("Cancel", 2131558891), _$$Lambda$ChatRightsEditActivity$JCoxEgngqfok3q7QQt4m73nFirw.INSTANCE);
         this.showDialog(var9);
      } catch (Exception var7) {
         FileLog.e((Throwable)var7);
      }

   }

   // $FF: synthetic method
   public void lambda$null$5$ChatRightsEditActivity(BottomSheet.Builder var1, View var2) {
      int var3 = (Integer)var2.getTag();
      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  if (var3 == 4) {
                     Calendar var9 = Calendar.getInstance();
                     int var4 = var9.get(1);
                     var3 = var9.get(2);
                     int var5 = var9.get(5);

                     try {
                        Activity var6 = this.getParentActivity();
                        _$$Lambda$ChatRightsEditActivity$6G9ZDTyeTzElkJks0Z6TcSqzhXs var7 = new _$$Lambda$ChatRightsEditActivity$6G9ZDTyeTzElkJks0Z6TcSqzhXs(this);
                        DatePickerDialog var10 = new DatePickerDialog(var6, var7, var4, var3, var5);
                        DatePicker var13 = var10.getDatePicker();
                        Calendar var11 = Calendar.getInstance();
                        var11.setTimeInMillis(System.currentTimeMillis());
                        var11.set(11, var11.getMinimum(11));
                        var11.set(12, var11.getMinimum(12));
                        var11.set(13, var11.getMinimum(13));
                        var11.set(14, var11.getMinimum(14));
                        var13.setMinDate(var11.getTimeInMillis());
                        var11.setTimeInMillis(System.currentTimeMillis() + 31536000000L);
                        var11.set(11, var11.getMaximum(11));
                        var11.set(12, var11.getMaximum(12));
                        var11.set(13, var11.getMaximum(13));
                        var11.set(14, var11.getMaximum(14));
                        var13.setMaxDate(var11.getTimeInMillis());
                        var10.setButton(-1, LocaleController.getString("Set", 2131560727), var10);
                        var10.setButton(-2, LocaleController.getString("Cancel", 2131558891), _$$Lambda$ChatRightsEditActivity$nJ41ofgPd7pnNhbrTqNHlhWO7ws.INSTANCE);
                        if (VERSION.SDK_INT >= 21) {
                           _$$Lambda$ChatRightsEditActivity$Z9VfsFUkfVTZPmGc9vtBuBrNWOo var12 = new _$$Lambda$ChatRightsEditActivity$Z9VfsFUkfVTZPmGc9vtBuBrNWOo(var13);
                           var10.setOnShowListener(var12);
                        }

                        this.showDialog(var10);
                     } catch (Exception var8) {
                        FileLog.e((Throwable)var8);
                     }
                  }
               } else {
                  this.bannedRights.until_date = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 2592000;
                  this.listViewAdapter.notifyItemChanged(this.untilDateRow);
               }
            } else {
               this.bannedRights.until_date = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 604800;
               this.listViewAdapter.notifyItemChanged(this.untilDateRow);
            }
         } else {
            this.bannedRights.until_date = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime() + 86400;
            this.listViewAdapter.notifyItemChanged(this.untilDateRow);
         }
      } else {
         this.bannedRights.until_date = 0;
         this.listViewAdapter.notifyItemChanged(this.untilDateRow);
      }

      var1.getDismissRunnable().run();
   }

   // $FF: synthetic method
   public void lambda$onDonePressed$7$ChatRightsEditActivity(int var1) {
      this.chatId = var1;
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(var1);
      this.onDonePressed();
   }

   public boolean onBackPressed() {
      return this.checkDiscard();
   }

   public void onResume() {
      super.onResume();
      ChatRightsEditActivity.ListAdapter var1 = this.listViewAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   public void setDelegate(ChatRightsEditActivity.ChatRightsEditActivityDelegate var1) {
      this.delegate = var1;
   }

   public interface ChatRightsEditActivityDelegate {
      void didSetRights(int var1, TLRPC.TL_chatAdminRights var2, TLRPC.TL_chatBannedRights var3);
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return ChatRightsEditActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == 0) {
            return 0;
         } else if (var1 != 1 && var1 != ChatRightsEditActivity.this.rightsShadowRow && var1 != ChatRightsEditActivity.this.removeAdminShadowRow && var1 != ChatRightsEditActivity.this.untilSectionRow) {
            if (var1 == 2) {
               return 3;
            } else if (var1 != ChatRightsEditActivity.this.changeInfoRow && var1 != ChatRightsEditActivity.this.postMessagesRow && var1 != ChatRightsEditActivity.this.editMesagesRow && var1 != ChatRightsEditActivity.this.deleteMessagesRow && var1 != ChatRightsEditActivity.this.addAdminsRow && var1 != ChatRightsEditActivity.this.banUsersRow && var1 != ChatRightsEditActivity.this.addUsersRow && var1 != ChatRightsEditActivity.this.pinMessagesRow && var1 != ChatRightsEditActivity.this.sendMessagesRow && var1 != ChatRightsEditActivity.this.sendMediaRow && var1 != ChatRightsEditActivity.this.sendStickersRow && var1 != ChatRightsEditActivity.this.embedLinksRow && var1 != ChatRightsEditActivity.this.sendPollsRow) {
               if (var1 == ChatRightsEditActivity.this.cantEditInfoRow) {
                  return 1;
               } else {
                  return var1 == ChatRightsEditActivity.this.untilDateRow ? 6 : 2;
               }
            } else {
               return 4;
            }
         } else {
            return 5;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2 = ChatRightsEditActivity.this.canEdit;
         boolean var3 = false;
         if (!var2) {
            return false;
         } else {
            int var4 = var1.getItemViewType();
            if (ChatRightsEditActivity.this.currentType == 0 && var4 == 4) {
               int var5 = var1.getAdapterPosition();
               if (var5 == ChatRightsEditActivity.this.changeInfoRow) {
                  return ChatRightsEditActivity.this.myAdminRights.change_info;
               }

               if (var5 == ChatRightsEditActivity.this.postMessagesRow) {
                  return ChatRightsEditActivity.this.myAdminRights.post_messages;
               }

               if (var5 == ChatRightsEditActivity.this.editMesagesRow) {
                  return ChatRightsEditActivity.this.myAdminRights.edit_messages;
               }

               if (var5 == ChatRightsEditActivity.this.deleteMessagesRow) {
                  return ChatRightsEditActivity.this.myAdminRights.delete_messages;
               }

               if (var5 == ChatRightsEditActivity.this.addAdminsRow) {
                  return ChatRightsEditActivity.this.myAdminRights.add_admins;
               }

               if (var5 == ChatRightsEditActivity.this.banUsersRow) {
                  return ChatRightsEditActivity.this.myAdminRights.ban_users;
               }

               if (var5 == ChatRightsEditActivity.this.addUsersRow) {
                  return ChatRightsEditActivity.this.myAdminRights.invite_users;
               }

               if (var5 == ChatRightsEditActivity.this.pinMessagesRow) {
                  return ChatRightsEditActivity.this.myAdminRights.pin_messages;
               }
            }

            var2 = var3;
            if (var4 != 3) {
               var2 = var3;
               if (var4 != 1) {
                  var2 = var3;
                  if (var4 != 5) {
                     var2 = true;
                  }
               }
            }

            return var2;
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         boolean var5 = false;
         int var7;
         switch(var3) {
         case 0:
            ((UserCell)var1.itemView).setData(ChatRightsEditActivity.this.currentUser, (CharSequence)null, (CharSequence)null, 0);
            break;
         case 1:
            TextInfoPrivacyCell var14 = (TextInfoPrivacyCell)var1.itemView;
            if (var2 == ChatRightsEditActivity.this.cantEditInfoRow) {
               var14.setText(LocaleController.getString("EditAdminCantEdit", 2131559307));
            }
            break;
         case 2:
            TextSettingsCell var13 = (TextSettingsCell)var1.itemView;
            if (var2 == ChatRightsEditActivity.this.removeAdminRow) {
               var13.setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
               var13.setTag("windowBackgroundWhiteRedText5");
               if (ChatRightsEditActivity.this.currentType == 0) {
                  var13.setText(LocaleController.getString("EditAdminRemoveAdmin", 2131559316), false);
               } else if (ChatRightsEditActivity.this.currentType == 1) {
                  var13.setText(LocaleController.getString("UserRestrictionsBlock", 2131560997), false);
               }
            }
            break;
         case 3:
            HeaderCell var12 = (HeaderCell)var1.itemView;
            if (ChatRightsEditActivity.this.currentType == 0) {
               var12.setText(LocaleController.getString("EditAdminWhatCanDo", 2131559318));
            } else if (ChatRightsEditActivity.this.currentType == 1) {
               var12.setText(LocaleController.getString("UserRestrictionsCanDo", 2131560998));
            }
            break;
         case 4:
            TextCheckCell2 var11 = (TextCheckCell2)var1.itemView;
            var7 = ChatRightsEditActivity.this.changeInfoRow;
            var3 = 2131165736;
            boolean var8;
            String var16;
            if (var2 == var7) {
               if (ChatRightsEditActivity.this.currentType == 0) {
                  if (ChatRightsEditActivity.this.isChannel) {
                     var11.setTextAndCheck(LocaleController.getString("EditAdminChangeChannelInfo", 2131559308), ChatRightsEditActivity.this.adminRights.change_info, true);
                  } else {
                     var11.setTextAndCheck(LocaleController.getString("EditAdminChangeGroupInfo", 2131559309), ChatRightsEditActivity.this.adminRights.change_info, true);
                  }
               } else if (ChatRightsEditActivity.this.currentType == 1) {
                  var16 = LocaleController.getString("UserRestrictionsChangeInfo", 2131560999);
                  if (!ChatRightsEditActivity.this.bannedRights.change_info && !ChatRightsEditActivity.this.defaultBannedRights.change_info) {
                     var8 = true;
                  } else {
                     var8 = false;
                  }

                  var11.setTextAndCheck(var16, var8, false);
                  if (!ChatRightsEditActivity.this.defaultBannedRights.change_info) {
                     var3 = 0;
                  }

                  var11.setIcon(var3);
               }
            } else if (var2 == ChatRightsEditActivity.this.postMessagesRow) {
               var11.setTextAndCheck(LocaleController.getString("EditAdminPostMessages", 2131559314), ChatRightsEditActivity.this.adminRights.post_messages, true);
            } else if (var2 == ChatRightsEditActivity.this.editMesagesRow) {
               var11.setTextAndCheck(LocaleController.getString("EditAdminEditMessages", 2131559311), ChatRightsEditActivity.this.adminRights.edit_messages, true);
            } else if (var2 == ChatRightsEditActivity.this.deleteMessagesRow) {
               if (ChatRightsEditActivity.this.isChannel) {
                  var11.setTextAndCheck(LocaleController.getString("EditAdminDeleteMessages", 2131559310), ChatRightsEditActivity.this.adminRights.delete_messages, true);
               } else {
                  var11.setTextAndCheck(LocaleController.getString("EditAdminGroupDeleteMessages", 2131559312), ChatRightsEditActivity.this.adminRights.delete_messages, true);
               }
            } else if (var2 == ChatRightsEditActivity.this.addAdminsRow) {
               var11.setTextAndCheck(LocaleController.getString("EditAdminAddAdmins", 2131559303), ChatRightsEditActivity.this.adminRights.add_admins, false);
            } else if (var2 == ChatRightsEditActivity.this.banUsersRow) {
               var11.setTextAndCheck(LocaleController.getString("EditAdminBanUsers", 2131559306), ChatRightsEditActivity.this.adminRights.ban_users, true);
            } else if (var2 == ChatRightsEditActivity.this.addUsersRow) {
               if (ChatRightsEditActivity.this.currentType == 0) {
                  if (ChatObject.isActionBannedByDefault(ChatRightsEditActivity.this.currentChat, 3)) {
                     var11.setTextAndCheck(LocaleController.getString("EditAdminAddUsers", 2131559304), ChatRightsEditActivity.this.adminRights.invite_users, true);
                  } else {
                     var11.setTextAndCheck(LocaleController.getString("EditAdminAddUsersViaLink", 2131559305), ChatRightsEditActivity.this.adminRights.invite_users, true);
                  }
               } else if (ChatRightsEditActivity.this.currentType == 1) {
                  var16 = LocaleController.getString("UserRestrictionsInviteUsers", 2131561003);
                  if (!ChatRightsEditActivity.this.bannedRights.invite_users && !ChatRightsEditActivity.this.defaultBannedRights.invite_users) {
                     var8 = true;
                  } else {
                     var8 = false;
                  }

                  var11.setTextAndCheck(var16, var8, true);
                  if (!ChatRightsEditActivity.this.defaultBannedRights.invite_users) {
                     var3 = 0;
                  }

                  var11.setIcon(var3);
               }
            } else if (var2 == ChatRightsEditActivity.this.pinMessagesRow) {
               if (ChatRightsEditActivity.this.currentType == 0) {
                  var11.setTextAndCheck(LocaleController.getString("EditAdminPinMessages", 2131559313), ChatRightsEditActivity.this.adminRights.pin_messages, true);
               } else if (ChatRightsEditActivity.this.currentType == 1) {
                  var16 = LocaleController.getString("UserRestrictionsPinMessages", 2131561013);
                  if (!ChatRightsEditActivity.this.bannedRights.pin_messages && !ChatRightsEditActivity.this.defaultBannedRights.pin_messages) {
                     var8 = true;
                  } else {
                     var8 = false;
                  }

                  var11.setTextAndCheck(var16, var8, true);
                  if (!ChatRightsEditActivity.this.defaultBannedRights.pin_messages) {
                     var3 = 0;
                  }

                  var11.setIcon(var3);
               }
            } else if (var2 == ChatRightsEditActivity.this.sendMessagesRow) {
               var16 = LocaleController.getString("UserRestrictionsSend", 2131561015);
               if (!ChatRightsEditActivity.this.bannedRights.send_messages && !ChatRightsEditActivity.this.defaultBannedRights.send_messages) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var11.setTextAndCheck(var16, var8, true);
               if (!ChatRightsEditActivity.this.defaultBannedRights.send_messages) {
                  var3 = 0;
               }

               var11.setIcon(var3);
            } else if (var2 == ChatRightsEditActivity.this.sendMediaRow) {
               var16 = LocaleController.getString("UserRestrictionsSendMedia", 2131561016);
               if (!ChatRightsEditActivity.this.bannedRights.send_media && !ChatRightsEditActivity.this.defaultBannedRights.send_media) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var11.setTextAndCheck(var16, var8, true);
               if (!ChatRightsEditActivity.this.defaultBannedRights.send_media) {
                  var3 = 0;
               }

               var11.setIcon(var3);
            } else if (var2 == ChatRightsEditActivity.this.sendStickersRow) {
               var16 = LocaleController.getString("UserRestrictionsSendStickers", 2131561018);
               if (!ChatRightsEditActivity.this.bannedRights.send_stickers && !ChatRightsEditActivity.this.defaultBannedRights.send_stickers) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var11.setTextAndCheck(var16, var8, true);
               if (!ChatRightsEditActivity.this.defaultBannedRights.send_stickers) {
                  var3 = 0;
               }

               var11.setIcon(var3);
            } else if (var2 == ChatRightsEditActivity.this.embedLinksRow) {
               var16 = LocaleController.getString("UserRestrictionsEmbedLinks", 2131561002);
               if (!ChatRightsEditActivity.this.bannedRights.embed_links && !ChatRightsEditActivity.this.defaultBannedRights.embed_links) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var11.setTextAndCheck(var16, var8, true);
               if (!ChatRightsEditActivity.this.defaultBannedRights.embed_links) {
                  var3 = 0;
               }

               var11.setIcon(var3);
            } else if (var2 == ChatRightsEditActivity.this.sendPollsRow) {
               var16 = LocaleController.getString("UserRestrictionsSendPolls", 2131561017);
               if (!ChatRightsEditActivity.this.bannedRights.send_polls && !ChatRightsEditActivity.this.defaultBannedRights.send_polls) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var11.setTextAndCheck(var16, var8, true);
               if (!ChatRightsEditActivity.this.defaultBannedRights.send_polls) {
                  var3 = 0;
               }

               var11.setIcon(var3);
            }

            if (var2 != ChatRightsEditActivity.this.sendMediaRow && var2 != ChatRightsEditActivity.this.sendStickersRow && var2 != ChatRightsEditActivity.this.embedLinksRow && var2 != ChatRightsEditActivity.this.sendPollsRow) {
               if (var2 == ChatRightsEditActivity.this.sendMessagesRow) {
                  var8 = var5;
                  if (!ChatRightsEditActivity.this.bannedRights.view_messages) {
                     var8 = var5;
                     if (!ChatRightsEditActivity.this.defaultBannedRights.view_messages) {
                        var8 = true;
                     }
                  }

                  var11.setEnabled(var8);
               }
            } else {
               var8 = var4;
               if (!ChatRightsEditActivity.this.bannedRights.send_messages) {
                  var8 = var4;
                  if (!ChatRightsEditActivity.this.bannedRights.view_messages) {
                     var8 = var4;
                     if (!ChatRightsEditActivity.this.defaultBannedRights.send_messages) {
                        var8 = var4;
                        if (!ChatRightsEditActivity.this.defaultBannedRights.view_messages) {
                           var8 = true;
                        }
                     }
                  }
               }

               var11.setEnabled(var8);
            }
            break;
         case 5:
            ShadowSectionCell var15 = (ShadowSectionCell)var1.itemView;
            var7 = ChatRightsEditActivity.this.rightsShadowRow;
            var3 = 2131165395;
            if (var2 == var7) {
               Context var10 = this.mContext;
               if (ChatRightsEditActivity.this.removeAdminRow == -1) {
                  var2 = var3;
               } else {
                  var2 = 2131165394;
               }

               var15.setBackgroundDrawable(Theme.getThemedDrawable(var10, var2, "windowBackgroundGrayShadow"));
            } else if (var2 == ChatRightsEditActivity.this.removeAdminShadowRow) {
               var15.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            } else {
               var15.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
            }
            break;
         case 6:
            TextDetailCell var6 = (TextDetailCell)var1.itemView;
            if (var2 == ChatRightsEditActivity.this.untilDateRow) {
               String var9;
               if (ChatRightsEditActivity.this.bannedRights.until_date != 0 && Math.abs((long)ChatRightsEditActivity.this.bannedRights.until_date - System.currentTimeMillis() / 1000L) <= 315360000L) {
                  var9 = LocaleController.formatDateForBan((long)ChatRightsEditActivity.this.bannedRights.until_date);
               } else {
                  var9 = LocaleController.getString("UserRestrictionsUntilForever", 2131561019);
               }

               var6.setTextAndValue(LocaleController.getString("UserRestrictionsDuration", 2131561001), var9, false);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        if (var2 != 5) {
                           var3 = new TextDetailCell(this.mContext);
                           ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        } else {
                           var3 = new ShadowSectionCell(this.mContext);
                        }
                     } else {
                        var3 = new TextCheckCell2(this.mContext);
                        ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     }
                  } else {
                     var3 = new HeaderCell(this.mContext);
                     ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
               } else {
                  var3 = new TextSettingsCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextInfoPrivacyCell(this.mContext);
               ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            }
         } else {
            var3 = new UserCell(this.mContext, 4, 0);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}
