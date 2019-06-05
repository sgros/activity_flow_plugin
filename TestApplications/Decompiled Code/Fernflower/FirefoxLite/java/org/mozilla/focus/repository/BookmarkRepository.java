package org.mozilla.focus.repository;

import android.arch.lifecycle.LiveData;
import java.util.UUID;
import org.mozilla.focus.persistence.BookmarkModel;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.threadutils.ThreadUtils;

public class BookmarkRepository {
   private static volatile BookmarkRepository instance;
   private BookmarksDatabase bookmarksDatabase;

   private BookmarkRepository(BookmarksDatabase var1) {
      this.bookmarksDatabase = var1;
   }

   public static BookmarkRepository getInstance(BookmarksDatabase var0) {
      if (instance == null) {
         synchronized(BookmarkRepository.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (instance == null) {
                  BookmarkRepository var1 = new BookmarkRepository(var0);
                  instance = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return instance;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      } else {
         return instance;
      }
   }

   // $FF: synthetic method
   public static void lambda$addBookmark$0(BookmarkRepository var0, BookmarkModel var1) {
      var0.bookmarksDatabase.bookmarkDao().addBookmarks(var1);
   }

   // $FF: synthetic method
   public static void lambda$deleteBookmark$2(BookmarkRepository var0, BookmarkModel var1) {
      var0.bookmarksDatabase.bookmarkDao().deleteBookmark(var1);
   }

   // $FF: synthetic method
   public static void lambda$deleteBookmarksByUrl$3(BookmarkRepository var0, String var1) {
      var0.bookmarksDatabase.bookmarkDao().deleteBookmarksByUrl(var1);
   }

   // $FF: synthetic method
   public static void lambda$updateBookmark$1(BookmarkRepository var0, BookmarkModel var1) {
      var0.bookmarksDatabase.bookmarkDao().updateBookmark(var1);
   }

   public String addBookmark(String var1, String var2) {
      BookmarkModel var3 = new BookmarkModel(UUID.randomUUID().toString(), var1, var2);
      ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$BookmarkRepository$8WxFpO2wYbT03C6OwFTQylWhMog(this, var3)));
      return var3.getId();
   }

   public void deleteBookmark(BookmarkModel var1) {
      ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$BookmarkRepository$a9c7CNfBHih3SA3M26E9Sbc7Huk(this, var1)));
   }

   public void deleteBookmarksByUrl(String var1) {
      ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$BookmarkRepository$cfPzGBgt2X_MpJzTyuopwNpkN6Y(this, var1)));
   }

   public LiveData getBookmarkById(String var1) {
      return this.bookmarksDatabase.bookmarkDao().getBookmarkById(var1);
   }

   public LiveData getBookmarksByUrl(String var1) {
      return this.bookmarksDatabase.bookmarkDao().getBookmarksByUrl(var1);
   }

   public LiveData loadBookmarks() {
      return this.bookmarksDatabase.bookmarkDao().loadBookmarks();
   }

   public void updateBookmark(BookmarkModel var1) {
      ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$BookmarkRepository$yYrJZhrxaqhZq3J2ZPvJtIWwED0(this, var1)));
   }
}
