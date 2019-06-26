// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.animation.Animator$AnimatorListener;
import java.util.Collection;
import android.content.SharedPreferences$Editor;
import android.app.Dialog;
import android.widget.TextView;
import android.animation.Animator;
import android.widget.LinearLayout$LayoutParams;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View$OnClickListener;
import org.telegram.ui.Cells.TextCheckBoxCell;
import android.animation.AnimatorSet;
import org.telegram.ui.Cells.MaxFileSizeCell;
import android.widget.LinearLayout;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.BottomSheet;
import android.view.View;
import org.telegram.ui.Cells.TextCheckCell;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.messenger.DownloadController;
import org.telegram.ui.ActionBar.BaseFragment;

public class DataAutoDownloadActivity extends BaseFragment
{
    private boolean animateChecked;
    private int autoDownloadRow;
    private int autoDownloadSectionRow;
    private int currentPresetNum;
    private int currentType;
    private DownloadController.Preset defaultPreset;
    private int filesRow;
    private DownloadController.Preset highPreset;
    private String key;
    private String key2;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private DownloadController.Preset lowPreset;
    private DownloadController.Preset mediumPreset;
    private int photosRow;
    private ArrayList<DownloadController.Preset> presets;
    private int rowCount;
    private int selectedPreset;
    private int typeHeaderRow;
    private DownloadController.Preset typePreset;
    private int typeSectionRow;
    private int usageHeaderRow;
    private int usageProgressRow;
    private int usageSectionRow;
    private int videosRow;
    private boolean wereAnyChanges;
    
    public DataAutoDownloadActivity(int currentType) {
        this.presets = new ArrayList<DownloadController.Preset>();
        this.selectedPreset = 1;
        this.currentType = currentType;
        this.lowPreset = DownloadController.getInstance(super.currentAccount).lowPreset;
        this.mediumPreset = DownloadController.getInstance(super.currentAccount).mediumPreset;
        this.highPreset = DownloadController.getInstance(super.currentAccount).highPreset;
        currentType = this.currentType;
        if (currentType == 0) {
            this.currentPresetNum = DownloadController.getInstance(super.currentAccount).currentMobilePreset;
            this.typePreset = DownloadController.getInstance(super.currentAccount).mobilePreset;
            this.defaultPreset = this.mediumPreset;
            this.key = "mobilePreset";
            this.key2 = "currentMobilePreset";
        }
        else if (currentType == 1) {
            this.currentPresetNum = DownloadController.getInstance(super.currentAccount).currentWifiPreset;
            this.typePreset = DownloadController.getInstance(super.currentAccount).wifiPreset;
            this.defaultPreset = this.highPreset;
            this.key = "wifiPreset";
            this.key2 = "currentWifiPreset";
        }
        else {
            this.currentPresetNum = DownloadController.getInstance(super.currentAccount).currentRoamingPreset;
            this.typePreset = DownloadController.getInstance(super.currentAccount).roamingPreset;
            this.defaultPreset = this.lowPreset;
            this.key = "roamingPreset";
            this.key2 = "currentRoamingPreset";
        }
    }
    
