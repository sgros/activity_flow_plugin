// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import org.mozilla.focus.utils.ProviderUtils;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.Cursor;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.database.ContentObserver;
import android.content.UriMatcher;
import android.content.ContentProvider;

public class ScreenshotProvider extends ContentProvider
{
    private static final UriMatcher sUriMatcher;
    private ScreenshotDatabaseHelper mDbHelper;
    
    static {
        (sUriMatcher = new UriMatcher(-1)).addURI("org.mozilla.rocket.provider.screenshotprovider", "screenshot", 1);
    }
    
    private void notifyScreenshotChange() {
        this.getContext().getContentResolver().notifyChange(ScreenshotContract.Screenshot.CONTENT_URI, (ContentObserver)null);
    }
    
    public int delete(final Uri obj, final String s, final String[] array) {
        final SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (ScreenshotProvider.sUriMatcher.match(obj) == 1) {
            return writableDatabase.delete("screenshot", s, array);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new UnsupportedOperationException(sb.toString());
    }
    
    public String getType(final Uri obj) {
        if (ScreenshotProvider.sUriMatcher.match(obj) == 1) {
            return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.screenshotprovider.screenshot";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Uri insert(final Uri obj, final ContentValues contentValues) {
        final SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (ScreenshotProvider.sUriMatcher.match(obj) != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("URI: ");
            sb.append(obj);
            throw new UnsupportedOperationException(sb.toString());
        }
        final long insert = writableDatabase.insert("screenshot", (String)null, new ContentValues(contentValues));
        if (insert < 0L) {
            return null;
        }
        this.notifyScreenshotChange();
        return ContentUris.withAppendedId(obj, insert);
    }
    
    public boolean onCreate() {
        this.mDbHelper = ScreenshotDatabaseHelper.getsInstacne(this.getContext());
        return true;
    }
    
    public Cursor query(final Uri obj, final String[] array, final String s, final String[] array2, final String s2) {
        final SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        if (ScreenshotProvider.sUriMatcher.match(obj) == 1) {
            sqLiteQueryBuilder.setTables("screenshot");
            return sqLiteQueryBuilder.query(this.mDbHelper.getReadableDatabase(), array, s, array2, (String)null, (String)null, s2, ProviderUtils.getLimitParam(obj.getQueryParameter("offset"), obj.getQueryParameter("limit")));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public int update(final Uri obj, final ContentValues contentValues, final String s, final String[] array) {
        final SQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        if (ScreenshotProvider.sUriMatcher.match(obj) == 1) {
            return writableDatabase.update("screenshot", contentValues, s, array);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URI: ");
        sb.append(obj);
        throw new UnsupportedOperationException(sb.toString());
    }
}
