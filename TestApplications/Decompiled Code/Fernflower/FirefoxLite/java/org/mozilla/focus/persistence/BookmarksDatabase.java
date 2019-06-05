package org.mozilla.focus.persistence;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

public abstract class BookmarksDatabase extends RoomDatabase {
   private static volatile BookmarksDatabase instance;

   public static BookmarksDatabase getInstance(Context var0) {
      if (instance == null) {
         synchronized(BookmarksDatabase.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (instance == null) {
                  instance = (BookmarksDatabase)Room.databaseBuilder(var0.getApplicationContext(), BookmarksDatabase.class, "bookmarks.db").build();
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return instance;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var13 = var10000;

            try {
               throw var13;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               continue;
            }
         }
      } else {
         return instance;
      }
   }

   public abstract BookmarkDao bookmarkDao();
}
