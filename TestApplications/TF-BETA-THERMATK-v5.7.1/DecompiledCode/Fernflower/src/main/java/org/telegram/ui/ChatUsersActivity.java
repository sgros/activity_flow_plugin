package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Adapters.SearchAdapterHelper$SearchAdapterHelperDelegate$_CC;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class ChatUsersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   public static final int TYPE_ADMIN = 1;
   public static final int TYPE_BANNED = 0;
   public static final int TYPE_KICKED = 3;
   public static final int TYPE_USERS = 2;
   private static final int done_button = 1;
   private static final int search_button = 0;
   private int addNew2Row;
   private int addNewRow;
   private int addNewSectionRow;
   private int addUsersRow;
   private int blockedEmptyRow;
   private int botEndRow;
   private int botHeaderRow;
   private int botStartRow;
   private ArrayList bots = new ArrayList();
   private boolean botsEndReached;
   private SparseArray botsMap = new SparseArray();
   private int changeInfoRow;
   private int chatId;
   private ArrayList contacts = new ArrayList();
   private boolean contactsEndReached;
   private int contactsEndRow;
   private int contactsHeaderRow;
   private SparseArray contactsMap = new SparseArray();
   private int contactsStartRow;
   private TLRPC.Chat currentChat;
   private TLRPC.TL_chatBannedRights defaultBannedRights = new TLRPC.TL_chatBannedRights();
   private int delayResults;
   private ChatUsersActivity.ChatUsersActivityDelegate delegate;
   private ActionBarMenuItem doneItem;
   private int embedLinksRow;
   private EmptyTextProgressView emptyView;
   private boolean firstLoaded;
   private TLRPC.ChatFull info;
   private String initialBannedRights;
   private boolean isChannel;
   private RecyclerListView listView;
   private ChatUsersActivity.ListAdapter listViewAdapter;
   private boolean loadingUsers;
   private int membersHeaderRow;
   private boolean needOpenSearch;
   private ArrayList participants = new ArrayList();
   private int participantsDivider2Row;
   private int participantsDividerRow;
   private int participantsEndRow;
   private int participantsInfoRow;
   private SparseArray participantsMap = new SparseArray();
   private int participantsStartRow;
   private int permissionsSectionRow;
   private int pinMessagesRow;
   private int recentActionsRow;
   private int removedUsersRow;
   private int restricted1SectionRow;
   private int rowCount;
   private ActionBarMenuItem searchItem;
   private ChatUsersActivity.SearchAdapter searchListViewAdapter;
   private boolean searchWas;
   private boolean searching;
   private int selectType;
   private int sendMediaRow;
   private int sendMessagesRow;
   private int sendPollsRow;
   private int sendStickersRow;
   private int type;

   public ChatUsersActivity(Bundle var1) {
      super(var1);
      this.chatId = super.arguments.getInt("chat_id");
      this.type = super.arguments.getInt("type");
      this.needOpenSearch = super.arguments.getBoolean("open_search");
      this.selectType = super.arguments.getInt("selectType");
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
      TLRPC.Chat var4 = this.currentChat;
      if (var4 != null) {
         TLRPC.TL_chatBannedRights var5 = var4.default_banned_rights;
         if (var5 != null) {
            TLRPC.TL_chatBannedRights var2 = this.defaultBannedRights;
            var2.view_messages = var5.view_messages;
            var2.send_stickers = var5.send_stickers;
            var2.send_media = var5.send_media;
            var2.embed_links = var5.embed_links;
            var2.send_messages = var5.send_messages;
            var2.send_games = var5.send_games;
            var2.send_inline = var5.send_inline;
            var2.send_gifs = var5.send_gifs;
            var2.pin_messages = var5.pin_messages;
            var2.send_polls = var5.send_polls;
            var2.invite_users = var5.invite_users;
            var2.change_info = var5.change_info;
         }
      }

      this.initialBannedRights = ChatObject.getBannedRightsString(this.defaultBannedRights);
      boolean var3;
      if (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.isChannel = var3;
   }

   // $FF: synthetic method
   static int access$1300(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1400(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1800(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1900(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2000(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2100(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2200(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3100(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3300(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6200(ChatUsersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static void access$6300(ChatUsersActivity var0, int var1, TLObject var2, TLRPC.TL_chatAdminRights var3, TLRPC.TL_chatBannedRights var4, boolean var5, int var6, boolean var7) {
      var0.openRightsEdit(var1, var2, var3, var4, var5, var6, var7);
   }

   private boolean checkDiscard() {
      if (!ChatObject.getBannedRightsString(this.defaultBannedRights).equals(this.initialBannedRights)) {
         AlertDialog.Builder var1 = new AlertDialog.Builder(this.getParentActivity());
         var1.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131560995));
         if (this.isChannel) {
            var1.setMessage(LocaleController.getString("ChannelSettingsChangedAlert", 2131558999));
         } else {
            var1.setMessage(LocaleController.getString("GroupSettingsChangedAlert", 2131559613));
         }

         var1.setPositiveButton(LocaleController.getString("ApplyTheme", 2131558639), new _$$Lambda$ChatUsersActivity$Yqu9jp4YK_ycF_M_XeitW5mibTI(this));
         var1.setNegativeButton(LocaleController.getString("PassportDiscard", 2131560212), new _$$Lambda$ChatUsersActivity$236mIhGiuKn_O04cfPssFb_GqQI(this));
         this.showDialog(var1.create());
         return false;
      } else {
         return true;
      }
   }

   private boolean createMenuForParticipant(TLObject var1, boolean var2) {
      if (var1 != null && this.selectType == 0) {
         int var4;
         boolean var5;
         int var8;
         TLRPC.TL_chatAdminRights var9;
         TLRPC.TL_chatBannedRights var17;
         label201: {
            TLRPC.TL_chatBannedRights var6;
            TLRPC.TL_chatAdminRights var7;
            if (var1 instanceof TLRPC.ChannelParticipant) {
               TLRPC.ChannelParticipant var3 = (TLRPC.ChannelParticipant)var1;
               var4 = var3.user_id;
               var5 = var3.can_edit;
               var6 = var3.banned_rights;
               var7 = var3.admin_rights;
               var8 = var3.date;
            } else {
               if (!(var1 instanceof TLRPC.ChatParticipant)) {
                  var5 = false;
                  var4 = 0;
                  var8 = 0;
                  var9 = null;
                  var17 = null;
                  break label201;
               }

               TLRPC.ChatParticipant var18 = (TLRPC.ChatParticipant)var1;
               var4 = var18.user_id;
               var8 = var18.date;
               var5 = ChatObject.canAddAdmins(this.currentChat);
               var7 = null;
               var6 = null;
            }

            var9 = var7;
            var17 = var6;
         }

         if (var4 != 0 && var4 != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
            int var10 = this.type;
            AlertDialog var16;
            if (var10 != 2) {
               CharSequence[] var21;
               String var22;
               int[] var24;
               String var26;
               if (var10 == 3 && ChatObject.canBlockUsers(this.currentChat)) {
                  if (var2) {
                     return true;
                  }

                  var26 = LocaleController.getString("ChannelEditPermissions", 2131558951);
                  var22 = LocaleController.getString("ChannelDeleteFromList", 2131558945);
                  var24 = new int[]{2131165273, 2131165348};
                  var21 = new CharSequence[]{var26, var22};
               } else if (this.type == 0 && ChatObject.canBlockUsers(this.currentChat)) {
                  if (var2) {
                     return true;
                  }

                  if (ChatObject.canAddUsers(this.currentChat)) {
                     if (this.isChannel) {
                        var8 = 2131558922;
                        var22 = "ChannelAddToChannel";
                     } else {
                        var8 = 2131558923;
                        var22 = "ChannelAddToGroup";
                     }

                     var22 = LocaleController.getString(var22, var8);
                  } else {
                     var22 = null;
                  }

                  var26 = LocaleController.getString("ChannelDeleteFromList", 2131558945);
                  var24 = new int[]{2131165272, 2131165348};
                  var21 = new CharSequence[]{var22, var26};
               } else if (this.type == 1 && ChatObject.canAddAdmins(this.currentChat) && var5) {
                  if (var2) {
                     return true;
                  }

                  if (this.currentChat.creator || !(var1 instanceof TLRPC.TL_channelParticipantCreator) && var5) {
                     var21 = new CharSequence[]{LocaleController.getString("EditAdminRights", 2131559317), LocaleController.getString("ChannelRemoveUserAdmin", 2131558995)};
                     var24 = new int[]{2131165271, 2131165274};
                  } else {
                     var22 = LocaleController.getString("ChannelRemoveUserAdmin", 2131558995);
                     var24 = new int[]{2131165274};
                     var21 = new CharSequence[]{var22};
                  }
               } else {
                  var24 = null;
                  var21 = null;
               }

               if (var21 == null) {
                  return false;
               }

               AlertDialog.Builder var27 = new AlertDialog.Builder(this.getParentActivity());
               var27.setItems(var21, var24, new _$$Lambda$ChatUsersActivity$PIq1XQnFcSvW_MebsnvdhxTy3xQ(this, var21, var4, var9, var1, var17));
               var16 = var27.create();
               this.showDialog(var16);
               if (this.type == 1) {
                  var16.setItemColor(var21.length - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
               }
            } else {
               TLRPC.User var11 = MessagesController.getInstance(super.currentAccount).getUser(var4);
               boolean var23;
               if (!ChatObject.canAddAdmins(this.currentChat) || !(var1 instanceof TLRPC.TL_channelParticipant) && !(var1 instanceof TLRPC.TL_channelParticipantBanned) && !(var1 instanceof TLRPC.TL_chatParticipant) && !var5) {
                  var23 = false;
               } else {
                  var23 = true;
               }

               boolean var12 = var1 instanceof TLRPC.TL_channelParticipantAdmin;
               if ((var12 || var1 instanceof TLRPC.TL_channelParticipantCreator || var1 instanceof TLRPC.TL_chatParticipantCreator || var1 instanceof TLRPC.TL_chatParticipantAdmin) && !var5) {
                  var5 = false;
               } else {
                  var5 = true;
               }

               boolean var13;
               if (!var12 && !(var1 instanceof TLRPC.TL_chatParticipantAdmin)) {
                  var13 = false;
               } else {
                  var13 = true;
               }

               ArrayList var14;
               ArrayList var19;
               ArrayList var20;
               if (!var2) {
                  var20 = new ArrayList();
                  var19 = new ArrayList();
                  var14 = new ArrayList();
               } else {
                  var14 = null;
                  var20 = null;
                  var19 = null;
               }

               if (var23) {
                  if (var2) {
                     return true;
                  }

                  String var15;
                  if (var13) {
                     var15 = LocaleController.getString("EditAdminRights", 2131559317);
                  } else {
                     var15 = LocaleController.getString("SetAsAdmin", 2131560731);
                  }

                  var20.add(var15);
                  var14.add(2131165271);
                  var19.add(0);
               }

               if (ChatObject.canBlockUsers(this.currentChat) && var5) {
                  if (var2) {
                     return true;
                  }

                  if (!this.isChannel) {
                     if (ChatObject.isChannel(this.currentChat)) {
                        var20.add(LocaleController.getString("ChangePermissions", 2131558910));
                        var14.add(2131165273);
                        var19.add(1);
                     }

                     var20.add(LocaleController.getString("KickFromGroup", 2131559714));
                     var14.add(2131165274);
                     var19.add(2);
                  } else {
                     var20.add(LocaleController.getString("ChannelRemoveUser", 2131558994));
                     var14.add(2131165274);
                     var19.add(2);
                  }

                  var23 = true;
               } else {
                  var23 = false;
               }

               if (var19 == null || var19.isEmpty()) {
                  return false;
               }

               AlertDialog.Builder var25 = new AlertDialog.Builder(this.getParentActivity());
               var25.setItems((CharSequence[])var20.toArray(new CharSequence[var19.size()]), AndroidUtilities.toIntArray(var14), new _$$Lambda$ChatUsersActivity$llOV1TtGtwD9D9cr147J5EH_9co(this, var19, var11, var4, var5, var1, var8, var9, var17));
               var16 = var25.create();
               this.showDialog(var16);
               if (var23) {
                  var16.setItemColor(var20.size() - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private String formatUserPermissions(TLRPC.TL_chatBannedRights var1) {
      if (var1 == null) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder();
         boolean var3 = var1.view_messages;
         if (var3 && this.defaultBannedRights.view_messages != var3) {
            var2.append(LocaleController.getString("UserRestrictionsNoRead", 2131561008));
         }

         var3 = var1.send_messages;
         if (var3 && this.defaultBannedRights.send_messages != var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(LocaleController.getString("UserRestrictionsNoSend", 2131561009));
         }

         var3 = var1.send_media;
         if (var3 && this.defaultBannedRights.send_media != var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(LocaleController.getString("UserRestrictionsNoSendMedia", 2131561010));
         }

         var3 = var1.send_stickers;
         if (var3 && this.defaultBannedRights.send_stickers != var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(LocaleController.getString("UserRestrictionsNoSendStickers", 2131561012));
         }

         var3 = var1.send_polls;
         if (var3 && this.defaultBannedRights.send_polls != var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(LocaleController.getString("UserRestrictionsNoSendPolls", 2131561011));
         }

         var3 = var1.embed_links;
         if (var3 && this.defaultBannedRights.embed_links != var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(LocaleController.getString("UserRestrictionsNoEmbedLinks", 2131561005));
         }

         var3 = var1.invite_users;
         if (var3 && this.defaultBannedRights.invite_users != var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(LocaleController.getString("UserRestrictionsNoInviteUsers", 2131561006));
         }

         var3 = var1.pin_messages;
         if (var3 && this.defaultBannedRights.pin_messages != var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(LocaleController.getString("UserRestrictionsNoPinMessages", 2131561007));
         }

         var3 = var1.change_info;
         if (var3 && this.defaultBannedRights.change_info != var3) {
            if (var2.length() != 0) {
               var2.append(", ");
            }

            var2.append(LocaleController.getString("UserRestrictionsNoChangeInfo", 2131561004));
         }

         if (var2.length() != 0) {
            var2.replace(0, 1, var2.substring(0, 1).toUpperCase());
            var2.append('.');
         }

         return var2.toString();
      }
   }

   private TLObject getAnyParticipant(int var1) {
      for(int var2 = 0; var2 < 3; ++var2) {
         SparseArray var3;
         if (var2 == 0) {
            var3 = this.contactsMap;
         } else if (var2 == 1) {
            var3 = this.botsMap;
         } else {
            var3 = this.participantsMap;
         }

         TLObject var4 = (TLObject)var3.get(var1);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   private int getChannelAdminParticipantType(TLObject var1) {
      if (!(var1 instanceof TLRPC.TL_channelParticipantCreator) && !(var1 instanceof TLRPC.TL_channelParticipantSelf)) {
         return !(var1 instanceof TLRPC.TL_channelParticipantAdmin) && !(var1 instanceof TLRPC.TL_channelParticipant) ? 2 : 1;
      } else {
         return 0;
      }
   }

   private void loadChatParticipants(int var1, int var2) {
      if (!this.loadingUsers) {
         this.contactsEndReached = false;
         this.botsEndReached = false;
         this.loadChatParticipants(var1, var2, true);
      }
   }

   private void loadChatParticipants(int var1, int var2, boolean var3) {
      var3 = ChatObject.isChannel(this.currentChat);
      byte var4 = 0;
      byte var5 = 0;
      ChatUsersActivity.ListAdapter var11;
      if (!var3) {
         this.loadingUsers = false;
         this.participants.clear();
         this.bots.clear();
         this.contacts.clear();
         this.participantsMap.clear();
         this.contactsMap.clear();
         this.botsMap.clear();
         var1 = this.type;
         TLRPC.ChatParticipant var6;
         if (var1 == 1) {
            TLRPC.ChatFull var10 = this.info;
            if (var10 != null) {
               var2 = var10.participants.participants.size();

               for(var1 = var5; var1 < var2; ++var1) {
                  var6 = (TLRPC.ChatParticipant)this.info.participants.participants.get(var1);
                  if (var6 instanceof TLRPC.TL_chatParticipantCreator || var6 instanceof TLRPC.TL_chatParticipantAdmin) {
                     this.participants.add(var6);
                  }

                  this.participantsMap.put(var6.user_id, var6);
               }
            }
         } else if (var1 == 2 && this.info != null) {
            int var9 = UserConfig.getInstance(super.currentAccount).clientUserId;
            var2 = this.info.participants.participants.size();

            for(var1 = var4; var1 < var2; ++var1) {
               var6 = (TLRPC.ChatParticipant)this.info.participants.participants.get(var1);
               if (this.selectType == 0 || var6.user_id != var9) {
                  if (this.selectType == 1) {
                     if (ContactsController.getInstance(super.currentAccount).isContact(var6.user_id)) {
                        this.contacts.add(var6);
                        this.contactsMap.put(var6.user_id, var6);
                     } else {
                        this.participants.add(var6);
                        this.participantsMap.put(var6.user_id, var6);
                     }
                  } else if (ContactsController.getInstance(super.currentAccount).isContact(var6.user_id)) {
                     this.contacts.add(var6);
                     this.contactsMap.put(var6.user_id, var6);
                  } else {
                     TLRPC.User var7 = MessagesController.getInstance(super.currentAccount).getUser(var6.user_id);
                     if (var7 != null && var7.bot) {
                        this.bots.add(var6);
                        this.botsMap.put(var6.user_id, var6);
                     } else {
                        this.participants.add(var6);
                        this.participantsMap.put(var6.user_id, var6);
                     }
                  }
               }
            }
         }

         var11 = this.listViewAdapter;
         if (var11 != null) {
            var11.notifyDataSetChanged();
         }

         this.updateRows();
         var11 = this.listViewAdapter;
         if (var11 != null) {
            var11.notifyDataSetChanged();
         }
      } else {
         this.loadingUsers = true;
         EmptyTextProgressView var13 = this.emptyView;
         if (var13 != null && !this.firstLoaded) {
            var13.showProgress();
         }

         var11 = this.listViewAdapter;
         if (var11 != null) {
            var11.notifyDataSetChanged();
         }

         TLRPC.TL_channels_getParticipants var15 = new TLRPC.TL_channels_getParticipants();
         var15.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chatId);
         int var8 = this.type;
         if (var8 == 0) {
            var15.filter = new TLRPC.TL_channelParticipantsKicked();
         } else if (var8 == 1) {
            var15.filter = new TLRPC.TL_channelParticipantsAdmins();
         } else if (var8 == 2) {
            label114: {
               TLRPC.ChatFull var12 = this.info;
               if (var12 != null && var12.participants_count <= 200) {
                  TLRPC.Chat var14 = this.currentChat;
                  if (var14 != null && var14.megagroup) {
                     var15.filter = new TLRPC.TL_channelParticipantsRecent();
                     break label114;
                  }
               }

               if (this.selectType == 1) {
                  if (!this.contactsEndReached) {
                     this.delayResults = 2;
                     var15.filter = new TLRPC.TL_channelParticipantsContacts();
                     this.contactsEndReached = true;
                     this.loadChatParticipants(0, 200, false);
                  } else {
                     var15.filter = new TLRPC.TL_channelParticipantsRecent();
                  }
               } else if (!this.contactsEndReached) {
                  this.delayResults = 3;
                  var15.filter = new TLRPC.TL_channelParticipantsContacts();
                  this.contactsEndReached = true;
                  this.loadChatParticipants(0, 200, false);
               } else if (!this.botsEndReached) {
                  var15.filter = new TLRPC.TL_channelParticipantsBots();
                  this.botsEndReached = true;
                  this.loadChatParticipants(0, 200, false);
               } else {
                  var15.filter = new TLRPC.TL_channelParticipantsRecent();
               }
            }
         } else if (var8 == 3) {
            var15.filter = new TLRPC.TL_channelParticipantsBanned();
         }

         var15.filter.q = "";
         var15.offset = var1;
         var15.limit = var2;
         var1 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var15, new _$$Lambda$ChatUsersActivity$VPgAUAFJXqsKcGEM9EFJ9hJ7L7M(this, var15));
         ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var1, super.classGuid);
      }

   }

   private void openRightsEdit(int var1, TLObject var2, TLRPC.TL_chatAdminRights var3, TLRPC.TL_chatBannedRights var4, boolean var5, int var6, boolean var7) {
      int var8 = this.chatId;
      TLRPC.TL_chatBannedRights var9 = this.defaultBannedRights;
      boolean var10;
      if (var2 == null) {
         var10 = true;
      } else {
         var10 = false;
      }

      ChatRightsEditActivity var11 = new ChatRightsEditActivity(var1, var8, var3, var9, var4, var6, var5, var10);
      var11.setDelegate(new _$$Lambda$ChatUsersActivity$s_tj1rO5rel9STPZNYUOQpZ18CQ(this, var2, var7));
      this.presentFragment(var11, var7);
   }

   private void openRightsEdit2(int var1, int var2, TLObject var3, TLRPC.TL_chatAdminRights var4, TLRPC.TL_chatBannedRights var5, boolean var6, int var7, boolean var8) {
      ChatRightsEditActivity var9 = new ChatRightsEditActivity(var1, this.chatId, var4, this.defaultBannedRights, var5, var7, true, false);
      var9.setDelegate(new _$$Lambda$ChatUsersActivity$0nOnj72e_Go4w_fy56ow2OoyZ5U(this, var7, var1, var2));
      this.presentFragment(var9);
   }

   private void processDone() {
      if (this.type == 3) {
         if (!ChatObject.getBannedRightsString(this.defaultBannedRights).equals(this.initialBannedRights)) {
            MessagesController.getInstance(super.currentAccount).setDefaultBannedRole(this.chatId, this.defaultBannedRights, ChatObject.isChannel(this.currentChat), this);
            TLRPC.Chat var1 = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
            if (var1 != null) {
               var1.default_banned_rights = this.defaultBannedRights;
            }
         }

         this.finishFragment();
      }
   }

   private void removeParticipants(int var1) {
      int var2 = 0;

      boolean var3;
      for(var3 = false; var2 < 3; ++var2) {
         SparseArray var4;
         ArrayList var5;
         if (var2 == 0) {
            var4 = this.contactsMap;
            var5 = this.contacts;
         } else if (var2 == 1) {
            var4 = this.botsMap;
            var5 = this.bots;
         } else {
            var4 = this.participantsMap;
            var5 = this.participants;
         }

         TLObject var6 = (TLObject)var4.get(var1);
         if (var6 != null) {
            var4.remove(var1);
            var5.remove(var6);
            var3 = true;
         }
      }

      if (var3) {
         this.updateRows();
         this.listViewAdapter.notifyDataSetChanged();
      }

   }

   private void removeParticipants(TLObject var1) {
      if (var1 instanceof TLRPC.ChatParticipant) {
         this.removeParticipants(((TLRPC.ChatParticipant)var1).user_id);
      } else if (var1 instanceof TLRPC.ChannelParticipant) {
         this.removeParticipants(((TLRPC.ChannelParticipant)var1).user_id);
      }

   }

   private void removeUser(int var1) {
      if (ChatObject.isChannel(this.currentChat)) {
         TLRPC.User var2 = MessagesController.getInstance(super.currentAccount).getUser(var1);
         MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chatId, var2, (TLRPC.ChatFull)null);
         this.finishFragment();
      }
   }

   private void updateParticipantWithRights(TLRPC.ChannelParticipant var1, TLRPC.TL_chatAdminRights var2, TLRPC.TL_chatBannedRights var3, int var4, boolean var5) {
      int var6 = 0;
      boolean var7 = false;

      boolean var10;
      for(TLRPC.ChannelParticipant var8 = var1; var6 < 3; var7 = var10) {
         SparseArray var11;
         if (var6 == 0) {
            var11 = this.contactsMap;
         } else if (var6 == 1) {
            var11 = this.botsMap;
         } else {
            var11 = this.participantsMap;
         }

         TLObject var9 = (TLObject)var11.get(var8.user_id);
         if (var9 instanceof TLRPC.ChannelParticipant) {
            var1 = (TLRPC.ChannelParticipant)var9;
            var1.admin_rights = var2;
            var1.banned_rights = var3;
            var8 = var1;
            if (var5) {
               var1.promoted_by = UserConfig.getInstance(super.currentAccount).getClientUserId();
               var8 = var1;
            }
         }

         var10 = var7;
         if (var5) {
            var10 = var7;
            if (var9 != null) {
               var10 = var7;
               if (!var7) {
                  ChatUsersActivity.ChatUsersActivityDelegate var12 = this.delegate;
                  var10 = var7;
                  if (var12 != null) {
                     var12.didAddParticipantToList(var4, var9);
                     var10 = true;
                  }
               }
            }
         }

         ++var6;
      }

   }

   private void updateRows() {
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
      TLRPC.Chat var1 = this.currentChat;
      if (var1 != null) {
         this.recentActionsRow = -1;
         this.addNewRow = -1;
         this.addNew2Row = -1;
         this.addNewSectionRow = -1;
         this.restricted1SectionRow = -1;
         this.participantsStartRow = -1;
         this.participantsDividerRow = -1;
         this.participantsDivider2Row = -1;
         this.participantsEndRow = -1;
         this.participantsInfoRow = -1;
         this.blockedEmptyRow = -1;
         this.permissionsSectionRow = -1;
         this.sendMessagesRow = -1;
         this.sendMediaRow = -1;
         this.sendStickersRow = -1;
         this.sendPollsRow = -1;
         this.embedLinksRow = -1;
         this.addUsersRow = -1;
         this.pinMessagesRow = -1;
         this.changeInfoRow = -1;
         this.removedUsersRow = -1;
         this.contactsHeaderRow = -1;
         this.contactsStartRow = -1;
         this.contactsEndRow = -1;
         this.botHeaderRow = -1;
         this.botStartRow = -1;
         this.botEndRow = -1;
         this.membersHeaderRow = -1;
         boolean var2 = false;
         this.rowCount = 0;
         int var3 = this.type;
         int var6;
         if (var3 == 3) {
            var6 = this.rowCount++;
            this.permissionsSectionRow = var6;
            var6 = this.rowCount++;
            this.sendMessagesRow = var6;
            var6 = this.rowCount++;
            this.sendMediaRow = var6;
            var6 = this.rowCount++;
            this.sendStickersRow = var6;
            var6 = this.rowCount++;
            this.sendPollsRow = var6;
            var6 = this.rowCount++;
            this.embedLinksRow = var6;
            var6 = this.rowCount++;
            this.addUsersRow = var6;
            var6 = this.rowCount++;
            this.pinMessagesRow = var6;
            var6 = this.rowCount++;
            this.changeInfoRow = var6;
            if (ChatObject.isChannel(var1)) {
               var6 = this.rowCount++;
               this.participantsDivider2Row = var6;
               var6 = this.rowCount++;
               this.removedUsersRow = var6;
            }

            var6 = this.rowCount++;
            this.participantsDividerRow = var6;
            if (ChatObject.canBlockUsers(this.currentChat)) {
               var6 = this.rowCount++;
               this.addNewRow = var6;
            }

            if (!this.participants.isEmpty()) {
               var6 = this.rowCount;
               this.participantsStartRow = var6;
               this.rowCount = var6 + this.participants.size();
               this.participantsEndRow = this.rowCount;
            }

            if (this.addNewRow != -1 || this.participantsStartRow != -1) {
               var6 = this.rowCount++;
               this.addNewSectionRow = var6;
            }
         } else if (var3 == 0) {
            if (ChatObject.canBlockUsers(var1)) {
               var6 = this.rowCount++;
               this.addNewRow = var6;
               if (!this.participants.isEmpty()) {
                  var6 = this.rowCount++;
                  this.participantsInfoRow = var6;
               }
            }

            if (!this.participants.isEmpty()) {
               var6 = this.rowCount++;
               this.restricted1SectionRow = var6;
               var6 = this.rowCount;
               this.participantsStartRow = var6;
               this.rowCount = var6 + this.participants.size();
               this.participantsEndRow = this.rowCount;
            }

            if (this.participantsStartRow != -1) {
               if (this.participantsInfoRow == -1) {
                  var6 = this.rowCount++;
                  this.participantsInfoRow = var6;
               } else {
                  var6 = this.rowCount++;
                  this.addNewSectionRow = var6;
               }
            } else {
               var6 = this.rowCount++;
               this.blockedEmptyRow = var6;
            }
         } else {
            boolean var4 = true;
            if (var3 == 1) {
               if (ChatObject.isChannel(var1) && this.currentChat.megagroup) {
                  TLRPC.ChatFull var5 = this.info;
                  if (var5 == null || var5.participants_count <= 200) {
                     var6 = this.rowCount++;
                     this.recentActionsRow = var6;
                     var6 = this.rowCount++;
                     this.addNewSectionRow = var6;
                  }
               }

               if (ChatObject.canAddAdmins(this.currentChat)) {
                  var6 = this.rowCount++;
                  this.addNewRow = var6;
               }

               if (!this.participants.isEmpty()) {
                  var6 = this.rowCount;
                  this.participantsStartRow = var6;
                  this.rowCount = var6 + this.participants.size();
                  this.participantsEndRow = this.rowCount;
               }

               var6 = this.rowCount++;
               this.participantsInfoRow = var6;
            } else if (var3 == 2) {
               if (this.selectType == 0 && ChatObject.canAddUsers(var1)) {
                  var3 = this.rowCount++;
                  this.addNewRow = var3;
               }

               if (!this.contacts.isEmpty()) {
                  var6 = this.rowCount++;
                  this.contactsHeaderRow = var6;
                  var6 = this.rowCount;
                  this.contactsStartRow = var6;
                  this.rowCount = var6 + this.contacts.size();
                  this.contactsEndRow = this.rowCount;
                  var2 = true;
               }

               if (!this.bots.isEmpty()) {
                  var6 = this.rowCount++;
                  this.botHeaderRow = var6;
                  var6 = this.rowCount;
                  this.botStartRow = var6;
                  this.rowCount = var6 + this.bots.size();
                  this.botEndRow = this.rowCount;
                  var2 = var4;
               }

               if (!this.participants.isEmpty()) {
                  if (var2) {
                     var6 = this.rowCount++;
                     this.membersHeaderRow = var6;
                  }

                  var6 = this.rowCount;
                  this.participantsStartRow = var6;
                  this.rowCount = var6 + this.participants.size();
                  this.participantsEndRow = this.rowCount;
               }

               var6 = this.rowCount;
               if (var6 != 0) {
                  this.rowCount = var6 + 1;
                  this.participantsInfoRow = var6;
               }
            }
         }

      }
   }

   public View createView(Context var1) {
      this.searching = false;
      this.searchWas = false;
      super.actionBar.setBackButtonImage(2131165409);
      ActionBar var2 = super.actionBar;
      byte var3 = 1;
      var2.setAllowOverlayTitle(true);
      int var4 = this.type;
      if (var4 == 3) {
         super.actionBar.setTitle(LocaleController.getString("ChannelPermissions", 2131558985));
      } else if (var4 == 0) {
         super.actionBar.setTitle(LocaleController.getString("ChannelBlacklist", 2131558932));
      } else if (var4 == 1) {
         super.actionBar.setTitle(LocaleController.getString("ChannelAdministrators", 2131558927));
      } else if (var4 == 2) {
         var4 = this.selectType;
         if (var4 == 0) {
            if (this.isChannel) {
               super.actionBar.setTitle(LocaleController.getString("ChannelSubscribers", 2131559004));
            } else {
               super.actionBar.setTitle(LocaleController.getString("ChannelMembers", 2131558962));
            }
         } else if (var4 == 1) {
            super.actionBar.setTitle(LocaleController.getString("ChannelAddAdmin", 2131558918));
         } else if (var4 == 2) {
            super.actionBar.setTitle(LocaleController.getString("ChannelBlockUser", 2131558933));
         } else if (var4 == 3) {
            super.actionBar.setTitle(LocaleController.getString("ChannelAddException", 2131558919));
         }
      }

      label76: {
         super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            public void onItemClick(int var1) {
               if (var1 == -1) {
                  if (ChatUsersActivity.this.checkDiscard()) {
                     ChatUsersActivity.this.finishFragment();
                  }
               } else if (var1 == 1) {
                  ChatUsersActivity.this.processDone();
               }

            }
         });
         if (this.selectType == 0) {
            var4 = this.type;
            if (var4 != 2 && var4 != 0 && var4 != 3) {
               break label76;
            }
         }

         this.searchListViewAdapter = new ChatUsersActivity.SearchAdapter(var1);
         ActionBarMenu var8 = super.actionBar.createMenu();
         this.searchItem = var8.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            public void onSearchCollapse() {
               ChatUsersActivity.this.searchListViewAdapter.searchDialogs((String)null);
               ChatUsersActivity.this.searching = false;
               ChatUsersActivity.this.searchWas = false;
               ChatUsersActivity.this.listView.setAdapter(ChatUsersActivity.this.listViewAdapter);
               ChatUsersActivity.this.listViewAdapter.notifyDataSetChanged();
               ChatUsersActivity.this.listView.setFastScrollVisible(true);
               ChatUsersActivity.this.listView.setVerticalScrollBarEnabled(false);
               ChatUsersActivity.this.emptyView.setShowAtCenter(false);
               if (ChatUsersActivity.this.doneItem != null) {
                  ChatUsersActivity.this.doneItem.setVisibility(0);
               }

            }

            public void onSearchExpand() {
               ChatUsersActivity.this.searching = true;
               ChatUsersActivity.this.emptyView.setShowAtCenter(true);
               if (ChatUsersActivity.this.doneItem != null) {
                  ChatUsersActivity.this.doneItem.setVisibility(8);
               }

            }

            public void onTextChanged(EditText var1) {
               if (ChatUsersActivity.this.searchListViewAdapter != null) {
                  String var2 = var1.getText().toString();
                  if (var2.length() != 0) {
                     ChatUsersActivity.this.searchWas = true;
                     if (ChatUsersActivity.this.listView != null && ChatUsersActivity.this.listView.getAdapter() != ChatUsersActivity.this.searchListViewAdapter) {
                        ChatUsersActivity.this.listView.setAdapter(ChatUsersActivity.this.searchListViewAdapter);
                        ChatUsersActivity.this.searchListViewAdapter.notifyDataSetChanged();
                        ChatUsersActivity.this.listView.setFastScrollVisible(false);
                        ChatUsersActivity.this.listView.setVerticalScrollBarEnabled(true);
                     }
                  }

                  ChatUsersActivity.this.searchListViewAdapter.searchDialogs(var2);
               }
            }
         });
         if (this.type == 3) {
            this.searchItem.setSearchFieldHint(LocaleController.getString("ChannelSearchException", 2131558997));
         } else {
            this.searchItem.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
         }

         if (this.type == 3) {
            this.doneItem = var8.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0F), LocaleController.getString("Done", 2131559299));
         }
      }

      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var9 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      var4 = this.type;
      if (var4 == 0 || var4 == 2 || var4 == 3) {
         this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
      }

      this.emptyView.setShowAtCenter(true);
      var9.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.emptyView);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      RecyclerListView var5 = this.listView;
      ChatUsersActivity.ListAdapter var6 = new ChatUsersActivity.ListAdapter(var1);
      this.listViewAdapter = var6;
      var5.setAdapter(var6);
      RecyclerListView var7 = this.listView;
      if (!LocaleController.isRTL) {
         var3 = 2;
      }

      var7.setVerticalScrollbarPosition(var3);
      var9.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ChatUsersActivity$5cdlJaYt4Fjs1RdGaQE3yCAVZ1M(this)));
      this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$ChatUsersActivity$0hbN7epVO7jllCcszytHvZXGqYU(this)));
      if (this.searchItem != null) {
         this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView var1, int var2) {
               if (var2 == 1) {
                  AndroidUtilities.hideKeyboard(ChatUsersActivity.this.getParentActivity().getCurrentFocus());
               }

            }

            public void onScrolled(RecyclerView var1, int var2, int var3) {
            }
         });
      }

      if (this.loadingUsers) {
         this.emptyView.showProgress();
      } else {
         this.emptyView.showTextView();
      }

      this.updateRows();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.chatInfoDidLoad) {
         TLRPC.ChatFull var4 = (TLRPC.ChatFull)var3[0];
         boolean var5 = (Boolean)var3[2];
         if (var4.id == this.chatId && (!var5 || !ChatObject.isChannel(this.currentChat))) {
            this.info = var4;
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatUsersActivity$i3MUTxy_8Wq7Kw42VYgYl8VfzuQ(this));
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo var1 = new _$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo(this);
      ThemeDescription var2 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, ManageChatUserCell.class, ManageChatTextCell.class, TextCheckCell2.class, TextSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var10 = this.listView;
      Paint var11 = Theme.dividerPaint;
      ThemeDescription var12 = new ThemeDescription(var10, 0, new Class[]{View.class}, var11, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var13 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var14 = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4");
      ThemeDescription var15 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var16 = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader");
      ThemeDescription var17 = new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_graySectionText");
      ThemeDescription var18 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection");
      ThemeDescription var19 = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var20 = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText");
      ThemeDescription var21 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var29 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2");
      ThemeDescription var22 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switch2Track");
      ThemeDescription var23 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switch2TrackChecked");
      ThemeDescription var24 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var25 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var26 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var27 = this.listView;
      Drawable var28 = Theme.avatar_broadcastDrawable;
      Drawable var30 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var29, var22, var23, var24, var25, var26, new ThemeDescription(var27, 0, new Class[]{ManageChatUserCell.class}, (Paint)null, new Drawable[]{var28, var30}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueButton"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueIcon")};
   }

   public boolean hasSelectType() {
      boolean var1;
      if (this.selectType != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$16$ChatUsersActivity(DialogInterface var1, int var2) {
      this.processDone();
   }

   // $FF: synthetic method
   public void lambda$checkDiscard$17$ChatUsersActivity(DialogInterface var1, int var2) {
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createMenuForParticipant$14$ChatUsersActivity(CharSequence[] var1, int var2, TLRPC.TL_chatAdminRights var3, TLObject var4, TLRPC.TL_chatBannedRights var5, DialogInterface var6, int var7) {
      int var8 = this.type;
      ChatRightsEditActivity var9;
      if (var8 == 1) {
         if (var7 == 0 && var1.length == 2) {
            var9 = new ChatRightsEditActivity(var2, this.chatId, var3, (TLRPC.TL_chatBannedRights)null, (TLRPC.TL_chatBannedRights)null, 0, true, false);
            var9.setDelegate(new _$$Lambda$ChatUsersActivity$VvBPc6EKF_AvBHC8o_spJJmYy1w(this, var4));
            this.presentFragment(var9);
         } else {
            MessagesController.getInstance(super.currentAccount).setUserAdminRole(this.chatId, MessagesController.getInstance(super.currentAccount).getUser(var2), new TLRPC.TL_chatAdminRights(), true ^ this.isChannel, this, false);
            this.removeParticipants(var2);
         }
      } else if (var8 != 0 && var8 != 3) {
         if (var7 == 0) {
            MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chatId, MessagesController.getInstance(super.currentAccount).getUser(var2), (TLRPC.ChatFull)null);
         }
      } else {
         if (var7 == 0) {
            var8 = this.type;
            if (var8 == 3) {
               var9 = new ChatRightsEditActivity(var2, this.chatId, (TLRPC.TL_chatAdminRights)null, this.defaultBannedRights, var5, 1, true, false);
               var9.setDelegate(new _$$Lambda$ChatUsersActivity$Kj7_HFBJQnmhoJfYSpDZ4Apo2QA(this, var4));
               this.presentFragment(var9);
            } else if (var8 == 0) {
               TLRPC.User var10 = MessagesController.getInstance(super.currentAccount).getUser(var2);
               MessagesController.getInstance(super.currentAccount).addUserToChat(this.chatId, var10, (TLRPC.ChatFull)null, 0, (String)null, this, (Runnable)null);
            }
         } else if (var7 == 1) {
            TLRPC.TL_channels_editBanned var11 = new TLRPC.TL_channels_editBanned();
            var11.user_id = MessagesController.getInstance(super.currentAccount).getInputUser(var2);
            var11.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chatId);
            var11.banned_rights = new TLRPC.TL_chatBannedRights();
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var11, new _$$Lambda$ChatUsersActivity$YPa3vUbbr6vXSVmrX_ZenS_Yswk(this));
            if (this.searchItem != null && super.actionBar.isSearchFieldVisible()) {
               super.actionBar.closeSearchField();
            }
         }

         if (var7 == 0 && this.type == 0 || var7 == 1) {
            this.removeParticipants(var4);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createMenuForParticipant$9$ChatUsersActivity(ArrayList var1, TLRPC.User var2, int var3, boolean var4, TLObject var5, int var6, TLRPC.TL_chatAdminRights var7, TLRPC.TL_chatBannedRights var8, DialogInterface var9, int var10) {
      if ((Integer)var1.get(var10) == 2) {
         MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chatId, var2, (TLRPC.ChatFull)null);
         this.removeParticipants(var3);
         if (this.searchItem != null && super.actionBar.isSearchFieldVisible()) {
            super.actionBar.closeSearchField();
         }
      } else if (!var4 || !(var5 instanceof TLRPC.TL_channelParticipantAdmin) && !(var5 instanceof TLRPC.TL_chatParticipantAdmin)) {
         this.openRightsEdit2(var3, var6, var5, var7, var8, var4, (Integer)var1.get(var10), false);
      } else {
         AlertDialog.Builder var11 = new AlertDialog.Builder(this.getParentActivity());
         var11.setTitle(LocaleController.getString("AppName", 2131558635));
         var11.setMessage(LocaleController.formatString("AdminWillBeRemoved", 2131558598, ContactsController.formatName(var2.first_name, var2.last_name)));
         var11.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ChatUsersActivity$qfOJbwP_In4NBZQ0LodUcQ4_E5A(this, var3, var6, var5, var7, var8, var4, var1, var10));
         var11.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var11.create());
      }

   }

   // $FF: synthetic method
   public void lambda$createView$4$ChatUsersActivity(View var1, int var2) {
      boolean var3;
      if (this.listView.getAdapter() == this.listViewAdapter) {
         var3 = true;
      } else {
         var3 = false;
      }

      byte var4 = 3;
      boolean var6;
      boolean var7;
      TLRPC.TL_chatBannedRights var12;
      Bundle var21;
      if (var3) {
         ChatUsersActivity var28;
         if (var2 == this.addNewRow) {
            var2 = this.type;
            if (var2 != 0 && var2 != 3) {
               if (var2 == 1) {
                  var21 = new Bundle();
                  var21.putInt("chat_id", this.chatId);
                  var21.putInt("type", 2);
                  var21.putInt("selectType", 1);
                  var28 = new ChatUsersActivity(var21);
                  var28.setDelegate(new _$$Lambda$ChatUsersActivity$RHVQKwjVgC2Fj6Os2cWfGRwR2VY(this));
                  var28.setInfo(this.info);
                  this.presentFragment(var28);
               } else if (var2 == 2) {
                  Bundle var24 = new Bundle();
                  var24.putBoolean("addToGroup", true);
                  String var29;
                  if (this.isChannel) {
                     var29 = "channelId";
                  } else {
                     var29 = "chatId";
                  }

                  var24.putInt(var29, this.currentChat.id);
                  GroupCreateActivity var26 = new GroupCreateActivity(var24);
                  SparseArray var30 = this.contactsMap;
                  if (var30 != null && var30.size() != 0) {
                     var30 = this.contactsMap;
                  } else {
                     var30 = this.participantsMap;
                  }

                  var26.setIgnoreUsers(var30);
                  var26.setDelegate((GroupCreateActivity.ContactsAddActivityDelegate)(new ChatUsersActivity$3(this)));
                  this.presentFragment(var26);
               }
            } else {
               var21 = new Bundle();
               var21.putInt("chat_id", this.chatId);
               var21.putInt("type", 2);
               byte var17 = var4;
               if (this.type == 0) {
                  var17 = 2;
               }

               var21.putInt("selectType", var17);
               var28 = new ChatUsersActivity(var21);
               var28.setInfo(this.info);
               this.presentFragment(var28);
            }

            return;
         }

         if (var2 == this.recentActionsRow) {
            this.presentFragment(new ChannelAdminLogActivity(this.currentChat));
            return;
         }

         if (var2 == this.removedUsersRow) {
            var21 = new Bundle();
            var21.putInt("chat_id", this.chatId);
            var21.putInt("type", 0);
            var28 = new ChatUsersActivity(var21);
            var28.setInfo(this.info);
            this.presentFragment(var28);
            return;
         }

         if (var2 == this.addNew2Row) {
            this.presentFragment(new GroupInviteActivity(this.chatId));
            return;
         }

         if (var2 > this.permissionsSectionRow && var2 <= this.changeInfoRow) {
            TextCheckCell2 var23 = (TextCheckCell2)var1;
            if (!var23.isEnabled()) {
               return;
            }

            if (var23.hasIcon()) {
               if (TextUtils.isEmpty(this.currentChat.username) || var2 != this.pinMessagesRow && var2 != this.changeInfoRow) {
                  Toast.makeText(this.getParentActivity(), LocaleController.getString("EditCantEditPermissions", 2131559319), 0).show();
               } else {
                  Toast.makeText(this.getParentActivity(), LocaleController.getString("EditCantEditPermissionsPublic", 2131559320), 0).show();
               }

               return;
            }

            var23.setChecked(var23.isChecked() ^ true);
            if (var2 == this.changeInfoRow) {
               var12 = this.defaultBannedRights;
               var12.change_info ^= true;
            } else if (var2 == this.addUsersRow) {
               var12 = this.defaultBannedRights;
               var12.invite_users ^= true;
            } else if (var2 == this.pinMessagesRow) {
               var12 = this.defaultBannedRights;
               var12.pin_messages ^= true;
            } else {
               var6 = var23.isChecked();
               if (var2 == this.sendMessagesRow) {
                  var12 = this.defaultBannedRights;
                  var12.send_messages ^= true;
               } else if (var2 == this.sendMediaRow) {
                  var12 = this.defaultBannedRights;
                  var12.send_media ^= true;
               } else if (var2 == this.sendStickersRow) {
                  var12 = this.defaultBannedRights;
                  var7 = var12.send_stickers ^ true;
                  var12.send_inline = var7;
                  var12.send_gifs = var7;
                  var12.send_games = var7;
                  var12.send_stickers = var7;
               } else if (var2 == this.embedLinksRow) {
                  var12 = this.defaultBannedRights;
                  var12.embed_links ^= true;
               } else if (var2 == this.sendPollsRow) {
                  var12 = this.defaultBannedRights;
                  var12.send_polls ^= true;
               }

               RecyclerView.ViewHolder var27;
               if (var6 ^ true) {
                  var12 = this.defaultBannedRights;
                  if (var12.view_messages && !var12.send_messages) {
                     var12.send_messages = true;
                     var27 = this.listView.findViewHolderForAdapterPosition(this.sendMessagesRow);
                     if (var27 != null) {
                        ((TextCheckCell2)var27.itemView).setChecked(false);
                     }
                  }

                  var12 = this.defaultBannedRights;
                  if (var12.view_messages || var12.send_messages) {
                     var12 = this.defaultBannedRights;
                     if (!var12.send_media) {
                        var12.send_media = true;
                        var27 = this.listView.findViewHolderForAdapterPosition(this.sendMediaRow);
                        if (var27 != null) {
                           ((TextCheckCell2)var27.itemView).setChecked(false);
                        }
                     }
                  }

                  var12 = this.defaultBannedRights;
                  if (var12.view_messages || var12.send_messages) {
                     var12 = this.defaultBannedRights;
                     if (!var12.send_polls) {
                        var12.send_polls = true;
                        var27 = this.listView.findViewHolderForAdapterPosition(this.sendPollsRow);
                        if (var27 != null) {
                           ((TextCheckCell2)var27.itemView).setChecked(false);
                        }
                     }
                  }

                  var12 = this.defaultBannedRights;
                  if (var12.view_messages || var12.send_messages) {
                     var12 = this.defaultBannedRights;
                     if (!var12.send_stickers) {
                        var12.send_inline = true;
                        var12.send_gifs = true;
                        var12.send_games = true;
                        var12.send_stickers = true;
                        var27 = this.listView.findViewHolderForAdapterPosition(this.sendStickersRow);
                        if (var27 != null) {
                           ((TextCheckCell2)var27.itemView).setChecked(false);
                        }
                     }
                  }

                  var12 = this.defaultBannedRights;
                  if (var12.view_messages || var12.send_messages) {
                     var12 = this.defaultBannedRights;
                     if (!var12.embed_links) {
                        var12.embed_links = true;
                        var27 = this.listView.findViewHolderForAdapterPosition(this.embedLinksRow);
                        if (var27 != null) {
                           ((TextCheckCell2)var27.itemView).setChecked(false);
                        }
                     }
                  }
               } else {
                  var12 = this.defaultBannedRights;
                  if (!var12.embed_links || !var12.send_inline || !var12.send_media || !var12.send_polls) {
                     var12 = this.defaultBannedRights;
                     if (var12.send_messages) {
                        var12.send_messages = false;
                        var27 = this.listView.findViewHolderForAdapterPosition(this.sendMessagesRow);
                        if (var27 != null) {
                           ((TextCheckCell2)var27.itemView).setChecked(true);
                        }
                     }
                  }
               }
            }

            return;
         }
      }

      Object var5;
      TLRPC.TL_chatBannedRights var9;
      TLObject var11;
      TLObject var22;
      if (var3) {
         var11 = this.listViewAdapter.getItem(var2);
         if (var11 instanceof TLRPC.ChannelParticipant) {
            TLRPC.ChannelParticipant var8 = (TLRPC.ChannelParticipant)var11;
            var2 = var8.user_id;
            var9 = var8.banned_rights;
            var5 = var8.admin_rights;
            if ((var8 instanceof TLRPC.TL_channelParticipantAdmin || var8 instanceof TLRPC.TL_channelParticipantCreator) && !var8.can_edit) {
               var6 = false;
            } else {
               var6 = true;
            }

            if (var11 instanceof TLRPC.TL_channelParticipantCreator) {
               var5 = new TLRPC.TL_chatAdminRights();
               ((TLRPC.TL_chatAdminRights)var5).add_admins = true;
               ((TLRPC.TL_chatAdminRights)var5).pin_messages = true;
               ((TLRPC.TL_chatAdminRights)var5).invite_users = true;
               ((TLRPC.TL_chatAdminRights)var5).ban_users = true;
               ((TLRPC.TL_chatAdminRights)var5).delete_messages = true;
               ((TLRPC.TL_chatAdminRights)var5).edit_messages = true;
               ((TLRPC.TL_chatAdminRights)var5).post_messages = true;
               ((TLRPC.TL_chatAdminRights)var5).change_info = true;
            }

            var22 = var11;
            var12 = var9;
         } else if (var11 instanceof TLRPC.ChatParticipant) {
            var2 = ((TLRPC.ChatParticipant)var11).user_id;
            var6 = this.currentChat.creator;
            if (var11 instanceof TLRPC.TL_chatParticipantCreator) {
               var5 = new TLRPC.TL_chatAdminRights();
               ((TLRPC.TL_chatAdminRights)var5).add_admins = true;
               ((TLRPC.TL_chatAdminRights)var5).pin_messages = true;
               ((TLRPC.TL_chatAdminRights)var5).invite_users = true;
               ((TLRPC.TL_chatAdminRights)var5).ban_users = true;
               ((TLRPC.TL_chatAdminRights)var5).delete_messages = true;
               ((TLRPC.TL_chatAdminRights)var5).edit_messages = true;
               ((TLRPC.TL_chatAdminRights)var5).post_messages = true;
               ((TLRPC.TL_chatAdminRights)var5).change_info = true;
            } else {
               var5 = null;
            }

            var22 = var11;
            var12 = null;
         } else {
            var22 = var11;
            var12 = null;
            var5 = var12;
            var6 = false;
            var2 = 0;
         }
      } else {
         TLObject var18 = this.searchListViewAdapter.getItem(var2);
         if (var18 instanceof TLRPC.User) {
            TLRPC.User var13 = (TLRPC.User)var18;
            MessagesController.getInstance(super.currentAccount).putUser(var13, false);
            var2 = var13.id;
            var11 = this.getAnyParticipant(var2);
         } else {
            var11 = var18;
            if (!(var18 instanceof TLRPC.ChannelParticipant)) {
               if (var18 instanceof TLRPC.ChatParticipant) {
                  var11 = var18;
               } else {
                  var11 = null;
               }
            }

            var2 = 0;
         }

         if (var11 instanceof TLRPC.ChannelParticipant) {
            if (var11 instanceof TLRPC.TL_channelParticipantCreator) {
               return;
            }

            TLRPC.ChannelParticipant var19 = (TLRPC.ChannelParticipant)var11;
            var2 = var19.user_id;
            if ((var19 instanceof TLRPC.TL_channelParticipantAdmin || var19 instanceof TLRPC.TL_channelParticipantCreator) && !var19.can_edit) {
               var6 = false;
            } else {
               var6 = true;
            }

            var9 = var19.banned_rights;
            var5 = var19.admin_rights;
            var22 = var11;
            var12 = var9;
         } else if (var11 instanceof TLRPC.ChatParticipant) {
            if (var11 instanceof TLRPC.TL_chatParticipantCreator) {
               return;
            }

            var2 = ((TLRPC.ChatParticipant)var11).user_id;
            var6 = this.currentChat.creator;
            var22 = var11;
            var12 = null;
            var5 = var12;
         } else if (var11 == null) {
            var22 = var11;
            var12 = null;
            var5 = var12;
            var6 = true;
         } else {
            var22 = var11;
            var12 = null;
            var5 = var12;
            var6 = false;
         }
      }

      if (var2 != 0) {
         int var14 = this.selectType;
         byte var15;
         if (var14 != 0) {
            if (var14 != 3 && var14 != 1) {
               this.removeUser(var2);
            } else if (this.selectType != 1 && var6 && (var22 instanceof TLRPC.TL_channelParticipantAdmin || var22 instanceof TLRPC.TL_chatParticipantAdmin)) {
               TLRPC.User var10 = MessagesController.getInstance(super.currentAccount).getUser(var2);
               AlertDialog.Builder var25 = new AlertDialog.Builder(this.getParentActivity());
               var25.setTitle(LocaleController.getString("AppName", 2131558635));
               var25.setMessage(LocaleController.formatString("AdminWillBeRemoved", 2131558598, ContactsController.formatName(var10.first_name, var10.last_name)));
               var25.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ChatUsersActivity$xwU0MxcdyPOafR1XH_TLaX0p0yc(this, var10, var22, (TLRPC.TL_chatAdminRights)var5, var12, var6));
               var25.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
               this.showDialog(var25.create());
            } else {
               if (this.selectType == 1) {
                  var15 = 0;
               } else {
                  var15 = 1;
               }

               if (this.selectType == 1) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               this.openRightsEdit(var2, var22, (TLRPC.TL_chatAdminRights)var5, var12, var6, var15, var7);
            }
         } else {
            var14 = this.type;
            if (var14 != 1) {
               if (var14 != 0 && var14 != 3) {
                  var6 = false;
               } else {
                  var6 = ChatObject.canBlockUsers(this.currentChat);
               }
            } else if (var2 == UserConfig.getInstance(super.currentAccount).getClientUserId() || !this.currentChat.creator && !var6) {
               var6 = false;
            } else {
               var6 = true;
            }

            var14 = this.type;
            if (var14 == 0 || var14 != 1 && this.isChannel || this.type == 2 && this.selectType == 0) {
               if (var2 == this.getUserConfig().getClientUserId()) {
                  return;
               }

               var21 = new Bundle();
               var21.putInt("user_id", var2);
               this.presentFragment(new ProfileActivity(var21));
            } else {
               if (var12 == null) {
                  var12 = new TLRPC.TL_chatBannedRights();
                  var12.view_messages = true;
                  var12.send_stickers = true;
                  var12.send_media = true;
                  var12.embed_links = true;
                  var12.send_messages = true;
                  var12.send_games = true;
                  var12.send_inline = true;
                  var12.send_gifs = true;
                  var12.pin_messages = true;
                  var12.send_polls = true;
                  var12.invite_users = true;
                  var12.change_info = true;
               }

               int var16 = this.chatId;
               var9 = this.defaultBannedRights;
               if (this.type == 1) {
                  var15 = 0;
               } else {
                  var15 = 1;
               }

               if (var22 == null) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               ChatRightsEditActivity var20 = new ChatRightsEditActivity(var2, var16, (TLRPC.TL_chatAdminRights)var5, var9, var12, var15, var6, var7);
               var20.setDelegate(new _$$Lambda$ChatUsersActivity$YHObzh31hcmoIGxeIXPRtV6u3bY(this, var22));
               this.presentFragment(var20);
            }
         }
      }

   }

   // $FF: synthetic method
   public boolean lambda$createView$5$ChatUsersActivity(View var1, int var2) {
      Activity var6 = this.getParentActivity();
      boolean var3 = false;
      boolean var4 = var3;
      if (var6 != null) {
         RecyclerView.Adapter var7 = this.listView.getAdapter();
         ChatUsersActivity.ListAdapter var5 = this.listViewAdapter;
         var4 = var3;
         if (var7 == var5) {
            var4 = var3;
            if (this.createMenuForParticipant(var5.getItem(var2), false)) {
               var4 = true;
            }
         }
      }

      return var4;
   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$15$ChatUsersActivity() {
      this.loadChatParticipants(0, 200);
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$22$ChatUsersActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof ManageChatUserCell) {
               ((ManageChatUserCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$loadChatParticipants$21$ChatUsersActivity(TLRPC.TL_channels_getParticipants var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatUsersActivity$IhBMaAxofZIxg2fr49it1UefZbE(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public int lambda$null$0$ChatUsersActivity(TLObject var1, TLObject var2) {
      int var3 = this.getChannelAdminParticipantType(var1);
      int var4 = this.getChannelAdminParticipantType(var2);
      if (var3 > var4) {
         return 1;
      } else {
         return var3 < var4 ? -1 : 0;
      }
   }

   // $FF: synthetic method
   public void lambda$null$1$ChatUsersActivity(int var1, TLObject var2) {
      if (var2 != null && this.participantsMap.get(var1) == null) {
         this.participants.add(var2);
         Collections.sort(this.participants, new _$$Lambda$ChatUsersActivity$M5Fh2EarJwZERsobLDwzO3z4iPk(this));
         this.updateRows();
         this.listViewAdapter.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$null$10$ChatUsersActivity(TLObject var1, int var2, TLRPC.TL_chatAdminRights var3, TLRPC.TL_chatBannedRights var4) {
      if (var1 instanceof TLRPC.ChannelParticipant) {
         TLRPC.ChannelParticipant var5 = (TLRPC.ChannelParticipant)var1;
         var5.admin_rights = var3;
         var5.banned_rights = var4;
         this.updateParticipantWithRights(var5, var3, var4, 0, false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$11$ChatUsersActivity(TLObject var1, int var2, TLRPC.TL_chatAdminRights var3, TLRPC.TL_chatBannedRights var4) {
      if (var1 instanceof TLRPC.ChannelParticipant) {
         TLRPC.ChannelParticipant var5 = (TLRPC.ChannelParticipant)var1;
         var5.admin_rights = var3;
         var5.banned_rights = var4;
         this.updateParticipantWithRights(var5, var3, var4, 0, false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$12$ChatUsersActivity(TLRPC.Updates var1) {
      TLRPC.Chat var2 = (TLRPC.Chat)var1.chats.get(0);
      MessagesController.getInstance(super.currentAccount).loadFullChat(var2.id, 0, true);
   }

   // $FF: synthetic method
   public void lambda$null$13$ChatUsersActivity(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         TLRPC.Updates var3 = (TLRPC.Updates)var1;
         MessagesController.getInstance(super.currentAccount).processUpdates(var3, false);
         if (!var3.chats.isEmpty()) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatUsersActivity$4z_sufq32stWQmeljAxBuSXODwQ(this, var3), 1000L);
         }
      }

   }

   // $FF: synthetic method
   public int lambda$null$18$ChatUsersActivity(int var1, TLObject var2, TLObject var3) {
      int var5;
      TLRPC.User var7;
      label85: {
         TLRPC.ChannelParticipant var4 = (TLRPC.ChannelParticipant)var2;
         TLRPC.ChannelParticipant var6 = (TLRPC.ChannelParticipant)var3;
         TLRPC.User var8 = MessagesController.getInstance(super.currentAccount).getUser(var4.user_id);
         var7 = MessagesController.getInstance(super.currentAccount).getUser(var6.user_id);
         if (var8 != null) {
            TLRPC.UserStatus var10 = var8.status;
            if (var10 != null) {
               if (var8.self) {
                  var5 = var1 + '썐';
               } else {
                  var5 = var10.expires;
               }
               break label85;
            }
         }

         var5 = 0;
      }

      label80: {
         if (var7 != null) {
            TLRPC.UserStatus var9 = var7.status;
            if (var9 != null) {
               if (var7.self) {
                  var1 += 50000;
               } else {
                  var1 = var9.expires;
               }
               break label80;
            }
         }

         var1 = 0;
      }

      if (var5 > 0 && var1 > 0) {
         if (var5 > var1) {
            return 1;
         } else {
            return var5 < var1 ? -1 : 0;
         }
      } else if (var5 < 0 && var1 < 0) {
         if (var5 > var1) {
            return 1;
         } else {
            return var5 < var1 ? -1 : 0;
         }
      } else if (var5 < 0 && var1 > 0 || var5 == 0 && var1 != 0) {
         return -1;
      } else {
         return (var1 >= 0 || var5 <= 0) && (var1 != 0 || var5 == 0) ? 0 : 1;
      }
   }

   // $FF: synthetic method
   public int lambda$null$19$ChatUsersActivity(TLObject var1, TLObject var2) {
      int var3 = this.getChannelAdminParticipantType(var1);
      int var4 = this.getChannelAdminParticipantType(var2);
      if (var3 > var4) {
         return 1;
      } else {
         return var3 < var4 ? -1 : 0;
      }
   }

   // $FF: synthetic method
   public void lambda$null$2$ChatUsersActivity(TLRPC.User var1, TLObject var2, TLRPC.TL_chatAdminRights var3, TLRPC.TL_chatBannedRights var4, boolean var5, DialogInterface var6, int var7) {
      int var8 = var1.id;
      byte var9;
      if (this.selectType == 1) {
         var9 = 0;
      } else {
         var9 = 1;
      }

      this.openRightsEdit(var8, var2, var3, var4, var5, var9, false);
   }

   // $FF: synthetic method
   public void lambda$null$20$ChatUsersActivity(TLRPC.TL_error var1, TLObject var2, TLRPC.TL_channels_getParticipants var3) {
      if (var1 == null) {
         TLRPC.TL_channels_channelParticipants var4 = (TLRPC.TL_channels_channelParticipants)var2;
         MessagesController.getInstance(super.currentAccount).putUsers(var4.users, false);
         int var5 = UserConfig.getInstance(super.currentAccount).getClientUserId();
         int var6;
         if (this.selectType != 0) {
            for(var6 = 0; var6 < var4.participants.size(); ++var6) {
               if (((TLRPC.ChannelParticipant)var4.participants.get(var6)).user_id == var5) {
                  var4.participants.remove(var6);
                  break;
               }
            }
         }

         SparseArray var7;
         EmptyTextProgressView var8;
         ArrayList var16;
         ArrayList var18;
         if (this.type == 2) {
            --this.delayResults;
            TLRPC.ChannelParticipantsFilter var12 = var3.filter;
            ArrayList var13;
            SparseArray var14;
            if (var12 instanceof TLRPC.TL_channelParticipantsContacts) {
               var13 = this.contacts;
               var14 = this.contactsMap;
            } else if (var12 instanceof TLRPC.TL_channelParticipantsBots) {
               var13 = this.bots;
               var14 = this.botsMap;
            } else {
               var13 = this.participants;
               var14 = this.participantsMap;
            }

            var16 = var13;
            var7 = var14;
            if (this.delayResults <= 0) {
               var8 = this.emptyView;
               var16 = var13;
               var7 = var14;
               if (var8 != null) {
                  var8.showTextView();
                  var16 = var13;
                  var7 = var14;
               }
            }
         } else {
            var18 = this.participants;
            SparseArray var15 = this.participantsMap;
            var15.clear();
            var8 = this.emptyView;
            var16 = var18;
            var7 = var15;
            if (var8 != null) {
               var8.showTextView();
               var7 = var15;
               var16 = var18;
            }
         }

         var16.clear();
         var16.addAll(var4.participants);
         var5 = var4.participants.size();

         TLRPC.ChannelParticipant var17;
         for(var6 = 0; var6 < var5; ++var6) {
            var17 = (TLRPC.ChannelParticipant)var4.participants.get(var6);
            var7.put(var17.user_id, var17);
         }

         if (this.type == 2) {
            var5 = this.participants.size();

            int var10;
            for(var6 = 0; var6 < var5; var5 = var10) {
               int var9;
               label90: {
                  var17 = (TLRPC.ChannelParticipant)this.participants.get(var6);
                  if (this.contactsMap.get(var17.user_id) == null) {
                     var9 = var6;
                     var10 = var5;
                     if (this.botsMap.get(var17.user_id) == null) {
                        break label90;
                     }
                  }

                  this.participants.remove(var6);
                  this.participantsMap.remove(var17.user_id);
                  var9 = var6 - 1;
                  var10 = var5 - 1;
               }

               var6 = var9 + 1;
            }
         }

         try {
            if ((this.type == 0 || this.type == 3 || this.type == 2) && this.currentChat != null && this.currentChat.megagroup && this.info instanceof TLRPC.TL_channelFull && this.info.participants_count <= 200) {
               var6 = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime();
               _$$Lambda$ChatUsersActivity$JfDHhpbwPgHf9ZtKvhAah5Ozjtk var20 = new _$$Lambda$ChatUsersActivity$JfDHhpbwPgHf9ZtKvhAah5Ozjtk(this, var6);
               Collections.sort(var16, var20);
            } else if (this.type == 1) {
               var18 = this.participants;
               _$$Lambda$ChatUsersActivity$kCXpt6NAhotHD9nHkOp2DvagI6I var19 = new _$$Lambda$ChatUsersActivity$kCXpt6NAhotHD9nHkOp2DvagI6I(this);
               Collections.sort(var18, var19);
            }
         } catch (Exception var11) {
            FileLog.e((Throwable)var11);
         }
      }

      if (this.type != 2 || this.delayResults <= 0) {
         this.loadingUsers = false;
         this.firstLoaded = true;
      }

      this.updateRows();
      ChatUsersActivity.ListAdapter var21 = this.listViewAdapter;
      if (var21 != null) {
         var21.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$ChatUsersActivity(TLObject var1, int var2, TLRPC.TL_chatAdminRights var3, TLRPC.TL_chatBannedRights var4) {
      if (var1 instanceof TLRPC.ChannelParticipant) {
         TLRPC.ChannelParticipant var5 = (TLRPC.ChannelParticipant)var1;
         var5.admin_rights = var3;
         var5.banned_rights = var4;
         this.updateParticipantWithRights(var5, var3, var4, 0, false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$8$ChatUsersActivity(int var1, int var2, TLObject var3, TLRPC.TL_chatAdminRights var4, TLRPC.TL_chatBannedRights var5, boolean var6, ArrayList var7, int var8, DialogInterface var9, int var10) {
      this.openRightsEdit2(var1, var2, var3, var4, var5, var6, (Integer)var7.get(var8), false);
   }

   // $FF: synthetic method
   public void lambda$openRightsEdit$7$ChatUsersActivity(TLObject var1, boolean var2, int var3, TLRPC.TL_chatAdminRights var4, TLRPC.TL_chatBannedRights var5) {
      if (var1 instanceof TLRPC.ChannelParticipant) {
         TLRPC.ChannelParticipant var6 = (TLRPC.ChannelParticipant)var1;
         var6.admin_rights = var4;
         var6.banned_rights = var5;
      }

      if (var2) {
         this.removeSelfFromStack();
      }

   }

   // $FF: synthetic method
   public void lambda$openRightsEdit2$6$ChatUsersActivity(int var1, int var2, int var3, int var4, TLRPC.TL_chatAdminRights var5, TLRPC.TL_chatBannedRights var6) {
      if (var1 == 0) {
         for(var1 = 0; var1 < this.participants.size(); ++var1) {
            TLObject var7 = (TLObject)this.participants.get(var1);
            Object var10;
            if (var7 instanceof TLRPC.ChannelParticipant) {
               if (((TLRPC.ChannelParticipant)var7).user_id == var2) {
                  if (var4 == 1) {
                     var10 = new TLRPC.TL_channelParticipantAdmin();
                  } else {
                     var10 = new TLRPC.TL_channelParticipant();
                  }

                  ((TLRPC.ChannelParticipant)var10).admin_rights = var5;
                  ((TLRPC.ChannelParticipant)var10).banned_rights = var6;
                  ((TLRPC.ChannelParticipant)var10).inviter_id = UserConfig.getInstance(super.currentAccount).getClientUserId();
                  ((TLRPC.ChannelParticipant)var10).user_id = var2;
                  ((TLRPC.ChannelParticipant)var10).date = var3;
                  this.participants.set(var1, var10);
                  break;
               }
            } else if (var7 instanceof TLRPC.ChatParticipant) {
               TLRPC.ChatParticipant var8 = (TLRPC.ChatParticipant)var7;
               if (var4 == 1) {
                  var10 = new TLRPC.TL_chatParticipantAdmin();
               } else {
                  var10 = new TLRPC.TL_chatParticipant();
               }

               ((TLRPC.ChatParticipant)var10).user_id = var8.user_id;
               ((TLRPC.ChatParticipant)var10).date = var8.date;
               ((TLRPC.ChatParticipant)var10).inviter_id = var8.inviter_id;
               int var9 = this.info.participants.participants.indexOf(var8);
               if (var9 >= 0) {
                  this.info.participants.participants.set(var9, var10);
               }

               this.loadChatParticipants(0, 200);
            }
         }
      } else if (var1 == 1 && var4 == 0) {
         this.removeParticipants(var2);
      }

   }

   public boolean onBackPressed() {
      return this.checkDiscard();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
      this.loadChatParticipants(0, 200);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
   }

   public void onResume() {
      super.onResume();
      AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
      AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
      ChatUsersActivity.ListAdapter var1 = this.listViewAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   protected void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1 && !var2 && this.needOpenSearch) {
         this.searchItem.openSearch(true);
      }

   }

   public void setDelegate(ChatUsersActivity.ChatUsersActivityDelegate var1) {
      this.delegate = var1;
   }

   public void setInfo(TLRPC.ChatFull var1) {
      this.info = var1;
   }

   public interface ChatUsersActivityDelegate {
      void didAddParticipantToList(int var1, TLObject var2);
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public TLObject getItem(int var1) {
         if (var1 >= ChatUsersActivity.this.participantsStartRow && var1 < ChatUsersActivity.this.participantsEndRow) {
            return (TLObject)ChatUsersActivity.this.participants.get(var1 - ChatUsersActivity.this.participantsStartRow);
         } else if (var1 >= ChatUsersActivity.this.contactsStartRow && var1 < ChatUsersActivity.this.contactsEndRow) {
            return (TLObject)ChatUsersActivity.this.contacts.get(var1 - ChatUsersActivity.this.contactsStartRow);
         } else {
            return var1 >= ChatUsersActivity.this.botStartRow && var1 < ChatUsersActivity.this.botEndRow ? (TLObject)ChatUsersActivity.this.bots.get(var1 - ChatUsersActivity.this.botStartRow) : null;
         }
      }

      public int getItemCount() {
         return ChatUsersActivity.this.loadingUsers && !ChatUsersActivity.this.firstLoaded ? 0 : ChatUsersActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != ChatUsersActivity.this.addNewRow && var1 != ChatUsersActivity.this.addNew2Row && var1 != ChatUsersActivity.this.recentActionsRow) {
            if (var1 >= ChatUsersActivity.this.participantsStartRow && var1 < ChatUsersActivity.this.participantsEndRow || var1 >= ChatUsersActivity.this.botStartRow && var1 < ChatUsersActivity.this.botEndRow || var1 >= ChatUsersActivity.this.contactsStartRow && var1 < ChatUsersActivity.this.contactsEndRow) {
               return 0;
            } else if (var1 != ChatUsersActivity.this.addNewSectionRow && var1 != ChatUsersActivity.this.participantsDividerRow && var1 != ChatUsersActivity.this.participantsDivider2Row) {
               if (var1 != ChatUsersActivity.this.restricted1SectionRow && var1 != ChatUsersActivity.this.permissionsSectionRow) {
                  if (var1 == ChatUsersActivity.this.participantsInfoRow) {
                     return 1;
                  } else if (var1 == ChatUsersActivity.this.blockedEmptyRow) {
                     return 4;
                  } else if (var1 == ChatUsersActivity.this.removedUsersRow) {
                     return 6;
                  } else if (var1 != ChatUsersActivity.this.changeInfoRow && var1 != ChatUsersActivity.this.addUsersRow && var1 != ChatUsersActivity.this.pinMessagesRow && var1 != ChatUsersActivity.this.sendMessagesRow && var1 != ChatUsersActivity.this.sendMediaRow && var1 != ChatUsersActivity.this.sendStickersRow && var1 != ChatUsersActivity.this.embedLinksRow && var1 != ChatUsersActivity.this.sendPollsRow) {
                     return var1 != ChatUsersActivity.this.membersHeaderRow && var1 != ChatUsersActivity.this.contactsHeaderRow && var1 != ChatUsersActivity.this.botHeaderRow ? 0 : 8;
                  } else {
                     return 7;
                  }
               } else {
                  return 5;
               }
            } else {
               return 3;
            }
         } else {
            return 2;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         if (var2 == 7) {
            return ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat);
         } else {
            boolean var3 = false;
            if (var2 == 0) {
               TLObject var4 = ((ManageChatUserCell)var1.itemView).getCurrentObject();
               return !(var4 instanceof TLRPC.User) || !((TLRPC.User)var4).self;
            } else {
               if (var2 == 0 || var2 == 2 || var2 == 6) {
                  var3 = true;
               }

               return var3;
            }
         }
      }

      // $FF: synthetic method
      public boolean lambda$onCreateViewHolder$0$ChatUsersActivity$ListAdapter(ManageChatUserCell var1, boolean var2) {
         TLObject var3 = ChatUsersActivity.this.listViewAdapter.getItem((Integer)var1.getTag());
         return ChatUsersActivity.this.createMenuForParticipant(var3, var2 ^ true);
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = false;
         boolean var9 = false;
         boolean var10 = false;
         String var11;
         String var18;
         switch(var3) {
         case 0:
            ManageChatUserCell var28 = (ManageChatUserCell)var1.itemView;
            var28.setTag(var2);
            TLObject var22 = this.getItem(var2);
            if (var2 >= ChatUsersActivity.this.participantsStartRow && var2 < ChatUsersActivity.this.participantsEndRow) {
               var3 = ChatUsersActivity.this.participantsEndRow;
            } else if (var2 >= ChatUsersActivity.this.contactsStartRow && var2 < ChatUsersActivity.this.contactsEndRow) {
               var3 = ChatUsersActivity.this.contactsEndRow;
            } else {
               var3 = ChatUsersActivity.this.botEndRow;
            }

            int var13;
            int var14;
            int var15;
            TLRPC.TL_chatBannedRights var23;
            if (var22 instanceof TLRPC.ChannelParticipant) {
               TLRPC.ChannelParticipant var12 = (TLRPC.ChannelParticipant)var22;
               var13 = var12.user_id;
               var14 = var12.kicked_by;
               var15 = var12.promoted_by;
               var23 = var12.banned_rights;
               var10 = var12 instanceof TLRPC.TL_channelParticipantBanned;
               var5 = var12 instanceof TLRPC.TL_channelParticipantCreator;
               var4 = var12 instanceof TLRPC.TL_channelParticipantAdmin;
            } else {
               TLRPC.ChatParticipant var24 = (TLRPC.ChatParticipant)var22;
               var13 = var24.user_id;
               var5 = var24 instanceof TLRPC.TL_chatParticipantCreator;
               var4 = var24 instanceof TLRPC.TL_chatParticipantAdmin;
               var23 = null;
               var14 = 0;
               var15 = 0;
               var10 = false;
            }

            TLRPC.User var27 = MessagesController.getInstance(ChatUsersActivity.access$3100(ChatUsersActivity.this)).getUser(var13);
            if (var27 != null) {
               if (ChatUsersActivity.this.type == 3) {
                  var18 = ChatUsersActivity.this.formatUserPermissions(var23);
                  var10 = var6;
                  if (var2 != var3 - 1) {
                     var10 = true;
                  }

                  var28.setData(var27, (CharSequence)null, var18, var10);
               } else {
                  TLRPC.User var25;
                  if (ChatUsersActivity.this.type == 0) {
                     label227: {
                        if (var10) {
                           var25 = MessagesController.getInstance(ChatUsersActivity.access$3300(ChatUsersActivity.this)).getUser(var14);
                           if (var25 != null) {
                              var18 = LocaleController.formatString("UserRemovedBy", 2131560992, ContactsController.formatName(var25.first_name, var25.last_name));
                              break label227;
                           }
                        }

                        var18 = null;
                     }

                     var10 = var7;
                     if (var2 != var3 - 1) {
                        var10 = true;
                     }

                     var28.setData(var27, (CharSequence)null, var18, var10);
                  } else if (ChatUsersActivity.this.type == 1) {
                     if (var5) {
                        var18 = LocaleController.getString("ChannelCreator", 2131558942);
                     } else {
                        label347: {
                           if (var4) {
                              var25 = MessagesController.getInstance(ChatUsersActivity.access$3400(ChatUsersActivity.this)).getUser(var15);
                              if (var25 != null) {
                                 var18 = LocaleController.formatString("EditAdminPromotedBy", 2131559315, ContactsController.formatName(var25.first_name, var25.last_name));
                                 break label347;
                              }
                           }

                           var18 = null;
                        }
                     }

                     var10 = var8;
                     if (var2 != var3 - 1) {
                        var10 = true;
                     }

                     var28.setData(var27, (CharSequence)null, var18, var10);
                  } else if (ChatUsersActivity.this.type == 2) {
                     var10 = var9;
                     if (var2 != var3 - 1) {
                        var10 = true;
                     }

                     var28.setData(var27, (CharSequence)null, (CharSequence)null, var10);
                  }
               }
            }
            break;
         case 1:
            TextInfoPrivacyCell var21 = (TextInfoPrivacyCell)var1.itemView;
            if (var2 == ChatUsersActivity.this.participantsInfoRow) {
               if (ChatUsersActivity.this.type != 0 && ChatUsersActivity.this.type != 3) {
                  if (ChatUsersActivity.this.type == 1) {
                     if (ChatUsersActivity.this.addNewRow != -1) {
                        if (ChatUsersActivity.this.isChannel) {
                           var21.setText(LocaleController.getString("ChannelAdminsInfo", 2131558928));
                        } else {
                           var21.setText(LocaleController.getString("MegaAdminsInfo", 2131559826));
                        }

                        var21.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                     } else {
                        var21.setText("");
                        var21.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                     }
                  } else if (ChatUsersActivity.this.type == 2) {
                     if (ChatUsersActivity.this.isChannel && ChatUsersActivity.this.selectType == 0) {
                        var21.setText(LocaleController.getString("ChannelMembersInfo", 2131558963));
                     } else {
                        var21.setText("");
                     }

                     var21.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  }
               } else {
                  if (ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat)) {
                     if (ChatUsersActivity.this.isChannel) {
                        var21.setText(LocaleController.getString("NoBlockedChannel2", 2131559914));
                     } else {
                        var21.setText(LocaleController.getString("NoBlockedGroup2", 2131559915));
                     }
                  } else if (ChatUsersActivity.this.isChannel) {
                     var21.setText(LocaleController.getString("NoBlockedChannel2", 2131559914));
                  } else {
                     var21.setText(LocaleController.getString("NoBlockedGroup2", 2131559915));
                  }

                  var21.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               }
            }
            break;
         case 2:
            ManageChatTextCell var20 = (ManageChatTextCell)var1.itemView;
            var20.setColors("windowBackgroundWhiteGrayIcon", "windowBackgroundWhiteBlackText");
            if (var2 == ChatUsersActivity.this.addNewRow) {
               if (ChatUsersActivity.this.type == 3) {
                  var20.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                  var11 = LocaleController.getString("ChannelAddException", 2131558919);
                  if (ChatUsersActivity.this.participantsStartRow != -1) {
                     var10 = true;
                  }

                  var20.setText(var11, (String)null, 2131165275, var10);
               } else if (ChatUsersActivity.this.type == 0) {
                  var20.setText(LocaleController.getString("ChannelBlockUser", 2131558933), (String)null, 2131165275, false);
               } else if (ChatUsersActivity.this.type == 1) {
                  var20.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                  var20.setText(LocaleController.getString("ChannelAddAdmin", 2131558918), (String)null, 2131165279, true);
               } else if (ChatUsersActivity.this.type == 2) {
                  var20.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                  if (ChatUsersActivity.this.isChannel) {
                     var11 = LocaleController.getString("AddSubscriber", 2131558588);
                     var10 = var4;
                     if (ChatUsersActivity.this.membersHeaderRow == -1) {
                        var10 = var4;
                        if (!ChatUsersActivity.this.participants.isEmpty()) {
                           var10 = true;
                        }
                     }

                     var20.setText(var11, (String)null, 2131165272, var10);
                  } else {
                     var11 = LocaleController.getString("AddMember", 2131558573);
                     var10 = var5;
                     if (ChatUsersActivity.this.membersHeaderRow == -1) {
                        var10 = var5;
                        if (!ChatUsersActivity.this.participants.isEmpty()) {
                           var10 = true;
                        }
                     }

                     var20.setText(var11, (String)null, 2131165272, var10);
                  }
               }
            } else if (var2 == ChatUsersActivity.this.recentActionsRow) {
               var20.setText(LocaleController.getString("EventLog", 2131559382), (String)null, 2131165405, false);
            } else if (var2 == ChatUsersActivity.this.addNew2Row) {
               var20.setText(LocaleController.getString("ChannelInviteViaLink", 2131558953), (String)null, 2131165787, true);
            }
            break;
         case 3:
            if (var2 == ChatUsersActivity.this.addNewSectionRow || ChatUsersActivity.this.type == 3 && var2 == ChatUsersActivity.this.participantsDividerRow && ChatUsersActivity.this.addNewRow == -1 && ChatUsersActivity.this.participantsStartRow == -1) {
               var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            } else {
               var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
            }
         case 4:
         default:
            break;
         case 5:
            HeaderCell var19 = (HeaderCell)var1.itemView;
            if (var2 == ChatUsersActivity.this.restricted1SectionRow) {
               if (ChatUsersActivity.this.type == 0) {
                  if (ChatUsersActivity.this.info != null) {
                     var2 = ChatUsersActivity.this.info.kicked_count;
                  } else {
                     var2 = ChatUsersActivity.this.participants.size();
                  }

                  if (var2 != 0) {
                     var19.setText(LocaleController.formatPluralString("RemovedUser", var2));
                  } else {
                     var19.setText(LocaleController.getString("ChannelBlockedUsers", 2131558934));
                  }
               } else {
                  var19.setText(LocaleController.getString("ChannelRestrictedUsers", 2131558996));
               }
            } else if (var2 == ChatUsersActivity.this.permissionsSectionRow) {
               var19.setText(LocaleController.getString("ChannelPermissionsHeader", 2131558986));
            }
            break;
         case 6:
            TextSettingsCell var26 = (TextSettingsCell)var1.itemView;
            var18 = LocaleController.getString("ChannelBlacklist", 2131558932);
            if (ChatUsersActivity.this.info != null) {
               var2 = ChatUsersActivity.this.info.kicked_count;
            } else {
               var2 = 0;
            }

            var26.setTextAndValue(var18, String.format("%d", var2), false);
            break;
         case 7:
            TextCheckCell2 var17 = (TextCheckCell2)var1.itemView;
            if (var2 == ChatUsersActivity.this.changeInfoRow) {
               var11 = LocaleController.getString("UserRestrictionsChangeInfo", 2131560999);
               if (!ChatUsersActivity.this.defaultBannedRights.change_info && TextUtils.isEmpty(ChatUsersActivity.this.currentChat.username)) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               var17.setTextAndCheck(var11, var10, false);
            } else if (var2 == ChatUsersActivity.this.addUsersRow) {
               var17.setTextAndCheck(LocaleController.getString("UserRestrictionsInviteUsers", 2131561003), ChatUsersActivity.this.defaultBannedRights.invite_users ^ true, true);
            } else if (var2 != ChatUsersActivity.this.pinMessagesRow) {
               if (var2 == ChatUsersActivity.this.sendMessagesRow) {
                  var17.setTextAndCheck(LocaleController.getString("UserRestrictionsSend", 2131561015), ChatUsersActivity.this.defaultBannedRights.send_messages ^ true, true);
               } else if (var2 == ChatUsersActivity.this.sendMediaRow) {
                  var17.setTextAndCheck(LocaleController.getString("UserRestrictionsSendMedia", 2131561016), ChatUsersActivity.this.defaultBannedRights.send_media ^ true, true);
               } else if (var2 == ChatUsersActivity.this.sendStickersRow) {
                  var17.setTextAndCheck(LocaleController.getString("UserRestrictionsSendStickers", 2131561018), ChatUsersActivity.this.defaultBannedRights.send_stickers ^ true, true);
               } else if (var2 == ChatUsersActivity.this.embedLinksRow) {
                  var17.setTextAndCheck(LocaleController.getString("UserRestrictionsEmbedLinks", 2131561002), ChatUsersActivity.this.defaultBannedRights.embed_links ^ true, true);
               } else if (var2 == ChatUsersActivity.this.sendPollsRow) {
                  var17.setTextAndCheck(LocaleController.getString("UserRestrictionsSendPolls", 2131561017), ChatUsersActivity.this.defaultBannedRights.send_polls ^ true, true);
               }
            } else {
               var11 = LocaleController.getString("UserRestrictionsPinMessages", 2131561013);
               if (!ChatUsersActivity.this.defaultBannedRights.pin_messages && TextUtils.isEmpty(ChatUsersActivity.this.currentChat.username)) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               var17.setTextAndCheck(var11, var10, true);
            }

            if (var2 != ChatUsersActivity.this.sendMediaRow && var2 != ChatUsersActivity.this.sendStickersRow && var2 != ChatUsersActivity.this.embedLinksRow && var2 != ChatUsersActivity.this.sendPollsRow) {
               if (var2 == ChatUsersActivity.this.sendMessagesRow) {
                  var17.setEnabled(ChatUsersActivity.this.defaultBannedRights.view_messages ^ true);
               }
            } else {
               if (!ChatUsersActivity.this.defaultBannedRights.send_messages && !ChatUsersActivity.this.defaultBannedRights.view_messages) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               var17.setEnabled(var10);
            }

            if (ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat)) {
               if ((var2 != ChatUsersActivity.this.addUsersRow || ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 3)) && (var2 != ChatUsersActivity.this.pinMessagesRow || ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 0)) && (var2 != ChatUsersActivity.this.changeInfoRow || ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 1)) && (TextUtils.isEmpty(ChatUsersActivity.this.currentChat.username) || var2 != ChatUsersActivity.this.pinMessagesRow && var2 != ChatUsersActivity.this.changeInfoRow)) {
                  var17.setIcon(0);
               } else {
                  var17.setIcon(2131165736);
               }
            } else {
               var17.setIcon(0);
            }
            break;
         case 8:
            GraySectionCell var16 = (GraySectionCell)var1.itemView;
            if (var2 == ChatUsersActivity.this.membersHeaderRow) {
               if (ChatObject.isChannel(ChatUsersActivity.this.currentChat) && !ChatUsersActivity.this.currentChat.megagroup) {
                  var16.setText(LocaleController.getString("ChannelOtherSubscribers", 2131558984));
               } else {
                  var16.setText(LocaleController.getString("ChannelOtherMembers", 2131558983));
               }
            } else if (var2 == ChatUsersActivity.this.botHeaderRow) {
               var16.setText(LocaleController.getString("ChannelBots", 2131558935));
            } else if (var2 == ChatUsersActivity.this.contactsHeaderRow) {
               if (ChatObject.isChannel(ChatUsersActivity.this.currentChat) && !ChatUsersActivity.this.currentChat.megagroup) {
                  var16.setText(LocaleController.getString("ChannelContacts", 2131558941));
               } else {
                  var16.setText(LocaleController.getString("GroupContacts", 2131559601));
               }
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         boolean var3 = true;
         Object var8;
         switch(var2) {
         case 0:
            Context var10 = this.mContext;
            var2 = ChatUsersActivity.this.type;
            byte var6 = 6;
            byte var9;
            if (var2 != 0 && ChatUsersActivity.this.type != 3) {
               var9 = 6;
            } else {
               var9 = 7;
            }

            byte var7 = var6;
            if (ChatUsersActivity.this.type != 0) {
               if (ChatUsersActivity.this.type == 3) {
                  var7 = var6;
               } else {
                  var7 = 2;
               }
            }

            if (ChatUsersActivity.this.selectType != 0) {
               var3 = false;
            }

            var8 = new ManageChatUserCell(var10, var9, var7, var3);
            ((View)var8).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ((ManageChatUserCell)var8).setDelegate(new _$$Lambda$ChatUsersActivity$ListAdapter$eixJJWW_1mDLHNoI_EEjSfsGLFc(this));
            break;
         case 1:
            var8 = new TextInfoPrivacyCell(this.mContext);
            break;
         case 2:
            var8 = new ManageChatTextCell(this.mContext);
            ((View)var8).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 3:
            var8 = new ShadowSectionCell(this.mContext);
            break;
         case 4:
            var8 = new FrameLayout(this.mContext) {
               protected void onMeasure(int var1, int var2) {
                  super.onMeasure(var1, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var2) - AndroidUtilities.dp(56.0F), 1073741824));
               }
            };
            ((FrameLayout)var8).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            LinearLayout var4 = new LinearLayout(this.mContext);
            var4.setOrientation(1);
            ((FrameLayout)var8).addView(var4, LayoutHelper.createFrame(-2, -2.0F, 17, 20.0F, 0.0F, 20.0F, 0.0F));
            ImageView var5 = new ImageView(this.mContext);
            var5.setImageResource(2131165400);
            var5.setScaleType(ScaleType.CENTER);
            var5.setColorFilter(new PorterDuffColorFilter(Theme.getColor("emptyListPlaceholder"), Mode.MULTIPLY));
            var4.addView(var5, LayoutHelper.createLinear(-2, -2, 1));
            TextView var11 = new TextView(this.mContext);
            var11.setText(LocaleController.getString("NoBlockedUsers", 2131559916));
            var11.setTextColor(Theme.getColor("emptyListPlaceholder"));
            var11.setTextSize(1, 16.0F);
            var11.setGravity(1);
            var11.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            var4.addView(var11, LayoutHelper.createLinear(-2, -2, 1, 0, 10, 0, 0));
            var11 = new TextView(this.mContext);
            if (ChatUsersActivity.this.isChannel) {
               var11.setText(LocaleController.getString("NoBlockedChannel2", 2131559914));
            } else {
               var11.setText(LocaleController.getString("NoBlockedGroup2", 2131559915));
            }

            var11.setTextColor(Theme.getColor("emptyListPlaceholder"));
            var11.setTextSize(1, 15.0F);
            var11.setGravity(1);
            var4.addView(var11, LayoutHelper.createLinear(-2, -2, 1, 0, 10, 0, 0));
            ((View)var8).setLayoutParams(new RecyclerView.LayoutParams(-1, -1));
            break;
         case 5:
            var8 = new HeaderCell(this.mContext, false, 21, 11, false);
            ((FrameLayout)var8).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ((HeaderCell)var8).setHeight(43);
            break;
         case 6:
            var8 = new TextSettingsCell(this.mContext);
            ((View)var8).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 7:
            var8 = new TextCheckCell2(this.mContext);
            ((View)var8).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         default:
            var8 = new GraySectionCell(this.mContext);
         }

         return new RecyclerListView.Holder((View)var8);
      }

      public void onViewRecycled(RecyclerView.ViewHolder var1) {
         View var2 = var1.itemView;
         if (var2 instanceof ManageChatUserCell) {
            ((ManageChatUserCell)var2).recycle();
         }

      }
   }

   private class SearchAdapter extends RecyclerListView.SelectionAdapter {
      private int contactsStartRow;
      private int globalStartRow;
      private int groupStartRow;
      private Context mContext;
      private SearchAdapterHelper searchAdapterHelper;
      private ArrayList searchResult = new ArrayList();
      private ArrayList searchResultNames = new ArrayList();
      private Runnable searchRunnable;
      private int totalCount;

      public SearchAdapter(Context var2) {
         this.mContext = var2;
         this.searchAdapterHelper = new SearchAdapterHelper(true);
         this.searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() {
            // $FF: synthetic method
            public SparseArray getExcludeUsers() {
               return SearchAdapterHelper$SearchAdapterHelperDelegate$_CC.$default$getExcludeUsers(this);
            }

            public void onDataSetChanged() {
               SearchAdapter.this.notifyDataSetChanged();
            }

            public void onSetHashtags(ArrayList var1, HashMap var2) {
            }
         });
      }

      private void processSearch(String var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChatUsersActivity$SearchAdapter$zcGPzg6AlSiEVmqjCWms3OvMW4s(this, var1));
      }

      private void updateSearchResults(ArrayList var1, ArrayList var2, ArrayList var3) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChatUsersActivity$SearchAdapter$Sw9CFmRc9E_mExIImd0E0Zq2CWY(this, var1, var2, var3));
      }

      public TLObject getItem(int var1) {
         int var2 = this.searchAdapterHelper.getGroupSearch().size();
         int var3 = var1;
         if (var2 != 0) {
            var3 = var2 + 1;
            if (var3 > var1) {
               if (var1 == 0) {
                  return null;
               }

               return (TLObject)this.searchAdapterHelper.getGroupSearch().get(var1 - 1);
            }

            var3 = var1 - var3;
         }

         var2 = this.searchResult.size();
         var1 = var3;
         if (var2 != 0) {
            var1 = var2 + 1;
            if (var1 > var3) {
               if (var3 == 0) {
                  return null;
               }

               return (TLObject)this.searchResult.get(var3 - 1);
            }

            var1 = var3 - var1;
         }

         var3 = this.searchAdapterHelper.getGlobalSearch().size();
         if (var3 != 0 && var3 + 1 > var1) {
            return var1 == 0 ? null : (TLObject)this.searchAdapterHelper.getGlobalSearch().get(var1 - 1);
         } else {
            return null;
         }
      }

      public int getItemCount() {
         int var1 = this.searchResult.size();
         int var2 = this.searchAdapterHelper.getGlobalSearch().size();
         int var3 = this.searchAdapterHelper.getGroupSearch().size();
         int var4 = 0;
         if (var1 != 0) {
            var4 = 0 + var1 + 1;
         }

         var1 = var4;
         if (var2 != 0) {
            var1 = var4 + var2 + 1;
         }

         var4 = var1;
         if (var3 != 0) {
            var4 = var1 + var3 + 1;
         }

         return var4;
      }

      public int getItemViewType(int var1) {
         return var1 != this.globalStartRow && var1 != this.groupStartRow && var1 != this.contactsStartRow ? 0 : 1;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3 = true;
         if (var2 == 1) {
            var3 = false;
         }

         return var3;
      }

      // $FF: synthetic method
      public void lambda$null$1$ChatUsersActivity$SearchAdapter(String var1, ArrayList var2, ArrayList var3) {
         String var4 = var1.trim().toLowerCase();
         if (var4.length() == 0) {
            this.updateSearchResults(new ArrayList(), new ArrayList(), new ArrayList());
         } else {
            String var5;
            label146: {
               var5 = LocaleController.getInstance().getTranslitString(var4);
               if (!var4.equals(var5)) {
                  var1 = var5;
                  if (var5.length() != 0) {
                     break label146;
                  }
               }

               var1 = null;
            }

            byte var6;
            if (var1 != null) {
               var6 = 1;
            } else {
               var6 = 0;
            }

            String[] var7 = new String[var6 + 1];
            var7[0] = var4;
            if (var1 != null) {
               var7[1] = var1;
            }

            ArrayList var8 = new ArrayList();
            ArrayList var21 = new ArrayList();
            ArrayList var9 = new ArrayList();
            int var10;
            int var14;
            StringBuilder var19;
            boolean var23;
            StringBuilder var26;
            if (var2 != null) {
               for(var10 = 0; var10 < var2.size(); ++var10) {
                  TLRPC.ChatParticipant var11 = (TLRPC.ChatParticipant)var2.get(var10);
                  TLRPC.User var12 = MessagesController.getInstance(ChatUsersActivity.access$1900(ChatUsersActivity.this)).getUser(var11.user_id);
                  if (var12.id != UserConfig.getInstance(ChatUsersActivity.access$2000(ChatUsersActivity.this)).getClientUserId()) {
                     String var13 = ContactsController.formatName(var12.first_name, var12.last_name).toLowerCase();
                     var5 = LocaleController.getInstance().getTranslitString(var13);
                     var1 = var5;
                     if (var13.equals(var5)) {
                        var1 = null;
                     }

                     var14 = var7.length;
                     int var15 = 0;

                     for(var23 = false; var15 < var14; ++var15) {
                        label132: {
                           label131: {
                              var5 = var7[var15];
                              if (!var13.startsWith(var5)) {
                                 StringBuilder var16 = new StringBuilder();
                                 var16.append(" ");
                                 var16.append(var5);
                                 if (!var13.contains(var16.toString())) {
                                    if (var1 == null) {
                                       break label131;
                                    }

                                    if (!var1.startsWith(var5)) {
                                       var16 = new StringBuilder();
                                       var16.append(" ");
                                       var16.append(var5);
                                       if (!var1.contains(var16.toString())) {
                                          break label131;
                                       }
                                    }
                                 }
                              }

                              var23 = true;
                              break label132;
                           }

                           String var29 = var12.username;
                           if (var29 != null && var29.startsWith(var5)) {
                              var23 = true;
                           }
                        }

                        if (var23) {
                           if (var23) {
                              var21.add(AndroidUtilities.generateSearchName(var12.first_name, var12.last_name, var5));
                           } else {
                              var19 = new StringBuilder();
                              var19.append("@");
                              var19.append(var12.username);
                              var1 = var19.toString();
                              var26 = new StringBuilder();
                              var26.append("@");
                              var26.append(var5);
                              var21.add(AndroidUtilities.generateSearchName(var1, (String)null, var26.toString()));
                           }

                           var9.add(var11);
                           break;
                        }
                     }
                  }
               }
            }

            if (var3 != null) {
               for(var10 = 0; var10 < var3.size(); ++var10) {
                  TLRPC.TL_contact var20 = (TLRPC.TL_contact)var3.get(var10);
                  TLRPC.User var22 = MessagesController.getInstance(ChatUsersActivity.access$2100(ChatUsersActivity.this)).getUser(var20.user_id);
                  if (var22.id != UserConfig.getInstance(ChatUsersActivity.access$2200(ChatUsersActivity.this)).getClientUserId()) {
                     String var24 = ContactsController.formatName(var22.first_name, var22.last_name).toLowerCase();
                     String var18 = LocaleController.getInstance().getTranslitString(var24);
                     var1 = var18;
                     if (var24.equals(var18)) {
                        var1 = null;
                     }

                     int var17 = var7.length;
                     var14 = 0;

                     for(boolean var28 = false; var14 < var17; var28 = var23) {
                        label103: {
                           label102: {
                              var18 = var7[var14];
                              if (!var24.startsWith(var18)) {
                                 var26 = new StringBuilder();
                                 var26.append(" ");
                                 var26.append(var18);
                                 if (!var24.contains(var26.toString())) {
                                    if (var1 == null) {
                                       break label102;
                                    }

                                    if (!var1.startsWith(var18)) {
                                       var26 = new StringBuilder();
                                       var26.append(" ");
                                       var26.append(var18);
                                       if (!var1.contains(var26.toString())) {
                                          break label102;
                                       }
                                    }
                                 }
                              }

                              var23 = true;
                              break label103;
                           }

                           String var27 = var22.username;
                           var23 = var28;
                           if (var27 != null) {
                              var23 = var28;
                              if (var27.startsWith(var18)) {
                                 var23 = true;
                              }
                           }
                        }

                        if (var23) {
                           if (var23) {
                              var21.add(AndroidUtilities.generateSearchName(var22.first_name, var22.last_name, var18));
                           } else {
                              var19 = new StringBuilder();
                              var19.append("@");
                              var19.append(var22.username);
                              var1 = var19.toString();
                              StringBuilder var25 = new StringBuilder();
                              var25.append("@");
                              var25.append(var18);
                              var21.add(AndroidUtilities.generateSearchName(var1, (String)null, var25.toString()));
                           }

                           var8.add(var22);
                           break;
                        }

                        ++var14;
                     }
                  }
               }
            }

            this.updateSearchResults(var8, var21, var9);
         }
      }

      // $FF: synthetic method
      public boolean lambda$onCreateViewHolder$4$ChatUsersActivity$SearchAdapter(ManageChatUserCell var1, boolean var2) {
         if (this.getItem((Integer)var1.getTag()) instanceof TLRPC.ChannelParticipant) {
            TLRPC.ChannelParticipant var3 = (TLRPC.ChannelParticipant)this.getItem((Integer)var1.getTag());
            return ChatUsersActivity.this.createMenuForParticipant(var3, var2 ^ true);
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      public void lambda$processSearch$2$ChatUsersActivity$SearchAdapter(String var1) {
         ArrayList var2 = null;
         this.searchRunnable = null;
         ArrayList var3;
         if (!ChatObject.isChannel(ChatUsersActivity.this.currentChat) && ChatUsersActivity.this.info != null) {
            var3 = new ArrayList(ChatUsersActivity.this.info.participants.participants);
         } else {
            var3 = null;
         }

         if (ChatUsersActivity.this.selectType == 1) {
            var2 = new ArrayList(ContactsController.getInstance(ChatUsersActivity.access$1800(ChatUsersActivity.this)).contacts);
         }

         SearchAdapterHelper var4 = this.searchAdapterHelper;
         boolean var5;
         if (ChatUsersActivity.this.selectType != 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         int var6;
         if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
            var6 = ChatUsersActivity.this.chatId;
         } else {
            var6 = 0;
         }

         var4.queryServerSearch(var1, var5, false, true, false, var6, ChatUsersActivity.this.type);
         if (var3 != null || var2 != null) {
            Utilities.searchQueue.postRunnable(new _$$Lambda$ChatUsersActivity$SearchAdapter$nCoaqRgr8A9Exi9qEjyQaRMerr0(this, var1, var3, var2));
         }

      }

      // $FF: synthetic method
      public void lambda$searchDialogs$0$ChatUsersActivity$SearchAdapter(String var1) {
         this.processSearch(var1);
      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$3$ChatUsersActivity$SearchAdapter(ArrayList var1, ArrayList var2, ArrayList var3) {
         this.searchResult = var1;
         this.searchResultNames = var2;
         this.searchAdapterHelper.mergeResults(this.searchResult);
         if (!ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
            var1 = this.searchAdapterHelper.getGroupSearch();
            var1.clear();
            var1.addAll(var3);
         }

         this.notifyDataSetChanged();
      }

      public void notifyDataSetChanged() {
         this.totalCount = 0;
         int var1 = this.searchAdapterHelper.getGroupSearch().size();
         if (var1 != 0) {
            this.groupStartRow = 0;
            this.totalCount += var1 + 1;
         } else {
            this.groupStartRow = -1;
         }

         int var2 = this.searchResult.size();
         if (var2 != 0) {
            var1 = this.totalCount;
            this.contactsStartRow = var1;
            this.totalCount = var1 + var2 + 1;
         } else {
            this.contactsStartRow = -1;
         }

         var2 = this.searchAdapterHelper.getGlobalSearch().size();
         if (var2 != 0) {
            var1 = this.totalCount;
            this.globalStartRow = var1;
            this.totalCount = var1 + var2 + 1;
         } else {
            this.globalStartRow = -1;
         }

         super.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 == 1) {
               GraySectionCell var16 = (GraySectionCell)var1.itemView;
               if (var2 == this.groupStartRow) {
                  if (ChatUsersActivity.this.type == 0) {
                     var16.setText(LocaleController.getString("ChannelBlockedUsers", 2131558934));
                  } else if (ChatUsersActivity.this.type == 3) {
                     var16.setText(LocaleController.getString("ChannelRestrictedUsers", 2131558996));
                  } else if (ChatUsersActivity.this.isChannel) {
                     var16.setText(LocaleController.getString("ChannelSubscribers", 2131559004));
                  } else {
                     var16.setText(LocaleController.getString("ChannelMembers", 2131558962));
                  }
               } else if (var2 == this.globalStartRow) {
                  var16.setText(LocaleController.getString("GlobalSearch", 2131559594));
               } else if (var2 == this.contactsStartRow) {
                  var16.setText(LocaleController.getString("Contacts", 2131559149));
               }
            }
         } else {
            TLObject var4 = this.getItem(var2);
            TLRPC.User var18;
            if (var4 instanceof TLRPC.User) {
               var18 = (TLRPC.User)var4;
            } else if (var4 instanceof TLRPC.ChannelParticipant) {
               var18 = MessagesController.getInstance(ChatUsersActivity.access$1300(ChatUsersActivity.this)).getUser(((TLRPC.ChannelParticipant)var4).user_id);
            } else {
               if (!(var4 instanceof TLRPC.ChatParticipant)) {
                  return;
               }

               var18 = MessagesController.getInstance(ChatUsersActivity.access$1400(ChatUsersActivity.this)).getUser(((TLRPC.ChatParticipant)var4).user_id);
            }

            String var5;
            int var6;
            StringBuilder var7;
            Object var8;
            String var9;
            boolean var19;
            label119: {
               var5 = var18.username;
               var6 = this.searchAdapterHelper.getGroupSearch().size();
               var7 = null;
               var8 = null;
               var3 = var2;
               if (var6 != 0) {
                  var3 = var6 + 1;
                  if (var3 > var2) {
                     var9 = this.searchAdapterHelper.getLastFoundChannel();
                     var19 = true;
                     break label119;
                  }

                  var3 = var2 - var3;
               }

               var2 = var3;
               var9 = null;
               var19 = false;
            }

            Object var11;
            label113: {
               var6 = var2;
               if (!var19) {
                  int var10 = this.searchResult.size();
                  var6 = var2;
                  if (var10 != 0) {
                     var6 = var10 + 1;
                     if (var6 > var2) {
                        var11 = (CharSequence)this.searchResultNames.get(var2 - 1);
                        if (var11 != null && !TextUtils.isEmpty(var5)) {
                           String var12 = ((CharSequence)var11).toString();
                           var7 = new StringBuilder();
                           var7.append("@");
                           var7.append(var5);
                           if (var12.startsWith(var7.toString())) {
                              var7 = null;
                              var8 = var11;
                              var11 = var7;
                           }
                        }

                        var19 = true;
                        break label113;
                     }

                     var6 = var2 - var6;
                  }
               }

               var11 = null;
               var8 = var7;
               var2 = var6;
            }

            if (!var19 && var5 != null) {
               var3 = this.searchAdapterHelper.getGlobalSearch().size();
               if (var3 != 0 && var3 + 1 > var2) {
                  label135: {
                     String var21 = this.searchAdapterHelper.getLastFoundUsername();
                     String var20 = var21;
                     if (var21.startsWith("@")) {
                        var20 = var21.substring(1);
                     }

                     SpannableStringBuilder var26;
                     label102: {
                        Exception var10000;
                        label131: {
                           boolean var10001;
                           try {
                              var26 = new SpannableStringBuilder();
                              var26.append("@");
                              var26.append(var5);
                              var6 = var5.toLowerCase().indexOf(var20);
                           } catch (Exception var15) {
                              var10000 = var15;
                              var10001 = false;
                              break label131;
                           }

                           var8 = var26;
                           if (var6 == -1) {
                              break label135;
                           }

                           try {
                              var3 = var20.length();
                           } catch (Exception var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label131;
                           }

                           if (var6 == 0) {
                              ++var3;
                           } else {
                              ++var6;
                           }

                           try {
                              ForegroundColorSpan var24 = new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4"));
                              var26.setSpan(var24, var6, var3 + var6, 33);
                              break label102;
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                           }
                        }

                        Exception var23 = var10000;
                        FileLog.e((Throwable)var23);
                        var8 = var5;
                        break label135;
                     }

                     var8 = var26;
                  }
               }
            }

            if (var9 != null) {
               String var25 = UserObject.getUserName(var18);
               SpannableStringBuilder var22 = new SpannableStringBuilder(var25);
               var3 = var25.toLowerCase().indexOf(var9);
               var11 = var22;
               if (var3 != -1) {
                  var22.setSpan(new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), var3, var9.length() + var3, 33);
                  var11 = var22;
               }
            }

            ManageChatUserCell var17 = (ManageChatUserCell)var1.itemView;
            var17.setTag(var2);
            var17.setData(var18, (CharSequence)var11, (CharSequence)var8, false);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 0) {
            var4 = new GraySectionCell(this.mContext);
         } else {
            Context var5 = this.mContext;
            boolean var3;
            if (ChatUsersActivity.this.selectType == 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            var4 = new ManageChatUserCell(var5, 2, 2, var3);
            ((View)var4).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ((ManageChatUserCell)var4).setDelegate(new _$$Lambda$ChatUsersActivity$SearchAdapter$fSIulZwDi8NAXDLh6m3_GQxlD8U(this));
         }

         return new RecyclerListView.Holder((View)var4);
      }

      public void onViewRecycled(RecyclerView.ViewHolder var1) {
         View var2 = var1.itemView;
         if (var2 instanceof ManageChatUserCell) {
            ((ManageChatUserCell)var2).recycle();
         }

      }

      public void searchDialogs(String var1) {
         if (this.searchRunnable != null) {
            Utilities.searchQueue.cancelRunnable(this.searchRunnable);
            this.searchRunnable = null;
         }

         if (TextUtils.isEmpty(var1)) {
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchAdapterHelper.mergeResults((ArrayList)null);
            SearchAdapterHelper var5 = this.searchAdapterHelper;
            boolean var2;
            if (ChatUsersActivity.this.type != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            int var3;
            if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
               var3 = ChatUsersActivity.this.chatId;
            } else {
               var3 = 0;
            }

            var5.queryServerSearch((String)null, var2, false, true, false, var3, ChatUsersActivity.this.type);
            this.notifyDataSetChanged();
         } else {
            DispatchQueue var4 = Utilities.searchQueue;
            _$$Lambda$ChatUsersActivity$SearchAdapter$6g0h_djjISAKh4b_dwWIpTZOwOI var6 = new _$$Lambda$ChatUsersActivity$SearchAdapter$6g0h_djjISAKh4b_dwWIpTZOwOI(this, var1);
            this.searchRunnable = var6;
            var4.postRunnable(var6, 300L);
         }

      }
   }
}
