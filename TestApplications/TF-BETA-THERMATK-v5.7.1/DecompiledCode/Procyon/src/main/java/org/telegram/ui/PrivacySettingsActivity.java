// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.widget.Toast;
import org.telegram.messenger.FileLog;
import android.content.DialogInterface;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.AndroidUtilities;
import android.view.View$OnClickListener;
import org.telegram.ui.Cells.CheckBoxCell;
import android.widget.LinearLayout;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.SharedConfig;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import android.content.Context;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PrivacySettingsActivity extends BaseFragment implements NotificationCenterDelegate
{
    private int advancedSectionRow;
    private int blockedRow;
    private int botsDetailRow;
    private int botsSectionRow;
    private int callsRow;
    private boolean[] clear;
    private int clearDraftsRow;
    private int contactsDeleteRow;
    private int contactsDetailRow;
    private int contactsSectionRow;
    private int contactsSuggestRow;
    private int contactsSyncRow;
    private boolean currentSuggest;
    private boolean currentSync;
    private int deleteAccountDetailRow;
    private int deleteAccountRow;
    private int forwardsRow;
    private int groupsDetailRow;
    private int groupsRow;
    private int lastSeenRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean newSuggest;
    private boolean newSync;
    private int passcodeRow;
    private int passportRow;
    private int passwordRow;
    private int paymentsClearRow;
    private int phoneNumberRow;
    private int privacySectionRow;
    private int profilePhotoRow;
    private AlertDialog progressDialog;
    private int rowCount;
    private int secretDetailRow;
    private int secretMapRow;
    private int secretSectionRow;
    private int secretWebpageRow;
    private int securitySectionRow;
    private int sessionsDetailRow;
    private int sessionsRow;
    private int webSessionsRow;
    
    public PrivacySettingsActivity() {
        this.clear = new boolean[2];
    }
    
    public static String formatRulesString(final int n, final int n2) {
        final ArrayList<TLRPC.PrivacyRule> privacyRules = ContactsController.getInstance(n).getPrivacyRules(n2);
        if (privacyRules.size() == 0) {
            if (n2 == 3) {
                return LocaleController.getString("P2PNobody", 2131560141);
            }
            return LocaleController.getString("LastSeenNobody", 2131559739);
        }
        else {
            int i = 0;
            int n3 = -1;
            int n4 = 0;
            int n5 = 0;
            while (i < privacyRules.size()) {
                final TLRPC.PrivacyRule privacyRule = privacyRules.get(i);
                int n7;
                int n8;
                int n9;
                if (privacyRule instanceof TLRPC.TL_privacyValueAllowChatParticipants) {
                    final TLRPC.TL_privacyValueAllowChatParticipants tl_privacyValueAllowChatParticipants = (TLRPC.TL_privacyValueAllowChatParticipants)privacyRule;
                    final int size = tl_privacyValueAllowChatParticipants.chats.size();
                    int n6 = n5;
                    for (int j = 0; j < size; ++j) {
                        final TLRPC.Chat chat = MessagesController.getInstance(n).getChat(tl_privacyValueAllowChatParticipants.chats.get(j));
                        if (chat != null) {
                            n6 += chat.participants_count;
                        }
                    }
                    n7 = n6;
                    n8 = n3;
                    n9 = n4;
                }
                else if (privacyRule instanceof TLRPC.TL_privacyValueDisallowChatParticipants) {
                    final TLRPC.TL_privacyValueDisallowChatParticipants tl_privacyValueDisallowChatParticipants = (TLRPC.TL_privacyValueDisallowChatParticipants)privacyRule;
                    final int size2 = tl_privacyValueDisallowChatParticipants.chats.size();
                    int index = 0;
                    while (true) {
                        n8 = n3;
                        n9 = n4;
                        n7 = n5;
                        if (index >= size2) {
                            break;
                        }
                        final TLRPC.Chat chat2 = MessagesController.getInstance(n).getChat(tl_privacyValueDisallowChatParticipants.chats.get(index));
                        if (chat2 != null) {
                            n4 += chat2.participants_count;
                        }
                        ++index;
                    }
                }
                else if (privacyRule instanceof TLRPC.TL_privacyValueAllowUsers) {
                    n7 = n5 + ((TLRPC.TL_privacyValueAllowUsers)privacyRule).users.size();
                    n8 = n3;
                    n9 = n4;
                }
                else if (privacyRule instanceof TLRPC.TL_privacyValueDisallowUsers) {
                    n9 = n4 + ((TLRPC.TL_privacyValueDisallowUsers)privacyRule).users.size();
                    n8 = n3;
                    n7 = n5;
                }
                else {
                    n8 = n3;
                    n9 = n4;
                    n7 = n5;
                    if (n3 == -1) {
                        if (privacyRule instanceof TLRPC.TL_privacyValueAllowAll) {
                            n8 = 0;
                            n9 = n4;
                            n7 = n5;
                        }
                        else if (privacyRule instanceof TLRPC.TL_privacyValueDisallowAll) {
                            n8 = 1;
                            n9 = n4;
                            n7 = n5;
                        }
                        else {
                            n8 = 2;
                            n7 = n5;
                            n9 = n4;
                        }
                    }
                }
                ++i;
                n3 = n8;
                n4 = n9;
                n5 = n7;
            }
            if (n3 != 0 && (n3 != -1 || n4 <= 0)) {
                if (n3 != 2 && (n3 != -1 || n4 <= 0 || n5 <= 0)) {
                    if (n3 != 1 && n5 <= 0) {
                        return "unknown";
                    }
                    if (n2 == 3) {
                        if (n5 == 0) {
                            return LocaleController.getString("P2PNobody", 2131560141);
                        }
                        return LocaleController.formatString("P2PNobodyPlus", 2131560142, n5);
                    }
                    else {
                        if (n5 == 0) {
                            return LocaleController.getString("LastSeenNobody", 2131559739);
                        }
                        return LocaleController.formatString("LastSeenNobodyPlus", 2131559740, n5);
                    }
                }
                else if (n2 == 3) {
                    if (n5 == 0 && n4 == 0) {
                        return LocaleController.getString("P2PContacts", 2131560134);
                    }
                    if (n5 != 0 && n4 != 0) {
                        return LocaleController.formatString("P2PContactsMinusPlus", 2131560136, n4, n5);
                    }
                    if (n4 != 0) {
                        return LocaleController.formatString("P2PContactsMinus", 2131560135, n4);
                    }
                    return LocaleController.formatString("P2PContactsPlus", 2131560137, n5);
                }
                else {
                    if (n5 == 0 && n4 == 0) {
                        return LocaleController.getString("LastSeenContacts", 2131559730);
                    }
                    if (n5 != 0 && n4 != 0) {
                        return LocaleController.formatString("LastSeenContactsMinusPlus", 2131559732, n4, n5);
                    }
                    if (n4 != 0) {
                        return LocaleController.formatString("LastSeenContactsMinus", 2131559731, n4);
                    }
                    return LocaleController.formatString("LastSeenContactsPlus", 2131559733, n5);
                }
            }
            else if (n2 == 3) {
                if (n4 == 0) {
                    return LocaleController.getString("P2PEverybody", 2131560139);
                }
                return LocaleController.formatString("P2PEverybodyMinus", 2131560140, n4);
            }
            else {
                if (n4 == 0) {
                    return LocaleController.getString("LastSeenEverybody", 2131559736);
                }
                return LocaleController.formatString("LastSeenEverybodyMinus", 2131559737, n4);
            }
        }
    }
    
    private void loadPasswordSettings() {
        if (UserConfig.getInstance(super.currentAccount).hasSecureData) {
            return;
        }
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_account_getPassword(), new _$$Lambda$PrivacySettingsActivity$7MIQgKJpVA2M6eCuYxDI1zmVMaY(this), 10);
    }
    
    private void updateRows() {
        this.rowCount = 0;
        this.privacySectionRow = this.rowCount++;
        this.blockedRow = this.rowCount++;
        this.phoneNumberRow = this.rowCount++;
        this.lastSeenRow = this.rowCount++;
        this.profilePhotoRow = this.rowCount++;
        this.forwardsRow = this.rowCount++;
        this.callsRow = this.rowCount++;
        this.groupsRow = this.rowCount++;
        this.groupsDetailRow = this.rowCount++;
        this.securitySectionRow = this.rowCount++;
        this.passcodeRow = this.rowCount++;
        this.passwordRow = this.rowCount++;
        this.sessionsRow = this.rowCount++;
        this.sessionsDetailRow = this.rowCount++;
        this.advancedSectionRow = this.rowCount++;
        this.clearDraftsRow = this.rowCount++;
        this.deleteAccountRow = this.rowCount++;
        this.deleteAccountDetailRow = this.rowCount++;
        this.botsSectionRow = this.rowCount++;
        if (UserConfig.getInstance(super.currentAccount).hasSecureData) {
            this.passportRow = this.rowCount++;
        }
        else {
            this.passportRow = -1;
        }
        this.paymentsClearRow = this.rowCount++;
        this.webSessionsRow = this.rowCount++;
        this.botsDetailRow = this.rowCount++;
        this.contactsSectionRow = this.rowCount++;
        this.contactsDeleteRow = this.rowCount++;
        this.contactsSyncRow = this.rowCount++;
        this.contactsSuggestRow = this.rowCount++;
        this.contactsDetailRow = this.rowCount++;
        this.secretSectionRow = this.rowCount++;
        this.secretMapRow = this.rowCount++;
        this.secretWebpageRow = this.rowCount++;
        this.secretDetailRow = this.rowCount++;
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("PrivacySettings", 2131560509));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PrivacySettingsActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.listView = new RecyclerListView(context)).setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PrivacySettingsActivity$4NW4D3yhnbANs4O1uZu09FeFwHI(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.privacyRulesUpdated) {
            final ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, HeaderCell.class, TextCheckCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        ContactsController.getInstance(super.currentAccount).loadPrivacySettings();
        final boolean syncContacts = UserConfig.getInstance(super.currentAccount).syncContacts;
        this.newSync = syncContacts;
        this.currentSync = syncContacts;
        final boolean suggestContacts = UserConfig.getInstance(super.currentAccount).suggestContacts;
        this.newSuggest = suggestContacts;
        this.currentSuggest = suggestContacts;
        this.updateRows();
        this.loadPasswordSettings();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
        if (this.currentSync != this.newSync) {
            UserConfig.getInstance(super.currentAccount).syncContacts = this.newSync;
            UserConfig.getInstance(super.currentAccount).saveConfig(false);
            if (this.newSync) {
                ContactsController.getInstance(super.currentAccount).forceImportContacts();
                if (this.getParentActivity() != null) {
                    Toast.makeText((Context)this.getParentActivity(), (CharSequence)LocaleController.getString("SyncContactsAdded", 2131560850), 0).show();
                }
            }
        }
        final boolean newSuggest = this.newSuggest;
        if (newSuggest != this.currentSuggest) {
            if (!newSuggest) {
                DataQuery.getInstance(super.currentAccount).clearTopPeers();
            }
            UserConfig.getInstance(super.currentAccount).suggestContacts = this.newSuggest;
            UserConfig.getInstance(super.currentAccount).saveConfig(false);
            final TLRPC.TL_contacts_toggleTopPeers tl_contacts_toggleTopPeers = new TLRPC.TL_contacts_toggleTopPeers();
            tl_contacts_toggleTopPeers.enabled = this.newSuggest;
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_contacts_toggleTopPeers, (RequestDelegate)_$$Lambda$PrivacySettingsActivity$IBy4A3n5R5n7oI7wDCjl___b3rk.INSTANCE);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return PrivacySettingsActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == PrivacySettingsActivity.this.passportRow || n == PrivacySettingsActivity.this.lastSeenRow || n == PrivacySettingsActivity.this.phoneNumberRow || n == PrivacySettingsActivity.this.blockedRow || n == PrivacySettingsActivity.this.deleteAccountRow || n == PrivacySettingsActivity.this.sessionsRow || n == PrivacySettingsActivity.this.webSessionsRow || n == PrivacySettingsActivity.this.passwordRow || n == PrivacySettingsActivity.this.passcodeRow || n == PrivacySettingsActivity.this.groupsRow || n == PrivacySettingsActivity.this.paymentsClearRow || n == PrivacySettingsActivity.this.secretMapRow || n == PrivacySettingsActivity.this.contactsDeleteRow || n == PrivacySettingsActivity.this.clearDraftsRow) {
                return 0;
            }
            if (n == PrivacySettingsActivity.this.deleteAccountDetailRow || n == PrivacySettingsActivity.this.groupsDetailRow || n == PrivacySettingsActivity.this.sessionsDetailRow || n == PrivacySettingsActivity.this.secretDetailRow || n == PrivacySettingsActivity.this.botsDetailRow || n == PrivacySettingsActivity.this.contactsDetailRow) {
                return 1;
            }
            if (n == PrivacySettingsActivity.this.securitySectionRow || n == PrivacySettingsActivity.this.advancedSectionRow || n == PrivacySettingsActivity.this.privacySectionRow || n == PrivacySettingsActivity.this.secretSectionRow || n == PrivacySettingsActivity.this.botsSectionRow || n == PrivacySettingsActivity.this.contactsSectionRow) {
                return 2;
            }
            if (n != PrivacySettingsActivity.this.secretWebpageRow && n != PrivacySettingsActivity.this.contactsSyncRow && n != PrivacySettingsActivity.this.contactsSuggestRow) {
                return 0;
            }
            return 3;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            final int access$000 = PrivacySettingsActivity.this.passcodeRow;
            boolean b = false;
            if (adapterPosition == access$000 || adapterPosition == PrivacySettingsActivity.this.passwordRow || adapterPosition == PrivacySettingsActivity.this.blockedRow || adapterPosition == PrivacySettingsActivity.this.sessionsRow || adapterPosition == PrivacySettingsActivity.this.secretWebpageRow || adapterPosition == PrivacySettingsActivity.this.webSessionsRow || adapterPosition == PrivacySettingsActivity.this.clearDraftsRow || (adapterPosition == PrivacySettingsActivity.this.groupsRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(1)) || (adapterPosition == PrivacySettingsActivity.this.lastSeenRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(0)) || (adapterPosition == PrivacySettingsActivity.this.callsRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(2)) || (adapterPosition == PrivacySettingsActivity.this.profilePhotoRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(4)) || (adapterPosition == PrivacySettingsActivity.this.forwardsRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(5)) || (adapterPosition == PrivacySettingsActivity.this.phoneNumberRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(6)) || (adapterPosition == PrivacySettingsActivity.this.deleteAccountRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingDeleteInfo()) || adapterPosition == PrivacySettingsActivity.this.paymentsClearRow || adapterPosition == PrivacySettingsActivity.this.secretMapRow || adapterPosition == PrivacySettingsActivity.this.contactsSyncRow || adapterPosition == PrivacySettingsActivity.this.passportRow || adapterPosition == PrivacySettingsActivity.this.contactsDeleteRow || adapterPosition == PrivacySettingsActivity.this.contactsSuggestRow) {
                b = true;
            }
            return b;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = true;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType == 3) {
                            final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                            if (n == PrivacySettingsActivity.this.secretWebpageRow) {
                                final String string = LocaleController.getString("SecretWebPage", 2131560673);
                                if (MessagesController.getInstance(PrivacySettingsActivity.this.currentAccount).secretWebpagePreview != 1) {
                                    b = false;
                                }
                                textCheckCell.setTextAndCheck(string, b, false);
                            }
                            else if (n == PrivacySettingsActivity.this.contactsSyncRow) {
                                textCheckCell.setTextAndCheck(LocaleController.getString("SyncContacts", 2131560849), PrivacySettingsActivity.this.newSync, true);
                            }
                            else if (n == PrivacySettingsActivity.this.contactsSuggestRow) {
                                textCheckCell.setTextAndCheck(LocaleController.getString("SuggestContacts", 2131560840), PrivacySettingsActivity.this.newSuggest, false);
                            }
                        }
                    }
                    else {
                        final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                        if (n == PrivacySettingsActivity.this.privacySectionRow) {
                            headerCell.setText(LocaleController.getString("PrivacyTitle", 2131560511));
                        }
                        else if (n == PrivacySettingsActivity.this.securitySectionRow) {
                            headerCell.setText(LocaleController.getString("SecurityTitle", 2131560675));
                        }
                        else if (n == PrivacySettingsActivity.this.advancedSectionRow) {
                            headerCell.setText(LocaleController.getString("PrivacyAdvanced", 2131560475));
                        }
                        else if (n == PrivacySettingsActivity.this.secretSectionRow) {
                            headerCell.setText(LocaleController.getString("SecretChat", 2131560669));
                        }
                        else if (n == PrivacySettingsActivity.this.botsSectionRow) {
                            headerCell.setText(LocaleController.getString("PrivacyBots", 2131560476));
                        }
                        else if (n == PrivacySettingsActivity.this.contactsSectionRow) {
                            headerCell.setText(LocaleController.getString("Contacts", 2131559149));
                        }
                    }
                }
                else {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (n == PrivacySettingsActivity.this.deleteAccountDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("DeleteAccountHelp", 2131559228));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else if (n == PrivacySettingsActivity.this.groupsDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("GroupsAndChannelsHelp", 2131559625));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else if (n == PrivacySettingsActivity.this.sessionsDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("SessionsInfo", 2131560725));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else if (n == PrivacySettingsActivity.this.secretDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("SecretWebPageInfo", 2131560674));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else if (n == PrivacySettingsActivity.this.botsDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("PrivacyBotsInfo", 2131560477));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else if (n == PrivacySettingsActivity.this.contactsDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("SuggestContactsInfo", 2131560842));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                }
            }
            else {
                final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                if (n == PrivacySettingsActivity.this.blockedRow) {
                    if (!BaseFragment.this.getMessagesController().loadingBlockedUsers) {
                        if (BaseFragment.this.getMessagesController().blockedUsers.size() == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("BlockedUsers", 2131558835), LocaleController.getString("EmptyExceptions", 2131559346), true);
                        }
                        else {
                            textSettingsCell.setTextAndValue(LocaleController.getString("BlockedUsers", 2131558835), String.format("%d", BaseFragment.this.getMessagesController().blockedUsers.size()), true);
                        }
                    }
                    else {
                        textSettingsCell.setText(LocaleController.getString("BlockedUsers", 2131558835), true);
                    }
                }
                else if (n == PrivacySettingsActivity.this.sessionsRow) {
                    textSettingsCell.setText(LocaleController.getString("SessionsTitle", 2131560726), false);
                }
                else if (n == PrivacySettingsActivity.this.webSessionsRow) {
                    textSettingsCell.setText(LocaleController.getString("WebSessionsTitle", 2131561104), false);
                }
                else if (n == PrivacySettingsActivity.this.passwordRow) {
                    textSettingsCell.setText(LocaleController.getString("TwoStepVerification", 2131560919), true);
                }
                else if (n == PrivacySettingsActivity.this.passcodeRow) {
                    textSettingsCell.setText(LocaleController.getString("Passcode", 2131560160), true);
                }
                else if (n == PrivacySettingsActivity.this.phoneNumberRow) {
                    String s;
                    if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(6)) {
                        s = LocaleController.getString("Loading", 2131559768);
                    }
                    else {
                        s = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 6);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyPhone", 2131560498), s, true);
                }
                else if (n == PrivacySettingsActivity.this.lastSeenRow) {
                    String s2;
                    if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(0)) {
                        s2 = LocaleController.getString("Loading", 2131559768);
                    }
                    else {
                        s2 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 0);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyLastSeen", 2131560492), s2, true);
                }
                else if (n == PrivacySettingsActivity.this.groupsRow) {
                    String s3;
                    if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(1)) {
                        s3 = LocaleController.getString("Loading", 2131559768);
                    }
                    else {
                        s3 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 1);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("GroupsAndChannels", 2131559624), s3, false);
                }
                else if (n == PrivacySettingsActivity.this.callsRow) {
                    String s4;
                    if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(2)) {
                        s4 = LocaleController.getString("Loading", 2131559768);
                    }
                    else {
                        s4 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 2);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("Calls", 2131558888), s4, true);
                }
                else if (n == PrivacySettingsActivity.this.profilePhotoRow) {
                    String s5;
                    if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(4)) {
                        s5 = LocaleController.getString("Loading", 2131559768);
                    }
                    else {
                        s5 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 4);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyProfilePhoto", 2131560505), s5, true);
                }
                else if (n == PrivacySettingsActivity.this.forwardsRow) {
                    String s6;
                    if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(5)) {
                        s6 = LocaleController.getString("Loading", 2131559768);
                    }
                    else {
                        s6 = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 5);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyForwards", 2131560484), s6, true);
                }
                else if (n == PrivacySettingsActivity.this.passportRow) {
                    textSettingsCell.setText(LocaleController.getString("TelegramPassport", 2131560872), true);
                }
                else if (n == PrivacySettingsActivity.this.deleteAccountRow) {
                    String s7;
                    if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingDeleteInfo()) {
                        s7 = LocaleController.getString("Loading", 2131559768);
                    }
                    else {
                        n = ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getDeleteAccountTTL();
                        if (n <= 182) {
                            s7 = LocaleController.formatPluralString("Months", n / 30);
                        }
                        else if (n == 365) {
                            s7 = LocaleController.formatPluralString("Years", n / 365);
                        }
                        else {
                            s7 = LocaleController.formatPluralString("Days", n);
                        }
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("DeleteAccountIfAwayFor2", 2131559229), s7, false);
                }
                else if (n == PrivacySettingsActivity.this.clearDraftsRow) {
                    textSettingsCell.setText(LocaleController.getString("PrivacyDeleteCloudDrafts", 2131560481), true);
                }
                else if (n == PrivacySettingsActivity.this.paymentsClearRow) {
                    textSettingsCell.setText(LocaleController.getString("PrivacyPaymentsClear", 2131560496), true);
                }
                else if (n == PrivacySettingsActivity.this.secretMapRow) {
                    n = SharedConfig.mapPreviewType;
                    String s8;
                    if (n != 0) {
                        if (n != 1) {
                            s8 = LocaleController.getString("MapPreviewProviderNobody", 2131559803);
                        }
                        else {
                            s8 = LocaleController.getString("MapPreviewProviderGoogle", 2131559802);
                        }
                    }
                    else {
                        s8 = LocaleController.getString("MapPreviewProviderTelegram", 2131559804);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("MapPreviewProvider", 2131559801), s8, true);
                }
                else if (n == PrivacySettingsActivity.this.contactsDeleteRow) {
                    textSettingsCell.setText(LocaleController.getString("SyncContactsDelete", 2131560851), true);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        frameLayout = new TextCheckCell(this.mContext);
                        ((TextCheckCell)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                    else {
                        frameLayout = new HeaderCell(this.mContext);
                        ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    frameLayout = new TextInfoPrivacyCell(this.mContext);
                }
            }
            else {
                frameLayout = new TextSettingsCell(this.mContext);
                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
}
