package org.telegram.p004ui;

import android.content.Context;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.FeaturedStickerSetCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.p004ui.Components.StickersAlert;
import org.telegram.p004ui.Components.StickersAlert.StickersAlertInstallDelegate;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.StickerSetCovered;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetShortName;

/* renamed from: org.telegram.ui.FeaturedStickersActivity */
public class FeaturedStickersActivity extends BaseFragment implements NotificationCenterDelegate {
    private LongSparseArray<StickerSetCovered> installingStickerSets = new LongSparseArray();
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int rowCount;
    private int stickersEndRow;
    private int stickersShadowRow;
    private int stickersStartRow;
    private ArrayList<Long> unreadStickers = null;

    /* renamed from: org.telegram.ui.FeaturedStickersActivity$1 */
    class C41841 extends ActionBarMenuOnItemClick {
        C41841() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                FeaturedStickersActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.FeaturedStickersActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return FeaturedStickersActivity.this.rowCount;
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (getItemViewType(i) == 0) {
                ArrayList featuredStickerSets = DataQuery.getInstance(FeaturedStickersActivity.this.currentAccount).getFeaturedStickerSets();
                FeaturedStickerSetCell featuredStickerSetCell = (FeaturedStickerSetCell) viewHolder.itemView;
                featuredStickerSetCell.setTag(Integer.valueOf(i));
                StickerSetCovered stickerSetCovered = (StickerSetCovered) featuredStickerSets.get(i);
                boolean z = true;
                boolean z2 = i != featuredStickerSets.size() - 1;
                boolean z3 = FeaturedStickersActivity.this.unreadStickers != null && FeaturedStickersActivity.this.unreadStickers.contains(Long.valueOf(stickerSetCovered.set.f466id));
                featuredStickerSetCell.setStickersSet(stickerSetCovered, z2, z3);
                if (FeaturedStickersActivity.this.installingStickerSets.indexOfKey(stickerSetCovered.set.f466id) < 0) {
                    z = false;
                }
                if (z && featuredStickerSetCell.isInstalled()) {
                    FeaturedStickersActivity.this.installingStickerSets.remove(stickerSetCovered.set.f466id);
                    featuredStickerSetCell.setDrawProgress(false);
                    z = false;
                }
                featuredStickerSetCell.setDrawProgress(z);
            }
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        public /* synthetic */ void lambda$onCreateViewHolder$0$FeaturedStickersActivity$ListAdapter(View view) {
            FeaturedStickerSetCell featuredStickerSetCell = (FeaturedStickerSetCell) view.getParent();
            StickerSetCovered stickerSet = featuredStickerSetCell.getStickerSet();
            if (FeaturedStickersActivity.this.installingStickerSets.indexOfKey(stickerSet.set.f466id) < 0) {
                FeaturedStickersActivity.this.installingStickerSets.put(stickerSet.set.f466id, stickerSet);
                DataQuery.getInstance(FeaturedStickersActivity.this.currentAccount).removeStickersSet(FeaturedStickersActivity.this.getParentActivity(), stickerSet.set, 2, FeaturedStickersActivity.this, false);
                featuredStickerSetCell.setDrawProgress(true);
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View featuredStickerSetCell;
            if (i == 0) {
                featuredStickerSetCell = new FeaturedStickerSetCell(this.mContext);
                featuredStickerSetCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                featuredStickerSetCell.setAddOnClickListener(new C1523x75729519(this));
            } else if (i != 1) {
                featuredStickerSetCell = null;
            } else {
                featuredStickerSetCell = new TextInfoPrivacyCell(this.mContext);
                featuredStickerSetCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            }
            featuredStickerSetCell.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(featuredStickerSetCell);
        }

        public int getItemViewType(int i) {
            if ((i < FeaturedStickersActivity.this.stickersStartRow || i >= FeaturedStickersActivity.this.stickersEndRow) && i == FeaturedStickersActivity.this.stickersShadowRow) {
                return 1;
            }
            return 0;
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DataQuery.getInstance(this.currentAccount).checkFeaturedStickers();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        ArrayList unreadStickerSets = DataQuery.getInstance(this.currentAccount).getUnreadStickerSets();
        if (unreadStickerSets != null) {
            this.unreadStickers = new ArrayList(unreadStickerSets);
        }
        updateRows();
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("FeaturedStickers", C1067R.string.FeaturedStickers));
        this.actionBar.setActionBarMenuOnItemClick(new C41841());
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setFocusable(true);
        this.listView.setTag(Integer.valueOf(14));
        this.layoutManager = new LinearLayoutManager(context) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager.setOrientation(1);
        this.listView.setLayoutManager(this.layoutManager);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new C3691-$$Lambda$FeaturedStickersActivity$HF_uvC2bkOnxNGqLkTScu_kNf5M(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$0$FeaturedStickersActivity(final View view, int i) {
        if (i >= this.stickersStartRow && i < this.stickersEndRow && getParentActivity() != null) {
            InputStickerSet tL_inputStickerSetID;
            final StickerSetCovered stickerSetCovered = (StickerSetCovered) DataQuery.getInstance(this.currentAccount).getFeaturedStickerSets().get(i);
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
                public void onStickerSetUninstalled() {
                }

                public void onStickerSetInstalled() {
                    ((FeaturedStickerSetCell) view).setDrawProgress(true);
                    LongSparseArray access$300 = FeaturedStickersActivity.this.installingStickerSets;
                    StickerSetCovered stickerSetCovered = stickerSetCovered;
                    access$300.put(stickerSetCovered.set.f466id, stickerSetCovered);
                }
            });
            showDialog(stickersAlert);
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.featuredStickersDidLoad) {
            if (this.unreadStickers == null) {
                this.unreadStickers = DataQuery.getInstance(this.currentAccount).getUnreadStickerSets();
            }
            updateRows();
        } else if (i == NotificationCenter.stickersDidLoad) {
            updateVisibleTrendingSets();
        }
    }

