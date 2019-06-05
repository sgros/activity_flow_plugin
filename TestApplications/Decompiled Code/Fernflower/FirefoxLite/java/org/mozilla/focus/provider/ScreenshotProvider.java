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
import org.mozilla.focus.utils.ProviderUtils;

public class ScreenshotProvider extends ContentProvider {
   private static final UriMatcher sUriMatcher = new UriMatcher(-1);
   private ScreenshotDatabaseHelper mDbHelper;

   static {
      sUriMatcher.addURI("org.mozilla.rocket.provider.screenshotprovider", "screenshot", 1);
   }

   private void notifyScreenshotChange() {
      this.getContext().getContentResolver().notifyChange(ScreenshotContract.Screenshot.CONTENT_URI, (ContentObserver)null);
   }

   public int delete(Uri var1, String var2, String[] var3) {
      SQLiteDatabase var4 = this.mDbHelper.getWritableDatabase();
      if (sUriMatcher.match(var1) == 1) {
         return var4.delete("screenshot", var2, var3);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("URI: ");
         var5.append(var1);
         throw new UnsupportedOperationException(var5.toString());
      }
   }

   public String getType(Uri var1) {
      if (sUriMatcher.match(var1) == 1) {
         return "vnd.android.cursor.dir/vnd.org.mozilla.rocket.provider.screenshotprovider.screenshot";
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("URI: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public Uri insert(Uri var1, ContentValues var2) {
      SQLiteDatabase var3 = this.mDbHelper.getWritableDatabase();
      if (sUriMatcher.match(var1) == 1) {
         long var4 = var3.insert("screenshot", (String)null, new ContentValues(var2));
         if (var4 < 0L) {
            return null;
         } else {
            this.notifyScreenshotChange();
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
      this.mDbHelper = ScreenshotDatabaseHelper.getsInstacne(this.getContext());
      return true;
   }

   public Cursor query(Uri var1, String[] var2, String var3, String[] var4, String var5) {
      SQLiteQueryBuilder var6 = new SQLiteQueryBuilder();
      if (sUriMatcher.match(var1) == 1) {
         var6.setTables("screenshot");
         return var6.query(this.mDbHelper.getReadableDatabase(), var2, var3, var4, (String)null, (String)null, var5, ProviderUtils.getLimitParam(var1.getQueryParameter("offset"), var1.getQueryParameter("limit")));
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("URI: ");
         var7.append(var1);
         throw new IllegalArgumentException(var7.toString());
      }
   }

   public int update(Uri var1, ContentValues var2, String var3, String[] var4) {
      SQLiteDatabase var5 = this.mDbHelper.getWritableDatabase();
      if (sUriMatcher.match(var1) == 1) {
         return var5.update("screenshot", var2, var3, var4);
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("URI: ");
         var6.append(var1);
         throw new UnsupportedOperationException(var6.toString());
      }
   }
}
