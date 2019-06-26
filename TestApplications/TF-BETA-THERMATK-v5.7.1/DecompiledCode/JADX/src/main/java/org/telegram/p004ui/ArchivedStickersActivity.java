package org.telegram.p004ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.ArchivedStickerSetCell;
import org.telegram.p004ui.Cells.LoadingCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.p004ui.Components.StickersAlert;
import org.telegram.p004ui.Components.StickersAlert.StickersAlertInstallDelegate;
import org.telegram.p004ui.Components.Switch;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.StickerSetCovered;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC.TL_messages_archivedStickers;
import org.telegram.tgnet.TLRPC.TL_messages_getArchivedStickers;

/* renamed from: org.telegram.ui.ArchivedStickersActivity */
public class ArchivedStickersActivity extends BaseFragment implements NotificationCenterDelegate {
    private int currentType;
    private EmptyTextProgressView emptyView;
    private boolean endReached;
    private boolean firstLoaded;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loadingStickers;
    private int rowCount;
    private ArrayList<StickerSetCovered> sets = new ArrayList();
    private int stickersEndRow;
    private int stickersLoadingRow;
    private int stickersShadowRow;
    private int stickersStartRow;

    /* renamed from: org.telegram.ui.ArchivedStickersActivity$1 */
    class C39231 extends ActionBarMenuOnItemClick {
        C39231() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                ArchivedStickersActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.ArchivedStickersActivity$3 */
    class C39253 extends OnScrollListener {
        C39253() {
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (!ArchivedStickersActivity.this.loadingStickers && !ArchivedStickersActivity.this.endReached && ArchivedStickersActivity.this.layoutManager.findLastVisibleItemPosition() > ArchivedStickersActivity.this.stickersLoadingRow - 2) {
                ArchivedStickersActivity.this.getStickers();
            }
        }
    }

