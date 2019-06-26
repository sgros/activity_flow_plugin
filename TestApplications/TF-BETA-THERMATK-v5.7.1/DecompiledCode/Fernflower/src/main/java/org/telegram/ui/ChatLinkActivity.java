package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class ChatLinkActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int search_button = 0;
   private int chatEndRow;
   private int chatStartRow;
   private ArrayList chats = new ArrayList();
   private int createChatRow;
   private TLRPC.Chat currentChat;
   private int currentChatId;
   private int detailRow;
   private EmptyTextProgressView emptyView;
   private int helpRow;
   private TLRPC.ChatFull info;
   private boolean isChannel;
   private RecyclerListView listView;
   private ChatLinkActivity.ListAdapter listViewAdapter;
   private boolean loadingChats;
   private int removeChatRow;
   private int rowCount;
   private ChatLinkActivity.SearchAdapter searchAdapter;
   private ActionBarMenuItem searchItem;
   private boolean searchWas;
   private boolean searching;
   private boolean waitingForChatCreate;
   private TLRPC.Chat waitingForFullChat;
   private AlertDialog waitingForFullChatProgressAlert;

   public ChatLinkActivity(int var1) {
      this.currentChatId = var1;
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(var1);
      boolean var2;
      if (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.isChannel = var2;
   }

   // $FF: synthetic method
   static MessagesController access$2300(ChatLinkActivity var0) {
      return var0.getMessagesController();
   }

   // $FF: synthetic method
   static void access$2400(ChatLinkActivity var0, TLRPC.Chat var1, BaseFragment var2) {
      var0.linkChat(var1, var2);
   }

   // $FF: synthetic method
   static View access$600(ChatLinkActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$700(ChatLinkActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$800(ChatLinkActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$900(ChatLinkActivity var0) {
      return var0.fragmentView;
   }

   private void linkChat(TLRPC.Chat var1, BaseFragment var2) {
      if (var1 != null) {
         if (!ChatObject.isChannel(var1)) {
            MessagesController.getInstance(super.currentAccount).convertToMegaGroup(this.getParentActivity(), var1.id, new _$$Lambda$ChatLinkActivity$KB_csNS4ZyyV341Vg7wZcvTIREM(this, var2));
         } else {
            AlertDialog[] var3 = new AlertDialog[1];
            AlertDialog var4;
            if (var2 != null) {
               var4 = null;
            } else {
               var4 = new AlertDialog(this.getParentActivity(), 3);
            }

            var3[0] = var4;
            TLRPC.TL_channels_setDiscussionGroup var5 = new TLRPC.TL_channels_setDiscussionGroup();
            var5.broadcast = MessagesController.getInputChannel(this.currentChat);
            var5.group = MessagesController.getInputChannel(var1);
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$97kBY0LYAi0P_kzDEVlDTHmupZw(this, var3, this.getConnectionsManager().sendRequest(var5, new _$$Lambda$ChatLinkActivity$Tjh48No2Z3EZ2EiDDrohPMflh_A(this, var3, var1, var2))), 500L);
         }
      }
   }

   private void loadChats() {
      if (this.info.linked_chat_id != 0) {
         this.chats.clear();
         TLRPC.Chat var1 = this.getMessagesController().getChat(this.info.linked_chat_id);
         if (var1 != null) {
            this.chats.add(var1);
         }

         ActionBarMenuItem var2 = this.searchItem;
         if (var2 != null) {
            var2.setVisibility(8);
         }
      }

      if (!this.loadingChats && this.isChannel && this.info.linked_chat_id == 0) {
         this.loadingChats = true;
         TLRPC.TL_channels_getGroupsForDiscussion var3 = new TLRPC.TL_channels_getGroupsForDiscussion();
         this.getConnectionsManager().sendRequest(var3, new _$$Lambda$ChatLinkActivity$tq6x8HdoUQyHzEq8SE366Ggv0uM(this));
      }

   }

   private void showLinkAlert(TLRPC.Chat var1, boolean var2) {
      TLRPC.ChatFull var3 = this.getMessagesController().getChatFull(var1.id);
      byte var4 = 3;
      if (var3 == null) {
         if (var2) {
            this.getMessagesController().loadFullChat(var1.id, 0, true);
            this.waitingForFullChat = var1;
            this.waitingForFullChatProgressAlert = new AlertDialog(this.getParentActivity(), 3);
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$XAuETeIZs_27DO0KwtCzWWlThog(this), 500L);
         }

      } else {
         AlertDialog.Builder var5 = new AlertDialog.Builder(this.getParentActivity());
         TextView var6 = new TextView(this.getParentActivity());
         var6.setTextColor(Theme.getColor("dialogTextBlack"));
         var6.setTextSize(1, 16.0F);
         byte var7;
         if (LocaleController.isRTL) {
            var7 = 5;
         } else {
            var7 = 3;
         }

         var6.setGravity(var7 | 48);
         String var8;
         if (TextUtils.isEmpty(var1.username)) {
            var8 = LocaleController.formatString("DiscussionLinkGroupPublicPrivateAlert", 2131559292, var1.title, this.currentChat.title);
         } else if (TextUtils.isEmpty(this.currentChat.username)) {
            var8 = LocaleController.formatString("DiscussionLinkGroupPrivateAlert", 2131559290, var1.title, this.currentChat.title);
         } else {
            var8 = LocaleController.formatString("DiscussionLinkGroupPublicAlert", 2131559291, var1.title, this.currentChat.title);
         }

         String var9 = var8;
         if (var3.hidden_prehistory) {
            StringBuilder var15 = new StringBuilder();
            var15.append(var8);
            var15.append("\n\n");
            var15.append(LocaleController.getString("DiscussionLinkGroupAlertHistory", 2131559289));
            var9 = var15.toString();
         }

         var6.setText(AndroidUtilities.replaceTags(var9));
         FrameLayout var17 = new FrameLayout(this.getParentActivity());
         var5.setView(var17);
         AvatarDrawable var16 = new AvatarDrawable();
         var16.setTextSize(AndroidUtilities.dp(12.0F));
         BackupImageView var10 = new BackupImageView(this.getParentActivity());
         var10.setRoundRadius(AndroidUtilities.dp(20.0F));
         if (LocaleController.isRTL) {
            var7 = 5;
         } else {
            var7 = 3;
         }

         var17.addView(var10, LayoutHelper.createFrame(40, 40.0F, var7 | 48, 22.0F, 5.0F, 22.0F, 0.0F));
         TextView var11 = new TextView(this.getParentActivity());
         var11.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
         var11.setTextSize(1, 20.0F);
         var11.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var11.setLines(1);
         var11.setMaxLines(1);
         var11.setSingleLine(true);
         if (LocaleController.isRTL) {
            var7 = 5;
         } else {
            var7 = 3;
         }

         var11.setGravity(var7 | 16);
         var11.setEllipsize(TruncateAt.END);
         var11.setText(var1.title);
         if (LocaleController.isRTL) {
            var7 = 5;
         } else {
            var7 = 3;
         }

         var2 = LocaleController.isRTL;
         byte var12 = 21;
         byte var13;
         if (var2) {
            var13 = 21;
         } else {
            var13 = 76;
         }

         float var14 = (float)var13;
         var13 = var12;
         if (LocaleController.isRTL) {
            var13 = 76;
         }

         var17.addView(var11, LayoutHelper.createFrame(-1, -2.0F, var7 | 48, var14, 11.0F, (float)var13, 0.0F));
         var7 = var4;
         if (LocaleController.isRTL) {
            var7 = 5;
         }

         var17.addView(var6, LayoutHelper.createFrame(-2, -2.0F, var7 | 48, 24.0F, 57.0F, 24.0F, 9.0F));
         var16.setInfo(var1);
         var10.setImage((ImageLocation)ImageLocation.getForChat(var1, false), "50_50", (Drawable)var16, (Object)var1);
         var5.setPositiveButton(LocaleController.getString("DiscussionLinkGroup", 2131559288), new _$$Lambda$ChatLinkActivity$tvkiY8dJp0f6xPm_0V9yT1Vcn74(this, var3, var1));
         var5.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var5.create());
      }
   }

   private void updateRows() {
      this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.currentChatId);
      if (this.currentChat != null) {
         byte var1 = 0;
         this.rowCount = 0;
         this.helpRow = -1;
         this.createChatRow = -1;
         this.chatStartRow = -1;
         this.chatEndRow = -1;
         this.removeChatRow = -1;
         this.detailRow = -1;
         int var2 = this.rowCount++;
         this.helpRow = var2;
         if (this.isChannel) {
            if (this.info.linked_chat_id == 0) {
               var2 = this.rowCount++;
               this.createChatRow = var2;
            }

            var2 = this.rowCount;
            this.chatStartRow = var2;
            this.rowCount = var2 + this.chats.size();
            var2 = this.rowCount;
            this.chatEndRow = var2;
            if (this.info.linked_chat_id != 0) {
               this.rowCount = var2 + 1;
               this.createChatRow = var2;
            }

            var2 = this.rowCount++;
            this.detailRow = var2;
         } else {
            var2 = this.rowCount;
            this.chatStartRow = var2;
            this.rowCount = var2 + this.chats.size();
            var2 = this.rowCount;
            this.chatEndRow = var2;
            this.rowCount = var2 + 1;
            this.createChatRow = var2;
            var2 = this.rowCount++;
            this.detailRow = var2;
         }

         ChatLinkActivity.ListAdapter var3 = this.listViewAdapter;
         if (var3 != null) {
            var3.notifyDataSetChanged();
         }

         ActionBarMenuItem var4 = this.searchItem;
         if (var4 != null) {
            if (this.chats.size() <= 10) {
               var1 = 8;
            }

            var4.setVisibility(var1);
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
      super.actionBar.setTitle(LocaleController.getString("Discussion", 2131559280));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ChatLinkActivity.this.finishFragment();
            }

         }
      });
      this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            ChatLinkActivity.this.searchAdapter.searchDialogs((String)null);
            ChatLinkActivity.this.searching = false;
            ChatLinkActivity.this.searchWas = false;
            ChatLinkActivity.this.listView.setAdapter(ChatLinkActivity.this.listViewAdapter);
            ChatLinkActivity.this.listViewAdapter.notifyDataSetChanged();
            ChatLinkActivity.this.listView.setFastScrollVisible(true);
            ChatLinkActivity.this.listView.setVerticalScrollBarEnabled(false);
            ChatLinkActivity.this.emptyView.setShowAtCenter(false);
            ChatLinkActivity.access$600(ChatLinkActivity.this).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            ChatLinkActivity.access$700(ChatLinkActivity.this).setTag("windowBackgroundGray");
            ChatLinkActivity.this.emptyView.showProgress();
         }

         public void onSearchExpand() {
            ChatLinkActivity.this.searching = true;
            ChatLinkActivity.this.emptyView.setShowAtCenter(true);
         }

         public void onTextChanged(EditText var1) {
            if (ChatLinkActivity.this.searchAdapter != null) {
               String var2 = var1.getText().toString();
               if (var2.length() != 0) {
                  ChatLinkActivity.this.searchWas = true;
                  if (ChatLinkActivity.this.listView != null && ChatLinkActivity.this.listView.getAdapter() != ChatLinkActivity.this.searchAdapter) {
                     ChatLinkActivity.this.listView.setAdapter(ChatLinkActivity.this.searchAdapter);
                     ChatLinkActivity.access$800(ChatLinkActivity.this).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     ChatLinkActivity.access$900(ChatLinkActivity.this).setTag("windowBackgroundWhite");
                     ChatLinkActivity.this.searchAdapter.notifyDataSetChanged();
                     ChatLinkActivity.this.listView.setFastScrollVisible(false);
                     ChatLinkActivity.this.listView.setVerticalScrollBarEnabled(true);
                     ChatLinkActivity.this.emptyView.showProgress();
                  }
               }

               ChatLinkActivity.this.searchAdapter.searchDialogs(var2);
            }
         }
      });
      this.searchItem.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      this.searchAdapter = new ChatLinkActivity.SearchAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      super.fragmentView.setTag("windowBackgroundGray");
      FrameLayout var7 = (FrameLayout)super.fragmentView;
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.showProgress();
      this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
      var7.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.emptyView);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      RecyclerListView var4 = this.listView;
      ChatLinkActivity.ListAdapter var5 = new ChatLinkActivity.ListAdapter(var1);
      this.listViewAdapter = var5;
      var4.setAdapter(var5);
      RecyclerListView var6 = this.listView;
      if (!LocaleController.isRTL) {
         var3 = 2;
      }

      var6.setVerticalScrollbarPosition(var3);
      var7.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ChatLinkActivity$EId5OESEIJ9T5F7R7Ql_mhkWmi4(this)));
      this.updateRows();
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.chatInfoDidLoad) {
         TLRPC.ChatFull var5 = (TLRPC.ChatFull)var3[0];
         var1 = var5.id;
         if (var1 == this.currentChatId) {
            this.info = var5;
            this.loadChats();
            this.updateRows();
         } else {
            TLRPC.Chat var6 = this.waitingForFullChat;
            if (var6 != null && var6.id == var1) {
               try {
                  this.waitingForFullChatProgressAlert.dismiss();
               } catch (Throwable var4) {
               }

               this.waitingForFullChatProgressAlert = null;
               this.showLinkAlert(this.waitingForFullChat, false);
               this.waitingForFullChat = null;
            }
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU var1 = new _$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU(this);
      ThemeDescription var2 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ManageChatUserCell.class, ManageChatTextCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var4 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var9 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var10 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var11 = this.listView;
      Paint var12 = Theme.dividerPaint;
      ThemeDescription var13 = new ThemeDescription(var11, 0, new Class[]{View.class}, var12, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var14 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var20 = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4");
      ThemeDescription var15 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var16 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var21 = new ThemeDescription(this.listView, 0, new Class[]{ManageChatUserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var17 = this.listView;
      Drawable var18 = Theme.avatar_broadcastDrawable;
      Drawable var19 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var13, var14, var20, var15, var16, var21, new ThemeDescription(var17, 0, new Class[]{ManageChatUserCell.class}, (Paint)null, new Drawable[]{var18, var19}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueButton"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{ManageChatTextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueIcon")};
   }

   // $FF: synthetic method
   public void lambda$createView$5$ChatLinkActivity(View var1, int var2) {
      if (this.getParentActivity() != null) {
         RecyclerView.Adapter var3 = this.listView.getAdapter();
         ChatLinkActivity.SearchAdapter var6 = this.searchAdapter;
         TLRPC.Chat var7;
         if (var3 == var6) {
            var7 = var6.getItem(var2);
         } else {
            int var4 = this.chatStartRow;
            if (var2 >= var4 && var2 < this.chatEndRow) {
               var7 = (TLRPC.Chat)this.chats.get(var2 - var4);
            } else {
               var7 = null;
            }
         }

         Bundle var10;
         if (var7 != null) {
            if (this.isChannel && this.info.linked_chat_id == 0) {
               this.showLinkAlert(var7, true);
            } else {
               var10 = new Bundle();
               var10.putInt("chat_id", var7.id);
               this.presentFragment(new ChatActivity(var10));
            }

         } else {
            if (var2 == this.createChatRow) {
               if (this.isChannel && this.info.linked_chat_id == 0) {
                  var10 = new Bundle();
                  ArrayList var13 = new ArrayList();
                  var13.add(this.getUserConfig().getClientUserId());
                  var10.putIntegerArrayList("result", var13);
                  var10.putInt("chatType", 4);
                  GroupCreateFinalActivity var14 = new GroupCreateFinalActivity(var10);
                  var14.setDelegate(new ChatLinkActivity$3(this));
                  this.presentFragment(var14);
               } else {
                  if (this.chats.isEmpty()) {
                     return;
                  }

                  var7 = (TLRPC.Chat)this.chats.get(0);
                  AlertDialog.Builder var5 = new AlertDialog.Builder(this.getParentActivity());
                  String var8;
                  String var9;
                  if (this.isChannel) {
                     var8 = LocaleController.getString("DiscussionUnlinkGroup", 2131559296);
                     var9 = LocaleController.formatString("DiscussionUnlinkChannelAlert", 2131559295, var7.title);
                  } else {
                     var8 = LocaleController.getString("DiscussionUnlink", 2131559294);
                     var9 = LocaleController.formatString("DiscussionUnlinkGroupAlert", 2131559297, var7.title);
                  }

                  var5.setTitle(var8);
                  var5.setMessage(AndroidUtilities.replaceTags(var9));
                  var5.setPositiveButton(LocaleController.getString("DiscussionUnlink", 2131559293), new _$$Lambda$ChatLinkActivity$Igj2ZC8r9x_yYwrHQwXpVZFNIyY(this));
                  var5.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                  AlertDialog var11 = var5.create();
                  this.showDialog(var11);
                  TextView var12 = (TextView)var11.getButton(-1);
                  if (var12 != null) {
                     var12.setTextColor(Theme.getColor("dialogTextRed2"));
                  }
               }
            }

         }
      }
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$16$ChatLinkActivity() {
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
   public void lambda$linkChat$11$ChatLinkActivity(AlertDialog[] var1, TLRPC.Chat var2, BaseFragment var3, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$QC2Mz33KGE4zAmpWeIpueArOQXY(this, var1, var2, var3));
   }

   // $FF: synthetic method
   public void lambda$linkChat$13$ChatLinkActivity(AlertDialog[] var1, int var2) {
      if (var1[0] != null) {
         var1[0].setOnCancelListener(new _$$Lambda$ChatLinkActivity$Iy__bAEHS1bHHS34WDzDt7Rdm0U(this, var2));
         this.showDialog(var1[0]);
      }
   }

   // $FF: synthetic method
   public void lambda$linkChat$9$ChatLinkActivity(BaseFragment var1, int var2) {
      MessagesController.getInstance(super.currentAccount).toogleChannelInvitesHistory(var2, false);
      this.linkChat(this.getMessagesController().getChat(var2), var1);
   }

   // $FF: synthetic method
   public void lambda$loadChats$15$ChatLinkActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$tbTNvaUEjjYHPnRSNVytKd0ibuA(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$0$ChatLinkActivity(AlertDialog[] var1) {
      try {
         var1[0].dismiss();
      } catch (Throwable var3) {
      }

      var1[0] = null;
      this.info.linked_chat_id = 0;
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, this.info, 0, false, null);
      this.getMessagesController().loadFullChat(this.currentChatId, 0, true);
      if (!this.isChannel) {
         this.finishFragment();
      }

   }

   // $FF: synthetic method
   public void lambda$null$1$ChatLinkActivity(AlertDialog[] var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$quMUtF8QHjfTQ_MuJuvx5U43M8k(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$10$ChatLinkActivity(AlertDialog[] var1, TLRPC.Chat var2, BaseFragment var3) {
      if (var1[0] != null) {
         try {
            var1[0].dismiss();
         } catch (Throwable var5) {
         }

         var1[0] = null;
      }

      this.info.linked_chat_id = var2.id;
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, this.info, 0, false, null);
      this.getMessagesController().loadFullChat(this.currentChatId, 0, true);
      if (var3 != null) {
         this.removeSelfFromStack();
         var3.finishFragment();
      } else {
         this.finishFragment();
      }

   }

   // $FF: synthetic method
   public void lambda$null$12$ChatLinkActivity(int var1, DialogInterface var2) {
      ConnectionsManager.getInstance(super.currentAccount).cancelRequest(var1, true);
   }

   // $FF: synthetic method
   public void lambda$null$14$ChatLinkActivity(TLObject var1) {
      if (var1 instanceof TLRPC.messages_Chats) {
         TLRPC.messages_Chats var2 = (TLRPC.messages_Chats)var1;
         this.getMessagesController().putChats(var2.chats, false);
         this.chats = var2.chats;
      }

      this.loadingChats = false;
      this.updateRows();
   }

   // $FF: synthetic method
   public void lambda$null$2$ChatLinkActivity(int var1, DialogInterface var2) {
      ConnectionsManager.getInstance(super.currentAccount).cancelRequest(var1, true);
   }

   // $FF: synthetic method
   public void lambda$null$3$ChatLinkActivity(AlertDialog[] var1, int var2) {
      if (var1[0] != null) {
         var1[0].setOnCancelListener(new _$$Lambda$ChatLinkActivity$Eb3fJqb9_RHvQPpwLb48kNyZrZ8(this, var2));
         this.showDialog(var1[0]);
      }
   }

   // $FF: synthetic method
   public void lambda$null$4$ChatLinkActivity(DialogInterface var1, int var2) {
      if (!this.isChannel || this.info.linked_chat_id != 0) {
         AlertDialog[] var3 = new AlertDialog[]{new AlertDialog(this.getParentActivity(), 3)};
         TLRPC.TL_channels_setDiscussionGroup var4 = new TLRPC.TL_channels_setDiscussionGroup();
         if (this.isChannel) {
            var4.broadcast = MessagesController.getInputChannel(this.currentChat);
            var4.group = new TLRPC.TL_inputChannelEmpty();
         } else {
            var4.broadcast = new TLRPC.TL_inputChannelEmpty();
            var4.group = MessagesController.getInputChannel(this.currentChat);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$EDqDx1_jf5fdYyg4SJu9ZmeA1SY(this, var3, this.getConnectionsManager().sendRequest(var4, new _$$Lambda$ChatLinkActivity$Hzb_oINiZZCK4So4QPZpn91gK3A(this, var3))), 500L);
      }

   }

   // $FF: synthetic method
   public void lambda$null$6$ChatLinkActivity(DialogInterface var1) {
      this.waitingForFullChat = null;
   }

   // $FF: synthetic method
   public void lambda$showLinkAlert$7$ChatLinkActivity() {
      AlertDialog var1 = this.waitingForFullChatProgressAlert;
      if (var1 != null) {
         var1.setOnCancelListener(new _$$Lambda$ChatLinkActivity$14f5beHvshaSysjkW1gsXt7mUc4(this));
         this.showDialog(this.waitingForFullChatProgressAlert);
      }
   }

   // $FF: synthetic method
   public void lambda$showLinkAlert$8$ChatLinkActivity(TLRPC.ChatFull var1, TLRPC.Chat var2, DialogInterface var3, int var4) {
      if (var1.hidden_prehistory) {
         MessagesController.getInstance(super.currentAccount).toogleChannelInvitesHistory(var2.id, false);
      }

      this.linkChat(var2, (BaseFragment)null);
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
      this.loadChats();
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      this.getNotificationCenter().removeObserver(this, NotificationCenter.chatInfoDidLoad);
   }

   public void onResume() {
      super.onResume();
      ChatLinkActivity.ListAdapter var1 = this.listViewAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   public void setInfo(TLRPC.ChatFull var1) {
      this.info = var1;
   }

   public class HintInnerCell extends FrameLayout {
      private ImageView imageView;
      private TextView messageTextView;

      public HintInnerCell(Context var2) {
         super(var2);
         this.imageView = new ImageView(var2);
         ImageView var3 = this.imageView;
         int var4;
         if (Theme.getCurrentTheme().isDark()) {
            var4 = 2131165881;
         } else {
            var4 = 2131165880;
         }

         var3.setImageResource(var4);
         this.addView(this.imageView, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 20.0F, 8.0F, 0.0F));
         this.messageTextView = new TextView(var2);
         this.messageTextView.setTextColor(Theme.getColor("chats_message"));
         this.messageTextView.setTextSize(1, 14.0F);
         this.messageTextView.setGravity(17);
         TLRPC.Chat var5;
         if (ChatLinkActivity.this.isChannel) {
            if (ChatLinkActivity.this.info != null && ChatLinkActivity.this.info.linked_chat_id != 0) {
               var5 = ChatLinkActivity.this.getMessagesController().getChat(ChatLinkActivity.this.info.linked_chat_id);
               if (var5 != null) {
                  this.messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("DiscussionChannelGroupSetHelp", 2131559281, var5.title)));
               }
            } else {
               this.messageTextView.setText(LocaleController.getString("DiscussionChannelHelp", 2131559282));
            }
         } else {
            var5 = ChatLinkActivity.this.getMessagesController().getChat(ChatLinkActivity.this.info.linked_chat_id);
            if (var5 != null) {
               this.messageTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("DiscussionGroupHelp", 2131559285, var5.title)));
            }
         }

         this.addView(this.messageTextView, LayoutHelper.createFrame(-1, -2.0F, 51, 52.0F, 124.0F, 52.0F, 27.0F));
      }
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return ChatLinkActivity.this.loadingChats ? 0 : ChatLinkActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == ChatLinkActivity.this.helpRow) {
            return 3;
         } else if (var1 != ChatLinkActivity.this.createChatRow && var1 != ChatLinkActivity.this.removeChatRow) {
            return var1 >= ChatLinkActivity.this.chatStartRow && var1 < ChatLinkActivity.this.chatEndRow ? 0 : 1;
         } else {
            return 2;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3;
         if (var2 != 0 && var2 != 2) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  ManageChatTextCell var7 = (ManageChatTextCell)var1.itemView;
                  if (ChatLinkActivity.this.isChannel) {
                     if (ChatLinkActivity.this.info.linked_chat_id != 0) {
                        var7.setColors("windowBackgroundWhiteRedText5", "windowBackgroundWhiteRedText5");
                        var7.setText(LocaleController.getString("DiscussionUnlinkGroup", 2131559296), (String)null, 2131165274, false);
                     } else {
                        var7.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                        var7.setText(LocaleController.getString("DiscussionCreateGroup", 2131559284), (String)null, 2131165580, true);
                     }
                  } else {
                     var7.setColors("windowBackgroundWhiteRedText5", "windowBackgroundWhiteRedText5");
                     var7.setText(LocaleController.getString("DiscussionUnlinkChannel", 2131559294), (String)null, 2131165274, false);
                  }
               }
            } else {
               TextInfoPrivacyCell var8 = (TextInfoPrivacyCell)var1.itemView;
               if (var2 == ChatLinkActivity.this.detailRow) {
                  if (ChatLinkActivity.this.isChannel) {
                     var8.setText(LocaleController.getString("DiscussionChannelHelp2", 2131559283));
                  } else {
                     var8.setText(LocaleController.getString("DiscussionGroupHelp2", 2131559286));
                  }
               }
            }
         } else {
            ManageChatUserCell var5 = (ManageChatUserCell)var1.itemView;
            var5.setTag(var2);
            TLRPC.Chat var6 = (TLRPC.Chat)ChatLinkActivity.this.chats.get(var2 - ChatLinkActivity.this.chatStartRow);
            String var9;
            if (TextUtils.isEmpty(var6.username)) {
               var9 = null;
            } else {
               StringBuilder var10 = new StringBuilder();
               var10.append("@");
               var10.append(var6.username);
               var9 = var10.toString();
            }

            if (var2 != ChatLinkActivity.this.chatEndRow - 1 || ChatLinkActivity.this.info.linked_chat_id != 0) {
               var4 = true;
            }

            var5.setData(var6, (CharSequence)null, var9, var4);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  var3 = ChatLinkActivity.this.new HintInnerCell(this.mContext);
               } else {
                  var3 = new ManageChatTextCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextInfoPrivacyCell(this.mContext);
               ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            }
         } else {
            var3 = new ManageChatUserCell(this.mContext, 6, 2, false);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }

      public void onViewRecycled(RecyclerView.ViewHolder var1) {
         View var2 = var1.itemView;
         if (var2 instanceof ManageChatUserCell) {
            ((ManageChatUserCell)var2).recycle();
         }

      }
   }

   private class SearchAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;
      private ArrayList searchResult = new ArrayList();
      private ArrayList searchResultNames = new ArrayList();
      private Runnable searchRunnable;
      private int searchStartRow;
      private int totalCount;

      public SearchAdapter(Context var2) {
         this.mContext = var2;
      }

      private void processSearch(String var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$SearchAdapter$uzR0bcURzUaKW4VFIVGjup_TFMI(this, var1));
      }

      private void updateSearchResults(ArrayList var1, ArrayList var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$SearchAdapter$brffftfBeb_mTh3bCNRySLqW8nw(this, var1, var2));
      }

      public TLRPC.Chat getItem(int var1) {
         return (TLRPC.Chat)this.searchResult.get(var1);
      }

      public int getItemCount() {
         return this.searchResult.size();
      }

      public int getItemViewType(int var1) {
         return 0;
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
      public void lambda$null$1$ChatLinkActivity$SearchAdapter(String var1, ArrayList var2) {
         String var3 = var1.trim().toLowerCase();
         if (var3.length() == 0) {
            this.updateSearchResults(new ArrayList(), new ArrayList());
         } else {
            String var4;
            label76: {
               var4 = LocaleController.getInstance().getTranslitString(var3);
               if (!var3.equals(var4)) {
                  var1 = var4;
                  if (var4.length() != 0) {
                     break label76;
                  }
               }

               var1 = null;
            }

            byte var5;
            if (var1 != null) {
               var5 = 1;
            } else {
               var5 = 0;
            }

            String[] var6 = new String[var5 + 1];
            var6[0] = var3;
            if (var1 != null) {
               var6[1] = var1;
            }

            ArrayList var15 = new ArrayList();
            ArrayList var7 = new ArrayList();

            for(int var8 = 0; var8 < var2.size(); ++var8) {
               TLRPC.Chat var9 = (TLRPC.Chat)var2.get(var8);
               String var10 = var9.title.toLowerCase();
               var4 = LocaleController.getInstance().getTranslitString(var10);
               var1 = var4;
               if (var10.equals(var4)) {
                  var1 = null;
               }

               int var11 = var6.length;
               int var12 = 0;

               boolean var17;
               for(boolean var13 = false; var12 < var11; var13 = var17) {
                  label65: {
                     label64: {
                        var4 = var6[var12];
                        if (!var10.startsWith(var4)) {
                           StringBuilder var14 = new StringBuilder();
                           var14.append(" ");
                           var14.append(var4);
                           if (!var10.contains(var14.toString())) {
                              if (var1 == null) {
                                 break label64;
                              }

                              if (!var1.startsWith(var4)) {
                                 var14 = new StringBuilder();
                                 var14.append(" ");
                                 var14.append(var4);
                                 if (!var1.contains(var14.toString())) {
                                    break label64;
                                 }
                              }
                           }
                        }

                        var17 = true;
                        break label65;
                     }

                     String var19 = var9.username;
                     var17 = var13;
                     if (var19 != null) {
                        var17 = var13;
                        if (var19.startsWith(var4)) {
                           var17 = true;
                        }
                     }
                  }

                  if (var17) {
                     if (var17) {
                        var7.add(AndroidUtilities.generateSearchName(var9.title, (String)null, var4));
                     } else {
                        StringBuilder var16 = new StringBuilder();
                        var16.append("@");
                        var16.append(var9.username);
                        var1 = var16.toString();
                        StringBuilder var18 = new StringBuilder();
                        var18.append("@");
                        var18.append(var4);
                        var7.add(AndroidUtilities.generateSearchName(var1, (String)null, var18.toString()));
                     }

                     var15.add(var9);
                     break;
                  }

                  ++var12;
               }
            }

            this.updateSearchResults(var15, var7);
         }
      }

      // $FF: synthetic method
      public void lambda$processSearch$2$ChatLinkActivity$SearchAdapter(String var1) {
         this.searchRunnable = null;
         ArrayList var2 = new ArrayList(ChatLinkActivity.this.chats);
         Utilities.searchQueue.postRunnable(new _$$Lambda$ChatLinkActivity$SearchAdapter$W0qju9LU94_vc17TYEYKWpSKSG8(this, var1, var2));
      }

      // $FF: synthetic method
      public void lambda$searchDialogs$0$ChatLinkActivity$SearchAdapter(String var1) {
         this.processSearch(var1);
      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$3$ChatLinkActivity$SearchAdapter(ArrayList var1, ArrayList var2) {
         this.searchResult = var1;
         this.searchResultNames = var2;
         if (ChatLinkActivity.this.listView.getAdapter() == ChatLinkActivity.this.searchAdapter) {
            ChatLinkActivity.this.emptyView.showTextView();
         }

         this.notifyDataSetChanged();
      }

      public void notifyDataSetChanged() {
         this.totalCount = 0;
         int var1 = this.searchResult.size();
         if (var1 != 0) {
            int var2 = this.totalCount;
            this.searchStartRow = var2;
            this.totalCount = var2 + var1 + 1;
         } else {
            this.searchStartRow = -1;
         }

         super.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         TLRPC.Chat var3 = (TLRPC.Chat)this.searchResult.get(var2);
         String var4 = var3.username;
         CharSequence var5 = (CharSequence)this.searchResultNames.get(var2);
         Object var6 = null;
         CharSequence var7 = var5;
         CharSequence var8 = (CharSequence)var6;
         if (var5 != null) {
            var7 = var5;
            var8 = (CharSequence)var6;
            if (!TextUtils.isEmpty(var4)) {
               String var9 = var5.toString();
               StringBuilder var10 = new StringBuilder();
               var10.append("@");
               var10.append(var4);
               var7 = var5;
               var8 = (CharSequence)var6;
               if (var9.startsWith(var10.toString())) {
                  var8 = var5;
                  var7 = null;
               }
            }
         }

         ManageChatUserCell var11 = (ManageChatUserCell)var1.itemView;
         var11.setTag(var2);
         var11.setData(var3, var7, var8, false);
      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         ManageChatUserCell var3 = new ManageChatUserCell(this.mContext, 6, 2, false);
         var3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         return new RecyclerListView.Holder(var3);
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
            this.notifyDataSetChanged();
         } else {
            DispatchQueue var2 = Utilities.searchQueue;
            _$$Lambda$ChatLinkActivity$SearchAdapter$TUvgAYvpvIggtszTT20rbb2gK0U var3 = new _$$Lambda$ChatLinkActivity$SearchAdapter$TUvgAYvpvIggtszTT20rbb2gK0U(this, var1);
            this.searchRunnable = var3;
            var2.postRunnable(var3, 300L);
         }

      }
   }
}
