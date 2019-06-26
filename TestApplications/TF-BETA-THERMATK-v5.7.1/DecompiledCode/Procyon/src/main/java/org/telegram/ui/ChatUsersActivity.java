// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.messenger.UserObject;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.Utilities;
import java.util.HashMap;
import org.telegram.ui.Adapters.SearchAdapterHelper$SearchAdapterHelperDelegate$_CC;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import android.widget.TextView;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import org.telegram.messenger.FileLog;
import java.util.Collection;
import java.util.Comparator;
import java.util.Collections;
import android.app.Activity;
import android.widget.Toast;
import android.text.TextUtils;
import android.content.DialogInterface;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ActionBarMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.ContactsController;
import org.telegram.ui.ActionBar.Theme;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import android.os.Bundle;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;
import org.telegram.tgnet.TLObject;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChatUsersActivity extends BaseFragment implements NotificationCenterDelegate
{
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
    private ArrayList<TLObject> bots;
    private boolean botsEndReached;
    private SparseArray<TLObject> botsMap;
    private int changeInfoRow;
    private int chatId;
    private ArrayList<TLObject> contacts;
    private boolean contactsEndReached;
    private int contactsEndRow;
    private int contactsHeaderRow;
    private SparseArray<TLObject> contactsMap;
    private int contactsStartRow;
    private TLRPC.Chat currentChat;
    private TLRPC.TL_chatBannedRights defaultBannedRights;
    private int delayResults;
    private ChatUsersActivityDelegate delegate;
    private ActionBarMenuItem doneItem;
    private int embedLinksRow;
    private EmptyTextProgressView emptyView;
    private boolean firstLoaded;
    private TLRPC.ChatFull info;
    private String initialBannedRights;
    private boolean isChannel;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private boolean loadingUsers;
    private int membersHeaderRow;
    private boolean needOpenSearch;
    private ArrayList<TLObject> participants;
    private int participantsDivider2Row;
    private int participantsDividerRow;
    private int participantsEndRow;
    private int participantsInfoRow;
    private SparseArray<TLObject> participantsMap;
    private int participantsStartRow;
    private int permissionsSectionRow;
    private int pinMessagesRow;
    private int recentActionsRow;
    private int removedUsersRow;
    private int restricted1SectionRow;
    private int rowCount;
    private ActionBarMenuItem searchItem;
    private SearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;
    private int selectType;
    private int sendMediaRow;
    private int sendMessagesRow;
    private int sendPollsRow;
    private int sendStickersRow;
    private int type;
    
    public ChatUsersActivity(final Bundle bundle) {
        super(bundle);
        this.defaultBannedRights = new TLRPC.TL_chatBannedRights();
        this.participants = new ArrayList<TLObject>();
        this.bots = new ArrayList<TLObject>();
        this.contacts = new ArrayList<TLObject>();
        this.participantsMap = (SparseArray<TLObject>)new SparseArray();
        this.botsMap = (SparseArray<TLObject>)new SparseArray();
        this.contactsMap = (SparseArray<TLObject>)new SparseArray();
        this.chatId = super.arguments.getInt("chat_id");
        this.type = super.arguments.getInt("type");
        this.needOpenSearch = super.arguments.getBoolean("open_search");
        this.selectType = super.arguments.getInt("selectType");
        this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
        final TLRPC.Chat currentChat = this.currentChat;
        if (currentChat != null) {
            final TLRPC.TL_chatBannedRights default_banned_rights = currentChat.default_banned_rights;
            if (default_banned_rights != null) {
                final TLRPC.TL_chatBannedRights defaultBannedRights = this.defaultBannedRights;
                defaultBannedRights.view_messages = default_banned_rights.view_messages;
                defaultBannedRights.send_stickers = default_banned_rights.send_stickers;
                defaultBannedRights.send_media = default_banned_rights.send_media;
                defaultBannedRights.embed_links = default_banned_rights.embed_links;
                defaultBannedRights.send_messages = default_banned_rights.send_messages;
                defaultBannedRights.send_games = default_banned_rights.send_games;
                defaultBannedRights.send_inline = default_banned_rights.send_inline;
                defaultBannedRights.send_gifs = default_banned_rights.send_gifs;
                defaultBannedRights.pin_messages = default_banned_rights.pin_messages;
                defaultBannedRights.send_polls = default_banned_rights.send_polls;
                defaultBannedRights.invite_users = default_banned_rights.invite_users;
                defaultBannedRights.change_info = default_banned_rights.change_info;
            }
        }
        this.initialBannedRights = ChatObject.getBannedRightsString(this.defaultBannedRights);
        this.isChannel = (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup);
    }
    
    private boolean checkDiscard() {
        if (!ChatObject.getBannedRightsString(this.defaultBannedRights).equals(this.initialBannedRights)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
            builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131560995));
            if (this.isChannel) {
                builder.setMessage(LocaleController.getString("ChannelSettingsChangedAlert", 2131558999));
            }
            else {
                builder.setMessage(LocaleController.getString("GroupSettingsChangedAlert", 2131559613));
            }
            builder.setPositiveButton(LocaleController.getString("ApplyTheme", 2131558639), (DialogInterface$OnClickListener)new _$$Lambda$ChatUsersActivity$Yqu9jp4YK_ycF_M_XeitW5mibTI(this));
            builder.setNegativeButton(LocaleController.getString("PassportDiscard", 2131560212), (DialogInterface$OnClickListener)new _$$Lambda$ChatUsersActivity$236mIhGiuKn_O04cfPssFb_GqQI(this));
            this.showDialog(builder.create());
            return false;
        }
        return true;
    }
    
    private boolean createMenuForParticipant(final TLObject tlObject, final boolean b) {
        if (tlObject == null || this.selectType != 0) {
            return false;
        }
        int i = 0;
        boolean b2 = false;
        int n = 0;
        TLRPC.TL_chatAdminRights tl_chatAdminRights = null;
        TLRPC.TL_chatBannedRights tl_chatBannedRights = null;
        Label_0125: {
            TLRPC.TL_chatBannedRights banned_rights;
            TLRPC.TL_chatAdminRights admin_rights;
            if (tlObject instanceof TLRPC.ChannelParticipant) {
                final TLRPC.ChannelParticipant channelParticipant = (TLRPC.ChannelParticipant)tlObject;
                i = channelParticipant.user_id;
                b2 = channelParticipant.can_edit;
                banned_rights = channelParticipant.banned_rights;
                admin_rights = channelParticipant.admin_rights;
                n = channelParticipant.date;
            }
            else {
                if (!(tlObject instanceof TLRPC.ChatParticipant)) {
                    b2 = false;
                    i = 0;
                    n = 0;
                    tl_chatAdminRights = null;
                    tl_chatBannedRights = null;
                    break Label_0125;
                }
                final TLRPC.ChatParticipant chatParticipant = (TLRPC.ChatParticipant)tlObject;
                i = chatParticipant.user_id;
                n = chatParticipant.date;
                b2 = ChatObject.canAddAdmins(this.currentChat);
                admin_rights = null;
                banned_rights = null;
            }
            tl_chatAdminRights = admin_rights;
            tl_chatBannedRights = banned_rights;
        }
        if (i != 0 && i != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
            final int type = this.type;
            if (type == 2) {
                final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(i);
                final boolean b3 = ChatObject.canAddAdmins(this.currentChat) && (tlObject instanceof TLRPC.TL_channelParticipant || tlObject instanceof TLRPC.TL_channelParticipantBanned || tlObject instanceof TLRPC.TL_chatParticipant || b2);
                final boolean b4 = tlObject instanceof TLRPC.TL_channelParticipantAdmin;
                final boolean b5 = (!b4 && !(tlObject instanceof TLRPC.TL_channelParticipantCreator) && !(tlObject instanceof TLRPC.TL_chatParticipantCreator) && !(tlObject instanceof TLRPC.TL_chatParticipantAdmin)) || b2;
                final boolean b6 = b4 || tlObject instanceof TLRPC.TL_chatParticipantAdmin;
                ArrayList<String> list;
                ArrayList<Integer> list2;
                ArrayList<Integer> list3;
                if (!b) {
                    list = new ArrayList<String>();
                    list2 = new ArrayList<Integer>();
                    list3 = new ArrayList<Integer>();
                }
                else {
                    list3 = null;
                    list = null;
                    list2 = null;
                }
                if (b3) {
                    if (b) {
                        return true;
                    }
                    String e;
                    if (b6) {
                        e = LocaleController.getString("EditAdminRights", 2131559317);
                    }
                    else {
                        e = LocaleController.getString("SetAsAdmin", 2131560731);
                    }
                    list.add(e);
                    list3.add(2131165271);
                    list2.add(0);
                }
                boolean b7;
                if (ChatObject.canBlockUsers(this.currentChat) && b5) {
                    if (b) {
                        return true;
                    }
                    if (!this.isChannel) {
                        if (ChatObject.isChannel(this.currentChat)) {
                            list.add(LocaleController.getString("ChangePermissions", 2131558910));
                            list3.add(2131165273);
                            list2.add(1);
                        }
                        list.add(LocaleController.getString("KickFromGroup", 2131559714));
                        list3.add(2131165274);
                        list2.add(2);
                    }
                    else {
                        list.add(LocaleController.getString("ChannelRemoveUser", 2131558994));
                        list3.add(2131165274);
                        list2.add(2);
                    }
                    b7 = true;
                }
                else {
                    b7 = false;
                }
                if (list2 == null || list2.isEmpty()) {
                    return false;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                builder.setItems(list.toArray(new CharSequence[list2.size()]), AndroidUtilities.toIntArray(list3), (DialogInterface$OnClickListener)new _$$Lambda$ChatUsersActivity$llOV1TtGtwD9D9cr147J5EH_9co(this, list2, user, i, b5, tlObject, n, tl_chatAdminRights, tl_chatBannedRights));
                final AlertDialog create = builder.create();
                this.showDialog(create);
                if (b7) {
                    create.setItemColor(list.size() - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
                }
            }
            else {
                int[] array;
                CharSequence[] array2;
                if (type == 3 && ChatObject.canBlockUsers(this.currentChat)) {
                    if (b) {
                        return true;
                    }
                    final String string = LocaleController.getString("ChannelEditPermissions", 2131558951);
                    final String string2 = LocaleController.getString("ChannelDeleteFromList", 2131558945);
                    array = new int[] { 2131165273, 2131165348 };
                    array2 = new CharSequence[] { string, string2 };
                }
                else if (this.type == 0 && ChatObject.canBlockUsers(this.currentChat)) {
                    if (b) {
                        return true;
                    }
                    String string3;
                    if (ChatObject.canAddUsers(this.currentChat)) {
                        int n2;
                        String s;
                        if (this.isChannel) {
                            n2 = 2131558922;
                            s = "ChannelAddToChannel";
                        }
                        else {
                            n2 = 2131558923;
                            s = "ChannelAddToGroup";
                        }
                        string3 = LocaleController.getString(s, n2);
                    }
                    else {
                        string3 = null;
                    }
                    final String string4 = LocaleController.getString("ChannelDeleteFromList", 2131558945);
                    array = new int[] { 2131165272, 2131165348 };
                    array2 = new CharSequence[] { string3, string4 };
                }
                else if (this.type == 1 && ChatObject.canAddAdmins(this.currentChat) && b2) {
                    if (b) {
                        return true;
                    }
                    if (!this.currentChat.creator && (tlObject instanceof TLRPC.TL_channelParticipantCreator || !b2)) {
                        final String string5 = LocaleController.getString("ChannelRemoveUserAdmin", 2131558995);
                        array = new int[] { 2131165274 };
                        array2 = new CharSequence[] { string5 };
                    }
                    else {
                        array2 = new CharSequence[] { LocaleController.getString("EditAdminRights", 2131559317), LocaleController.getString("ChannelRemoveUserAdmin", 2131558995) };
                        final int[] array3;
                        array = (array3 = new int[2]);
                        array3[0] = 2131165271;
                        array3[1] = 2131165274;
                    }
                }
                else {
                    array = null;
                    array2 = null;
                }
                if (array2 == null) {
                    return false;
                }
                final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)this.getParentActivity());
                builder2.setItems(array2, array, (DialogInterface$OnClickListener)new _$$Lambda$ChatUsersActivity$PIq1XQnFcSvW_MebsnvdhxTy3xQ(this, array2, i, tl_chatAdminRights, tlObject, tl_chatBannedRights));
                final AlertDialog create2 = builder2.create();
                this.showDialog(create2);
                if (this.type == 1) {
                    create2.setItemColor(array2.length - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
                }
            }
            return true;
        }
        return false;
    }
    
    private String formatUserPermissions(final TLRPC.TL_chatBannedRights tl_chatBannedRights) {
        if (tl_chatBannedRights == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final boolean view_messages = tl_chatBannedRights.view_messages;
        if (view_messages && this.defaultBannedRights.view_messages != view_messages) {
            sb.append(LocaleController.getString("UserRestrictionsNoRead", 2131561008));
        }
        final boolean send_messages = tl_chatBannedRights.send_messages;
        if (send_messages && this.defaultBannedRights.send_messages != send_messages) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSend", 2131561009));
        }
        final boolean send_media = tl_chatBannedRights.send_media;
        if (send_media && this.defaultBannedRights.send_media != send_media) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendMedia", 2131561010));
        }
        final boolean send_stickers = tl_chatBannedRights.send_stickers;
        if (send_stickers && this.defaultBannedRights.send_stickers != send_stickers) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendStickers", 2131561012));
        }
        final boolean send_polls = tl_chatBannedRights.send_polls;
        if (send_polls && this.defaultBannedRights.send_polls != send_polls) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoSendPolls", 2131561011));
        }
        final boolean embed_links = tl_chatBannedRights.embed_links;
        if (embed_links && this.defaultBannedRights.embed_links != embed_links) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoEmbedLinks", 2131561005));
        }
        final boolean invite_users = tl_chatBannedRights.invite_users;
        if (invite_users && this.defaultBannedRights.invite_users != invite_users) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoInviteUsers", 2131561006));
        }
        final boolean pin_messages = tl_chatBannedRights.pin_messages;
        if (pin_messages && this.defaultBannedRights.pin_messages != pin_messages) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoPinMessages", 2131561007));
        }
        final boolean change_info = tl_chatBannedRights.change_info;
        if (change_info && this.defaultBannedRights.change_info != change_info) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(LocaleController.getString("UserRestrictionsNoChangeInfo", 2131561004));
        }
        if (sb.length() != 0) {
            sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
            sb.append('.');
        }
        return sb.toString();
    }
    
    private TLObject getAnyParticipant(final int n) {
        for (int i = 0; i < 3; ++i) {
            SparseArray<TLObject> sparseArray;
            if (i == 0) {
                sparseArray = this.contactsMap;
            }
            else if (i == 1) {
                sparseArray = this.botsMap;
            }
            else {
                sparseArray = this.participantsMap;
            }
            final TLObject tlObject = (TLObject)sparseArray.get(n);
            if (tlObject != null) {
                return tlObject;
            }
        }
        return null;
    }
    
    private int getChannelAdminParticipantType(final TLObject tlObject) {
        if (tlObject instanceof TLRPC.TL_channelParticipantCreator || tlObject instanceof TLRPC.TL_channelParticipantSelf) {
            return 0;
        }
        if (!(tlObject instanceof TLRPC.TL_channelParticipantAdmin) && !(tlObject instanceof TLRPC.TL_channelParticipant)) {
            return 2;
        }
        return 1;
    }
    
    private void loadChatParticipants(final int n, final int n2) {
        if (this.loadingUsers) {
            return;
        }
        this.contactsEndReached = false;
        this.botsEndReached = false;
        this.loadChatParticipants(n, n2, true);
    }
    
    private void loadChatParticipants(int i, int limit, final boolean b) {
        final boolean channel = ChatObject.isChannel(this.currentChat);
        final int n = 0;
        final int n2 = 0;
        if (!channel) {
            this.loadingUsers = false;
            this.participants.clear();
            this.bots.clear();
            this.contacts.clear();
            this.participantsMap.clear();
            this.contactsMap.clear();
            this.botsMap.clear();
            i = this.type;
            if (i == 1) {
                final TLRPC.ChatFull info = this.info;
                if (info != null) {
                    TLRPC.ChatParticipant e;
                    for (limit = info.participants.participants.size(), i = n2; i < limit; ++i) {
                        e = this.info.participants.participants.get(i);
                        if (e instanceof TLRPC.TL_chatParticipantCreator || e instanceof TLRPC.TL_chatParticipantAdmin) {
                            this.participants.add(e);
                        }
                        this.participantsMap.put(e.user_id, (Object)e);
                    }
                }
            }
            else if (i == 2 && this.info != null) {
                final int clientUserId = UserConfig.getInstance(super.currentAccount).clientUserId;
                TLRPC.ChatParticipant e2;
                TLRPC.User user;
                for (limit = this.info.participants.participants.size(), i = n; i < limit; ++i) {
                    e2 = this.info.participants.participants.get(i);
                    if (this.selectType == 0 || e2.user_id != clientUserId) {
                        if (this.selectType == 1) {
                            if (ContactsController.getInstance(super.currentAccount).isContact(e2.user_id)) {
                                this.contacts.add(e2);
                                this.contactsMap.put(e2.user_id, (Object)e2);
                            }
                            else {
                                this.participants.add(e2);
                                this.participantsMap.put(e2.user_id, (Object)e2);
                            }
                        }
                        else if (ContactsController.getInstance(super.currentAccount).isContact(e2.user_id)) {
                            this.contacts.add(e2);
                            this.contactsMap.put(e2.user_id, (Object)e2);
                        }
                        else {
                            user = MessagesController.getInstance(super.currentAccount).getUser(e2.user_id);
                            if (user != null && user.bot) {
                                this.bots.add(e2);
                                this.botsMap.put(e2.user_id, (Object)e2);
                            }
                            else {
                                this.participants.add(e2);
                                this.participantsMap.put(e2.user_id, (Object)e2);
                            }
                        }
                    }
                }
            }
            final ListAdapter listViewAdapter = this.listViewAdapter;
            if (listViewAdapter != null) {
                ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
            }
            this.updateRows();
            final ListAdapter listViewAdapter2 = this.listViewAdapter;
            if (listViewAdapter2 != null) {
                ((RecyclerView.Adapter)listViewAdapter2).notifyDataSetChanged();
            }
        }
        else {
            this.loadingUsers = true;
            final EmptyTextProgressView emptyView = this.emptyView;
            if (emptyView != null && !this.firstLoaded) {
                emptyView.showProgress();
            }
            final ListAdapter listViewAdapter3 = this.listViewAdapter;
            if (listViewAdapter3 != null) {
                ((RecyclerView.Adapter)listViewAdapter3).notifyDataSetChanged();
            }
            final TLRPC.TL_channels_getParticipants tl_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
            tl_channels_getParticipants.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chatId);
            final int type = this.type;
            Label_0865: {
                if (type == 0) {
                    tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsKicked();
                }
                else if (type == 1) {
                    tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsAdmins();
                }
                else if (type == 2) {
                    final TLRPC.ChatFull info2 = this.info;
                    if (info2 != null && info2.participants_count <= 200) {
                        final TLRPC.Chat currentChat = this.currentChat;
                        if (currentChat != null && currentChat.megagroup) {
                            tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsRecent();
                            break Label_0865;
                        }
                    }
                    if (this.selectType == 1) {
                        if (!this.contactsEndReached) {
                            this.delayResults = 2;
                            tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsContacts();
                            this.contactsEndReached = true;
                            this.loadChatParticipants(0, 200, false);
                        }
                        else {
                            tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsRecent();
                        }
                    }
                    else if (!this.contactsEndReached) {
                        this.delayResults = 3;
                        tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsContacts();
                        this.contactsEndReached = true;
                        this.loadChatParticipants(0, 200, false);
                    }
                    else if (!this.botsEndReached) {
                        tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsBots();
                        this.botsEndReached = true;
                        this.loadChatParticipants(0, 200, false);
                    }
                    else {
                        tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsRecent();
                    }
                }
                else if (type == 3) {
                    tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsBanned();
                }
            }
            tl_channels_getParticipants.filter.q = "";
            tl_channels_getParticipants.offset = i;
            tl_channels_getParticipants.limit = limit;
            i = ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_channels_getParticipants, new _$$Lambda$ChatUsersActivity$VPgAUAFJXqsKcGEM9EFJ9hJ7L7M(this, tl_channels_getParticipants));
            ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(i, super.classGuid);
        }
    }
    
    private void openRightsEdit(final int n, final TLObject tlObject, final TLRPC.TL_chatAdminRights tl_chatAdminRights, final TLRPC.TL_chatBannedRights tl_chatBannedRights, final boolean b, final int n2, final boolean b2) {
        final ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(n, this.chatId, tl_chatAdminRights, this.defaultBannedRights, tl_chatBannedRights, n2, b, tlObject == null);
        chatRightsEditActivity.setDelegate((ChatRightsEditActivity.ChatRightsEditActivityDelegate)new _$$Lambda$ChatUsersActivity$s_tj1rO5rel9STPZNYUOQpZ18CQ(this, tlObject, b2));
        this.presentFragment(chatRightsEditActivity, b2);
    }
    
    private void openRightsEdit2(final int n, final int n2, final TLObject tlObject, final TLRPC.TL_chatAdminRights tl_chatAdminRights, final TLRPC.TL_chatBannedRights tl_chatBannedRights, final boolean b, final int n3, final boolean b2) {
        final ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(n, this.chatId, tl_chatAdminRights, this.defaultBannedRights, tl_chatBannedRights, n3, true, false);
        chatRightsEditActivity.setDelegate((ChatRightsEditActivity.ChatRightsEditActivityDelegate)new _$$Lambda$ChatUsersActivity$0nOnj72e_Go4w_fy56ow2OoyZ5U(this, n3, n, n2));
        this.presentFragment(chatRightsEditActivity);
    }
    
    private void processDone() {
        if (this.type != 3) {
            return;
        }
        if (!ChatObject.getBannedRightsString(this.defaultBannedRights).equals(this.initialBannedRights)) {
            MessagesController.getInstance(super.currentAccount).setDefaultBannedRole(this.chatId, this.defaultBannedRights, ChatObject.isChannel(this.currentChat), this);
            final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
            if (chat != null) {
                chat.default_banned_rights = this.defaultBannedRights;
            }
        }
        this.finishFragment();
    }
    
    private void removeParticipants(final int n) {
        int i = 0;
        boolean b = false;
        while (i < 3) {
            SparseArray<TLObject> sparseArray;
            ArrayList<TLObject> list;
            if (i == 0) {
                sparseArray = this.contactsMap;
                list = this.contacts;
            }
            else if (i == 1) {
                sparseArray = this.botsMap;
                list = this.bots;
            }
            else {
                sparseArray = this.participantsMap;
                list = this.participants;
            }
            final TLObject o = (TLObject)sparseArray.get(n);
            if (o != null) {
                sparseArray.remove(n);
                list.remove(o);
                b = true;
            }
            ++i;
        }
        if (b) {
            this.updateRows();
            this.listViewAdapter.notifyDataSetChanged();
        }
    }
    
    private void removeParticipants(final TLObject tlObject) {
        if (tlObject instanceof TLRPC.ChatParticipant) {
            this.removeParticipants(((TLRPC.ChatParticipant)tlObject).user_id);
        }
        else if (tlObject instanceof TLRPC.ChannelParticipant) {
            this.removeParticipants(((TLRPC.ChannelParticipant)tlObject).user_id);
        }
    }
    
    private void removeUser(final int i) {
        if (!ChatObject.isChannel(this.currentChat)) {
            return;
        }
        MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chatId, MessagesController.getInstance(super.currentAccount).getUser(i), null);
        this.finishFragment();
    }
    
    private void updateParticipantWithRights(TLRPC.ChannelParticipant channelParticipant, final TLRPC.TL_chatAdminRights admin_rights, final TLRPC.TL_chatBannedRights banned_rights, final int n, final boolean b) {
        int i = 0;
        int n2 = 0;
        TLRPC.ChannelParticipant channelParticipant2 = channelParticipant;
        while (i < 3) {
            SparseArray<TLObject> sparseArray;
            if (i == 0) {
                sparseArray = this.contactsMap;
            }
            else if (i == 1) {
                sparseArray = this.botsMap;
            }
            else {
                sparseArray = this.participantsMap;
            }
            final TLObject tlObject = (TLObject)sparseArray.get(channelParticipant2.user_id);
            if (tlObject instanceof TLRPC.ChannelParticipant) {
                channelParticipant = (TLRPC.ChannelParticipant)tlObject;
                channelParticipant.admin_rights = admin_rights;
                channelParticipant.banned_rights = banned_rights;
                channelParticipant2 = channelParticipant;
                if (b) {
                    channelParticipant.promoted_by = UserConfig.getInstance(super.currentAccount).getClientUserId();
                    channelParticipant2 = channelParticipant;
                }
            }
            int n3 = n2;
            if (b) {
                n3 = n2;
                if (tlObject != null && (n3 = n2) == 0) {
                    final ChatUsersActivityDelegate delegate = this.delegate;
                    n3 = n2;
                    if (delegate != null) {
                        delegate.didAddParticipantToList(n, tlObject);
                        n3 = 1;
                    }
                }
            }
            ++i;
            n2 = n3;
        }
    }
    
    private void updateRows() {
        this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
        final TLRPC.Chat currentChat = this.currentChat;
        if (currentChat == null) {
            return;
        }
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
        int n = 0;
        this.rowCount = 0;
        final int type = this.type;
        if (type == 3) {
            this.permissionsSectionRow = this.rowCount++;
            this.sendMessagesRow = this.rowCount++;
            this.sendMediaRow = this.rowCount++;
            this.sendStickersRow = this.rowCount++;
            this.sendPollsRow = this.rowCount++;
            this.embedLinksRow = this.rowCount++;
            this.addUsersRow = this.rowCount++;
            this.pinMessagesRow = this.rowCount++;
            this.changeInfoRow = this.rowCount++;
            if (ChatObject.isChannel(currentChat)) {
                this.participantsDivider2Row = this.rowCount++;
                this.removedUsersRow = this.rowCount++;
            }
            this.participantsDividerRow = this.rowCount++;
            if (ChatObject.canBlockUsers(this.currentChat)) {
                this.addNewRow = this.rowCount++;
            }
            if (!this.participants.isEmpty()) {
                final int rowCount = this.rowCount;
                this.participantsStartRow = rowCount;
                this.rowCount = rowCount + this.participants.size();
                this.participantsEndRow = this.rowCount;
            }
            if (this.addNewRow != -1 || this.participantsStartRow != -1) {
                this.addNewSectionRow = this.rowCount++;
            }
        }
        else if (type == 0) {
            if (ChatObject.canBlockUsers(currentChat)) {
                this.addNewRow = this.rowCount++;
                if (!this.participants.isEmpty()) {
                    this.participantsInfoRow = this.rowCount++;
                }
            }
            if (!this.participants.isEmpty()) {
                this.restricted1SectionRow = this.rowCount++;
                final int rowCount2 = this.rowCount;
                this.participantsStartRow = rowCount2;
                this.rowCount = rowCount2 + this.participants.size();
                this.participantsEndRow = this.rowCount;
            }
            if (this.participantsStartRow != -1) {
                if (this.participantsInfoRow == -1) {
                    this.participantsInfoRow = this.rowCount++;
                }
                else {
                    this.addNewSectionRow = this.rowCount++;
                }
            }
            else {
                this.blockedEmptyRow = this.rowCount++;
            }
        }
        else {
            final int n2 = 1;
            if (type == 1) {
                if (ChatObject.isChannel(currentChat) && this.currentChat.megagroup) {
                    final TLRPC.ChatFull info = this.info;
                    if (info == null || info.participants_count <= 200) {
                        this.recentActionsRow = this.rowCount++;
                        this.addNewSectionRow = this.rowCount++;
                    }
                }
                if (ChatObject.canAddAdmins(this.currentChat)) {
                    this.addNewRow = this.rowCount++;
                }
                if (!this.participants.isEmpty()) {
                    final int rowCount3 = this.rowCount;
                    this.participantsStartRow = rowCount3;
                    this.rowCount = rowCount3 + this.participants.size();
                    this.participantsEndRow = this.rowCount;
                }
                this.participantsInfoRow = this.rowCount++;
            }
            else if (type == 2) {
                if (this.selectType == 0 && ChatObject.canAddUsers(currentChat)) {
                    this.addNewRow = this.rowCount++;
                }
                if (!this.contacts.isEmpty()) {
                    this.contactsHeaderRow = this.rowCount++;
                    final int rowCount4 = this.rowCount;
                    this.contactsStartRow = rowCount4;
                    this.rowCount = rowCount4 + this.contacts.size();
                    this.contactsEndRow = this.rowCount;
                    n = 1;
                }
                if (!this.bots.isEmpty()) {
                    this.botHeaderRow = this.rowCount++;
                    final int rowCount5 = this.rowCount;
                    this.botStartRow = rowCount5;
                    this.rowCount = rowCount5 + this.bots.size();
                    this.botEndRow = this.rowCount;
                    n = n2;
                }
                if (!this.participants.isEmpty()) {
                    if (n != 0) {
                        this.membersHeaderRow = this.rowCount++;
                    }
                    final int rowCount6 = this.rowCount;
                    this.participantsStartRow = rowCount6;
                    this.rowCount = rowCount6 + this.participants.size();
                    this.participantsEndRow = this.rowCount;
                }
                final int rowCount7 = this.rowCount;
                if (rowCount7 != 0) {
                    this.rowCount = rowCount7 + 1;
                    this.participantsInfoRow = rowCount7;
                }
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        super.actionBar.setBackButtonImage(2131165409);
        final ActionBar actionBar = super.actionBar;
        int verticalScrollbarPosition = 1;
        actionBar.setAllowOverlayTitle(true);
        final int type = this.type;
        if (type == 3) {
            super.actionBar.setTitle(LocaleController.getString("ChannelPermissions", 2131558985));
        }
        else if (type == 0) {
            super.actionBar.setTitle(LocaleController.getString("ChannelBlacklist", 2131558932));
        }
        else if (type == 1) {
            super.actionBar.setTitle(LocaleController.getString("ChannelAdministrators", 2131558927));
        }
        else if (type == 2) {
            final int selectType = this.selectType;
            if (selectType == 0) {
                if (this.isChannel) {
                    super.actionBar.setTitle(LocaleController.getString("ChannelSubscribers", 2131559004));
                }
                else {
                    super.actionBar.setTitle(LocaleController.getString("ChannelMembers", 2131558962));
                }
            }
            else if (selectType == 1) {
                super.actionBar.setTitle(LocaleController.getString("ChannelAddAdmin", 2131558918));
            }
            else if (selectType == 2) {
                super.actionBar.setTitle(LocaleController.getString("ChannelBlockUser", 2131558933));
            }
            else if (selectType == 3) {
                super.actionBar.setTitle(LocaleController.getString("ChannelAddException", 2131558919));
            }
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    if (ChatUsersActivity.this.checkDiscard()) {
                        ChatUsersActivity.this.finishFragment();
                    }
                }
                else if (n == 1) {
                    ChatUsersActivity.this.processDone();
                }
            }
        });
        Label_0417: {
            if (this.selectType == 0) {
                final int type2 = this.type;
                if (type2 != 2 && type2 != 0 && type2 != 3) {
                    break Label_0417;
                }
            }
            this.searchListViewAdapter = new SearchAdapter(context);
            final ActionBarMenu menu = super.actionBar.createMenu();
            this.searchItem = menu.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
                @Override
                public void onSearchCollapse() {
                    ChatUsersActivity.this.searchListViewAdapter.searchDialogs(null);
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
                
                @Override
                public void onSearchExpand() {
                    ChatUsersActivity.this.searching = true;
                    ChatUsersActivity.this.emptyView.setShowAtCenter(true);
                    if (ChatUsersActivity.this.doneItem != null) {
                        ChatUsersActivity.this.doneItem.setVisibility(8);
                    }
                }
                
                @Override
                public void onTextChanged(final EditText editText) {
                    if (ChatUsersActivity.this.searchListViewAdapter == null) {
                        return;
                    }
                    final String string = editText.getText().toString();
                    if (string.length() != 0) {
                        ChatUsersActivity.this.searchWas = true;
                        if (ChatUsersActivity.this.listView != null && ChatUsersActivity.this.listView.getAdapter() != ChatUsersActivity.this.searchListViewAdapter) {
                            ChatUsersActivity.this.listView.setAdapter(ChatUsersActivity.this.searchListViewAdapter);
                            ChatUsersActivity.this.searchListViewAdapter.notifyDataSetChanged();
                            ChatUsersActivity.this.listView.setFastScrollVisible(false);
                            ChatUsersActivity.this.listView.setVerticalScrollBarEnabled(true);
                        }
                    }
                    ChatUsersActivity.this.searchListViewAdapter.searchDialogs(string);
                }
            });
            if (this.type == 3) {
                this.searchItem.setSearchFieldHint(LocaleController.getString("ChannelSearchException", 2131558997));
            }
            else {
                this.searchItem.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
            }
            if (this.type == 3) {
                this.doneItem = menu.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", 2131559299));
            }
        }
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        this.emptyView = new EmptyTextProgressView(context);
        final int type3 = this.type;
        if (type3 == 0 || type3 == 2 || type3 == 3) {
            this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
        }
        this.emptyView.setShowAtCenter(true);
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setAdapter(this.listViewAdapter = new ListAdapter(context));
        final RecyclerListView listView = this.listView;
        if (!LocaleController.isRTL) {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ChatUsersActivity$5cdlJaYt4Fjs1RdGaQE3yCAVZ1M(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$ChatUsersActivity$0hbN7epVO7jllCcszytHvZXGqYU(this));
        if (this.searchItem != null) {
            this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                    if (n == 1) {
                        AndroidUtilities.hideKeyboard(ChatUsersActivity.this.getParentActivity().getCurrentFocus());
                    }
                }
                
                @Override
                public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                }
            });
        }
        if (this.loadingUsers) {
            this.emptyView.showProgress();
        }
        else {
            this.emptyView.showTextView();
        }
        this.updateRows();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.chatInfoDidLoad) {
            final TLRPC.ChatFull info = (TLRPC.ChatFull)array[0];
            final boolean booleanValue = (boolean)array[2];
            if (info.id == this.chatId && (!booleanValue || !ChatObject.isChannel(this.currentChat))) {
                this.info = info;
                AndroidUtilities.runOnUIThread(new _$$Lambda$ChatUsersActivity$i3MUTxy_8Wq7Kw42VYgYl8VfzuQ(this));
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo $$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo = new _$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo(this);
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { HeaderCell.class, ManageChatUserCell.class, ManageChatTextCell.class, TextCheckCell2.class, TextSettingsCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { GraySectionCell.class }, new String[] { "textView" }, null, null, null, "key_graySectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GraySectionCell.class }, null, null, null, "graySection"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell2.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell2.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell2.class }, new String[] { "checkBox" }, null, null, null, "switch2Track"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell2.class }, new String[] { "checkBox" }, null, null, null, "switch2TrackChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatUsersActivity$dJZ4uYLUeYD83G4_LKo6mt8S4mo, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteBlueButton"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueIcon") };
    }
    
    public boolean hasSelectType() {
        return this.selectType != 0;
    }
    
    @Override
    public boolean onBackPressed() {
        return this.checkDiscard();
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        this.loadChatParticipants(0, 200);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    protected void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b && !b2 && this.needOpenSearch) {
            this.searchItem.openSearch(true);
        }
    }
    
    public void setDelegate(final ChatUsersActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setInfo(final TLRPC.ChatFull info) {
        this.info = info;
    }
    
    public interface ChatUsersActivityDelegate
    {
        void didAddParticipantToList(final int p0, final TLObject p1);
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        public TLObject getItem(final int n) {
            if (n >= ChatUsersActivity.this.participantsStartRow && n < ChatUsersActivity.this.participantsEndRow) {
                return ChatUsersActivity.this.participants.get(n - ChatUsersActivity.this.participantsStartRow);
            }
            if (n >= ChatUsersActivity.this.contactsStartRow && n < ChatUsersActivity.this.contactsEndRow) {
                return ChatUsersActivity.this.contacts.get(n - ChatUsersActivity.this.contactsStartRow);
            }
            if (n >= ChatUsersActivity.this.botStartRow && n < ChatUsersActivity.this.botEndRow) {
                return ChatUsersActivity.this.bots.get(n - ChatUsersActivity.this.botStartRow);
            }
            return null;
        }
        
        @Override
        public int getItemCount() {
            if (ChatUsersActivity.this.loadingUsers && !ChatUsersActivity.this.firstLoaded) {
                return 0;
            }
            return ChatUsersActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == ChatUsersActivity.this.addNewRow || n == ChatUsersActivity.this.addNew2Row || n == ChatUsersActivity.this.recentActionsRow) {
                return 2;
            }
            if ((n >= ChatUsersActivity.this.participantsStartRow && n < ChatUsersActivity.this.participantsEndRow) || (n >= ChatUsersActivity.this.botStartRow && n < ChatUsersActivity.this.botEndRow) || (n >= ChatUsersActivity.this.contactsStartRow && n < ChatUsersActivity.this.contactsEndRow)) {
                return 0;
            }
            if (n == ChatUsersActivity.this.addNewSectionRow || n == ChatUsersActivity.this.participantsDividerRow || n == ChatUsersActivity.this.participantsDivider2Row) {
                return 3;
            }
            if (n == ChatUsersActivity.this.restricted1SectionRow || n == ChatUsersActivity.this.permissionsSectionRow) {
                return 5;
            }
            if (n == ChatUsersActivity.this.participantsInfoRow) {
                return 1;
            }
            if (n == ChatUsersActivity.this.blockedEmptyRow) {
                return 4;
            }
            if (n == ChatUsersActivity.this.removedUsersRow) {
                return 6;
            }
            if (n == ChatUsersActivity.this.changeInfoRow || n == ChatUsersActivity.this.addUsersRow || n == ChatUsersActivity.this.pinMessagesRow || n == ChatUsersActivity.this.sendMessagesRow || n == ChatUsersActivity.this.sendMediaRow || n == ChatUsersActivity.this.sendStickersRow || n == ChatUsersActivity.this.embedLinksRow || n == ChatUsersActivity.this.sendPollsRow) {
                return 7;
            }
            if (n != ChatUsersActivity.this.membersHeaderRow && n != ChatUsersActivity.this.contactsHeaderRow && n != ChatUsersActivity.this.botHeaderRow) {
                return 0;
            }
            return 8;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 7) {
                return ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat);
            }
            boolean b = false;
            if (itemViewType == 0) {
                final TLObject currentObject = ((ManageChatUserCell)viewHolder.itemView).getCurrentObject();
                return !(currentObject instanceof TLRPC.User) || !((TLRPC.User)currentObject).self;
            }
            if (itemViewType == 0 || itemViewType == 2 || itemViewType == 6) {
                b = true;
            }
            return b;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = false;
            final boolean b2 = false;
            final boolean b3 = false;
            final boolean b4 = false;
            final boolean b5 = false;
            final boolean b6 = false;
            boolean b7 = false;
            switch (itemViewType) {
                case 8: {
                    final GraySectionCell graySectionCell = (GraySectionCell)viewHolder.itemView;
                    if (n == ChatUsersActivity.this.membersHeaderRow) {
                        if (ChatObject.isChannel(ChatUsersActivity.this.currentChat) && !ChatUsersActivity.this.currentChat.megagroup) {
                            graySectionCell.setText(LocaleController.getString("ChannelOtherSubscribers", 2131558984));
                            break;
                        }
                        graySectionCell.setText(LocaleController.getString("ChannelOtherMembers", 2131558983));
                        break;
                    }
                    else {
                        if (n == ChatUsersActivity.this.botHeaderRow) {
                            graySectionCell.setText(LocaleController.getString("ChannelBots", 2131558935));
                            break;
                        }
                        if (n != ChatUsersActivity.this.contactsHeaderRow) {
                            break;
                        }
                        if (ChatObject.isChannel(ChatUsersActivity.this.currentChat) && !ChatUsersActivity.this.currentChat.megagroup) {
                            graySectionCell.setText(LocaleController.getString("ChannelContacts", 2131558941));
                            break;
                        }
                        graySectionCell.setText(LocaleController.getString("GroupContacts", 2131559601));
                        break;
                    }
                    break;
                }
                case 7: {
                    final TextCheckCell2 textCheckCell2 = (TextCheckCell2)viewHolder.itemView;
                    if (n == ChatUsersActivity.this.changeInfoRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsChangeInfo", 2131560999), !ChatUsersActivity.this.defaultBannedRights.change_info && TextUtils.isEmpty((CharSequence)ChatUsersActivity.this.currentChat.username), false);
                    }
                    else if (n == ChatUsersActivity.this.addUsersRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsInviteUsers", 2131561003), ChatUsersActivity.this.defaultBannedRights.invite_users ^ true, true);
                    }
                    else if (n == ChatUsersActivity.this.pinMessagesRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsPinMessages", 2131561013), !ChatUsersActivity.this.defaultBannedRights.pin_messages && TextUtils.isEmpty((CharSequence)ChatUsersActivity.this.currentChat.username), true);
                    }
                    else if (n == ChatUsersActivity.this.sendMessagesRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSend", 2131561015), ChatUsersActivity.this.defaultBannedRights.send_messages ^ true, true);
                    }
                    else if (n == ChatUsersActivity.this.sendMediaRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendMedia", 2131561016), ChatUsersActivity.this.defaultBannedRights.send_media ^ true, true);
                    }
                    else if (n == ChatUsersActivity.this.sendStickersRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendStickers", 2131561018), ChatUsersActivity.this.defaultBannedRights.send_stickers ^ true, true);
                    }
                    else if (n == ChatUsersActivity.this.embedLinksRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsEmbedLinks", 2131561002), ChatUsersActivity.this.defaultBannedRights.embed_links ^ true, true);
                    }
                    else if (n == ChatUsersActivity.this.sendPollsRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendPolls", 2131561017), ChatUsersActivity.this.defaultBannedRights.send_polls ^ true, true);
                    }
                    if (n != ChatUsersActivity.this.sendMediaRow && n != ChatUsersActivity.this.sendStickersRow && n != ChatUsersActivity.this.embedLinksRow && n != ChatUsersActivity.this.sendPollsRow) {
                        if (n == ChatUsersActivity.this.sendMessagesRow) {
                            textCheckCell2.setEnabled(ChatUsersActivity.this.defaultBannedRights.view_messages ^ true);
                        }
                    }
                    else {
                        textCheckCell2.setEnabled(!ChatUsersActivity.this.defaultBannedRights.send_messages && !ChatUsersActivity.this.defaultBannedRights.view_messages);
                    }
                    if (!ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat)) {
                        textCheckCell2.setIcon(0);
                        break;
                    }
                    if ((n == ChatUsersActivity.this.addUsersRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 3)) || (n == ChatUsersActivity.this.pinMessagesRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 0)) || (n == ChatUsersActivity.this.changeInfoRow && !ChatObject.canUserDoAdminAction(ChatUsersActivity.this.currentChat, 1)) || (!TextUtils.isEmpty((CharSequence)ChatUsersActivity.this.currentChat.username) && (n == ChatUsersActivity.this.pinMessagesRow || n == ChatUsersActivity.this.changeInfoRow))) {
                        textCheckCell2.setIcon(2131165736);
                        break;
                    }
                    textCheckCell2.setIcon(0);
                    break;
                }
                case 6: {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    final String string = LocaleController.getString("ChannelBlacklist", 2131558932);
                    if (ChatUsersActivity.this.info != null) {
                        n = ChatUsersActivity.this.info.kicked_count;
                    }
                    else {
                        n = 0;
                    }
                    textSettingsCell.setTextAndValue(string, String.format("%d", n), false);
                    break;
                }
                case 5: {
                    final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                    if (n == ChatUsersActivity.this.restricted1SectionRow) {
                        if (ChatUsersActivity.this.type != 0) {
                            headerCell.setText(LocaleController.getString("ChannelRestrictedUsers", 2131558996));
                            break;
                        }
                        if (ChatUsersActivity.this.info != null) {
                            n = ChatUsersActivity.this.info.kicked_count;
                        }
                        else {
                            n = ChatUsersActivity.this.participants.size();
                        }
                        if (n != 0) {
                            headerCell.setText(LocaleController.formatPluralString("RemovedUser", n));
                            break;
                        }
                        headerCell.setText(LocaleController.getString("ChannelBlockedUsers", 2131558934));
                        break;
                    }
                    else {
                        if (n == ChatUsersActivity.this.permissionsSectionRow) {
                            headerCell.setText(LocaleController.getString("ChannelPermissionsHeader", 2131558986));
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 3: {
                    if (n != ChatUsersActivity.this.addNewSectionRow && (ChatUsersActivity.this.type != 3 || n != ChatUsersActivity.this.participantsDividerRow || ChatUsersActivity.this.addNewRow != -1 || ChatUsersActivity.this.participantsStartRow != -1)) {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                        break;
                    }
                    viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    break;
                }
                case 2: {
                    final ManageChatTextCell manageChatTextCell = (ManageChatTextCell)viewHolder.itemView;
                    manageChatTextCell.setColors("windowBackgroundWhiteGrayIcon", "windowBackgroundWhiteBlackText");
                    if (n == ChatUsersActivity.this.addNewRow) {
                        if (ChatUsersActivity.this.type == 3) {
                            manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                            final String string2 = LocaleController.getString("ChannelAddException", 2131558919);
                            if (ChatUsersActivity.this.participantsStartRow != -1) {
                                b7 = true;
                            }
                            manageChatTextCell.setText(string2, null, 2131165275, b7);
                            break;
                        }
                        if (ChatUsersActivity.this.type == 0) {
                            manageChatTextCell.setText(LocaleController.getString("ChannelBlockUser", 2131558933), null, 2131165275, false);
                            break;
                        }
                        if (ChatUsersActivity.this.type == 1) {
                            manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                            manageChatTextCell.setText(LocaleController.getString("ChannelAddAdmin", 2131558918), null, 2131165279, true);
                            break;
                        }
                        if (ChatUsersActivity.this.type != 2) {
                            break;
                        }
                        manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                        if (ChatUsersActivity.this.isChannel) {
                            final String string3 = LocaleController.getString("AddSubscriber", 2131558588);
                            boolean b8 = b;
                            if (ChatUsersActivity.this.membersHeaderRow == -1) {
                                b8 = b;
                                if (!ChatUsersActivity.this.participants.isEmpty()) {
                                    b8 = true;
                                }
                            }
                            manageChatTextCell.setText(string3, null, 2131165272, b8);
                            break;
                        }
                        final String string4 = LocaleController.getString("AddMember", 2131558573);
                        boolean b9 = b2;
                        if (ChatUsersActivity.this.membersHeaderRow == -1) {
                            b9 = b2;
                            if (!ChatUsersActivity.this.participants.isEmpty()) {
                                b9 = true;
                            }
                        }
                        manageChatTextCell.setText(string4, null, 2131165272, b9);
                        break;
                    }
                    else {
                        if (n == ChatUsersActivity.this.recentActionsRow) {
                            manageChatTextCell.setText(LocaleController.getString("EventLog", 2131559382), null, 2131165405, false);
                            break;
                        }
                        if (n == ChatUsersActivity.this.addNew2Row) {
                            manageChatTextCell.setText(LocaleController.getString("ChannelInviteViaLink", 2131558953), null, 2131165787, true);
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 1: {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (n != ChatUsersActivity.this.participantsInfoRow) {
                        break;
                    }
                    if (ChatUsersActivity.this.type == 0 || ChatUsersActivity.this.type == 3) {
                        if (ChatObject.canBlockUsers(ChatUsersActivity.this.currentChat)) {
                            if (ChatUsersActivity.this.isChannel) {
                                textInfoPrivacyCell.setText(LocaleController.getString("NoBlockedChannel2", 2131559914));
                            }
                            else {
                                textInfoPrivacyCell.setText(LocaleController.getString("NoBlockedGroup2", 2131559915));
                            }
                        }
                        else if (ChatUsersActivity.this.isChannel) {
                            textInfoPrivacyCell.setText(LocaleController.getString("NoBlockedChannel2", 2131559914));
                        }
                        else {
                            textInfoPrivacyCell.setText(LocaleController.getString("NoBlockedGroup2", 2131559915));
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        break;
                    }
                    if (ChatUsersActivity.this.type == 1) {
                        if (ChatUsersActivity.this.addNewRow != -1) {
                            if (ChatUsersActivity.this.isChannel) {
                                textInfoPrivacyCell.setText(LocaleController.getString("ChannelAdminsInfo", 2131558928));
                            }
                            else {
                                textInfoPrivacyCell.setText(LocaleController.getString("MegaAdminsInfo", 2131559826));
                            }
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                            break;
                        }
                        textInfoPrivacyCell.setText("");
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        break;
                    }
                    else {
                        if (ChatUsersActivity.this.type == 2) {
                            if (ChatUsersActivity.this.isChannel && ChatUsersActivity.this.selectType == 0) {
                                textInfoPrivacyCell.setText(LocaleController.getString("ChannelMembersInfo", 2131558963));
                            }
                            else {
                                textInfoPrivacyCell.setText("");
                            }
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 0: {
                    final ManageChatUserCell manageChatUserCell = (ManageChatUserCell)viewHolder.itemView;
                    manageChatUserCell.setTag((Object)n);
                    final TLObject item = this.getItem(n);
                    int n2;
                    if (n >= ChatUsersActivity.this.participantsStartRow && n < ChatUsersActivity.this.participantsEndRow) {
                        n2 = ChatUsersActivity.this.participantsEndRow;
                    }
                    else if (n >= ChatUsersActivity.this.contactsStartRow && n < ChatUsersActivity.this.contactsEndRow) {
                        n2 = ChatUsersActivity.this.contactsEndRow;
                    }
                    else {
                        n2 = ChatUsersActivity.this.botEndRow;
                    }
                    int i;
                    int kicked_by;
                    int promoted_by;
                    TLRPC.TL_chatBannedRights banned_rights;
                    boolean b10;
                    boolean b11;
                    boolean b12;
                    if (item instanceof TLRPC.ChannelParticipant) {
                        final TLRPC.ChannelParticipant channelParticipant = (TLRPC.ChannelParticipant)item;
                        i = channelParticipant.user_id;
                        kicked_by = channelParticipant.kicked_by;
                        promoted_by = channelParticipant.promoted_by;
                        banned_rights = channelParticipant.banned_rights;
                        b10 = (channelParticipant instanceof TLRPC.TL_channelParticipantBanned);
                        b11 = (channelParticipant instanceof TLRPC.TL_channelParticipantCreator);
                        b12 = (channelParticipant instanceof TLRPC.TL_channelParticipantAdmin);
                    }
                    else {
                        final TLRPC.ChatParticipant chatParticipant = (TLRPC.ChatParticipant)item;
                        i = chatParticipant.user_id;
                        b11 = (chatParticipant instanceof TLRPC.TL_chatParticipantCreator);
                        b12 = (chatParticipant instanceof TLRPC.TL_chatParticipantAdmin);
                        banned_rights = null;
                        kicked_by = 0;
                        promoted_by = 0;
                        b10 = false;
                    }
                    final TLRPC.User user = MessagesController.getInstance(ChatUsersActivity.this.currentAccount).getUser(i);
                    if (user == null) {
                        break;
                    }
                    if (ChatUsersActivity.this.type == 3) {
                        final String access$3200 = ChatUsersActivity.this.formatUserPermissions(banned_rights);
                        boolean b13 = b3;
                        if (n != n2 - 1) {
                            b13 = true;
                        }
                        manageChatUserCell.setData(user, null, access$3200, b13);
                        break;
                    }
                    if (ChatUsersActivity.this.type == 0) {
                        String formatString = null;
                        Label_2278: {
                            if (b10) {
                                final TLRPC.User user2 = MessagesController.getInstance(ChatUsersActivity.this.currentAccount).getUser(kicked_by);
                                if (user2 != null) {
                                    formatString = LocaleController.formatString("UserRemovedBy", 2131560992, ContactsController.formatName(user2.first_name, user2.last_name));
                                    break Label_2278;
                                }
                            }
                            formatString = null;
                        }
                        boolean b14 = b4;
                        if (n != n2 - 1) {
                            b14 = true;
                        }
                        manageChatUserCell.setData(user, null, formatString, b14);
                        break;
                    }
                    if (ChatUsersActivity.this.type == 1) {
                        String s = null;
                        Label_2396: {
                            if (b11) {
                                s = LocaleController.getString("ChannelCreator", 2131558942);
                            }
                            else {
                                if (b12) {
                                    final TLRPC.User user3 = MessagesController.getInstance(ChatUsersActivity.this.currentAccount).getUser(promoted_by);
                                    if (user3 != null) {
                                        s = LocaleController.formatString("EditAdminPromotedBy", 2131559315, ContactsController.formatName(user3.first_name, user3.last_name));
                                        break Label_2396;
                                    }
                                }
                                s = null;
                            }
                        }
                        boolean b15 = b5;
                        if (n != n2 - 1) {
                            b15 = true;
                        }
                        manageChatUserCell.setData(user, null, s, b15);
                        break;
                    }
                    if (ChatUsersActivity.this.type == 2) {
                        boolean b16 = b6;
                        if (n != n2 - 1) {
                            b16 = true;
                        }
                        manageChatUserCell.setData(user, null, null, b16);
                        break;
                    }
                    break;
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int access$900) {
            boolean b = true;
            Object o = null;
            switch (access$900) {
                default: {
                    o = new GraySectionCell(this.mContext);
                    break;
                }
                case 7: {
                    o = new TextCheckCell2(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 6: {
                    o = new TextSettingsCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 5: {
                    o = new HeaderCell(this.mContext, false, 21, 11, false);
                    ((FrameLayout)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    ((HeaderCell)o).setHeight(43);
                    break;
                }
                case 4: {
                    o = new FrameLayout(this.mContext) {
                        protected void onMeasure(final int n, final int n2) {
                            super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2) - AndroidUtilities.dp(56.0f), 1073741824));
                        }
                    };
                    ((FrameLayout)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    final LinearLayout linearLayout = new LinearLayout(this.mContext);
                    linearLayout.setOrientation(1);
                    ((FrameLayout)o).addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 20.0f, 0.0f, 20.0f, 0.0f));
                    final ImageView imageView = new ImageView(this.mContext);
                    imageView.setImageResource(2131165400);
                    imageView.setScaleType(ImageView$ScaleType.CENTER);
                    imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("emptyListPlaceholder"), PorterDuff$Mode.MULTIPLY));
                    linearLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1));
                    final TextView textView = new TextView(this.mContext);
                    textView.setText((CharSequence)LocaleController.getString("NoBlockedUsers", 2131559916));
                    textView.setTextColor(Theme.getColor("emptyListPlaceholder"));
                    textView.setTextSize(1, 16.0f);
                    textView.setGravity(1);
                    textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    linearLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1, 0, 10, 0, 0));
                    final TextView textView2 = new TextView(this.mContext);
                    if (ChatUsersActivity.this.isChannel) {
                        textView2.setText((CharSequence)LocaleController.getString("NoBlockedChannel2", 2131559914));
                    }
                    else {
                        textView2.setText((CharSequence)LocaleController.getString("NoBlockedGroup2", 2131559915));
                    }
                    textView2.setTextColor(Theme.getColor("emptyListPlaceholder"));
                    textView2.setTextSize(1, 15.0f);
                    textView2.setGravity(1);
                    linearLayout.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1, 0, 10, 0, 0));
                    ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -1));
                    break;
                }
                case 3: {
                    o = new ShadowSectionCell(this.mContext);
                    break;
                }
                case 2: {
                    o = new ManageChatTextCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 1: {
                    o = new TextInfoPrivacyCell(this.mContext);
                    break;
                }
                case 0: {
                    final Context mContext = this.mContext;
                    access$900 = ChatUsersActivity.this.type;
                    final int n = 6;
                    if (access$900 != 0 && ChatUsersActivity.this.type != 3) {
                        access$900 = 6;
                    }
                    else {
                        access$900 = 7;
                    }
                    int n2 = n;
                    if (ChatUsersActivity.this.type != 0) {
                        if (ChatUsersActivity.this.type == 3) {
                            n2 = n;
                        }
                        else {
                            n2 = 2;
                        }
                    }
                    if (ChatUsersActivity.this.selectType != 0) {
                        b = false;
                    }
                    o = new ManageChatUserCell(mContext, access$900, n2, b);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    ((ManageChatUserCell)o).setDelegate((ManageChatUserCell.ManageChatUserCellDelegate)new _$$Lambda$ChatUsersActivity$ListAdapter$eixJJWW_1mDLHNoI_EEjSfsGLFc(this));
                    break;
                }
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewRecycled(final ViewHolder viewHolder) {
            final View itemView = viewHolder.itemView;
            if (itemView instanceof ManageChatUserCell) {
                ((ManageChatUserCell)itemView).recycle();
            }
        }
    }
    
    private class SearchAdapter extends SelectionAdapter
    {
        private int contactsStartRow;
        private int globalStartRow;
        private int groupStartRow;
        private Context mContext;
        private SearchAdapterHelper searchAdapterHelper;
        private ArrayList<TLObject> searchResult;
        private ArrayList<CharSequence> searchResultNames;
        private Runnable searchRunnable;
        private int totalCount;
        
        public SearchAdapter(final Context mContext) {
            this.searchResult = new ArrayList<TLObject>();
            this.searchResultNames = new ArrayList<CharSequence>();
            this.mContext = mContext;
            (this.searchAdapterHelper = new SearchAdapterHelper(true)).setDelegate((SearchAdapterHelper.SearchAdapterHelperDelegate)new SearchAdapterHelper.SearchAdapterHelperDelegate() {
                @Override
                public void onDataSetChanged() {
                    SearchAdapter.this.notifyDataSetChanged();
                }
                
                @Override
                public void onSetHashtags(final ArrayList<HashtagObject> list, final HashMap<String, HashtagObject> hashMap) {
                }
            });
        }
        
        private void processSearch(final String s) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatUsersActivity$SearchAdapter$zcGPzg6AlSiEVmqjCWms3OvMW4s(this, s));
        }
        
        private void updateSearchResults(final ArrayList<TLObject> list, final ArrayList<CharSequence> list2, final ArrayList<TLObject> list3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatUsersActivity$SearchAdapter$Sw9CFmRc9E_mExIImd0E0Zq2CWY(this, list, list2, list3));
        }
        
        public TLObject getItem(int n) {
            final int size = this.searchAdapterHelper.getGroupSearch().size();
            int n2 = n;
            if (size != 0) {
                final int n3 = size + 1;
                if (n3 > n) {
                    if (n == 0) {
                        return null;
                    }
                    return this.searchAdapterHelper.getGroupSearch().get(n - 1);
                }
                else {
                    n2 = n - n3;
                }
            }
            final int size2 = this.searchResult.size();
            n = n2;
            if (size2 != 0) {
                n = size2 + 1;
                if (n > n2) {
                    if (n2 == 0) {
                        return null;
                    }
                    return this.searchResult.get(n2 - 1);
                }
                else {
                    n = n2 - n;
                }
            }
            final int size3 = this.searchAdapterHelper.getGlobalSearch().size();
            if (size3 == 0 || size3 + 1 <= n) {
                return null;
            }
            if (n == 0) {
                return null;
            }
            return this.searchAdapterHelper.getGlobalSearch().get(n - 1);
        }
        
        @Override
        public int getItemCount() {
            final int size = this.searchResult.size();
            final int size2 = this.searchAdapterHelper.getGlobalSearch().size();
            final int size3 = this.searchAdapterHelper.getGroupSearch().size();
            int n = 0;
            if (size != 0) {
                n = 0 + (size + 1);
            }
            int n2 = n;
            if (size2 != 0) {
                n2 = n + (size2 + 1);
            }
            int n3 = n2;
            if (size3 != 0) {
                n3 = n2 + (size3 + 1);
            }
            return n3;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n != this.globalStartRow && n != this.groupStartRow && n != this.contactsStartRow) {
                return 0;
            }
            return 1;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = true;
            if (itemViewType == 1) {
                b = false;
            }
            return b;
        }
        
        @Override
        public void notifyDataSetChanged() {
            this.totalCount = 0;
            final int size = this.searchAdapterHelper.getGroupSearch().size();
            if (size != 0) {
                this.groupStartRow = 0;
                this.totalCount += size + 1;
            }
            else {
                this.groupStartRow = -1;
            }
            final int size2 = this.searchResult.size();
            if (size2 != 0) {
                final int totalCount = this.totalCount;
                this.contactsStartRow = totalCount;
                this.totalCount = totalCount + (size2 + 1);
            }
            else {
                this.contactsStartRow = -1;
            }
            final int size3 = this.searchAdapterHelper.getGlobalSearch().size();
            if (size3 != 0) {
                final int totalCount2 = this.totalCount;
                this.globalStartRow = totalCount2;
                this.totalCount = totalCount2 + (size3 + 1);
            }
            else {
                this.globalStartRow = -1;
            }
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    final GraySectionCell graySectionCell = (GraySectionCell)viewHolder.itemView;
                    if (i == this.groupStartRow) {
                        if (ChatUsersActivity.this.type == 0) {
                            graySectionCell.setText(LocaleController.getString("ChannelBlockedUsers", 2131558934));
                        }
                        else if (ChatUsersActivity.this.type == 3) {
                            graySectionCell.setText(LocaleController.getString("ChannelRestrictedUsers", 2131558996));
                        }
                        else if (ChatUsersActivity.this.isChannel) {
                            graySectionCell.setText(LocaleController.getString("ChannelSubscribers", 2131559004));
                        }
                        else {
                            graySectionCell.setText(LocaleController.getString("ChannelMembers", 2131558962));
                        }
                    }
                    else if (i == this.globalStartRow) {
                        graySectionCell.setText(LocaleController.getString("GlobalSearch", 2131559594));
                    }
                    else if (i == this.contactsStartRow) {
                        graySectionCell.setText(LocaleController.getString("Contacts", 2131559149));
                    }
                }
            }
            else {
                final TLObject item = this.getItem(i);
                TLRPC.User user;
                if (item instanceof TLRPC.User) {
                    user = (TLRPC.User)item;
                }
                else if (item instanceof TLRPC.ChannelParticipant) {
                    user = MessagesController.getInstance(ChatUsersActivity.this.currentAccount).getUser(((TLRPC.ChannelParticipant)item).user_id);
                }
                else {
                    if (!(item instanceof TLRPC.ChatParticipant)) {
                        return;
                    }
                    user = MessagesController.getInstance(ChatUsersActivity.this.currentAccount).getUser(((TLRPC.ChatParticipant)item).user_id);
                }
                final String username = user.username;
                final int size = this.searchAdapterHelper.getGroupSearch().size();
                final CharSequence charSequence = null;
                Object o = null;
                int n = i;
                String lastFoundChannel = null;
                int n3 = 0;
                Label_0339: {
                    if (size != 0) {
                        final int n2 = size + 1;
                        if (n2 > i) {
                            lastFoundChannel = this.searchAdapterHelper.getLastFoundChannel();
                            n3 = 1;
                            break Label_0339;
                        }
                        n = i - n2;
                    }
                    i = n;
                    lastFoundChannel = null;
                    n3 = 0;
                }
                int n4 = i;
                Object o2 = null;
                Label_0485: {
                    if (n3 == 0) {
                        final int size2 = this.searchResult.size();
                        n4 = i;
                        if (size2 != 0) {
                            final int n5 = size2 + 1;
                            if (n5 > i) {
                                o2 = this.searchResultNames.get(i - 1);
                                if (o2 != null && !TextUtils.isEmpty((CharSequence)username)) {
                                    final String string = ((CharSequence)o2).toString();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("@");
                                    sb.append(username);
                                    if (string.startsWith(sb.toString())) {
                                        final CharSequence charSequence2 = null;
                                        o = o2;
                                        o2 = charSequence2;
                                    }
                                }
                                n3 = 1;
                                break Label_0485;
                            }
                            n4 = i - n5;
                        }
                    }
                    o2 = null;
                    o = charSequence;
                    i = n4;
                }
                if (n3 == 0 && username != null) {
                    final int size3 = this.searchAdapterHelper.getGlobalSearch().size();
                    if (size3 != 0 && size3 + 1 > i) {
                        String str;
                        final String s = str = this.searchAdapterHelper.getLastFoundUsername();
                        if (s.startsWith("@")) {
                            str = s.substring(1);
                        }
                        try {
                            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                            spannableStringBuilder.append((CharSequence)"@");
                            spannableStringBuilder.append((CharSequence)username);
                            int index = username.toLowerCase().indexOf(str);
                            o = spannableStringBuilder;
                            if (index != -1) {
                                int length = str.length();
                                if (index == 0) {
                                    ++length;
                                }
                                else {
                                    ++index;
                                }
                                spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), index, length + index, 33);
                                o = spannableStringBuilder;
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                            o = username;
                        }
                    }
                }
                if (lastFoundChannel != null) {
                    final String userName = UserObject.getUserName(user);
                    final SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder((CharSequence)userName);
                    final int index2 = userName.toLowerCase().indexOf(lastFoundChannel);
                    o2 = spannableStringBuilder2;
                    if (index2 != -1) {
                        spannableStringBuilder2.setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), index2, lastFoundChannel.length() + index2, 33);
                        o2 = spannableStringBuilder2;
                    }
                }
                final ManageChatUserCell manageChatUserCell = (ManageChatUserCell)viewHolder.itemView;
                manageChatUserCell.setTag((Object)i);
                manageChatUserCell.setData(user, (CharSequence)o2, (CharSequence)o, false);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                frameLayout = new GraySectionCell(this.mContext);
            }
            else {
                frameLayout = new ManageChatUserCell(this.mContext, 2, 2, ChatUsersActivity.this.selectType == 0);
                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                ((ManageChatUserCell)frameLayout).setDelegate((ManageChatUserCell.ManageChatUserCellDelegate)new _$$Lambda$ChatUsersActivity$SearchAdapter$fSIulZwDi8NAXDLh6m3_GQxlD8U(this));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
        
        @Override
        public void onViewRecycled(final ViewHolder viewHolder) {
            final View itemView = viewHolder.itemView;
            if (itemView instanceof ManageChatUserCell) {
                ((ManageChatUserCell)itemView).recycle();
            }
        }
        
        public void searchDialogs(final String s) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (TextUtils.isEmpty((CharSequence)s)) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                this.searchAdapterHelper.mergeResults(null);
                final SearchAdapterHelper searchAdapterHelper = this.searchAdapterHelper;
                final boolean b = ChatUsersActivity.this.type != 0;
                int access$1100;
                if (ChatObject.isChannel(ChatUsersActivity.this.currentChat)) {
                    access$1100 = ChatUsersActivity.this.chatId;
                }
                else {
                    access$1100 = 0;
                }
                searchAdapterHelper.queryServerSearch(null, b, false, true, false, access$1100, ChatUsersActivity.this.type);
                this.notifyDataSetChanged();
            }
            else {
                Utilities.searchQueue.postRunnable(this.searchRunnable = new _$$Lambda$ChatUsersActivity$SearchAdapter$6g0h_djjISAKh4b_dwWIpTZOwOI(this, s), 300L);
            }
        }
    }
}
