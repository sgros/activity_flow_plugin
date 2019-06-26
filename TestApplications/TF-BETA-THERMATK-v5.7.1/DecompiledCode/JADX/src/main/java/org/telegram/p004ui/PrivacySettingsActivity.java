package org.telegram.p004ui;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.BottomSheet;
import org.telegram.p004ui.ActionBar.BottomSheet.BottomSheetCell;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.CheckBoxCell;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.TextCheckCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.PrivacyRule;
import org.telegram.tgnet.TLRPC.TL_accountDaysTTL;
import org.telegram.tgnet.TLRPC.TL_account_getPassword;
import org.telegram.tgnet.TLRPC.TL_account_password;
import org.telegram.tgnet.TLRPC.TL_account_setAccountTTL;
import org.telegram.tgnet.TLRPC.TL_boolTrue;
import org.telegram.tgnet.TLRPC.TL_contacts_toggleTopPeers;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_clearAllDrafts;
import org.telegram.tgnet.TLRPC.TL_payments_clearSavedInfo;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowAll;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowChatParticipants;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowUsers;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowAll;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowChatParticipants;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowUsers;

/* renamed from: org.telegram.ui.PrivacySettingsActivity */
public class PrivacySettingsActivity extends BaseFragment implements NotificationCenterDelegate {
    private int advancedSectionRow;
    private int blockedRow;
    private int botsDetailRow;
    private int botsSectionRow;
    private int callsRow;
    private boolean[] clear = new boolean[2];
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

    /* renamed from: org.telegram.ui.PrivacySettingsActivity$1 */
    class C43041 extends ActionBarMenuOnItemClick {
        C43041() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                PrivacySettingsActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.PrivacySettingsActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            if (adapterPosition == PrivacySettingsActivity.this.passcodeRow || adapterPosition == PrivacySettingsActivity.this.passwordRow || adapterPosition == PrivacySettingsActivity.this.blockedRow || adapterPosition == PrivacySettingsActivity.this.sessionsRow || adapterPosition == PrivacySettingsActivity.this.secretWebpageRow || adapterPosition == PrivacySettingsActivity.this.webSessionsRow || adapterPosition == PrivacySettingsActivity.this.clearDraftsRow || ((adapterPosition == PrivacySettingsActivity.this.groupsRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(1)) || ((adapterPosition == PrivacySettingsActivity.this.lastSeenRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(0)) || ((adapterPosition == PrivacySettingsActivity.this.callsRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(2)) || ((adapterPosition == PrivacySettingsActivity.this.profilePhotoRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(4)) || ((adapterPosition == PrivacySettingsActivity.this.forwardsRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(5)) || ((adapterPosition == PrivacySettingsActivity.this.phoneNumberRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(6)) || ((adapterPosition == PrivacySettingsActivity.this.deleteAccountRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingDeleteInfo()) || adapterPosition == PrivacySettingsActivity.this.paymentsClearRow || adapterPosition == PrivacySettingsActivity.this.secretMapRow || adapterPosition == PrivacySettingsActivity.this.contactsSyncRow || adapterPosition == PrivacySettingsActivity.this.passportRow || adapterPosition == PrivacySettingsActivity.this.contactsDeleteRow || adapterPosition == PrivacySettingsActivity.this.contactsSuggestRow)))))))) {
                return true;
            }
            return false;
        }

