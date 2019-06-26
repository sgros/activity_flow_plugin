package org.telegram.p004ui;

import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.ShadowSectionCell;
import org.telegram.p004ui.Cells.TextDetailSettingsCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;

/* renamed from: org.telegram.ui.LogoutActivity */
public class LogoutActivity extends BaseFragment {
    private int addAccountRow;
    private int alternativeHeaderRow;
    private int alternativeSectionRow;
    private AnimatorSet animatorSet;
    private int cacheRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int logoutRow;
    private int logoutSectionRow;
    private int passcodeRow;
    private int phoneRow;
    private int rowCount;
    private int supportRow;

    /* renamed from: org.telegram.ui.LogoutActivity$1 */
    class C42241 extends ActionBarMenuOnItemClick {
        C42241() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                LogoutActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.LogoutActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return LogoutActivity.this.rowCount;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == LogoutActivity.this.alternativeHeaderRow) {
                    headerCell.setText(LocaleController.getString("AlternativeOptions", C1067R.string.AlternativeOptions));
                }
            } else if (itemViewType == 1) {
                TextDetailSettingsCell textDetailSettingsCell = (TextDetailSettingsCell) viewHolder.itemView;
                if (i == LogoutActivity.this.addAccountRow) {
                    textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("AddAnotherAccount", C1067R.string.AddAnotherAccount), LocaleController.getString("AddAnotherAccountInfo", C1067R.string.AddAnotherAccountInfo), C1067R.C1065drawable.actions_addmember2, true);
                } else if (i == LogoutActivity.this.passcodeRow) {
                    textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("SetPasscode", C1067R.string.SetPasscode), LocaleController.getString("SetPasscodeInfo", C1067R.string.SetPasscodeInfo), C1067R.C1065drawable.menu_passcode, true);
                } else if (i == LogoutActivity.this.cacheRow) {
                    textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("ClearCache", C1067R.string.ClearCache), LocaleController.getString("ClearCacheInfo", C1067R.string.ClearCacheInfo), C1067R.C1065drawable.menu_clearcache, true);
                } else if (i == LogoutActivity.this.phoneRow) {
                    textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("ChangePhoneNumber", C1067R.string.ChangePhoneNumber), LocaleController.getString("ChangePhoneNumberInfo", C1067R.string.ChangePhoneNumberInfo), C1067R.C1065drawable.menu_newphone, true);
                } else if (i == LogoutActivity.this.supportRow) {
                    textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("ContactSupport", C1067R.string.ContactSupport), LocaleController.getString("ContactSupportInfo", C1067R.string.ContactSupportInfo), C1067R.C1065drawable.menu_support, false);
                }
            } else if (itemViewType == 3) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (i == LogoutActivity.this.logoutRow) {
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText5));
                    textSettingsCell.setText(LocaleController.getString("LogOutTitle", C1067R.string.LogOutTitle), false);
                }
            } else if (itemViewType == 4) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i == LogoutActivity.this.logoutSectionRow) {
                    textInfoPrivacyCell.setText(LocaleController.getString("LogOutInfo", C1067R.string.LogOutInfo));
                }
            }
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == LogoutActivity.this.addAccountRow || adapterPosition == LogoutActivity.this.passcodeRow || adapterPosition == LogoutActivity.this.cacheRow || adapterPosition == LogoutActivity.this.phoneRow || adapterPosition == LogoutActivity.this.supportRow || adapterPosition == LogoutActivity.this.logoutRow;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View headerCell;
            String str = Theme.key_windowBackgroundWhite;
            if (i == 0) {
                headerCell = new HeaderCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor(str));
            } else if (i != 1) {
                View shadowSectionCell;
                if (i == 2) {
                    shadowSectionCell = new ShadowSectionCell(this.mContext);
                } else if (i != 3) {
                    shadowSectionCell = new TextInfoPrivacyCell(this.mContext);
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                } else {
                    headerCell = new TextSettingsCell(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(str));
                }
                headerCell = shadowSectionCell;
            } else {
                headerCell = new TextDetailSettingsCell(this.mContext);
                headerCell.setMultilineDetail(true);
                headerCell.setBackgroundColor(Theme.getColor(str));
            }
            headerCell.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(headerCell);
        }

        public int getItemViewType(int i) {
            if (i == LogoutActivity.this.alternativeHeaderRow) {
                return 0;
            }
            if (i == LogoutActivity.this.addAccountRow || i == LogoutActivity.this.passcodeRow || i == LogoutActivity.this.cacheRow || i == LogoutActivity.this.phoneRow || i == LogoutActivity.this.supportRow) {
                return 1;
            }
            if (i == LogoutActivity.this.alternativeSectionRow) {
                return 2;
            }
            return i == LogoutActivity.this.logoutRow ? 3 : 4;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.alternativeHeaderRow = i;
        if (UserConfig.getActivatedAccountsCount() < 3) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.addAccountRow = i;
        } else {
            this.addAccountRow = -1;
        }
        if (SharedConfig.passcodeHash.length() <= 0) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.passcodeRow = i;
        } else {
            this.passcodeRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.cacheRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.phoneRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.supportRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.alternativeSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.logoutRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.logoutSectionRow = i;
        return true;
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString("LogOutTitle", C1067R.string.LogOutTitle));
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new C42241());
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.listView = new RecyclerListView(context);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new C3747-$$Lambda$LogoutActivity$m1rAFJ32lHZiphjBl2dyKZG7_Z0(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$1$LogoutActivity(View view, int i, float f, float f2) {
        int i2 = 0;
        if (i == this.addAccountRow) {
            int i3 = -1;
            while (i2 < 3) {
                if (!UserConfig.getInstance(i2).isClientActivated()) {
                    i3 = i2;
                    break;
                }
                i2++;
            }
            if (i3 >= 0) {
                presentFragment(new LoginActivity(i3));
            }
        } else if (i == this.passcodeRow) {
            presentFragment(new PasscodeActivity(0));
        } else if (i == this.cacheRow) {
            presentFragment(new CacheControlActivity());
        } else if (i == this.phoneRow) {
            presentFragment(new ChangePhoneHelpActivity());
        } else if (i == this.supportRow) {
            showDialog(AlertsCreator.createSupportAlert(this));
        } else if (i == this.logoutRow && getParentActivity() != null) {
            Builder builder = new Builder(getParentActivity());
            builder.setMessage(LocaleController.getString("AreYouSureLogout", C1067R.string.AreYouSureLogout));
            builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1661-$$Lambda$LogoutActivity$j4TP2hjvfLt3Pmf-Vc0G6Kn6GSI(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
            showDialog(builder.create());
        }
    }

    public /* synthetic */ void lambda$null$0$LogoutActivity(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(1);
    }

    /* Access modifiers changed, original: protected */
    public void onDialogDismiss(Dialog dialog) {
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[17];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, TextDetailSettingsCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        View view = this.listView;
        Class[] clsArr = new Class[]{TextSettingsCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        themeDescriptionArr[10] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteRedText5);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[16] = new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        return themeDescriptionArr;
    }
}
