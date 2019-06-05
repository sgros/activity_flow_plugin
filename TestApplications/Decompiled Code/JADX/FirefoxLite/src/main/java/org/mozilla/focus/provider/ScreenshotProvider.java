package org.mozilla.focus.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import org.mozilla.focus.provider.ScreenshotContract.Screenshot;
import org.mozilla.focus.utils.ProviderUtils;

public class ScreenshotProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);
    private ScreenshotDatabaseHelper mDbHelper;

    static {
        sUriMatcher.addURI("org.mozilla.rocket.provider.screenshotprovider", "screenshot", 1);
    }

    public boolean onCreate() {
        this.mDbHelper = ScreenshotDatabaseHelper.getsInstacne(getContext());
        return true;
    }

    public int delete(Uri uri, String str, String[] strArr) {
        SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (sUriMatcher.match(uri) == 1) {
            return writableDatabase.delete("screenshot", str, strArr);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public String getType(Uri uri) {
        if (sUriMatcher.match(uri) == 1) {
            return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.screenshotprovider.screenshot";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (sUriMatcher.match(uri) == 1) {
            long insert = writableDatabase.insert("screenshot", null, new ContentValues(contentValues));
            if (insert < 0) {
                return null;
            }
            notifyScreenshotChange();
            return ContentUris.withAppendedId(uri, insert);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
        if (sUriMatcher.match(uri) == 1) {
            sQLiteQueryBuilder.setTables("screenshot");
            return sQLiteQueryBuilder.query(this.mDbHelper.getReadableDatabase(), strArr, str, strArr2, null, null, str2, ProviderUtils.getLimitParam(uri.getQueryParameter("offset"), uri.getQueryParameter("limit")));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (sUriMatcher.match(uri) == 1) {
            return writableDatabase.update("screenshot", contentValues, str, strArr);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("URI: ");
        stringBuilder.append(uri);
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    private void notifyScreenshotChange() {
        getContext().getContentResolver().notifyChange(Screenshot.CONTENT_URI, null);
    }
}
