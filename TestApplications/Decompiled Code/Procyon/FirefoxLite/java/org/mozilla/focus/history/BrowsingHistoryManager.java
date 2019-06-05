// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.history;

import org.mozilla.icon.FavIconUtils;
import java.util.Iterator;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.content.Context;
import org.mozilla.focus.provider.HistoryContract;
import org.mozilla.focus.history.model.Site;
import android.content.ContentResolver;
import java.lang.ref.WeakReference;
import org.mozilla.focus.provider.QueryHandler;
import java.util.ArrayList;

public class BrowsingHistoryManager
{
    private static BrowsingHistoryManager sInstance;
    private BrowsingHistoryContentObserver mContentObserver;
    private ArrayList<ContentChangeListener> mListeners;
    private QueryHandler mQueryHandler;
    private WeakReference<ContentResolver> mResolver;
    
    public static BrowsingHistoryManager getInstance() {
        if (BrowsingHistoryManager.sInstance == null) {
            BrowsingHistoryManager.sInstance = new BrowsingHistoryManager();
        }
        return BrowsingHistoryManager.sInstance;
    }
    
    public static Site prepareSiteForFirstInsert(final String s, final String s2, final long n) {
        return new Site(0L, s2, s, 0L, n, (String)QueryHandler.OBJECT_NO_VALUE);
    }
    
    private static Site prepareSiteForUpdate(final String s, final String s2, final String s3) {
        return new Site(0L, s, s2, 0L, 0L, s3);
    }
    
    public static void updateHistory(final String s, final String s2, final String s3) {
        updateHistory(s, s2, s3, null);
    }
    
    public static void updateHistory(final String s, final String s2, final String s3, final QueryHandler.AsyncUpdateListener asyncUpdateListener) {
        getInstance().updateLastEntry(prepareSiteForUpdate(s, s2, s3), asyncUpdateListener);
    }
    
    public void delete(final long i, final QueryHandler.AsyncDeleteListener asyncDeleteListener) {
        this.mQueryHandler.startDelete(1, (Object)new QueryHandler.AsyncDeleteWrapper(i, asyncDeleteListener), HistoryContract.BrowsingHistory.CONTENT_URI, "_id = ?", new String[] { Long.toString(i) });
    }
    
    public void deleteAll(final QueryHandler.AsyncDeleteListener asyncDeleteListener) {
        this.mQueryHandler.startDelete(1, (Object)new QueryHandler.AsyncDeleteWrapper(-1L, asyncDeleteListener), HistoryContract.BrowsingHistory.CONTENT_URI, "1", (String[])null);
    }
    
    public void init(final Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        this.mResolver = new WeakReference<ContentResolver>(contentResolver);
        this.mQueryHandler = new QueryHandler(contentResolver);
        this.mContentObserver = new BrowsingHistoryContentObserver(null);
        this.mListeners = new ArrayList<ContentChangeListener>();
    }
    
    public void insert(final Site site, final QueryHandler.AsyncInsertListener asyncInsertListener) {
        this.mQueryHandler.postWorker(new Runnable() {
            @Override
            public void run() {
                BrowsingHistoryManager.this.mQueryHandler.startInsert(1, (Object)asyncInsertListener, HistoryContract.BrowsingHistory.CONTENT_URI, QueryHandler.getContentValuesFromSite(site));
            }
        });
    }
    
    public void query(final int i, final int j, final QueryHandler.AsyncQueryListener asyncQueryListener) {
        final QueryHandler mQueryHandler = this.mQueryHandler;
        final StringBuilder sb = new StringBuilder();
        sb.append(HistoryContract.BrowsingHistory.CONTENT_URI.toString());
        sb.append("?offset=");
        sb.append(i);
        sb.append("&limit=");
        sb.append(j);
        mQueryHandler.startQuery(1, (Object)asyncQueryListener, Uri.parse(sb.toString()), (String[])null, (String)null, (String[])null, "last_view_timestamp DESC");
    }
    
    public void queryTopSites(final int i, final int j, final QueryHandler.AsyncQueryListener asyncQueryListener) {
        final QueryHandler mQueryHandler = this.mQueryHandler;
        final StringBuilder sb = new StringBuilder();
        sb.append(HistoryContract.BrowsingHistory.CONTENT_URI.toString());
        sb.append("?limit=");
        sb.append(i);
        mQueryHandler.startQuery(1, (Object)asyncQueryListener, Uri.parse(sb.toString()), (String[])null, "view_count >= ?", new String[] { Integer.toString(j) }, "view_count DESC");
    }
    
    public void updateLastEntry(final Site site, final QueryHandler.AsyncUpdateListener asyncUpdateListener) {
        this.mQueryHandler.postWorker(new Runnable() {
            @Override
            public void run() {
                BrowsingHistoryManager.this.mQueryHandler.startUpdate(1, (Object)asyncUpdateListener, HistoryContract.BrowsingHistory.CONTENT_URI, QueryHandler.getContentValuesFromSite(site), "_id = ( SELECT _id FROM browsing_history WHERE url = ? ORDER BY last_view_timestamp DESC)", new String[] { site.getUrl() });
            }
        });
    }
    
    private final class BrowsingHistoryContentObserver extends ContentObserver
    {
        public BrowsingHistoryContentObserver(final Handler handler) {
            super(handler);
        }
        
        public void onChange(final boolean b) {
            final Iterator<ContentChangeListener> iterator = BrowsingHistoryManager.this.mListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onContentChanged();
            }
        }
    }
    
    public interface ContentChangeListener
    {
        void onContentChanged();
    }
    
    public static class UpdateHistoryWrapper implements Consumer<String>
    {
        private String title;
        private String url;
        
        public UpdateHistoryWrapper(final String title, final String url) {
            this.title = title;
            this.url = url;
        }
        
        public void accept(final String s) {
            BrowsingHistoryManager.updateHistory(this.title, this.url, s);
        }
    }
}