    /* renamed from: org.telegram.ui.ArchivedStickersActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return ArchivedStickersActivity.this.rowCount;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (getItemViewType(i) == 0) {
                ArchivedStickerSetCell archivedStickerSetCell = (ArchivedStickerSetCell) viewHolder.itemView;
                archivedStickerSetCell.setTag(Integer.valueOf(i));
                StickerSetCovered stickerSetCovered = (StickerSetCovered) ArchivedStickersActivity.this.sets.get(i);
                boolean z = true;
                if (i == ArchivedStickersActivity.this.sets.size() - 1) {
                    z = false;
                }
                archivedStickerSetCell.setStickersSet(stickerSetCovered, z);
                archivedStickerSetCell.setChecked(DataQuery.getInstance(ArchivedStickersActivity.this.currentAccount).isStickerPackInstalled(stickerSetCovered.set.f466id));
            }
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0$ArchivedStickersActivity$ListAdapter(Switch switchR, boolean z) {
            int intValue = ((Integer) ((ArchivedStickerSetCell) switchR.getParent()).getTag()).intValue();
            if (intValue < ArchivedStickersActivity.this.sets.size()) {
                DataQuery.getInstance(ArchivedStickersActivity.this.currentAccount).removeStickersSet(ArchivedStickersActivity.this.getParentActivity(), ((StickerSetCovered) ArchivedStickersActivity.this.sets.get(intValue)).set, !z ? 1 : 2, ArchivedStickersActivity.this, false);
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View loadingCell;
            if (i != 0) {
                String str = Theme.key_windowBackgroundGrayShadow;
                if (i == 1) {
                    loadingCell = new LoadingCell(this.mContext);
                    loadingCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, str));
                } else if (i != 2) {
                    loadingCell = null;
                } else {
                    loadingCell = new TextInfoPrivacyCell(this.mContext);
                    loadingCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, str));
                }
            } else {
                View archivedStickerSetCell = new ArchivedStickerSetCell(this.mContext, true);
                archivedStickerSetCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                archivedStickerSetCell.setOnCheckClick(new C3572xb9570854(this));
                loadingCell = archivedStickerSetCell;
            }
            loadingCell.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(loadingCell);
        }

        public int getItemViewType(int i) {
            if (i >= ArchivedStickersActivity.this.stickersStartRow && i < ArchivedStickersActivity.this.stickersEndRow) {
                return 0;
            }
            if (i == ArchivedStickersActivity.this.stickersLoadingRow) {
                return 1;
            }
            if (i == ArchivedStickersActivity.this.stickersShadowRow) {
                return 2;
            }
            return 0;
        }
    }

    public ArchivedStickersActivity(int i) {
        this.currentType = i;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        getStickers();
        updateRows();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needReloadArchivedStickers);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needReloadArchivedStickers);
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == 0) {
            this.actionBar.setTitle(LocaleController.getString("ArchivedStickers", C1067R.string.ArchivedStickers));
        } else {
            this.actionBar.setTitle(LocaleController.getString("ArchivedMasks", C1067R.string.ArchivedMasks));
        }
        this.actionBar.setActionBarMenuOnItemClick(new C39231());
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.emptyView = new EmptyTextProgressView(context);
        if (this.currentType == 0) {
            this.emptyView.setText(LocaleController.getString("ArchivedStickersEmpty", C1067R.string.ArchivedStickersEmpty));
        } else {
            this.emptyView.setText(LocaleController.getString("ArchivedMasksEmpty", C1067R.string.ArchivedMasksEmpty));
        }
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        if (this.loadingStickers) {
            this.emptyView.showProgress();
        } else {
            this.emptyView.showTextView();
        }
        this.listView = new RecyclerListView(context);
        this.listView.setFocusable(true);
        this.listView.setEmptyView(this.emptyView);
        RecyclerListView recyclerListView = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new C3573-$$Lambda$ArchivedStickersActivity$a3I8kBmTW-AQtR9tR_9PNmdkhkA(this));
        this.listView.setOnScrollListener(new C39253());
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$0$ArchivedStickersActivity(final View view, int i) {
        if (i >= this.stickersStartRow && i < this.stickersEndRow && getParentActivity() != null) {
            InputStickerSet tL_inputStickerSetID;
            StickerSetCovered stickerSetCovered = (StickerSetCovered) this.sets.get(i);
            if (stickerSetCovered.set.f466id != 0) {
                tL_inputStickerSetID = new TL_inputStickerSetID();
                tL_inputStickerSetID.f460id = stickerSetCovered.set.f466id;
            } else {
                tL_inputStickerSetID = new TL_inputStickerSetShortName();
                tL_inputStickerSetID.short_name = stickerSetCovered.set.short_name;
            }
            InputStickerSet inputStickerSet = tL_inputStickerSetID;
            inputStickerSet.access_hash = stickerSetCovered.set.access_hash;
            StickersAlert stickersAlert = new StickersAlert(getParentActivity(), this, inputStickerSet, null, null);
            stickersAlert.setInstallDelegate(new StickersAlertInstallDelegate() {
                public void onStickerSetInstalled() {
                    ((ArchivedStickerSetCell) view).setChecked(true);
                }

                public void onStickerSetUninstalled() {
                    ((ArchivedStickerSetCell) view).setChecked(false);
                }
            });
            showDialog(stickersAlert);
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        if (this.sets.isEmpty()) {
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersLoadingRow = -1;
            this.stickersShadowRow = -1;
        } else {
            int i = this.rowCount;
            this.stickersStartRow = i;
            this.stickersEndRow = i + this.sets.size();
            this.rowCount += this.sets.size();
            if (this.endReached) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.stickersShadowRow = i;
                this.stickersLoadingRow = -1;
            } else {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.stickersLoadingRow = i;
                this.stickersShadowRow = -1;
            }
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void getStickers() {
        if (!this.loadingStickers && !this.endReached) {
            long j;
            boolean z = true;
            this.loadingStickers = true;
            EmptyTextProgressView emptyTextProgressView = this.emptyView;
            if (!(emptyTextProgressView == null || this.firstLoaded)) {
                emptyTextProgressView.showProgress();
            }
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
            TL_messages_getArchivedStickers tL_messages_getArchivedStickers = new TL_messages_getArchivedStickers();
            if (this.sets.isEmpty()) {
                j = 0;
            } else {
                ArrayList arrayList = this.sets;
                j = ((StickerSetCovered) arrayList.get(arrayList.size() - 1)).set.f466id;
            }
            tL_messages_getArchivedStickers.offset_id = j;
            tL_messages_getArchivedStickers.limit = 15;
            if (this.currentType != 1) {
                z = false;
            }
            tL_messages_getArchivedStickers.masks = z;
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getArchivedStickers, new C3571-$$Lambda$ArchivedStickersActivity$8wdae9P9JBu4YoQztgGGYjdp1Pw(this)), this.classGuid);
        }
    }

    public /* synthetic */ void lambda$getStickers$2$ArchivedStickersActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1161-$$Lambda$ArchivedStickersActivity$W1mSPGuq-a6awcQIM1Y_7zj88qM(this, tL_error, tLObject));
    }

    public /* synthetic */ void lambda$null$1$ArchivedStickersActivity(TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            TL_messages_archivedStickers tL_messages_archivedStickers = (TL_messages_archivedStickers) tLObject;
            this.sets.addAll(tL_messages_archivedStickers.sets);
            this.endReached = tL_messages_archivedStickers.sets.size() != 15;
            this.loadingStickers = false;
            this.firstLoaded = true;
            EmptyTextProgressView emptyTextProgressView = this.emptyView;
            if (emptyTextProgressView != null) {
                emptyTextProgressView.showTextView();
            }
            updateRows();
        }
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.needReloadArchivedStickers) {
            this.firstLoaded = false;
            this.endReached = false;
            this.sets.clear();
            updateRows();
            EmptyTextProgressView emptyTextProgressView = this.emptyView;
            if (emptyTextProgressView != null) {
                emptyTextProgressView.showProgress();
            }
            getStickers();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[17];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ArchivedStickerSetCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LoadingCell.class, TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[7] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r1[8] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r1[9] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[10] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        r1[11] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle);
        r1[12] = new ThemeDescription(this.listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, null, null, null, Theme.key_progressCircle);
        r1[13] = new ThemeDescription(this.listView, 0, new Class[]{ArchivedStickerSetCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[14] = new ThemeDescription(this.listView, 0, new Class[]{ArchivedStickerSetCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        View view = this.listView;
        Class[] clsArr = new Class[]{ArchivedStickerSetCell.class};
        String[] strArr = new String[1];
        strArr[0] = "checkBox";
        r1[15] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_switchTrack);
        r1[16] = new ThemeDescription(this.listView, 0, new Class[]{ArchivedStickerSetCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        return r1;
    }
}
