package org.mozilla.focus.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DownloadInfoProvider extends ContentProvider {
   private static final UriMatcher sUriMatcher = new UriMatcher(-1);
   private DownloadInfoDbHelper mDbHelper;

   static {
      sUriMatcher.addURI("org.mozilla.rocket.provider.downloadprovider", "download_info", 2);
   }

   private String getLimitParam(String var1, String var2) {
      if (var2 == null) {
         var2 = null;
      } else if (var1 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(",");
         var3.append(var2);
         var2 = var3.toString();
      }

      return var2;
   }

   private void notifyChange() {
      this.getContext().getContentResolver().notifyChange(DownloadContract.Download.CONTENT_URI, (ContentObserver)null);
   }

   public int delete(Uri var1, String var2, String[] var3) {
      SQLiteDatabase var4 = this.mDbHelper.getWritableDB();
      if (sUriMatcher.match(var1) == 2) {
         int var5 = var4.delete("download_info", var2, var3);
         if (var5 > 0) {
            this.notifyChange();
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
      if (sUriMatcher.match(var1) == 2) {
         return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.downloadprovider.downloadinfo";
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("URI: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public Uri insert(Uri var1, ContentValues var2) {
      SQLiteDatabase var3 = this.mDbHelper.getWritableDB();
      if (sUriMatcher.match(var1) == 2) {
         Object var4 = null;
         Long var7 = var3.insert("download_info", (String)null, var2);
         Uri var6 = (Uri)var4;
         if (var7 > 0L) {
            this.notifyChange();
            var6 = ContentUris.withAppendedId(var1, var7);
         }

         return var6;
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("URI: ");
         var5.append(var1);
         throw new UnsupportedOperationException(var5.toString());
      }
   }

   public boolean onCreate() {
      this.mDbHelper = DownloadInfoDbHelper.getsInstance(this.getContext());
      return true;
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      SQLiteQueryBuilder var6 = new SQLiteQueryBuilder();
      if (sUriMatcher.match(var1) == 2) {
         var6.setTables("download_info");
         return var6.query(this.mDbHelper.getReadableDB(), var2, var3, var4, (String)null, (String)null, var5, this.getLimitParam(var1.getQueryParameter("offset"), var1.getQueryParameter("limit")));
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("URI: ");
         var7.append(var1);
         throw new IllegalArgumentException(var7.toString());
      }
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      SQLiteDatabase var5 = this.mDbHelper.getWritableDB();
      if (sUriMatcher.match(var1) == 2) {
         int var6 = var5.update("download_info", var2, var3, var4);
         if (var6 > 0) {
            this.notifyChange();
         }

         return var6;
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("URI: ");
         var7.append(var1);
         throw new UnsupportedOperationException(var7.toString());
      }
   }
}
