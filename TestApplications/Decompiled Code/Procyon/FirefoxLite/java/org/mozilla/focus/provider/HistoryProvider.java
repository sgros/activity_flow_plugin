// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import org.mozilla.focus.utils.ProviderUtils;
import org.mozilla.rocket.persistance.History.HistoryDatabase;
import android.content.ContentUris;
import android.net.Uri;
import android.database.ContentObserver;
import android.database.Cursor;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.content.ContentValues;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.content.UriMatcher;
import android.content.ContentProvider;

public class HistoryProvider extends ContentProvider
{
    private static final UriMatcher sUriMatcher;
    private SupportSQLiteOpenHelper mDbHelper;
    
    static {
        (sUriMatcher = new UriMatcher(-1)).addURI("org.mozilla.rocket.provider.historyprovider", "browsing_history", 1);
    }
    
    private long insertWithUrlUnique(final SupportSQLiteDatabase supportSQLiteDatabase, final ContentValues contentValues) {
        Cursor cursor = null;
        Label_0196: {
            try {
                final Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("browsing_history").columns(null).selection("url = ?", new String[] { contentValues.getAsString("url") }).groupBy(null).having(null).orderBy(null).create());
                long i;
                final long n = i = -1L;
                if (query != null) {
                    try {
                        if (query.moveToFirst()) {
                            i = query.getLong(query.getColumnIndex("_id"));
                            contentValues.put("view_count", Long.valueOf(query.getLong(query.getColumnIndex("view_count")) + 1L));
                            if (supportSQLiteDatabase.update("browsing_history", 2, contentValues, "_id = ?", new String[] { Long.toString(i) }) == 0) {
                                i = n;
                            }
                        }
                        else {
                            contentValues.put("view_count", Integer.valueOf(1));
                            i = supportSQLiteDatabase.insert("browsing_history", 2, contentValues);
                        }
                    }
                    finally {
                        break Label_0196;
                    }
                }
                if (query != null) {
                    query.close();
                }
                return i;
            }
            finally {
                cursor = null;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
    
    private void notifyBrowsingHistoryChange() {
        this.getContext().getContentResolver().notifyChange(HistoryContract.BrowsingHistory.CONTENT_URI, (ContentObserver)null);
    }
    
    public int delete(final Uri obj, final String s, final String[] array) {
        final SupportSQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (HistoryProvider.sUriMatcher.match(obj) == 1) {
            final int delete = writableDatabase.delete("browsing_history", s, array);
            if (delete > 0) {
                this.notifyBrowsingHistoryChange();
            }
            return delete;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new UnsupportedOperationException(sb.toString());
    }
    
    public String getType(final Uri obj) {
        if (HistoryProvider.sUriMatcher.match(obj) == 1) {
            return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.historyprovider.browsinghistory";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Uri insert(final Uri obj, final ContentValues contentValues) {
        final SupportSQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (HistoryProvider.sUriMatcher.match(obj) != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("URI: ");
            sb.append(obj);
            throw new UnsupportedOperationException(sb.toString());
        }
        final long insertWithUrlUnique = this.insertWithUrlUnique(writableDatabase, new ContentValues(contentValues));
        if (insertWithUrlUnique < 0L) {
            return null;
        }
        this.notifyBrowsingHistoryChange();
        return ContentUris.withAppendedId(obj, insertWithUrlUnique);
    }
    
    public boolean onCreate() {
        this.mDbHelper = HistoryDatabase.getInstance(this.getContext()).getOpenHelper();
        return true;
    }
    
    public Cursor query(final Uri obj, final String[] array, final String s, final String[] array2, final String s2) {
        if (HistoryProvider.sUriMatcher.match(obj) == 1) {
            return this.mDbHelper.getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("browsing_history").columns(array).selection(s, array2).orderBy(s2).limit(ProviderUtils.getLimitParam(obj.getQueryParameter("offset"), obj.getQueryParameter("limit"))).create());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public int update(final Uri obj, final ContentValues contentValues, final String s, final String[] array) {
        if (HistoryProvider.sUriMatcher.match(obj) == 1) {
            final int update = this.mDbHelper.getWritableDatabase().update("browsing_history", 2, contentValues, s, array);
            if (update > 0) {
                this.notifyBrowsingHistoryChange();
            }
            return update;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new UnsupportedOperationException(sb.toString());
    }
}
