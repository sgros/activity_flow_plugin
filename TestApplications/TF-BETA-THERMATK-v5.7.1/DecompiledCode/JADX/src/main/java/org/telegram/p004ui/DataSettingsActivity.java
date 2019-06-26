package org.telegram.p004ui;

import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.DownloadController.Preset;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.NotificationsCheckCell;
import org.telegram.p004ui.Cells.ShadowSectionCell;
import org.telegram.p004ui.Cells.TextCheckCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.p004ui.Components.voip.VoIPHelper;

/* renamed from: org.telegram.ui.DataSettingsActivity */
public class DataSettingsActivity extends BaseFragment {
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

    /* renamed from: org.telegram.ui.DataSettingsActivity$1 */
    class C41651 extends ActionBarMenuOnItemClick {
        C41651() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                DataSettingsActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.DataSettingsActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return DataSettingsActivity.this.rowCount;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            ViewHolder viewHolder2 = viewHolder;
            int i2 = i;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                boolean z = false;
                if (itemViewType == 1) {
                    TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder2.itemView;
                    textSettingsCell.setCanDisable(false);
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (i2 == DataSettingsActivity.this.storageUsageRow) {
                        textSettingsCell.setText(LocaleController.getString("StorageUsage", C1067R.string.StorageUsage), true);
                        return;
                    } else if (i2 == DataSettingsActivity.this.useLessDataForCallsRow) {
                        String str = null;
                        i2 = MessagesController.getGlobalMainSettings().getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
                        if (i2 == 0) {
                            str = LocaleController.getString("UseLessDataNever", C1067R.string.UseLessDataNever);
                        } else if (i2 == 1) {
                            str = LocaleController.getString("UseLessDataOnMobile", C1067R.string.UseLessDataOnMobile);
                        } else if (i2 == 2) {
                            str = LocaleController.getString("UseLessDataAlways", C1067R.string.UseLessDataAlways);
                        } else if (i2 == 3) {
                            str = LocaleController.getString("UseLessDataOnRoaming", C1067R.string.UseLessDataOnRoaming);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("VoipUseLessData", C1067R.string.VoipUseLessData), str, true);
                        return;
                    } else if (i2 == DataSettingsActivity.this.dataUsageRow) {
                        textSettingsCell.setText(LocaleController.getString("NetworkUsage", C1067R.string.NetworkUsage), false);
                        return;
                    } else if (i2 == DataSettingsActivity.this.proxyRow) {
                        textSettingsCell.setText(LocaleController.getString("ProxySettings", C1067R.string.ProxySettings), false);
                        return;
                    } else if (i2 == DataSettingsActivity.this.resetDownloadRow) {
                        textSettingsCell.setCanDisable(true);
                        textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText));
                        textSettingsCell.setText(LocaleController.getString("ResetAutomaticMediaDownload", C1067R.string.ResetAutomaticMediaDownload), false);
                        return;
                    } else if (i2 == DataSettingsActivity.this.quickRepliesRow) {
                        textSettingsCell.setText(LocaleController.getString("VoipQuickReplies", C1067R.string.VoipQuickReplies), false);
                        return;
                    } else {
                        return;
                    }
                } else if (itemViewType == 2) {
                    HeaderCell headerCell = (HeaderCell) viewHolder2.itemView;
                    if (i2 == DataSettingsActivity.this.mediaDownloadSectionRow) {
                        headerCell.setText(LocaleController.getString("AutomaticMediaDownload", C1067R.string.AutomaticMediaDownload));
                        return;
                    } else if (i2 == DataSettingsActivity.this.usageSectionRow) {
                        headerCell.setText(LocaleController.getString("DataUsage", C1067R.string.DataUsage));
                        return;
                    } else if (i2 == DataSettingsActivity.this.callsSectionRow) {
                        headerCell.setText(LocaleController.getString("Calls", C1067R.string.Calls));
                        return;
                    } else if (i2 == DataSettingsActivity.this.proxySectionRow) {
                        headerCell.setText(LocaleController.getString("Proxy", C1067R.string.Proxy));
                        return;
                    } else if (i2 == DataSettingsActivity.this.streamSectionRow) {
                        headerCell.setText(LocaleController.getString("Streaming", C1067R.string.Streaming));
                        return;
                    } else if (i2 == DataSettingsActivity.this.autoplayHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoplayMedia", C1067R.string.AutoplayMedia));
                        return;
                    } else {
                        return;
                    }
                } else if (itemViewType == 3) {
                    TextCheckCell textCheckCell = (TextCheckCell) viewHolder2.itemView;
                    if (i2 == DataSettingsActivity.this.enableStreamRow) {
                        String string = LocaleController.getString("EnableStreaming", C1067R.string.EnableStreaming);
                        boolean z2 = SharedConfig.streamMedia;
                        if (DataSettingsActivity.this.enableAllStreamRow != -1) {
                            z = true;
                        }
                        textCheckCell.setTextAndCheck(string, z2, z);
                        return;
                    } else if (i2 != DataSettingsActivity.this.enableCacheStreamRow) {
                        if (i2 == DataSettingsActivity.this.enableMkvRow) {
                            textCheckCell.setTextAndCheck("(beta only) Show MKV as Video", SharedConfig.streamMkv, true);
                            return;
                        } else if (i2 == DataSettingsActivity.this.enableAllStreamRow) {
                            textCheckCell.setTextAndCheck("(beta only) Stream All Videos", SharedConfig.streamAllVideo, false);
                            return;
                        } else if (i2 == DataSettingsActivity.this.autoplayGifsRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("AutoplayGIF", C1067R.string.AutoplayGIF), SharedConfig.autoplayGifs, true);
                            return;
                        } else if (i2 == DataSettingsActivity.this.autoplayVideoRow) {
                            textCheckCell.setTextAndCheck(LocaleController.getString("AutoplayVideo", C1067R.string.AutoplayVideo), SharedConfig.autoplayVideo, false);
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (itemViewType == 4) {
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder2.itemView;
                    if (i2 == DataSettingsActivity.this.enableAllStreamInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("EnableAllStreamingInfo", C1067R.string.EnableAllStreamingInfo));
                        return;
                    }
                    return;
                } else if (itemViewType == 5) {
                    String string2;
                    boolean z3;
                    Preset currentMobilePreset;
                    NotificationsCheckCell notificationsCheckCell;
                    String str2;
                    NotificationsCheckCell notificationsCheckCell2 = (NotificationsCheckCell) viewHolder2.itemView;
                    StringBuilder stringBuilder = new StringBuilder();
                    if (i2 == DataSettingsActivity.this.mobileRow) {
                        string2 = LocaleController.getString("WhenUsingMobileData", C1067R.string.WhenUsingMobileData);
                        z3 = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).mobilePreset.enabled;
                        currentMobilePreset = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).getCurrentMobilePreset();
                    } else if (i2 == DataSettingsActivity.this.wifiRow) {
                        string2 = LocaleController.getString("WhenConnectedOnWiFi", C1067R.string.WhenConnectedOnWiFi);
                        z3 = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).wifiPreset.enabled;
                        currentMobilePreset = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).getCurrentWiFiPreset();
                    } else {
                        string2 = LocaleController.getString("WhenRoaming", C1067R.string.WhenRoaming);
                        z3 = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).roamingPreset.enabled;
                        currentMobilePreset = DownloadController.getInstance(DataSettingsActivity.this.currentAccount).getCurrentRoamingPreset();
                    }
                    String str3 = string2;
                    int i3 = 0;
                    Object obj = null;
                    int i4 = 0;
                    Object obj2 = null;
                    Object obj3 = null;
                    while (true) {
                        int[] iArr = currentMobilePreset.mask;
                        if (i3 >= iArr.length) {
                            break;
                        }
                        if (obj == null && (iArr[i3] & 1) != 0) {
                            i4++;
                            obj = 1;
                        }
                        if (obj2 == null && (currentMobilePreset.mask[i3] & 4) != 0) {
                            i4++;
                            obj2 = 1;
                        }
                        if (obj3 == null && (currentMobilePreset.mask[i3] & 8) != 0) {
                            i4++;
                            obj3 = 1;
                        }
                        i3++;
                    }
                    if (!currentMobilePreset.enabled || i4 == 0) {
                        notificationsCheckCell = notificationsCheckCell2;
                        str2 = str3;
                        stringBuilder.append(LocaleController.getString("NoMediaAutoDownload", C1067R.string.NoMediaAutoDownload));
                    } else {
                        if (obj != null) {
                            stringBuilder.append(LocaleController.getString("AutoDownloadPhotosOn", C1067R.string.AutoDownloadPhotosOn));
                        }
                        string2 = ", ";
                        String str4 = " (%1$s)";
                        if (obj2 != null) {
                            if (stringBuilder.length() > 0) {
                                stringBuilder.append(string2);
                            }
                            stringBuilder.append(LocaleController.getString("AutoDownloadVideosOn", C1067R.string.AutoDownloadVideosOn));
                            Object[] objArr = new Object[1];
                            notificationsCheckCell = notificationsCheckCell2;
                            str2 = str3;
                            objArr[0] = AndroidUtilities.formatFileSize((long) currentMobilePreset.sizes[DownloadController.typeToIndex(4)], true);
                            stringBuilder.append(String.format(str4, objArr));
                        } else {
                            notificationsCheckCell = notificationsCheckCell2;
                            str2 = str3;
                        }
                        if (obj3 != null) {
                            if (stringBuilder.length() > 0) {
                                stringBuilder.append(string2);
                            }
                            stringBuilder.append(LocaleController.getString("AutoDownloadFilesOn", C1067R.string.AutoDownloadFilesOn));
                            stringBuilder.append(String.format(str4, new Object[]{AndroidUtilities.formatFileSize((long) currentMobilePreset.sizes[DownloadController.typeToIndex(8)], true)}));
                        }
                    }
                    boolean z4 = !(obj == null && obj2 == null && obj3 == null) && z3;
                    notificationsCheckCell.setTextAndValueAndCheck(str2, stringBuilder, z4, 0, true, true);
                    return;
                } else {
                    return;
                }
            }
            itemViewType = DataSettingsActivity.this.proxySection2Row;
            String str5 = Theme.key_windowBackgroundGrayShadow;
            if (i2 == itemViewType) {
                viewHolder2.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, str5));
            } else {
                viewHolder2.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str5));
            }
        }

        public void onViewAttachedToWindow(ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 3) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition == DataSettingsActivity.this.enableCacheStreamRow) {
                    textCheckCell.setChecked(SharedConfig.saveStreamMedia);
                } else if (adapterPosition == DataSettingsActivity.this.enableStreamRow) {
                    textCheckCell.setChecked(SharedConfig.streamMedia);
                } else if (adapterPosition == DataSettingsActivity.this.enableAllStreamRow) {
                    textCheckCell.setChecked(SharedConfig.streamAllVideo);
                } else if (adapterPosition == DataSettingsActivity.this.enableMkvRow) {
                    textCheckCell.setChecked(SharedConfig.streamMkv);
                } else if (adapterPosition == DataSettingsActivity.this.autoplayGifsRow) {
                    textCheckCell.setChecked(SharedConfig.autoplayGifs);
                } else if (adapterPosition == DataSettingsActivity.this.autoplayVideoRow) {
                    textCheckCell.setChecked(SharedConfig.autoplayVideo);
                }
            }
        }

        public boolean isRowEnabled(int i) {
            boolean z = false;
            if (i == DataSettingsActivity.this.resetDownloadRow) {
                DownloadController instance = DownloadController.getInstance(DataSettingsActivity.this.currentAccount);
                if (!(instance.lowPreset.equals(instance.getCurrentRoamingPreset()) && instance.lowPreset.isEnabled() == instance.roamingPreset.enabled && instance.mediumPreset.equals(instance.getCurrentMobilePreset()) && instance.mediumPreset.isEnabled() == instance.mobilePreset.enabled && instance.highPreset.equals(instance.getCurrentWiFiPreset()) && instance.highPreset.isEnabled() == instance.wifiPreset.enabled)) {
                    z = true;
                }
                return z;
            }
            if (i == DataSettingsActivity.this.mobileRow || i == DataSettingsActivity.this.roamingRow || i == DataSettingsActivity.this.wifiRow || i == DataSettingsActivity.this.storageUsageRow || i == DataSettingsActivity.this.useLessDataForCallsRow || i == DataSettingsActivity.this.dataUsageRow || i == DataSettingsActivity.this.proxyRow || i == DataSettingsActivity.this.enableCacheStreamRow || i == DataSettingsActivity.this.enableStreamRow || i == DataSettingsActivity.this.enableAllStreamRow || i == DataSettingsActivity.this.enableMkvRow || i == DataSettingsActivity.this.quickRepliesRow || i == DataSettingsActivity.this.autoplayVideoRow || i == DataSettingsActivity.this.autoplayGifsRow) {
                z = true;
            }
            return z;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return isRowEnabled(viewHolder.getAdapterPosition());
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textSettingsCell;
            if (i != 0) {
                String str = Theme.key_windowBackgroundWhite;
                if (i == 1) {
                    textSettingsCell = new TextSettingsCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                } else if (i == 2) {
                    textSettingsCell = new HeaderCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                } else if (i == 3) {
                    textSettingsCell = new TextCheckCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                } else if (i == 4) {
                    textSettingsCell = new TextInfoPrivacyCell(this.mContext);
                    textSettingsCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                } else if (i != 5) {
                    textSettingsCell = null;
                } else {
                    textSettingsCell = new NotificationsCheckCell(this.mContext);
                    textSettingsCell.setBackgroundColor(Theme.getColor(str));
                }
            } else {
                textSettingsCell = new ShadowSectionCell(this.mContext);
            }
            textSettingsCell.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(textSettingsCell);
        }

        public int getItemViewType(int i) {
            if (i == DataSettingsActivity.this.mediaDownloadSection2Row || i == DataSettingsActivity.this.usageSection2Row || i == DataSettingsActivity.this.callsSection2Row || i == DataSettingsActivity.this.proxySection2Row || i == DataSettingsActivity.this.autoplaySectionRow) {
                return 0;
            }
            if (i == DataSettingsActivity.this.mediaDownloadSectionRow || i == DataSettingsActivity.this.streamSectionRow || i == DataSettingsActivity.this.callsSectionRow || i == DataSettingsActivity.this.usageSectionRow || i == DataSettingsActivity.this.proxySectionRow || i == DataSettingsActivity.this.autoplayHeaderRow) {
                return 2;
            }
            if (i == DataSettingsActivity.this.enableCacheStreamRow || i == DataSettingsActivity.this.enableStreamRow || i == DataSettingsActivity.this.enableAllStreamRow || i == DataSettingsActivity.this.enableMkvRow || i == DataSettingsActivity.this.autoplayGifsRow || i == DataSettingsActivity.this.autoplayVideoRow) {
                return 3;
            }
            if (i == DataSettingsActivity.this.enableAllStreamInfoRow) {
                return 4;
            }
            return (i == DataSettingsActivity.this.mobileRow || i == DataSettingsActivity.this.wifiRow || i == DataSettingsActivity.this.roamingRow) ? 5 : 1;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DownloadController.getInstance(this.currentAccount).loadAutoDownloadConfig(true);
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.usageSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.storageUsageRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.dataUsageRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.usageSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.mediaDownloadSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.mobileRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.wifiRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.roamingRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.resetDownloadRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.mediaDownloadSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.autoplayHeaderRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.autoplayGifsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.autoplayVideoRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.autoplaySectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.streamSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.enableStreamRow = i;
        if (BuildVars.DEBUG_VERSION) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.enableMkvRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.enableAllStreamRow = i;
        } else {
            this.enableAllStreamRow = -1;
            this.enableMkvRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.enableAllStreamInfoRow = i;
        this.enableCacheStreamRow = -1;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.useLessDataForCallsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.quickRepliesRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.proxySectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.proxyRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.proxySection2Row = i;
        return true;
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString("DataSettings", C1067R.string.DataSettings));
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new C41651());
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.listView = new RecyclerListView(context);
        this.listView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new C3678-$$Lambda$DataSettingsActivity$ltmANPI19HrezwGNNfIa2VQKaYc(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$2$DataSettingsActivity(View view, int i, float f, float f2) {
        int i2 = 2;
        int i3 = 0;
        if (i == this.mobileRow || i == this.roamingRow || i == this.wifiRow) {
            if ((!LocaleController.isRTL || f > ((float) AndroidUtilities.m26dp(76.0f))) && (LocaleController.isRTL || f < ((float) (view.getMeasuredWidth() - AndroidUtilities.m26dp(76.0f))))) {
                if (i == this.mobileRow) {
                    i2 = 0;
                } else if (i == this.wifiRow) {
                    i2 = 1;
                }
                presentFragment(new DataAutoDownloadActivity(i2));
            } else {
                Preset preset;
                Preset preset2;
                String str;
                String str2;
                boolean isRowEnabled = this.listAdapter.isRowEnabled(this.resetDownloadRow);
                NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
                boolean isChecked = notificationsCheckCell.isChecked();
                if (i == this.mobileRow) {
                    preset = DownloadController.getInstance(this.currentAccount).mobilePreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).mediumPreset;
                    str = "mobilePreset";
                    str2 = "currentMobilePreset";
                } else if (i == this.wifiRow) {
                    preset = DownloadController.getInstance(this.currentAccount).wifiPreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).highPreset;
                    str = "wifiPreset";
                    str2 = "currentWifiPreset";
                    i3 = 1;
                } else {
                    Preset preset3 = DownloadController.getInstance(this.currentAccount).roamingPreset;
                    preset2 = DownloadController.getInstance(this.currentAccount).lowPreset;
                    str = "roamingPreset";
                    str2 = "currentRoamingPreset";
                    preset = preset3;
                    i3 = 2;
                }
                if (isChecked || !preset.enabled) {
                    preset.enabled ^= 1;
                } else {
                    preset.set(preset2);
                }
                Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
                edit.putString(str, preset.toString());
                edit.putInt(str2, 3);
                edit.commit();
                notificationsCheckCell.setChecked(isChecked ^ 1);
                ViewHolder findContainingViewHolder = this.listView.findContainingViewHolder(view);
                if (findContainingViewHolder != null) {
                    this.listAdapter.onBindViewHolder(findContainingViewHolder, i);
                }
                DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
                DownloadController.getInstance(this.currentAccount).savePresetToServer(i3);
                if (isRowEnabled != this.listAdapter.isRowEnabled(this.resetDownloadRow)) {
                    this.listAdapter.notifyItemChanged(this.resetDownloadRow);
                }
            }
        } else if (i == this.resetDownloadRow) {
            if (getParentActivity() != null && view.isEnabled()) {
                Builder builder = new Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("ResetAutomaticMediaDownloadAlertTitle", C1067R.string.ResetAutomaticMediaDownloadAlertTitle));
                builder.setMessage(LocaleController.getString("ResetAutomaticMediaDownloadAlert", C1067R.string.ResetAutomaticMediaDownloadAlert));
                builder.setPositiveButton(LocaleController.getString("Reset", C1067R.string.Reset), new C1488-$$Lambda$DataSettingsActivity$z7kMV53_j7S-p3GIoSYZ6qy2-tA(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                AlertDialog create = builder.create();
                showDialog(create);
                TextView textView = (TextView) create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor(Theme.key_dialogTextRed2));
                }
            }
        } else if (i == this.storageUsageRow) {
            presentFragment(new CacheControlActivity());
        } else if (i == this.useLessDataForCallsRow) {
            Dialog createSingleChoiceDialog;
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            int i4 = globalMainSettings.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
            if (i4 != 0) {
                if (i4 == 1) {
                    i4 = 2;
                } else if (i4 == 2) {
                    i4 = 3;
                } else if (i4 == 3) {
                    i4 = 1;
                }
                createSingleChoiceDialog = AlertsCreator.createSingleChoiceDialog(getParentActivity(), new String[]{LocaleController.getString("UseLessDataNever", C1067R.string.UseLessDataNever), LocaleController.getString("UseLessDataOnRoaming", C1067R.string.UseLessDataOnRoaming), LocaleController.getString("UseLessDataOnMobile", C1067R.string.UseLessDataOnMobile), LocaleController.getString("UseLessDataAlways", C1067R.string.UseLessDataAlways)}, LocaleController.getString("VoipUseLessData", C1067R.string.VoipUseLessData), i4, new C1487-$$Lambda$DataSettingsActivity$ClwlcXUhT2KAlvJObQobk4HWNyw(this, globalMainSettings, i));
                setVisibleDialog(createSingleChoiceDialog);
                createSingleChoiceDialog.show();
            }
            i4 = 0;
            createSingleChoiceDialog = AlertsCreator.createSingleChoiceDialog(getParentActivity(), new String[]{LocaleController.getString("UseLessDataNever", C1067R.string.UseLessDataNever), LocaleController.getString("UseLessDataOnRoaming", C1067R.string.UseLessDataOnRoaming), LocaleController.getString("UseLessDataOnMobile", C1067R.string.UseLessDataOnMobile), LocaleController.getString("UseLessDataAlways", C1067R.string.UseLessDataAlways)}, LocaleController.getString("VoipUseLessData", C1067R.string.VoipUseLessData), i4, new C1487-$$Lambda$DataSettingsActivity$ClwlcXUhT2KAlvJObQobk4HWNyw(this, globalMainSettings, i));
            setVisibleDialog(createSingleChoiceDialog);
            createSingleChoiceDialog.show();
        } else if (i == this.dataUsageRow) {
            presentFragment(new DataUsageActivity());
        } else if (i == this.proxyRow) {
            presentFragment(new ProxyListActivity());
        } else if (i == this.enableStreamRow) {
            SharedConfig.toggleStreamMedia();
            ((TextCheckCell) view).setChecked(SharedConfig.streamMedia);
        } else if (i == this.enableAllStreamRow) {
            SharedConfig.toggleStreamAllVideo();
            ((TextCheckCell) view).setChecked(SharedConfig.streamAllVideo);
        } else if (i == this.enableMkvRow) {
            SharedConfig.toggleStreamMkv();
            ((TextCheckCell) view).setChecked(SharedConfig.streamMkv);
        } else if (i == this.enableCacheStreamRow) {
            SharedConfig.toggleSaveStreamMedia();
            ((TextCheckCell) view).setChecked(SharedConfig.saveStreamMedia);
        } else if (i == this.quickRepliesRow) {
            presentFragment(new QuickRepliesSettingsActivity());
        } else if (i == this.autoplayGifsRow) {
            SharedConfig.toggleAutoplayGifs();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(SharedConfig.autoplayGifs);
            }
        } else if (i == this.autoplayVideoRow) {
            SharedConfig.toggleAutoplayVideo();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(SharedConfig.autoplayVideo);
            }
        }
    }

    public /* synthetic */ void lambda$null$0$DataSettingsActivity(DialogInterface dialogInterface, int i) {
        Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
        for (int i2 = 0; i2 < 3; i2++) {
            Preset preset;
            Preset preset2;
            String str;
            if (i2 == 0) {
                preset = DownloadController.getInstance(this.currentAccount).mobilePreset;
                preset2 = DownloadController.getInstance(this.currentAccount).mediumPreset;
                str = "mobilePreset";
            } else if (i2 == 1) {
                preset = DownloadController.getInstance(this.currentAccount).wifiPreset;
                preset2 = DownloadController.getInstance(this.currentAccount).highPreset;
                str = "wifiPreset";
            } else {
                preset = DownloadController.getInstance(this.currentAccount).roamingPreset;
                preset2 = DownloadController.getInstance(this.currentAccount).lowPreset;
                str = "roamingPreset";
            }
            preset.set(preset2);
            preset.enabled = preset2.isEnabled();
            DownloadController.getInstance(this.currentAccount).currentMobilePreset = 3;
            edit.putInt("currentMobilePreset", 3);
            DownloadController.getInstance(this.currentAccount).currentWifiPreset = 3;
            edit.putInt("currentWifiPreset", 3);
            DownloadController.getInstance(this.currentAccount).currentRoamingPreset = 3;
            edit.putInt("currentRoamingPreset", 3);
            edit.putString(str, preset.toString());
        }
        edit.commit();
        DownloadController.getInstance(this.currentAccount).checkAutodownloadSettings();
        for (i = 0; i < 3; i++) {
            DownloadController.getInstance(this.currentAccount).savePresetToServer(i);
        }
        this.listAdapter.notifyItemRangeChanged(this.mobileRow, 4);
    }

    public /* synthetic */ void lambda$null$1$DataSettingsActivity(SharedPreferences sharedPreferences, int i, DialogInterface dialogInterface, int i2) {
        int i3 = 3;
        if (i2 == 0) {
            i3 = 0;
        } else if (i2 != 1) {
            i3 = i2 != 2 ? i2 != 3 ? -1 : 2 : 1;
        }
        if (i3 != -1) {
            sharedPreferences.edit().putInt("VoipDataSaving", i3).commit();
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(i);
        }
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
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[23];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, NotificationsCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        View view = this.listView;
        Class[] clsArr = new Class[]{NotificationsCheckCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        themeDescriptionArr[7] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        clsArr = new Class[]{NotificationsCheckCell.class};
        strArr = new String[1];
        strArr[0] = "valueTextView";
        themeDescriptionArr[8] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        view = this.listView;
        clsArr = new Class[]{NotificationsCheckCell.class};
        strArr = new String[1];
        strArr[0] = "checkBox";
        themeDescriptionArr[9] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_switchTrack);
        themeDescriptionArr[10] = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[16] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[17] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[18] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[19] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack);
        themeDescriptionArr[20] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        themeDescriptionArr[21] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[22] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        return themeDescriptionArr;
    }
}
