package org.mozilla.focus.provider;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import org.mozilla.focus.utils.ProviderUtils;
import org.mozilla.rocket.persistance.History.HistoryDatabase;

public class HistoryProvider extends ContentProvider {
   private static final UriMatcher sUriMatcher = new UriMatcher(-1);
   private SupportSQLiteOpenHelper mDbHelper;

   static {
      sUriMatcher.addURI("org.mozilla.rocket.provider.historyprovider", "browsing_history", 1);
   }

   private long insertWithUrlUnique(SupportSQLiteDatabase var1, ContentValues var2) {
      boolean var12 = false;

      Cursor var3;
      try {
         var12 = true;
         var3 = var1.query(SupportSQLiteQueryBuilder.builder("browsing_history").columns((String[])null).selection("url = ?", new String[]{var2.getAsString("url")}).groupBy((String)null).having((String)null).orderBy((String)null).create());
         var12 = false;
      } finally {
         if (var12) {
            var1 = null;
            if (var1 != null) {
               var1.close();
            }

         }
      }

      long var4 = -1L;
      long var6 = var4;
      if (var3 != null) {
         label112: {
            try {
               if (!var3.moveToFirst()) {
                  var2.put("view_count", 1);
                  var6 = var1.insert("browsing_history", 2, var2);
                  break label112;
               }

               var6 = var3.getLong(var3.getColumnIndex("_id"));
               var2.put("view_count", var3.getLong(var3.getColumnIndex("view_count")) + 1L);
               if (var1.update("browsing_history", 2, var2, "_id = ?", new String[]{Long.toString(var6)}) != 0) {
                  break label112;
               }
            } finally {
               ;
            }

            var6 = var4;
         }
      }

      if (var3 != null) {
         var3.close();
      }

      return var6;
   }

   private void notifyBrowsingHistoryChange() {
      this.getContext().getContentResolver().notifyChange(HistoryContract.BrowsingHistory.CONTENT_URI, (ContentObserver)null);
   }

   public int delete(Uri var1, String var2, String[] var3) {
      SupportSQLiteDatabase var4 = this.mDbHelper.getWritableDatabase();
      if (sUriMatcher.match(var1) == 1) {
         int var5 = var4.delete("browsing_history", var2, var3);
         if (var5 > 0) {
            this.notifyBrowsingHistoryChange();
         }

         return var5;
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("URI: ");
         var6.append(var1);
         throw new UnsupportedOperationException(var6.toString());
      }
   }

   public String getType(Uri var1) {
      if (sUriMatcher.match(var1) == 1) {
         return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.historyprovider.browsinghistory";
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("URI: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public Uri insert(Uri var1, ContentValues var2) {
      SupportSQLiteDatabase var3 = this.mDbHelper.getWritableDatabase();
      if (sUriMatcher.match(var1) == 1) {
         long var4 = this.insertWithUrlUnique(var3, new ContentValues(var2));
         if (var4 < 0L) {
            return null;
         } else {
            this.notifyBrowsingHistoryChange();
            return ContentUris.withAppendedId(var1, var4);
         }
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("URI: ");
         var6.append(var1);
         throw new UnsupportedOperationException(var6.toString());
      }
   }

   public boolean onCreate() {
      this.mDbHelper = HistoryDatabase.getInstance(this.getContext()).getOpenHelper();
      return true;
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      if (sUriMatcher.match(var1) == 1) {
         return this.mDbHelper.getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("browsing_history").columns(var2).selection(var3, var4).orderBy(var5).limit(ProviderUtils.getLimitParam(var1.getQueryParameter("offset"), var1.getQueryParameter("limit"))).create());
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("URI: ");
         var6.append(var1);
         throw new IllegalArgumentException(var6.toString());
      }
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      if (sUriMatcher.match(var1) == 1) {
         int var5 = this.mDbHelper.getWritableDatabase().update("browsing_history", 2, var2, var3, var4);
         if (var5 > 0) {
            this.notifyBrowsingHistoryChange();
         }

         return var5;
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("URI: ");
         var6.append(var1);
         throw new UnsupportedOperationException(var6.toString());
      }
   }
}
