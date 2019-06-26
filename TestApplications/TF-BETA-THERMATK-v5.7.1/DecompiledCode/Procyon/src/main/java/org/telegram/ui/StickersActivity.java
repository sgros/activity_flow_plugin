// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.Canvas;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import org.telegram.ui.Components.URLSpanNoUnderline;
import android.text.SpannableStringBuilder;
import android.app.Activity;
import android.widget.Toast;
import android.content.ClipData;
import org.telegram.messenger.ApplicationLoader;
import android.content.ClipboardManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import java.util.Locale;
import android.content.Intent;
import org.telegram.messenger.SharedConfig;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.app.Dialog;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.StickerSetCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.DataQuery;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class StickersActivity extends BaseFragment implements NotificationCenterDelegate
{
    private int archivedInfoRow;
    private int archivedRow;
    private int currentType;
    private int featuredInfoRow;
    private int featuredRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int masksInfoRow;
    private int masksRow;
    private boolean needReorder;
    private int rowCount;
    private int stickersEndRow;
    private int stickersShadowRow;
    private int stickersStartRow;
    private int suggestInfoRow;
    private int suggestRow;
    
    public StickersActivity(final int currentType) {
        this.currentType = currentType;
    }
    
    private void sendReorder() {
        if (!this.needReorder) {
            return;
        }
        DataQuery.getInstance(super.currentAccount).calcNewHash(this.currentType);
        this.needReorder = false;
        final TLRPC.TL_messages_reorderStickerSets tl_messages_reorderStickerSets = new TLRPC.TL_messages_reorderStickerSets();
        tl_messages_reorderStickerSets.masks = (this.currentType == 1);
        final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(super.currentAccount).getStickerSets(this.currentType);
        for (int i = 0; i < stickerSets.size(); ++i) {
            tl_messages_reorderStickerSets.order.add(stickerSets.get(i).set.id);
        }
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_reorderStickerSets, (RequestDelegate)_$$Lambda$StickersActivity$eNJ1JVvMbECvyP_jXUZlvL1o67I.INSTANCE);
        NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, this.currentType);
    }
    
    private void updateRows() {
        this.rowCount = 0;
        if (this.currentType == 0) {
            this.suggestRow = this.rowCount++;
            this.featuredRow = this.rowCount++;
            this.featuredInfoRow = this.rowCount++;
            this.masksRow = this.rowCount++;
            this.masksInfoRow = this.rowCount++;
        }
        else {
            this.featuredRow = -1;
            this.featuredInfoRow = -1;
            this.masksRow = -1;
            this.masksInfoRow = -1;
        }
        if (DataQuery.getInstance(super.currentAccount).getArchivedStickersCount(this.currentType) != 0) {
            this.archivedRow = this.rowCount++;
            this.archivedInfoRow = this.rowCount++;
        }
        else {
            this.archivedRow = -1;
            this.archivedInfoRow = -1;
        }
        final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(super.currentAccount).getStickerSets(this.currentType);
        if (!stickerSets.isEmpty()) {
            final int rowCount = this.rowCount;
            this.stickersStartRow = rowCount;
            this.stickersEndRow = rowCount + stickerSets.size();
            this.rowCount += stickerSets.size();
            this.stickersShadowRow = this.rowCount++;
        }
        else {
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersShadowRow = -1;
        }
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == 0) {
            super.actionBar.setTitle(LocaleController.getString("StickersName", 2131560810));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("Masks", 2131559809));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    StickersActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.listView = new RecyclerListView(context)).setFocusable(true);
        this.listView.setTag((Object)7);
        (this.layoutManager = new LinearLayoutManager(context)).setOrientation(1);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)this.layoutManager);
        new ItemTouchHelper((ItemTouchHelper.Callback)new TouchHelperCallback()).attachToRecyclerView(this.listView);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$StickersActivity$52bQnWSJW3OnfBam1s3y37TWiNA(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.stickersDidLoad) {
            if ((int)array[0] == this.currentType) {
                this.updateRows();
            }
        }
        else if (n == NotificationCenter.featuredStickersDidLoad) {
            final ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                ((RecyclerView.Adapter)listAdapter).notifyItemChanged(0);
            }
        }
        else if (n == NotificationCenter.archivedStickersCountDidLoad && (int)array[0] == this.currentType) {
            this.updateRows();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { StickerSetCell.class, TextSettingsCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteLinkText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { StickerSetCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { StickerSetCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[] { StickerSetCell.class }, new String[] { "optionsButton" }, null, null, null, "stickers_menuSelector"), new ThemeDescription((View)this.listView, 0, new Class[] { StickerSetCell.class }, new String[] { "optionsButton" }, null, null, null, "stickers_menu") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DataQuery.getInstance(super.currentAccount).checkStickers(this.currentType);
        if (this.currentType == 0) {
            DataQuery.getInstance(super.currentAccount).checkFeaturedStickers();
        }
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.archivedStickersCountDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
        this.updateRows();
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.archivedStickersCountDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
        this.sendReorder();
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
        
        private void processSelectionOption(int n, final TLRPC.TL_messages_stickerSet set) {
            if (n == 0) {
                final DataQuery instance = DataQuery.getInstance(StickersActivity.this.currentAccount);
                final Activity parentActivity = StickersActivity.this.getParentActivity();
                final TLRPC.StickerSet set2 = set.set;
                if (!set2.archived) {
                    n = 1;
                }
                else {
                    n = 2;
                }
                instance.removeStickersSet((Context)parentActivity, set2, n, StickersActivity.this, true);
            }
            else if (n == 1) {
                DataQuery.getInstance(StickersActivity.this.currentAccount).removeStickersSet((Context)StickersActivity.this.getParentActivity(), set.set, 0, StickersActivity.this, true);
            }
            else if (n == 2) {
                try {
                    final Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    final Locale us = Locale.US;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("https://");
                    sb.append(MessagesController.getInstance(StickersActivity.this.currentAccount).linkPrefix);
                    sb.append("/addstickers/%s");
                    intent.putExtra("android.intent.extra.TEXT", String.format(us, sb.toString(), set.set.short_name));
                    StickersActivity.this.getParentActivity().startActivityForResult(Intent.createChooser(intent, (CharSequence)LocaleController.getString("StickersShare", 2131560813)), 500);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            else if (n == 3) {
                try {
                    final ClipboardManager clipboardManager = (ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard");
                    final Locale us2 = Locale.US;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("https://");
                    sb2.append(MessagesController.getInstance(StickersActivity.this.currentAccount).linkPrefix);
                    sb2.append("/addstickers/%s");
                    clipboardManager.setPrimaryClip(ClipData.newPlainText((CharSequence)"label", (CharSequence)String.format(us2, sb2.toString(), set.set.short_name)));
                    Toast.makeText((Context)StickersActivity.this.getParentActivity(), (CharSequence)LocaleController.getString("LinkCopied", 2131559751), 0).show();
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
        }
        
        @Override
        public int getItemCount() {
            return StickersActivity.this.rowCount;
        }
        
        @Override
        public long getItemId(final int n) {
            if (n >= StickersActivity.this.stickersStartRow && n < StickersActivity.this.stickersEndRow) {
                return DataQuery.getInstance(StickersActivity.this.currentAccount).getStickerSets(StickersActivity.this.currentType).get(n - StickersActivity.this.stickersStartRow).set.id;
            }
            if (n != StickersActivity.this.suggestRow && n != StickersActivity.this.suggestInfoRow && n != StickersActivity.this.archivedRow && n != StickersActivity.this.archivedInfoRow && n != StickersActivity.this.featuredRow && n != StickersActivity.this.featuredInfoRow && n != StickersActivity.this.masksRow && n != StickersActivity.this.masksInfoRow) {
                return n;
            }
            return -2147483648L;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n >= StickersActivity.this.stickersStartRow && n < StickersActivity.this.stickersEndRow) {
                return 0;
            }
            if (n == StickersActivity.this.featuredInfoRow || n == StickersActivity.this.archivedInfoRow || n == StickersActivity.this.masksInfoRow) {
                return 1;
            }
            if (n == StickersActivity.this.featuredRow || n == StickersActivity.this.archivedRow || n == StickersActivity.this.masksRow || n == StickersActivity.this.suggestRow) {
                return 2;
            }
            if (n != StickersActivity.this.stickersShadowRow && n != StickersActivity.this.suggestInfoRow) {
                return 0;
            }
            return 3;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 2;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = false;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType == 3) {
                            if (n == StickersActivity.this.stickersShadowRow) {
                                viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                            }
                            else if (n == StickersActivity.this.suggestInfoRow) {
                                viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                            }
                        }
                    }
                    else if (n == StickersActivity.this.featuredRow) {
                        n = DataQuery.getInstance(StickersActivity.this.currentAccount).getUnreadStickerSets().size();
                        final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                        final String string = LocaleController.getString("FeaturedStickers", 2131559479);
                        String format;
                        if (n != 0) {
                            format = String.format("%d", n);
                        }
                        else {
                            format = "";
                        }
                        textSettingsCell.setTextAndValue(string, format, false);
                    }
                    else if (n == StickersActivity.this.archivedRow) {
                        if (StickersActivity.this.currentType == 0) {
                            ((TextSettingsCell)viewHolder.itemView).setText(LocaleController.getString("ArchivedStickers", 2131558659), false);
                        }
                        else {
                            ((TextSettingsCell)viewHolder.itemView).setText(LocaleController.getString("ArchivedMasks", 2131558654), false);
                        }
                    }
                    else if (n == StickersActivity.this.masksRow) {
                        ((TextSettingsCell)viewHolder.itemView).setText(LocaleController.getString("Masks", 2131559809), false);
                    }
                    else if (n == StickersActivity.this.suggestRow) {
                        n = SharedConfig.suggestStickers;
                        String s;
                        if (n != 0) {
                            if (n != 1) {
                                s = LocaleController.getString("SuggestStickersNone", 2131560846);
                            }
                            else {
                                s = LocaleController.getString("SuggestStickersInstalled", 2131560845);
                            }
                        }
                        else {
                            s = LocaleController.getString("SuggestStickersAll", 2131560844);
                        }
                        ((TextSettingsCell)viewHolder.itemView).setTextAndValue(LocaleController.getString("SuggestStickers", 2131560843), s, true);
                    }
                }
                else if (n == StickersActivity.this.featuredInfoRow) {
                    final String string2 = LocaleController.getString("FeaturedStickersInfo", 2131559480);
                    n = string2.indexOf("@stickers");
                    if (n != -1) {
                        try {
                            final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)string2);
                            text.setSpan((Object)new URLSpanNoUnderline("@stickers") {
                                @Override
                                public void onClick(final View view) {
                                    MessagesController.getInstance(StickersActivity.this.currentAccount).openByUserName("stickers", StickersActivity.this, 1);
                                }
                            }, n, n + 9, 18);
                            ((TextInfoPrivacyCell)viewHolder.itemView).setText((CharSequence)text);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                            ((TextInfoPrivacyCell)viewHolder.itemView).setText(string2);
                        }
                    }
                    else {
                        ((TextInfoPrivacyCell)viewHolder.itemView).setText(string2);
                    }
                }
                else if (n == StickersActivity.this.archivedInfoRow) {
                    if (StickersActivity.this.currentType == 0) {
                        ((TextInfoPrivacyCell)viewHolder.itemView).setText(LocaleController.getString("ArchivedStickersInfo", 2131558663));
                    }
                    else {
                        ((TextInfoPrivacyCell)viewHolder.itemView).setText(LocaleController.getString("ArchivedMasksInfo", 2131558658));
                    }
                }
                else if (n == StickersActivity.this.masksInfoRow) {
                    ((TextInfoPrivacyCell)viewHolder.itemView).setText(LocaleController.getString("MasksInfo", 2131559816));
                }
            }
            else {
                final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(StickersActivity.this.currentAccount).getStickerSets(StickersActivity.this.currentType);
                n -= StickersActivity.this.stickersStartRow;
                final StickerSetCell stickerSetCell = (StickerSetCell)viewHolder.itemView;
                final TLRPC.TL_messages_stickerSet set = stickerSets.get(n);
                if (n != stickerSets.size() - 1) {
                    b = true;
                }
                stickerSetCell.setStickersSet(set, b);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            o = null;
                        }
                        else {
                            o = new ShadowSectionCell(this.mContext);
                        }
                    }
                    else {
                        o = new TextSettingsCell(this.mContext);
                        ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    o = new TextInfoPrivacyCell(this.mContext);
                    ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                }
            }
            else {
                o = new StickerSetCell(this.mContext, 1);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                ((StickerSetCell)o).setOnOptionsClick((View$OnClickListener)new _$$Lambda$StickersActivity$ListAdapter$xPbh5swVjrfEVlhBcsKoz2sSeSI(this));
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
        
        public void swapElements(final int n, final int n2) {
            if (n != n2) {
                StickersActivity.this.needReorder = true;
            }
            final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(StickersActivity.this.currentAccount).getStickerSets(StickersActivity.this.currentType);
            final TLRPC.TL_messages_stickerSet element = stickerSets.get(n - StickersActivity.this.stickersStartRow);
            stickerSets.set(n - StickersActivity.this.stickersStartRow, stickerSets.get(n2 - StickersActivity.this.stickersStartRow));
            stickerSets.set(n2 - StickersActivity.this.stickersStartRow, element);
            this.notifyItemMoved(n, n2);
        }
    }
    
    public class TouchHelperCallback extends Callback
    {
        @Override
        public void clearView(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }
        
        @Override
        public int getMovementFlags(final RecyclerView recyclerView, final ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 0) {
                return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
            }
            return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
        }
        
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
        
        @Override
        public void onChildDraw(final Canvas canvas, final RecyclerView recyclerView, final ViewHolder viewHolder, final float n, final float n2, final int n3, final boolean b) {
            super.onChildDraw(canvas, recyclerView, viewHolder, n, n2, n3, b);
        }
        
        @Override
        public boolean onMove(final RecyclerView recyclerView, final ViewHolder viewHolder, final ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
                return false;
            }
            StickersActivity.this.listAdapter.swapElements(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }
        
        @Override
        public void onSelectedChanged(final ViewHolder viewHolder, final int n) {
            if (n != 0) {
                StickersActivity.this.listView.cancelClickRunnables(false);
                viewHolder.itemView.setPressed(true);
            }
            super.onSelectedChanged(viewHolder, n);
        }
        
        @Override
        public void onSwiped(final ViewHolder viewHolder, final int n) {
        }
    }
}
