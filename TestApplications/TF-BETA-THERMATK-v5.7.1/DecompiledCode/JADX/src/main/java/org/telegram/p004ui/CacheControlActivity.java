package org.telegram.p004ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.io.File;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ClearCacheService;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.Utilities;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.BottomSheet.BottomSheetCell;
import org.telegram.p004ui.ActionBar.BottomSheet.Builder;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.CheckBoxCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;

/* renamed from: org.telegram.ui.CacheControlActivity */
public class CacheControlActivity extends BaseFragment {
    private long audioSize = -1;
    private int cacheInfoRow;
    private int cacheRow;
    private long cacheSize = -1;
    private boolean calculating = true;
    private volatile boolean canceled = false;
    private boolean[] clear = new boolean[6];
    private int databaseInfoRow;
    private int databaseRow;
    private long databaseSize = -1;
    private long documentsSize = -1;
    private int keepMediaInfoRow;
    private int keepMediaRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private long musicSize = -1;
    private long photoSize = -1;
    private int rowCount;
    private long totalSize = -1;
    private long videoSize = -1;

    /* renamed from: org.telegram.ui.CacheControlActivity$1 */
    class C39341 extends ActionBarMenuOnItemClick {
        C39341() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                CacheControlActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.CacheControlActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == CacheControlActivity.this.databaseRow || ((adapterPosition == CacheControlActivity.this.cacheRow && CacheControlActivity.this.totalSize > 0) || adapterPosition == CacheControlActivity.this.keepMediaRow);
        }

