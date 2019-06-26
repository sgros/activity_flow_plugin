// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.tgnet.NativeByteBuffer;
import android.view.ViewGroup;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.messenger.MessagesStorage;
import android.content.SharedPreferences$Editor;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import org.telegram.messenger.ClearCacheService;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.MessagesController;
import android.content.DialogInterface;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import android.view.View$OnClickListener;
import org.telegram.ui.Cells.CheckBoxCell;
import android.widget.LinearLayout;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import java.io.File;
import org.telegram.messenger.Utilities;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.ActionBar.BaseFragment;

public class CacheControlActivity extends BaseFragment
{
    private long audioSize;
    private int cacheInfoRow;
    private int cacheRow;
    private long cacheSize;
    private boolean calculating;
    private volatile boolean canceled;
    private boolean[] clear;
    private int databaseInfoRow;
    private int databaseRow;
    private long databaseSize;
    private long documentsSize;
    private int keepMediaInfoRow;
    private int keepMediaRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private long musicSize;
    private long photoSize;
    private int rowCount;
    private long totalSize;
    private long videoSize;
    
    public CacheControlActivity() {
        this.databaseSize = -1L;
        this.cacheSize = -1L;
        this.documentsSize = -1L;
        this.audioSize = -1L;
        this.musicSize = -1L;
        this.photoSize = -1L;
        this.videoSize = -1L;
        this.totalSize = -1L;
        this.clear = new boolean[6];
        this.calculating = true;
        this.canceled = false;
    }
    
    private void cleanupFolders() {
        final AlertDialog alertDialog = new AlertDialog((Context)this.getParentActivity(), 3);
        alertDialog.setCanCacnel(false);
        alertDialog.show();
        Utilities.globalQueue.postRunnable(new _$$Lambda$CacheControlActivity$jBkbYZANDW5o41l52WQZczRyijs(this, alertDialog));
    }
    
    private long getDirectorySize(final File file, final int n) {
        long dirSize;
        final long n2 = dirSize = 0L;
        if (file != null) {
            if (this.canceled) {
                dirSize = n2;
            }
            else if (file.isDirectory()) {
                dirSize = Utilities.getDirSize(file.getAbsolutePath(), n);
            }
            else {
                dirSize = n2;
                if (file.isFile()) {
                    dirSize = 0L + file.length();
                }
            }
        }
        return dirSize;
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("StorageUsage", 2131560832));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    CacheControlActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$CacheControlActivity$5oIUhVifUjQ5reXmB61Qqo1Afzs(this));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.rowCount = 0;
        this.keepMediaRow = this.rowCount++;
        this.keepMediaInfoRow = this.rowCount++;
        this.cacheRow = this.rowCount++;
        this.cacheInfoRow = this.rowCount++;
        this.databaseRow = this.rowCount++;
        this.databaseInfoRow = this.rowCount++;
        this.databaseSize = MessagesStorage.getInstance(super.currentAccount).getDatabaseSize();
        Utilities.globalQueue.postRunnable(new _$$Lambda$CacheControlActivity$2KHbuhNmXFdFJaa7SHWCPv8UB_I(this));
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.canceled = true;
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
            return CacheControlActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n != CacheControlActivity.this.databaseInfoRow && n != CacheControlActivity.this.cacheInfoRow && n != CacheControlActivity.this.keepMediaInfoRow) {
                return 0;
            }
            return 1;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == CacheControlActivity.this.databaseRow || (adapterPosition == CacheControlActivity.this.cacheRow && CacheControlActivity.this.totalSize > 0L) || adapterPosition == CacheControlActivity.this.keepMediaRow;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int int1) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (int1 == CacheControlActivity.this.databaseInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("LocalDatabaseInfo", 2131559774));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    }
                    else if (int1 == CacheControlActivity.this.cacheInfoRow) {
                        textInfoPrivacyCell.setText("");
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else if (int1 == CacheControlActivity.this.keepMediaInfoRow) {
                        textInfoPrivacyCell.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("KeepMediaInfo", 2131559712)));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                }
            }
            else {
                final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                if (int1 == CacheControlActivity.this.databaseRow) {
                    textSettingsCell.setTextAndValue(LocaleController.getString("LocalDatabase", 2131559772), AndroidUtilities.formatFileSize(CacheControlActivity.this.databaseSize), false);
                }
                else if (int1 == CacheControlActivity.this.cacheRow) {
                    if (CacheControlActivity.this.calculating) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("ClearMediaCache", 2131559110), LocaleController.getString("CalculatingSize", 2131558868), false);
                    }
                    else {
                        final String string = LocaleController.getString("ClearMediaCache", 2131559110);
                        String s;
                        if (CacheControlActivity.this.totalSize == 0L) {
                            s = LocaleController.getString("CacheEmpty", 2131558867);
                        }
                        else {
                            s = AndroidUtilities.formatFileSize(CacheControlActivity.this.totalSize);
                        }
                        textSettingsCell.setTextAndValue(string, s, false);
                    }
                }
                else if (int1 == CacheControlActivity.this.keepMediaRow) {
                    int1 = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
                    String s2;
                    if (int1 == 0) {
                        s2 = LocaleController.formatPluralString("Weeks", 1);
                    }
                    else if (int1 == 1) {
                        s2 = LocaleController.formatPluralString("Months", 1);
                    }
                    else if (int1 == 3) {
                        s2 = LocaleController.formatPluralString("Days", 3);
                    }
                    else {
                        s2 = LocaleController.getString("KeepMediaForever", 2131559711);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("KeepMedia", 2131559710), s2, false);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                frameLayout = new TextInfoPrivacyCell(this.mContext);
            }
            else {
                frameLayout = new TextSettingsCell(this.mContext);
                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
}
