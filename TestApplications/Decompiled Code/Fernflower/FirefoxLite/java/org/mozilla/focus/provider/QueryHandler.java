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
   public static final Object OBJECT_NO_VALUE;
   private Handler mWorkerHandler;

   public QueryHandler(ContentResolver var1) {
      super(var1);
   }

   private static Screenshot cursorToScreenshot(Cursor var0) {
      Screenshot var1 = new Screenshot();
      var1.setId(var0.getLong(var0.getColumnIndex("_id")));
      var1.setTitle(var0.getString(var0.getColumnIndex("title")));
      var1.setUrl(var0.getString(var0.getColumnIndex("url")));
      var1.setTimestamp(var0.getLong(var0.getColumnIndex("timestamp")));
      var1.setImageUri(var0.getString(var0.getColumnIndex("image_uri")));
      return var1;
   }

   private static Site cursorToSite(Cursor var0) {
      return new Site(var0.getLong(var0.getColumnIndex("_id")), var0.getString(var0.getColumnIndex("title")), var0.getString(var0.getColumnIndex("url")), var0.getLong(var0.getColumnIndex("view_count")), var0.getLong(var0.getColumnIndex("last_view_timestamp")), var0.getString(var0.getColumnIndex("fav_icon_uri")));
   }

   public static ContentValues getContentValuesFromScreenshot(Screenshot var0) {
      ContentValues var1 = new ContentValues();
      if (var0.getTitle() != OBJECT_NO_VALUE) {
         var1.put("title", var0.getTitle());
      }

      if (var0.getUrl() != OBJECT_NO_VALUE) {
         var1.put("url", var0.getUrl());
      }

      if (var0.getTimestamp() != 0L) {
         var1.put("timestamp", var0.getTimestamp());
      }

      if (var0.getImageUri() != OBJECT_NO_VALUE) {
         var1.put("image_uri", var0.getImageUri());
      }

      return var1;
   }

   public static ContentValues getContentValuesFromSite(Site var0) {
      ContentValues var1 = new ContentValues();
      if (var0.getTitle() != OBJECT_NO_VALUE) {
         var1.put("title", var0.getTitle());
      }

      var1.put("url", var0.getUrl());
      if (var0.getViewCount() != 0L) {
         var1.put("view_count", var0.getViewCount());
      }

      if (var0.getLastViewTimestamp() != 0L) {
         var1.put("last_view_timestamp", var0.getLastViewTimestamp());
      }

      if (var0.getFavIconUri() != OBJECT_NO_VALUE) {
         var1.put("fav_icon_uri", var0.getFavIconUri());
      }

      return var1;
   }

   protected Handler createHandler(Looper var1) {
      this.mWorkerHandler = super.createHandler(var1);
      return this.mWorkerHandler;
   }

   protected void onDeleteComplete(int var1, Object var2, int var3) {
      switch(var1) {
      case 1:
      case 2:
         if (var2 != null) {
            QueryHandler.AsyncDeleteWrapper var4 = (QueryHandler.AsyncDeleteWrapper)var2;
            if (var4.listener != null) {
               var4.listener.onDeleteComplete(var3, var4.id);
            }
         }
      default:
      }
   }

   protected void onInsertComplete(int var1, Object var2, Uri var3) {
      switch(var1) {
      case 1:
      case 2:
         if (var2 != null) {
            long var4;
            if (var3 == null) {
               var4 = -1L;
            } else {
               var4 = Long.parseLong(var3.getLastPathSegment());
            }

            ((QueryHandler.AsyncInsertListener)var2).onInsertComplete(var4);
         }
      default:
      }
   }

   protected void onQueryComplete(int var1, Object var2, Cursor var3) {
      ArrayList var4;
      switch(var1) {
      case 1:
         if (var2 != null) {
            var4 = new ArrayList();
            if (var3 != null) {
               while(var3.moveToNext()) {
                  var4.add(cursorToSite(var3));
               }

               var3.close();
            }

            ((QueryHandler.AsyncQueryListener)var2).onQueryComplete(var4);
         }
         break;
      case 2:
         if (var2 != null) {
            var4 = new ArrayList();
            if (var3 != null) {
               while(var3.moveToNext()) {
                  var4.add(cursorToScreenshot(var3));
               }

               var3.close();
            }

            ((QueryHandler.AsyncQueryListener)var2).onQueryComplete(var4);
         }
      }

   }

   protected void onUpdateComplete(int var1, Object var2, int var3) {
      switch(var1) {
      case 1:
      case 2:
         if (var2 != null) {
            ((QueryHandler.AsyncUpdateListener)var2).onUpdateComplete(var3);
         }
      default:
      }
   }

   public void postWorker(Runnable var1) {
      this.mWorkerHandler.post(var1);
   }

   public interface AsyncDeleteListener {
      void onDeleteComplete(int var1, long var2);
   }

   public static final class AsyncDeleteWrapper {
      public long id;
      public QueryHandler.AsyncDeleteListener listener;

      public AsyncDeleteWrapper(long var1, QueryHandler.AsyncDeleteListener var3) {
         this.id = var1;
         this.listener = var3;
      }
   }

   public interface AsyncInsertListener {
      void onInsertComplete(long var1);
   }

   public interface AsyncQueryListener {
      void onQueryComplete(List var1);
   }

   public interface AsyncUpdateListener {
      void onUpdateComplete(int var1);
   }
}
