// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.content.DialogInterface$OnShowListener;
import android.os.Build$VERSION;
import android.app.DatePickerDialog$OnDateSetListener;
import android.app.DatePickerDialog;
import org.telegram.messenger.FileLog;
import android.app.TimePickerDialog$OnTimeSetListener;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View$OnClickListener;
import android.widget.LinearLayout;
import org.telegram.ui.ActionBar.BottomSheet;
import android.os.Bundle;
import org.telegram.ui.Cells.DialogRadioCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells2.UserCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.LayoutHelper;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.MessagesStorage;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.DatePicker;
import android.content.DialogInterface;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChatRightsEditActivity extends BaseFragment
{
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
    private String currentBannedRights;
    private TLRPC.Chat currentChat;
    private int currentType;
    private TLRPC.User currentUser;
    private TLRPC.TL_chatBannedRights defaultBannedRights;
    private ChatRightsEditActivityDelegate delegate;
    private int deleteMessagesRow;
    private int editMesagesRow;
    private int embedLinksRow;
    private boolean isAddingNew;
    private boolean isChannel;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
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
    
    public ChatRightsEditActivity(int n, int untilDateRow, TLRPC.TL_chatAdminRights myAdminRights, final TLRPC.TL_chatBannedRights defaultBannedRights, final TLRPC.TL_chatBannedRights tl_chatBannedRights, final int currentType, final boolean canEdit, final boolean isAddingNew) {
        this.currentBannedRights = "";
        this.isAddingNew = isAddingNew;
        this.chatId = untilDateRow;
        this.currentUser = MessagesController.getInstance(super.currentAccount).getUser(n);
        this.currentType = currentType;
        this.canEdit = canEdit;
        this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
        final TLRPC.Chat currentChat = this.currentChat;
        n = 0;
        if (currentChat != null) {
            this.isChannel = (ChatObject.isChannel(currentChat) && !this.currentChat.megagroup);
            this.myAdminRights = this.currentChat.admin_rights;
        }
        if (this.myAdminRights == null) {
            this.myAdminRights = new TLRPC.TL_chatAdminRights();
            final TLRPC.TL_chatAdminRights myAdminRights2 = this.myAdminRights;
            myAdminRights2.add_admins = true;
            myAdminRights2.pin_messages = true;
            myAdminRights2.invite_users = true;
            myAdminRights2.ban_users = true;
            myAdminRights2.delete_messages = true;
            myAdminRights2.edit_messages = true;
            myAdminRights2.post_messages = true;
            myAdminRights2.change_info = true;
        }
        Label_0983: {
            if (currentType == 0) {
                this.adminRights = new TLRPC.TL_chatAdminRights();
                if (myAdminRights == null) {
                    final TLRPC.TL_chatAdminRights adminRights = this.adminRights;
                    myAdminRights = this.myAdminRights;
                    adminRights.change_info = myAdminRights.change_info;
                    adminRights.post_messages = myAdminRights.post_messages;
                    adminRights.edit_messages = myAdminRights.edit_messages;
                    adminRights.delete_messages = myAdminRights.delete_messages;
                    adminRights.ban_users = myAdminRights.ban_users;
                    adminRights.invite_users = myAdminRights.invite_users;
                    adminRights.pin_messages = myAdminRights.pin_messages;
                    break Label_0983;
                }
                final TLRPC.TL_chatAdminRights adminRights2 = this.adminRights;
                adminRights2.change_info = myAdminRights.change_info;
                adminRights2.post_messages = myAdminRights.post_messages;
                adminRights2.edit_messages = myAdminRights.edit_messages;
                adminRights2.delete_messages = myAdminRights.delete_messages;
                adminRights2.ban_users = myAdminRights.ban_users;
                adminRights2.invite_users = myAdminRights.invite_users;
                adminRights2.pin_messages = myAdminRights.pin_messages;
                adminRights2.add_admins = myAdminRights.add_admins;
                if (!adminRights2.change_info && !adminRights2.post_messages && !adminRights2.edit_messages && !adminRights2.delete_messages && !adminRights2.ban_users && !adminRights2.invite_users && !adminRights2.pin_messages && !adminRights2.add_admins) {
                    break Label_0983;
                }
            }
            else {
                this.defaultBannedRights = defaultBannedRights;
                if (this.defaultBannedRights == null) {
                    this.defaultBannedRights = new TLRPC.TL_chatBannedRights();
                    final TLRPC.TL_chatBannedRights defaultBannedRights2 = this.defaultBannedRights;
                    defaultBannedRights2.pin_messages = false;
                    defaultBannedRights2.change_info = false;
                    defaultBannedRights2.invite_users = false;
                    defaultBannedRights2.send_polls = false;
                    defaultBannedRights2.send_inline = false;
                    defaultBannedRights2.send_games = false;
                    defaultBannedRights2.send_gifs = false;
                    defaultBannedRights2.send_stickers = false;
                    defaultBannedRights2.embed_links = false;
                    defaultBannedRights2.send_messages = false;
                    defaultBannedRights2.send_media = false;
                    defaultBannedRights2.view_messages = false;
                }
                this.bannedRights = new TLRPC.TL_chatBannedRights();
                if (tl_chatBannedRights == null) {
                    final TLRPC.TL_chatBannedRights bannedRights = this.bannedRights;
                    bannedRights.pin_messages = false;
                    bannedRights.change_info = false;
                    bannedRights.invite_users = false;
                    bannedRights.send_polls = false;
                    bannedRights.send_inline = false;
                    bannedRights.send_games = false;
                    bannedRights.send_gifs = false;
                    bannedRights.send_stickers = false;
                    bannedRights.embed_links = false;
                    bannedRights.send_messages = false;
                    bannedRights.send_media = false;
                    bannedRights.view_messages = false;
                }
                else {
                    final TLRPC.TL_chatBannedRights bannedRights2 = this.bannedRights;
                    bannedRights2.view_messages = tl_chatBannedRights.view_messages;
                    bannedRights2.send_messages = tl_chatBannedRights.send_messages;
                    bannedRights2.send_media = tl_chatBannedRights.send_media;
                    bannedRights2.send_stickers = tl_chatBannedRights.send_stickers;
                    bannedRights2.send_gifs = tl_chatBannedRights.send_gifs;
                    bannedRights2.send_games = tl_chatBannedRights.send_games;
                    bannedRights2.send_inline = tl_chatBannedRights.send_inline;
                    bannedRights2.embed_links = tl_chatBannedRights.embed_links;
                    bannedRights2.send_polls = tl_chatBannedRights.send_polls;
                    bannedRights2.invite_users = tl_chatBannedRights.invite_users;
                    bannedRights2.change_info = tl_chatBannedRights.change_info;
                    bannedRights2.pin_messages = tl_chatBannedRights.pin_messages;
                    bannedRights2.until_date = tl_chatBannedRights.until_date;
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
                if (tl_chatBannedRights != null) {
                    if (tl_chatBannedRights.view_messages) {
                        break Label_0983;
                    }
                }
            }
            n = 1;
        }
        this.rowCount += 3;
        if (currentType == 0) {
            if (this.isChannel) {
                untilDateRow = this.rowCount++;
                this.changeInfoRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.postMessagesRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.editMesagesRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.deleteMessagesRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.addUsersRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.addAdminsRow = untilDateRow;
            }
            else {
                untilDateRow = this.rowCount++;
                this.changeInfoRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.deleteMessagesRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.banUsersRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.addUsersRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.pinMessagesRow = untilDateRow;
                untilDateRow = this.rowCount++;
                this.addAdminsRow = untilDateRow;
            }
        }
        else if (currentType == 1) {
            untilDateRow = this.rowCount++;
            this.sendMessagesRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.sendMediaRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.sendStickersRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.sendPollsRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.embedLinksRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.addUsersRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.pinMessagesRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.changeInfoRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.untilSectionRow = untilDateRow;
            untilDateRow = this.rowCount++;
            this.untilDateRow = untilDateRow;
        }
        if (this.canEdit && n != 0) {
            n = this.rowCount++;
            this.rightsShadowRow = n;
            n = this.rowCount++;
            this.removeAdminRow = n;
            n = this.rowCount++;
            this.removeAdminShadowRow = n;
            this.cantEditInfoRow = -1;
        }
        else {
            this.removeAdminRow = -1;
            this.removeAdminShadowRow = -1;
            if (currentType == 0 && !this.canEdit) {
                this.rightsShadowRow = -1;
                n = this.rowCount++;
                this.cantEditInfoRow = n;
            }
            else {
                n = this.rowCount++;
                this.rightsShadowRow = n;
            }
        }
    }
    
    private boolean checkDiscard() {
        if (this.currentType != 1) {
            return true;
        }
        if (!this.currentBannedRights.equals(ChatObject.getBannedRightsString(this.bannedRights))) {
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
            builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131560995));
            builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("UserRestrictionsApplyChangesText", 2131560996, MessagesController.getInstance(super.currentAccount).getChat(this.chatId).title)));
            builder.setPositiveButton(LocaleController.getString("ApplyTheme", 2131558639), (DialogInterface$OnClickListener)new _$$Lambda$ChatRightsEditActivity$H_3StnNI8nSWDF0VmiZshr2iV_s(this));
            builder.setNegativeButton(LocaleController.getString("PassportDiscard", 2131560212), (DialogInterface$OnClickListener)new _$$Lambda$ChatRightsEditActivity$JCX77xMHDnfbaKObjN8bjfUr2po(this));
            this.showDialog(builder.create());
            return false;
        }
        return true;
    }
    
    private boolean isDefaultAdminRights() {
        final TLRPC.TL_chatAdminRights adminRights = this.adminRights;
        if (!adminRights.change_info || !adminRights.delete_messages || !adminRights.ban_users || !adminRights.invite_users || !adminRights.pin_messages || adminRights.add_admins) {
            final TLRPC.TL_chatAdminRights adminRights2 = this.adminRights;
            if (adminRights2.change_info || adminRights2.delete_messages || adminRights2.ban_users || adminRights2.invite_users || adminRights2.pin_messages || adminRights2.add_admins) {
                return false;
            }
        }
        return true;
    }
    
    private void onDonePressed() {
        final boolean channel = ChatObject.isChannel(this.currentChat);
        final int n = 1;
        final int n2 = 1;
        if (!channel) {
            final int currentType = this.currentType;
            if (currentType == 1 || (currentType == 0 && !this.isDefaultAdminRights())) {
                MessagesController.getInstance(super.currentAccount).convertToMegaGroup((Context)this.getParentActivity(), this.chatId, new _$$Lambda$ChatRightsEditActivity$hXfQmiw2gnL9RhXwZou1PDR6kt8(this));
                return;
            }
        }
        final int currentType2 = this.currentType;
        if (currentType2 == 0) {
            if (this.isChannel) {
                final TLRPC.TL_chatAdminRights adminRights = this.adminRights;
                adminRights.ban_users = false;
                adminRights.pin_messages = false;
            }
            else {
                final TLRPC.TL_chatAdminRights adminRights2 = this.adminRights;
                adminRights2.edit_messages = false;
                adminRights2.post_messages = false;
            }
            MessagesController.getInstance(super.currentAccount).setUserAdminRole(this.chatId, this.currentUser, this.adminRights, this.isChannel, this.getFragmentForAlert(1), this.isAddingNew);
            final ChatRightsEditActivityDelegate delegate = this.delegate;
            if (delegate != null) {
                final TLRPC.TL_chatAdminRights adminRights3 = this.adminRights;
                int n3 = n2;
                if (!adminRights3.change_info) {
                    n3 = n2;
                    if (!adminRights3.post_messages) {
                        n3 = n2;
                        if (!adminRights3.edit_messages) {
                            n3 = n2;
                            if (!adminRights3.delete_messages) {
                                n3 = n2;
                                if (!adminRights3.ban_users) {
                                    n3 = n2;
                                    if (!adminRights3.invite_users) {
                                        n3 = n2;
                                        if (!adminRights3.pin_messages) {
                                            if (adminRights3.add_admins) {
                                                n3 = n2;
                                            }
                                            else {
                                                n3 = 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                delegate.didSetRights(n3, this.adminRights, this.bannedRights);
            }
        }
        else if (currentType2 == 1) {
            MessagesController.getInstance(super.currentAccount).setUserBannedRole(this.chatId, this.currentUser, this.bannedRights, this.isChannel, this.getFragmentForAlert(1));
            final TLRPC.TL_chatBannedRights bannedRights = this.bannedRights;
            int n4 = n;
            if (!bannedRights.send_messages) {
                n4 = n;
                if (!bannedRights.send_stickers) {
                    n4 = n;
                    if (!bannedRights.embed_links) {
                        n4 = n;
                        if (!bannedRights.send_media) {
                            n4 = n;
                            if (!bannedRights.send_gifs) {
                                n4 = n;
                                if (!bannedRights.send_games) {
                                    if (bannedRights.send_inline) {
                                        n4 = n;
                                    }
                                    else {
                                        bannedRights.until_date = 0;
                                        n4 = 2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            final ChatRightsEditActivityDelegate delegate2 = this.delegate;
            if (delegate2 != null) {
                delegate2.didSetRights(n4, this.adminRights, this.bannedRights);
            }
        }
        this.finishFragment();
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        final ActionBar actionBar = super.actionBar;
        int verticalScrollbarPosition = 1;
        actionBar.setAllowOverlayTitle(true);
        if (this.currentType == 0) {
            super.actionBar.setTitle(LocaleController.getString("EditAdmin", 2131559302));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("UserRestrictions", 2131560994));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    if (ChatRightsEditActivity.this.checkDiscard()) {
                        ChatRightsEditActivity.this.finishFragment();
                    }
                }
                else if (n == 1) {
                    ChatRightsEditActivity.this.onDonePressed();
                }
            }
        });
        if (this.canEdit) {
            super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", 2131559299));
        }
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        this.listView = new RecyclerListView(context);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, 1, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
        this.listView.setAdapter(this.listViewAdapter = new ListAdapter(context));
        final RecyclerListView listView = this.listView;
        if (!LocaleController.isRTL) {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ChatRightsEditActivity$aMVVQ8fOnltbn3fHM_guwcH1dhE(this, context));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8 $$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8 = new _$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8(this);
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { UserCell.class, TextSettingsCell.class, TextCheckCell2.class, HeaderCell.class, TextDetailCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText5"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueImageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell2.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell2.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell2.class }, new String[] { "checkBox" }, null, null, null, "switch2Track"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell2.class }, new String[] { "checkBox" }, null, null, null, "switch2TrackChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatRightsEditActivity$GNRN9rFTP86SvIwNef_1LwNrqM8, "avatar_backgroundPink"), new ThemeDescription(null, 0, new Class[] { DialogRadioCell.class }, new String[] { "textView" }, null, null, null, "dialogTextBlack"), new ThemeDescription(null, 0, new Class[] { DialogRadioCell.class }, new String[] { "textView" }, null, null, null, "dialogTextGray2"), new ThemeDescription(null, ThemeDescription.FLAG_CHECKBOX, new Class[] { DialogRadioCell.class }, new String[] { "radioButton" }, null, null, null, "dialogRadioBackground"), new ThemeDescription(null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { DialogRadioCell.class }, new String[] { "radioButton" }, null, null, null, "dialogRadioBackgroundChecked") };
    }
    
    @Override
    public boolean onBackPressed() {
        return this.checkDiscard();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
    }
    
    public void setDelegate(final ChatRightsEditActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public interface ChatRightsEditActivityDelegate
    {
        void didSetRights(final int p0, final TLRPC.TL_chatAdminRights p1, final TLRPC.TL_chatBannedRights p2);
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return ChatRightsEditActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == 0) {
                return 0;
            }
            if (n == 1 || n == ChatRightsEditActivity.this.rightsShadowRow || n == ChatRightsEditActivity.this.removeAdminShadowRow || n == ChatRightsEditActivity.this.untilSectionRow) {
                return 5;
            }
            if (n == 2) {
                return 3;
            }
            if (n == ChatRightsEditActivity.this.changeInfoRow || n == ChatRightsEditActivity.this.postMessagesRow || n == ChatRightsEditActivity.this.editMesagesRow || n == ChatRightsEditActivity.this.deleteMessagesRow || n == ChatRightsEditActivity.this.addAdminsRow || n == ChatRightsEditActivity.this.banUsersRow || n == ChatRightsEditActivity.this.addUsersRow || n == ChatRightsEditActivity.this.pinMessagesRow || n == ChatRightsEditActivity.this.sendMessagesRow || n == ChatRightsEditActivity.this.sendMediaRow || n == ChatRightsEditActivity.this.sendStickersRow || n == ChatRightsEditActivity.this.embedLinksRow || n == ChatRightsEditActivity.this.sendPollsRow) {
                return 4;
            }
            if (n == ChatRightsEditActivity.this.cantEditInfoRow) {
                return 1;
            }
            if (n == ChatRightsEditActivity.this.untilDateRow) {
                return 6;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final boolean access$200 = ChatRightsEditActivity.this.canEdit;
            final boolean b = false;
            if (!access$200) {
                return false;
            }
            final int itemViewType = viewHolder.getItemViewType();
            if (ChatRightsEditActivity.this.currentType == 0 && itemViewType == 4) {
                final int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition == ChatRightsEditActivity.this.changeInfoRow) {
                    return ChatRightsEditActivity.this.myAdminRights.change_info;
                }
                if (adapterPosition == ChatRightsEditActivity.this.postMessagesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.post_messages;
                }
                if (adapterPosition == ChatRightsEditActivity.this.editMesagesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.edit_messages;
                }
                if (adapterPosition == ChatRightsEditActivity.this.deleteMessagesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.delete_messages;
                }
                if (adapterPosition == ChatRightsEditActivity.this.addAdminsRow) {
                    return ChatRightsEditActivity.this.myAdminRights.add_admins;
                }
                if (adapterPosition == ChatRightsEditActivity.this.banUsersRow) {
                    return ChatRightsEditActivity.this.myAdminRights.ban_users;
                }
                if (adapterPosition == ChatRightsEditActivity.this.addUsersRow) {
                    return ChatRightsEditActivity.this.myAdminRights.invite_users;
                }
                if (adapterPosition == ChatRightsEditActivity.this.pinMessagesRow) {
                    return ChatRightsEditActivity.this.myAdminRights.pin_messages;
                }
            }
            boolean b2 = b;
            if (itemViewType != 3) {
                b2 = b;
                if (itemViewType != 1) {
                    b2 = b;
                    if (itemViewType != 5) {
                        b2 = true;
                    }
                }
            }
            return b2;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = false;
            final boolean b2 = false;
            switch (itemViewType) {
                case 6: {
                    final TextDetailCell textDetailCell = (TextDetailCell)viewHolder.itemView;
                    if (n == ChatRightsEditActivity.this.untilDateRow) {
                        String s;
                        if (ChatRightsEditActivity.this.bannedRights.until_date != 0 && Math.abs(ChatRightsEditActivity.this.bannedRights.until_date - System.currentTimeMillis() / 1000L) <= 315360000L) {
                            s = LocaleController.formatDateForBan(ChatRightsEditActivity.this.bannedRights.until_date);
                        }
                        else {
                            s = LocaleController.getString("UserRestrictionsUntilForever", 2131561019);
                        }
                        textDetailCell.setTextAndValue(LocaleController.getString("UserRestrictionsDuration", 2131561001), s, false);
                        break;
                    }
                    break;
                }
                case 5: {
                    final ShadowSectionCell shadowSectionCell = (ShadowSectionCell)viewHolder.itemView;
                    final int access$2700 = ChatRightsEditActivity.this.rightsShadowRow;
                    final int n2 = 2131165395;
                    if (n == access$2700) {
                        final Context mContext = this.mContext;
                        if (ChatRightsEditActivity.this.removeAdminRow == -1) {
                            n = n2;
                        }
                        else {
                            n = 2131165394;
                        }
                        shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(mContext, n, "windowBackgroundGrayShadow"));
                        break;
                    }
                    if (n == ChatRightsEditActivity.this.removeAdminShadowRow) {
                        shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        break;
                    }
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    break;
                }
                case 4: {
                    final TextCheckCell2 textCheckCell2 = (TextCheckCell2)viewHolder.itemView;
                    final int access$2701 = ChatRightsEditActivity.this.changeInfoRow;
                    int n3 = 2131165736;
                    if (n == access$2701) {
                        if (ChatRightsEditActivity.this.currentType == 0) {
                            if (ChatRightsEditActivity.this.isChannel) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminChangeChannelInfo", 2131559308), ChatRightsEditActivity.this.adminRights.change_info, true);
                            }
                            else {
                                textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminChangeGroupInfo", 2131559309), ChatRightsEditActivity.this.adminRights.change_info, true);
                            }
                        }
                        else if (ChatRightsEditActivity.this.currentType == 1) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsChangeInfo", 2131560999), !ChatRightsEditActivity.this.bannedRights.change_info && !ChatRightsEditActivity.this.defaultBannedRights.change_info, false);
                            if (!ChatRightsEditActivity.this.defaultBannedRights.change_info) {
                                n3 = 0;
                            }
                            textCheckCell2.setIcon(n3);
                        }
                    }
                    else if (n == ChatRightsEditActivity.this.postMessagesRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminPostMessages", 2131559314), ChatRightsEditActivity.this.adminRights.post_messages, true);
                    }
                    else if (n == ChatRightsEditActivity.this.editMesagesRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminEditMessages", 2131559311), ChatRightsEditActivity.this.adminRights.edit_messages, true);
                    }
                    else if (n == ChatRightsEditActivity.this.deleteMessagesRow) {
                        if (ChatRightsEditActivity.this.isChannel) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminDeleteMessages", 2131559310), ChatRightsEditActivity.this.adminRights.delete_messages, true);
                        }
                        else {
                            textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminGroupDeleteMessages", 2131559312), ChatRightsEditActivity.this.adminRights.delete_messages, true);
                        }
                    }
                    else if (n == ChatRightsEditActivity.this.addAdminsRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminAddAdmins", 2131559303), ChatRightsEditActivity.this.adminRights.add_admins, false);
                    }
                    else if (n == ChatRightsEditActivity.this.banUsersRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminBanUsers", 2131559306), ChatRightsEditActivity.this.adminRights.ban_users, true);
                    }
                    else if (n == ChatRightsEditActivity.this.addUsersRow) {
                        if (ChatRightsEditActivity.this.currentType == 0) {
                            if (ChatObject.isActionBannedByDefault(ChatRightsEditActivity.this.currentChat, 3)) {
                                textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminAddUsers", 2131559304), ChatRightsEditActivity.this.adminRights.invite_users, true);
                            }
                            else {
                                textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminAddUsersViaLink", 2131559305), ChatRightsEditActivity.this.adminRights.invite_users, true);
                            }
                        }
                        else if (ChatRightsEditActivity.this.currentType == 1) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsInviteUsers", 2131561003), !ChatRightsEditActivity.this.bannedRights.invite_users && !ChatRightsEditActivity.this.defaultBannedRights.invite_users, true);
                            if (!ChatRightsEditActivity.this.defaultBannedRights.invite_users) {
                                n3 = 0;
                            }
                            textCheckCell2.setIcon(n3);
                        }
                    }
                    else if (n == ChatRightsEditActivity.this.pinMessagesRow) {
                        if (ChatRightsEditActivity.this.currentType == 0) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString("EditAdminPinMessages", 2131559313), ChatRightsEditActivity.this.adminRights.pin_messages, true);
                        }
                        else if (ChatRightsEditActivity.this.currentType == 1) {
                            textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsPinMessages", 2131561013), !ChatRightsEditActivity.this.bannedRights.pin_messages && !ChatRightsEditActivity.this.defaultBannedRights.pin_messages, true);
                            if (!ChatRightsEditActivity.this.defaultBannedRights.pin_messages) {
                                n3 = 0;
                            }
                            textCheckCell2.setIcon(n3);
                        }
                    }
                    else if (n == ChatRightsEditActivity.this.sendMessagesRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSend", 2131561015), !ChatRightsEditActivity.this.bannedRights.send_messages && !ChatRightsEditActivity.this.defaultBannedRights.send_messages, true);
                        if (!ChatRightsEditActivity.this.defaultBannedRights.send_messages) {
                            n3 = 0;
                        }
                        textCheckCell2.setIcon(n3);
                    }
                    else if (n == ChatRightsEditActivity.this.sendMediaRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendMedia", 2131561016), !ChatRightsEditActivity.this.bannedRights.send_media && !ChatRightsEditActivity.this.defaultBannedRights.send_media, true);
                        if (!ChatRightsEditActivity.this.defaultBannedRights.send_media) {
                            n3 = 0;
                        }
                        textCheckCell2.setIcon(n3);
                    }
                    else if (n == ChatRightsEditActivity.this.sendStickersRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendStickers", 2131561018), !ChatRightsEditActivity.this.bannedRights.send_stickers && !ChatRightsEditActivity.this.defaultBannedRights.send_stickers, true);
                        if (!ChatRightsEditActivity.this.defaultBannedRights.send_stickers) {
                            n3 = 0;
                        }
                        textCheckCell2.setIcon(n3);
                    }
                    else if (n == ChatRightsEditActivity.this.embedLinksRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsEmbedLinks", 2131561002), !ChatRightsEditActivity.this.bannedRights.embed_links && !ChatRightsEditActivity.this.defaultBannedRights.embed_links, true);
                        if (!ChatRightsEditActivity.this.defaultBannedRights.embed_links) {
                            n3 = 0;
                        }
                        textCheckCell2.setIcon(n3);
                    }
                    else if (n == ChatRightsEditActivity.this.sendPollsRow) {
                        textCheckCell2.setTextAndCheck(LocaleController.getString("UserRestrictionsSendPolls", 2131561017), !ChatRightsEditActivity.this.bannedRights.send_polls && !ChatRightsEditActivity.this.defaultBannedRights.send_polls, true);
                        if (!ChatRightsEditActivity.this.defaultBannedRights.send_polls) {
                            n3 = 0;
                        }
                        textCheckCell2.setIcon(n3);
                    }
                    if (n == ChatRightsEditActivity.this.sendMediaRow || n == ChatRightsEditActivity.this.sendStickersRow || n == ChatRightsEditActivity.this.embedLinksRow || n == ChatRightsEditActivity.this.sendPollsRow) {
                        boolean enabled = b;
                        if (!ChatRightsEditActivity.this.bannedRights.send_messages) {
                            enabled = b;
                            if (!ChatRightsEditActivity.this.bannedRights.view_messages) {
                                enabled = b;
                                if (!ChatRightsEditActivity.this.defaultBannedRights.send_messages) {
                                    enabled = b;
                                    if (!ChatRightsEditActivity.this.defaultBannedRights.view_messages) {
                                        enabled = true;
                                    }
                                }
                            }
                        }
                        textCheckCell2.setEnabled(enabled);
                        break;
                    }
                    if (n == ChatRightsEditActivity.this.sendMessagesRow) {
                        boolean enabled2 = b2;
                        if (!ChatRightsEditActivity.this.bannedRights.view_messages) {
                            enabled2 = b2;
                            if (!ChatRightsEditActivity.this.defaultBannedRights.view_messages) {
                                enabled2 = true;
                            }
                        }
                        textCheckCell2.setEnabled(enabled2);
                        break;
                    }
                    break;
                }
                case 3: {
                    final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                    if (ChatRightsEditActivity.this.currentType == 0) {
                        headerCell.setText(LocaleController.getString("EditAdminWhatCanDo", 2131559318));
                        break;
                    }
                    if (ChatRightsEditActivity.this.currentType == 1) {
                        headerCell.setText(LocaleController.getString("UserRestrictionsCanDo", 2131560998));
                        break;
                    }
                    break;
                }
                case 2: {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    if (n != ChatRightsEditActivity.this.removeAdminRow) {
                        break;
                    }
                    textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
                    textSettingsCell.setTag((Object)"windowBackgroundWhiteRedText5");
                    if (ChatRightsEditActivity.this.currentType == 0) {
                        textSettingsCell.setText(LocaleController.getString("EditAdminRemoveAdmin", 2131559316), false);
                        break;
                    }
                    if (ChatRightsEditActivity.this.currentType == 1) {
                        textSettingsCell.setText(LocaleController.getString("UserRestrictionsBlock", 2131560997), false);
                        break;
                    }
                    break;
                }
                case 1: {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (n == ChatRightsEditActivity.this.cantEditInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("EditAdminCantEdit", 2131559307));
                        break;
                    }
                    break;
                }
                case 0: {
                    ((UserCell)viewHolder.itemView).setData(ChatRightsEditActivity.this.currentUser, null, null, 0);
                    break;
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                if (n != 5) {
                                    o = new TextDetailCell(this.mContext);
                                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                }
                                else {
                                    o = new ShadowSectionCell(this.mContext);
                                }
                            }
                            else {
                                o = new TextCheckCell2(this.mContext);
                                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                            }
                        }
                        else {
                            o = new HeaderCell(this.mContext);
                            ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                    }
                    else {
                        o = new TextSettingsCell(this.mContext);
                        ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    o = new TextInfoPrivacyCell(this.mContext);
                    ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                }
            }
            else {
                o = new UserCell(this.mContext, 4, 0);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
}
