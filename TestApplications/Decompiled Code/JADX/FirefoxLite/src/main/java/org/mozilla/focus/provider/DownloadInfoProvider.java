package org.mozilla.focus.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import org.mozilla.focus.provider.DownloadContract.Download;

public class DownloadInfoProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);
    private DownloadInfoDbHelper mDbHelper;

    static {
        sUriMatcher.addURI("org.mozilla.rocket.provider.downloadprovider", "download_info", 2);
    }

    public boolean onCreate() {
        this.mDbHelper = DownloadInfoDbHelper.getsInstance(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
        if (sUriMatcher.match(uri) == 2) {
            sQLiteQueryBuilder.setTables("download_info");
            return sQLiteQueryBuilder.query(this.mDbHelper.getReadableDB(), strArr, str, strArr2, null, null, str2, getLimitParam(uri.getQueryParameter("offset"), uri.getQueryParameter("limit")));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public String getType(Uri uri) {
        if (sUriMatcher.match(uri) == 2) {
            return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.downloadprovider.downloadinfo";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase writableDB = this.mDbHelper.getWritableDB();
        if (sUriMatcher.match(uri) == 2) {
            Long valueOf = Long.valueOf(writableDB.insert("download_info", null, contentValues));
            if (valueOf.longValue() <= 0) {
                return null;
            }
            notifyChange();
            return ContentUris.withAppendedId(uri, valueOf.longValue());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public int delete(Uri uri, String str, String[] strArr) {
        SQLiteDatabase writableDB = this.mDbHelper.getWritableDB();
        if (sUriMatcher.match(uri) == 2) {
            int delete = writableDB.delete("download_info", str, strArr);
            if (delete > 0) {
                notifyChange();
            }
            return delete;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        SQLiteDatabase writableDB = this.mDbHelper.getWritableDB();
        if (sUriMatcher.match(uri) == 2) {
            int update = writableDB.update("download_info", contentValues, str, strArr);
            if (update > 0) {
                notifyChange();
            }
            return update;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    private void notifyChange() {
        getContext().getContentResolver().notifyChange(Download.CONTENT_URI, null);
    }

    private String getLimitParam(String str, String str2) {
        if (str2 == null) {
            return null;
        }
        if (str == null) {
            return str2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(",");
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }
}