        public int getItemCount() {
            return PrivacySettingsActivity.this.rowCount;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textSettingsCell;
            String str = Theme.key_windowBackgroundWhite;
            if (i == 0) {
                textSettingsCell = new TextSettingsCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor(str));
            } else if (i == 1) {
                textSettingsCell = new TextInfoPrivacyCell(this.mContext);
            } else if (i != 2) {
                textSettingsCell = new TextCheckCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor(str));
            } else {
                textSettingsCell = new HeaderCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor(str));
            }
            return new Holder(textSettingsCell);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            boolean z = true;
            String str;
            String string;
            if (itemViewType == 0) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (i == PrivacySettingsActivity.this.blockedRow) {
                    str = "BlockedUsers";
                    if (PrivacySettingsActivity.this.getMessagesController().loadingBlockedUsers) {
                        textSettingsCell.setText(LocaleController.getString(str, C1067R.string.BlockedUsers), true);
                    } else if (PrivacySettingsActivity.this.getMessagesController().blockedUsers.size() == 0) {
                        textSettingsCell.setTextAndValue(LocaleController.getString(str, C1067R.string.BlockedUsers), LocaleController.getString("EmptyExceptions", C1067R.string.EmptyExceptions), true);
                    } else {
                        textSettingsCell.setTextAndValue(LocaleController.getString(str, C1067R.string.BlockedUsers), String.format("%d", new Object[]{Integer.valueOf(PrivacySettingsActivity.this.getMessagesController().blockedUsers.size())}), true);
                    }
                } else if (i == PrivacySettingsActivity.this.sessionsRow) {
                    textSettingsCell.setText(LocaleController.getString("SessionsTitle", C1067R.string.SessionsTitle), false);
                } else if (i == PrivacySettingsActivity.this.webSessionsRow) {
                    textSettingsCell.setText(LocaleController.getString("WebSessionsTitle", C1067R.string.WebSessionsTitle), false);
                } else if (i == PrivacySettingsActivity.this.passwordRow) {
                    textSettingsCell.setText(LocaleController.getString("TwoStepVerification", C1067R.string.TwoStepVerification), true);
                } else if (i == PrivacySettingsActivity.this.passcodeRow) {
                    textSettingsCell.setText(LocaleController.getString("Passcode", C1067R.string.Passcode), true);
                } else {
                    String str2 = "Loading";
                    if (i == PrivacySettingsActivity.this.phoneNumberRow) {
                        if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(6)) {
                            string = LocaleController.getString(str2, C1067R.string.Loading);
                        } else {
                            string = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 6);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyPhone", C1067R.string.PrivacyPhone), string, true);
                    } else if (i == PrivacySettingsActivity.this.lastSeenRow) {
                        if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(0)) {
                            string = LocaleController.getString(str2, C1067R.string.Loading);
                        } else {
                            string = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 0);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyLastSeen", C1067R.string.PrivacyLastSeen), string, true);
                    } else if (i == PrivacySettingsActivity.this.groupsRow) {
                        if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(1)) {
                            string = LocaleController.getString(str2, C1067R.string.Loading);
                        } else {
                            string = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 1);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("GroupsAndChannels", C1067R.string.GroupsAndChannels), string, false);
                    } else if (i == PrivacySettingsActivity.this.callsRow) {
                        if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(2)) {
                            string = LocaleController.getString(str2, C1067R.string.Loading);
                        } else {
                            string = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 2);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("Calls", C1067R.string.Calls), string, true);
                    } else if (i == PrivacySettingsActivity.this.profilePhotoRow) {
                        if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(4)) {
                            string = LocaleController.getString(str2, C1067R.string.Loading);
                        } else {
                            string = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 4);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyProfilePhoto", C1067R.string.PrivacyProfilePhoto), string, true);
                    } else if (i == PrivacySettingsActivity.this.forwardsRow) {
                        if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingPrivicyInfo(5)) {
                            string = LocaleController.getString(str2, C1067R.string.Loading);
                        } else {
                            string = PrivacySettingsActivity.formatRulesString(PrivacySettingsActivity.this.currentAccount, 5);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyForwards", C1067R.string.PrivacyForwards), string, true);
                    } else if (i == PrivacySettingsActivity.this.passportRow) {
                        textSettingsCell.setText(LocaleController.getString("TelegramPassport", C1067R.string.TelegramPassport), true);
                    } else if (i == PrivacySettingsActivity.this.deleteAccountRow) {
                        if (ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingDeleteInfo()) {
                            string = LocaleController.getString(str2, C1067R.string.Loading);
                        } else {
                            i = ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getDeleteAccountTTL();
                            if (i <= 182) {
                                string = LocaleController.formatPluralString("Months", i / 30);
                            } else if (i == 365) {
                                string = LocaleController.formatPluralString("Years", i / 365);
                            } else {
                                string = LocaleController.formatPluralString("Days", i);
                            }
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("DeleteAccountIfAwayFor2", C1067R.string.DeleteAccountIfAwayFor2), string, false);
                    } else if (i == PrivacySettingsActivity.this.clearDraftsRow) {
                        textSettingsCell.setText(LocaleController.getString("PrivacyDeleteCloudDrafts", C1067R.string.PrivacyDeleteCloudDrafts), true);
                    } else if (i == PrivacySettingsActivity.this.paymentsClearRow) {
                        textSettingsCell.setText(LocaleController.getString("PrivacyPaymentsClear", C1067R.string.PrivacyPaymentsClear), true);
                    } else if (i == PrivacySettingsActivity.this.secretMapRow) {
                        i = SharedConfig.mapPreviewType;
                        if (i == 0) {
                            string = LocaleController.getString("MapPreviewProviderTelegram", C1067R.string.MapPreviewProviderTelegram);
                        } else if (i != 1) {
                            string = LocaleController.getString("MapPreviewProviderNobody", C1067R.string.MapPreviewProviderNobody);
                        } else {
                            string = LocaleController.getString("MapPreviewProviderGoogle", C1067R.string.MapPreviewProviderGoogle);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("MapPreviewProvider", C1067R.string.MapPreviewProvider), string, true);
                    } else if (i == PrivacySettingsActivity.this.contactsDeleteRow) {
                        textSettingsCell.setText(LocaleController.getString("SyncContactsDelete", C1067R.string.SyncContactsDelete), true);
                    }
                }
            } else if (itemViewType == 1) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                itemViewType = PrivacySettingsActivity.this.deleteAccountDetailRow;
                str = Theme.key_windowBackgroundGrayShadow;
                if (i == itemViewType) {
                    textInfoPrivacyCell.setText(LocaleController.getString("DeleteAccountHelp", C1067R.string.DeleteAccountHelp));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                } else if (i == PrivacySettingsActivity.this.groupsDetailRow) {
                    textInfoPrivacyCell.setText(LocaleController.getString("GroupsAndChannelsHelp", C1067R.string.GroupsAndChannelsHelp));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                } else if (i == PrivacySettingsActivity.this.sessionsDetailRow) {
                    textInfoPrivacyCell.setText(LocaleController.getString("SessionsInfo", C1067R.string.SessionsInfo));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                } else if (i == PrivacySettingsActivity.this.secretDetailRow) {
                    textInfoPrivacyCell.setText(LocaleController.getString("SecretWebPageInfo", C1067R.string.SecretWebPageInfo));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                } else if (i == PrivacySettingsActivity.this.botsDetailRow) {
                    textInfoPrivacyCell.setText(LocaleController.getString("PrivacyBotsInfo", C1067R.string.PrivacyBotsInfo));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                } else if (i == PrivacySettingsActivity.this.contactsDetailRow) {
                    textInfoPrivacyCell.setText(LocaleController.getString("SuggestContactsInfo", C1067R.string.SuggestContactsInfo));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                }
            } else if (itemViewType == 2) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == PrivacySettingsActivity.this.privacySectionRow) {
                    headerCell.setText(LocaleController.getString("PrivacyTitle", C1067R.string.PrivacyTitle));
                } else if (i == PrivacySettingsActivity.this.securitySectionRow) {
                    headerCell.setText(LocaleController.getString("SecurityTitle", C1067R.string.SecurityTitle));
                } else if (i == PrivacySettingsActivity.this.advancedSectionRow) {
                    headerCell.setText(LocaleController.getString("PrivacyAdvanced", C1067R.string.PrivacyAdvanced));
                } else if (i == PrivacySettingsActivity.this.secretSectionRow) {
                    headerCell.setText(LocaleController.getString("SecretChat", C1067R.string.SecretChat));
                } else if (i == PrivacySettingsActivity.this.botsSectionRow) {
                    headerCell.setText(LocaleController.getString("PrivacyBots", C1067R.string.PrivacyBots));
                } else if (i == PrivacySettingsActivity.this.contactsSectionRow) {
                    headerCell.setText(LocaleController.getString("Contacts", C1067R.string.Contacts));
                }
            } else if (itemViewType == 3) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i == PrivacySettingsActivity.this.secretWebpageRow) {
                    string = LocaleController.getString("SecretWebPage", C1067R.string.SecretWebPage);
                    if (MessagesController.getInstance(PrivacySettingsActivity.this.currentAccount).secretWebpagePreview != 1) {
                        z = false;
                    }
                    textCheckCell.setTextAndCheck(string, z, false);
                } else if (i == PrivacySettingsActivity.this.contactsSyncRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("SyncContacts", C1067R.string.SyncContacts), PrivacySettingsActivity.this.newSync, true);
                } else if (i == PrivacySettingsActivity.this.contactsSuggestRow) {
                    textCheckCell.setTextAndCheck(LocaleController.getString("SuggestContacts", C1067R.string.SuggestContacts), PrivacySettingsActivity.this.newSuggest, false);
                }
            }
        }

        public int getItemViewType(int i) {
            if (i == PrivacySettingsActivity.this.passportRow || i == PrivacySettingsActivity.this.lastSeenRow || i == PrivacySettingsActivity.this.phoneNumberRow || i == PrivacySettingsActivity.this.blockedRow || i == PrivacySettingsActivity.this.deleteAccountRow || i == PrivacySettingsActivity.this.sessionsRow || i == PrivacySettingsActivity.this.webSessionsRow || i == PrivacySettingsActivity.this.passwordRow || i == PrivacySettingsActivity.this.passcodeRow || i == PrivacySettingsActivity.this.groupsRow || i == PrivacySettingsActivity.this.paymentsClearRow || i == PrivacySettingsActivity.this.secretMapRow || i == PrivacySettingsActivity.this.contactsDeleteRow || i == PrivacySettingsActivity.this.clearDraftsRow) {
                return 0;
            }
            if (i == PrivacySettingsActivity.this.deleteAccountDetailRow || i == PrivacySettingsActivity.this.groupsDetailRow || i == PrivacySettingsActivity.this.sessionsDetailRow || i == PrivacySettingsActivity.this.secretDetailRow || i == PrivacySettingsActivity.this.botsDetailRow || i == PrivacySettingsActivity.this.contactsDetailRow) {
                return 1;
            }
            if (i == PrivacySettingsActivity.this.securitySectionRow || i == PrivacySettingsActivity.this.advancedSectionRow || i == PrivacySettingsActivity.this.privacySectionRow || i == PrivacySettingsActivity.this.secretSectionRow || i == PrivacySettingsActivity.this.botsSectionRow || i == PrivacySettingsActivity.this.contactsSectionRow) {
                return 2;
            }
            if (i == PrivacySettingsActivity.this.secretWebpageRow || i == PrivacySettingsActivity.this.contactsSyncRow || i == PrivacySettingsActivity.this.contactsSuggestRow) {
                return 3;
            }
            return 0;
        }
    }

    static /* synthetic */ void lambda$null$14(TLObject tLObject, TL_error tL_error) {
    }

    static /* synthetic */ void lambda$onFragmentDestroy$0(TLObject tLObject, TL_error tL_error) {
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        ContactsController.getInstance(this.currentAccount).loadPrivacySettings();
        boolean z = UserConfig.getInstance(this.currentAccount).syncContacts;
        this.newSync = z;
        this.currentSync = z;
        z = UserConfig.getInstance(this.currentAccount).suggestContacts;
        this.newSuggest = z;
        this.currentSuggest = z;
        updateRows();
        loadPasswordSettings();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
        if (this.currentSync != this.newSync) {
            UserConfig.getInstance(this.currentAccount).syncContacts = this.newSync;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (this.newSync) {
                ContactsController.getInstance(this.currentAccount).forceImportContacts();
                if (getParentActivity() != null) {
                    Toast.makeText(getParentActivity(), LocaleController.getString("SyncContactsAdded", C1067R.string.SyncContactsAdded), 0).show();
                }
            }
        }
        boolean z = this.newSuggest;
        if (z != this.currentSuggest) {
            if (!z) {
                DataQuery.getInstance(this.currentAccount).clearTopPeers();
            }
            UserConfig.getInstance(this.currentAccount).suggestContacts = this.newSuggest;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            TL_contacts_toggleTopPeers tL_contacts_toggleTopPeers = new TL_contacts_toggleTopPeers();
            tL_contacts_toggleTopPeers.enabled = this.newSuggest;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_contacts_toggleTopPeers, C3832-$$Lambda$PrivacySettingsActivity$IBy4A3n5R5n7oI7wDCjl___b3rk.INSTANCE);
        }
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("PrivacySettings", C1067R.string.PrivacySettings));
        this.actionBar.setActionBarMenuOnItemClick(new C43041());
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        RecyclerListView recyclerListView = this.listView;
        C43052 c43052 = new LinearLayoutManager(context, 1, false) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager = c43052;
        recyclerListView.setLayoutManager(c43052);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new C3829-$$Lambda$PrivacySettingsActivity$4NW4D3yhnbANs4O1uZu09FeFwHI(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$17$PrivacySettingsActivity(View view, int i) {
        if (view.isEnabled()) {
            if (i == this.blockedRow) {
                presentFragment(new PrivacyUsersActivity());
            } else {
                boolean z = false;
                if (i == this.sessionsRow) {
                    presentFragment(new SessionsActivity(0));
                } else if (i == this.webSessionsRow) {
                    presentFragment(new SessionsActivity(1));
                } else {
                    String str = "AppName";
                    String str2 = "Cancel";
                    Builder builder;
                    TextCheckCell textCheckCell;
                    if (i == this.clearDraftsRow) {
                        builder = new Builder(getParentActivity());
                        builder.setTitle(LocaleController.getString(str, C1067R.string.AppName));
                        builder.setMessage(LocaleController.getString("AreYouSureClearDrafts", C1067R.string.AreYouSureClearDrafts));
                        builder.setPositiveButton(LocaleController.getString("Delete", C1067R.string.Delete), new C1929-$$Lambda$PrivacySettingsActivity$ylpEMcd3Q-fRxhVvfyDGjrZOk0Q(this));
                        builder.setNegativeButton(LocaleController.getString(str2, C1067R.string.Cancel), null);
                        showDialog(builder.create());
                    } else if (i == this.deleteAccountRow) {
                        if (getParentActivity() != null) {
                            builder = new Builder(getParentActivity());
                            builder.setTitle(LocaleController.getString("DeleteAccountTitle", C1067R.string.DeleteAccountTitle));
                            r13 = new CharSequence[4];
                            String str3 = "Months";
                            r13[0] = LocaleController.formatPluralString(str3, 1);
                            r13[1] = LocaleController.formatPluralString(str3, 3);
                            r13[2] = LocaleController.formatPluralString(str3, 6);
                            r13[3] = LocaleController.formatPluralString("Years", 1);
                            builder.setItems(r13, new C1920-$$Lambda$PrivacySettingsActivity$Ec6kRVXNO1Ei2HcTWVQ5_P5hUGc(this));
                            builder.setNegativeButton(LocaleController.getString(str2, C1067R.string.Cancel), null);
                            showDialog(builder.create());
                        }
                    } else if (i == this.lastSeenRow) {
                        presentFragment(new PrivacyControlActivity(0));
                    } else if (i == this.phoneNumberRow) {
                        presentFragment(new PrivacyControlActivity(6));
                    } else if (i == this.groupsRow) {
                        presentFragment(new PrivacyControlActivity(1));
                    } else if (i == this.callsRow) {
                        presentFragment(new PrivacyControlActivity(2));
                    } else if (i == this.profilePhotoRow) {
                        presentFragment(new PrivacyControlActivity(4));
                    } else if (i == this.forwardsRow) {
                        presentFragment(new PrivacyControlActivity(5));
                    } else if (i == this.passwordRow) {
                        presentFragment(new TwoStepVerificationActivity(0));
                    } else if (i == this.passcodeRow) {
                        if (SharedConfig.passcodeHash.length() > 0) {
                            presentFragment(new PasscodeActivity(2));
                        } else {
                            presentFragment(new PasscodeActivity(0));
                        }
                    } else if (i == this.secretWebpageRow) {
                        if (MessagesController.getInstance(this.currentAccount).secretWebpagePreview == 1) {
                            MessagesController.getInstance(this.currentAccount).secretWebpagePreview = 0;
                        } else {
                            MessagesController.getInstance(this.currentAccount).secretWebpagePreview = 1;
                        }
                        MessagesController.getGlobalMainSettings().edit().putInt("secretWebpage2", MessagesController.getInstance(this.currentAccount).secretWebpagePreview).commit();
                        if (view instanceof TextCheckCell) {
                            textCheckCell = (TextCheckCell) view;
                            if (MessagesController.getInstance(this.currentAccount).secretWebpagePreview == 1) {
                                z = true;
                            }
                            textCheckCell.setChecked(z);
                        }
                    } else if (i == this.contactsDeleteRow) {
                        if (getParentActivity() != null) {
                            builder = new Builder(getParentActivity());
                            builder.setTitle(LocaleController.getString("Contacts", C1067R.string.Contacts));
                            builder.setMessage(LocaleController.getString("SyncContactsDeleteInfo", C1067R.string.SyncContactsDeleteInfo));
                            builder.setPositiveButton(LocaleController.getString(str2, C1067R.string.Cancel), null);
                            builder.setNegativeButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1925-$$Lambda$PrivacySettingsActivity$XTkqLHKOC0QMU4JQevFxzR8pfWg(this));
                            showDialog(builder.create());
                        }
                    } else if (i == this.contactsSuggestRow) {
                        textCheckCell = (TextCheckCell) view;
                        boolean z2 = this.newSuggest;
                        if (z2) {
                            Builder builder2 = new Builder(getParentActivity());
                            builder2.setTitle(LocaleController.getString(str, C1067R.string.AppName));
                            builder2.setMessage(LocaleController.getString("SuggestContactsAlert", C1067R.string.SuggestContactsAlert));
                            builder2.setPositiveButton(LocaleController.getString("MuteDisable", C1067R.string.MuteDisable), new C1917-$$Lambda$PrivacySettingsActivity$5uWMtQO-U72VaNWgF2qczAQ3ehw(this, textCheckCell));
                            builder2.setNegativeButton(LocaleController.getString(str2, C1067R.string.Cancel), null);
                            showDialog(builder2.create());
                        } else {
                            this.newSuggest = z2 ^ 1;
                            textCheckCell.setChecked(this.newSuggest);
                        }
                    } else if (i == this.contactsSyncRow) {
                        this.newSync ^= 1;
                        if (view instanceof TextCheckCell) {
                            ((TextCheckCell) view).setChecked(this.newSync);
                        }
                    } else if (i == this.secretMapRow) {
                        AlertsCreator.showSecretLocationAlert(getParentActivity(), this.currentAccount, new C1924-$$Lambda$PrivacySettingsActivity$UDnLY7DIuRnfm37P1EPe2aUKeCM(this), false);
                    } else if (i == this.paymentsClearRow) {
                        BottomSheet.Builder builder3 = new BottomSheet.Builder(getParentActivity());
                        builder3.setApplyTopPadding(false);
                        builder3.setApplyBottomPadding(false);
                        LinearLayout linearLayout = new LinearLayout(getParentActivity());
                        linearLayout.setOrientation(1);
                        int i2 = 0;
                        while (i2 < 2) {
                            CharSequence string = i2 == 0 ? LocaleController.getString("PrivacyClearShipping", C1067R.string.PrivacyClearShipping) : i2 == 1 ? LocaleController.getString("PrivacyClearPayment", C1067R.string.PrivacyClearPayment) : null;
                            this.clear[i2] = true;
                            CheckBoxCell checkBoxCell = new CheckBoxCell(getParentActivity(), 1, 21);
                            checkBoxCell.setTag(Integer.valueOf(i2));
                            checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            linearLayout.addView(checkBoxCell, LayoutHelper.createLinear(-1, 50));
                            checkBoxCell.setText(string, null, true, true);
                            checkBoxCell.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                            checkBoxCell.setOnClickListener(new C1926-$$Lambda$PrivacySettingsActivity$Z17JwQmal4wl3EV9bgEY12pZChE(this));
                            i2++;
                        }
                        BottomSheetCell bottomSheetCell = new BottomSheetCell(getParentActivity(), 1);
                        bottomSheetCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        bottomSheetCell.setTextAndIcon(LocaleController.getString("ClearButton", C1067R.string.ClearButton).toUpperCase(), 0);
                        bottomSheetCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText));
                        bottomSheetCell.setOnClickListener(new C1921-$$Lambda$PrivacySettingsActivity$GAEKbcOA8F2xyTKmSV5lsU1BRLk(this));
                        linearLayout.addView(bottomSheetCell, LayoutHelper.createLinear(-1, 50));
                        builder3.setCustomView(linearLayout);
                        showDialog(builder3.create());
                    } else if (i == this.passportRow) {
                        presentFragment(new PassportActivity(5, 0, "", "", null, null, null, null, null));
                    }
                }
            }
        }
    }

    public /* synthetic */ void lambda$null$3$PrivacySettingsActivity(DialogInterface dialogInterface, int i) {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_messages_clearAllDrafts(), new C3835-$$Lambda$PrivacySettingsActivity$s-IxNGqvJf4sWGFlV1kwOiRILw0(this));
    }

    public /* synthetic */ void lambda$null$1$PrivacySettingsActivity() {
        DataQuery.getInstance(this.currentAccount).clearAllDrafts();
    }

    public /* synthetic */ void lambda$null$2$PrivacySettingsActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1928-$$Lambda$PrivacySettingsActivity$f1z5m_pXWI9fMMWGfuS4tAirj68(this));
    }

    public /* synthetic */ void lambda$null$6$PrivacySettingsActivity(DialogInterface dialogInterface, int i) {
        i = i == 0 ? 30 : i == 1 ? 90 : i == 2 ? 182 : i == 3 ? 365 : 0;
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        alertDialog.setCanCacnel(false);
        alertDialog.show();
        TL_account_setAccountTTL tL_account_setAccountTTL = new TL_account_setAccountTTL();
        tL_account_setAccountTTL.ttl = new TL_accountDaysTTL();
        tL_account_setAccountTTL.ttl.days = i;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_setAccountTTL, new C3833-$$Lambda$PrivacySettingsActivity$J8W4oBcdXleAkqcvNpMiNXEJRwc(this, alertDialog, tL_account_setAccountTTL));
    }

    public /* synthetic */ void lambda$null$5$PrivacySettingsActivity(AlertDialog alertDialog, TL_account_setAccountTTL tL_account_setAccountTTL, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1923-$$Lambda$PrivacySettingsActivity$LsF3KI7j3KNxTTiwgalmqj7HqKo(this, alertDialog, tLObject, tL_account_setAccountTTL));
    }

    public /* synthetic */ void lambda$null$4$PrivacySettingsActivity(AlertDialog alertDialog, TLObject tLObject, TL_account_setAccountTTL tL_account_setAccountTTL) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (tLObject instanceof TL_boolTrue) {
            ContactsController.getInstance(this.currentAccount).setDeleteAccountTTL(tL_account_setAccountTTL.ttl.days);
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public /* synthetic */ void lambda$null$8$PrivacySettingsActivity(DialogInterface dialogInterface, int i) {
        this.progressDialog = new Builder(getParentActivity(), 3).show();
        this.progressDialog.setCanCacnel(false);
        if (this.currentSync != this.newSync) {
            UserConfig instance = UserConfig.getInstance(this.currentAccount);
            boolean z = this.newSync;
            instance.syncContacts = z;
            this.currentSync = z;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        ContactsController.getInstance(this.currentAccount).deleteAllContacts(new C1918-$$Lambda$PrivacySettingsActivity$AceH1btqmuD9dCVsGDT9w8V0zXo(this));
    }

    public /* synthetic */ void lambda$null$7$PrivacySettingsActivity() {
        this.progressDialog.dismiss();
    }

    public /* synthetic */ void lambda$null$11$PrivacySettingsActivity(TextCheckCell textCheckCell, DialogInterface dialogInterface, int i) {
        TL_payments_clearSavedInfo tL_payments_clearSavedInfo = new TL_payments_clearSavedInfo();
        boolean[] zArr = this.clear;
        tL_payments_clearSavedInfo.credentials = zArr[1];
        tL_payments_clearSavedInfo.info = zArr[0];
        UserConfig.getInstance(this.currentAccount).tmpPassword = null;
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_clearSavedInfo, new C3831-$$Lambda$PrivacySettingsActivity$9W-WSazbZdTZMbEaunliHg9yKU0(this, textCheckCell));
    }

    public /* synthetic */ void lambda$null$10$PrivacySettingsActivity(TextCheckCell textCheckCell, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1919-$$Lambda$PrivacySettingsActivity$DlGV63A9ypZdVnkR6Qd7Km-hz-I(this, textCheckCell));
    }

    public /* synthetic */ void lambda$null$9$PrivacySettingsActivity(TextCheckCell textCheckCell) {
        this.newSuggest ^= 1;
        textCheckCell.setChecked(this.newSuggest);
    }

    public /* synthetic */ void lambda$null$12$PrivacySettingsActivity() {
        this.listAdapter.notifyDataSetChanged();
    }

    public /* synthetic */ void lambda$null$13$PrivacySettingsActivity(View view) {
        CheckBoxCell checkBoxCell = (CheckBoxCell) view;
        int intValue = ((Integer) checkBoxCell.getTag()).intValue();
        boolean[] zArr = this.clear;
        zArr[intValue] = zArr[intValue] ^ 1;
        checkBoxCell.setChecked(zArr[intValue], true);
    }

    public /* synthetic */ void lambda$null$16$PrivacySettingsActivity(View view) {
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        Builder builder = new Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
        builder.setMessage(LocaleController.getString("PrivacyPaymentsClearAlert", C1067R.string.PrivacyPaymentsClearAlert));
        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1927-$$Lambda$PrivacySettingsActivity$deqj30NjdD0kcQ7DKtdHwkANODM(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$null$15$PrivacySettingsActivity(DialogInterface dialogInterface, int i) {
        TL_payments_clearSavedInfo tL_payments_clearSavedInfo = new TL_payments_clearSavedInfo();
        boolean[] zArr = this.clear;
        tL_payments_clearSavedInfo.credentials = zArr[1];
        tL_payments_clearSavedInfo.info = zArr[0];
        UserConfig.getInstance(this.currentAccount).tmpPassword = null;
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_clearSavedInfo, C3834-$$Lambda$PrivacySettingsActivity$RbXVegSlA75NxctCB1pTrsCmwsU.INSTANCE);
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.privacyRulesUpdated) {
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.privacySectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.blockedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.phoneNumberRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.lastSeenRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.profilePhotoRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.forwardsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupsDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.securitySectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.passcodeRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.passwordRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.sessionsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.sessionsDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.advancedSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.clearDraftsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.deleteAccountRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.deleteAccountDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.botsSectionRow = i;
        if (UserConfig.getInstance(this.currentAccount).hasSecureData) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.passportRow = i;
        } else {
            this.passportRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.paymentsClearRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.webSessionsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.botsDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactsDeleteRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactsSyncRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactsSuggestRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactsDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.secretSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.secretMapRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.secretWebpageRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.secretDetailRow = i;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void loadPasswordSettings() {
        if (!UserConfig.getInstance(this.currentAccount).hasSecureData) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_getPassword(), new C3830-$$Lambda$PrivacySettingsActivity$7MIQgKJpVA2M6eCuYxDI1zmVMaY(this), 10);
        }
    }

    public /* synthetic */ void lambda$loadPasswordSettings$19$PrivacySettingsActivity(TLObject tLObject, TL_error tL_error) {
        if (tLObject != null && ((TL_account_password) tLObject).has_secure_values) {
            AndroidUtilities.runOnUIThread(new C1922-$$Lambda$PrivacySettingsActivity$IncnhooBIVbvbnrTjiT1GT2RRI0(this));
        }
    }

    public /* synthetic */ void lambda$null$18$PrivacySettingsActivity() {
        UserConfig.getInstance(this.currentAccount).hasSecureData = true;
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
        updateRows();
    }

    public static String formatRulesString(int i, int i2) {
        int i3 = i2;
        ArrayList privacyRules = ContactsController.getInstance(i).getPrivacyRules(i3);
        String str = "P2PNobody";
        String str2 = "LastSeenNobody";
        if (privacyRules.size() != 0) {
            Object obj = -1;
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < privacyRules.size(); i6++) {
                PrivacyRule privacyRule = (PrivacyRule) privacyRules.get(i6);
                int size;
                if (privacyRule instanceof TL_privacyValueAllowChatParticipants) {
                    TL_privacyValueAllowChatParticipants tL_privacyValueAllowChatParticipants = (TL_privacyValueAllowChatParticipants) privacyRule;
                    size = tL_privacyValueAllowChatParticipants.chats.size();
                    int i7 = i5;
                    for (i5 = 0; i5 < size; i5++) {
                        Chat chat = MessagesController.getInstance(i).getChat((Integer) tL_privacyValueAllowChatParticipants.chats.get(i5));
                        if (chat != null) {
                            i7 += chat.participants_count;
                        }
                    }
                    i5 = i7;
                } else if (privacyRule instanceof TL_privacyValueDisallowChatParticipants) {
                    TL_privacyValueDisallowChatParticipants tL_privacyValueDisallowChatParticipants = (TL_privacyValueDisallowChatParticipants) privacyRule;
                    size = tL_privacyValueDisallowChatParticipants.chats.size();
                    for (int i8 = 0; i8 < size; i8++) {
                        Chat chat2 = MessagesController.getInstance(i).getChat((Integer) tL_privacyValueDisallowChatParticipants.chats.get(i8));
                        if (chat2 != null) {
                            i4 += chat2.participants_count;
                        }
                    }
                } else if (privacyRule instanceof TL_privacyValueAllowUsers) {
                    i5 += ((TL_privacyValueAllowUsers) privacyRule).users.size();
                } else if (privacyRule instanceof TL_privacyValueDisallowUsers) {
                    i4 += ((TL_privacyValueDisallowUsers) privacyRule).users.size();
                } else if (obj == -1) {
                    obj = privacyRule instanceof TL_privacyValueAllowAll ? null : privacyRule instanceof TL_privacyValueDisallowAll ? 1 : 2;
                }
            }
            if (obj == null || (obj == -1 && i4 > 0)) {
                if (i3 == 3) {
                    if (i4 == 0) {
                        return LocaleController.getString("P2PEverybody", C1067R.string.P2PEverybody);
                    }
                    return LocaleController.formatString("P2PEverybodyMinus", C1067R.string.P2PEverybodyMinus, Integer.valueOf(i4));
                } else if (i4 == 0) {
                    return LocaleController.getString("LastSeenEverybody", C1067R.string.LastSeenEverybody);
                } else {
                    return LocaleController.formatString("LastSeenEverybodyMinus", C1067R.string.LastSeenEverybodyMinus, Integer.valueOf(i4));
                }
            } else if (obj == 2 || (obj == -1 && i4 > 0 && i5 > 0)) {
                if (i3 == 3) {
                    if (i5 == 0 && i4 == 0) {
                        return LocaleController.getString("P2PContacts", C1067R.string.P2PContacts);
                    }
                    if (i5 != 0 && i4 != 0) {
                        return LocaleController.formatString("P2PContactsMinusPlus", C1067R.string.P2PContactsMinusPlus, Integer.valueOf(i4), Integer.valueOf(i5));
                    } else if (i4 != 0) {
                        return LocaleController.formatString("P2PContactsMinus", C1067R.string.P2PContactsMinus, Integer.valueOf(i4));
                    } else {
                        return LocaleController.formatString("P2PContactsPlus", C1067R.string.P2PContactsPlus, Integer.valueOf(i5));
                    }
                } else if (i5 == 0 && i4 == 0) {
                    return LocaleController.getString("LastSeenContacts", C1067R.string.LastSeenContacts);
                } else {
                    if (i5 != 0 && i4 != 0) {
                        return LocaleController.formatString("LastSeenContactsMinusPlus", C1067R.string.LastSeenContactsMinusPlus, Integer.valueOf(i4), Integer.valueOf(i5));
                    } else if (i4 != 0) {
                        return LocaleController.formatString("LastSeenContactsMinus", C1067R.string.LastSeenContactsMinus, Integer.valueOf(i4));
                    } else {
                        return LocaleController.formatString("LastSeenContactsPlus", C1067R.string.LastSeenContactsPlus, Integer.valueOf(i5));
                    }
                }
            } else if (obj != 1 && i5 <= 0) {
                return "unknown";
            } else {
                if (i3 == 3) {
                    if (i5 == 0) {
                        return LocaleController.getString(str, C1067R.string.P2PNobody);
                    }
                    return LocaleController.formatString("P2PNobodyPlus", C1067R.string.P2PNobodyPlus, Integer.valueOf(i5));
                } else if (i5 == 0) {
                    return LocaleController.getString(str2, C1067R.string.LastSeenNobody);
                } else {
                    return LocaleController.formatString("LastSeenNobodyPlus", C1067R.string.LastSeenNobodyPlus, Integer.valueOf(i5));
                }
            }
        } else if (i3 == 3) {
            return LocaleController.getString(str, C1067R.string.P2PNobody);
        } else {
            return LocaleController.getString(str2, C1067R.string.LastSeenNobody);
        }
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[18];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, TextCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        View view = this.listView;
        Class[] clsArr = new Class[]{TextSettingsCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        themeDescriptionArr[9] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        clsArr = new Class[]{TextSettingsCell.class};
        strArr = new String[1];
        strArr[0] = "valueTextView";
        themeDescriptionArr[10] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        view = this.listView;
        clsArr = new Class[]{TextCheckCell.class};
        strArr = new String[1];
        strArr[0] = "checkBox";
        themeDescriptionArr[16] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_switchTrack);
        themeDescriptionArr[17] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        return themeDescriptionArr;
    }
}
