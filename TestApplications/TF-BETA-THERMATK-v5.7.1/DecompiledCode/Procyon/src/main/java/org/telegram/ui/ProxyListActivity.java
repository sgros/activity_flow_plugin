// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.text.TextUtils$TruncateAt;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import org.telegram.messenger.DownloadController;
import android.content.DialogInterface;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.content.SharedPreferences$Editor;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.RequestTimeDelegate;
import org.telegram.tgnet.ConnectionsManager;
import android.os.SystemClock;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ProxyListActivity extends BaseFragment implements NotificationCenterDelegate
{
    private int callsDetailRow;
    private int callsRow;
    private int connectionsHeaderRow;
    private int currentConnectionState;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int proxyAddRow;
    private int proxyDetailRow;
    private int proxyEndRow;
    private int proxyStartRow;
    private int rowCount;
    private int useProxyDetailRow;
    private boolean useProxyForCalls;
    private int useProxyRow;
    private boolean useProxySettings;
    
    private void checkProxyList() {
        for (int size = SharedConfig.proxyList.size(), i = 0; i < size; ++i) {
            final SharedConfig.ProxyInfo proxyInfo = SharedConfig.proxyList.get(i);
            if (!proxyInfo.checking) {
                if (SystemClock.elapsedRealtime() - proxyInfo.availableCheckTime >= 120000L) {
                    proxyInfo.checking = true;
                    proxyInfo.proxyCheckPingId = ConnectionsManager.getInstance(super.currentAccount).checkProxy(proxyInfo.address, proxyInfo.port, proxyInfo.username, proxyInfo.password, proxyInfo.secret, new _$$Lambda$ProxyListActivity$FREOr2lMcXdjYTgHJySmzXcovWk(proxyInfo));
                }
            }
        }
    }
    
    private void updateRows(final boolean b) {
        final int n = 0;
        boolean b2 = false;
        this.rowCount = 0;
        this.useProxyRow = this.rowCount++;
        this.useProxyDetailRow = this.rowCount++;
        this.connectionsHeaderRow = this.rowCount++;
        if (!SharedConfig.proxyList.isEmpty()) {
            final int rowCount = this.rowCount;
            this.proxyStartRow = rowCount;
            this.rowCount = rowCount + SharedConfig.proxyList.size();
            this.proxyEndRow = this.rowCount;
        }
        else {
            this.proxyStartRow = -1;
            this.proxyEndRow = -1;
        }
        this.proxyAddRow = this.rowCount++;
        this.proxyDetailRow = this.rowCount++;
        final SharedConfig.ProxyInfo currentProxy = SharedConfig.currentProxy;
        if (currentProxy != null && !currentProxy.secret.isEmpty()) {
            if (this.callsRow != -1) {
                b2 = true;
            }
            this.callsRow = -1;
            this.callsDetailRow = -1;
            if (!b && b2) {
                this.listAdapter.notifyItemChanged(this.proxyDetailRow);
                this.listAdapter.notifyItemRangeRemoved(this.proxyDetailRow + 1, 2);
            }
        }
        else {
            int n2 = n;
            if (this.callsRow == -1) {
                n2 = 1;
            }
            this.callsRow = this.rowCount++;
            this.callsDetailRow = this.rowCount++;
            if (!b && n2 != 0) {
                this.listAdapter.notifyItemChanged(this.proxyDetailRow);
                this.listAdapter.notifyItemRangeInserted(this.proxyDetailRow + 1, 2);
            }
        }
        this.checkProxyList();
        if (b) {
            final ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setTitle(LocaleController.getString("ProxySettings", 2131560519));
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setAllowOverlayTitle(false);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ProxyListActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        this.listView = new RecyclerListView(context);
        ((DefaultItemAnimator)this.listView.getItemAnimator()).setDelayAnimations(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ProxyListActivity$_EXt_SJXR8hGLT7iiDQoSoNBA_A(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$ProxyListActivity$xYgyGp8TqWacHgvLq8TnwAzgoII(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int currentConnectionState, final int n, final Object... array) {
        if (currentConnectionState == NotificationCenter.proxySettingsChanged) {
            this.updateRows(true);
        }
        else if (currentConnectionState == NotificationCenter.didUpdateConnectionState) {
            currentConnectionState = ConnectionsManager.getInstance(n).getConnectionState();
            if (this.currentConnectionState != currentConnectionState) {
                this.currentConnectionState = currentConnectionState;
                if (this.listView != null && SharedConfig.currentProxy != null) {
                    currentConnectionState = SharedConfig.proxyList.indexOf(SharedConfig.currentProxy);
                    if (currentConnectionState >= 0) {
                        final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(currentConnectionState + this.proxyStartRow);
                        if (holder != null) {
                            ((TextDetailProxyCell)holder.itemView).updateStatus();
                        }
                    }
                }
            }
        }
        else if (currentConnectionState == NotificationCenter.proxyCheckDone && this.listView != null) {
            currentConnectionState = SharedConfig.proxyList.indexOf(array[0]);
            if (currentConnectionState >= 0) {
                final RecyclerListView.Holder holder2 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(currentConnectionState + this.proxyStartRow);
                if (holder2 != null) {
                    ((TextDetailProxyCell)holder2.itemView).updateStatus();
                }
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, TextDetailProxyCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailProxyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, new Class[] { TextDetailProxyCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteBlueText6"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, new Class[] { TextDetailProxyCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, new Class[] { TextDetailProxyCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGreenText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, new Class[] { TextDetailProxyCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteRedText4"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { TextDetailProxyCell.class }, new String[] { "checkImageView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4") };
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        SharedConfig.loadProxyList();
        this.currentConnectionState = ConnectionsManager.getInstance(super.currentAccount).getConnectionState();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxySettingsChanged);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxyCheckDone);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
        final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        this.useProxySettings = (globalMainSettings.getBoolean("proxy_enabled", false) && !SharedConfig.proxyList.isEmpty());
        this.useProxyForCalls = globalMainSettings.getBoolean("proxy_enabled_calls", false);
        this.updateRows(true);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxyCheckDone);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
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
            return ProxyListActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == ProxyListActivity.this.useProxyDetailRow || n == ProxyListActivity.this.proxyDetailRow) {
                return 0;
            }
            if (n == ProxyListActivity.this.proxyAddRow) {
                return 1;
            }
            if (n == ProxyListActivity.this.useProxyRow || n == ProxyListActivity.this.callsRow) {
                return 3;
            }
            if (n == ProxyListActivity.this.connectionsHeaderRow) {
                return 2;
            }
            if (n >= ProxyListActivity.this.proxyStartRow && n < ProxyListActivity.this.proxyEndRow) {
                return 5;
            }
            return 4;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == ProxyListActivity.this.useProxyRow || adapterPosition == ProxyListActivity.this.callsRow || adapterPosition == ProxyListActivity.this.proxyAddRow || (adapterPosition >= ProxyListActivity.this.proxyStartRow && adapterPosition < ProxyListActivity.this.proxyEndRow);
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                boolean checked = true;
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType != 3) {
                            if (itemViewType != 4) {
                                if (itemViewType == 5) {
                                    final TextDetailProxyCell textDetailProxyCell = (TextDetailProxyCell)viewHolder.itemView;
                                    final SharedConfig.ProxyInfo proxy = SharedConfig.proxyList.get(n - ProxyListActivity.this.proxyStartRow);
                                    textDetailProxyCell.setProxy(proxy);
                                    if (SharedConfig.currentProxy != proxy) {
                                        checked = false;
                                    }
                                    textDetailProxyCell.setChecked(checked);
                                }
                            }
                            else {
                                final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                                if (n == ProxyListActivity.this.callsDetailRow) {
                                    textInfoPrivacyCell.setText(LocaleController.getString("UseProxyForCallsInfo", 2131560973));
                                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                                }
                            }
                        }
                        else {
                            final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                            if (n == ProxyListActivity.this.useProxyRow) {
                                textCheckCell.setTextAndCheck(LocaleController.getString("UseProxySettings", 2131560978), ProxyListActivity.this.useProxySettings, false);
                            }
                            else if (n == ProxyListActivity.this.callsRow) {
                                textCheckCell.setTextAndCheck(LocaleController.getString("UseProxyForCalls", 2131560972), ProxyListActivity.this.useProxyForCalls, false);
                            }
                        }
                    }
                    else {
                        final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                        if (n == ProxyListActivity.this.connectionsHeaderRow) {
                            headerCell.setText(LocaleController.getString("ProxyConnections", 2131560517));
                        }
                    }
                }
                else {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    if (n == ProxyListActivity.this.proxyAddRow) {
                        textSettingsCell.setText(LocaleController.getString("AddProxy", 2131558581), false);
                    }
                }
            }
            else if (n == ProxyListActivity.this.proxyDetailRow && ProxyListActivity.this.callsRow == -1) {
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
                                    o = new TextDetailProxyCell(this.mContext);
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
                if (adapterPosition == ProxyListActivity.this.useProxyRow) {
                    textCheckCell.setChecked(ProxyListActivity.this.useProxySettings);
                }
                else if (adapterPosition == ProxyListActivity.this.callsRow) {
                    textCheckCell.setChecked(ProxyListActivity.this.useProxyForCalls);
                }
            }
        }
    }
    
    public class TextDetailProxyCell extends FrameLayout
    {
        private Drawable checkDrawable;
        private ImageView checkImageView;
        private int color;
        private SharedConfig.ProxyInfo currentInfo;
        private TextView textView;
        private TextView valueTextView;
        
        public TextDetailProxyCell(final Context context) {
            super(context);
            (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils$TruncateAt.END);
            final TextView textView = this.textView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int n2;
            if (isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            textView.setGravity(n2 | 0x10);
            final TextView textView2 = this.textView;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            final boolean isRTL2 = LocaleController.isRTL;
            final int n4 = 56;
            int n5;
            if (isRTL2) {
                n5 = 56;
            }
            else {
                n5 = 21;
            }
            final float n6 = (float)n5;
            int n7;
            if (LocaleController.isRTL) {
                n7 = 21;
            }
            else {
                n7 = 56;
            }
            this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n3 | 0x30, n6, 10.0f, (float)n7, 0.0f));
            (this.valueTextView = new TextView(context)).setTextSize(1, 13.0f);
            final TextView valueTextView = this.valueTextView;
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            valueTextView.setGravity(gravity);
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setCompoundDrawablePadding(AndroidUtilities.dp(6.0f));
            this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
            this.valueTextView.setPadding(0, 0, 0, 0);
            final TextView valueTextView2 = this.valueTextView;
            int n8;
            if (LocaleController.isRTL) {
                n8 = 5;
            }
            else {
                n8 = 3;
            }
            int n9;
            if (LocaleController.isRTL) {
                n9 = 56;
            }
            else {
                n9 = 21;
            }
            final float n10 = (float)n9;
            int n11 = n4;
            if (LocaleController.isRTL) {
                n11 = 21;
            }
            this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n8 | 0x30, n10, 35.0f, (float)n11, 0.0f));
            (this.checkImageView = new ImageView(context)).setImageResource(2131165786);
            this.checkImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText3"), PorterDuff$Mode.MULTIPLY));
            this.checkImageView.setScaleType(ImageView$ScaleType.CENTER);
            this.checkImageView.setContentDescription((CharSequence)LocaleController.getString("Edit", 2131559301));
            final ImageView checkImageView = this.checkImageView;
            int n12 = n;
            if (LocaleController.isRTL) {
                n12 = 3;
            }
            this.addView((View)checkImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n12 | 0x30, 8.0f, 8.0f, 8.0f, 0.0f));
            this.checkImageView.setOnClickListener((View$OnClickListener)new _$$Lambda$ProxyListActivity$TextDetailProxyCell$X7y0ocxBETGvRg05YaI__kXxQ00(this));
            this.setWillNotDraw(false);
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.updateStatus();
        }
        
        protected void onDraw(final Canvas canvas) {
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)AndroidUtilities.dp(20.0f);
            }
            final float n2 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(20.0f);
            }
            else {
                dp = 0;
            }
            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
        
        protected void onMeasure(final int n, final int n2) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + 1, 1073741824));
        }
        
        public void setChecked(final boolean b) {
            if (b) {
                if (this.checkDrawable == null) {
                    this.checkDrawable = this.getResources().getDrawable(2131165794).mutate();
                }
                final Drawable checkDrawable = this.checkDrawable;
                if (checkDrawable != null) {
                    checkDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(this.color, PorterDuff$Mode.MULTIPLY));
                }
                if (LocaleController.isRTL) {
                    this.valueTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.checkDrawable, (Drawable)null);
                }
                else {
                    this.valueTextView.setCompoundDrawablesWithIntrinsicBounds(this.checkDrawable, (Drawable)null, (Drawable)null, (Drawable)null);
                }
            }
            else {
                this.valueTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
            }
        }
        
        public void setProxy(final SharedConfig.ProxyInfo currentInfo) {
            final TextView textView = this.textView;
            final StringBuilder sb = new StringBuilder();
            sb.append(currentInfo.address);
            sb.append(":");
            sb.append(currentInfo.port);
            textView.setText((CharSequence)sb.toString());
            this.currentInfo = currentInfo;
        }
        
        public void setValue(final CharSequence text) {
            this.valueTextView.setText(text);
        }
        
        public void updateStatus() {
            final SharedConfig.ProxyInfo currentProxy = SharedConfig.currentProxy;
            final SharedConfig.ProxyInfo currentInfo = this.currentInfo;
            String tag = "windowBackgroundWhiteGrayText2";
            if (currentProxy == currentInfo && ProxyListActivity.this.useProxySettings) {
                if (ProxyListActivity.this.currentConnectionState != 3 && ProxyListActivity.this.currentConnectionState != 5) {
                    this.valueTextView.setText((CharSequence)LocaleController.getString("Connecting", 2131559137));
                }
                else {
                    if (this.currentInfo.ping != 0L) {
                        final TextView valueTextView = this.valueTextView;
                        final StringBuilder sb = new StringBuilder();
                        sb.append(LocaleController.getString("Connected", 2131559136));
                        sb.append(", ");
                        sb.append(LocaleController.formatString("Ping", 2131560449, this.currentInfo.ping));
                        valueTextView.setText((CharSequence)sb.toString());
                    }
                    else {
                        this.valueTextView.setText((CharSequence)LocaleController.getString("Connected", 2131559136));
                    }
                    final SharedConfig.ProxyInfo currentInfo2 = this.currentInfo;
                    if (!currentInfo2.checking && !currentInfo2.available) {
                        currentInfo2.availableCheckTime = 0L;
                    }
                    tag = "windowBackgroundWhiteBlueText6";
                }
            }
            else {
                final SharedConfig.ProxyInfo currentInfo3 = this.currentInfo;
                if (currentInfo3.checking) {
                    this.valueTextView.setText((CharSequence)LocaleController.getString("Checking", 2131559085));
                }
                else if (currentInfo3.available) {
                    if (currentInfo3.ping != 0L) {
                        final TextView valueTextView2 = this.valueTextView;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(LocaleController.getString("Available", 2131558807));
                        sb2.append(", ");
                        sb2.append(LocaleController.formatString("Ping", 2131560449, this.currentInfo.ping));
                        valueTextView2.setText((CharSequence)sb2.toString());
                    }
                    else {
                        this.valueTextView.setText((CharSequence)LocaleController.getString("Available", 2131558807));
                    }
                    tag = "windowBackgroundWhiteGreenText";
                }
                else {
                    this.valueTextView.setText((CharSequence)LocaleController.getString("Unavailable", 2131560929));
                    tag = "windowBackgroundWhiteRedText4";
                }
            }
            this.color = Theme.getColor(tag);
            this.valueTextView.setTag((Object)tag);
            this.valueTextView.setTextColor(this.color);
            final Drawable checkDrawable = this.checkDrawable;
            if (checkDrawable != null) {
                checkDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(this.color, PorterDuff$Mode.MULTIPLY));
            }
        }
    }
}