    private void fillPresets() {
        this.presets.clear();
        this.presets.add(this.lowPreset);
        this.presets.add(this.mediumPreset);
        this.presets.add(this.highPreset);
        if (!this.typePreset.equals(this.lowPreset) && !this.typePreset.equals(this.mediumPreset) && !this.typePreset.equals(this.highPreset)) {
            this.presets.add(this.typePreset);
        }
        Collections.sort(this.presets, (Comparator<? super DownloadController.Preset>)_$$Lambda$DataAutoDownloadActivity$E0PVxdOLHPC3ZjO_nDkt8nGBYVw.INSTANCE);
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            final RecyclerView.ViewHolder viewHolderForAdapterPosition = listView.findViewHolderForAdapterPosition(this.usageProgressRow);
            if (viewHolderForAdapterPosition != null) {
                viewHolderForAdapterPosition.itemView.requestLayout();
            }
            else {
                this.listAdapter.notifyItemChanged(this.usageProgressRow);
            }
        }
        final int currentPresetNum = this.currentPresetNum;
        if (currentPresetNum != 0 && (currentPresetNum != 3 || !this.typePreset.equals(this.lowPreset))) {
            final int currentPresetNum2 = this.currentPresetNum;
            if (currentPresetNum2 != 1 && (currentPresetNum2 != 3 || !this.typePreset.equals(this.mediumPreset))) {
                final int currentPresetNum3 = this.currentPresetNum;
                if (currentPresetNum3 != 2 && (currentPresetNum3 != 3 || !this.typePreset.equals(this.highPreset))) {
                    this.selectedPreset = this.presets.indexOf(this.typePreset);
                }
                else {
                    this.selectedPreset = this.presets.indexOf(this.highPreset);
                }
            }
            else {
                this.selectedPreset = this.presets.indexOf(this.mediumPreset);
            }
        }
        else {
            this.selectedPreset = this.presets.indexOf(this.lowPreset);
        }
    }
    
    private void updateRows() {
        this.rowCount = 0;
        this.autoDownloadRow = this.rowCount++;
        this.autoDownloadSectionRow = this.rowCount++;
        if (this.typePreset.enabled) {
            this.usageHeaderRow = this.rowCount++;
            this.usageProgressRow = this.rowCount++;
            this.usageSectionRow = this.rowCount++;
            this.typeHeaderRow = this.rowCount++;
            this.photosRow = this.rowCount++;
            this.videosRow = this.rowCount++;
            this.filesRow = this.rowCount++;
            this.typeSectionRow = this.rowCount++;
        }
        else {
            this.usageHeaderRow = -1;
            this.usageProgressRow = -1;
            this.usageSectionRow = -1;
            this.typeHeaderRow = -1;
            this.photosRow = -1;
            this.videosRow = -1;
            this.filesRow = -1;
            this.typeSectionRow = -1;
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        final int currentType = this.currentType;
        if (currentType == 0) {
            super.actionBar.setTitle(LocaleController.getString("AutoDownloadOnMobileData", 2131558758));
        }
        else if (currentType == 1) {
            super.actionBar.setTitle(LocaleController.getString("AutoDownloadOnWiFiData", 2131558763));
        }
        else if (currentType == 2) {
            super.actionBar.setTitle(LocaleController.getString("AutoDownloadOnRoamingData", 2131558760));
        }
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    DataAutoDownloadActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(false);
        ((DefaultItemAnimator)this.listView.getItemAnimator()).setDelayAnimations(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)new _$$Lambda$DataAutoDownloadActivity$z_MZui0AcXTnHUC_YO0Sopv_aSo(this));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { HeaderCell.class, NotificationsCheckCell.class, PresetChooseView.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCheckCell.class }, null, null, null, "windowBackgroundChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCheckCell.class }, null, null, null, "windowBackgroundUnchecked"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundCheckText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackBlue"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackBlueChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackBlueThumb"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackBlueThumbChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackBlueSelector"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackBlueSelectorChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { PresetChooseView.class }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { PresetChooseView.class }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { PresetChooseView.class }, null, null, null, "windowBackgroundWhiteGrayText") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.fillPresets();
        this.updateRows();
        return true;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (this.wereAnyChanges) {
            DownloadController.getInstance(super.currentAccount).savePresetToServer(this.currentType);
            this.wereAnyChanges = false;
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
            return DataAutoDownloadActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == DataAutoDownloadActivity.this.autoDownloadRow) {
                return 0;
            }
            if (n == DataAutoDownloadActivity.this.usageSectionRow) {
                return 1;
            }
            if (n == DataAutoDownloadActivity.this.usageHeaderRow || n == DataAutoDownloadActivity.this.typeHeaderRow) {
                return 2;
            }
            if (n == DataAutoDownloadActivity.this.usageProgressRow) {
                return 3;
            }
            if (n != DataAutoDownloadActivity.this.photosRow && n != DataAutoDownloadActivity.this.videosRow && n != DataAutoDownloadActivity.this.filesRow) {
                return 5;
            }
            return 4;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == DataAutoDownloadActivity.this.photosRow || adapterPosition == DataAutoDownloadActivity.this.videosRow || adapterPosition == DataAutoDownloadActivity.this.filesRow;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 2) {
                    if (itemViewType != 4) {
                        if (itemViewType == 5) {
                            final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                            if (n == DataAutoDownloadActivity.this.typeSectionRow) {
                                textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadAudioInfo", 2131558740));
                                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                                textInfoPrivacyCell.setFixedSize(0);
                            }
                            else if (n == DataAutoDownloadActivity.this.autoDownloadSectionRow) {
                                if (DataAutoDownloadActivity.this.usageHeaderRow == -1) {
                                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                                    if (DataAutoDownloadActivity.this.currentType == 0) {
                                        textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnMobileDataInfo", 2131558759));
                                    }
                                    else if (DataAutoDownloadActivity.this.currentType == 1) {
                                        textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnWiFiDataInfo", 2131558764));
                                    }
                                    else if (DataAutoDownloadActivity.this.currentType == 2) {
                                        textInfoPrivacyCell.setText(LocaleController.getString("AutoDownloadOnRoamingDataInfo", 2131558761));
                                    }
                                }
                                else {
                                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                                    textInfoPrivacyCell.setText(null);
                                    textInfoPrivacyCell.setFixedSize(12);
                                }
                            }
                        }
                    }
                    else {
                        final NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell)viewHolder.itemView;
                        String s;
                        int n2;
                        if (n == DataAutoDownloadActivity.this.photosRow) {
                            s = LocaleController.getString("AutoDownloadPhotos", 2131558765);
                            n2 = 1;
                        }
                        else if (n == DataAutoDownloadActivity.this.videosRow) {
                            s = LocaleController.getString("AutoDownloadVideos", 2131558775);
                            n2 = 4;
                        }
                        else {
                            s = LocaleController.getString("AutoDownloadFiles", 2131558745);
                            n2 = 8;
                        }
                        DownloadController.Preset preset;
                        if (DataAutoDownloadActivity.this.currentType == 0) {
                            preset = DownloadController.getInstance(DataAutoDownloadActivity.this.currentAccount).getCurrentMobilePreset();
                        }
                        else if (DataAutoDownloadActivity.this.currentType == 1) {
                            preset = DownloadController.getInstance(DataAutoDownloadActivity.this.currentAccount).getCurrentWiFiPreset();
                        }
                        else {
                            preset = DownloadController.getInstance(DataAutoDownloadActivity.this.currentAccount).getCurrentRoamingPreset();
                        }
                        final int n3 = preset.sizes[DownloadController.typeToIndex(n2)];
                        final StringBuilder sb = new StringBuilder();
                        int n4 = 0;
                        int n5 = 0;
                        while (true) {
                            final int[] mask = preset.mask;
                            if (n4 >= mask.length) {
                                break;
                            }
                            int n6 = n5;
                            if ((mask[n4] & n2) != 0x0) {
                                if (sb.length() != 0) {
                                    sb.append(", ");
                                }
                                if (n4 != 0) {
                                    if (n4 != 1) {
                                        if (n4 != 2) {
                                            if (n4 == 3) {
                                                sb.append(LocaleController.getString("AutoDownloadChannels", 2131558741));
                                            }
                                        }
                                        else {
                                            sb.append(LocaleController.getString("AutoDownloadGroups", 2131558748));
                                        }
                                    }
                                    else {
                                        sb.append(LocaleController.getString("AutoDownloadPm", 2131558768));
                                    }
                                }
                                else {
                                    sb.append(LocaleController.getString("AutoDownloadContacts", 2131558742));
                                }
                                n6 = n5 + 1;
                            }
                            ++n4;
                            n5 = n6;
                        }
                        StringBuilder sb2 = null;
                        Label_0707: {
                            if (n5 == 4) {
                                sb.setLength(0);
                                if (n == DataAutoDownloadActivity.this.photosRow) {
                                    sb.append(LocaleController.getString("AutoDownloadOnAllChats", 2131558756));
                                }
                                else {
                                    sb.append(LocaleController.formatString("AutoDownloadUpToOnAllChats", 2131558774, AndroidUtilities.formatFileSize(n3)));
                                }
                            }
                            else if (n5 == 0) {
                                sb.append(LocaleController.getString("AutoDownloadOff", 2131558755));
                            }
                            else {
                                if (n == DataAutoDownloadActivity.this.photosRow) {
                                    sb2 = new StringBuilder(LocaleController.formatString("AutoDownloadOnFor", 2131558757, sb.toString()));
                                    break Label_0707;
                                }
                                sb2 = new StringBuilder(LocaleController.formatString("AutoDownloadOnUpToFor", 2131558762, AndroidUtilities.formatFileSize(n3), sb.toString()));
                                break Label_0707;
                            }
                            sb2 = sb;
                        }
                        if (DataAutoDownloadActivity.this.animateChecked) {
                            notificationsCheckCell.setChecked(n5 != 0);
                        }
                        notificationsCheckCell.setTextAndValueAndCheck(s, sb2, n5 != 0, 0, true, n != DataAutoDownloadActivity.this.filesRow);
                    }
                }
                else {
                    final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                    if (n == DataAutoDownloadActivity.this.usageHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoDownloadDataUsage", 2131558744));
                    }
                    else if (n == DataAutoDownloadActivity.this.typeHeaderRow) {
                        headerCell.setText(LocaleController.getString("AutoDownloadTypes", 2131558773));
                    }
                }
            }
            else {
                final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                if (n == DataAutoDownloadActivity.this.autoDownloadRow) {
                    textCheckCell.setDrawCheckRipple(true);
                    textCheckCell.setTextAndCheck(LocaleController.getString("AutoDownloadMedia", 2131558753), DataAutoDownloadActivity.this.typePreset.enabled, false);
                    final boolean enabled = DataAutoDownloadActivity.this.typePreset.enabled;
                    final String s2 = "windowBackgroundChecked";
                    String tag;
                    if (enabled) {
                        tag = "windowBackgroundChecked";
                    }
                    else {
                        tag = "windowBackgroundUnchecked";
                    }
                    textCheckCell.setTag((Object)tag);
                    String s3;
                    if (DataAutoDownloadActivity.this.typePreset.enabled) {
                        s3 = s2;
                    }
                    else {
                        s3 = "windowBackgroundUnchecked";
                    }
                    textCheckCell.setBackgroundColor(Theme.getColor(s3));
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
                                    o = null;
                                }
                                else {
                                    o = new TextInfoPrivacyCell(this.mContext);
                                    ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                                }
                            }
                            else {
                                o = new NotificationsCheckCell(this.mContext);
                                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                            }
                        }
                        else {
                            o = new PresetChooseView(this.mContext);
                            ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                    }
                    else {
                        o = new HeaderCell(this.mContext);
                        ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    o = new ShadowSectionCell(this.mContext);
                }
            }
            else {
                o = new TextCheckCell(this.mContext);
                ((TextCheckCell)o).setColors("windowBackgroundCheckText", "switchTrackBlue", "switchTrackBlueChecked", "switchTrackBlueThumb", "switchTrackBlueThumbChecked");
                ((TextCheckCell)o).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                ((TextCheckCell)o).setHeight(56);
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class PresetChooseView extends View
    {
        private int circleSize;
        private String custom;
        private int customSize;
        private int gapSize;
        private String high;
        private int highSize;
        private int lineSize;
        private String low;
        private int lowSize;
        private String medium;
        private int mediumSize;
        private boolean moving;
        private Paint paint;
        private int sideSide;
        private boolean startMoving;
        private int startMovingPreset;
        private float startX;
        private TextPaint textPaint;
        
        public PresetChooseView(final Context context) {
            super(context);
            this.paint = new Paint(1);
            (this.textPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(13.0f));
            this.low = LocaleController.getString("AutoDownloadLow", 2131558750);
            this.lowSize = (int)Math.ceil(this.textPaint.measureText(this.low));
            this.medium = LocaleController.getString("AutoDownloadMedium", 2131558754);
            this.mediumSize = (int)Math.ceil(this.textPaint.measureText(this.medium));
            this.high = LocaleController.getString("AutoDownloadHigh", 2131558749);
            this.highSize = (int)Math.ceil(this.textPaint.measureText(this.high));
            this.custom = LocaleController.getString("AutoDownloadCustom", 2131558743);
            this.customSize = (int)Math.ceil(this.textPaint.measureText(this.custom));
        }
        
        private void setPreset(int i) {
            DataAutoDownloadActivity.this.selectedPreset = i;
            final DownloadController.Preset preset = DataAutoDownloadActivity.this.presets.get(DataAutoDownloadActivity.this.selectedPreset);
            final DownloadController.Preset access$200 = DataAutoDownloadActivity.this.lowPreset;
            i = 0;
            if (preset == access$200) {
                DataAutoDownloadActivity.this.currentPresetNum = 0;
            }
            else if (preset == DataAutoDownloadActivity.this.mediumPreset) {
                DataAutoDownloadActivity.this.currentPresetNum = 1;
            }
            else if (preset == DataAutoDownloadActivity.this.highPreset) {
                DataAutoDownloadActivity.this.currentPresetNum = 2;
            }
            else {
                DataAutoDownloadActivity.this.currentPresetNum = 3;
            }
            if (DataAutoDownloadActivity.this.currentType == 0) {
                DownloadController.getInstance(DataAutoDownloadActivity.this.currentAccount).currentMobilePreset = DataAutoDownloadActivity.this.currentPresetNum;
            }
            else if (DataAutoDownloadActivity.this.currentType == 1) {
                DownloadController.getInstance(DataAutoDownloadActivity.this.currentAccount).currentWifiPreset = DataAutoDownloadActivity.this.currentPresetNum;
            }
            else {
                DownloadController.getInstance(DataAutoDownloadActivity.this.currentAccount).currentRoamingPreset = DataAutoDownloadActivity.this.currentPresetNum;
            }
            final SharedPreferences$Editor edit = MessagesController.getMainSettings(DataAutoDownloadActivity.this.currentAccount).edit();
            edit.putInt(DataAutoDownloadActivity.this.key2, DataAutoDownloadActivity.this.currentPresetNum);
            edit.commit();
            DownloadController.getInstance(DataAutoDownloadActivity.this.currentAccount).checkAutodownloadSettings();
            while (i < 3) {
                final RecyclerView.ViewHolder viewHolderForAdapterPosition = DataAutoDownloadActivity.this.listView.findViewHolderForAdapterPosition(DataAutoDownloadActivity.this.photosRow + i);
                if (viewHolderForAdapterPosition != null) {
                    DataAutoDownloadActivity.this.listAdapter.onBindViewHolder(viewHolderForAdapterPosition, DataAutoDownloadActivity.this.photosRow + i);
                }
                ++i;
            }
            DataAutoDownloadActivity.this.wereAnyChanges = true;
            this.invalidate();
        }
        
        protected void onDraw(final Canvas canvas) {
            this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteGrayText"));
            final int n = this.getMeasuredHeight() / 2 + AndroidUtilities.dp(11.0f);
            for (int i = 0; i < DataAutoDownloadActivity.this.presets.size(); ++i) {
                final int sideSide = this.sideSide;
                final int lineSize = this.lineSize;
                final int gapSize = this.gapSize;
                final int circleSize = this.circleSize;
                final int n2 = sideSide + (lineSize + gapSize * 2 + circleSize) * i + circleSize / 2;
                if (i <= DataAutoDownloadActivity.this.selectedPreset) {
                    this.paint.setColor(Theme.getColor("switchTrackChecked"));
                }
                else {
                    this.paint.setColor(Theme.getColor("switchTrack"));
                }
                final float n3 = (float)n2;
                final float n4 = (float)n;
                int dp;
                if (i == DataAutoDownloadActivity.this.selectedPreset) {
                    dp = AndroidUtilities.dp(6.0f);
                }
                else {
                    dp = this.circleSize / 2;
                }
                canvas.drawCircle(n3, n4, (float)dp, this.paint);
                if (i != 0) {
                    final int n5 = this.circleSize / 2;
                    final int gapSize2 = this.gapSize;
                    final int lineSize2 = this.lineSize;
                    final int n6 = n2 - n5 - gapSize2 - lineSize2;
                    int n7 = 0;
                    Label_0258: {
                        if (i != DataAutoDownloadActivity.this.selectedPreset) {
                            n7 = lineSize2;
                            if (i != DataAutoDownloadActivity.this.selectedPreset + 1) {
                                break Label_0258;
                            }
                        }
                        n7 = lineSize2 - AndroidUtilities.dp(3.0f);
                    }
                    int n8 = n6;
                    if (i == DataAutoDownloadActivity.this.selectedPreset + 1) {
                        n8 = n6 + AndroidUtilities.dp(3.0f);
                    }
                    canvas.drawRect((float)n8, (float)(n - AndroidUtilities.dp(1.0f)), (float)(n8 + n7), (float)(AndroidUtilities.dp(1.0f) + n), this.paint);
                }
                final DownloadController.Preset preset = DataAutoDownloadActivity.this.presets.get(i);
                String s;
                int n9;
                if (preset == DataAutoDownloadActivity.this.lowPreset) {
                    s = this.low;
                    n9 = this.lowSize;
                }
                else if (preset == DataAutoDownloadActivity.this.mediumPreset) {
                    s = this.medium;
                    n9 = this.mediumSize;
                }
                else if (preset == DataAutoDownloadActivity.this.highPreset) {
                    s = this.high;
                    n9 = this.highSize;
                }
                else {
                    s = this.custom;
                    n9 = this.customSize;
                }
                if (i == 0) {
                    canvas.drawText(s, (float)AndroidUtilities.dp(22.0f), (float)AndroidUtilities.dp(28.0f), (Paint)this.textPaint);
                }
                else if (i == DataAutoDownloadActivity.this.presets.size() - 1) {
                    canvas.drawText(s, (float)(this.getMeasuredWidth() - n9 - AndroidUtilities.dp(22.0f)), (float)AndroidUtilities.dp(28.0f), (Paint)this.textPaint);
                }
                else {
                    canvas.drawText(s, (float)(n2 - n9 / 2), (float)AndroidUtilities.dp(28.0f), (Paint)this.textPaint);
                }
            }
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(74.0f), 1073741824));
            View$MeasureSpec.getSize(n);
            this.circleSize = AndroidUtilities.dp(6.0f);
            this.gapSize = AndroidUtilities.dp(2.0f);
            this.sideSide = AndroidUtilities.dp(22.0f);
            this.lineSize = (this.getMeasuredWidth() - this.circleSize * DataAutoDownloadActivity.this.presets.size() - this.gapSize * 2 * (DataAutoDownloadActivity.this.presets.size() - 1) - this.sideSide * 2) / (DataAutoDownloadActivity.this.presets.size() - 1);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final float x = motionEvent.getX();
            final int action = motionEvent.getAction();
            int i = 0;
            boolean startMoving = false;
            if (action == 0) {
                this.getParent().requestDisallowInterceptTouchEvent(true);
                for (int j = 0; j < DataAutoDownloadActivity.this.presets.size(); ++j) {
                    final int sideSide = this.sideSide;
                    final int lineSize = this.lineSize;
                    final int gapSize = this.gapSize;
                    final int circleSize = this.circleSize;
                    final int n = sideSide + (lineSize + gapSize * 2 + circleSize) * j + circleSize / 2;
                    if (x > n - AndroidUtilities.dp(15.0f) && x < n + AndroidUtilities.dp(15.0f)) {
                        if (j == DataAutoDownloadActivity.this.selectedPreset) {
                            startMoving = true;
                        }
                        this.startMoving = startMoving;
                        this.startX = x;
                        this.startMovingPreset = DataAutoDownloadActivity.this.selectedPreset;
                        break;
                    }
                }
            }
            else if (motionEvent.getAction() == 2) {
                if (this.startMoving) {
                    if (Math.abs(this.startX - x) >= AndroidUtilities.getPixelsInCM(0.5f, true)) {
                        this.moving = true;
                        this.startMoving = false;
                    }
                }
                else if (this.moving) {
                    while (i < DataAutoDownloadActivity.this.presets.size()) {
                        final int sideSide2 = this.sideSide;
                        final int lineSize2 = this.lineSize;
                        final int gapSize2 = this.gapSize;
                        final int circleSize2 = this.circleSize;
                        final int n2 = sideSide2 + (gapSize2 * 2 + lineSize2 + circleSize2) * i + circleSize2 / 2;
                        final int n3 = lineSize2 / 2 + circleSize2 / 2 + gapSize2;
                        if (x > n2 - n3 && x < n2 + n3) {
                            if (DataAutoDownloadActivity.this.selectedPreset != i) {
                                this.setPreset(i);
                                break;
                            }
                            break;
                        }
                        else {
                            ++i;
                        }
                    }
                }
            }
            else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                if (!this.moving) {
                    int k = 0;
                    while (k < 5) {
                        final int sideSide3 = this.sideSide;
                        final int lineSize3 = this.lineSize;
                        final int gapSize3 = this.gapSize;
                        final int circleSize3 = this.circleSize;
                        final int n4 = sideSide3 + (lineSize3 + gapSize3 * 2 + circleSize3) * k + circleSize3 / 2;
                        if (x > n4 - AndroidUtilities.dp(15.0f) && x < n4 + AndroidUtilities.dp(15.0f)) {
                            if (DataAutoDownloadActivity.this.selectedPreset != k) {
                                this.setPreset(k);
                                break;
                            }
                            break;
                        }
                        else {
                            ++k;
                        }
                    }
                }
                else if (DataAutoDownloadActivity.this.selectedPreset != this.startMovingPreset) {
                    this.setPreset(DataAutoDownloadActivity.this.selectedPreset);
                }
                this.startMoving = false;
                this.moving = false;
            }
            return true;
        }
    }
}
