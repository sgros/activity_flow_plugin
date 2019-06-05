package org.mozilla.focus.provider;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.screenshot.model.Screenshot;

public class QueryHandler extends AsyncQueryHandler {
    public static final Object OBJECT_NO_VALUE = null;
    private Handler mWorkerHandler;

    public interface AsyncDeleteListener {
        void onDeleteComplete(int i, long j);
    }

    public static final class AsyncDeleteWrapper {
        /* renamed from: id */
        public long f50id;
        public AsyncDeleteListener listener;

        public AsyncDeleteWrapper(long j, AsyncDeleteListener asyncDeleteListener) {
            this.f50id = j;
            this.listener = asyncDeleteListener;
        }
    }

    public interface AsyncInsertListener {
        void onInsertComplete(long j);
    }

    public interface AsyncQueryListener {
        void onQueryComplete(List list);
    }

    public interface AsyncUpdateListener {
        void onUpdateComplete(int i);
    }

    public QueryHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    /* Access modifiers changed, original: protected */
    public Handler createHandler(Looper looper) {
        this.mWorkerHandler = super.createHandler(looper);
        return this.mWorkerHandler;
    }

    public void postWorker(Runnable runnable) {
        this.mWorkerHandler.post(runnable);
    }

    /* Access modifiers changed, original: protected */
    public void onInsertComplete(int i, Object obj, Uri uri) {
        switch (i) {
            case 1:
            case 2:
                if (obj != null) {
                    ((AsyncInsertListener) obj).onInsertComplete(uri == null ? -1 : Long.parseLong(uri.getLastPathSegment()));
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDeleteComplete(int i, Object obj, int i2) {
        switch (i) {
            case 1:
            case 2:
                if (obj != null) {
                    AsyncDeleteWrapper asyncDeleteWrapper = (AsyncDeleteWrapper) obj;
                    if (asyncDeleteWrapper.listener != null) {
                        asyncDeleteWrapper.listener.onDeleteComplete(i2, asyncDeleteWrapper.f50id);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onUpdateComplete(int i, Object obj, int i2) {
        switch (i) {
            case 1:
            case 2:
                if (obj != null) {
                    ((AsyncUpdateListener) obj).onUpdateComplete(i2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onQueryComplete(int i, Object obj, Cursor cursor) {
        ArrayList arrayList;
        switch (i) {
            case 1:
                if (obj != null) {
                    arrayList = new ArrayList();
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            arrayList.add(cursorToSite(cursor));
                        }
                        cursor.close();
                    }
                    ((AsyncQueryListener) obj).onQueryComplete(arrayList);
                    return;
                }
                return;
            case 2:
                if (obj != null) {
                    arrayList = new ArrayList();
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            arrayList.add(cursorToScreenshot(cursor));
                        }
                        cursor.close();
                    }
                    ((AsyncQueryListener) obj).onQueryComplete(arrayList);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public static ContentValues getContentValuesFromSite(Site site) {
        ContentValues contentValues = new ContentValues();
        if (site.getTitle() != OBJECT_NO_VALUE) {
            contentValues.put("title", site.getTitle());
        }
        contentValues.put("url", site.getUrl());
        if (site.getViewCount() != 0) {
            contentValues.put("view_count", Long.valueOf(site.getViewCount()));
        }
        if (site.getLastViewTimestamp() != 0) {
            contentValues.put("last_view_timestamp", Long.valueOf(site.getLastViewTimestamp()));
        }
        if (site.getFavIconUri() != OBJECT_NO_VALUE) {
            contentValues.put("fav_icon_uri", site.getFavIconUri());
        }
        return contentValues;
    }

    public static ContentValues getContentValuesFromScreenshot(Screenshot screenshot) {
        ContentValues contentValues = new ContentValues();
        if (screenshot.getTitle() != OBJECT_NO_VALUE) {
            contentValues.put("title", screenshot.getTitle());
        }
        if (screenshot.getUrl() != OBJECT_NO_VALUE) {
            contentValues.put("url", screenshot.getUrl());
        }
        if (screenshot.getTimestamp() != 0) {
            contentValues.put("timestamp", Long.valueOf(screenshot.getTimestamp()));
        }
        if (screenshot.getImageUri() != OBJECT_NO_VALUE) {
            contentValues.put("image_uri", screenshot.getImageUri());
        }
        return contentValues;
    }

    private static Site cursorToSite(Cursor cursor) {
        return new Site(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("url")), cursor.getLong(cursor.getColumnIndex("view_count")), cursor.getLong(cursor.getColumnIndex("last_view_timestamp")), cursor.getString(cursor.getColumnIndex("fav_icon_uri")));
    }

    private static Screenshot cursorToScreenshot(Cursor cursor) {
        Screenshot screenshot = new Screenshot();
        screenshot.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        screenshot.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        screenshot.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        screenshot.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
        screenshot.setImageUri(cursor.getString(cursor.getColumnIndex("image_uri")));
        return screenshot;
    }
}
