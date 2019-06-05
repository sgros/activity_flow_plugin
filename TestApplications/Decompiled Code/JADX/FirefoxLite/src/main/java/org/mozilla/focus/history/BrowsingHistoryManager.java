package org.mozilla.focus.history;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.provider.HistoryContract.BrowsingHistory;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.provider.QueryHandler.AsyncDeleteListener;
import org.mozilla.focus.provider.QueryHandler.AsyncDeleteWrapper;
import org.mozilla.focus.provider.QueryHandler.AsyncInsertListener;
import org.mozilla.focus.provider.QueryHandler.AsyncQueryListener;
import org.mozilla.focus.provider.QueryHandler.AsyncUpdateListener;
import org.mozilla.icon.FavIconUtils.Consumer;

public class BrowsingHistoryManager {
    private static BrowsingHistoryManager sInstance;
    private BrowsingHistoryContentObserver mContentObserver;
    private ArrayList<ContentChangeListener> mListeners;
    private QueryHandler mQueryHandler;
    private WeakReference<ContentResolver> mResolver;

    private final class BrowsingHistoryContentObserver extends ContentObserver {
        public BrowsingHistoryContentObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z) {
            Iterator it = BrowsingHistoryManager.this.mListeners.iterator();
            while (it.hasNext()) {
                ((ContentChangeListener) it.next()).onContentChanged();
            }
        }
    }

    public interface ContentChangeListener {
        void onContentChanged();
    }

    public static class UpdateHistoryWrapper implements Consumer<String> {
        private String title;
        private String url;

        public UpdateHistoryWrapper(String str, String str2) {
            this.title = str;
            this.url = str2;
        }

        public void accept(String str) {
            BrowsingHistoryManager.updateHistory(this.title, this.url, str);
        }
    }

    public static BrowsingHistoryManager getInstance() {
        if (sInstance == null) {
            sInstance = new BrowsingHistoryManager();
        }
        return sInstance;
    }

    public void init(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        this.mResolver = new WeakReference(contentResolver);
        this.mQueryHandler = new QueryHandler(contentResolver);
        this.mContentObserver = new BrowsingHistoryContentObserver(null);
        this.mListeners = new ArrayList();
    }

    public static Site prepareSiteForFirstInsert(String str, String str2, long j) {
        return new Site(0, str2, str, 0, j, (String) QueryHandler.OBJECT_NO_VALUE);
    }

    public void insert(final Site site, final AsyncInsertListener asyncInsertListener) {
        this.mQueryHandler.postWorker(new Runnable() {
            public void run() {
                BrowsingHistoryManager.this.mQueryHandler.startInsert(1, asyncInsertListener, BrowsingHistory.CONTENT_URI, QueryHandler.getContentValuesFromSite(site));
            }
        });
    }

    public void delete(long j, AsyncDeleteListener asyncDeleteListener) {
        this.mQueryHandler.startDelete(1, new AsyncDeleteWrapper(j, asyncDeleteListener), BrowsingHistory.CONTENT_URI, "_id = ?", new String[]{Long.toString(j)});
    }

    public void deleteAll(AsyncDeleteListener asyncDeleteListener) {
        this.mQueryHandler.startDelete(1, new AsyncDeleteWrapper(-1, asyncDeleteListener), BrowsingHistory.CONTENT_URI, "1", null);
    }

    public void updateLastEntry(final Site site, final AsyncUpdateListener asyncUpdateListener) {
        this.mQueryHandler.postWorker(new Runnable() {
            public void run() {
                BrowsingHistoryManager.this.mQueryHandler.startUpdate(1, asyncUpdateListener, BrowsingHistory.CONTENT_URI, QueryHandler.getContentValuesFromSite(site), "_id = ( SELECT _id FROM browsing_history WHERE url = ? ORDER BY last_view_timestamp DESC)", new String[]{site.getUrl()});
            }
        });
    }

    public void query(int i, int i2, AsyncQueryListener asyncQueryListener) {
        QueryHandler queryHandler = this.mQueryHandler;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BrowsingHistory.CONTENT_URI.toString());
        stringBuilder.append("?offset=");
        stringBuilder.append(i);
        stringBuilder.append("&limit=");
        stringBuilder.append(i2);
        queryHandler.startQuery(1, asyncQueryListener, Uri.parse(stringBuilder.toString()), null, null, null, "last_view_timestamp DESC");
    }

    public void queryTopSites(int i, int i2, AsyncQueryListener asyncQueryListener) {
        QueryHandler queryHandler = this.mQueryHandler;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BrowsingHistory.CONTENT_URI.toString());
        stringBuilder.append("?limit=");
        stringBuilder.append(i);
        AsyncQueryListener asyncQueryListener2 = asyncQueryListener;
        queryHandler.startQuery(1, asyncQueryListener2, Uri.parse(stringBuilder.toString()), null, "view_count >= ?", new String[]{Integer.toString(i2)}, "view_count DESC");
    }

    private static Site prepareSiteForUpdate(String str, String str2, String str3) {
        return new Site(0, str, str2, 0, 0, str3);
    }

    public static void updateHistory(String str, String str2, String str3) {
        updateHistory(str, str2, str3, null);
    }

    public static void updateHistory(String str, String str2, String str3, AsyncUpdateListener asyncUpdateListener) {
        getInstance().updateLastEntry(prepareSiteForUpdate(str, str2, str3), asyncUpdateListener);
    }
}
