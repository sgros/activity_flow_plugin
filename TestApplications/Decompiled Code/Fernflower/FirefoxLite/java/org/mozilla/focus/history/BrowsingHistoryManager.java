package org.mozilla.focus.history;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.provider.HistoryContract;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.icon.FavIconUtils;

public class BrowsingHistoryManager {
   private static BrowsingHistoryManager sInstance;
   private BrowsingHistoryManager.BrowsingHistoryContentObserver mContentObserver;
   private ArrayList mListeners;
   private QueryHandler mQueryHandler;
   private WeakReference mResolver;

   public static BrowsingHistoryManager getInstance() {
      if (sInstance == null) {
         sInstance = new BrowsingHistoryManager();
      }

      return sInstance;
   }

   public static Site prepareSiteForFirstInsert(String var0, String var1, long var2) {
      return new Site(0L, var1, var0, 0L, var2, (String)QueryHandler.OBJECT_NO_VALUE);
   }

   private static Site prepareSiteForUpdate(String var0, String var1, String var2) {
      return new Site(0L, var0, var1, 0L, 0L, var2);
   }

   public static void updateHistory(String var0, String var1, String var2) {
      updateHistory(var0, var1, var2, (QueryHandler.AsyncUpdateListener)null);
   }

   public static void updateHistory(String var0, String var1, String var2, QueryHandler.AsyncUpdateListener var3) {
      getInstance().updateLastEntry(prepareSiteForUpdate(var0, var1, var2), var3);
   }

   public void delete(long var1, QueryHandler.AsyncDeleteListener var3) {
      this.mQueryHandler.startDelete(1, new QueryHandler.AsyncDeleteWrapper(var1, var3), HistoryContract.BrowsingHistory.CONTENT_URI, "_id = ?", new String[]{Long.toString(var1)});
   }

   public void deleteAll(QueryHandler.AsyncDeleteListener var1) {
      this.mQueryHandler.startDelete(1, new QueryHandler.AsyncDeleteWrapper(-1L, var1), HistoryContract.BrowsingHistory.CONTENT_URI, "1", (String[])null);
   }

   public void init(Context var1) {
      ContentResolver var2 = var1.getContentResolver();
      this.mResolver = new WeakReference(var2);
      this.mQueryHandler = new QueryHandler(var2);
      this.mContentObserver = new BrowsingHistoryManager.BrowsingHistoryContentObserver((Handler)null);
      this.mListeners = new ArrayList();
   }

   public void insert(final Site var1, final QueryHandler.AsyncInsertListener var2) {
      this.mQueryHandler.postWorker(new Runnable() {
         public void run() {
            ContentValues var1x = QueryHandler.getContentValuesFromSite(var1);
            BrowsingHistoryManager.this.mQueryHandler.startInsert(1, var2, HistoryContract.BrowsingHistory.CONTENT_URI, var1x);
         }
      });
   }

   public void query(int var1, int var2, QueryHandler.AsyncQueryListener var3) {
      QueryHandler var4 = this.mQueryHandler;
      StringBuilder var5 = new StringBuilder();
      var5.append(HistoryContract.BrowsingHistory.CONTENT_URI.toString());
      var5.append("?offset=");
      var5.append(var1);
      var5.append("&limit=");
      var5.append(var2);
      var4.startQuery(1, var3, Uri.parse(var5.toString()), (String[])null, (String)null, (String[])null, "last_view_timestamp DESC");
   }

   public void queryTopSites(int var1, int var2, QueryHandler.AsyncQueryListener var3) {
      QueryHandler var4 = this.mQueryHandler;
      StringBuilder var5 = new StringBuilder();
      var5.append(HistoryContract.BrowsingHistory.CONTENT_URI.toString());
      var5.append("?limit=");
      var5.append(var1);
      var4.startQuery(1, var3, Uri.parse(var5.toString()), (String[])null, "view_count >= ?", new String[]{Integer.toString(var2)}, "view_count DESC");
   }

   public void updateLastEntry(final Site var1, final QueryHandler.AsyncUpdateListener var2) {
      this.mQueryHandler.postWorker(new Runnable() {
         public void run() {
            ContentValues var1x = QueryHandler.getContentValuesFromSite(var1);
            BrowsingHistoryManager.this.mQueryHandler.startUpdate(1, var2, HistoryContract.BrowsingHistory.CONTENT_URI, var1x, "_id = ( SELECT _id FROM browsing_history WHERE url = ? ORDER BY last_view_timestamp DESC)", new String[]{var1.getUrl()});
         }
      });
   }

   private final class BrowsingHistoryContentObserver extends ContentObserver {
      public BrowsingHistoryContentObserver(Handler var2) {
         super(var2);
      }

      public void onChange(boolean var1) {
         Iterator var2 = BrowsingHistoryManager.this.mListeners.iterator();

         while(var2.hasNext()) {
            ((BrowsingHistoryManager.ContentChangeListener)var2.next()).onContentChanged();
         }

      }
   }

   public interface ContentChangeListener {
      void onContentChanged();
   }

   public static class UpdateHistoryWrapper implements FavIconUtils.Consumer {
      private String title;
      private String url;

      public UpdateHistoryWrapper(String var1, String var2) {
         this.title = var1;
         this.url = var2;
      }

      public void accept(String var1) {
         BrowsingHistoryManager.updateHistory(this.title, this.url, var1);
      }
   }
}