    private void updateVisibleTrendingSets() {
        LinearLayoutManager linearLayoutManager = this.layoutManager;
        if (linearLayoutManager != null) {
            int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            if (findFirstVisibleItemPosition != -1) {
                int findLastVisibleItemPosition = this.layoutManager.findLastVisibleItemPosition();
                if (findLastVisibleItemPosition != -1) {
                    this.listAdapter.notifyItemRangeChanged(findFirstVisibleItemPosition, (findLastVisibleItemPosition - findFirstVisibleItemPosition) + 1);
                }
            }
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        ArrayList featuredStickerSets = DataQuery.getInstance(this.currentAccount).getFeaturedStickerSets();
        if (featuredStickerSets.isEmpty()) {
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersShadowRow = -1;
        } else {
            int i = this.rowCount;
            this.stickersStartRow = i;
            this.stickersEndRow = i + featuredStickerSets.size();
            this.rowCount += featuredStickerSets.size();
            int i2 = this.rowCount;
            this.rowCount = i2 + 1;
            this.stickersShadowRow = i2;
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        DataQuery.getInstance(this.currentAccount).markFaturedStickersAsRead(true);
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[17];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{FeaturedStickerSetCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r1[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r1[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[10] = new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"progressPaint"}, null, null, null, Theme.key_featuredStickers_buttonProgress);
        r1[11] = new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[12] = new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        View view = this.listView;
        Class[] clsArr = new Class[]{FeaturedStickerSetCell.class};
        String[] strArr = new String[1];
        strArr[0] = "addButton";
        r1[13] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_featuredStickers_buttonText);
        r1[14] = new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"checkImage"}, null, null, null, Theme.key_featuredStickers_addedIcon);
        r1[15] = new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{FeaturedStickerSetCell.class}, new String[]{"addButton"}, null, null, null, Theme.key_featuredStickers_addButton);
        r1[16] = new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{FeaturedStickerSetCell.class}, new String[]{"addButton"}, null, null, null, Theme.key_featuredStickers_addButtonPressed);
        return r1;
    }
}
