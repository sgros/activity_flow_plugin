package org.mozilla.focus.provider;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper;
import android.arch.persistence.p000db.SupportSQLiteQueryBuilder;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import org.mozilla.focus.provider.HistoryContract.BrowsingHistory;
import org.mozilla.focus.utils.ProviderUtils;
import org.mozilla.rocket.persistance.History.HistoryDatabase;

public class HistoryProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);
    private SupportSQLiteOpenHelper mDbHelper;

    static {
        sUriMatcher.addURI("org.mozilla.rocket.provider.historyprovider", "browsing_history", 1);
    }

    public boolean onCreate() {
        this.mDbHelper = HistoryDatabase.getInstance(getContext()).getOpenHelper();
        return true;
    }

    public int delete(Uri uri, String str, String[] strArr) {
        SupportSQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (sUriMatcher.match(uri) == 1) {
            int delete = writableDatabase.delete("browsing_history", str, strArr);
            if (delete > 0) {
                notifyBrowsingHistoryChange();
            }
            return delete;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public String getType(Uri uri) {
        if (sUriMatcher.match(uri) == 1) {
            return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.historyprovider.browsinghistory";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        SupportSQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (sUriMatcher.match(uri) == 1) {
            long insertWithUrlUnique = insertWithUrlUnique(writableDatabase, new ContentValues(contentValues));
            if (insertWithUrlUnique < 0) {
                return null;
            }
            notifyBrowsingHistoryChange();
            return ContentUris.withAppendedId(uri, insertWithUrlUnique);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (sUriMatcher.match(uri) == 1) {
            return this.mDbHelper.getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("browsing_history").columns(strArr).selection(str, strArr2).orderBy(str2).limit(ProviderUtils.getLimitParam(uri.getQueryParameter("offset"), uri.getQueryParameter("limit"))).create());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        if (sUriMatcher.match(uri) == 1) {
            int update = this.mDbHelper.getWritableDatabase().update("browsing_history", 2, contentValues, str, strArr);
            if (update > 0) {
                notifyBrowsingHistoryChange();
            }
            return update;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0098  */
    private long insertWithUrlUnique(android.arch.persistence.p000db.SupportSQLiteDatabase r16, android.content.ContentValues r17) {
        /*
        r15 = this;
        r0 = r16;
        r4 = r17;
        r1 = 0;
        r2 = "browsing_history";
        r2 = android.arch.persistence.p000db.SupportSQLiteQueryBuilder.builder(r2);	 Catch:{ all -> 0x0094 }
        r2 = r2.columns(r1);	 Catch:{ all -> 0x0094 }
        r3 = "url = ?";
        r5 = 1;
        r6 = new java.lang.String[r5];	 Catch:{ all -> 0x0094 }
        r7 = "url";
        r7 = r4.getAsString(r7);	 Catch:{ all -> 0x0094 }
        r8 = 0;
        r6[r8] = r7;	 Catch:{ all -> 0x0094 }
        r2 = r2.selection(r3, r6);	 Catch:{ all -> 0x0094 }
        r2 = r2.groupBy(r1);	 Catch:{ all -> 0x0094 }
        r2 = r2.having(r1);	 Catch:{ all -> 0x0094 }
        r2 = r2.orderBy(r1);	 Catch:{ all -> 0x0094 }
        r2 = r2.create();	 Catch:{ all -> 0x0094 }
        r7 = r0.query(r2);	 Catch:{ all -> 0x0094 }
        r9 = -1;
        if (r7 == 0) goto L_0x008e;
    L_0x0039:
        r1 = r7.moveToFirst();	 Catch:{ all -> 0x008c }
        if (r1 == 0) goto L_0x007b;
    L_0x003f:
        r1 = "_id";
        r1 = r7.getColumnIndex(r1);	 Catch:{ all -> 0x008c }
        r11 = r7.getLong(r1);	 Catch:{ all -> 0x008c }
        r1 = "view_count";
        r2 = "view_count";
        r2 = r7.getColumnIndex(r2);	 Catch:{ all -> 0x008c }
        r2 = r7.getLong(r2);	 Catch:{ all -> 0x008c }
        r13 = 1;
        r2 = r2 + r13;
        r2 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x008c }
        r4.put(r1, r2);	 Catch:{ all -> 0x008c }
        r2 = "browsing_history";
        r3 = 2;
        r6 = "_id = ?";
        r13 = new java.lang.String[r5];	 Catch:{ all -> 0x008c }
        r1 = java.lang.Long.toString(r11);	 Catch:{ all -> 0x008c }
        r13[r8] = r1;	 Catch:{ all -> 0x008c }
        r1 = r16;
        r4 = r17;
        r5 = r6;
        r6 = r13;
        r0 = r1.update(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x008c }
        if (r0 != 0) goto L_0x0079;
    L_0x0078:
        goto L_0x008e;
    L_0x0079:
        r9 = r11;
        goto L_0x008e;
    L_0x007b:
        r1 = "view_count";
        r2 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x008c }
        r4.put(r1, r2);	 Catch:{ all -> 0x008c }
        r1 = "browsing_history";
        r2 = 2;
        r9 = r0.insert(r1, r2, r4);	 Catch:{ all -> 0x008c }
        goto L_0x008e;
    L_0x008c:
        r0 = move-exception;
        goto L_0x0096;
    L_0x008e:
        if (r7 == 0) goto L_0x0093;
    L_0x0090:
        r7.close();
    L_0x0093:
        return r9;
    L_0x0094:
        r0 = move-exception;
        r7 = r1;
    L_0x0096:
        if (r7 == 0) goto L_0x009b;
    L_0x0098:
        r7.close();
    L_0x009b:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.provider.HistoryProvider.insertWithUrlUnique(android.arch.persistence.db.SupportSQLiteDatabase, android.content.ContentValues):long");
    }

    private void notifyBrowsingHistoryChange() {
        getContext().getContentResolver().notifyChange(BrowsingHistory.CONTENT_URI, null);
    }
}