        public int getItemCount() {
            return CacheControlActivity.this.rowCount;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textInfoPrivacyCell;
            if (i != 0) {
                textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
            } else {
                textInfoPrivacyCell = new TextSettingsCell(this.mContext);
                textInfoPrivacyCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            return new Holder(textInfoPrivacyCell);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            String str;
            if (itemViewType == 0) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (i == CacheControlActivity.this.databaseRow) {
                    textSettingsCell.setTextAndValue(LocaleController.getString("LocalDatabase", C1067R.string.LocalDatabase), AndroidUtilities.formatFileSize(CacheControlActivity.this.databaseSize), false);
                } else if (i == CacheControlActivity.this.cacheRow) {
                    str = "ClearMediaCache";
                    if (CacheControlActivity.this.calculating) {
                        textSettingsCell.setTextAndValue(LocaleController.getString(str, C1067R.string.ClearMediaCache), LocaleController.getString("CalculatingSize", C1067R.string.CalculatingSize), false);
                    } else {
                        textSettingsCell.setTextAndValue(LocaleController.getString(str, C1067R.string.ClearMediaCache), CacheControlActivity.this.totalSize == 0 ? LocaleController.getString("CacheEmpty", C1067R.string.CacheEmpty) : AndroidUtilities.formatFileSize(CacheControlActivity.this.totalSize), false);
                    }
                } else if (i == CacheControlActivity.this.keepMediaRow) {
                    String formatPluralString;
                    i = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
                    if (i == 0) {
                        formatPluralString = LocaleController.formatPluralString("Weeks", 1);
                    } else if (i == 1) {
                        formatPluralString = LocaleController.formatPluralString("Months", 1);
                    } else if (i == 3) {
                        formatPluralString = LocaleController.formatPluralString("Days", 3);
                    } else {
                        formatPluralString = LocaleController.getString("KeepMediaForever", C1067R.string.KeepMediaForever);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("KeepMedia", C1067R.string.KeepMedia), formatPluralString, false);
                }
            } else if (itemViewType == 1) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                itemViewType = CacheControlActivity.this.databaseInfoRow;
                str = Theme.key_windowBackgroundGrayShadow;
                if (i == itemViewType) {
                    textInfoPrivacyCell.setText(LocaleController.getString("LocalDatabaseInfo", C1067R.string.LocalDatabaseInfo));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, str));
                } else if (i == CacheControlActivity.this.cacheInfoRow) {
                    textInfoPrivacyCell.setText("");
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                } else if (i == CacheControlActivity.this.keepMediaInfoRow) {
                    textInfoPrivacyCell.setText(AndroidUtilities.replaceTags(LocaleController.getString("KeepMediaInfo", C1067R.string.KeepMediaInfo)));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                }
            }
        }

        public int getItemViewType(int i) {
            return (i == CacheControlActivity.this.databaseInfoRow || i == CacheControlActivity.this.cacheInfoRow || i == CacheControlActivity.this.keepMediaInfoRow) ? 1 : 0;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:69:0x025c in {8, 17, 18, 22, 37, 38, 40, 41, 43, 45, 48, 49, 50, 51, 52, 54, 56, 58, 60, 61, 64, 66, 68} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public /* synthetic */ void lambda$null$6$CacheControlActivity(org.telegram.p004ui.ActionBar.AlertDialog r19) {
        /*
        r18 = this;
        r1 = r18;
        r2 = r19;
        r3 = " AND mid != ";
        r0 = r1.currentAccount;	 Catch:{ Exception -> 0x0246 }
        r0 = org.telegram.messenger.MessagesStorage.getInstance(r0);	 Catch:{ Exception -> 0x0246 }
        r4 = r0.getDatabase();	 Catch:{ Exception -> 0x0246 }
        r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0246 }
        r5.<init>();	 Catch:{ Exception -> 0x0246 }
        r0 = "SELECT did FROM dialogs WHERE 1";	 Catch:{ Exception -> 0x0246 }
        r6 = 0;	 Catch:{ Exception -> 0x0246 }
        r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x0246 }
        r0 = r4.queryFinalized(r0, r7);	 Catch:{ Exception -> 0x0246 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0246 }
        r7.<init>();	 Catch:{ Exception -> 0x0246 }
        r7 = r0.next();	 Catch:{ Exception -> 0x0246 }
        r8 = 1;	 Catch:{ Exception -> 0x0246 }
        if (r7 == 0) goto L_0x0040;	 Catch:{ Exception -> 0x0246 }
        r9 = r0.longValue(r6);	 Catch:{ Exception -> 0x0246 }
        r7 = (int) r9;	 Catch:{ Exception -> 0x0246 }
        r11 = 32;	 Catch:{ Exception -> 0x0246 }
        r11 = r9 >> r11;	 Catch:{ Exception -> 0x0246 }
        r12 = (int) r11;	 Catch:{ Exception -> 0x0246 }
        if (r7 == 0) goto L_0x0023;	 Catch:{ Exception -> 0x0246 }
        if (r12 == r8) goto L_0x0023;	 Catch:{ Exception -> 0x0246 }
        r7 = java.lang.Long.valueOf(r9);	 Catch:{ Exception -> 0x0246 }
        r5.add(r7);	 Catch:{ Exception -> 0x0246 }
        goto L_0x0023;	 Catch:{ Exception -> 0x0246 }
        r0.dispose();	 Catch:{ Exception -> 0x0246 }
        r0 = "REPLACE INTO messages_holes VALUES(?, ?, ?)";	 Catch:{ Exception -> 0x0246 }
        r7 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0246 }
        r0 = "REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)";	 Catch:{ Exception -> 0x0246 }
        r9 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0246 }
        r4.beginTransaction();	 Catch:{ Exception -> 0x0246 }
        r10 = 0;	 Catch:{ Exception -> 0x0246 }
        r0 = r5.size();	 Catch:{ Exception -> 0x0246 }
        if (r10 >= r0) goto L_0x0203;
        r0 = r5.get(r10);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r11 = r0;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r11 = (java.lang.Long) r11;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r12 = "SELECT COUNT(mid) FROM messages WHERE uid = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r12);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r12 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.queryFinalized(r0, r12);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r12 = r0.next();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        if (r12 == 0) goto L_0x0082;
        r12 = r0.intValue(r6);	 Catch:{ Exception -> 0x0246 }
        goto L_0x0083;
        r12 = 0;
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = 2;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        if (r12 > r0) goto L_0x008e;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r17 = r5;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = r7;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        goto L_0x01f8;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r12 = "SELECT last_mid_i, last_mid FROM dialogs WHERE did = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r12);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r12 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r12 = r4.queryFinalized(r0, r12);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r12.next();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        if (r0 == 0) goto L_0x01f2;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r13 = r12.longValue(r6);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r16 = r7;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r6 = r12.longValue(r8);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r8 = "SELECT data FROM messages WHERE uid = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r8);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r8 = " AND mid IN (";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r8);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r13);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r8 = ",";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r8);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r6);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r8 = ")";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r8);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r8 = 0;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r15 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r15 = r4.queryFinalized(r0, r15);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r8 = -1;
        r0 = r15.next();	 Catch:{ Exception -> 0x0117, all -> 0x023c }
        if (r0 == 0) goto L_0x0114;
        r17 = r5;
        r5 = 0;
        r0 = r15.byteBufferValue(r5);	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        if (r0 == 0) goto L_0x010d;	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r2 = r0.readInt32(r5);	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r2 = org.telegram.tgnet.TLRPC.Message.TLdeserialize(r0, r2, r5);	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r5 = r1.currentAccount;	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r5 = r5.clientUserId;	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r2.readAttachPath(r0, r5);	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r0.reuse();	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        if (r2 == 0) goto L_0x010d;	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r0 = r2.f461id;	 Catch:{ Exception -> 0x0112, all -> 0x023c }
        r8 = r0;
        r2 = r19;
        r5 = r17;
        goto L_0x00e3;
        r0 = move-exception;
        goto L_0x011a;
        r17 = r5;
        goto L_0x011d;
        r0 = move-exception;
        r17 = r5;
        org.telegram.messenger.FileLog.m30e(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r15.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = "DELETE FROM messages WHERE uid = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r2);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r3);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r13);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r3);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r6);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = "DELETE FROM messages_holes WHERE uid = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r2);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = "DELETE FROM bot_keyboard WHERE uid = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r2);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = "DELETE FROM media_counts_v2 WHERE uid = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r2);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = "DELETE FROM media_v2 WHERE uid = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r2);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.<init>();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = "DELETE FROM media_holes_v2 WHERE uid = ";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r2);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.append(r11);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r1.currentAccount;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = org.telegram.messenger.DataQuery.getInstance(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r5 = r11.longValue();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = 0;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.clearBotKeyboard(r5, r2);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = -1;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        if (r8 == r2) goto L_0x01ef;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r5 = r11.longValue();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = r16;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        org.telegram.messenger.MessagesStorage.createFirstHoles(r5, r2, r9, r8);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        goto L_0x01f5;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = r16;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        goto L_0x01f5;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r17 = r5;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = r7;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r12.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r10 = r10 + 1;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r7 = r2;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r5 = r17;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r6 = 0;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r8 = 1;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = r19;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        goto L_0x0053;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2 = r7;	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r2.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r9.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r4.commitTransaction();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = "PRAGMA journal_size_limit = 0";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = "VACUUM";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = "PRAGMA journal_size_limit = -1";	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r4.executeFast(r0);	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = r0.stepThis();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0.dispose();	 Catch:{ Exception -> 0x0240, all -> 0x023c }
        r0 = new org.telegram.ui.-$$Lambda$CacheControlActivity$Vg0mI-k3yibTxFQ18Xoo27lsi5o;
        r2 = r19;
        r0.<init>(r1, r2);
        goto L_0x024f;
        r0 = move-exception;
        r2 = r19;
        goto L_0x0253;
        r0 = move-exception;
        r2 = r19;
        goto L_0x0247;
        r0 = move-exception;
        goto L_0x0253;
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);	 Catch:{ all -> 0x0244 }
        r0 = new org.telegram.ui.-$$Lambda$CacheControlActivity$Vg0mI-k3yibTxFQ18Xoo27lsi5o;
        r0.<init>(r1, r2);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
        return;
        r3 = new org.telegram.ui.-$$Lambda$CacheControlActivity$Vg0mI-k3yibTxFQ18Xoo27lsi5o;
        r3.<init>(r1, r2);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r3);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.CacheControlActivity.lambda$null$6$CacheControlActivity(org.telegram.ui.ActionBar.AlertDialog):void");
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.keepMediaRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.keepMediaInfoRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.cacheRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.cacheInfoRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.databaseRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.databaseInfoRow = i;
        this.databaseSize = MessagesStorage.getInstance(this.currentAccount).getDatabaseSize();
        Utilities.globalQueue.postRunnable(new C1209-$$Lambda$CacheControlActivity$2KHbuhNmXFdFJaa7SHWCPv8UB-I(this));
        return true;
    }

    public /* synthetic */ void lambda$onFragmentCreate$1$CacheControlActivity() {
        this.cacheSize = getDirectorySize(FileLoader.checkDirectory(4), 0);
        if (!this.canceled) {
            this.photoSize = getDirectorySize(FileLoader.checkDirectory(0), 0);
            if (!this.canceled) {
                this.videoSize = getDirectorySize(FileLoader.checkDirectory(2), 0);
                if (!this.canceled) {
                    this.documentsSize = getDirectorySize(FileLoader.checkDirectory(3), 1);
                    if (!this.canceled) {
                        this.musicSize = getDirectorySize(FileLoader.checkDirectory(3), 2);
                        if (!this.canceled) {
                            this.audioSize = getDirectorySize(FileLoader.checkDirectory(1), 0);
                            this.totalSize = ((((this.cacheSize + this.videoSize) + this.audioSize) + this.photoSize) + this.documentsSize) + this.musicSize;
                            AndroidUtilities.runOnUIThread(new C1216-$$Lambda$CacheControlActivity$rl9mnpe2QS7cCoiKVnOTFMsRWgA(this));
                        }
                    }
                }
            }
        }
    }

    public /* synthetic */ void lambda$null$0$CacheControlActivity() {
        this.calculating = false;
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.canceled = true;
    }

    private long getDirectorySize(File file, int i) {
        if (file == null || this.canceled) {
            return 0;
        }
        if (file.isDirectory()) {
            return Utilities.getDirSize(file.getAbsolutePath(), i);
        }
        if (file.isFile()) {
            return 0 + file.length();
        }
        return 0;
    }

    private void cleanupFolders() {
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        alertDialog.setCanCacnel(false);
        alertDialog.show();
        Utilities.globalQueue.postRunnable(new C1214-$$Lambda$CacheControlActivity$jBkbYZANDW5o41l52WQZczRyijs(this, alertDialog));
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0094 A:{SYNTHETIC} */
    public /* synthetic */ void lambda$cleanupFolders$3$CacheControlActivity(org.telegram.p004ui.ActionBar.AlertDialog r13) {
        /*
        r12 = this;
        r0 = 0;
        r1 = 0;
        r2 = 0;
    L_0x0003:
        r3 = 6;
        if (r1 >= r3) goto L_0x0098;
    L_0x0006:
        r3 = r12.clear;
        r3 = r3[r1];
        r4 = 1;
        if (r3 != 0) goto L_0x000f;
    L_0x000d:
        goto L_0x0094;
    L_0x000f:
        r3 = -1;
        r5 = 4;
        r6 = 2;
        r7 = 3;
        if (r1 != 0) goto L_0x0018;
    L_0x0015:
        r8 = 0;
    L_0x0016:
        r9 = 0;
        goto L_0x0031;
    L_0x0018:
        if (r1 != r4) goto L_0x001c;
    L_0x001a:
        r8 = 2;
        goto L_0x0016;
    L_0x001c:
        if (r1 != r6) goto L_0x0021;
    L_0x001e:
        r8 = 3;
        r9 = 1;
        goto L_0x0031;
    L_0x0021:
        if (r1 != r7) goto L_0x0026;
    L_0x0023:
        r8 = 3;
        r9 = 2;
        goto L_0x0031;
    L_0x0026:
        if (r1 != r5) goto L_0x002a;
    L_0x0028:
        r8 = 1;
        goto L_0x0016;
    L_0x002a:
        r8 = 5;
        if (r1 != r8) goto L_0x002f;
    L_0x002d:
        r8 = 4;
        goto L_0x0016;
    L_0x002f:
        r8 = -1;
        goto L_0x0016;
    L_0x0031:
        if (r8 != r3) goto L_0x0034;
    L_0x0033:
        goto L_0x0094;
    L_0x0034:
        r3 = org.telegram.messenger.FileLoader.checkDirectory(r8);
        if (r3 == 0) goto L_0x0046;
    L_0x003a:
        r3 = r3.getAbsolutePath();
        r10 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        org.telegram.messenger.Utilities.clearDir(r3, r9, r10);
    L_0x0046:
        if (r8 != r5) goto L_0x0054;
    L_0x0048:
        r2 = org.telegram.messenger.FileLoader.checkDirectory(r5);
        r2 = r12.getDirectorySize(r2, r9);
        r12.cacheSize = r2;
    L_0x0052:
        r2 = 1;
        goto L_0x0094;
    L_0x0054:
        if (r8 != r4) goto L_0x0061;
    L_0x0056:
        r3 = org.telegram.messenger.FileLoader.checkDirectory(r4);
        r3 = r12.getDirectorySize(r3, r9);
        r12.audioSize = r3;
        goto L_0x0094;
    L_0x0061:
        if (r8 != r7) goto L_0x007b;
    L_0x0063:
        if (r9 != r4) goto L_0x0070;
    L_0x0065:
        r3 = org.telegram.messenger.FileLoader.checkDirectory(r7);
        r3 = r12.getDirectorySize(r3, r9);
        r12.documentsSize = r3;
        goto L_0x0094;
    L_0x0070:
        r3 = org.telegram.messenger.FileLoader.checkDirectory(r7);
        r3 = r12.getDirectorySize(r3, r9);
        r12.musicSize = r3;
        goto L_0x0094;
    L_0x007b:
        if (r8 != 0) goto L_0x0088;
    L_0x007d:
        r2 = org.telegram.messenger.FileLoader.checkDirectory(r0);
        r2 = r12.getDirectorySize(r2, r9);
        r12.photoSize = r2;
        goto L_0x0052;
    L_0x0088:
        if (r8 != r6) goto L_0x0094;
    L_0x008a:
        r3 = org.telegram.messenger.FileLoader.checkDirectory(r6);
        r3 = r12.getDirectorySize(r3, r9);
        r12.videoSize = r3;
    L_0x0094:
        r1 = r1 + 1;
        goto L_0x0003;
    L_0x0098:
        r0 = r12.cacheSize;
        r3 = r12.videoSize;
        r0 = r0 + r3;
        r3 = r12.audioSize;
        r0 = r0 + r3;
        r3 = r12.photoSize;
        r0 = r0 + r3;
        r3 = r12.documentsSize;
        r0 = r0 + r3;
        r3 = r12.musicSize;
        r0 = r0 + r3;
        r12.totalSize = r0;
        r0 = new org.telegram.ui.-$$Lambda$CacheControlActivity$Lvpblwm67qF5Tz21D4W9HaTE1WA;
        r0.<init>(r12, r2, r13);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.CacheControlActivity.lambda$cleanupFolders$3$CacheControlActivity(org.telegram.ui.ActionBar.AlertDialog):void");
    }

    public /* synthetic */ void lambda$null$2$CacheControlActivity(boolean z, AlertDialog alertDialog) {
        if (z) {
            ImageLoader.getInstance().clearMemory();
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("StorageUsage", C1067R.string.StorageUsage));
        this.actionBar.setActionBarMenuOnItemClick(new C39341());
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setVerticalScrollBarEnabled(false);
        RecyclerListView recyclerListView = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new C3586-$$Lambda$CacheControlActivity$5oIUhVifUjQ5reXmB61Qqo1Afzs(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$10$CacheControlActivity(View view, int i) {
        int i2 = i;
        if (getParentActivity() != null) {
            int i3 = 2;
            int i4 = 4;
            Builder builder;
            if (i2 == this.keepMediaRow) {
                builder = new Builder(getParentActivity());
                builder.setItems(new CharSequence[]{LocaleController.formatPluralString("Days", 3), LocaleController.formatPluralString("Weeks", 1), LocaleController.formatPluralString("Months", 1), LocaleController.getString("KeepMediaForever", C1067R.string.KeepMediaForever)}, new C1213-$$Lambda$CacheControlActivity$Z3kCWEOqHs90j5WRi-5avKV3MH4(this));
                showDialog(builder.create());
            } else if (i2 == this.databaseRow) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                builder2.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
                builder2.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                builder2.setMessage(LocaleController.getString("LocalDatabaseClear", C1067R.string.LocalDatabaseClear));
                builder2.setPositiveButton(LocaleController.getString("CacheClear", C1067R.string.CacheClear), new C1211-$$Lambda$CacheControlActivity$OxPoOxpLG_g1G1jasfpkphw5fag(this));
                showDialog(builder2.create());
            } else if (i2 == this.cacheRow && this.totalSize > 0 && getParentActivity() != null) {
                builder = new Builder(getParentActivity());
                builder.setApplyTopPadding(false);
                builder.setApplyBottomPadding(false);
                LinearLayout linearLayout = new LinearLayout(getParentActivity());
                linearLayout.setOrientation(1);
                int i5 = 0;
                while (i5 < 6) {
                    CharSequence string;
                    long j;
                    if (i5 == 0) {
                        long j2 = this.photoSize;
                        string = LocaleController.getString("LocalPhotoCache", C1067R.string.LocalPhotoCache);
                        j = j2;
                    } else if (i5 == 1) {
                        j = this.videoSize;
                        string = LocaleController.getString("LocalVideoCache", C1067R.string.LocalVideoCache);
                    } else if (i5 == i3) {
                        j = this.documentsSize;
                        string = LocaleController.getString("LocalDocumentCache", C1067R.string.LocalDocumentCache);
                    } else if (i5 == 3) {
                        j = this.musicSize;
                        string = LocaleController.getString("LocalMusicCache", C1067R.string.LocalMusicCache);
                    } else if (i5 == i4) {
                        j = this.audioSize;
                        string = LocaleController.getString("LocalAudioCache", C1067R.string.LocalAudioCache);
                    } else if (i5 == 5) {
                        j = this.cacheSize;
                        string = LocaleController.getString("LocalCache", C1067R.string.LocalCache);
                    } else {
                        string = null;
                        j = 0;
                    }
                    if (j > 0) {
                        this.clear[i5] = true;
                        CheckBoxCell checkBoxCell = new CheckBoxCell(getParentActivity(), 1, 21);
                        checkBoxCell.setTag(Integer.valueOf(i5));
                        checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        linearLayout.addView(checkBoxCell, LayoutHelper.createLinear(-1, 50));
                        checkBoxCell.setText(string, AndroidUtilities.formatFileSize(j), true, true);
                        checkBoxCell.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                        checkBoxCell.setOnClickListener(new C1215-$$Lambda$CacheControlActivity$nToR5mmUsDX6DDZcwMPXsRZbyZs(this));
                    } else {
                        this.clear[i5] = false;
                    }
                    i5++;
                    i3 = 2;
                    i4 = 4;
                }
                BottomSheetCell bottomSheetCell = new BottomSheetCell(getParentActivity(), 1);
                bottomSheetCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                bottomSheetCell.setTextAndIcon(LocaleController.getString("ClearMediaCache", C1067R.string.ClearMediaCache).toUpperCase(), 0);
                bottomSheetCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText));
                bottomSheetCell.setOnClickListener(new C1217-$$Lambda$CacheControlActivity$xjCzZWiHEv1HCRKFRUbJfrKMn_g(this));
                linearLayout.addView(bottomSheetCell, LayoutHelper.createLinear(-1, 50));
                builder.setCustomView(linearLayout);
                showDialog(builder.create());
            }
        }
    }

    public /* synthetic */ void lambda$null$4$CacheControlActivity(DialogInterface dialogInterface, int i) {
        Editor edit = MessagesController.getGlobalMainSettings().edit();
        String str = "keep_media";
        if (i == 0) {
            edit.putInt(str, 3);
        } else if (i == 1) {
            edit.putInt(str, 0);
        } else if (i == 2) {
            edit.putInt(str, 1);
        } else if (i == 3) {
            edit.putInt(str, 2);
        }
        edit.commit();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 1, new Intent(ApplicationLoader.applicationContext, ClearCacheService.class), 0);
        AlarmManager alarmManager = (AlarmManager) ApplicationLoader.applicationContext.getSystemService("alarm");
        alarmManager.cancel(service);
        if (i != 3) {
            alarmManager.setInexactRepeating(0, 0, 86400000, service);
        }
    }

    public /* synthetic */ void lambda$null$7$CacheControlActivity(DialogInterface dialogInterface, int i) {
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        alertDialog.setCanCacnel(false);
        alertDialog.show();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new C1208-$$Lambda$CacheControlActivity$2BeyhPeIil2krQM27Ksn4kqSTlk(this, alertDialog));
    }

    public /* synthetic */ void lambda$null$5$CacheControlActivity(AlertDialog alertDialog) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (this.listAdapter != null) {
            this.databaseSize = MessagesStorage.getInstance(this.currentAccount).getDatabaseSize();
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public /* synthetic */ void lambda$null$8$CacheControlActivity(View view) {
        CheckBoxCell checkBoxCell = (CheckBoxCell) view;
        int intValue = ((Integer) checkBoxCell.getTag()).intValue();
        boolean[] zArr = this.clear;
        zArr[intValue] = zArr[intValue] ^ 1;
        checkBoxCell.setChecked(zArr[intValue], true);
    }

    public /* synthetic */ void lambda$null$9$CacheControlActivity(View view) {
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        cleanupFolders();
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[12];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r1[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        View view = this.listView;
        Class[] clsArr = new Class[]{TextSettingsCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        r1[8] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[9] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        r1[10] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[11] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        return r1;
    }
}