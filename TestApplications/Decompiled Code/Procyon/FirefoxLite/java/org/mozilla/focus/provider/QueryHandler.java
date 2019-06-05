// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import java.util.List;
import java.util.ArrayList;
import android.net.Uri;
import android.os.Looper;
import android.content.ContentValues;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.screenshot.model.Screenshot;
import android.database.Cursor;
import android.content.ContentResolver;
import android.os.Handler;
import android.content.AsyncQueryHandler;

public class QueryHandler extends AsyncQueryHandler
{
    public static final Object OBJECT_NO_VALUE;
    private Handler mWorkerHandler;
    
    public QueryHandler(final ContentResolver contentResolver) {
        super(contentResolver);
    }
    
    private static Screenshot cursorToScreenshot(final Cursor cursor) {
        final Screenshot screenshot = new Screenshot();
        screenshot.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        screenshot.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        screenshot.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        screenshot.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
        screenshot.setImageUri(cursor.getString(cursor.getColumnIndex("image_uri")));
        return screenshot;
    }
    
    private static Site cursorToSite(final Cursor cursor) {
        return new Site(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("url")), cursor.getLong(cursor.getColumnIndex("view_count")), cursor.getLong(cursor.getColumnIndex("last_view_timestamp")), cursor.getString(cursor.getColumnIndex("fav_icon_uri")));
    }
    
    public static ContentValues getContentValuesFromScreenshot(final Screenshot screenshot) {
        final ContentValues contentValues = new ContentValues();
        if (screenshot.getTitle() != QueryHandler.OBJECT_NO_VALUE) {
            contentValues.put("title", screenshot.getTitle());
        }
        if (screenshot.getUrl() != QueryHandler.OBJECT_NO_VALUE) {
            contentValues.put("url", screenshot.getUrl());
        }
        if (screenshot.getTimestamp() != 0L) {
            contentValues.put("timestamp", Long.valueOf(screenshot.getTimestamp()));
        }
        if (screenshot.getImageUri() != QueryHandler.OBJECT_NO_VALUE) {
            contentValues.put("image_uri", screenshot.getImageUri());
        }
        return contentValues;
    }
    
    public static ContentValues getContentValuesFromSite(final Site site) {
        final ContentValues contentValues = new ContentValues();
        if (site.getTitle() != QueryHandler.OBJECT_NO_VALUE) {
            contentValues.put("title", site.getTitle());
        }
        contentValues.put("url", site.getUrl());
        if (site.getViewCount() != 0L) {
            contentValues.put("view_count", Long.valueOf(site.getViewCount()));
        }
        if (site.getLastViewTimestamp() != 0L) {
            contentValues.put("last_view_timestamp", Long.valueOf(site.getLastViewTimestamp()));
        }
        if (site.getFavIconUri() != QueryHandler.OBJECT_NO_VALUE) {
            contentValues.put("fav_icon_uri", site.getFavIconUri());
        }
        return contentValues;
    }
    
    protected Handler createHandler(final Looper looper) {
        return this.mWorkerHandler = super.createHandler(looper);
    }
    
    protected void onDeleteComplete(final int n, final Object o, final int n2) {
        switch (n) {
            case 1:
            case 2: {
                if (o == null) {
                    break;
                }
                final AsyncDeleteWrapper asyncDeleteWrapper = (AsyncDeleteWrapper)o;
                if (asyncDeleteWrapper.listener != null) {
                    asyncDeleteWrapper.listener.onDeleteComplete(n2, asyncDeleteWrapper.id);
                    break;
                }
                break;
            }
        }
    }
    
    protected void onInsertComplete(final int n, final Object o, final Uri uri) {
        switch (n) {
            case 1:
            case 2: {
                if (o != null) {
                    long long1;
                    if (uri == null) {
                        long1 = -1L;
                    }
                    else {
                        long1 = Long.parseLong(uri.getLastPathSegment());
                    }
                    ((AsyncInsertListener)o).onInsertComplete(long1);
                    break;
                }
                break;
            }
        }
    }
    
    protected void onQueryComplete(final int n, final Object o, final Cursor cursor) {
        switch (n) {
            case 2: {
                if (o != null) {
                    final ArrayList<Screenshot> list = new ArrayList<Screenshot>();
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            list.add(cursorToScreenshot(cursor));
                        }
                        cursor.close();
                    }
                    ((AsyncQueryListener)o).onQueryComplete(list);
                    break;
                }
                break;
            }
            case 1: {
                if (o != null) {
                    final ArrayList<Site> list2 = new ArrayList<Site>();
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            list2.add(cursorToSite(cursor));
                        }
                        cursor.close();
                    }
                    ((AsyncQueryListener)o).onQueryComplete(list2);
                    break;
                }
                break;
            }
        }
    }
    
    protected void onUpdateComplete(final int n, final Object o, final int n2) {
        switch (n) {
            case 1:
            case 2: {
                if (o != null) {
                    ((AsyncUpdateListener)o).onUpdateComplete(n2);
                    break;
                }
                break;
            }
        }
    }
    
    public void postWorker(final Runnable runnable) {
        this.mWorkerHandler.post(runnable);
    }
    
    public interface AsyncDeleteListener
    {
        void onDeleteComplete(final int p0, final long p1);
    }
    
    public static final class AsyncDeleteWrapper
    {
        public long id;
        public AsyncDeleteListener listener;
        
        public AsyncDeleteWrapper(final long id, final AsyncDeleteListener listener) {
            this.id = id;
            this.listener = listener;
        }
    }
    
    public interface AsyncInsertListener
    {
        void onInsertComplete(final long p0);
    }
    
    public interface AsyncQueryListener
    {
        void onQueryComplete(final List p0);
    }
    
    public interface AsyncUpdateListener
    {
        void onUpdateComplete(final int p0);
    }
}
