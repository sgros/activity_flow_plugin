// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import org.telegram.messenger.BuildVars;
import android.content.DialogInterface;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.messenger.MessagesController;
import android.widget.TextView;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.BaseFragment;

public class DataSettingsActivity extends BaseFragment
{
    private AnimatorSet animatorSet;
    private int autoplayGifsRow;
    private int autoplayHeaderRow;
    private int autoplaySectionRow;
    private int autoplayVideoRow;
    private int callsSection2Row;
    private int callsSectionRow;
    private int dataUsageRow;
    private int enableAllStreamInfoRow;
    private int enableAllStreamRow;
    private int enableCacheStreamRow;
    private int enableMkvRow;
    private int enableStreamRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int mediaDownloadSection2Row;
    private int mediaDownloadSectionRow;
    private int mobileRow;
    private int proxyRow;
    private int proxySection2Row;
    private int proxySectionRow;
    private int quickRepliesRow;
    private int resetDownloadRow;
    private int roamingRow;
    private int rowCount;
    private int storageUsageRow;
    private int streamSectionRow;
    private int usageSection2Row;
    private int usageSectionRow;
    private int useLessDataForCallsRow;
    private int wifiRow;
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setTitle(LocaleController.getString("DataSettings", 2131559193));
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    DataSettingsActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)new _$$Lambda$DataSettingsActivity$ltmANPI19HrezwGNNfIa2VQKaYc(this));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, NotificationsCheckCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4") };
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DownloadController.getInstance(super.currentAccount).loadAutoDownloadConfig(true);
        this.rowCount = 0;
        this.usageSectionRow = this.rowCount++;
        this.storageUsageRow = this.rowCount++;
        this.dataUsageRow = this.rowCount++;
        this.usageSection2Row = this.rowCount++;
        this.mediaDownloadSectionRow = this.rowCount++;
        this.mobileRow = this.rowCount++;
        this.wifiRow = this.rowCount++;
        this.roamingRow = this.rowCount++;
        this.resetDownloadRow = this.rowCount++;
        this.mediaDownloadSection2Row = this.rowCount++;
        this.autoplayHeaderRow = this.rowCount++;
        this.autoplayGifsRow = this.rowCount++;
        this.autoplayVideoRow = this.rowCount++;
        this.autoplaySectionRow = this.rowCount++;
        this.streamSectionRow = this.rowCount++;
        this.enableStreamRow = this.rowCount++;
        if (BuildVars.DEBUG_VERSION) {
            this.enableMkvRow = this.rowCount++;
            this.enableAllStreamRow = this.rowCount++;
        }
        else {
            this.enableAllStreamRow = -1;
            this.enableMkvRow = -1;
        }
        this.enableAllStreamInfoRow = this.rowCount++;
        this.enableCacheStreamRow = -1;
        this.callsSectionRow = this.rowCount++;
        this.useLessDataForCallsRow = this.rowCount++;
        this.quickRepliesRow = this.rowCount++;
        this.callsSection2Row = this.rowCount++;
        this.proxySectionRow = this.rowCount++;
        this.proxyRow = this.rowCount++;
        this.proxySection2Row = this.rowCount++;
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
            return DataSettingsActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == DataSettingsActivity.this.mediaDownloadSection2Row || n == DataSettingsActivity.this.usageSection2Row || n == DataSettingsActivity.this.callsSection2Row || n == DataSettingsActivity.this.proxySection2Row || n == DataSettingsActivity.this.autoplaySectionRow) {
                return 0;
            }
            if (n == DataSettingsActivity.this.mediaDownloadSectionRow || n == DataSettingsActivity.this.streamSectionRow || n == DataSettingsActivity.this.callsSectionRow || n == DataSettingsActivity.this.usageSectionRow || n == DataSettingsActivity.this.proxySectionRow || n == DataSettingsActivity.this.autoplayHeaderRow) {
                return 2;
            }
            if (n == DataSettingsActivity.this.enableCacheStreamRow || n == DataSettingsActivity.this.enableStreamRow || n == DataSettingsActivity.this.enableAllStreamRow || n == DataSettingsActivity.this.enableMkvRow || n == DataSettingsActivity.this.autoplayGifsRow || n == DataSettingsActivity.this.autoplayVideoRow) {
                return 3;
            }
            if (n == DataSettingsActivity.this.enableAllStreamInfoRow) {
                return 4;
            }
            if (n != DataSettingsActivity.this.mobileRow && n != DataSettingsActivity.this.wifiRow && n != DataSettingsActivity.this.roamingRow) {
                return 1;
            }
            return 5;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return this.isRowEnabled(viewHolder.getAdapterPosition());
        }
        
        public boolean isRowEnabled(final int n) {
            final int access$600 = DataSettingsActivity.this.resetDownloadRow;
            final boolean b = false;
            boolean b2 = false;
            if (n == access$600) {
                final DownloadController instance = DownloadController.getInstance(DataSettingsActivity.this.currentAccount);
                if (!instance.lowPreset.equals(instance.getCurrentRoamingPreset()) || instance.lowPreset.isEnabled() != instance.roamingPreset.enabled || !instance.mediumPreset.equals(instance.getCurrentMobilePreset()) || instance.mediumPreset.isEnabled() != instance.mobilePreset.enabled || !instance.highPreset.equals(instance.getCurrentWiFiPreset()) || instance.highPreset.isEnabled() != instance.wifiPreset.enabled) {
                    b2 = true;
                }
                return b2;
            }
            if (n != DataSettingsActivity.this.mobileRow && n != DataSettingsActivity.this.roamingRow && n != DataSettingsActivity.this.wifiRow && n != DataSettingsActivity.this.storageUsageRow && n != DataSettingsActivity.this.useLessDataForCallsRow && n != DataSettingsActivity.this.dataUsageRow && n != DataSettingsActivity.this.proxyRow && n != DataSettingsActivity.this.enableCacheStreamRow && n != DataSettingsActivity.this.enableStreamRow && n != DataSettingsActivity.this.enableAllStreamRow && n != DataSettingsActivity.this.enableMkvRow && n != DataSettingsActivity.this.quickRepliesRow && n != DataSettingsActivity.this.autoplayVideoRow) {
                final boolean b3 = b;
                if (n != DataSettingsActivity.this.autoplayGifsRow) {
                    return b3;
                }
            }
            return true;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int int1) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                boolean b = false;
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType != 3) {
                            if (itemViewType != 4) {
                                if (itemViewType == 5) {
                                    final NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell)viewHolder.itemView;
                                    final StringBuilder sb = new StringBuilder();
                                    String s;
                                    boolean b2;
                                    DownloadController.Preset preset;
                                    if (int1 == DataSettingsActivity.this.mobileRow) {
                                        s = LocaleController.getString("WhenUsingMobileData", 2131561113);
                                        b2 = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).mobilePreset.enabled;
                                        preset = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).getCurrentMobilePreset();
                                    }
                                    else if (int1 == DataSettingsActivity.this.wifiRow) {
                                        s = LocaleController.getString("WhenConnectedOnWiFi", 2131561111);
                                        b2 = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).wifiPreset.enabled;
                                        preset = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).getCurrentWiFiPreset();
                                    }
                                    else {
                                        s = LocaleController.getString("WhenRoaming", 2131561112);
                                        b2 = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).roamingPreset.enabled;
                                        preset = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).getCurrentRoamingPreset();
                                    }
                                    int n = 0;
                                    int n2 = 0;
                                    int1 = 0;
                                    int n3 = 0;
                                    int n4 = 0;
                                    while (true) {
                                        final int[] mask = preset.mask;
                                        if (n >= mask.length) {
                                            break;
                                        }
                                        int n5 = n2;
                                        int n6 = int1;
                                        if (n2 == 0) {
                                            n5 = n2;
                                            n6 = int1;
                                            if ((mask[n] & 0x1) != 0x0) {
                                                n6 = int1 + 1;
                                                n5 = 1;
                                            }
                                        }
                                        int n7 = n6;
                                        int n8;
                                        if ((n8 = n3) == 0) {
                                            n7 = n6;
                                            n8 = n3;
                                            if ((preset.mask[n] & 0x4) != 0x0) {
                                                n7 = n6 + 1;
                                                n8 = 1;
                                            }
                                        }
                                        int1 = n7;
                                        int n9;
                                        if ((n9 = n4) == 0) {
                                            int1 = n7;
                                            n9 = n4;
                                            if ((preset.mask[n] & 0x8) != 0x0) {
                                                int1 = n7 + 1;
                                                n9 = 1;
                                            }
                                        }
                                        ++n;
                                        n2 = n5;
                                        n3 = n8;
                                        n4 = n9;
                                    }
                                    if (preset.enabled && int1 != 0) {
                                        if (n2 != 0) {
                                            sb.append(LocaleController.getString("AutoDownloadPhotosOn", 2131558766));
                                        }
                                        if (n3 != 0) {
                                            if (sb.length() > 0) {
                                                sb.append(", ");
                                            }
                                            sb.append(LocaleController.getString("AutoDownloadVideosOn", 2131558776));
                                            sb.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize(preset.sizes[DownloadController.typeToIndex(4)], true)));
                                        }
                                        if (n4 != 0) {
                                            if (sb.length() > 0) {
                                                sb.append(", ");
                                            }
                                            sb.append(LocaleController.getString("AutoDownloadFilesOn", 2131558746));
                                            sb.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize(preset.sizes[DownloadController.typeToIndex(8)], true)));
                                        }
                                    }
                                    else {
                                        sb.append(LocaleController.getString("NoMediaAutoDownload", 2131559930));
                                    }
                                    notificationsCheckCell.setTextAndValueAndCheck(s, sb, (n2 != 0 || n3 != 0 || n4 != 0) && b2, 0, true, true);
                                }
                            }
                            else {
                                final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                                if (int1 == DataSettingsActivity.this.enableAllStreamInfoRow) {
                                    textInfoPrivacyCell.setText(LocaleController.getString("EnableAllStreamingInfo", 2131559347));
                                }
                            }
                        }
                        else {
                            final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                            if (int1 == DataSettingsActivity.this.enableStreamRow) {
                                final String string = LocaleController.getString("EnableStreaming", 2131559349);
                                final boolean streamMedia = SharedConfig.streamMedia;
                                if (DataSettingsActivity.this.enableAllStreamRow != -1) {
                                    b = true;
                                }
                                textCheckCell.setTextAndCheck(string, streamMedia, b);
                            }
                            else if (int1 != DataSettingsActivity.this.enableCacheStreamRow) {
                                if (int1 == DataSettingsActivity.this.enableMkvRow) {
                                    textCheckCell.setTextAndCheck("(beta only) Show MKV as Video", SharedConfig.streamMkv, true);
                                }
                                else if (int1 == DataSettingsActivity.this.enableAllStreamRow) {
                                    textCheckCell.setTextAndCheck("(beta only) Stream All Videos", SharedConfig.streamAllVideo, false);
                                }
                                else if (int1 == DataSettingsActivity.this.autoplayGifsRow) {
                                    textCheckCell.setTextAndCheck(LocaleController.getString("AutoplayGIF", 2131558803), SharedConfig.autoplayGifs, true);
                                }
                                else if (int1 == DataSettingsActivity.this.autoplayVideoRow) {
                                    textCheckCell.setTextAndCheck(LocaleController.getString("AutoplayVideo", 2131558805), SharedConfig.autoplayVideo, false);
                                }
                            }
                        }
                    }
                    else {
                        final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                        if (int1 == DataSettingsActivity.this.mediaDownloadSectionRow) {
                            headerCell.setText(LocaleController.getString("AutomaticMediaDownload", 2131558802));
                        }
                        else if (int1 == DataSettingsActivity.this.usageSectionRow) {
                            headerCell.setText(LocaleController.getString("DataUsage", 2131559194));
                        }
                        else if (int1 == DataSettingsActivity.this.callsSectionRow) {
                            headerCell.setText(LocaleController.getString("Calls", 2131558888));
                        }
                        else if (int1 == DataSettingsActivity.this.proxySectionRow) {
                            headerCell.setText(LocaleController.getString("Proxy", 2131560516));
                        }
                        else if (int1 == DataSettingsActivity.this.streamSectionRow) {
                            headerCell.setText(LocaleController.getString("Streaming", 2131560833));
                        }
                        else if (int1 == DataSettingsActivity.this.autoplayHeaderRow) {
                            headerCell.setText(LocaleController.getString("AutoplayMedia", 2131558804));
                        }
                    }
                }
                else {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    textSettingsCell.setCanDisable(false);
                    textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    if (int1 == DataSettingsActivity.this.storageUsageRow) {
                        textSettingsCell.setText(LocaleController.getString("StorageUsage", 2131560832), true);
                    }
                    else if (int1 == DataSettingsActivity.this.useLessDataForCallsRow) {
                        final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                        String s2 = null;
                        int1 = globalMainSettings.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
                        if (int1 != 0) {
                            if (int1 != 1) {
                                if (int1 != 2) {
                                    if (int1 == 3) {
                                        s2 = LocaleController.getString("UseLessDataOnRoaming", 2131560970);
                                    }
                                }
                                else {
                                    s2 = LocaleController.getString("UseLessDataAlways", 2131560967);
                                }
                            }
                            else {
                                s2 = LocaleController.getString("UseLessDataOnMobile", 2131560969);
                            }
                        }
                        else {
                            s2 = LocaleController.getString("UseLessDataNever", 2131560968);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("VoipUseLessData", 2131561093), s2, true);
                    }
                    else if (int1 == DataSettingsActivity.this.dataUsageRow) {
                        textSettingsCell.setText(LocaleController.getString("NetworkUsage", 2131559889), false);
                    }
                    else if (int1 == DataSettingsActivity.this.proxyRow) {
                        textSettingsCell.setText(LocaleController.getString("ProxySettings", 2131560519), false);
                    }
                    else if (int1 == DataSettingsActivity.this.resetDownloadRow) {
                        textSettingsCell.setCanDisable(true);
                        textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText"));
                        textSettingsCell.setText(LocaleController.getString("ResetAutomaticMediaDownload", 2131560590), false);
                    }
                    else if (int1 == DataSettingsActivity.this.quickRepliesRow) {
                        textSettingsCell.setText(LocaleController.getString("VoipQuickReplies", 2131561086), false);
                    }
                }
            }
            else if (int1 == DataSettingsActivity.this.proxySection2Row) {
                viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            }
            else {
                viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
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
                                    o = null;
                                }
                                else {
                                    o = new NotificationsCheckCell(this.mContext);
                                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                }
                            }
                            else {
                                o = new TextInfoPrivacyCell(this.mContext);
                                ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                            }
                        }
                        else {
                            o = new TextCheckCell(this.mContext);
                            ((TextCheckCell)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
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
                o = new ShadowSectionCell(this.mContext);
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewAttachedToWindow(final ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 3) {
                final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                final int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition == DataSettingsActivity.this.enableCacheStreamRow) {
                    textCheckCell.setChecked(SharedConfig.saveStreamMedia);
                }
                else if (adapterPosition == DataSettingsActivity.this.enableStreamRow) {
                    textCheckCell.setChecked(SharedConfig.streamMedia);
                }
                else if (adapterPosition == DataSettingsActivity.this.enableAllStreamRow) {
                    textCheckCell.setChecked(SharedConfig.streamAllVideo);
                }
                else if (adapterPosition == DataSettingsActivity.this.enableMkvRow) {
                    textCheckCell.setChecked(SharedConfig.streamMkv);
                }
                else if (adapterPosition == DataSettingsActivity.this.autoplayGifsRow) {
                    textCheckCell.setChecked(SharedConfig.autoplayGifs);
                }
                else if (adapterPosition == DataSettingsActivity.this.autoplayVideoRow) {
                    textCheckCell.setChecked(SharedConfig.autoplayVideo);
                }
            }
        }
    }
}
