// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import android.database.sqlite.SQLiteQueryBuilder;
import android.database.Cursor;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.database.ContentObserver;
import android.content.UriMatcher;
import android.content.ContentProvider;

public class DownloadInfoProvider extends ContentProvider
{
    private static final UriMatcher sUriMatcher;
    private DownloadInfoDbHelper mDbHelper;
    
    static {
        (sUriMatcher = new UriMatcher(-1)).addURI("org.mozilla.rocket.provider.downloadprovider", "download_info", 2);
    }
    
    private String getLimitParam(final String str, String string) {
        if (string == null) {
            string = null;
        }
        else if (str != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(",");
            sb.append(string);
            string = sb.toString();
        }
        return string;
    }
    
    private void notifyChange() {
        this.getContext().getContentResolver().notifyChange(DownloadContract.Download.CONTENT_URI, (ContentObserver)null);
    }
    
    public int delete(final Uri obj, final String s, final String[] array) {
        final SQLiteDatabase writableDB = this.mDbHelper.getWritableDB();
        if (DownloadInfoProvider.sUriMatcher.match(obj) == 2) {
            final int delete = writableDB.delete("download_info", s, array);
            if (delete > 0) {
                this.notifyChange();
            }
            return delete;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new UnsupportedOperationException(sb.toString());
    }
    
    public String getType(final Uri obj) {
        if (DownloadInfoProvider.sUriMatcher.match(obj) == 2) {
            return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.downloadprovider.downloadinfo";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Uri insert(final Uri obj, final ContentValues contentValues) {
        final SQLiteDatabase writableDB = this.mDbHelper.getWritableDB();
        if (DownloadInfoProvider.sUriMatcher.match(obj) == 2) {
            final Uri uri = null;
            final Long value = writableDB.insert("download_info", (String)null, contentValues);
            Uri withAppendedId = uri;
            if (value > 0L) {
                this.notifyChange();
                withAppendedId = ContentUris.withAppendedId(obj, (long)value);
            }
            return withAppendedId;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new UnsupportedOperationException(sb.toString());
    }
    
    public boolean onCreate() {
        this.mDbHelper = DownloadInfoDbHelper.getsInstance(this.getContext());
        return true;
    }
    
    public Cursor query(final Uri obj, final String[] array, final String s, final String[] array2, final String s2) {
        final SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        if (DownloadInfoProvider.sUriMatcher.match(obj) == 2) {
            sqLiteQueryBuilder.setTables("download_info");
            return sqLiteQueryBuilder.query(this.mDbHelper.getReadableDB(), array, s, array2, (String)null, (String)null, s2, this.getLimitParam(obj.getQueryParameter("offset"), obj.getQueryParameter("limit")));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public int update(final Uri obj, final ContentValues contentValues, final String s, final String[] array) {
        final SQLiteDatabase writableDB = this.mDbHelper.getWritableDB();
        if (DownloadInfoProvider.sUriMatcher.match(obj) == 2) {
            final int update = writableDB.update("download_info", contentValues, s, array);
            if (update > 0) {
                this.notifyChange();
            }
            return update;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new UnsupportedOperationException(sb.toString());
    }
}
