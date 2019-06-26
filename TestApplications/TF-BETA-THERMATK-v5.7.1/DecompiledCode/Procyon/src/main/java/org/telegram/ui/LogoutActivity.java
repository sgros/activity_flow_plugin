// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.MessagesController;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.app.Dialog;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import org.telegram.ui.Components.RecyclerListView;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.BaseFragment;

public class LogoutActivity extends BaseFragment
{
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
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setTitle(LocaleController.getString("LogOutTitle", 2131559785));
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    LogoutActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)new _$$Lambda$LogoutActivity$m1rAFJ32lHZiphjBl2dyKZG7_Z0(this));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, HeaderCell.class, TextDetailSettingsCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText5"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon") };
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.rowCount = 0;
        this.alternativeHeaderRow = this.rowCount++;
        if (UserConfig.getActivatedAccountsCount() < 3) {
            this.addAccountRow = this.rowCount++;
        }
        else {
            this.addAccountRow = -1;
        }
        if (SharedConfig.passcodeHash.length() <= 0) {
            this.passcodeRow = this.rowCount++;
        }
        else {
            this.passcodeRow = -1;
        }
        this.cacheRow = this.rowCount++;
        this.phoneRow = this.rowCount++;
        this.supportRow = this.rowCount++;
        this.alternativeSectionRow = this.rowCount++;
        this.logoutRow = this.rowCount++;
        this.logoutSectionRow = this.rowCount++;
        return true;
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
            return LogoutActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == LogoutActivity.this.alternativeHeaderRow) {
                return 0;
            }
            if (n == LogoutActivity.this.addAccountRow || n == LogoutActivity.this.passcodeRow || n == LogoutActivity.this.cacheRow || n == LogoutActivity.this.phoneRow || n == LogoutActivity.this.supportRow) {
                return 1;
            }
            if (n == LogoutActivity.this.alternativeSectionRow) {
                return 2;
            }
            if (n == LogoutActivity.this.logoutRow) {
                return 3;
            }
            return 4;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == LogoutActivity.this.addAccountRow || adapterPosition == LogoutActivity.this.passcodeRow || adapterPosition == LogoutActivity.this.cacheRow || adapterPosition == LogoutActivity.this.phoneRow || adapterPosition == LogoutActivity.this.supportRow || adapterPosition == LogoutActivity.this.logoutRow;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 3) {
                        if (itemViewType == 4) {
                            final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                            if (n == LogoutActivity.this.logoutSectionRow) {
                                textInfoPrivacyCell.setText(LocaleController.getString("LogOutInfo", 2131559784));
                            }
                        }
                    }
                    else {
                        final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                        if (n == LogoutActivity.this.logoutRow) {
                            textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
                            textSettingsCell.setText(LocaleController.getString("LogOutTitle", 2131559785), false);
                        }
                    }
                }
                else {
                    final TextDetailSettingsCell textDetailSettingsCell = (TextDetailSettingsCell)viewHolder.itemView;
                    if (n == LogoutActivity.this.addAccountRow) {
                        textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("AddAnotherAccount", 2131558562), LocaleController.getString("AddAnotherAccountInfo", 2131558563), 2131165272, true);
                    }
                    else if (n == LogoutActivity.this.passcodeRow) {
                        textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("SetPasscode", 2131560734), LocaleController.getString("SetPasscodeInfo", 2131560735), 2131165590, true);
                    }
                    else if (n == LogoutActivity.this.cacheRow) {
                        textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("ClearCache", 2131559103), LocaleController.getString("ClearCacheInfo", 2131559105), 2131165575, true);
                    }
                    else if (n == LogoutActivity.this.phoneRow) {
                        textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("ChangePhoneNumber", 2131558913), LocaleController.getString("ChangePhoneNumberInfo", 2131558914), 2131165587, true);
                    }
                    else if (n == LogoutActivity.this.supportRow) {
                        textDetailSettingsCell.setTextAndValueAndIcon(LocaleController.getString("ContactSupport", 2131559147), LocaleController.getString("ContactSupportInfo", 2131559148), 2131165600, false);
                    }
                }
            }
            else {
                final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                if (n == LogoutActivity.this.alternativeHeaderRow) {
                    headerCell.setText(LocaleController.getString("AlternativeOptions", 2131558610));
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
                            o = new TextInfoPrivacyCell(this.mContext);
                            ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                        }
                        else {
                            o = new TextSettingsCell(this.mContext);
                            ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                    }
                    else {
                        o = new ShadowSectionCell(this.mContext);
                    }
                }
                else {
                    o = new TextDetailSettingsCell(this.mContext);
                    ((TextDetailSettingsCell)o).setMultilineDetail(true);
                    ((FrameLayout)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
            }
            else {
                o = new HeaderCell(this.mContext);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
    }
}
